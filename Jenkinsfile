pipeline {
    agent {
        docker {
            image 'jmesserli/openjdk-with-docker'
            args '-v /var/run/docker.sock:/var/run/docker.sock'
        }
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
            environment { DOCKER = credentials('docker-deploy') }

            steps {
                sh 'docker login -u "$DOCKER_USR" -p "$DOCKER_PSW" docker.pegnu.cloud:443'
                sh 'docker build -t docker.pegnu.cloud:443/svmeal:latest .'
                sh 'docker push docker.pegnu.cloud:443/svmeal:latest'

                script {
                    configFileProvider([
                            configFile(fileId: '3bfec3c0-2d29-4616-acb6-06d514491d6f', targetLocation: 'known_hosts')
                    ]) {}
                    sshagent(credentials: ['svmeal-deploy-ssh']) {
                        sh 'ssh -4 -v -o UserKnownHostsFile=known_hosts svmeal-deploy@helios.peg.nu -C "cd /opt/svmeal && ./pullRestart.sh"'
                    }
                }
            }
        }
    }

    post {
        always {
            deleteDir()
        }
    }
}
