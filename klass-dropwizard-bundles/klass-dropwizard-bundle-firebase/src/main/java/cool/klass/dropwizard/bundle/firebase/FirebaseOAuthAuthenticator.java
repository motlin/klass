package cool.klass.dropwizard.bundle.firebase;

import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.Map;
import java.util.Optional;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseToken;
import cool.klass.firebase.principal.FirebasePrincipal;
import io.dropwizard.auth.Authenticator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

public class FirebaseOAuthAuthenticator implements Authenticator<String, FirebasePrincipal>
{
    private static final Logger LOGGER = LoggerFactory.getLogger(FirebaseOAuthAuthenticator.class);

    private final FirebaseAuth firebaseAuth;

    public FirebaseOAuthAuthenticator(FirebaseAuth firebaseAuth)
    {
        this.firebaseAuth = firebaseAuth;
    }

    @Override
    public Optional<FirebasePrincipal> authenticate(String credentials)
    {
        try
        {
            FirebaseToken       firebaseToken = this.firebaseAuth.verifyIdToken(credentials);
            Map<String, Object> claims        = firebaseToken.getClaims();

            Boolean             emailVerified  = (Boolean) claims.get("email_verified");
            Map<String, Object> firebase       = (Map<String, Object>) claims.get("firebase");
            String              signInProvider = (String) firebase.get("sign_in_provider");

            FirebasePrincipal firebasePrincipal = new FirebasePrincipal(
                    firebaseToken.getUid(),
                    firebaseToken.getName(),
                    firebaseToken.getEmail(),
                    emailVerified,
                    firebaseToken.getIssuer(),
                    firebaseToken.getPicture(),
                    signInProvider);

            MDC.put("principal", String.valueOf(firebasePrincipal));

            return Optional.of(firebasePrincipal);
        }
        catch (FirebaseAuthException e)
        {
            MDC.remove("principal");

            Throwable cause = e.getCause();
            if (cause instanceof UnknownHostException)
            {
                throw new RuntimeException(e);
            }
            if (cause instanceof SocketTimeoutException)
            {
                throw new RuntimeException(e);
            }
            LOGGER.warn("", e);
            return Optional.empty();
        }
    }
}
