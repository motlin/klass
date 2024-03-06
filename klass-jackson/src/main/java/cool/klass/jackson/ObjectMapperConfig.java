package cool.klass.jackson;

import javax.annotation.Nonnull;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.PrettyPrinter;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.util.StdDateFormat;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import com.typesafe.config.ConfigRenderOptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class ObjectMapperConfig
{
    private static final Logger LOGGER = LoggerFactory.getLogger(ObjectMapperConfig.class);

    private static final PrettyPrinter DEFAULT_PRETTY_PRINTER = new JsonPrettyPrinter();

    private ObjectMapperConfig()
    {
        throw new AssertionError("Suppress default constructor for noninstantiability");
    }

    public static void configure(@Nonnull ObjectMapper objectMapper)
    {
        Config config             = ConfigFactory.load();
        Config objectMapperConfig = config.getConfig("jackson.objectmapper");

        if (LOGGER.isDebugEnabled())
        {
            ConfigRenderOptions configRenderOptions = ConfigRenderOptions.defaults()
                    .setJson(false)
                    .setOriginComments(false);
            String render = objectMapperConfig.root().render(configRenderOptions);
            LOGGER.debug("ObjectMapper configuration:\n{}", render);
        }

        boolean prettyPrint            = objectMapperConfig.getBoolean("prettyPrint");
        String  serializationInclusion = objectMapperConfig.getString("serializationInclusion");
        Include include                = Include.valueOf(serializationInclusion);

        if (prettyPrint)
        {
            objectMapper.setDefaultPrettyPrinter(DEFAULT_PRETTY_PRINTER);
            objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
        }

        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        objectMapper.disable(SerializationFeature.WRITE_DURATIONS_AS_TIMESTAMPS);
        objectMapper.setDateFormat(new StdDateFormat());
        objectMapper.setSerializationInclusion(include);
    }
}
