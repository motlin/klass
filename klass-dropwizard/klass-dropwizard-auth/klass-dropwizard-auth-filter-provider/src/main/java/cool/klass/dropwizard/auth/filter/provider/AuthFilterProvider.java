package cool.klass.dropwizard.auth.filter.provider;

import java.security.Principal;

import io.dropwizard.auth.AuthFilter;

public interface AuthFilterProvider
{
    AuthFilter<?, ? extends Principal> getAuthFilter();
}
