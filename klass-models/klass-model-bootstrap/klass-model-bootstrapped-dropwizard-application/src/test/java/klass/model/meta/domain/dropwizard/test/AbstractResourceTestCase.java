package klass.model.meta.domain.dropwizard.test;

import javax.annotation.Nonnull;
import javax.ws.rs.client.Client;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import io.dropwizard.client.JerseyClientBuilder;
import io.dropwizard.client.JerseyClientConfiguration;
import io.dropwizard.testing.ResourceHelpers;
import io.dropwizard.testing.junit.DropwizardAppRule;
import io.dropwizard.util.Duration;
import io.liftwizard.junit.rule.log.marker.LogMarkerTestRule;
import io.liftwizard.junit.rule.match.json.JsonMatchRule;
import io.liftwizard.reladomo.test.rule.ReladomoLoadDataTestRule;
import klass.model.meta.domain.dropwizard.application.KlassBootstrappedMetaModelApplication;
import klass.model.meta.domain.dropwizard.application.KlassBootstrappedMetaModelConfiguration;
import org.junit.Rule;
import org.junit.rules.RuleChain;
import org.junit.rules.TestRule;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public abstract class AbstractResourceTestCase
{
    @Rule
    public final JsonMatchRule jsonMatchRule = new JsonMatchRule(this.getClass());

    protected final DropwizardAppRule<KlassBootstrappedMetaModelConfiguration> appRule = new DropwizardAppRule<>(
            KlassBootstrappedMetaModelApplication.class,
            ResourceHelpers.resourceFilePath("config-test.json5"));

    protected final TestRule reladomoLoadDataTestRule = new ReladomoLoadDataTestRule();
    protected final TestRule logMarkerTestRule = new LogMarkerTestRule();

    @Rule
    public final TestRule rule = RuleChain
            .outerRule(this.appRule)
            .around(this.reladomoLoadDataTestRule)
            .around(this.logMarkerTestRule);

    protected Client getClient(@Nonnull String testName)
    {
        var jerseyClientConfiguration = new JerseyClientConfiguration();
        jerseyClientConfiguration.setTimeout(Duration.minutes(5));

        String className = this.getClass().getCanonicalName();
        String clientName = className + "." + testName;

        return new JerseyClientBuilder(this.appRule.getEnvironment())
                .using(jerseyClientConfiguration)
                .build(clientName);
    }

    protected void assertUrlReturns(@Nonnull String testName, @Nonnull String url)
    {
        Class<?> klass      = this.getClass();
        String   clientName = klass.getPackage().getName() + '.' + klass.getSimpleName() + '.' + testName;
        Client   client     = this.getClient(clientName);
        Response response = client
                .target("http://localhost:{port}/api/" + url)
                .resolveTemplate("port", this.appRule.getLocalPort())
                .request()
                .get();

        this.assertResponseStatus(response, Status.OK);
        String jsonResponse = response.readEntity(String.class);

        String resourceClassPathLocation = klass.getSimpleName() + '.' + testName + ".json";
        this.jsonMatchRule.assertFileContents(resourceClassPathLocation, jsonResponse);
    }

    protected void assertResponseStatus(@Nonnull Response response, Status status)
    {
        response.bufferEntity();
        String entityAsString = response.readEntity(String.class);
        assertThat(entityAsString, response.getStatusInfo(), is(status));
    }
}
