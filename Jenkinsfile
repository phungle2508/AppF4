// Jenkinsfile using Password Authentication (Not Recommended)

pipeline {
    agent any

    environment {
        VPS_HOST = '152.42.195.205' // It is better to keep this here
        REMOTE_SCRIPT_PATH = '/home/ubuntu/AppF4/deploy_service.sh'
    }

    stages {
        // STAGE 1 REMAINS THE SAME...
        stage('1. Detect Submodule Changes') {
            steps {
                script {
                    // This entire script block is unchanged.
                    echo "Checking for new commits..."
                    checkout([$class: 'GitSCM', branches: [[name: '*/master']], userRemoteConfigs: [[credentialsId: 'github-credentials', url: 'https://github.com/shegga9x/AppF4.git']], submoduleCfg: []])
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

        // ##################################################################
        // ## THIS STAGE IS REPLACED TO USE PASSWORD AUTHENTICATION        ##
        // ##################################################################
        stage('2. Trigger Deployment on VPS') {
            when {
                expression { return env.CHANGED_SERVICES != null && !env.CHANGED_SERVICES.isEmpty() }
            }
            steps {
                script {
                    def servicesToDeploy = env.CHANGED_SERVICES.split(',')
                    
                    // Define the remote server connection details for the sshScript step
                    def remote = [
                        name: 'vps-server',
                        host: env.VPS_HOST,
                        user: 'root', // The username for the credential
                        allowAnyHosts: true // Bypasses the strict host key checking
                    ]

                    for (String service : servicesToDeploy) {
                        echo "--- Triggering deployment for service: ${service} ---"
                        
                        // Use the sshScript step with your password credential
                        // This is less secure than using sshagent with a key.
                        sshScript remote: remote, credentialsId: 'vps-password-credentials', script: "bash ${env.REMOTE_SCRIPT_PATH} ${service}"
                    }
                }
            }
        }
    }

    // post and helper functions remain the same...
    post { always { echo 'Pipeline finished.' } }
}
def getChangedFiles() { def changedFiles = []; def diff = sh(script: "git diff-tree --no-commit-id --name-only -r HEAD", returnStdout: true).trim(); if (diff) { changedFiles.addAll(diff.split('\n')) }; return changedFiles }
def findChangedServices(List filePaths) { def services = new HashSet<String>(); filePaths.each { path -> def matcher = (path =~ /^backend\/([^\/]+)\//); if (matcher.find()) { services.add(matcher.group(1)) } }; return services }
