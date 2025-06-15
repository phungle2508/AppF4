// Jenkinsfile (Final Corrected Version)

pipeline {
    agent any

    environment {
        // --- CHANGE THESE VALUES ---
        VPS_HOST = '152.42.195.205'
        REMOTE_SCRIPT_PATH = '/root/f4-microserices-vps-configuration/AppF4/deploy_service.sh'
    }

    stages {
        stage('1. Detect Submodule Changes') {
            steps {
                script {
                    echo "Checking for new commits..."

                    checkout([
                        $class: 'GitSCM',
                        branches: [[name: 'origin/main']],
                        userRemoteConfigs: [[url: 'https://github.com/shegga9x/AppF4.git']]
                    ])

                    def changedFiles = getChangedFiles()
                    echo "Changed files: ${changedFiles.join(', ')}"

                    def changedServices = findChangedServices(changedFiles)

                    if (changedServices.isEmpty()) {
                        echo "No changes detected in 'backend' submodules. Skipping deployment."
                        currentBuild.result = 'NOT_BUILT'
                        return
                    }

                    echo "Detected changes in the following services: ${changedServices.join(', ')}"
                    env.CHANGED_SERVICES = changedServices.join(',')
                }
            }
        }

        stage('2. Trigger Deployment on VPS') {
            when {
                expression { return env.CHANGED_SERVICES != null && !env.CHANGED_SERVICES.isEmpty() }
            }
            steps {
                script {
                    def servicesToDeploy = env.CHANGED_SERVICES.split(',')
                    
                    def remote = [ name: 'vps-server', host: env.VPS_HOST, user: 'root', allowAnyHosts: true ]

                    for (String service : servicesToDeploy) {
                        echo "--- Triggering deployment for service: ${service} ---"
                        sshScript remote: remote, credentialsId: 'vps-password-credentials', script: "bash ${env.REMOTE_SCRIPT_PATH} ${service}"
                    }
                }
            }
        }
    }

    post { always { echo 'Pipeline finished.' } }
}

// --- HELPER FUNCTIONS ---
def getChangedFiles() {
    def changedFiles = []
    // This part is correct
    def diff = sh(script: "git diff-tree --no-commit-id --name-only -r HEAD", returnStdout: true).trim()
    if (diff) {
        changedFiles.addAll(diff.split('\n'))
    }
    return changedFiles
}

def findChangedServices(List filePaths) {
    def services = new HashSet<String>()
    filePaths.each { path ->
        //
        // ######################################################
        // ## THIS REGEX HAS BEEN CORRECTED WITH A '?'         ##
        // ######################################################
        def matcher = (path =~ /^backend\/([^\/]+)\/?/)
        //
        if (matcher.find()) {
            services.add(matcher.group(1))
        }
    }
    return services
}
