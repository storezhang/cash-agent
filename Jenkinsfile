node {

    def DOCKER_REGISTRY = "storezhang"
    def DOCKER_IMAGE_NAME = JOB_NAME
    def SERVER_HOST = "";
    def SERVER_PORT = "";

    try {
        stage("拉取代码") {
            checkout([$class: 'GitSCM', branches: [[name: '*/master']], doGenerateSubmoduleConfigurations: false, extensions: [], submoduleCfg: [], userRemoteConfigs: [[credentialsId: 'storezhang-common-old', url: 'https://git.ruijc.com/storezhang/cash-agent.git']]])
        }

        stage("执行Maven编译") {
            sh "mvn -version"
            sh "mvn -Dmaven.test.failure.ignore clean package"
        }

        stage("打包Docker镜像") {
            timeout(600) {
                sh "docker build -t ${DOCKER_IMAGE_NAME} ."
            }

            withCredentials([usernamePassword(credentialsId: "storezhang-common-new", passwordVariable: "PASSWD", usernameVariable: "USERNAME")]) {
                sh "docker login - u = '$USERNAME' - p = '$PASSWD'"
                sh "docker push $DOCKER_REGISTRY/$DOCKER_IMAGE_NAME"
            }
        }

        stage ("部署到服务器") {
            withCredentials([usernamePassword(credentialsId: "storezhang-common-new", passwordVariable: "PASSWD", usernameVariable: "USERNAME")]) {
                sh "sshpass -p $PASSWD ssh $USERNAME@$SERVER_HOST -p $SERVER_PORT 'echo $PASSWD | docker stop cash-agent && echo $PASSWD | sudo docker run -d --restart=always --name=cash-agent'"
            }
        }
    } catch (exc) {
        emailext attachLog: true,
                to: "wanglin@digisky.com",
                body: JOB_NAME + " - 构建失败\u274C请检查\u2757\r\n\r\n" + "$DEFAULT_CONTENT" + "\r\n" + "logfile : ",
                recipientProviders: [[$class: "DevelopersRecipientProvider"]],
                subject: "$DEFAULT_SUBJECT" + " - FAILURE \u274C"
    }
}