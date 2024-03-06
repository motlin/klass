package com.stackoverflow.dropwizard.command;

import com.stackoverflow.dropwizard.application.StackOverflowConfiguration;
import com.stackoverflow.reladomo.data.generator.StackOverflowTestDataGenerator;
import io.dropwizard.cli.ConfiguredCommand;
import io.dropwizard.setup.Bootstrap;
import net.sourceforge.argparse4j.inf.Namespace;

public class StackOverflowTestDataGeneratorCommand extends ConfiguredCommand<StackOverflowConfiguration>
{
    public StackOverflowTestDataGeneratorCommand()
    {
        super("generate-data", "Generate Reladomo example data and write it into the database.");
    }

    @Override
    protected void run(
            Bootstrap<StackOverflowConfiguration> bootstrap,
            Namespace namespace,
            StackOverflowConfiguration configuration)
    {
        StackOverflowTestDataGenerator.populateData();
    }
}
