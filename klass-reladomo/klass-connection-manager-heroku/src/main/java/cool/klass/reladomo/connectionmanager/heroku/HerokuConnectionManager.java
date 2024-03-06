package cool.klass.reladomo.connectionmanager.heroku;

import java.sql.Connection;
import java.util.TimeZone;

import javax.annotation.Nonnull;

import com.gs.fw.common.mithra.bulkloader.BulkLoader;
import com.gs.fw.common.mithra.connectionmanager.SourcelessConnectionManager;
import com.gs.fw.common.mithra.connectionmanager.XAConnectionManager;
import com.gs.fw.common.mithra.databasetype.DatabaseType;
import com.gs.fw.common.mithra.databasetype.PostgresDatabaseType;

public final class HerokuConnectionManager implements SourcelessConnectionManager
{
    private static final HerokuConnectionManager INSTANCE = new HerokuConnectionManager();

    @Nonnull
    private final XAConnectionManager xaConnectionManager;
    private final TimeZone            databaseTimeZone;
    private final String              schemaName;

    private HerokuConnectionManager()
    {
        this.schemaName       = "klass-app";
        this.databaseTimeZone = TimeZone.getTimeZone("UTC");
        XAConnectionManager xaConnectionManager = new XAConnectionManager();
        xaConnectionManager.setDriverClassName("org.postgresql.Driver");
        xaConnectionManager.setMaxWait(500);
        xaConnectionManager.setJdbcConnectionString(System.getenv("JDBC_DATABASE_URL"));
        xaConnectionManager.setJdbcUser(System.getenv("JDBC_DATABASE_USERNAME"));
        xaConnectionManager.setJdbcPassword(System.getenv("JDBC_DATABASE_PASSWORD"));
        xaConnectionManager.setPoolName("Reladomo default connection pool");
        xaConnectionManager.setInitialSize(1);
        xaConnectionManager.setPoolSize(10);
        xaConnectionManager.initialisePool();
        this.xaConnectionManager = xaConnectionManager;
    }

    @Nonnull
    @SuppressWarnings("unused")
    public static HerokuConnectionManager getInstance()
    {
        return INSTANCE;
    }

    @Nonnull
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
        return PostgresDatabaseType.getInstance();
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
