package cool.klass.dropwizard.bundle.auth.filter;

import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.annotation.Nonnull;
import javax.ws.rs.container.ContainerResponseFilter;

import com.google.auto.service.AutoService;
import cool.klass.dropwizard.bundle.prioritized.PrioritizedBundle;
import cool.klass.dropwizard.configuration.auth.filter.AuthFilterFactory;
import cool.klass.dropwizard.configuration.auth.filter.AuthFilterFactoryProvider;
import cool.klass.servlet.filter.mdc.ClearMDCContainerResponseFilter;
import io.dropwizard.auth.AuthDynamicFeature;
import io.dropwizard.auth.AuthFilter;
import io.dropwizard.auth.AuthValueFactoryProvider.Binder;
import io.dropwizard.auth.chained.ChainedAuthFilter;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import org.eclipse.collections.api.list.ImmutableList;
import org.eclipse.collections.impl.list.mutable.ListAdapter;
import org.glassfish.jersey.server.filter.RolesAllowedDynamicFeature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@AutoService(PrioritizedBundle.class)
public class AuthFilterBundle
        implements PrioritizedBundle<AuthFilterFactoryProvider>
{
    private static final Logger LOGGER = LoggerFactory.getLogger(AuthFilterBundle.class);

    @Override
    public void initialize(Bootstrap<?> bootstrap)
    {
    }

    @Override
    public void run(AuthFilterFactoryProvider configuration, @Nonnull Environment environment)
    {
        List<AuthFilterFactory>                  authFilterFactories = configuration.getAuthFilterFactories();
        List<AuthFilter<?, ? extends Principal>> authFilters         = this.getAuthFilters(authFilterFactories);

        if (authFilters.isEmpty())
        {
            LOGGER.warn("{} disabled.", AuthFilterBundle.class.getSimpleName());
            return;
        }

        Stream<String> authFilterNames = authFilters
                .stream()
                .map(Object::getClass)
                .map(Class::getSimpleName);

        LOGGER.info("Running {} with auth filters {}.", AuthFilterBundle.class.getSimpleName(), authFilterNames);

        environment.jersey().register(this.getAuthDynamicFeature(authFilters));
        environment.jersey().register(this.getClearMDCContainerResponseFilter(authFilterFactories));
        environment.jersey().register(RolesAllowedDynamicFeature.class);
        environment.jersey().register(new Binder<>(Principal.class));

        LOGGER.info("Completing {}.", AuthFilterBundle.class.getSimpleName());
    }

    @Nonnull
    private List<AuthFilter<?, ? extends Principal>> getAuthFilters(List<AuthFilterFactory> authFilterFactories)
    {
        return authFilterFactories
                .stream()
                .map(AuthFilterFactory::createAuthFilter)
                .collect(Collectors.toList());
    }

    @Nonnull
    private AuthDynamicFeature getAuthDynamicFeature(List<AuthFilter<?, ? extends Principal>> authFilters)
    {
        ChainedAuthFilter chainedAuthFilter = new ChainedAuthFilter(authFilters);
        return new AuthDynamicFeature(chainedAuthFilter);
    }

    @Nonnull
    private ContainerResponseFilter getClearMDCContainerResponseFilter(List<AuthFilterFactory> authFilterFactories)
    {
        ImmutableList<String> mdcKeys = ListAdapter.adapt(authFilterFactories)
                .flatCollect(AuthFilterFactory::getMDCKeys)
                .toImmutable();
        return new ClearMDCContainerResponseFilter(mdcKeys);
    }
}
