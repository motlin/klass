package cool.klass.xample.coverage.dropwizard.application;

import javax.annotation.Nonnull;

import com.fasterxml.jackson.databind.ObjectMapper;
import cool.klass.dropwizard.command.model.json.GenerateJsonModelCommand;
import cool.klass.model.meta.domain.api.DomainModel;
import cool.klass.model.meta.domain.api.source.DomainModelWithSourceCode;
import cool.klass.serialization.jackson.module.meta.model.module.KlassMetaModelJacksonModule;
import cool.klass.service.klass.html.KlassHtmlResource;
import cool.klass.servlet.filter.mdc.jsonview.JsonViewDynamicFeature;
import cool.klass.servlet.logging.structured.klass.response.KlassResponseStructuredLoggingFilter;
import cool.klass.xample.coverage.graphql.runtime.wiring.CoverageExampleRuntimeWiringBuilder;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import io.liftwizard.dropwizard.bundle.graphql.LiftwizardGraphQLBundle;

public class CoverageExampleApplication
        extends AbstractCoverageExampleApplication
{
    public static void main(String[] args) throws Exception
    {
        new CoverageExampleApplication().run(args);
    }

    @Override
    public void initialize(@Nonnull Bootstrap<CoverageExampleConfiguration> bootstrap)
    {
        super.initialize(bootstrap);
    }

    @Override
    protected void initializeCommands(@Nonnull Bootstrap<CoverageExampleConfiguration> bootstrap)
    {
        bootstrap.addCommand(new GenerateJsonModelCommand<>(this));
    }

    @Override
    protected void initializeBundles(@Nonnull Bootstrap<CoverageExampleConfiguration> bootstrap)
    {
        super.initializeBundles(bootstrap);

        bootstrap.addBundle(new LiftwizardGraphQLBundle<>(new CoverageExampleRuntimeWiringBuilder()));
    }

    @Override
    protected void registerLoggingFilters(@Nonnull Environment environment)
    {
        super.registerLoggingFilters(environment);

        environment.jersey().register(KlassResponseStructuredLoggingFilter.class);
        environment.jersey().register(JsonViewDynamicFeature.class);
    }

    @Override
    protected void registerJacksonModules(@Nonnull Environment environment)
    {
        super.registerJacksonModules(environment);

        environment.getObjectMapper().registerModule(new KlassMetaModelJacksonModule());
    }

    @Override
    public void run(
            @Nonnull CoverageExampleConfiguration configuration,
            @Nonnull Environment environment) throws Exception
    {
        ObjectMapper objectMapper = environment.getObjectMapper();
        DomainModel domainModel = configuration
                .getKlassFactory()
                .getDomainModelFactory()
                .createDomainModel(objectMapper);

        environment.jersey().register(new JsonViewDynamicFeature(domainModel));

        // TODO: Move up to generated abstract class?
        if (domainModel instanceof DomainModelWithSourceCode)
        {
            environment.jersey().register(new KlassHtmlResource((DomainModelWithSourceCode) domainModel));
        }

        super.run(configuration, environment);
    }
}
