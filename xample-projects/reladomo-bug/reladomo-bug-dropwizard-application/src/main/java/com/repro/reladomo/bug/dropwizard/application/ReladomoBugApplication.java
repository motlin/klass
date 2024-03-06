package com.repro.reladomo.bug.dropwizard.application;

import java.util.ServiceLoader;

import javax.annotation.Nonnull;

import cool.klass.dropwizard.bundle.bootstrap.writer.BootstrapWriterBundle;
import cool.klass.dropwizard.bundle.test.data.SampleDataGeneratorBundle;
import com.repro.reladomo.bug.ChapterWithQuote;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import com.typesafe.config.ConfigRenderOptions;
import io.dropwizard.Bundle;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ReladomoBugApplication extends AbstractReladomoBugApplication
{
    private static final Logger LOGGER = LoggerFactory.getLogger(ReladomoBugApplication.class);

    public static void main(String[] args) throws Exception
    {
        new ReladomoBugApplication().run(args);
    }

    @Override
    public void initialize(@Nonnull Bootstrap<ReladomoBugConfiguration> bootstrap)
    {
        super.initialize(bootstrap);

        ServiceLoader<Bundle> bundleServiceLoader = ServiceLoader.load(Bundle.class);
        for (Bundle bundle : bundleServiceLoader)
        {
            bootstrap.addBundle(bundle);
        }

        // TODO: Move up?
        bootstrap.addBundle(new SampleDataGeneratorBundle(this.domainModel, this.dataStore));
        bootstrap.addBundle(new BootstrapWriterBundle(this.domainModel, this.dataStore));

        // TODO: application initialization
    }

    @Override
    public void run(
            ReladomoBugConfiguration configuration,
            @Nonnull Environment environment)
    {
        super.run(configuration, environment);

        Config config      = ConfigFactory.load();
        Config klassConfig = config.getConfig("klass");
        ConfigRenderOptions configRenderOptions = ConfigRenderOptions.defaults()
                .setJson(false)
                .setOriginComments(false);
        String render = klassConfig.root().render(configRenderOptions);
        LOGGER.info("Klass HOCON configuration:\n{}", render);

        ChapterWithQuote chapterWithQuote = new ChapterWithQuote();
        chapterWithQuote.setText("chapter text");
    }
}
