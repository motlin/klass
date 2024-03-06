package cool.klass.reladomo.configuration;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import com.gs.fw.common.mithra.MithraManager;
import com.gs.fw.common.mithra.MithraManagerProvider;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import com.typesafe.config.ConfigRenderOptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class ReladomoConfig
{
    private static final Logger LOGGER = LoggerFactory.getLogger(ReladomoConfig.class);

    private ReladomoConfig()
    {
        throw new AssertionError("Suppress default constructor for noninstantiability");
    }

    public static void configure()
    {
        Config config         = ConfigFactory.load();
        Config reladomoConfig = config.getConfig("klass.data.reladomo");

        int          transactionTimeoutSeconds = reladomoConfig.getInt("transactionTimeoutSeconds");
        List<String> reladomoRuntimeConfigurationPaths = reladomoConfig.getStringList("reladomoRuntimeConfigurationPaths");

        if (LOGGER.isDebugEnabled())
        {
            ConfigRenderOptions configRenderOptions = ConfigRenderOptions.defaults()
                    .setJson(false)
                    .setOriginComments(false);
            String render = reladomoConfig.root().render(configRenderOptions);
            LOGGER.debug("Reladomo configuration:\n{}", render);
        }

        MithraManager mithraManager = MithraManagerProvider.getMithraManager();
        mithraManager.setTransactionTimeout(transactionTimeoutSeconds);
        // Notification should be configured here. Refer to notification/Notification.html under reladomo-javadoc.jar.

        reladomoRuntimeConfigurationPaths.forEach(ReladomoConfig::loadReladomoRuntimeConfigurationPath);
    }

    private static void loadReladomoRuntimeConfigurationPath(String reladomoRuntimeConfigurationPath)
    {
        LOGGER.info("Loading Reladomo configuration XML: {}", reladomoRuntimeConfigurationPath);
        try (InputStream inputStream = ReladomoConfig.class.getClassLoader()
                .getResourceAsStream(reladomoRuntimeConfigurationPath))
        {
            MithraManagerProvider.getMithraManager().readConfiguration(inputStream);
        }
        catch (IOException e)
        {
            throw new RuntimeException(e);
        }
    }
}
