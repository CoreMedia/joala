#!/usr/bin/env groovy

import hudson.model.*;
import jenkins.model.*;

import hudson.plugins.git.*;

def scm = new GitSCM("https://github.com/CoreMedia/joala.git")
scm.branches = [new BranchSpec("*/jenkins")];

def flowDefinition = new org.jenkinsci.plugins.workflow.cps.CpsScmFlowDefinition(scm, "Jenkinsfile")

def parent = Jenkins.instance
def job = new org.jenkinsci.plugins.workflow.job.WorkflowJob(parent, "Joala/jenkins")
job.definition = flowDefinition

parent.reload()
