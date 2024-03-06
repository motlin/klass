package cool.klass.dropwizard.configuration.factory;

import javax.validation.Validator;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.dropwizard.configuration.ConfigurationFactory;
import io.dropwizard.configuration.ConfigurationFactoryFactory;
import io.dropwizard.configuration.JsonConfigurationFactory;

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
        return strictObjectMapper;
    }
}
