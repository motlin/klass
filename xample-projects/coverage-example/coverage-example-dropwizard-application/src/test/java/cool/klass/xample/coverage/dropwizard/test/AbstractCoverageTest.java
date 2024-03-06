package cool.klass.xample.coverage.dropwizard.test;

import java.time.Clock;
import java.time.Instant;

import javax.annotation.Nonnull;
import javax.ws.rs.client.Client;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import cool.klass.data.store.DataStore;
import cool.klass.dropwizard.configuration.KlassFactory;
import cool.klass.model.meta.domain.api.DomainModel;
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
            "reladomo-runtime-configuration/TestReladomoRuntimeConfiguration.xml");

    @Before
    public void setUpSampleData()
    {
        KlassFactory klassFactory = RULE.getConfiguration().getKlassFactory();
        DataStore    dataStore    = klassFactory.getDataStoreFactory().getDataStore();
        Clock        clock        = klassFactory.getClockFactory().getClock();
        DomainModel  domainModel  = klassFactory.getDomainModelFactory().getDomainModel();
        Instant      now          = Instant.now(clock);

        SampleDataGenerator sampleDataGenerator = new SampleDataGenerator(
                domainModel,
                dataStore,
                now,
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
