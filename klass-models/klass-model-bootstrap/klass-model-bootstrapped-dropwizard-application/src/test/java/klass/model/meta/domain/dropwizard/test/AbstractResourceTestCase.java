package klass.model.meta.domain.dropwizard.test;

import java.util.List;

import javax.annotation.Nonnull;
import javax.ws.rs.client.Client;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import io.dropwizard.client.JerseyClientBuilder;
import io.dropwizard.client.JerseyClientConfiguration;
import io.dropwizard.testing.ResourceHelpers;
import io.dropwizard.testing.junit.DropwizardAppRule;
import io.dropwizard.util.Duration;
import io.liftwizard.dropwizard.configuration.reladomo.ReladomoFactory;
import io.liftwizard.junit.rule.log.marker.LogMarkerTestRule;
import io.liftwizard.junit.rule.match.json.JsonMatchRule;
import klass.model.meta.domain.dropwizard.application.KlassBootstrappedMetaModelApplication;
import klass.model.meta.domain.dropwizard.application.KlassBootstrappedMetaModelConfiguration;
import org.eclipse.collections.impl.utility.Iterate;
import org.junit.Rule;
import org.junit.rules.TestRule;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class AbstractResourceTestCase
{
    @Rule
    public final TestRule logMarkerTestRule = new LogMarkerTestRule();

    @Rule
    public final DropwizardAppRule<KlassBootstrappedMetaModelConfiguration> appRule = new DropwizardAppRule<>(
            KlassBootstrappedMetaModelApplication.class,
            ResourceHelpers.resourceFilePath("config-test.json5"));

    @Rule
    public final JsonMatchRule jsonMatchRule = new JsonMatchRule();

    protected Client getClient(@Nonnull String clientName)
    {
        JerseyClientConfiguration jerseyClientConfiguration = new JerseyClientConfiguration();
        jerseyClientConfiguration.setTimeout(Duration.minutes(5));

        return new JerseyClientBuilder(this.appRule.getEnvironment())
                .using(jerseyClientConfiguration)
                .build(clientName);
    }

    protected void assertUrlReturns(@Nonnull String testName, @Nonnull String url)
    {
        Class<?> klass      = this.getClass();
        String   clientName = klass.getPackage().getName() + '.' + klass.getSimpleName() + '.' + testName;
        Client   client     = this.getClient(clientName);
        String   uriString  = "http://localhost:" + this.appRule.getLocalPort() + "/api" + url;
        Response response   = client.target(uriString).request().get();

        this.assertResponseStatus(response, Status.OK);
        String jsonResponse = response.readEntity(String.class);

        String resourceClassPathLocation = klass.getSimpleName() + '.' + testName + ".json";
        this.jsonMatchRule.assertFileContents(resourceClassPathLocation, jsonResponse, klass);
    }

    private void assertResponseStatus(@Nonnull Response response, @Nonnull Status status)
    {
        response.bufferEntity();
        String entityAsString = response.readEntity(String.class);
        assertThat(entityAsString, response.getStatusInfo(), is(status));
    }

    private String getRuntimeConfigFilename()
    {
        ReladomoFactory reladomoFactory           = this.appRule.getConfiguration().getReladomoFactory();
        List<String>    runtimeConfigurationPaths = reladomoFactory.getRuntimeConfigurationPaths();
        return Iterate.getOnly(runtimeConfigurationPaths);
    }
}
