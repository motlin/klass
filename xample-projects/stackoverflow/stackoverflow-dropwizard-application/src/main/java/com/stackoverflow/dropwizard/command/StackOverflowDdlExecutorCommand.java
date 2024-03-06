package com.stackoverflow.dropwizard.command;

import cool.klass.reladomo.connectionmanager.h2.H2ConnectionManager;
import cool.klass.reladomo.ddl.executor.DatabaseDdlExecutor;
import com.stackoverflow.dropwizard.application.StackOverflowConfiguration;
import io.dropwizard.Application;
import io.dropwizard.cli.EnvironmentCommand;
import io.dropwizard.setup.Environment;
import net.sourceforge.argparse4j.inf.Namespace;

public class StackOverflowDdlExecutorCommand extends EnvironmentCommand<StackOverflowConfiguration>
{
    public StackOverflowDdlExecutorCommand(Application<StackOverflowConfiguration> application)
    {
        super(application, "execute-ddls", "Execute Reladomo-generated DDLs to drop and recreate all tables.");
    }

    @Override
    protected void run(Environment environment, Namespace namespace, StackOverflowConfiguration configuration)
    {
        DatabaseDdlExecutor.executeSql(H2ConnectionManager.getInstance());
    }
}
