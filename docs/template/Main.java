package com.f4.gateway;

import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;
import com.mycompany.myapp.config.ApplicationProperties;
import com.mycompany.myapp.config.CRLFLogConverter;
import jakarta.annotation.PostConstruct;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.Collection;
import java.util.Optional;
import java.io.IOException;
import java.io.InputStream;
import java.io.BufferedReader;
import java.io.FileReader;
import java.nio.file.Path;
import java.nio.file.Paths;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.liquibase.LiquibaseProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.Environment;
import org.springframework.core.env.StandardEnvironment;
import org.springframework.core.io.ClassPathResource;
import tech.jhipster.config.DefaultProfileUtil;
import tech.jhipster.config.JHipsterConstants;
import org.yaml.snakeyaml.Yaml;

@SpringBootApplication
@EnableConfigurationProperties({ LiquibaseProperties.class, ApplicationProperties.class })
public class Main {

    private static final Logger LOG = LoggerFactory.getLogger(Main.class);
    private static Session sshSession;

    private final Environment env;

    public Main(Environment env) {
        this.env = env;
    }

    /**
     * Initializes apiGateway.
     * <p>
     * Spring profiles can be configured with a program argument
     * --spring.profiles.active=your-active-profile
     * <p>
     * You can find more information on how profiles work with JHipster on <a href=
     * "https://www.jhipster.tech/profiles/">https://www.jhipster.tech/profiles/</a>.
     */
    @PostConstruct
    public void initApplication() {
        Collection<String> activeProfiles = Arrays.asList(env.getActiveProfiles());
        if (activeProfiles.contains(JHipsterConstants.SPRING_PROFILE_DEVELOPMENT) &&
                activeProfiles.contains(JHipsterConstants.SPRING_PROFILE_PRODUCTION)) {
            LOG.error(
                    "You have misconfigured your application! It should not run "
                            + "with both the 'dev' and 'prod' profiles at the same time.");
        }
        if (activeProfiles.contains(JHipsterConstants.SPRING_PROFILE_DEVELOPMENT) &&
                activeProfiles.contains(JHipsterConstants.SPRING_PROFILE_CLOUD)) {
            LOG.error(
                    "You have misconfigured your application! It should not "
                            + "run with both the 'dev' and 'cloud' profiles at the same time.");
        }
    }

    /**
     * Setup SSH remote port forwarding using JSch.
     * This creates a tunnel from a remote server port to a local port.
     * 
     * @param remoteHost The remote host to connect to
     * @param remotePort The remote port to forward from
     * @param localPort  The local port to forward to
     * @param user       The SSH user for authentication
     * @param password
     */
    private static void setupSshPortForwarding(String remoteHost, int remotePort, int localPort, String user,
            String password) {
        try {
            JSch jsch = new JSch();
            sshSession = jsch.getSession(user, remoteHost, 22);

            // Configure the session
            java.util.Properties config = new java.util.Properties();
            config.put("StrictHostKeyChecking", "no");
            sshSession.setConfig(config);

            // Set up password authentication (you might want to use key-based auth instead)
            sshSession.setPassword(password);

            LOG.info("Connecting to SSH server...");
            sshSession.connect();

            // Set up port forwarding
            sshSession.setPortForwardingR(remotePort, "localhost", localPort);
            LOG.info("SSH port forwarding established: remote port {} -> local port {}", remotePort, localPort);

        } catch (Exception e) {
            LOG.error("Error setting up SSH port forwarding", e);
            if (sshSession != null && sshSession.isConnected()) {
                sshSession.disconnect();
            }
        }
    }

