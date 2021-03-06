package realtime.buildEnv;

import groovy.transform.Field

@Field
String commit;

@Field
String branch;

@Field
def theJob;

@Field
String repoName;


def call() {
	
  sh 'git rev-parse HEAD > status'
  commit = readFile('status').trim()

  // env.JOB_NAME will look a bit like magnayn/RealTime/dev%2Fmain

  // figure out the branch name
  def jobName = "${env.JOB_NAME}"
  def idx = jobName.lastIndexOf('/');
  branch = jobName.substring(idx+1);
  // Un-remove the / to - conversion.
  branch = branch.replace("-","/");
  branch = branch.replace("%2F","/");

  idx = jobName.indexOf('/');
  repoName = jobName.substring(0,idx);


  /* Doesn't work - not checked out on a branch
  sh "git branch | sed -n '/\\* /s///p' > status"
  def branch = readFile('status').trim()
  */

  theJob = jobName.replace("/", " ");
  
  echo "Build of ${env.JOB_NAME} #${env.BUILD_NUMBER} : ${commit} on ${branch} @ ${repoName}";

  return this;

}

String tagBase(String prefix) {
  def tagProject = branch.replace("/","-");

  //According to https://github.com/docker/distribution/blob/master/reference/regexp.go upper case chars are not allowed...
  tagProject = tagProject.toLowerCase();

  String tagBase = "${prefix}-${tagProject}"
  String s = repoName.toLowerCase();

  // Add github name if it's not allocatesoftware
  if( !s.equals("allocatesoftware") ) {
    tagBase = "${s}-${tagBase}";
  }

  //replace any spaces with hyphen
  tagBase = tagBase.replace(" ", "-");
  return tagBase;
}

// Return the build tags for this project
String buildTags(String repository, String prefix) {

  def tagBase = tagBase(prefix);
  def thisTag       = "${repository}/${tagBase}:${env.BUILD_NUMBER}";
  def thisLatestTag = "${repository}/${tagBase}:latest";
  String tags       = "${thisLatestTag}\n${thisTag}";
  return tags;
}



boolean isPR() {  
  return( branch.startsWith("PR/") );      
}

return this;