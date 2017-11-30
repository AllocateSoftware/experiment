import org.jenkinsci.plugins.jvctg.config.ViolationConfig;
import se.bjurr.violations.lib.reports.Reporter;


class BuildEnv implements java.io.Serializable {
	String jobName;
	def commit;
	def branch;
	def theJob;

	public BuildEnv(env, commit) {
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

BuildEnv buildEnv;

node {
  ws("experiment") {

	stage name:"Checkout";    
		checkout scm;

		sh 'git rev-parse HEAD > status'
	  commit = readFile('status').trim()

	  buildEnv = new BuildEnv(env, commit);


       echo "Env: ${buildEnv}";
  	

  
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
  
  // report back

stage('Static code analysis') {
  //sh "${mvnHome}/bin/mvn package -DskipTests -Dmaven.test.failure.ignore=false -Dsurefire.skip=true -Dmaven.compile.fork=true -Dmaven.javadoc.skip=true"

  sh "${mvnHome}/bin/mvn findbugs:check";


  step([
   $class: 'ViolationsToGitHubRecorder', 
   config: [
    gitHubUrl: 'https://api.github.com/', 
    repositoryOwner: 'AllocateSoftware', 
    repositoryName: 'experiment', 
    pullRequestId: '${pr}', 
    useOAuth2Token: false, 
    useOAuth2TokenCredentials: true,
    oAuth2TokenCredentialsId: '2f56662a-5302-4cc6-9bd9-084abd43457d',
    oAuth2Token: '', 
    useUsernamePassword: true, 
    username: 'admin', 
    password: 'admin', 
    useUsernamePasswordCredentials: false, 
    usernamePasswordCredentialsId: '',
    createCommentWithAllSingleFileComments: false, 
    createSingleFileComments: true, 
    commentOnlyChangedContent: true, 
    minSeverity: 'INFO',
    keepOldComments: false,
    violationConfigs: [
     [ pattern: '.*/checkstyle-result\\.xml$', parser: 'CHECKSTYLE', reporter: 'Checkstyle' ], 
     [ pattern: '.*/findbugsXml\\.xml$', parser: 'FINDBUGS', reporter: 'Findbugs' ], 
     [ pattern: '.*/pmd\\.xml$', parser: 'PMD', reporter: 'PMD' ], 
    ]
   ]
  ])

  }

 }
}



