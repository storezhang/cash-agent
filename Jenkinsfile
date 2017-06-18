node {

    stage ('检出代码') {
        checkout([$class: 'GitSCM', branches: [[name: '*/master']], doGenerateSubmoduleConfigurations: false, extensions: [], submoduleCfg: [], userRemoteConfigs: [[credentialsId: 'git-ruijc', url: 'https://git.ruijc.com/storezhang/cash-agent.git']]])
    }

    stage('执行Maven测试及打包') {
        sh 'ls'
        dir('./cash-agent') {
            sh 'mvn -version'
            sh 'mvn  -Dmaven.test.failure.ignore clean package'
        }
    }
}