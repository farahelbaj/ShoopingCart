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
                script {
                    dockerImage = docker.build("${IMAGE_NAME}:${IMAGE_TAG}")
                }
            }
        }

        stage('Push Docker') {
            steps {
                script {
                    docker.withRegistry('', 'dockerhub-credentials') {
                        dockerImage.push("${IMAGE_TAG}")
                    }
                }
            }
        }
    }
}