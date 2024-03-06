package cool.klass.dropwizard.configuration.auth.filter;

import java.util.List;

public interface AuthFilterFactoryProvider
{
    List<AuthFilterFactory> getAuthFilterFactories();
}
