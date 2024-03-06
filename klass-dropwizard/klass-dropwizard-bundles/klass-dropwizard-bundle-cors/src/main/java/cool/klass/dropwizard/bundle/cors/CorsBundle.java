package cool.klass.dropwizard.bundle.cors;

import java.util.EnumSet;

import javax.annotation.Nonnull;
import javax.servlet.DispatcherType;
import javax.servlet.FilterRegistration.Dynamic;

import com.google.auto.service.AutoService;
import cool.klass.dropwizard.bundle.prioritized.PrioritizedBundle;
import cool.klass.dropwizard.configuration.AbstractKlassConfiguration;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import org.eclipse.jetty.servlets.CrossOriginFilter;

@AutoService(PrioritizedBundle.class)
public class CorsBundle implements PrioritizedBundle
{
    @Override
    public void initialize(Bootstrap<?> bootstrap)
    {
    }

    @Override
    public void run(AbstractKlassConfiguration configuration, @Nonnull Environment environment)
    {
        // https://stackoverflow.com/a/25801822/23572

        Dynamic cors = environment.servlets().addFilter("CORS", CrossOriginFilter.class);

        // TODO  #47: Enhance CorsBundle to be configurable through HOCON.
        cors.setInitParameter(CrossOriginFilter.ALLOWED_ORIGINS_PARAM, "*");
        cors.setInitParameter(
                CrossOriginFilter.ALLOWED_HEADERS_PARAM,
                "X-Requested-With,Content-Type,Accept,Origin,Authorization");
        cors.setInitParameter(CrossOriginFilter.ALLOWED_METHODS_PARAM, "OPTIONS,GET,PUT,POST,DELETE,HEAD");
        cors.setInitParameter(CrossOriginFilter.ALLOW_CREDENTIALS_PARAM, "true");
        cors.addMappingForUrlPatterns(EnumSet.allOf(DispatcherType.class), true, "/*");
    }
}
