package cool.klass.dropwizard.bundle.ddl.executor;

import java.sql.Connection;
import java.sql.SQLException;

import com.codahale.metrics.MetricRegistry;
import com.google.auto.service.AutoService;
import cool.klass.dropwizard.bundle.prioritized.PrioritizedBundle;
import cool.klass.dropwizard.configuration.ddl.executor.DdlExecutorFactory;
import cool.klass.dropwizard.configuration.ddl.executor.DdlExecutorFactoryProvider;
import cool.klass.reladomo.ddl.executor.DatabaseDdlExecutor;
import io.dropwizard.db.DataSourceFactory;
import io.dropwizard.db.ManagedDataSource;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@AutoService(PrioritizedBundle.class)
public class DdlExecutorBundle implements PrioritizedBundle<DdlExecutorFactoryProvider>
{
    private static final Logger LOGGER = LoggerFactory.getLogger(DdlExecutorBundle.class);

    @Override
    public int getPriority()
    {
        return -3;
    }

    @Override
    public void initialize(Bootstrap<?> bootstrap)
    {
    }

    @Override
    public void run(DdlExecutorFactoryProvider configuration, Environment environment) throws SQLException
    {
        DdlExecutorFactory ddlExecutorFactory = configuration.getDdlExecutorFactory();
        if (!ddlExecutorFactory.isEnabled())
        {
            LOGGER.info("{} disabled.", DdlExecutorBundle.class.getSimpleName());
            return;
        }

        LOGGER.info("Running {}.", DdlExecutorBundle.class.getSimpleName());

        MetricRegistry    metricRegistry    = environment.metrics();
        String            poolName          = DdlExecutorBundle.class.getSimpleName() + " Pool";
        DataSourceFactory dataSourceFactory = ddlExecutorFactory.getDataSourceFactory();
        ManagedDataSource managedDataSource = dataSourceFactory.build(metricRegistry, poolName);
        Connection        connection        = managedDataSource.getConnection();
        DatabaseDdlExecutor.executeSql(connection);

        LOGGER.info("Completing {}.", DdlExecutorBundle.class.getSimpleName());
    }
}
