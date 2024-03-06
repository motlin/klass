package cool.klass.dropwizard.configuration.auth.filter.impersonation;

import java.util.Optional;

import javax.annotation.Nonnull;

import io.dropwizard.auth.Authenticator;

public class ImpersonationAuthenticator implements Authenticator<String, ImpersonatedPrincipal>
{
    @Nonnull
    @Override
    public Optional<ImpersonatedPrincipal> authenticate(String principalName)
    {
        ImpersonatedPrincipal impersonatedUser = new ImpersonatedPrincipal(principalName);
        return Optional.of(impersonatedUser);
    }
}
