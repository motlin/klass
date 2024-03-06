package cool.klass.xample.coverage.dropwizard.test;

import javax.annotation.Nonnull;
import javax.ws.rs.client.Client;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import cool.klass.xample.coverage.dropwizard.application.CoverageExampleApplication;
import cool.klass.xample.coverage.dropwizard.application.CoverageExampleConfiguration;
import io.dropwizard.client.JerseyClientBuilder;
import io.dropwizard.client.JerseyClientConfiguration;
import io.dropwizard.testing.ResourceHelpers;
import io.dropwizard.testing.junit.DropwizardAppRule;
import io.dropwizard.util.Duration;
import io.liftwizard.junit.rule.log.marker.LogMarkerTestRule;
import org.junit.Rule;
import org.junit.rules.RuleChain;
import org.junit.rules.TestRule;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class AbstractCoverageTest
{
    protected final DropwizardAppRule<CoverageExampleConfiguration> appRule = new DropwizardAppRule<>(
            CoverageExampleApplication.class,
            ResourceHelpers.resourceFilePath("config-test.json5"));

    protected final TestRule logMarkerTestRule = new LogMarkerTestRule();

    @Rule
    public final TestRule rule = RuleChain
            .outerRule(this.appRule)
            .around(this.logMarkerTestRule);

    protected Client getClient(String clientName)
    {
        var jerseyClientConfiguration = new JerseyClientConfiguration();
        jerseyClientConfiguration.setTimeout(Duration.minutes(5));

        return new JerseyClientBuilder(this.appRule.getEnvironment())
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
