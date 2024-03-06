package cool.klass.xample.coverage.dropwizard.test;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

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

        //language=JSON
        String json = """
                {
                  "propertiesOptionalId": 1,
                  "optionalString": "PropertiesOptional optionalString 1 ☝",
                  "optionalInteger": 1,
                  "optionalLong": 100000000000,
                  "optionalDouble": 1.0123456789,
                  "optionalFloat": 1.0123457,
                  "optionalBoolean": true,
                  "optionalInstant": "1999-12-31T23:59:00Z",
                  "optionalLocalDate": "1999-12-31",
                  "version": {
                    "number": 1
                  }
                }
                """;

        Response putResponse = client
                .target("http://localhost:{port}/api/propertiesOptional/{id}")
                .resolveTemplate("port", this.appRule.getLocalPort())
                .resolveTemplate("id", 1)
                .request()
                .put(Entity.json(json));

        this.assertResponse("putFirst1", Status.NO_CONTENT, putResponse);

        Response getResponse = client
                .target("http://localhost:{port}/api/propertiesOptional/{id}")
                .resolveTemplate("port", this.appRule.getLocalPort())
                .resolveTemplate("id", 1)
                .request()
                .get();

        this.assertResponse("putFirst2", Status.OK, getResponse);
    }
}
