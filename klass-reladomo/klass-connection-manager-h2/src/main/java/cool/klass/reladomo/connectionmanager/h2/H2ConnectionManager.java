package cool.klass.reladomo.connectionmanager.h2;

import java.sql.Connection;
import java.util.TimeZone;

import com.gs.fw.common.mithra.bulkloader.BulkLoader;
import com.gs.fw.common.mithra.connectionmanager.SourcelessConnectionManager;
import com.gs.fw.common.mithra.connectionmanager.XAConnectionManager;
import com.gs.fw.common.mithra.databasetype.DatabaseType;
import com.gs.fw.common.mithra.databasetype.H2DatabaseType;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class H2ConnectionManager implements SourcelessConnectionManager
{
    private static final Logger LOGGER = LoggerFactory.getLogger(H2ConnectionManager.class);

    private static final H2ConnectionManager INSTANCE = new H2ConnectionManager();

    private final XAConnectionManager xaConnectionManager;
    private final TimeZone            databaseTimeZone;
    private final String              schemaName;

    private H2ConnectionManager()
    {
        Config config                    = ConfigFactory.load();
        Config h2Config                  = config.getConfig("reladomo.connectionManager.h2");
        String driverClassName           = h2Config.getString("driverClassName");
        int    maxWait                   = h2Config.getInt("maxWait");
        String jdbcConnectionString      = h2Config.getString("jdbcConnectionString");
        String jdbcUser                  = h2Config.getString("jdbcUser");
        String jdbcPassword              = h2Config.getString("jdbcPassword");
        String connectionPoolName        = h2Config.getString("connectionPoolName");
        int    initialConnectionPoolSize = h2Config.getInt("initialConnectionPoolSize");
        int    defaultConnectionPoolSize = h2Config.getInt("defaultConnectionPoolSize");
        String timeZoneName              = h2Config.getString("timeZoneName");

        this.schemaName = h2Config.getString("schemaName");
        this.databaseTimeZone = TimeZone.getTimeZone(timeZoneName);

        LOGGER.info("{}", h2Config);

        XAConnectionManager xaConnectionManager = new XAConnectionManager();
        xaConnectionManager.setDriverClassName(driverClassName);
        xaConnectionManager.setMaxWait(maxWait);
        xaConnectionManager.setJdbcConnectionString(jdbcConnectionString);
        xaConnectionManager.setJdbcUser(jdbcUser);
        xaConnectionManager.setJdbcPassword(jdbcPassword);
        xaConnectionManager.setPoolName(connectionPoolName);
        xaConnectionManager.setInitialSize(initialConnectionPoolSize);
        xaConnectionManager.setPoolSize(defaultConnectionPoolSize);
        xaConnectionManager.initialisePool();
        this.xaConnectionManager = xaConnectionManager;
    }

    @SuppressWarnings("unused")
    public static H2ConnectionManager getInstance()
    {
        return INSTANCE;
    }

    @Override
    public BulkLoader createBulkLoader()
    {
        throw new RuntimeException("BulkLoader is not supported");
    }

    @Override
    public Connection getConnection()
    {
        return this.xaConnectionManager.getConnection();
    }

    @Override
    public DatabaseType getDatabaseType()
    {
        return H2DatabaseType.getInstance();
    }

    @Override
    public TimeZone getDatabaseTimeZone()
    {
        return this.databaseTimeZone;
    }

    @Override
    public String getDatabaseIdentifier()
    {
        return this.schemaName;
    }
}
