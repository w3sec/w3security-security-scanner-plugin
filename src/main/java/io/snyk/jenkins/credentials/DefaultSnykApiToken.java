package io.w3security.jenkins.credentials;

import static hudson.util.Secret.fromString;

import com.cloudbees.plugins.credentials.CredentialsScope;
import com.cloudbees.plugins.credentials.impl.BaseStandardCredentials;
import hudson.Extension;
import hudson.util.Secret;
import javax.annotation.Nonnull;
import org.kohsuke.stapler.DataBoundConstructor;

/**
 * Default implementation of {@link W3SecurityApiToken} for use by Jenkins {@link com.cloudbees.plugins.credentials.CredentialsProvider}
 * instances that store {@link Secret} locally.
 */
public class DefaultW3SecurityApiToken
  extends BaseStandardCredentials
  implements W3SecurityApiToken {

  @Nonnull
  private final Secret token;

  @DataBoundConstructor
  public DefaultW3SecurityApiToken(
    CredentialsScope scope,
    String id,
    String description,
    @Nonnull String token
  ) {
    super(scope, id, description);
    this.token = fromString(token);
  }

  @Nonnull
  @Override
  public Secret getToken() {
    return token;
  }

  @Extension
  public static class DefaultW3SecurityApiTokenDescriptor
    extends BaseStandardCredentialsDescriptor {

    @Nonnull
    @Override
    public String getDisplayName() {
      return "W3Security API token";
    }
  }
}
