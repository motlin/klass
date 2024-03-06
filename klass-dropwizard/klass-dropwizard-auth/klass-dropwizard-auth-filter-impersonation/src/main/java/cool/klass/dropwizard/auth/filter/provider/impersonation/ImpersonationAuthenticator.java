package cool.klass.dropwizard.auth.filter.provider.impersonation;

import java.util.Optional;

import io.dropwizard.auth.Authenticator;

public class ImpersonationAuthenticator implements Authenticator<String, ImpersonatedPrincipal>
{
    @Override
    public Optional<ImpersonatedPrincipal> authenticate(String principalName)
    {
        ImpersonatedPrincipal impersonatedUser = new ImpersonatedPrincipal(principalName);
        return Optional.of(impersonatedUser);
    }
}
