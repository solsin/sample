// src/solsin/Common.groovy
package solsin

def checkoutSCM(String branch) {
    def timeStamp = Calendar.getInstance().getTime().format('YYYYMMdd',TimeZone.getTimeZone('CST'))
    def VERSION_NUMBER = timeStamp+"."+currentBuild.number
    echo VERSION_NUMBER
    
	git branch: branch, credentialsId: 'glyde-codecommit-admin', url: 'https://git-codecommit.ap-northeast-2.amazonaws.com/v1/repos/glyde-mall-develop'
	sh "git tag -a ${VERSION_NUMBER} -m 'tagged by jenkins'"
    withCredentials([
        usernamePassword(credentialsId: 'glyde-codecommit-admin', usernameVariable: 'USERNAME', passwordVariable: 'PASSWORD')
    ]) {
        sh "git push https://${USERNAME}:${PASSWORD}@git-codecommit.ap-northeast-2.amazonaws.com/v1/repos/glyde-mall-develop --tags"
    }
}

return this