import groovy.transform.Field

@Field
def buildEnv;

node {
  ws("experiment") {

 stage(name: "Checkout") {
 	 checkout scm;


  	 buildEnv = load 'jenkins/buildEnv.groovy';
     buildEnv();


	}
	
  int pr = 0;
  if( buildEnv.isPR() ) {
  	pr = Integer.parseInt(buildEnv.branch.substring(3));

  	echo "This is PR ${pr}";

  }

  // build
	def mvnHome = tool 'latest';
  //sh "${mvnHome}/bin/mvn -B verify"
  env.MAVEN_OPTS="-Xmx2G";

/* Execute maven */
  sh "${mvnHome}/bin/mvn -c clean install";
  
  // report back
   if( pr != 0 ) {
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
}



