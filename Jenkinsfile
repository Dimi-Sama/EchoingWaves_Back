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
        
        stage('Install Docker Compose') {
            steps {
                sh '''
                # Vérifier si Docker Compose est déjà installé
                if ! command -v docker-compose &> /dev/null; then
                    echo "Installation de Docker Compose..."
                    curl -L "https://github.com/docker/compose/releases/download/v2.24.6/docker-compose-$(uname -s)-$(uname -m)" -o docker-compose
                    chmod +x docker-compose
                    sudo mv docker-compose /usr/local/bin/
                fi
                docker-compose --version
                '''
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
                # Puisque docker-compose n'est pas disponible, utilisons docker directement
                # Arrêter et supprimer les conteneurs existants
                docker stop echo-waves-app db-postgres || true
                docker rm echo-waves-app db-postgres || true
                
                # Créer un réseau
                docker network create echo-network || true
                
                # Démarrer PostgreSQL
                docker run -d --name db-postgres \
                  --network echo-network \
                  -p 5433:5432 \
                  -e POSTGRES_DB=EchoWaves \
                  -e POSTGRES_USER=Echo \
                  -e POSTGRES_PASSWORD=Waves1234 \
                  -v postgres-data:/var/lib/postgresql/data \
                  postgres:14
                
                # Attendre que PostgreSQL démarre
                sleep 10
                
                # Démarrer l'application
                docker run -d --name echo-waves-app \
                  --network echo-network \
                  -p 8081:8080 \
                  -e SPRING_DATASOURCE_URL=jdbc:postgresql://db-postgres:5432/EchoWaves \
                  -e SPRING_DATASOURCE_USERNAME=Echo \
                  -e SPRING_DATASOURCE_PASSWORD=Waves1234 \
                  -e SPRING_JPA_HIBERNATE_DDL_AUTO=update \
                  echo-waves:latest
                '''
            }
        }
        
        stage('Verify') {
            steps {
                sh '''
                # Attendre que l'application démarre
                sleep 20
                
                # Vérifier les logs
                docker logs echo-waves-app
                
                echo "Application déployée sur http://localhost:8081"
                '''
            }
        }
    }
} 