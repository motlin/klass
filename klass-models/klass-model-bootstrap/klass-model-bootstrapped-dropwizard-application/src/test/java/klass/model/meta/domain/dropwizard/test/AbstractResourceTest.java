package klass.model.meta.domain.dropwizard.test;

import java.util.List;

import javax.annotation.Nonnull;
import javax.ws.rs.client.Client;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import cool.klass.dropwizard.configuration.reladomo.ReladomoFactory;
import cool.klass.junit.rule.match.json.JsonMatchRule;
import cool.klass.reladomo.test.rule.ReladomoTestRule;
import io.dropwizard.client.JerseyClientBuilder;
import io.dropwizard.client.JerseyClientConfiguration;
import io.dropwizard.testing.ResourceHelpers;
import io.dropwizard.testing.junit.DropwizardAppRule;
import io.dropwizard.util.Duration;
import klass.model.meta.domain.dropwizard.application.KlassBootstrappedMetaModelApplication;
import klass.model.meta.domain.dropwizard.application.KlassBootstrappedMetaModelConfiguration;
import org.eclipse.collections.impl.utility.Iterate;
import org.junit.ClassRule;
import org.junit.Rule;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class AbstractResourceTest
{
    @ClassRule
    public static final DropwizardAppRule<KlassBootstrappedMetaModelConfiguration> RULE = new DropwizardAppRule<>(
            KlassBootstrappedMetaModelApplication.class,
            ResourceHelpers.resourceFilePath("config-test.yml"));

    @Rule
    public final ReladomoTestRule reladomoTestRule = new ReladomoTestRule(this.getRuntimeConfigFilename());

    @Rule
    public final JsonMatchRule jsonMatchRule = new JsonMatchRule();

    protected Client getClient(@Nonnull String clientName)
    {
        JerseyClientConfiguration jerseyClientConfiguration = new JerseyClientConfiguration();
        jerseyClientConfiguration.setTimeout(Duration.minutes(5));

        return new JerseyClientBuilder(RULE.getEnvironment())
                .using(jerseyClientConfiguration)
                .build(clientName);
    }

    protected void assertUrlReturns(@Nonnull String testName, @Nonnull String url)
    {
        Class<?> klass      = this.getClass();
        String   clientName = klass.getPackage().getName() + '.' + klass.getSimpleName() + '.' + testName;
        Client   client     = this.getClient(clientName);
        String   uriString  = "http://localhost:" + RULE.getLocalPort() + "/api" + url;
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
        ReladomoFactory reladomoFactory           = RULE.getConfiguration().getReladomoFactory();
        List<String>    runtimeConfigurationPaths = reladomoFactory.getRuntimeConfigurationPaths();
        return Iterate.getOnly(runtimeConfigurationPaths);
    }
}
