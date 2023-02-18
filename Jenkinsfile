pipeline {
    // This is a pipeline using jenkins.
    agent any
    // What to run goes here
    environment {
        DOCKERHUB_CREDENTIALS = credentials('dockerhub-cred')
        branch = "${env.BRANCH_NAME}"
    }
    stages {
        stage('Build') {
            when {
                allOf {
                    anyOf {
                        branch 'stg'; branch 'test'; branch 'prod'
                    }
                }
            }
            steps {
                script {
                    stage("${branch}-build-backend-service") {
                            sh 'docker build -t suraj362/url-shortner-backend-service:${BUILD_ID} -t suraj362/url-shortner-backend-service:latest ./backend-service'
                            sh 'echo $DOCKERHUB_CREDENTIALS_PSW | docker login -u $DOCKERHUB_CREDENTIALS_USR --password-stdin'
                            sh 'docker push suraj362/url-shortner-backend-service --all-tags'
                            sh 'docker rmi -f $(docker images "suraj362/url-shortner-backend-service" -a -q)'
                    }
                    stage("${branch}-build-redirect-service") {
                            sh 'docker build -t suraj362/url-shortner-redirect-service:${BUILD_ID} -t suraj362/url-shortner-redirect-service:latest ./redirect-service'
                            sh 'echo $DOCKERHUB_CREDENTIALS_PSW | docker login -u $DOCKERHUB_CREDENTIALS_USR --password-stdin'
                            sh 'docker push suraj362/url-shortner-redirect-service --all-tags'
                            sh 'docker rmi -f $(docker images "suraj362/url-shortner-redirect-service" -a -q)'
                    }
                }
            }
        }

        stage('Deploy') {
            when {
                allOf {
                    anyOf {
                        branch 'stg'; branch 'test'; branch 'prod'
                    }
                }
            }
            steps {
                script {
                    stage("${branch}-deploy") {
                        node("${branch}-server") {
                            checkout scm
                            sh 'docker-compose up -d'
                        }
                    }
                }
            }
        }
    }
}
