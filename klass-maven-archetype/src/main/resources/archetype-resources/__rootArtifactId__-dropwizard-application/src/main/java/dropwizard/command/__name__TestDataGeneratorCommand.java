package ${package}.dropwizard.command;

import ${package}.dropwizard.application.${name}Configuration;
import ${package}.reladomo.data.generator.${name}TestDataGenerator;
import io.dropwizard.Application;
import io.dropwizard.cli.EnvironmentCommand;
import io.dropwizard.setup.Environment;
import net.sourceforge.argparse4j.inf.Namespace;

public class ${name}TestDataGeneratorCommand extends EnvironmentCommand<${name}Configuration>
{
    public ${name}TestDataGeneratorCommand(Application<${name}Configuration> application)
    {
        super(application, "generate-data", "Generate Reladomo example data and write it into the database.");
    }

    @Override
    protected void run(Environment environment, Namespace namespace, ${name}Configuration configuration)
    {
        ${name}TestDataGenerator.populateData();
    }
}
