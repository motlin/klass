package cool.klass.dropwizard.bundle.ddl.executor;

import java.lang.reflect.Method;

import javax.annotation.Nonnull;

import com.google.auto.service.AutoService;
import com.gs.fw.common.mithra.connectionmanager.SourcelessConnectionManager;
import cool.klass.dropwizard.bundle.prioritized.PrioritizedBundle;
import cool.klass.reladomo.ddl.executor.DatabaseDdlExecutor;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import com.typesafe.config.ConfigRenderOptions;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@AutoService(PrioritizedBundle.class)
public class DdlExecutorBundle implements PrioritizedBundle
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
    public void run(@Nonnull Environment environment)
    {
        Config config           = ConfigFactory.load();
        Config dropTablesConfig = config.getConfig("klass.data.dropTables");

        if (LOGGER.isDebugEnabled())
        {
            ConfigRenderOptions configRenderOptions = ConfigRenderOptions.defaults()
                    .setJson(false)
                    .setOriginComments(false);
            String render = dropTablesConfig.root().render(configRenderOptions);
            LOGGER.debug("DDL Executor Bundle configuration:\n{}", render);
        }

        boolean enabled = dropTablesConfig.getBoolean("enabled");
        if (!enabled)
        {
            return;
        }

        String connectionManagerFullyQualifiedName = dropTablesConfig.getString("connectionManager");
        SourcelessConnectionManager connectionManager =
                this.getConnectionManager(connectionManagerFullyQualifiedName);
        DatabaseDdlExecutor.executeSql(connectionManager.getConnection());
    }

    protected SourcelessConnectionManager getConnectionManager(String connectionManagerFullyQualifiedName)
    {
        try
        {
            Class<?> connectionManagerClass = Class.forName(connectionManagerFullyQualifiedName);
            Method   getInstance            = connectionManagerClass.getMethod("getInstance");
            return (SourcelessConnectionManager) getInstance.invoke(null);
        }
        catch (ReflectiveOperationException e)
        {
            throw new RuntimeException(e);
        }
    }
}
