package klass.model.meta.domain.dropwizard.command;

import io.dropwizard.Application;
import io.dropwizard.cli.EnvironmentCommand;
import io.dropwizard.setup.Environment;
import klass.model.meta.domain.dropwizard.application.BootstrappedMetaModelConfiguration;
import klass.model.meta.domain.reladomo.data.generator.BootstrappedMetaModelTestDataGenerator;
import net.sourceforge.argparse4j.inf.Namespace;

public class BootstrappedMetaModelTestDataGeneratorCommand extends EnvironmentCommand<BootstrappedMetaModelConfiguration>
{
    public BootstrappedMetaModelTestDataGeneratorCommand(Application<BootstrappedMetaModelConfiguration> application)
    {
        super(application, "generate-data", "Generate Reladomo example data and write it into the database.");
    }

    @Override
    protected void run(Environment environment, Namespace namespace, BootstrappedMetaModelConfiguration configuration)
    {
        BootstrappedMetaModelTestDataGenerator.populateData();
    }
}
