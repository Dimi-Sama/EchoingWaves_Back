pipeline {
    agent any
    
    tools {
        maven 'Maven 3.9.6'
        jdk 'JDK 17'
    }
    
    stages {
        stage('Checkout') {
            steps {
                checkout scm
            }
        }
        
        stage('Build') {
            steps {
                sh 'mvn clean package -DskipTests -Dmaven.test.failure.ignore=true'
                sh 'ls -la target/'
            }
        }
        
        stage('Build Docker Image') {
            steps {
                sh 'docker build -t echo-waves:${BUILD_NUMBER} .'
                sh 'docker tag echo-waves:${BUILD_NUMBER} echo-waves:latest'
            }
        }
        
        stage('Deploy') {
            steps {
                sh '''
                docker-compose down || true
                
                docker-compose up -d
                
                docker-compose ps
                '''
            }
        }
        
        stage('Verify') {
            steps {
                sh '''
                sleep 20
                
                docker-compose logs app
                
                echo "Application déployée sur http://localhost:8081"
                '''
            }
        }
    }
} 