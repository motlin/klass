package cool.klass.dropwizard.auth.filter.provider.firebase;

import com.google.auto.service.AutoService;
import com.google.firebase.auth.FirebaseAuth;
import cool.klass.dropwizard.auth.filter.provider.AuthFilterProvider;
import cool.klass.firebase.principal.FirebasePrincipal;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import com.typesafe.config.ConfigRenderOptions;
import io.dropwizard.auth.Authenticator;
import io.dropwizard.auth.oauth.OAuthCredentialAuthFilter;
import io.dropwizard.auth.oauth.OAuthCredentialAuthFilter.Builder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@AutoService(AuthFilterProvider.class)
public class FirebaseAuthFilterProvider implements AuthFilterProvider
{
    private static final Logger LOGGER = LoggerFactory.getLogger(FirebaseAuthFilterProvider.class);

    @Override
    public OAuthCredentialAuthFilter<FirebasePrincipal> getAuthFilter()
    {
        Config config               = ConfigFactory.load();
        Config firebaseBundleConfig = config.getConfig("klass.firebase");

        if (LOGGER.isDebugEnabled())
        {
            ConfigRenderOptions configRenderOptions = ConfigRenderOptions.defaults()
                    .setJson(false)
                    .setOriginComments(false);
            String render = firebaseBundleConfig.root().render(configRenderOptions);
            LOGGER.debug("Firebase Bundle configuration:\n{}", render);
        }

        String firebaseCredentialsClasspathLocation = config.getString(
                "klass.firebase.firebaseCredentialsClasspathLocation");
        String databaseUrl = config.getString("klass.firebase.databaseUrl");

        FirebaseAuthFactory firebaseAuthFactory = new FirebaseAuthFactory(
                firebaseCredentialsClasspathLocation,
                databaseUrl);
        FirebaseAuth firebaseAuth = firebaseAuthFactory.getFirebaseAuth();

        Authenticator<String, FirebasePrincipal> authenticator = new FirebaseOAuthAuthenticator(firebaseAuth);

        return new Builder<FirebasePrincipal>()
                .setAuthenticator(authenticator)
                .setPrefix("Bearer")
                .buildAuthFilter();
    }
}
