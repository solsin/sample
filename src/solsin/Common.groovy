// src/solsin/Common.groovy
package solsin

def checkoutSCM(String branch, jobName) {
    def timeStamp = Calendar.getInstance().getTime().format('YYYY/MM/dd/',TimeZone.getTimeZone('CST'))
    def VERSION_NUMBER = timeStamp+"/${jobName}/"+currentBuild.number
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