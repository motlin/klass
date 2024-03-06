package cool.klass.reladomo.configuration;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import javax.annotation.Nonnull;

import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.gs.fw.common.mithra.MithraManager;
import com.gs.fw.common.mithra.MithraManagerProvider;
import com.gs.fw.common.mithra.MithraObject;
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

    public static void configure(@Nonnull ObjectMapper objectMapper, JsonSerializer<MithraObject> jsonSerializer)
    {
        Config config         = ConfigFactory.load();
        Config reladomoConfig = config.getConfig("klass.data.reladomo");

        if (LOGGER.isDebugEnabled())
        {
            ConfigRenderOptions configRenderOptions = ConfigRenderOptions.defaults()
                    .setJson(false)
                    .setOriginComments(false);
            String render = reladomoConfig.root().render(configRenderOptions);
            LOGGER.debug("Reladomo configuration:\n{}", render);
        }

        SimpleModule module = new SimpleModule();
        module.addSerializer(MithraObject.class, jsonSerializer);
        objectMapper.registerModule(module);

        int transactionTimeoutSeconds =
                reladomoConfig.getInt("transactionTimeoutSeconds");
        MithraManager mithraManager = MithraManagerProvider.getMithraManager();
        mithraManager.setTransactionTimeout(transactionTimeoutSeconds);

        // Notification should be configured here. Refer to notification/Notification.html under reladomo-javadoc.jar.

        // Rename enabled, since it's really only being used for the xml configuration mechanism vs MithraTestResource
        boolean enabled = reladomoConfig.getBoolean("enabled");
        if (!enabled)
        {
            return;
        }

        List<String> reladomoRuntimeConfigurationPaths =
                reladomoConfig.getStringList("reladomoRuntimeConfigurationPaths");
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
