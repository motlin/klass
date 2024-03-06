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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class ReladomoConfig
{
    private static final Logger LOGGER = LoggerFactory.getLogger(ReladomoConfig.class);

    private ReladomoConfig()
    {
        throw new AssertionError("Suppress default constructor for noninstantiability");
    }

    public static <T> void addSerializer(
            @Nonnull ObjectMapper objectMapper,
            @Nonnull Class<T> klass,
            @Nonnull JsonSerializer<T> jsonSerializer)
    {
        SimpleModule module = new SimpleModule();
        module.addSerializer(klass, jsonSerializer);
        objectMapper.registerModule(module);
    }

    public static void setTransactionTimeout(int transactionTimeoutSeconds)
    {
        MithraManager mithraManager = MithraManagerProvider.getMithraManager();
        mithraManager.setTransactionTimeout(transactionTimeoutSeconds);
    }

    public static void loadRuntimeConfigurations(@Nonnull List<String> reladomoRuntimeConfigurationPaths)
    {
        reladomoRuntimeConfigurationPaths.forEach(ReladomoConfig::loadRuntimeConfiguration);
    }

    private static void loadRuntimeConfiguration(String reladomoRuntimeConfigurationPath)
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
