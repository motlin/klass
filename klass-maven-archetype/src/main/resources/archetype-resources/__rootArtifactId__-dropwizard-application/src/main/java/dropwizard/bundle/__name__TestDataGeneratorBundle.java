package ${package}.dropwizard.bundle;

import ${package}.reladomo.data.generator.${name}TestDataGenerator;
import io.dropwizard.Bundle;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;

public class ${name}TestDataGeneratorBundle implements Bundle
{
    @Override
    public void initialize(Bootstrap<?> bootstrap)
    {
    }

    @Override
    public void run(Environment environment)
    {
        ${name}TestDataGenerator.populateData();
    }
}
