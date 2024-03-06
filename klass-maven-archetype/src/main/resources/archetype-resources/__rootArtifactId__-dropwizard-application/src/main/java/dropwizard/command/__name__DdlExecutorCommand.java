package ${package}.dropwizard.command;

import cool.klass.reladomo.connectionmanager.h2.H2ConnectionManager;
import cool.klass.reladomo.ddl.executor.DatabaseDdlExecutor;
import ${package}.dropwizard.application.${name}Configuration;
import io.dropwizard.Application;
import io.dropwizard.cli.EnvironmentCommand;
import io.dropwizard.setup.Environment;
import net.sourceforge.argparse4j.inf.Namespace;

// TODO: Extract this into a shared library command?
public class ${name}DdlExecutorCommand extends EnvironmentCommand<${name}Configuration>
{
    public ${name}DdlExecutorCommand(Application<${name}Configuration> application)
    {
        super(application, "execute-ddls", "Execute Reladomo-generated DDLs to drop and recreate all tables.");
    }

    @Override
    protected void run(Environment environment, Namespace namespace, ${name}Configuration configuration)
    {
        DatabaseDdlExecutor.executeSql(H2ConnectionManager.getInstance());
    }
}
