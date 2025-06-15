// Jenkinsfile

pipeline {
    agent any

    environment {
        // --- CHANGE THESE VALUES ---
        VPS_HOST = 'your_vps_ip_address'
        // The path to your deployment script on the VPS
        REMOTE_SCRIPT_PATH = '/home/ubuntu/AppF4/deploy_service.sh'
    }

    stages {
        stage('1. Detect Submodule Changes') {
            steps {
                script {
                    echo "Checking for new commits..."
                    // Checkout the repository to inspect it.
                    // The 'submodule' option is crucial.
                    checkout([
                        $class: 'GitSCM',
                        branches: [[name: '*/master']], // Use 'main' if that's your default branch
                        userRemoteConfigs: [[
                            credentialsId: 'github-credentials', // Credential for private repo access
                            url: 'https://github.com/shegga9x/AppF4.git'
                        ]],
                        submoduleCfg: [] // This empty config ensures submodules are recognized
                    ])

                    // Get a list of changed files between the current and previous commit
                    def changedFiles = getChangedFiles()
                    echo "Changed files: ${changedFiles.join(', ')}"

                    // Find which unique services (submodules) were changed
                    def changedServices = findChangedServices(changedFiles)

                    if (changedServices.isEmpty()) {
                        echo "No changes detected in 'backend' submodules. Skipping deployment."
                        // Exit the pipeline gracefully
                        currentBuild.result = 'NOT_BUILT'
                        return
                    }

                    echo "Detected changes in the following services: ${changedServices.join(', ')}"
                    // Make the list of services available to the next stage
                    env.CHANGED_SERVICES = changedServices.join(',')
                }
            }
        }

        stage('2. Trigger Deployment on VPS') {
            // This stage only runs if the CHANGED_SERVICES variable is set
            when {
                environment name: 'CHANGED_SERVICES', value: '' , comparator: 'NOT_EQUALS'
            }
            steps {
                script {
                    // Convert the comma-separated string back to a list
                    def servicesToDeploy = env.CHANGED_SERVICES.split(',')
                    
                    // Loop through each changed service and run the remote script
                    for (String service : servicesToDeploy) {
                        echo "--- Triggering deployment for service: ${service} ---"
                        
                        // Use the SSH credentials stored in Jenkins
                        sshagent(credentials: ['vps-ssh-key']) {
                            sh """
                                ssh -o StrictHostKeyChecking=no ${env.VPS_USER}@${env.VPS_HOST} "bash ${env.REMOTE_SCRIPT_PATH} ${service}"
                            """
                        }
                    }
                }
            }
        }
    }

    post {
        always {
            echo 'Pipeline finished.'
        }
    }
}

// --- HELPER FUNCTIONS ---

// Returns a list of files changed in the latest push
def getChangedFiles() {
    def changedFiles = []
    // 'git diff-tree' is a reliable way to see what changed in the last commit
    def diff = sh(script: "git diff-tree --no-commit-id --name-only -r HEAD", returnStdout: true).trim()
    if (diff) {
        changedFiles.addAll(diff.split('\n'))
    }
    return changedFiles
}

// Analyzes the file paths and returns a unique set of service names
def findChangedServices(List filePaths) {
    def services = new HashSet<String>()
    filePaths.each { path ->
        // Check if the change was inside a backend submodule
        // Example path: "backend/user/src/main/java/com/example/App.java"
        def matcher = (path =~ /^backend\/([^\/]+)\//)
        if (matcher.find()) {
            services.add(matcher.group(1)) // Adds "user" to the set
        }
    }
    return services
}