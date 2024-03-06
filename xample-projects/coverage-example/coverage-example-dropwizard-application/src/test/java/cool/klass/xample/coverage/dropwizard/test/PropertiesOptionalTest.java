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
                String.format("http://localhost:%d/api/propertiesOptional/{id}", this.rule.getLocalPort()))
                .resolveTemplate("id", 1)
                .request()
                .get();

        this.assertResponseStatus(response, Status.OK);

        String jsonResponse = response.readEntity(String.class);
        //language=JSON
        String expected = ""
                + "{\n"
                + "  \"propertiesOptionalId\": 1,\n"
                + "  \"optionalString\": \"PropertiesOptional optionalString 1 ☝\",\n"
                + "  \"optionalInteger\": 1,\n"
                + "  \"optionalLong\": 100000000000,\n"
                + "  \"optionalDouble\": 1.0123456789,\n"
                + "  \"optionalFloat\": 1.0123457,\n"
                + "  \"optionalBoolean\": true,\n"
                + "  \"optionalInstant\": \"1999-12-31T23:59:00Z\",\n"
                + "  \"optionalLocalDate\": \"1999-12-31\",\n"
                + "  \"optionalDerived\": \"cool.klass.xample.coverage.PropertiesOptional.getOptionalDerived\"\n"
                + "}";
        JSONAssert.assertEquals(jsonResponse, expected, jsonResponse, JSONCompareMode.STRICT);
    }

    @Test
    public void getSecond() throws JSONException
    {
        Client client = this.getClient(
                "cool.klass.xample.coverage.dropwizard.test.PropertiesOptionalTest.getSecond");

        Response response = client.target(
                String.format("http://localhost:%d/api/propertiesOptional/{id}", this.rule.getLocalPort()))
                .resolveTemplate("id", 2)
                .request()
                .get();

        this.assertResponseStatus(response, Status.OK);

        String jsonResponse = response.readEntity(String.class);
        //language=JSON
        String expected = ""
                + "{\n"
                + "  \"propertiesOptionalId\": 2,\n"
                + "  \"optionalString\": null,\n"
                + "  \"optionalInteger\": null,\n"
                + "  \"optionalLong\": null,\n"
                + "  \"optionalDouble\": null,\n"
                + "  \"optionalFloat\": null,\n"
                + "  \"optionalBoolean\": null,\n"
                + "  \"optionalInstant\": null,\n"
                + "  \"optionalLocalDate\": null,\n"
                + "  \"optionalDerived\": \"cool.klass.xample.coverage.PropertiesOptional.getOptionalDerived\"\n"
                + "}";
        JSONAssert.assertEquals(jsonResponse, expected, jsonResponse, JSONCompareMode.STRICT);
    }
}
