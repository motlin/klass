package cool.klass.dropwizard.bundle.test.data;

import java.time.Instant;

import javax.annotation.Nonnull;

import com.google.auto.service.AutoService;
import cool.klass.data.store.DataStore;
import cool.klass.dropwizard.bundle.prioritized.PrioritizedBundle;
import cool.klass.dropwizard.configuration.AbstractKlassConfiguration;
import cool.klass.dropwizard.configuration.KlassFactory;
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
        implements PrioritizedBundle
{
    private static final Logger LOGGER = LoggerFactory.getLogger(SampleDataGeneratorBundle.class);

    @Override
    public int getPriority()
    {
        return -1;
    }

    @Override
    public void initialize(Bootstrap<?> bootstrap)
    {
    }

    @Override
    public void run(@Nonnull AbstractKlassConfiguration configuration, Environment environment)
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

        KlassFactory klassFactory = configuration.getKlassFactory();
        DataStore    dataStore    = klassFactory.getDataStoreFactory().getDataStore();
        DomainModel  domainModel  = klassFactory.getDomainModelFactory().getDomainModel();

        SampleDataGenerator sampleDataGenerator = new SampleDataGenerator(
                domainModel,
                dataStore,
                systemTime,
                skippedPackages);
        sampleDataGenerator.generate();
    }
}
