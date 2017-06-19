node {

    stage('执行Maven测试及打包') {
        sh 'ls'
        dir('./cash-agent') {
            sh 'mvn -version'
            sh 'mvn  -Dmaven.test.failure.ignore clean package'
        }
    }
}
