import jenkins.model.*
import org.jenkinsci.plugins.configfiles.maven.*
import org.jenkinsci.plugins.configfiles.maven.security.*

def configStore = Jenkins.instance.getExtensionList('org.jenkinsci.plugins.configfiles.GlobalConfigFiles')[0]

def serverCreds = [
        new ServerCredentialMapping("sonatype-nexus-snapshots", "sonatype-jira"),
        new ServerCredentialMapping("sonatype-nexus-staging", "sonatype-jira")
]

def configId =  'release-settings'
def configName = 'Joala Release Settings'
def configComment = 'Global Maven Settings suitable for releasing Joala. Credentials have to be provided within Maven calls.'
def configContent  = '''<settings>
  <!-- Other settings go here -->
</settings>'''

def globalConfig = new GlobalMavenSettingsConfig(configId, configName, configComment, configContent, true, serverCreds)
configStore.save(globalConfig)
