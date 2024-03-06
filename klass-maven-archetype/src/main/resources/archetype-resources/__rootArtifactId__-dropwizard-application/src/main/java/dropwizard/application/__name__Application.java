package ${package}.dropwizard.application;

import javax.annotation.Nonnull;

import cool.klass.serialization.jackson.module.meta.model.module.KlassMetaModelJacksonModule;
import cool.klass.servlet.filter.mdc.jsonview.JsonViewDynamicFeature;
import cool.klass.servlet.logging.structured.klass.response.KlassResponseStructuredLoggingFilter;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;

public class ${name}Application
        extends Abstract${name}Application
{
    public static void main(String[] args) throws Exception
    {
        new ${name}Application().run(args);
    }

    @Override
    public void initialize(@Nonnull Bootstrap<${name}Configuration> bootstrap)
    {
        super.initialize(bootstrap);

        // TODO: application initialization
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
            @Nonnull ${name}Configuration configuration,
            @Nonnull Environment environment) throws Exception
    {
        super.run(configuration, environment);
    }
}
