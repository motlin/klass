package cool.klass.dropwizard.bundle.reladomo;

import java.util.List;

import javax.annotation.Nonnull;

import com.google.auto.service.AutoService;
import com.gs.fw.common.mithra.MithraManagerProvider;
import cool.klass.dropwizard.bundle.prioritized.PrioritizedBundle;
import cool.klass.dropwizard.configuration.reladomo.ReladomoFactory;
import cool.klass.dropwizard.configuration.reladomo.ReladomoFactoryProvider;
import cool.klass.reladomo.configuration.ReladomoConfig;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import io.dropwizard.util.Duration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@AutoService(PrioritizedBundle.class)
public class ReladomoBundle implements PrioritizedBundle<ReladomoFactoryProvider>
{
    private static final Logger LOGGER = LoggerFactory.getLogger(ReladomoBundle.class);

    @Override
    public int getPriority()
    {
        return -3;
    }

    @Override
    public void initialize(Bootstrap<?> bootstrap)
    {
    }

    @Override
    public void run(@Nonnull ReladomoFactoryProvider configuration, @Nonnull Environment environment)
    {
        LOGGER.info("Running {}.", ReladomoBundle.class.getSimpleName());

        ReladomoFactory reladomoFactory = configuration.getReladomoFactory();

        Duration     transactionTimeout                     = reladomoFactory.getTransactionTimeout();
        int          transactionTimeoutSeconds              = Math.toIntExact(transactionTimeout.toSeconds());
        List<String> runtimeConfigurationPaths              = reladomoFactory.getRuntimeConfigurationPaths();
        boolean      enableRetrieveCountMetrics             = reladomoFactory.isEnableRetrieveCountMetrics();
        boolean      captureTransactionLevelPerformanceData = reladomoFactory.isCaptureTransactionLevelPerformanceData();

        ReladomoConfig.setTransactionTimeout(transactionTimeoutSeconds);
        // Notification should be configured here. Refer to notification/Notification.html under reladomo-javadoc.jar.
        ReladomoConfig.loadRuntimeConfigurations(runtimeConfigurationPaths);

        ReladomoConfig.setCaptureTransactionLevelPerformanceData(captureTransactionLevelPerformanceData);

        if (enableRetrieveCountMetrics)
        {
            ReladomoBundle.registerRetrieveCountMetrics(environment);
        }

        LOGGER.info("Completing {}.", ReladomoBundle.class.getSimpleName());
    }

    private static void registerRetrieveCountMetrics(@Nonnull Environment environment)
    {
        environment.metrics().gauge(
                "Reladomo database retrieve count",
                () -> () -> MithraManagerProvider.getMithraManager().getDatabaseRetrieveCount());
        environment.metrics().gauge(
                "Reladomo remote retrieve count",
                () -> () -> MithraManagerProvider.getMithraManager().getRemoteRetrieveCount());
    }
}
