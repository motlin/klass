package cool.klass.dropwizard.configuration.factory;

import javax.validation.Validator;

import com.fasterxml.jackson.core.JsonParser.Feature;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.util.StdDateFormat;
import io.dropwizard.configuration.ConfigurationFactory;
import io.dropwizard.configuration.ConfigurationFactoryFactory;
import io.dropwizard.configuration.JsonConfigurationFactory;

/**
 * Allows configuring Dropwizard using json. Based on {@link io.dropwizard.configuration.DefaultConfigurationFactoryFactory} but replacing yml with json.
 *
 * @see io.dropwizard.configuration.DefaultConfigurationFactoryFactory
 */
public class JsonConfigurationFactoryFactory<T> implements ConfigurationFactoryFactory<T>
{
    @Override
    public ConfigurationFactory<T> create(
            Class<T> klass,
            Validator validator,
            ObjectMapper objectMapper,
            String propertyPrefix)
    {
        ObjectMapper strictObjectMapper = this.getStrictObjectMapper(objectMapper);
        return new JsonConfigurationFactory<>(
                klass,
                validator,
                strictObjectMapper,
                propertyPrefix);
    }

    private ObjectMapper getStrictObjectMapper(ObjectMapper objectMapper)
    {
        ObjectMapper strictObjectMapper = objectMapper.copy();
        strictObjectMapper.enable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);

        strictObjectMapper.disable(DeserializationFeature.READ_DATE_TIMESTAMPS_AS_NANOSECONDS);

        strictObjectMapper.configure(Feature.ALLOW_COMMENTS, true);
        strictObjectMapper.configure(Feature.ALLOW_YAML_COMMENTS, true);
        strictObjectMapper.configure(Feature.ALLOW_UNQUOTED_CONTROL_CHARS, true);
        strictObjectMapper.configure(Feature.ALLOW_TRAILING_COMMA, true);

        strictObjectMapper.setDateFormat(new StdDateFormat());

        return strictObjectMapper;
    }
}
