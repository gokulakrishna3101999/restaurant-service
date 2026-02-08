pipeline {
  agent any

  environment {
    DOCKERHUB_CREDENTIALS = credentials('DOCKER_HUB_CREDENTIAL')
    VERSION = "${env.BUILD_ID}"

  }

  tools {
    maven "Maven"
  }

  stages {

    stage('Maven Build'){
        steps{
        sh 'mvn clean package  -DskipTests'
        }
    }

     stage('Run Tests') {
      steps {
        sh 'mvn test'
      }
    }

    stage('SonarQube Analysis') {
  steps {
    sh 'mvn clean org.jacoco:jacoco-maven-plugin:prepare-agent install sonar:sonar -Dsonar.host.url=http://3.86.191.137:9000/ -Dsonar.login=squ_c9f63d5a8197e2dfda6e3b03d4a16c7d9ad36c67'
  }
}


   // stage('Check code coverage') {
   //          steps {
   //              script {
   //                  def token = "squ_c9f63d5a8197e2dfda6e3b03d4a16c7d9ad36c67"
   //                  def sonarQubeUrl = "http://3.86.191.137:9000/api"
   //                  def componentKey = "com.krishna:restaurant"
   //                  def coverageThreshold = 80.0

   //                  def response = sh (
   //                      script: "curl -H 'Authorization: Bearer ${token}' '${sonarQubeUrl}/measures/component?component=${componentKey}&metricKeys=coverage'",
   //                      returnStdout: true
   //                  ).trim()

   //                  def coverage = sh (
   //                      script: "echo '${response}' | jq -r '.component.measures[0].value'",
   //                      returnStdout: true
   //                  ).trim().toDouble()

   //                  echo "Coverage: ${coverage}"

   //                  if (coverage < coverageThreshold) {
   //                      error "Coverage is below the threshold of ${coverageThreshold}%. Aborting the pipeline."
   //                  }
   //              }
   //          }
   //      }


      stage('Docker Build and Push') {
      steps {
          sh 'echo $DOCKERHUB_CREDENTIALS_PSW | docker login -u $DOCKERHUB_CREDENTIALS_USR --password-stdin'
          sh 'docker build -t siddukrishna/restaurant-microservice:${VERSION} .'
          sh 'docker push siddukrishna/restaurant-microservice:${VERSION}'
      }
    }


     stage('Cleanup Workspace') {
      steps {
        deleteDir()

      }
    }

stage('Update Image Tag in GitOps') {
      steps {
         checkout scmGit(branches: [[name: '*/master']], extensions: [], userRemoteConfigs: [[ credentialsId: 'git-ssh', url: 'git@github.gokulakrishna3101999/deployment.git']])
        script {
       sh '''
          sed -i "s/image:.*/image: siddukrishna\\/restaurant-microservice:${VERSION}/" AWS/restaurant-manifest.yaml
        '''
          sh 'git checkout master'
          sh 'git add .'
          sh 'git commit -m "Update image tag"'
        sshagent(['git-ssh'])
            {
                  sh('git push')
            }
        }
      }
    }


  }
}
