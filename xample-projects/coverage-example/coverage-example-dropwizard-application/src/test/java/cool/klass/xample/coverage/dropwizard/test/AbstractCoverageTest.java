package cool.klass.xample.coverage.dropwizard.test;

import java.time.Instant;
import java.util.concurrent.TimeUnit;

import javax.annotation.Nonnull;
import javax.ws.rs.client.Client;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import cool.klass.reladomo.sample.data.SampleDataGenerator;
import cool.klass.reladomo.test.rule.ReladomoTestRule;
import cool.klass.xample.coverage.dropwizard.application.CoverageExampleApplication;
import cool.klass.xample.coverage.dropwizard.application.CoverageExampleConfiguration;
import io.dropwizard.client.JerseyClientBuilder;
import io.dropwizard.client.JerseyClientConfiguration;
import io.dropwizard.testing.ResourceHelpers;
import io.dropwizard.testing.junit.DropwizardAppRule;
import io.dropwizard.util.Duration;
import org.eclipse.collections.impl.factory.Lists;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Rule;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class AbstractCoverageTest
{
    @ClassRule
    public static final DropwizardAppRule<CoverageExampleConfiguration> RULE = new DropwizardAppRule<>(
            CoverageExampleApplication.class,
            ResourceHelpers.resourceFilePath("config-test.yml"));

    @Rule
    public final ReladomoTestRule reladomoTestRule = new ReladomoTestRule(
            "reladomo-runtime-configuration/TestReladomoRuntimeConfiguration.xml")
            .transactionTimeout(5, TimeUnit.MINUTES);

    protected final Instant now = Instant.now();

    @Before
    public void setUpSampleData()
    {
        CoverageExampleApplication application = RULE.getApplication();
        SampleDataGenerator sampleDataGenerator = new SampleDataGenerator(
                application.getDomainModel(),
                application.getDataStore(),
                this.now,
                Lists.immutable.empty());
        sampleDataGenerator.generate();
    }

    protected Client getClient(String clientName)
    {
        JerseyClientConfiguration jerseyClientConfiguration = new JerseyClientConfiguration();
        jerseyClientConfiguration.setTimeout(Duration.minutes(5));

        return new JerseyClientBuilder(RULE.getEnvironment())
                .using(jerseyClientConfiguration)
                .build(clientName);
    }

    protected void assertResponseStatus(@Nonnull Response response, Status status)
    {
        response.bufferEntity();
        String entityAsString = response.readEntity(String.class);
        assertThat(entityAsString, response.getStatusInfo(), is(status));
    }
}
