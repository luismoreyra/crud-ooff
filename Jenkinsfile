node {
   
    def server
	def rtMaven
	def buildInfo
	def sonarqubeScannerHome


  EXECUTOR_NUMBER
echo "${env.EXECUTOR_NUMBER}"

	// Default settings 'stage'....
   stage name: "settings"

      sh "mkdir -p ./Apps/FFOOService/lib"
      sh "mkdir -p ./Apps/FFOOService/config"

   		sonarqubeScannerHome = tool name: 'SonarQubeDefault', type: 'hudson.plugins.sonar.SonarRunnerInstallation'

   		// Create an Artifactory server instance, as described above in this article:
   		server = Artifactory.server('ArtifactoryLocal')

    	// Create and set an Artifactory Maven Build instance:
    	rtMaven = Artifactory.newMavenBuild()

    	rtMaven.resolver server: server, releaseRepo: 'libs-release', snapshotRepo: 'libs-snapshot'
    	rtMaven.deployer server: server, releaseRepo: 'libs-release-local', snapshotRepo: 'libs-snapshot-local'
    	
    	// Optionally include or exclude artifacts to be deployed to Artifactory:
    	rtMaven.deployer.artifactDeploymentPatterns.addInclude("frog*").addExclude("*.zip")
    	// Set a Maven Tool defined in Jenkins "Manage":
    	rtMaven.tool = 'M3'
    	// Optionally set Maven Ops
    	rtMaven.opts = '-Xms1024m -Xmx4096m'
    	



   // Mark the code checkout 'stage'....
   stage name: "checkout"

   		checkout([$class: 'GitSCM', branches: [[name: '*/develop']], doGenerateSubmoduleConfigurations: false, extensions: [[$class: 'BuildChooserSetting', buildChooser: [$class: 'DefaultBuildChooser']]], submoduleCfg: [], userRemoteConfigs: [[credentialsId: 'bitbucket', url: 'https://mzegarra@bitbucket.org/ibkteam/ibk-digitalchannels-frequentoperations.git']]])

   		sh "git rev-parse HEAD > .git/commit-id"
        def commit_id = readFile('.git/commit-id').trim()
        echo "*************"
        echo commit_id
        echo "*************"
   
   
   	// Mark the code checkout 'stage'....
    stage name: "build"
   		// Run Maven:
    	buildInfo = rtMaven.run pom: 'digital-channel-ffoo/pom.xml', goals: 'clean test package org.jacoco:jacoco-maven-plugin:prepare-agent'
    	buildInfo.env.capture = true

    stage name: "publish"
      

     		parallel 'junit':{
  		    step([$class: 'JUnitResultArchiver', testResults: '**/target/surefire-reports/TEST-*.xml'])
  		}, 'jaCoCo':{
  		    step([$class: 'JacocoPublisher', execPattern:'**/target/**.exec', classPattern: 'build/classes/main', sourcePattern: 'src/main/java', exclusionPattern: '**/*Test.class'])
  		}, 'sonnar':{
  		    sh "'${sonarqubeScannerHome}/bin/sonar-scanner' -Dsonar.host.url=http://localhost:9000 -Dsonar.projectKey=pe.com.interbank.digitalchannel.ffoo:digital-channel-ffoo -Dsonar.projectName='${env.JOB_NAME}' -Dsonar.projectVersion=1.0.1-SNAPSHOT -Dsonar.projectBaseDir=./digital-channel-ffoo -Dsonar.sourceEncoding=UTF-8 -Dsonar.language=java -Dsonar.sources=src -Dsonar.modules=digital-channel-ffoo-integration,digital-channel-ffoo-model,digital-channel-ffoo-core,digital-channel-ffoo-persistence,digital-channel-ffoo-service,digital-channel-ffoo-web -Dsonar.junit.reportsPath=target/surefire-reports -Dsonar.surefire.reportsPath=target/surefire-reports -Dsonar.jacoco.itReportPath=target/failsafe-reports -Dsonar.dynamicAnalysis=reuseReports -Dsonar.java.coveragePlugin=jacoco -Dsonar.jacoco.reportPath=target/jacoco.exec -Dsonar.binaries=target/classes -Dsonar.java.binaries=target/classes"
  		}

    
    stage name: "publish artifactory"
   	
   		// Publish the build-info to Artifactory:
    	server.publishBuildInfo buildInfo
   	
    //stage name: "copy dependencies", concurrency: 1
       //rtMaven.opts = '-Xms1024m -Xmx4096m -DoutputDirectory=${env.WORKSPACE}/Apps/WallieService/lib'
       //rtMaven.run pom: 'mobile-microservice-wallie/pom.xml', goals: 'dependency:copy-dependencies'

       //sh "cd '${env.WORKSPACE}/Apps/WallieService/lib/'; rm -f *.war"

    //stage name: "copy configs", concurrency: 1    
        //checkout([$class: 'GitSCM', branches: [[name: '*/develop']], doGenerateSubmoduleConfigurations: false, 
        //extensions: [
        //  [$class: 'RelativeTargetDirectory', 
        //    relativeTargetDir: "${env.WORKSPACE}/Apps/WallieService/config"],[$class: 'BuildChooserSetting', buildChooser: [$class: 'DefaultBuildChooser']]], submoduleCfg: [], userRemoteConfigs: [[credentialsId: 'bitbucket', url: 'https://mzegarra@bitbucket.org/ibkteam/ibk-backend-configs.git']]])

        //sh "cp -R '${env.WORKSPACE}/Apps/WallieService/config/s227va6/WallieService/config/.' '${env.WORKSPACE}/Apps/WallieService/config'"

        //sh "rm -fr '${env.WORKSPACE}/Apps/WallieService/config/s227va6' '${env.WORKSPACE}/Apps/WallieService/config/s227va7' '${env.WORKSPACE}/Apps/WallieService/config/s227vp6' '${env.WORKSPACE}/Apps/WallieService/config/s227vp8' '${env.WORKSPACE}/Apps/WallieService/config/s227vp13' '${env.WORKSPACE}/Apps/WallieService/config/s227vp14' '${env.WORKSPACE}/Apps/WallieService/config/README.md'"
		
    stage name: "deploy DEV", concurrency: 1
   	
   		withCredentials([[$class: 'UsernamePasswordMultiBinding', credentialsId: 'consola_dmgr_dev',usernameVariable: 'user_consola_dmgr', passwordVariable: 'pwd_consola_dmgr']]){


     			sh "chmod 750 '${env.WORKSPACE}@script/deploy.sh'"

     			withEnv(["dmgr_host=${env.dmgr_host_dev}",
     			         "dmgr_port=${env.dmgr_port_dev}",
     			         "environment=${env.environment_dev}",
     			         "cell_name=${env.cell_name_dev}",
     			         "cluster_name=${env.cluster_name_dev}",
     			         "name_profile_dmgr=${env.name_profile_dmgr_dev}",
     			         "serverIHS=${env.serverIHS_dev}"]) {
  			    sh "'${env.WORKSPACE}@script/deploy.sh'"
  			}
   		}


   /* stage name: "deploy UAT", concurrency: 1

		try {
		    timeout(time: 1, unit: 'DAYS') {
		        input message: 'Do you want to UAT this build?',
		              parameters: [[$class: 'BooleanParameterDefinition',
		                            defaultValue: false,
		                            description: 'Ticking this box will do a release',
		                            name: 'UAT']]



		        withCredentials([[$class: 'UsernamePasswordMultiBinding', credentialsId: 'consola_dmgr_uat',usernameVariable: 'user_consola_dmgr', passwordVariable: 'pwd_consola_dmgr']]){


  		   			sh "chmod 750 '${env.WORKSPACE}@script/deploy.sh'"

  		   			withEnv(["dmgr_host=${env.dmgr_host_uat}",
  		   			         "dmgr_port=${env.dmgr_port_uat}",
  		   			         "environment=${env.environment_uat}",
  		   			         "cell_name=${env.cell_name_uat}",
  		   			         "cluster_name=${env.cluster_name_uat}",
  		   			         "name_profile_dmgr=${env.name_profile_dmgr_uat}",
  		   			         "serverIHS=${env.serverIHS_uat}"]) {
  					    sh "'${env.WORKSPACE}@script/deploy.sh'"
  					}
		   		}


		    }
		} catch (err) {
		    def user = err.getCauses()[0].getUser()
		    echo "Aborted by:\n ${user}"
		}*/



}



def version() {
  def matcher = readFile('digital-channel-ffoo/pom.xml') =~ '<version>(.+)</version>'
  matcher ? matcher[0][1] : null
}

