package cool.klass.xample.coverage.dropwizard.test;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import io.liftwizard.junit.rule.match.file.FileMatchRule;
import org.junit.Test;

public class PropertiesOptionalTest
        extends AbstractCoverageTest
{
    @Test
    public void getFirst()
    {
        Client client = this.getClient("getFirst");

        Response response = client
                .target("http://localhost:{port}/api/propertiesOptional/{id}")
                .resolveTemplate("port", this.appRule.getLocalPort())
                .resolveTemplate("id", 1)
                .request()
                .get();

        this.assertResponse("getFirst", Status.OK, response);
    }

    @Test
    public void getSecond()
    {
        Client client = this.getClient("getSecond");

        Response response = client
                .target("http://localhost:{port}/api/propertiesOptional/{id}")
                .resolveTemplate("port", this.appRule.getLocalPort())
                .resolveTemplate("id", 2)
                .request()
                .get();

        this.assertResponse("getSecond", Status.OK, response);
    }

    @Test
    public void putFirst()
    {
        Client client = this.getClient("putFirst");

        String jsonName = this.getClass().getSimpleName() + ".putFirst.json5";
        String json     = FileMatchRule.slurp(jsonName, this.getClass());

        Response putResponse = client
                .target("http://localhost:{port}/api/propertiesOptional/{id}")
                .resolveTemplate("port", this.appRule.getLocalPort())
                .resolveTemplate("id", 1)
                .request()
                .put(Entity.json(json));

        this.assertEmptyResponse(Status.NO_CONTENT, putResponse);

        Response getResponse = client
                .target("http://localhost:{port}/api/propertiesOptional/{id}")
                .resolveTemplate("port", this.appRule.getLocalPort())
                .resolveTemplate("id", 1)
                .request()
                .get();

        this.assertResponse("putFirst2", Status.OK, getResponse);
    }
}
