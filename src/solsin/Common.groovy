// src/solsin/Common.groovy
import solsin

def checkoutSCM(String branch) {
	return {
        stage("checkout") {
            git branch: gitBranch, credentialsId: 'glyde-codecommit-admin', url: 'https://git-codecommit.ap-northeast-2.amazonaws.com/v1/repos/glyde-mall-develop'             
        }        
	}
}

return this