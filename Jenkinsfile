def branchSuffix() {
    return env.BRANCH_NAME == 'master' ? '' : '-' + env.BRANCH_NAME.replaceAll(/[^0-9A-Za-z-]+/, '-')
}

def fullVersion() {
    return env.VERSION + '.' + env.BUILD_NUMBER + branchSuffix()
}

pipeline {
    agent {
        docker {
            image 'jmesserli/openjdk-with-docker'
            args '-v /var/run/docker.sock:/var/run/docker.sock'
        }
    }

    options {
        buildDiscarder(logRotator(numToKeepStr: '5'))
    }

    environment {
        VERSION = readFile('VERSION')
    }

    stages {
        stage('Prepare') {
            steps {
                sh 'chmod +x mvnw'
            }
        }

        stage('Build') {
            steps {
                sh './mvnw -DskipTests=true clean package'
            }

            post {
                success {
                    archiveArtifacts 'target/svmeal-api-*.jar'
                }
            }
        }

        stage('Test') {
            steps {
                sh './mvnw test'
            }

            post {
                always {
                    junit 'target/surefire-reports/*.xml'
                }
            }
        }

        stage('Docker & Deploy') {
            when { branch 'master' }
            environment {
                DOCKER = credentials('docker-deploy')
                OCTOPUS_API_KEY = credentials('octopus-deploy')
                FULL_VERSION = fullVersion()
            }

            steps {
                sh 'docker login -u "$DOCKER_USR" -p "$DOCKER_PSW" docker.pegnu.cloud:443'
                sh 'docker build -t docker.pegnu.cloud:443/svmeal:latest -t docker.pegnu.cloud:443/svmeal:$FULL_VERSION .'
                sh 'docker push docker.pegnu.cloud:443/svmeal:latest && docker push docker.pegnu.cloud:443/svmeal:$FULL_VERSION'

                sh '/opt/octo/Octo create-release --project "SV-Meal API" --version $FULL_VERSION --package svmeal:$FULL_VERSION --server https://deploy.pegnu.cloud --apiKey $OCTOPUS_API_KEY'
            }
        }
    }

    post {
        always {
            deleteDir()
        }
    }
}