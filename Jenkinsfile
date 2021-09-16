pipeline {
    agent any
    tools {
        jdk 'JAVA_11'
        maven 'M2_3_6_3'
    }
    stages {
        stage('sonar-qube') {
            steps {
                sh '''
                echo 'simular y verificar pruebas de codigo con sonar qube.'
                '''
            }
        }
        stage('compilar-java-test') {
            steps {
                sh '''
                echo 'realizar pruebas unitarias en esta segmento.'
                '''
            }
        }
        stage('compilar-java') {
            steps {
                sh '''
                mvn clean install -Dmaven.test.skip=true
                '''
            }
        }
        stage('login-docker') {
            steps {
                sh '''
                docker login docker.privjaac.com -u jaac.docker -p jaac.docker
                '''
            }
        }
        stage('eliminar-contenedor-imagen') {
            steps {
                sh '''
                #!/bin/bash
                container_id=$(docker ps -aq --filter name=api_photos)
                if [! -z $container_id]
                then
                    docker-compose -f dc-api-photos.yml up down
                fi
                image_id=$(docker images -q name=docker.privjaac.com/pragma/co-com-pragma-api-photos:latest)
                if [! -z $image_id]
                then
                    docker rmi $image_id
                fi
                '''
            }
        }
        stage('construir-subida-imagen') {
            steps {
                sh '''
                docker-compose -f dc-api-photos.yml build
                docker-compose -f dc-api-photos.yml push
                '''
            }
        }
        stage('ejecutar-contenedor-imagen') {
            steps {
                sh '''
                docker-compose -f dc-api-photos.yml up -d
                '''
            }
        }
        stage('logout-docker') {
            steps {
                sh '''
                docker logout
                '''
            }
        }
        stage('disponibilidad-contenedor') {
            steps {
                sh '''
                sleep 20s; curl -m 10 -s --head --request GET api.privjaac.com:9903/api/photos/actuator/health | grep 200
                '''
            }
        }
    }
}