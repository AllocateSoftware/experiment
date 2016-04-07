
node('docker') {
  ws("experiment") {

  	 stage name:"Checkout";    
  checkout scm;

  sh 'git rev-parse HEAD > status'
  commit = readFile('status').trim()

  // figure out the branch name
  def jobName = "${env.JOB_NAME}"
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


  //echo "Build of ${env.GIT_COMMIT} on ${env.GIT_BRANCH}";
  echo "Build of ${env.JOB_NAME} #${env.BUILD_NUMBER} : ${commit} on ${branch}";

  echo "Is this a PR?";

  }

}