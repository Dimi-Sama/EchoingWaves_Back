pipeline {
    agent any
    
    tools {
        maven 'Maven'
        jdk 'JDK17'
    }
    
    stages {
        stage('Checkout') {
            steps {
                checkout scm
            }
        }
        
        stage('Build') {
            steps {
                sh 'mvn clean package -DskipTests'
            }
        }
        
        stage('Test') {
            steps {
                sh 'mvn test'
            }
        }
        
        stage('Build Docker Image') {
            steps {
                sh 'docker build -t echo-waves:${BUILD_NUMBER} .'
            }
        }
        
        stage('Deploy') {
            steps {
                sh '''
                docker stop echo-waves || true
                docker rm echo-waves || true
                docker run -d -p 8080:8080 --name echo-waves echo-waves:${BUILD_NUMBER}
                '''
            }
        }
    }
} 