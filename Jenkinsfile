import org.jenkinsci.plugins.jvctg.config.ViolationConfig;
import se.bjurr.violations.lib.reports.Reporter;


class BuildEnv {
	String jobName;


	public void checkout() {
		stage name:"Checkout";    
  		checkout scm;

  		sh 'git rev-parse HEAD > status'
		  commit = readFile('status').trim()

		  // figure out the branch name
		  jobName = "${env.JOB_NAME}"
		  def idx = jobName.lastIndexOf('/');
		  branch = jobName.substring(idx+1);
		  // Un-remove the / to - conversion.
		  branch = branch.replace("-","/");
		  branch = branch.replace("%2F","/");


		  /* Doesn't work - not checked out on a branch
		  sh "git branch | sed -n '/\\* /s///p' > status"
		  def branch = readFile('status').trim()
		  */

		  theJob = jobName.replace("/", " ");


	}

	public String toString() {
		return "BuildEnv ${jobName}";
	}
}

BuildEnv env;

node {
  ws("experiment") {

  	buildEnv = new BuildEnv();

  	buildEnv.checkout();

  	System.out.println("Env: ${buildEnv}")
  	

  
  //echo "Build of ${env.GIT_COMMIT} on ${env.GIT_BRANCH}";
  echo "Build of ${env.JOB_NAME} #${env.BUILD_NUMBER} : ${commit} on ${branch}";

  echo "Is this a PR?";
  int pr = 0;
  if( branch.startsWith("PR/") ) {
  	pr = Integer.parseInt(branch.substring(3));

  	echo "This is PR ${pr}";

  }

  // build
	def mvnHome = tool 'maven';
  //sh "${mvnHome}/bin/mvn -B verify"
  env.MAVEN_OPTS="-Xmx2G";

/* Execute maven */
  sh "${mvnHome}/bin/mvn -c clean install";
  sh "${mvnHome}/bin/mvn findbugs:check";

  // report back
  
   def configs = [
        new ViolationConfig(Reporter.FINDBUGS, ".*/findbugs.*\\.xml\$")
        ];
    
    step([$class: 'ViolationsToGitHubRecorder', 
		repositoryOwner: 'AllocateSoftware',
    	repositoryName: 'experiment',
    	createSingleFileComments: true,
    	pullRequestId: "${pr}",
    violationConfigs: configs]);



  }

}



