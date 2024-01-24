pipeline {
    agent any

    environment {
        ARTIFACTORY_PASSWORD = credentials('artifactory_password')
        }

    stages {

    /* ---------------------------------------------- */
    /*              Jenkins Agent Pipeline            */
    /* ---------------------------------------------- */
         stage('Initialize') {
            agent {label 'jenkins-agent'}
                steps {
                    sh 'java --version'
                    sh 'mvn --version'
                    sh 'jf --version'
                }
        }


        stage('Build and Test') {
            agent {label 'jenkins-agent'}
                steps {
                    dir("backend") {
                        sh 'mvn clean install'
                    }
                    dir("cli") {
                        sh 'mvn clean install'
                    }
                    dir("bank") {
                        sh 'npm install'
                    }
                    dir("iswypls") {
                        sh 'npm install'
                    }
                }
        }

         stage('Deploy Maven to Artifactory') {
            agent {label 'jenkins-agent'}
                when {
                    branch 'main'
                }
                steps {
                    dir("backend") {
                        sh 'mvn -s ../mvn-settings.xml clean deploy -DupdateReleaseInfo=true'
                    }
                    dir("cli") {
                        sh 'mvn -s ../mvn-settings.xml clean deploy -DupdateReleaseInfo=true'
                    }
                }
        }

         stage('Deploy NodeJS to Artifactory') {
            agent {label 'vm-agent'}
                when {
                    branch 'main'
                }
                steps {
                    dir("bank") {
                        sh 'npm pack'
                        sh 'jf rt upload bank-0.0.1.tgz teama-libs-release-local/isa/devops/A/bank/2.0/'
                    }
                    dir("iswypls") {
                        sh 'npm pack'
                        sh 'jf rt upload iswypls-0.0.1.tgz teama-libs-release-local/isa/devops/A/iswypls/2.0/'
                    }
                }
        }
    /* ---------------------------------------------- */
    /*              Virtual Machine Pipeline          */
    /* ---------------------------------------------- */

        stage('Retrieve artifacts') {
            agent {label 'vm-agent'}
                when {
                    branch 'main'
                }
                steps {
                    dir("backend") {
                        sh 'curl -u admin:$ARTIFACTORY_PASSWORD -O "http://134.59.213.131:8081/artifactory/teama-libs-release-local/isa/devops/A/backend/2.0/backend-2.0.jar"'
                    }
                    dir("cli") {
                        sh 'curl -u admin:$ARTIFACTORY_PASSWORD -O "http://134.59.213.131:8081/artifactory/teama-libs-release-local/isa/devops/A/cli/2.0/cli-2.0.jar"'
                    }
                    dir("bank") {
                        sh 'curl -u admin:$ARTIFACTORY_PASSWORD -O "http://134.59.213.131:8081/artifactory/teama-libs-release-local/isa/devops/A/bank/2.0/bank-0.0.1.tgz"'
                    }
                    dir("iswypls") {
                        sh 'curl -u admin:$ARTIFACTORY_PASSWORD -O "http://134.59.213.131:8081/artifactory/teama-libs-release-local/isa/devops/A/iswypls/2.0/iswypls-0.0.1.tgz"'
                    }
                }
        }

        stage('Build Docker image') {
            agent {label 'vm-agent'}
                when {
                    branch 'main'
                }
                steps {
                    dir("backend") {
                        sh 'docker build --build-arg JAR_FILE=backend-2.0.jar -t devopsteama/backend .'
                    }
                    dir("cli") {
                        sh 'docker build --build-arg JAR_FILE=cli-2.0.jar -t devopsteama/cli .'
                    }
                    dir("bank") {
                        sh 'docker build -t devopsteama/bank .'
                    }
                    dir("iswypls") {
                        sh 'docker build -t devopsteama/iswypls .'
                    }
                }
        }

        stage ('Tag') {
            agent {label 'vm-agent'}
                when {
                    branch 'main'
                }
                steps {
                    dir("backend") {
                        sh 'docker tag devopsteama/backend:latest devopsteama/backend:2.0'
                    }
                    dir("cli") {
                        sh 'docker tag devopsteama/cli:latest devopsteama/cli:2.0'
                    }
                    dir("bank") {
                        sh 'docker tag devopsteama/bank:latest devopsteama/bank:2.0'
                    }
                    dir("iswypls") {
                        sh 'docker tag devopsteama/iswypls:latest devopsteama/iswypls:2.0'
                    }
                }
        }

        stage ('Push') {
            agent {label 'vm-agent'}
                when {
                    branch 'main'
                }
                steps {
                    dir("backend") {
                       sh 'docker push devopsteama/backend:2.0'
                    }
                    dir("cli") {
                       sh 'docker push devopsteama/cli:2.0'
                    }
                    dir("bank") {
                       sh 'docker push devopsteama/bank:2.0'
                    }
                    dir("iswypls") {
                       sh 'docker push devopsteama/iswypls:2.0'
                    }
                }
        }
    }
}



