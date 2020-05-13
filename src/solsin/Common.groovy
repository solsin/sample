// src/solsin/Common.groovy
package solsin

def checkoutSCM(String branch) {
    def timeStamp = Calendar.getInstance().getTime().format('YYYYMMdd',TimeZone.getTimeZone('CST'))
    def VERSION_NUMBER = timeStamp+"."+currentBuild.number
    echo VERSION_NUMBER
    
	git branch: branch, credentialsId: 'glyde-codecommit-admin', url: 'https://git-codecommit.ap-northeast-2.amazonaws.com/v1/repos/glyde-mall-develop'
	sh 'git tag -d ${VERSION_NUMBER}'
    withCredentials([
        usernamePassword(credentialsId: 'github', usernameVariable: 'GIT_USERNAME', passwordVariable: 'GIT_PASSWORD')
    ]) {
        sh 'git push origin ${VERSION_NUMBER}'
    }
}

return this