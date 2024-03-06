package cool.klass.dropwizard.bundle.test.data;

import java.time.Instant;
import java.util.Objects;

import com.google.auto.service.AutoService;
import cool.klass.data.store.DataStore;
import cool.klass.dropwizard.bundle.api.DataBundle;
import cool.klass.dropwizard.bundle.prioritized.PrioritizedBundle;
import cool.klass.dropwizard.configuration.AbstractKlassConfiguration;
import cool.klass.model.meta.domain.api.DomainModel;
import cool.klass.reladomo.sample.data.SampleDataGenerator;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import com.typesafe.config.ConfigRenderOptions;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import org.eclipse.collections.api.list.ImmutableList;
import org.eclipse.collections.impl.factory.Lists;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@AutoService(PrioritizedBundle.class)
public class SampleDataGeneratorBundle
        implements DataBundle
{
    private static final Logger LOGGER = LoggerFactory.getLogger(SampleDataGeneratorBundle.class);

    private DomainModel domainModel;

    @Override
    public int getPriority()
    {
        return -1;
    }

    @Override
    public void initialize(DomainModel domainModel)
    {
        this.domainModel = Objects.requireNonNull(domainModel);
    }

    @Override
    public void initialize(Bootstrap<?> bootstrap)
    {
    }

    @Override
    public void run(AbstractKlassConfiguration configuration, Environment environment)
    {
        Config config                    = ConfigFactory.load();
        Config sampleDataGeneratorConfig = config.getConfig("klass.data.generator.sample");

        if (LOGGER.isDebugEnabled())
        {
            ConfigRenderOptions configRenderOptions = ConfigRenderOptions.defaults()
                    .setJson(false)
                    .setOriginComments(false);
            String render = sampleDataGeneratorConfig.root().render(configRenderOptions);
            LOGGER.debug("Sample Data Generator Bundle configuration:\n{}", render);
        }

        boolean enabled = sampleDataGeneratorConfig.getBoolean("enabled");
        if (!enabled)
        {
            return;
        }

        String  systemTimeString = sampleDataGeneratorConfig.getString("dataSystemTime");
        Instant systemTime       = Instant.parse(systemTimeString);

        ImmutableList<String> skippedPackages = Lists.immutable.withAll(sampleDataGeneratorConfig.getStringList(
                "skippedPackages"));

        DataStore dataStore = configuration
                .getKlassFactory()
                .getDataStoreFactory()
                .getDataStore();

        SampleDataGenerator sampleDataGenerator = new SampleDataGenerator(
                this.domainModel,
                dataStore,
                systemTime,
                skippedPackages);
        sampleDataGenerator.generate();
    }
}
