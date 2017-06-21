node {

    echo "$TAG_NAME"
    def TEMP = JOB_NAME.getAt(0..(JOB_NAME.length() - 7))
    def IMAGE_NAME = TEMP.substring(TEMP.lastIndexOf("/") + 1)
    def WORK_PATH = IMAGE_NAME

    try {
        stage("拉取代码") {
            deleteDir()
            withCredentials([[$class: "UsernamePasswordMultiBinding", credentialsId: "storezhang-common-new", usernameVariable: "USERNAME", passwordVariable: "PASSWORD"]]) {
                sh "git clone https://"${USERNAME}":"${PASSWORD}"@git.ruijc.com/storezhang/cash-agent.git"
            }
        }
        stage("执行Maven编译") {
            dir("${WORK_PATH}") {
                sh "mvn -version"
                sh "mvn -Dmaven.test.failure.ignore clean package"
            }
        }
        stage("生成测试报告") {
            dir("${WORK_PATH}") {
                junit "target/surefire-reports/*.xml"
            }
        }

        stage("打包Docker镜像") {
            dir("${WORK_PATH}/dskyapp") {
                withDockerRegistry([credentialsId: "hub-docker", url: "https://hub-docker.ppgame.com"]) {
                    timeout(600) {
                        sh "docker build --rm -t ${IMAGE_NAME} ."
                    }
                    try {
                        if (fileExists("tag.txt")) {
                            def IMAGE_TAG = readFile "tag.txt"
                            sh "docker tag ${IMAGE_NAME} hub-docker.ppgame.com/sgz2/${IMAGE_NAME}:${IMAGE_TAG}"
                            sh "docker push hub-docker.ppgame.com/sgz2/${IMAGE_NAME}:${IMAGE_TAG}"
                        }
                    } finally {
                        sh "docker tag ${IMAGE_NAME} hub-docker.ppgame.com/sgz2/${IMAGE_NAME}:latest"
                        sh "docker push hub-docker.ppgame.com/sgz2/${IMAGE_NAME}:latest"
                    }
                }
            }
        }

        stage("发送电子邮件") {
            emailext attachLog: true,
                    to: "wanglin@digisky.com",
                    body: JOB_NAME + " - 构建成功\u2705\u2728\r\n\r\n" + "镜像hub-docker.ppgame.com/sgz2/${IMAGE_NAME} 已推送" + "\r\n\r\n" + "$DEFAULT_CONTENT" + "\r\n" + "logfile : ",
                    recipientProviders: [[$class: "DevelopersRecipientProvider"]],
                    subject: "$DEFAULT_SUBJECT" + " - SUCCESS \u2705"
        }
    } catch (exc) {
        emailext attachLog: true,
                to: "wanglin@digisky.com",
                body: JOB_NAME + " - 构建失败\u274C请检查\u2757\r\n\r\n" + "$DEFAULT_CONTENT" + "\r\n" + "logfile : ",
                recipientProviders: [[$class: "DevelopersRecipientProvider"]],
                subject: "$DEFAULT_SUBJECT" + " - FAILURE \u274C"
    }
}