package cool.klass.xample.coverage.dropwizard.test;

import javax.annotation.Nonnull;
import javax.ws.rs.client.Client;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import cool.klass.xample.coverage.dropwizard.application.CoverageExampleApplication;
import cool.klass.xample.coverage.dropwizard.application.CoverageExampleConfiguration;
import io.dropwizard.client.JerseyClientBuilder;
import io.dropwizard.client.JerseyClientConfiguration;
import io.dropwizard.testing.ResourceHelpers;
import io.dropwizard.testing.junit.DropwizardAppRule;
import io.dropwizard.util.Duration;
import io.liftwizard.junit.rule.match.file.FileMatchRule;
import org.junit.Rule;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class SourceCodeResourceTest
{
    @Rule
    public final FileMatchRule fileMatchRule = new FileMatchRule(this.getClass());

    @Rule
    public final DropwizardAppRule<CoverageExampleConfiguration> rule = new DropwizardAppRule<>(
            CoverageExampleApplication.class,
            ResourceHelpers.resourceFilePath("config-test.json5"));

    protected Client getClient(String clientName)
    {
        var jerseyClientConfiguration = new JerseyClientConfiguration();
        jerseyClientConfiguration.setTimeout(Duration.minutes(5));

        return new JerseyClientBuilder(this.rule.getEnvironment())
                .using(jerseyClientConfiguration)
                .build(clientName);
    }

    @Test
    public void smoke_test()
    {
        Client client = this.getClient(SourceCodeResourceTest.class.getCanonicalName() + ".smoke_test");

        String uri = String.format(
                "http://localhost:%d/api/meta/code/element/{topLevelElementName}",
                this.rule.getLocalPort());
        Response response = client
                .target(uri)
                .resolveTemplate("topLevelElementName", "User")
                .request(MediaType.TEXT_HTML_TYPE)
                .header("Authorization", "Impersonation User ID")
                .get();

        this.assertResponseStatus(response, Status.OK);
        String responseHtml = response.readEntity(String.class);
        // assertEquals(responseHtml, expected, responseHtml);

        String expectedStringClasspathLocation = this.getClass().getCanonicalName() + "#smoke_test.html";
        this.fileMatchRule.assertFileContents(expectedStringClasspathLocation, responseHtml);
    }

    private void assertResponseStatus(@Nonnull Response response, Status status)
    {
        response.bufferEntity();
        String entityAsString = response.readEntity(String.class);
        assertThat(entityAsString, response.getStatusInfo(), is(status));
    }
}
