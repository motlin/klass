package cool.klass.dropwizard.bundle.auth.filter;

import java.security.Principal;
import java.util.ServiceLoader;

import com.google.auto.service.AutoService;
import cool.klass.dropwizard.auth.filter.provider.AuthFilterProvider;
import cool.klass.dropwizard.bundle.prioritized.PrioritizedBundle;
import cool.klass.dropwizard.configuration.AbstractKlassConfiguration;
import io.dropwizard.auth.AuthDynamicFeature;
import io.dropwizard.auth.AuthFilter;
import io.dropwizard.auth.AuthValueFactoryProvider.Binder;
import io.dropwizard.auth.chained.ChainedAuthFilter;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import org.eclipse.collections.api.list.ImmutableList;
import org.eclipse.collections.impl.factory.Lists;
import org.glassfish.jersey.server.filter.RolesAllowedDynamicFeature;

@AutoService(PrioritizedBundle.class)
public class AuthFilterBundle
        implements PrioritizedBundle
{
    @Override
    public void initialize(Bootstrap<?> bootstrap)
    {
    }

    @Override
    public void run(AbstractKlassConfiguration configuration, Environment environment)
    {
        ServiceLoader<AuthFilterProvider> serviceLoader      = ServiceLoader.load(AuthFilterProvider.class);
        ImmutableList<AuthFilterProvider> prioritizedBundles = Lists.immutable.withAll(serviceLoader);
        ImmutableList<? extends AuthFilter<?, ? extends Principal>> authFilters =
                prioritizedBundles.collect(AuthFilterProvider::getAuthFilter);

        ChainedAuthFilter chainedAuthFilter = new ChainedAuthFilter(authFilters.castToList());

        environment.jersey().register(new AuthDynamicFeature(chainedAuthFilter));
        environment.jersey().register(RolesAllowedDynamicFeature.class);
        environment.jersey().register(new Binder<>(Principal.class));
    }
}
