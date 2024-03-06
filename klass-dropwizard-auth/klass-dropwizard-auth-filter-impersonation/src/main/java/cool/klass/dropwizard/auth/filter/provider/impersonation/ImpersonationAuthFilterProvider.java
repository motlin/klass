package cool.klass.dropwizard.auth.filter.provider.impersonation;

import com.google.auto.service.AutoService;
import cool.klass.dropwizard.auth.filter.provider.AuthFilterProvider;
import io.dropwizard.auth.AuthFilter;
import io.dropwizard.auth.oauth.OAuthCredentialAuthFilter.Builder;

@AutoService(AuthFilterProvider.class)
public class ImpersonationAuthFilterProvider implements AuthFilterProvider
{
    @Override
    public AuthFilter<?, ImpersonatedPrincipal> getAuthFilter()
    {
        return new Builder<ImpersonatedPrincipal>()
                .setAuthenticator(new ImpersonationAuthenticator())
                .setPrefix("Impersonation")
                .buildAuthFilter();
    }
}
