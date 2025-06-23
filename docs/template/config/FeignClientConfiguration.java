package com.f4.reel.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.f4.reel.client.api.*;
import feign.Feign;
import feign.Logger;
import feign.RequestInterceptor;
import feign.codec.Decoder;
import feign.codec.Encoder;
import feign.jackson.JacksonDecoder;
import feign.jackson.JacksonEncoder;
import feign.slf4j.Slf4jLogger;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.context.annotation.Lazy;

@Configuration
public class FeignClientConfiguration {

    private final LoadBalancerClient loadBalancerClient;

    public FeignClientConfiguration(LoadBalancerClient loadBalancerClient) {
        this.loadBalancerClient = loadBalancerClient;
    }

    @Bean
    public RequestInterceptor authRequestInterceptor() {
        return requestTemplate -> {
            var auth = SecurityContextHolder.getContext().getAuthentication();
            if (auth instanceof JwtAuthenticationToken jwt) {
                requestTemplate.header("Authorization", "Bearer " + jwt.getToken().getTokenValue());
            }
        };
    }

    @Bean
    public ObjectMapper feignObjectMapper() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        return mapper;
    }

    @Bean
    public Encoder feignEncoder(ObjectMapper objectMapper) {
        return new JacksonEncoder(objectMapper);
    }

    @Bean
    public Decoder feignDecoder(ObjectMapper objectMapper) {
        return new JacksonDecoder(objectMapper);
    }

    // === One bean per API client ===

    @Bean
    @Lazy
    public UserResourceApi userResourceApi(RequestInterceptor auth, Encoder enc, Decoder dec) {
        return feignFor("msuser", UserResourceApi.class, auth, enc, dec);
    }

    @Bean
    @Lazy
    public FeedItemResourceApi feedItemResourceApi(RequestInterceptor auth, Encoder enc, Decoder dec) {
        return feignFor("msfeed", FeedItemResourceApi.class, auth, enc, dec);
    }

    @Bean
    @Lazy
    public ReelResourceApi reelResourceApi(RequestInterceptor auth, Encoder enc, Decoder dec) {
        return feignFor("msreel", ReelResourceApi.class, auth, enc, dec);
    }

    @Bean
    @Lazy
    public NotificationResourceApi notificationResourceApi(RequestInterceptor auth, Encoder enc, Decoder dec) {
        return feignFor("msnotification", NotificationResourceApi.class, auth, enc, dec);
    }

    @Bean
    @Lazy
    public MsNotificationKafkaResourceApi kafkaNotificationApi(RequestInterceptor auth, Encoder enc, Decoder dec) {
        return feignFor("msnotification", MsNotificationKafkaResourceApi.class, auth, enc, dec);
    }

    @Bean
    @Lazy
    public KeycloakUserResourceApi keycloakUserApi(RequestInterceptor auth, Encoder enc, Decoder dec) {
        return feignFor("msuser", KeycloakUserResourceApi.class, auth, enc, dec);
    }

    // Add more beans as needed...

    // === Generic builder method ===
    private <T> T feignFor(String serviceName, Class<T> clazz, RequestInterceptor auth, Encoder enc, Decoder dec) {
        var instance = loadBalancerClient.choose(serviceName);
        if (instance == null) {
            throw new IllegalStateException("Service [" + serviceName + "] not found via discovery");
        }
        String baseUrl = instance.getUri().toString();
        return Feign.builder()
                .encoder(enc)
                .decoder(dec)
                .requestInterceptor(auth)
                .logger(new Slf4jLogger(clazz))
                .logLevel(Logger.Level.FULL)
                .target(clazz, baseUrl);
    }
}
