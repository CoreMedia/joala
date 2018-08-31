import com.cloudbees.plugins.credentials.*
import org.jenkinsci.plugins.plaincredentials.impl.*
import com.cloudbees.plugins.credentials.domains.*
import com.cloudbees.plugins.credentials.impl.*
import hudson.util.*

Credentials sonatypeCredentials = (Credentials) new UsernamePasswordCredentialsImpl(
        CredentialsScope.GLOBAL,
        "sonatype-jira",
        "Sonatype JIRA Credentials (https://issues.sonatype.org/); required for release to Maven Central",
        "your-jira-id",
        "your-jira-pwd"
)

Credentials githubOauthToken = (Credentials) new StringCredentialsImpl(
        CredentialsScope.GLOBAL,
        "github-oauth",
        "GitHub OAUTH Token required for uploading GitHub pages on release. Token requires permissions 'repo' and 'user'.",
        new Secret('your-github-oauth-token')
)

Credentials gpgSign = (Credentials) new CertificateCredentialsImpl(
        CredentialsScope.GLOBAL,
        "gpg-sign",
        "GPG Key for signing Maven Central Artifacts. Must have been published to hkp://pool.sks-keyservers.net/",
        "gpg-key-password",
        new CertificateCredentialsImpl.UploadedKeyStoreSource("")
)

def store = SystemCredentialsProvider.getInstance().getStore()

store.addCredentials(Domain.global(), sonatypeCredentials)
store.addCredentials(Domain.global(), githubOauthToken)
store.addCredentials(Domain.global(), gpgSign)
