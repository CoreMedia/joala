import jenkins.model.*

def jenkins = Jenkins.instance

def desc = jenkins.getDescriptor("hudson.plugins.git.GitSCM")

desc.setGlobalConfigName("Mark Michaelis")
desc.setGlobalConfigEmail("mark.michaelis@coremedia.com")

desc.save()
