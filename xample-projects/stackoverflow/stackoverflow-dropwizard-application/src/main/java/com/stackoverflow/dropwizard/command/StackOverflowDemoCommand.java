package com.stackoverflow.dropwizard.command;

import com.stackoverflow.dropwizard.application.StackOverflowConfiguration;
import io.dropwizard.Application;
import io.dropwizard.cli.EnvironmentCommand;
import io.dropwizard.cli.ServerCommand;
import io.dropwizard.setup.Environment;
import net.sourceforge.argparse4j.inf.Namespace;

public class StackOverflowDemoCommand extends EnvironmentCommand<StackOverflowConfiguration>
{
    private final Application<StackOverflowConfiguration> application;

    public StackOverflowDemoCommand(Application<StackOverflowConfiguration> application)
    {
        super(application, "demo", "Execute DDLs, insert example data, run server");
        this.application = application;
    }

    @Override
    protected void run(
            Environment environment,
            Namespace namespace,
            StackOverflowConfiguration configuration) throws Exception
    {
        new StackOverflowTestDataGeneratorCommand(this.application).run(environment, namespace, configuration);
        new ServerCommand<StackOverflowConfiguration>(this.application)
        {
            @Override
            public void run(
                    Environment environment,
                    Namespace namespace,
                    StackOverflowConfiguration configuration) throws Exception
            {
                super.run(environment, namespace, configuration);
            }
        }.run(environment, namespace, configuration);
    }
}
