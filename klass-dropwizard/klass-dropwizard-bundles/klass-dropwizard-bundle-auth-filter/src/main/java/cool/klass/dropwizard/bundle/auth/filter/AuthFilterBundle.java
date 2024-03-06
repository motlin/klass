package cool.klass.dropwizard.bundle.auth.filter;

import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.Nonnull;

import com.google.auto.service.AutoService;
import cool.klass.dropwizard.bundle.prioritized.PrioritizedBundle;
import cool.klass.dropwizard.configuration.auth.filter.AuthFilterFactory;
import cool.klass.dropwizard.configuration.auth.filter.AuthFilterFactoryProvider;
import io.dropwizard.auth.AuthDynamicFeature;
import io.dropwizard.auth.AuthFilter;
import io.dropwizard.auth.AuthValueFactoryProvider.Binder;
import io.dropwizard.auth.chained.ChainedAuthFilter;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
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
        List<? extends AuthFilter<?, ? extends Principal>> authFilters = configuration.getAuthFilterFactories()
                .stream()
                .map(AuthFilterFactory::createAuthFilter)
                .collect(Collectors.toList());

        if (authFilters.isEmpty())
        {
            LOGGER.info("{} disabled.", AuthFilterBundle.class.getSimpleName());
            throw new AssertionError();
        }

        LOGGER.info("Running {}.", AuthFilterBundle.class.getSimpleName());

        ChainedAuthFilter chainedAuthFilter = new ChainedAuthFilter(authFilters);

        environment.jersey().register(new AuthDynamicFeature(chainedAuthFilter));
        environment.jersey().register(RolesAllowedDynamicFeature.class);
        environment.jersey().register(new Binder<>(Principal.class));

        LOGGER.info("Completing {}.", AuthFilterBundle.class.getSimpleName());
    }
}
