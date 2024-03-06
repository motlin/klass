package ${package}.dropwizard.application;

import javax.annotation.Nonnull;

import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;

public class ${name}Application extends Abstract${name}Application
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
    public void run(
            ${name}Configuration configuration,
            @Nonnull Environment environment) throws Exception
    {
        super.run(configuration, environment);
    }
}
