import com.cloudbees.plugins.credentials.*
import org.jenkinsci.plugins.plaincredentials.impl.*
import com.cloudbees.plugins.credentials.domains.*
import com.cloudbees.plugins.credentials.impl.*
import hudson.util.*
import java.nio.file.*

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

//Path fileLocation = Paths.get("/path/to/some/file.txt");

//SecretBytes secretBytes = SecretBytes.fromBytes(Files.readAllBytes(fileLocation))
SecretBytes secretBytes = SecretBytes.fromBytes(new byte[0])

Credentials gpgSecRing = (Credentials) new FileCredentialsImpl(
        CredentialsScope.GLOBAL,
        "gpg-secring",
        "GPG Key for signing Maven Central Artifacts. Must have been published to hkp://pool.sks-keyservers.net/",
        "secring.gpg",
        secretBytes
)

Credentials gpgPassword = (Credentials) new StringCredentialsImpl(
        CredentialsScope.GLOBAL,
        "gpg-password",
        "Password for gpg key, as specified in gpg-sign.",
        new Secret('your-gpg-password')
)

def store = SystemCredentialsProvider.getInstance().getStore()

store.addCredentials(Domain.global(), sonatypeCredentials)
store.addCredentials(Domain.global(), githubOauthToken)
store.addCredentials(Domain.global(), gpgSecRing)
store.addCredentials(Domain.global(), gpgPassword)
