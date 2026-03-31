pipeline {
    agent any

    environment {
        IMAGE_NAME = "farahelba/farah-shopping-cart"
        IMAGE_TAG = "latest"
    }

    tools {
            jdk 'JDK21'
            maven 'maven3'
        }

        stages {
            stage('Checkout') {
                steps {
                    checkout scm
                }
            }

            stage('Build') {
                steps {
                    bat 'mvn clean compile'
                }
            }

            stage('Test') {
                steps {
                    bat 'mvn test'
                }
            }

            stage('Build Docker') {
                steps {
                    bat 'docker build -t %IMAGE_NAME%:%IMAGE_TAG% .'
                }
            }

            stage('Push Docker') {
                steps {
                    withCredentials([usernamePassword(credentialsId: 'dockerhub-credentials', usernameVariable: 'DOCKER_USER', passwordVariable: 'DOCKER_PASS')]) {
                        bat 'docker login -u %DOCKER_USER% -p %DOCKER_PASS%'
                        bat 'docker push %IMAGE_NAME%:%IMAGE_TAG%'
                    }
                }
            }
        }
    }