    /**
     * Clean up SSH session when the application shuts down
     */
    @PostConstruct
    public void cleanup() {
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            if (sshSession != null && sshSession.isConnected()) {
                sshSession.disconnect();
                LOG.info("SSH session disconnected");
            }
        }));
    }

    private static void logApplicationStartup(Environment env) {
        String protocol = Optional.ofNullable(env.getProperty("server.ssl.key-store")).map(key -> "https")
                .orElse("http");
        String applicationName = env.getProperty("spring.application.name");
        String serverPort = env.getProperty("server.port");
        String contextPath = Optional.ofNullable(env.getProperty("server.servlet.context-path"))
                .filter(StringUtils::isNotBlank)
                .orElse("/");
        String hostAddress = "localhost";
        try {
            hostAddress = InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException e) {
            LOG.warn("The host name could not be determined, using `localhost` as fallback");
        }
        LOG.info(
                CRLFLogConverter.CRLF_SAFE_MARKER,
                """

                        ----------------------------------------------------------
                        \tApplication '{}' is running! Access URLs:
                        \tLocal: \t\t{}://localhost:{}{}
                        \tExternal: \t{}://{}:{}{}
                        \tProfile(s): \t{}
                        ----------------------------------------------------------""",
                applicationName,
                protocol,
                serverPort,
                contextPath,
                protocol,
                hostAddress,
                serverPort,
                contextPath,
                env.getActiveProfiles().length == 0 ? env.getDefaultProfiles() : env.getActiveProfiles());

        String configServerStatus = env.getProperty("configserver.status");
        if (configServerStatus == null) {
            configServerStatus = "Not found or not setup for this application";
        }
        LOG.info(
                CRLFLogConverter.CRLF_SAFE_MARKER,
                "\n----------------------------------------------------------\n\t" +
                        "Config Server: \t{}\n----------------------------------------------------------",
                configServerStatus);
    }

    /**
     * Reads the server port from application-dev.yml file.
     * 
     * @return The server port from the configuration file, or 8800 if not found or
     *         error
     */
    private static int readServerPortFromConfig() {
        int defaultPort = 8000;
        try (InputStream inputStream = new ClassPathResource("config/application-dev.yml").getInputStream()) {
            Yaml yaml = new Yaml();
            Object data = yaml.load(inputStream);
            if (data instanceof java.util.Map) {
                @SuppressWarnings("unchecked")
                java.util.Map<String, Object> map = (java.util.Map<String, Object>) data;
                if (map.containsKey("server") && map.get("server") instanceof java.util.Map) {
                    @SuppressWarnings("unchecked")
                    java.util.Map<String, Object> serverConfig = (java.util.Map<String, Object>) map.get("server");
                    if (serverConfig.containsKey("port")) {
                        return Integer.parseInt(serverConfig.get("port").toString());
                    }
                }
            }
        } catch (IOException e) {
            LOG.warn("Could not load application-dev.yml, using default port {}", defaultPort);
        } catch (NumberFormatException e) {
            LOG.warn("Invalid port number in application-dev.yml, using default port {}", defaultPort);
        }
        return defaultPort;
    }

    /**
     * Reads the SSH password from the .env file at the root directory
     * 
     * @return The SSH password from the .env file, or null if not found
     */
    private static String readSshPasswordFromEnv() {
        // First try to look in the project root directory
        Path projectDir = Paths.get(System.getProperty("user.dir"));
        Path envPath = projectDir.resolve(".env");

        // If not found, try to look in the parent directory (AppF4)
        if (!envPath.toFile().exists()) {
            // Check if we're in a subdirectory of AppF4
            String dirName = projectDir.getFileName().toString();
            if (projectDir.getParent() != null &&
                    projectDir.getParent().getFileName() != null &&
                    "AppF4".equals(projectDir.getParent().getFileName().toString())) {
                envPath = projectDir.getParent().resolve(".env");
            } else if (projectDir.getParent() != null &&
                    projectDir.getParent().getParent() != null &&
                    projectDir.getParent().getParent().getFileName() != null &&
                    "AppF4".equals(projectDir.getParent().getParent().getFileName().toString())) {
                // We might be in a deeper subdirectory like AppF4/backend/ms_reel
                envPath = projectDir.getParent().getParent().resolve(".env");
            }
        }

        LOG.info("Looking for .env file at: {}", envPath.toAbsolutePath());

        if (!envPath.toFile().exists()) {
            LOG.warn("Could not find .env file at {}", envPath.toAbsolutePath());
            return null;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(envPath.toFile()))) {
            String line;
            while ((line = reader.readLine()) != null) {
                // Skip comment lines or empty lines
                if (line.trim().startsWith("//") || line.trim().isEmpty()) {
                    continue;
                }
                if (line.startsWith("SSH_PASSWORD=")) {
                    String password = line.substring("SSH_PASSWORD=".length());
                    LOG.info("Successfully read SSH password from .env file");
                    return password;
                }
            }
            LOG.warn("SSH_PASSWORD entry not found in .env file");
        } catch (IOException e) {
            LOG.error("Could not read .env file at {}", envPath, e);
        }
        return null;
    }

    /**
     * Main method, used to run the application.
     *
     * @param args the command line arguments.
     */
    public static void main(String[] args) {
        // Check if running in production profile (either from args or env)
        boolean isProdProfile = Arrays.asList(args).contains("--spring.profiles.active=prod") ||
                "prod".equals(System.getenv("SPRING_PROFILES_ACTIVE")) ||
                "prod".equals(System.getProperty("spring.profiles.active"));

        if (isProdProfile) {
            LOG.info("Running in production profile - skipping dev configuration");
            SpringApplication app = new SpringApplication(Main.class);
            DefaultProfileUtil.addDefaultProfile(app);
            Environment env = app.run(args).getEnvironment();
            logApplicationStartup(env);
            return;
        }

        // Initial Spring setup for dev environment
        SpringApplication app = new SpringApplication(Main.class);
        DefaultProfileUtil.addDefaultProfile(app);

        // Load additional configuration for dev environment
        app.setAdditionalProfiles("dev");
        System.setProperty("spring.config.additional-location", "classpath:/config/consul-config-dev.yml");

        // Get the environment before running the application
        ConfigurableEnvironment environment = new StandardEnvironment();
        environment.setActiveProfiles("dev");

        // Read server port from configuration
        int localPort = readServerPortFromConfig();
        int remotePort = localPort + 1000;

        // Set ports in environment before application start
        System.setProperty("ssh.local.port", String.valueOf(localPort));
        System.setProperty("ssh.remote.port", String.valueOf(remotePort));
        System.setProperty("spring.cloud.consul.discovery.port", String.valueOf(remotePort));

        // Now start the application with all the configuration from YML
        Environment env = app.run(args).getEnvironment();

        // After Spring environment is loaded, get the configuration values
        String remoteHost = env.getProperty("ssh.remote.host");
        String user = env.getProperty("ssh.user");
        boolean enableSshForwarding = Boolean.parseBoolean(env.getProperty("ssh.forwarding.enabled"));

        // Read SSH password from .env file
        String sshPassword = readSshPasswordFromEnv();

        // Check if SSH forwarding is enabled but password is missing
        if (enableSshForwarding && (sshPassword == null || sshPassword.trim().isEmpty())) {
            LOG.error("SSH forwarding is enabled but SSH_PASSWORD is missing in .env file");
            LOG.error("Application will now exit. Please create a .env file with SSH_PASSWORD at project root");

            // Give time for logs to be written
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }

            // Exit with error code
            System.exit(1);
        }

        // Set up SSH tunnel after application startup if enabled
        if (enableSshForwarding) {
            LOG.info("Establishing SSH tunnel after application startup...");
            setupSshPortForwarding(remoteHost, remotePort, localPort, user, sshPassword);
            // Give the SSH connection some time to establish
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }

        logApplicationStartup(env);
    }

}
