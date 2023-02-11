pipeline {
    // Where to run stuff.
    agent any
    // What to run goes here
    environment {
        DOCKERHUB_CREDENTIALS = credentials('dockerhub-cred')
    }
    stages {
        stage('${BRANCH_NAME}-build-backend-service') {
                when {
                    allOf {
                        anyOf {
                        branch 'stg'; branch 'test'; branch 'prod'
                        }
                        changeset 'backend-service/**'
                    }
                }
            steps {
                sh 'docker build -t suraj362/url-shortner-backend-service:${BUILD_ID} -t suraj362/url-shortner-backend-service:latest ./backend-service'
                sh 'echo $DOCKERHUB_CREDENTIALS_PSW | docker login -u $DOCKERHUB_CREDENTIALS_USR --password-stdin'
                sh 'docker push suraj362/url-shortner-backend-service --all-tags'
            }
        }
        stage('${BRANCH_NAME}-build-redirect-service') {
            when {
                allOf {
                    anyOf {
                        branch 'stg'; branch 'test'; branch 'prod'
                    }
                    changeset 'redirect-service/**'
                }
            }
            steps {
                sh 'docker build -t suraj362/url-shortner-redirect-service:${BUILD_ID} -t suraj362/url-shortner-redirect-service:latest ./redirect-service'
                sh 'echo $DOCKERHUB_CREDENTIALS_PSW | docker login -u $DOCKERHUB_CREDENTIALS_USR --password-stdin'
                sh 'docker push suraj362/url-shortner-redirect-service --all-tags'
            }
        }
        stage('${BRANCH_NAME}-deploy') {
            when {
                allOf {
                    anyOf {
                        branch 'stg'; branch 'test'; branch 'prod'
                    }
                    anyOf {
                        changeset 'redirect-service/**'
                        changeset 'backend-service/**'
                        changeset 'docker-compose.yml'
                        changeset '.env'
                    }
                }
            }
            steps {
                sh 'docker-compose up -d'
            }
            agent {
                label '${BRANCH_NAME}-server'
            }
        }
    }
}
