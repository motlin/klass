package cool.klass.xample.coverage.dropwizard.test;

import javax.ws.rs.client.Client;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import io.liftwizard.junit.rule.match.file.FileMatchRule;
import org.junit.Rule;
import org.junit.Test;

public class SourceCodeResourceTest
        extends AbstractCoverageTest
{
    @Rule
    public final FileMatchRule fileMatchRule = new FileMatchRule(this.getClass());

    @Test
    public void smoke_test()
    {
        Client client = this.getClient("smoke_test");

        Response response = client
                .target("http://localhost:{port}/api/meta/code/element/{topLevelElementName}")
                .resolveTemplate("port", this.appRule.getLocalPort())
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
}
