package com.stackoverflow.dropwizard.command;

import com.stackoverflow.dropwizard.application.StackOverflowConfiguration;
import com.stackoverflow.reladomo.data.generator.StackOverflowTestDataGenerator;
import io.dropwizard.Application;
import io.dropwizard.cli.EnvironmentCommand;
import io.dropwizard.setup.Environment;
import net.sourceforge.argparse4j.inf.Namespace;

public class StackOverflowTestDataGeneratorCommand extends EnvironmentCommand<StackOverflowConfiguration>
{
    public StackOverflowTestDataGeneratorCommand(Application<StackOverflowConfiguration> application)
    {
        super(application, "generate-data", "Generate Reladomo example data and write it into the database.");
    }

    @Override
    protected void run(Environment environment, Namespace namespace, StackOverflowConfiguration configuration)
    {
        StackOverflowTestDataGenerator.populateData();
    }
}
