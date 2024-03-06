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
                String.format("http://localhost:%d/api/propertiesRequired/{id}", this.rule.getLocalPort()))
                .resolveTemplate("id", 1)
                .request()
                .get();

        this.assertResponseStatus(response, Status.OK);

        String jsonResponse = response.readEntity(String.class);
        //language=JSON
        String expected = ""
                + "{\n"
                + "  \"propertiesRequiredId\": 1,\n"
                + "  \"requiredString\": \"PropertiesRequired requiredString 1 ☝\",\n"
                + "  \"requiredInteger\": 1,\n"
                + "  \"requiredLong\": 100000000000,\n"
                + "  \"requiredDouble\": 1.0123456789,\n"
                + "  \"requiredFloat\": 1.0123457,\n"
                + "  \"requiredBoolean\": true,\n"
                + "  \"requiredInstant\": \"1999-12-31T23:59:00Z\",\n"
                + "  \"requiredLocalDate\": \"1999-12-31\",\n"
                + "  \"requiredDerived\": \"cool.klass.xample.coverage.PropertiesRequired.getRequiredDerived\"\n"
                + "}";
        JSONAssert.assertEquals(jsonResponse, expected, jsonResponse, JSONCompareMode.STRICT);
    }

    @Test
    public void getSecond() throws JSONException
    {
        Client client = this.getClient(
                "cool.klass.xample.coverage.dropwizard.test.PropertiesRequiredTest.getSecond");

        Response response = client.target(
                String.format("http://localhost:%d/api/propertiesRequired/{id}", this.rule.getLocalPort()))
                .resolveTemplate("id", 2)
                .request()
                .get();

        this.assertResponseStatus(response, Status.OK);

        String jsonResponse = response.readEntity(String.class);
        //language=JSON
        String expected = ""
                + "{\n"
                + "  \"propertiesRequiredId\": 2,\n"
                + "  \"requiredString\": \"PropertiesRequired requiredString 2 ✌\",\n"
                + "  \"requiredInteger\": 2,\n"
                + "  \"requiredLong\": 200000000000,\n"
                + "  \"requiredDouble\": 2.0123456789,\n"
                + "  \"requiredFloat\": 2.0123456,\n"
                + "  \"requiredBoolean\": false,\n"
                + "  \"requiredInstant\": \"2000-01-01T00:00:00Z\",\n"
                + "  \"requiredLocalDate\": \"2000-01-01\",\n"
                + "  \"requiredDerived\": \"cool.klass.xample.coverage.PropertiesRequired.getRequiredDerived\"\n"
                + "}";
        JSONAssert.assertEquals(jsonResponse, expected, jsonResponse, JSONCompareMode.STRICT);
    }

    @Test
    public void putFirst() throws JSONException
    {
        Client client = this.getClient(
                "cool.klass.xample.coverage.dropwizard.test.PropertiesRequiredTest.putFirst");

        //language=JSON
        String json = ""
                + "{\n"
                + "  \"propertiesRequiredId\": 1,\n"
                + "  \"requiredString\": \"PropertiesRequired requiredString 1 ☝\",\n"
                + "  \"requiredInteger\": 1,\n"
                + "  \"requiredLong\": 100000000000,\n"
                + "  \"requiredDouble\": 1.0123456789,\n"
                + "  \"requiredFloat\": 1.0123457,\n"
                + "  \"requiredBoolean\": true,\n"
                + "  \"requiredInstant\": \"1999-12-31T23:59:00Z\",\n"
                + "  \"requiredLocalDate\": \"1999-12-31\",\n"
                + "  \"version\": {\n"
                + "    \"number\": 1\n"
                + "  }\n"
                + "}\n";

        Response putResponse = client.target(
                String.format("http://localhost:%d/api/propertiesRequired/{id}", this.rule.getLocalPort()))
                .resolveTemplate("id", 1)
                .request()
                .put(Entity.json(json));
        this.assertResponseStatus(putResponse, Status.NO_CONTENT);
        String putStringResponse = putResponse.readEntity(String.class);
        assertThat(putStringResponse, is(""));

        Response getResponse = client.target(
                String.format("http://localhost:%d/api/propertiesRequired/{id}", this.rule.getLocalPort()))
                .resolveTemplate("id", 1)
                .request()
                .get();

        this.assertResponseStatus(getResponse, Status.OK);

        String jsonResponse = getResponse.readEntity(String.class);
        //language=JSON
        String expected = ""
                + "{\n"
                + "  \"propertiesRequiredId\": 1,\n"
                + "  \"requiredString\": \"PropertiesRequired requiredString 1 ☝\",\n"
                + "  \"requiredInteger\": 1,\n"
                + "  \"requiredLong\": 100000000000,\n"
                + "  \"requiredDouble\": 1.0123456789,\n"
                + "  \"requiredFloat\": 1.0123457,\n"
                + "  \"requiredBoolean\": true,\n"
                + "  \"requiredInstant\": \"1999-12-31T23:59:00Z\",\n"
                + "  \"requiredLocalDate\": \"1999-12-31\",\n"
                + "  \"requiredDerived\": \"cool.klass.xample.coverage.PropertiesRequired.getRequiredDerived\"\n"
                + "}\n";
        JSONAssert.assertEquals(jsonResponse, expected, jsonResponse, JSONCompareMode.STRICT);
    }
}
