pipeline {
    agent any
    
    @Library('common-lib')

/**
    library identifier: 'common-lib@master', retriever: modernSCM(
        [$class: 'GitSCMSource',
        remote: 'https://github.com/solsin/jenkinsfile.git',
        credentialsId: 'github-solasin'])
*/
    stages {
        stage('Build') {
            steps {
                echo 'Building..'
                common.checkoutSCM("master")
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