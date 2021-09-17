pipeline {
    agent any
    parameters {
        string(name: "APP_NAME", defaultValue: "co-com-pragma-api-photos", description: "Nombre de nuestra aplicación")
        string(name: "APP_VERSION", defaultValue: "latest", description: "Versión de nuestra aplicación")
        string(name: "CONTAINER_NAME", defaultValue: "api-photos", description: "Nombre del contendor que guardará nuestra aplicación.")
        string(name: "APP_HOST_NAME", defaultValue: "api.privjaac.com", description: "DNS que apunta a nuestra aplicación.")
        string(name: "APP_HOST_PORT", defaultValue: "9903", description: "Puerto de arranque de nuestra aplicación.")
        string(name: "EUREKA_SERVER_HOST", defaultValue: "eureka.privjaac.com", description: "DNS que apunta a nuestro servidor eureka.")
        string(name: "DOCKER_HOST", defaultValue: "docker.privjaac.com", description: "DNS que apunta a nuestro servidor de registro de imágenes de docker.")
        string(name: "DOCKER_USER", defaultValue: "jaac.docker", description: "Usuario de acceso para nuestro servidor de imágenes de docker.")
        string(name: "DOCKER_PASS", defaultValue: "jaac.docker", description: "Clave de acceso para nuestro servidor de imágenes de docker.")
    }
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
                docker login docker.privjaac.com -u ${DOCKER_USER} -p ${DOCKER_PASS}
                '''
            }
        }
        stage('eliminar-contenedor-imagen') {
            steps {
                sh '''
                #!/bin/bash
                container_id=$(docker ps -aq --filter name=api-photos)
                if [! -z $container_id]
                then
                    docker-compose -f dc-${CONTAINER_NAME}.yml up down
                fi
                image_id=$(docker images -q name=docker.privjaac.com/pragma/${APP_NAME}:${APP_VERSION})
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
                docker-compose -f dc-${CONTAINER_NAME}.yml build
                docker-compose -f dc-${CONTAINER_NAME}.yml push
                '''
            }
        }
        stage('ejecutar-contenedor-imagen') {
            steps {
                sh '''
                docker-compose -f dc-${CONTAINER_NAME}.yml up -d
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
                sleep 20s; curl -m 10 -s --head --request GET ${APP_HOST_NAME}:${APP_HOST_PORT}/api/photos/actuator/health | grep 200
                '''
            }
        }
    }
}