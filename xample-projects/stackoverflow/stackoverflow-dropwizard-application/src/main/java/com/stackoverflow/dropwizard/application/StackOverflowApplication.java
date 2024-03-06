package com.stackoverflow.dropwizard.application;

import java.io.IOException;
import java.io.InputStream;

import com.gs.fw.common.mithra.MithraManager;
import com.gs.fw.common.mithra.MithraManagerProvider;
import com.stackoverflow.dropwizard.command.StackOverflowTestDataGeneratorCommand;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class StackOverflowApplication extends AbstractStackOverflowApplication
{
    private static final Logger LOGGER = LoggerFactory.getLogger(StackOverflowApplication.class);

    private static final int MAX_TRANSACTION_TIMEOUT_SECONDS = 120;

    public static void main(String[] args) throws Exception
    {
        new StackOverflowApplication().run(args);
    }

    @Override
    public void initialize(Bootstrap<StackOverflowConfiguration> bootstrap)
    {
        super.initialize(bootstrap);

        bootstrap.addCommand(new StackOverflowTestDataGeneratorCommand());

        // TODO: application initialization
        this.initializeReladomo();
        // TODO: Move this file to a subdirectory, and code generate it
        this.loadReladomoConfigurationXml("ReladomoRuntimeConfig.xml");
    }

    @Override
    public void run(
            StackOverflowConfiguration configuration,
            Environment environment)
    {
        super.run(configuration, environment);
    }

    private void initializeReladomo()
    {
        try
        {
            LOGGER.info("Transaction Timeout is {}", MAX_TRANSACTION_TIMEOUT_SECONDS);
            MithraManager mithraManager = MithraManagerProvider.getMithraManager();
            mithraManager.setTransactionTimeout(MAX_TRANSACTION_TIMEOUT_SECONDS);
            // Notification should be configured here. Refer to notification/Notification.html under reladomo-javadoc.jar.
        }
        catch (RuntimeException e)
        {
            LOGGER.error("Unable to initialize Reladomo!", e);
            throw e;
        }
        LOGGER.info("Reladomo has been initialized!");
    }

    private void loadReladomoConfigurationXml(String reladomoRuntimeConfig)
    {
        LOGGER.info("Reladomo configuration XML is {}", reladomoRuntimeConfig);
        try (InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream(reladomoRuntimeConfig))
        {
            MithraManagerProvider.getMithraManager().readConfiguration(inputStream);
        }
        catch (IOException e)
        {
            LOGGER.error("Unable to initialize Reladomo!", e);
            throw new RuntimeException("Unable to initialize Reladomo!", e);
        }
        LOGGER.info("Reladomo configuration XML {} is now loaded.", reladomoRuntimeConfig);
    }
}
