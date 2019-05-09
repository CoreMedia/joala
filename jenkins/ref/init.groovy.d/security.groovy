import jenkins.model.*
import hudson.security.csrf.*

Set<String> agentProtocols = Jenkins.instance.getAgentProtocols()

// Jenkins complains that these protocols are deprecated and should not
// be used anymore.
def excludedProtocols = ['CLI-connect', 'CLI2-connect', 'JNLP-connect', 'JNLP2-connect']
agentProtocols -= excludedProtocols

Jenkins.instance.setAgentProtocols(agentProtocols)

Jenkins.instance.setCrumbIssuer(new DefaultCrumbIssuer(true))
