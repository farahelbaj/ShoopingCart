pipeline {
    agent any

    environment {
        IMAGE_NAME = "yourdockerhubusername/shopping-cart"
        IMAGE_TAG = "latest"
    }

    tools {
        jdk 'jdk17'
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
                sh 'mvn clean compile'
            }
        }

        stage('Test') {
            steps {
                sh 'mvn test'
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