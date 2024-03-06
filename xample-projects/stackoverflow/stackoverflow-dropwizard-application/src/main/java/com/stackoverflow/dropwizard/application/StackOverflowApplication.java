package com.stackoverflow.dropwizard.application;

import cool.klass.dropwizard.bundle.httplogging.HttpLoggingBundle;
import cool.klass.dropwizard.bundle.objectmapper.ObjectMapperBundle;
import cool.klass.dropwizard.bundle.reladomo.ReladomoBundle;
import cool.klass.model.meta.domain.DomainModel;
import cool.klass.model.meta.loader.DomainModelLoader;
import com.stackoverflow.dropwizard.command.StackOverflowTestDataGeneratorCommand;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import com.typesafe.config.ConfigRenderOptions;
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
    public void initialize(Bootstrap<StackOverflowConfiguration> bootstrap)
    {
        super.initialize(bootstrap);

        bootstrap.addCommand(new StackOverflowTestDataGeneratorCommand());

        bootstrap.addBundle(new HttpLoggingBundle());
        bootstrap.addBundle(new ObjectMapperBundle());
        bootstrap.addBundle(new ReladomoBundle());
    }

    @Override
    public void run(
            StackOverflowConfiguration configuration,
            Environment environment)
    {
        Config config      = ConfigFactory.load();
        Config klassConfig = config.getConfig("klass");

        if (LOGGER.isInfoEnabled())
        {
            ConfigRenderOptions configRenderOptions = ConfigRenderOptions.defaults()
                    .setJson(false)
                    .setOriginComments(false);
            String render = klassConfig.root().render(configRenderOptions);
            LOGGER.info("Klass configuration:\n{}", render);
        }

        String rootPackage = klassConfig.getString("rootPackage");

        DomainModelLoader domainModelLoader = new DomainModelLoader(rootPackage);
        DomainModel       domainModel       = domainModelLoader.load();

        this.run(configuration, environment, domainModel);
    }
}
