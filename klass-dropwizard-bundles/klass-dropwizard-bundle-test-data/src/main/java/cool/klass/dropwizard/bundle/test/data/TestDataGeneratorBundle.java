package cool.klass.dropwizard.bundle.test.data;

import java.time.Instant;
import java.util.Objects;

import cool.klass.data.store.DataStore;
import cool.klass.model.meta.domain.api.DomainModel;
import cool.klass.reladomo.test.data.ReladomoTestDataGenerator;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import com.typesafe.config.ConfigRenderOptions;
import io.dropwizard.Bundle;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TestDataGeneratorBundle implements Bundle
{
    private static final Logger LOGGER = LoggerFactory.getLogger(TestDataGeneratorBundle.class);

    private final DomainModel domainModel;
    private final DataStore   dataStore;

    public TestDataGeneratorBundle(DomainModel domainModel, DataStore dataStore)
    {
        this.domainModel = Objects.requireNonNull(domainModel);
        this.dataStore = Objects.requireNonNull(dataStore);
    }

    @Override
    public void initialize(Bootstrap<?> bootstrap)
    {
    }

    @Override
    public void run(Environment environment)
    {
        Config config                  = ConfigFactory.load();
        Config testDataGeneratorConfig = config.getConfig("klass.data.generator.test");

        if (LOGGER.isInfoEnabled())
        {
            ConfigRenderOptions configRenderOptions = ConfigRenderOptions.defaults()
                    .setJson(false)
                    .setOriginComments(false);
            String render = testDataGeneratorConfig.root().render(configRenderOptions);
            LOGGER.info("Test Data Generator Bundle configuration:\n{}", render);
        }

        boolean enabled          = testDataGeneratorConfig.getBoolean("enabled");
        String  systemTimeString = testDataGeneratorConfig.getString("dataSystemTime");
        Instant systemTime       = Instant.parse(systemTimeString);
        if (enabled)
        {
            ReladomoTestDataGenerator reladomoTestDataGenerator = new ReladomoTestDataGenerator(
                    this.domainModel,
                    this.dataStore,
                    systemTime);
            reladomoTestDataGenerator.generate();
        }
    }
}
