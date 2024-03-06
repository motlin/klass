package cool.klass.reladomo.ddl.executor;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.regex.Pattern;

import com.gs.fw.common.mithra.connectionmanager.SourcelessConnectionManager;
import cool.klass.reladomo.connectionmanager.h2.H2ConnectionManager;
import org.eclipse.collections.api.set.MutableSet;
import org.eclipse.collections.impl.set.mutable.SetAdapter;
import org.h2.tools.RunScript;
import org.reflections.Reflections;
import org.reflections.scanners.ResourcesScanner;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class DatabaseDdlExecutor
{
    private static final Logger LOGGER = LoggerFactory.getLogger(DatabaseDdlExecutor.class);

    private DatabaseDdlExecutor()
    {
        throw new AssertionError("Suppress default constructor for noninstantiability");
    }

    public static void main(String[] args)
    {
        DatabaseDdlExecutor.executeSql(H2ConnectionManager.getInstance());
    }

    public static void executeSql(SourcelessConnectionManager connectionManager)
    {
        ConfigurationBuilder configurationBuilder = new ConfigurationBuilder()
                .setScanners(new ResourcesScanner())
                .setUrls(ClasspathHelper.forJavaClassPath());
        Reflections        reflections  = new Reflections(configurationBuilder);
        MutableSet<String> ddlLocations = SetAdapter.adapt(reflections.getResources(Pattern.compile(".*\\.ddl")));
        MutableSet<String> idxLocations = SetAdapter.adapt(reflections.getResources(Pattern.compile(".*\\.idx")));

        Connection connection = connectionManager.getConnection();
        ddlLocations.forEachWith(DatabaseDdlExecutor::runScript, connection);
        idxLocations.forEachWith(DatabaseDdlExecutor::runScript, connection);
    }

    private static void runScript(String ddlLocation, Connection connection)
    {
        LOGGER.info("Running sql script {}.", ddlLocation);
        InputStream inputStream = DatabaseDdlExecutor.class.getResourceAsStream("/" + ddlLocation);
        if (inputStream == null)
        {
            String message = String.format("Could not find sql script '%s' on classpath.", ddlLocation);
            throw new RuntimeException(message);
        }

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream)))
        {
            RunScript.execute(connection, reader);
        }
        catch (IOException | SQLException e)
        {
            LOGGER.error("Failed to run sql script {}.", ddlLocation, e);
            throw new RuntimeException(e);
        }
    }
}
