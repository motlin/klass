package cool.klass.xample.coverage.dropwizard.test;

import javax.ws.rs.client.Client;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.json.JSONException;
import org.junit.Test;
import org.skyscreamer.jsonassert.JSONAssert;
import org.skyscreamer.jsonassert.JSONCompareMode;

public class PropertiesOptionalTest extends AbstractCoverageTest
{
    @Test
    public void getFirst() throws JSONException
    {
        Client client = this.getClient(
                "cool.klass.xample.coverage.dropwizard.test.PropertiesOptionalTest.getFirst");

        Response response = client.target(
                String.format("http://localhost:%d/api/propertiesOptional/{id}", this.appRule.getLocalPort()))
                .resolveTemplate("id", 1)
                .request()
                .get();

        this.assertResponseStatus(response, Status.OK);

        String jsonResponse = response.readEntity(String.class);
        //language=JSON
        String expected = """
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
                  "optionalDerived": "cool.klass.xample.coverage.PropertiesOptional.getOptionalDerived"
                }""";
        JSONAssert.assertEquals(jsonResponse, expected, jsonResponse, JSONCompareMode.STRICT);
    }

    @Test
    public void getSecond() throws JSONException
    {
        Client client = this.getClient(
                "cool.klass.xample.coverage.dropwizard.test.PropertiesOptionalTest.getSecond");

        Response response = client.target(
                String.format("http://localhost:%d/api/propertiesOptional/{id}", this.appRule.getLocalPort()))
                .resolveTemplate("id", 2)
                .request()
                .get();

        this.assertResponseStatus(response, Status.OK);

        String jsonResponse = response.readEntity(String.class);
        //language=JSON
        String expected = """
                {
                  "propertiesOptionalId": 2,
                  "optionalString": null,
                  "optionalInteger": null,
                  "optionalLong": null,
                  "optionalDouble": null,
                  "optionalFloat": null,
                  "optionalBoolean": null,
                  "optionalInstant": null,
                  "optionalLocalDate": null,
                  "optionalDerived": "cool.klass.xample.coverage.PropertiesOptional.getOptionalDerived"
                }""";
        JSONAssert.assertEquals(jsonResponse, expected, jsonResponse, JSONCompareMode.STRICT);
    }
}
