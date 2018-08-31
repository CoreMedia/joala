import hudson.model.*
import hudson.plugins.git.*
import jenkins.model.*

@SuppressWarnings("GroovyAssignabilityCheck")
static def addWorkflowJob(parent, branchName) {
  def branchSpec = "*/${branchName}"
  def scm = new GitSCM("https://github.com/CoreMedia/joala.git")
  def flowDefinition = new org.jenkinsci.plugins.workflow.cps.CpsScmFlowDefinition(scm, "Jenkinsfile")
  scm.branches = [new BranchSpec(branchSpec)];
  def job = new org.jenkinsci.plugins.workflow.job.WorkflowJob(parent, "Joala@${branchName}")
  job.definition = flowDefinition
  job.description = "Builds branch ${branchName} of https://github.com/CoreMedia/joala"
}

def parent = Jenkins.instance

addWorkflowJob(parent, "jenkins")

parent.reload()
