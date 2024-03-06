package cool.klass.dropwizard.configuration.auth.filter.firebase;

import java.security.Principal;

import javax.annotation.Nonnull;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.google.auto.service.AutoService;
import cool.klass.dropwizard.configuration.auth.filter.AuthFilterFactory;
import cool.klass.firebase.principal.FirebasePrincipal;
import io.dropwizard.auth.AuthFilter;
import io.dropwizard.auth.Authenticator;
import io.dropwizard.auth.oauth.OAuthCredentialAuthFilter.Builder;

@JsonTypeName("firebase")
@AutoService(AuthFilterFactory.class)
public class FirebaseAuthFilterFactory implements AuthFilterFactory
{
    private @Valid @NotNull String credentialsClasspathLocation;
    private @Valid @NotNull String databaseUrl;

    @Nonnull
    @Override
    public AuthFilter<?, ? extends Principal> createAuthFilter()
    {
        FirebaseAuth firebaseAuthFactory = new FirebaseAuth(
                this.credentialsClasspathLocation,
                this.databaseUrl);
        com.google.firebase.auth.FirebaseAuth firebaseAuth = firebaseAuthFactory.getFirebaseAuth();

        Authenticator<String, FirebasePrincipal> authenticator = new FirebaseOAuthAuthenticator(firebaseAuth);

        return new Builder<FirebasePrincipal>()
                .setAuthenticator(authenticator)
                .setPrefix("Bearer")
                .buildAuthFilter();
    }

    @JsonProperty
    public String getCredentialsClasspathLocation()
    {
        return this.credentialsClasspathLocation;
    }

    @JsonProperty
    public void setCredentialsClasspathLocation(String credentialsClasspathLocation)
    {
        this.credentialsClasspathLocation = credentialsClasspathLocation;
    }

    @JsonProperty
    public String getDatabaseUrl()
    {
        return this.databaseUrl;
    }

    @JsonProperty
    public void setDatabaseUrl(String databaseUrl)
    {
        this.databaseUrl = databaseUrl;
    }
}
