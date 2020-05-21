@Library('common-lib') import solsin.*

pipeline {
    agent any

    stages {
        stage('Build') {
            steps {
                echo 'Building..'
                echo "Selected TAG: ${GIT_TAG}"
                script {
                    def common = new Common()
                    //common.checkoutSCM('master', 'dev')
                }                
            }
        }
        stage('Test') {
            steps {
                echo 'Testing..'
            }
        }
        stage('Deploy') {
            steps {
                echo 'Deploying....'
            }
        }
    }
}