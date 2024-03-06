package cool.klass.xample.coverage.dropwizard.test;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.json.JSONException;
import org.junit.Test;
import org.skyscreamer.jsonassert.JSONAssert;
import org.skyscreamer.jsonassert.JSONCompareMode;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class PropertiesRequiredTest extends AbstractCoverageTest
{
    @Test
    public void getFirst() throws JSONException
    {
        Client client = this.getClient(
                "cool.klass.xample.coverage.dropwizard.test.PropertiesRequiredTest.getFirst");

        Response response = client.target(
                String.format("http://localhost:%d/api/propertiesRequired/{id}", this.appRule.getLocalPort()))
                .resolveTemplate("id", 1)
                .request()
                .get();

        this.assertResponseStatus(response, Status.OK);

        String jsonResponse = response.readEntity(String.class);
        //language=JSON
        String expected = """
                {
                  "propertiesRequiredId": 1,
                  "requiredString": "PropertiesRequired requiredString 1 ☝",
                  "requiredInteger": 1,
                  "requiredLong": 100000000000,
                  "requiredDouble": 1.0123456789,
                  "requiredFloat": 1.0123457,
                  "requiredBoolean": true,
                  "requiredInstant": "1999-12-31T23:59:00Z",
                  "requiredLocalDate": "1999-12-31",
                  "requiredDerived": "cool.klass.xample.coverage.PropertiesRequired.getRequiredDerived"
                }""";
        JSONAssert.assertEquals(jsonResponse, expected, jsonResponse, JSONCompareMode.STRICT);
    }

    @Test
    public void getSecond() throws JSONException
    {
        Client client = this.getClient(
                "cool.klass.xample.coverage.dropwizard.test.PropertiesRequiredTest.getSecond");

        Response response = client.target(
                String.format("http://localhost:%d/api/propertiesRequired/{id}", this.appRule.getLocalPort()))
                .resolveTemplate("id", 2)
                .request()
                .get();

        this.assertResponseStatus(response, Status.OK);

        String jsonResponse = response.readEntity(String.class);
        //language=JSON
        String expected = """
                {
                  "propertiesRequiredId": 2,
                  "requiredString": "PropertiesRequired requiredString 2 ✌",
                  "requiredInteger": 2,
                  "requiredLong": 200000000000,
                  "requiredDouble": 2.0123456789,
                  "requiredFloat": 2.0123456,
                  "requiredBoolean": false,
                  "requiredInstant": "2000-01-01T00:00:00Z",
                  "requiredLocalDate": "2000-01-01",
                  "requiredDerived": "cool.klass.xample.coverage.PropertiesRequired.getRequiredDerived"
                }""";
        JSONAssert.assertEquals(jsonResponse, expected, jsonResponse, JSONCompareMode.STRICT);
    }

    @Test
    public void putFirst() throws JSONException
    {
        Client client = this.getClient(
                "cool.klass.xample.coverage.dropwizard.test.PropertiesRequiredTest.putFirst");

        //language=JSON
        String json = """
                {
                  "propertiesRequiredId": 1,
                  "requiredString": "PropertiesRequired requiredString 1 ☝",
                  "requiredInteger": 1,
                  "requiredLong": 100000000000,
                  "requiredDouble": 1.0123456789,
                  "requiredFloat": 1.0123457,
                  "requiredBoolean": true,
                  "requiredInstant": "1999-12-31T23:59:00Z",
                  "requiredLocalDate": "1999-12-31",
                  "version": {
                    "number": 1
                  }
                }
                """;

        Response putResponse = client.target(
                String.format("http://localhost:%d/api/propertiesRequired/{id}", this.appRule.getLocalPort()))
                .resolveTemplate("id", 1)
                .request()
                .put(Entity.json(json));
        this.assertResponseStatus(putResponse, Status.NO_CONTENT);
        String putStringResponse = putResponse.readEntity(String.class);
        assertThat(putStringResponse, is(""));

        Response getResponse = client.target(
                String.format("http://localhost:%d/api/propertiesRequired/{id}", this.appRule.getLocalPort()))
                .resolveTemplate("id", 1)
                .request()
                .get();

        this.assertResponseStatus(getResponse, Status.OK);

        String jsonResponse = getResponse.readEntity(String.class);
        //language=JSON
        String expected = """
                {
                  "propertiesRequiredId": 1,
                  "requiredString": "PropertiesRequired requiredString 1 ☝",
                  "requiredInteger": 1,
                  "requiredLong": 100000000000,
                  "requiredDouble": 1.0123456789,
                  "requiredFloat": 1.0123457,
                  "requiredBoolean": true,
                  "requiredInstant": "1999-12-31T23:59:00Z",
                  "requiredLocalDate": "1999-12-31",
                  "requiredDerived": "cool.klass.xample.coverage.PropertiesRequired.getRequiredDerived"
                }
                """;
        JSONAssert.assertEquals(jsonResponse, expected, jsonResponse, JSONCompareMode.STRICT);
    }
}
