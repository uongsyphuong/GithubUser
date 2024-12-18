#!
pipeline {
    agent {
        label 'ci-android'
    }
    options {
        buildDiscarder(logRotator(numToKeepStr: '5'))
    }
    stages {
        stage('Accept SDK Licenses') {
            steps {
                // sh 'yes | /usr/local/share/android-sdk/cmdline-tools/latest/bin/sdkmanager --update'
                sh 'yes | /usr/local/share/android-sdk/cmdline-tools/latest/bin/sdkmanager --licenses'
            }
        }
        stage('Clean') {
            steps {
                sh './gradlew clean'
            }
        }
        stage('Formatting check') {
            steps {
                sh './gradlew spotlessCheck'
            }
        }
        stage('Build Alpha') {

            steps {
                sh './gradlew build'
            }
        }

    post {
        always {
            cleanWs()
        }
    }
}

