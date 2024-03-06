package cool.klass.dropwizard.bundle.firebase;

import javax.annotation.Nonnull;

import com.google.auto.service.AutoService;
import com.google.firebase.auth.FirebaseAuth;
import cool.klass.dropwizard.bundle.prioritized.PrioritizedBundle;
import cool.klass.firebase.principal.FirebasePrincipal;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import com.typesafe.config.ConfigRenderOptions;
import io.dropwizard.auth.AuthDynamicFeature;
import io.dropwizard.auth.AuthValueFactoryProvider.Binder;
import io.dropwizard.auth.Authenticator;
import io.dropwizard.auth.oauth.OAuthCredentialAuthFilter;
import io.dropwizard.auth.oauth.OAuthCredentialAuthFilter.Builder;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@AutoService(PrioritizedBundle.class)
public class FirebaseBundle implements PrioritizedBundle
{
    private static final Logger LOGGER = LoggerFactory.getLogger(FirebaseBundle.class);

    @Override
    public void initialize(Bootstrap<?> bootstrap)
    {
    }

    @Override
    public void run(@Nonnull Environment environment)
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
        OAuthCredentialAuthFilter<FirebasePrincipal> authFilter = new Builder<FirebasePrincipal>()
                .setAuthenticator(authenticator)
                .setPrefix("Bearer")
                .buildAuthFilter();
        AuthDynamicFeature authDynamicFeature = new AuthDynamicFeature(authFilter);
        environment.jersey().register(authDynamicFeature);
        environment.jersey().register(new Binder<>(FirebasePrincipal.class));
    }
}
