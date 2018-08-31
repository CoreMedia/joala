import hudson.model.*;
import jenkins.model.*;

def descriptor = new hudson.model.JDK.DescriptorImpl();
descriptor.setInstallations( new hudson.model.JDK("JDK8", "/usr/lib/jvm/java-8-openjdk-amd64"));

def mavenExtensionInstance=Jenkins.instance.getExtensionList(hudson.tasks.Maven.DescriptorImpl.class)[0];
def mavenInstallations=(mavenExtensionInstance.installations as List);
mavenInstallations.add(new hudson.tasks.Maven.MavenInstallation("MAVEN3", "/usr/share/maven", []));
mavenExtensionInstance.installations=mavenInstallations
mavenExtensionInstance.save()
