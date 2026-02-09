pipeline {
    agent any

    environment {
        VERSION = "${BUILD_ID}"
        SONAR_HOST_URL = "http://54.87.146.64:9000/"
    }

    tools {
        maven "Maven"
    }

    stages {

        stage('Maven Build') {
            steps {
                sh 'mvn clean verify -DskipTests'
            }
        }

        stage('Run Tests') {
            steps {
                sh 'mvn test'
            }
        }

        stage('SonarQube Analysis') {
            environment {
                SONAR_TOKEN = "squ_2bebcf1d78953aed06bcbbcf5fa678cf51c595a6"
            }
            steps {
                sh """
                    mvn org.jacoco:jacoco-maven-plugin:prepare-agent \
                        sonar:sonar \
                        -Dsonar.host.url=${SONAR_HOST_URL} \
                        -Dsonar.login=${SONAR_TOKEN}
                """
            }
        }

        stage('Check Code Coverage') {
            environment {
                SONAR_TOKEN = "squ_2bebcf1d78953aed06bcbbcf5fa678cf51c595a6"
            }
            steps {
                script {
                    def componentKey = "com.krishna:restaurant"
                    def coverageThreshold = 80.0

                    def response = sh(
                        script: """
                            curl -s -f -H "Authorization: Bearer ${SONAR_TOKEN}" \
                            "${SONAR_HOST_URL}api/measures/component?component=${componentKey}&metricKeys=coverage"
                        """,
                        returnStdout: true
                    ).trim()

                    if (!response) {
                        error "SonarQube API returned empty response"
                    }

                    def json
                    try {
                        json = readJSON(text: response)
                    } catch (e) {
                        error "Invalid JSON from SonarQube: ${response}"
                    }

                    if (!json?.component?.measures || json.component.measures.isEmpty()) {
                        error "Coverage metric not found in SonarQube response"
                    }

                    def coverage = json.component.measures[0].value.toDouble()

                    echo "✅ Code Coverage: ${coverage}%"

                    if (coverage < coverageThreshold) {
                        error "❌ Coverage ${coverage}% is below threshold ${coverageThreshold}%"
                    }
                }
            }
        }

        stage('Docker Build and Push') {
            environment {
                DOCKERHUB = credentials('DOCKER_HUB_CREDENTIAL')
            }
            steps {
                sh '''
                    echo "$DOCKERHUB_PSW" | docker login -u "$DOCKERHUB_USR" --password-stdin
                    docker build -t siddukrishna/restaurant-microservice:${VERSION} .
                    docker push siddukrishna/restaurant-microservice:${VERSION}
                '''
            }
        }


        stage('Update Image Tag in GitOps') {
          steps {
            sshagent(['git-ssh']) {
              sh '''
                rm -rf deployment
                git clone git@github.com:gokulakrishna3101999/deployment.git
                cd deployment

                git checkout master
                git pull origin master

                sed -i "s|image:.*|image: siddukrishna/restaurant-microservice:${VERSION}|" AWS/restaurant-manifest.yaml

                git add AWS/restaurant-manifest.yaml
                git commit -m "Update restaurant image to ${VERSION}" || echo "No changes to commit"
                git push origin master
              '''
            }
          }
        }

        stage('Cleanup Workspace') {
            steps {
                deleteDir()
            }
        }
    }
}
