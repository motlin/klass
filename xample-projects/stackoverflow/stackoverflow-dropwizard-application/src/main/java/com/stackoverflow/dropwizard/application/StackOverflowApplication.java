package com.stackoverflow.dropwizard.application;

import java.util.ServiceLoader;

import javax.annotation.Nonnull;

import cool.klass.dropwizard.bundle.test.data.SampleDataGeneratorBundle;
import cool.klass.model.converter.bootstrap.writer.KlassBootstrapWriter;
import com.stackoverflow.service.resource.QuestionResourceManual;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import com.typesafe.config.ConfigRenderOptions;
import io.dropwizard.Bundle;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class StackOverflowApplication extends AbstractStackOverflowApplication
{
    private static final Logger LOGGER = LoggerFactory.getLogger(StackOverflowApplication.class);

    public static void main(String[] args) throws Exception
    {
        new StackOverflowApplication().run(args);
    }

    @Override
    public void initialize(@Nonnull Bootstrap<StackOverflowConfiguration> bootstrap)
    {
        super.initialize(bootstrap);

        ServiceLoader<Bundle> bundleServiceLoader = ServiceLoader.load(Bundle.class);
        for (Bundle bundle : bundleServiceLoader)
        {
            bootstrap.addBundle(bundle);
        }

        bootstrap.addBundle(new SampleDataGeneratorBundle(this.domainModel, this.dataStore));

        // TODO: application initialization
    }

    @Override
    public void run(
            StackOverflowConfiguration configuration,
            @Nonnull Environment environment)
    {
        super.run(configuration, environment);

        Config config = ConfigFactory.load();
        Config klassConfig  = config.getConfig("klass");
        ConfigRenderOptions configRenderOptions = ConfigRenderOptions.defaults()
                .setJson(false)
                .setOriginComments(false);
        String render = klassConfig.root().render(configRenderOptions);
        LOGGER.info("Klass HOCON configuration:\n{}", render);

        environment.jersey().register(new QuestionResourceManual(this.dataStore));

        // TODO: Move up to generated superclass
        KlassBootstrapWriter klassBootstrapWriter = new KlassBootstrapWriter(this.domainModel);
        klassBootstrapWriter.bootstrapMetaModel();
    }
}
