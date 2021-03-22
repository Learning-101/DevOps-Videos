<<>> --> replace with apporirate values 

node(<<nodetype>>){
   //Clean Workspace
   stage(<<stagename-Clean\Delete>>){
     cleanWs()
   }
   
   //pull/fetch the code from the Version Control System
   stage(<<stagename-VCS>>){
      //scm
	  //Syntax code
	  git branch: '<<branchName>>', credentialsId: '<<credentialsId>>', url: 'https://github.com/<<repopath>>'
	  //Sample Code
	  git branch: 'main', credentialsId: 'bc2f9526-78e5-4eab-8f9e-981de797d744', url: 'https://github.com/DevopsTrainingInfoOrg/SampleWebApplication.git'
	  //(or)
	  //checkout
	  //Syntax code
	  checkout([$class: '<<versionControl>>', branches: [[name: '*/<<branchName>>']], doGenerateSubmoduleConfigurations: false, extensions: [], submoduleCfg: [], userRemoteConfigs: [[credentialsId: '<<credentialsId>>', url: '<<VersionControlRepoURL>>']]])
	  //Sample Code
	  checkout([$class: 'GitSCM', branches: [[name: '*/main']], doGenerateSubmoduleConfigurations: false, extensions: [], submoduleCfg: [], userRemoteConfigs: [[credentialsId: 'mynawin2rana', url: 'https://github.com/DevopsTrainingInfoOrg/SampleWebApplication']]])
	 }
	 
	stage(<<stagename-build>>){
		
		  
				if(isUnix()){
				 withEnv(["PATH+JDK=${tool 'java1.8'}/bin","PATH+MAVEN_HOME=${tool 'MAVEN_HOME_1'}"]){
				   sh "mvn clean install"
				   }
				}
				else{
				   withEnv(["PATH+JDK=${tool 'java1.8'}/bin","PATH+MAVEN_HOME=${tool 'MAVEN_HOME_1'}"]){
					bat "mvn clean install"
					}
				}
		
	}
	
	
	stage(<<stagename-test>>){
		if(isUnix()){
		   sh "mvn test"
		}
		else{
			bat "mvn test"
		}
	}
	
	
	stage(<<stagename-QA>>){
		if(isUnix()){
		   sh "<<testscript start>>"
		}
		else{
			bat "<<testscript start>>"
		}
	}
	
	stage(<<stagename-Report>>){
		if(isUnix()){
		   sh "<<testscript start>>"
		}
		else{
			bat "<<testscript start>>"
		}
	}
	
	//Storing the binaries files / executable files into the artifacts repo's 
	stage(<<stagename-Artifacts>>){
	// jenkins job itself
	 archiveArtifacts artifacts: '**/target/*.jar', fingerprint: true 
	 //Thirdparty aritfacts  - jfrog artifactory/Nexus  
	 curl -u<<username>>:<<password/apikey>> -T  <<filepath>> "<<artifactory_baseurl>>/<<repourl>>/<<file>>"
	
		if(isUnix()){
		//jfrog artifactory
		 sh 'curl -uadmin:xxxxx -T  target/SampleExam.jar "http://localhost:8046/artifactory/mytest/2021/03/02/01/SampleExample-1.0.0.jar"'
		//nexus
		sh'curl -v -u admin:xxxx --upload-file target/SampleExam.jar http://localhost:8081/repository/target/SampleExam.jar'
		}
		else{
		 bat 'curl -uadmin:xxxx -T  target/SampleExam.jar "http://localhost:8046/artifactory/mytest/2021/03/02/01/SampleExample-1.0.0.jar"'
		 //nexus
		bat 'curl -v -u admin:xxxx --upload-file target/SampleExam.jar http://localhost:8081/repository/target/SampleExam.jar'
 
		}
	
	}
	
	post {
        always {
            echo 'One way or another, I have finished'
            deleteDir() /* clean up our workspace */
			cleanWs() /*Clean up our workspace*/
        }
        success {
            echo 'I succeeded!'
			 mail to: 'team@example.com',
             subject: "success Pipeline: ${currentBuild.fullDisplayName}",
             body: "Job success more information or log please check the url ${env.BUILD_URL}"
        }
        unstable {
            echo 'I am unstable :/'
			mail to: 'team@example.com',
             subject: "unstable Pipeline: ${currentBuild.fullDisplayName}",
             body: "Something is wrong with ${env.BUILD_URL}"
        }
        failure {
            echo 'I failed :('
			mail to: 'team@example.com',
             subject: "failed Pipeline: ${currentBuild.fullDisplayName}",
             body: "Something is wrong with ${env.BUILD_URL}"
        }
        changed {
            echo 'Things were different before...'
        }
    }
	
}
