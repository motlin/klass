package cool.klass.xample.coverage.dropwizard.test;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.json.JSONException;
import org.junit.Test;
import org.skyscreamer.jsonassert.JSONAssert;
import org.skyscreamer.jsonassert.JSONCompareMode;

public class AllKeyPropertiesTest extends AbstractCoverageTest
{
    @Test
    public void get() throws JSONException
    {
        Client client = this.getClient(
                "cool.klass.xample.coverage.dropwizard.test.AllKeyPropertiesTest.get");

        Response response = client.target(
                String.format("http://localhost:%d/api/allKeyProperties", RULE.getLocalPort()))
                .request()
                .get();

        this.assertResponseStatus(response, Status.OK);

        String jsonResponse = response.readEntity(String.class);
        //language=JSON
        String expected = ""
                + "[\n"
                + "  {\n"
                + "    \"keyString\": \"AllKeyProperties keyString 1 ☝\",\n"
                + "    \"keyInteger\": 1,\n"
                + "    \"keyLong\": 1,\n"
                + "    \"keyDouble\": 1.0123456789,\n"
                + "    \"keyFloat\": 1.0123457,\n"
                + "    \"keyBoolean\": true,\n"
                + "    \"keyInstant\": \"1999-12-31T23:59:00Z\",\n"
                + "    \"keyLocalDate\": \"1999-12-31\",\n"
                + "    \"allForeignKeyProperties\": [\n"
                + "      {\n"
                + "        \"id\": 1,\n"
                + "        \"foreignKeyString\": \"AllKeyProperties keyString 1 ☝\",\n"
                + "        \"foreignKeyInteger\": 1,\n"
                + "        \"foreignKeyLong\": 1,\n"
                + "        \"foreignKeyDouble\": 1.0123456789,\n"
                + "        \"foreignKeyFloat\": 1.0123457,\n"
                + "        \"foreignKeyBoolean\": true,\n"
                + "        \"foreignKeyInstant\": \"1999-12-31T23:59:00Z\",\n"
                + "        \"foreignKeyLocalDate\": \"1999-12-31\",\n"
                + "        \"data\": \"AllForeignKeyProperties data 1 ☝\"\n"
                + "      }\n"
                + "    ]\n"
                + "  },\n"
                + "  {\n"
                + "    \"keyString\": \"AllKeyProperties keyString 2 ✌\",\n"
                + "    \"keyInteger\": 2,\n"
                + "    \"keyLong\": 2,\n"
                + "    \"keyDouble\": 2.0123456789,\n"
                + "    \"keyFloat\": 2.0123456,\n"
                + "    \"keyBoolean\": false,\n"
                + "    \"keyInstant\": \"2000-01-01T00:00:00Z\",\n"
                + "    \"keyLocalDate\": \"2000-01-01\",\n"
                + "    \"allForeignKeyProperties\": [\n"
                + "      {\n"
                + "        \"id\": 2,\n"
                + "        \"foreignKeyString\": \"AllKeyProperties keyString 2 ✌\",\n"
                + "        \"foreignKeyInteger\": 2,\n"
                + "        \"foreignKeyLong\": 2,\n"
                + "        \"foreignKeyDouble\": 2.0123456789,\n"
                + "        \"foreignKeyFloat\": 2.0123456,\n"
                + "        \"foreignKeyBoolean\": false,\n"
                + "        \"foreignKeyInstant\": \"2000-01-01T00:00:00Z\",\n"
                + "        \"foreignKeyLocalDate\": \"2000-01-01\",\n"
                + "        \"data\": \"AllForeignKeyProperties data 2 ✌\"\n"
                + "      }\n"
                + "    ]\n"
                + "  }\n"
                + "]\n";

        JSONAssert.assertEquals(expected, jsonResponse, JSONCompareMode.STRICT);
    }

    @Test
    public void post() throws JSONException
    {
        Client client = this.getClient(
                "cool.klass.xample.coverage.dropwizard.test.AllKeyPropertiesTest.post");

        //language=JSON
        String json = ""
                + "[\n"
                + "  {\n"
                + "    \"keyString\": \"AllKeyProperties keyString 1 ☝\",\n"
                + "    \"keyInteger\": 1,\n"
                + "    \"keyLong\": 100000000000,\n"
                + "    \"keyDouble\": 1.0123456789,\n"
                + "    \"keyFloat\": 1.0123457,\n"
                + "    \"keyBoolean\": true,\n"
                + "    \"keyInstant\": \"1999-12-31T23:59:00Z\",\n"
                + "    \"keyLocalDate\": \"1999-12-31\",\n"
                + "    \"allForeignKeyProperties\": [\n"
                + "      {\n"
                + "        \"id\": 1,\n"
                + "        \"foreignKeyString\": \"AllKeyProperties keyString 1 ☝\",\n"
                + "        \"foreignKeyInteger\": 1,\n"
                + "        \"foreignKeyLong\": 100000000000,\n"
                + "        \"foreignKeyDouble\": 1.0123456789,\n"
                + "        \"foreignKeyFloat\": 1.0123457,\n"
                + "        \"foreignKeyBoolean\": true,\n"
                + "        \"foreignKeyInstant\": \"1999-12-31T23:59:00Z\",\n"
                + "        \"foreignKeyLocalDate\": \"1999-12-31\"\n"
                + "      }\n"
                + "    ]\n"
                + "  },\n"
                + "  {\n"
                + "    \"keyString\": \"AllKeyProperties keyString 2 ✌\",\n"
                + "    \"keyInteger\": 2,\n"
                + "    \"keyLong\": 200000000000,\n"
                + "    \"keyDouble\": 2.0123456789,\n"
                + "    \"keyFloat\": 2.0123456,\n"
                + "    \"keyBoolean\": false,\n"
                + "    \"keyInstant\": \"2000-01-01T00:00:00Z\",\n"
                + "    \"keyLocalDate\": \"2000-01-01\",\n"
                + "    \"allForeignKeyProperties\": [\n"
                + "      {\n"
                + "        \"id\": 2,\n"
                + "        \"foreignKeyString\": \"AllKeyProperties keyString 2 ✌\",\n"
                + "        \"foreignKeyInteger\": 2,\n"
                + "        \"foreignKeyLong\": 200000000000,\n"
                + "        \"foreignKeyDouble\": 2.0123456789,\n"
                + "        \"foreignKeyFloat\": 2.0123456,\n"
                + "        \"foreignKeyBoolean\": false,\n"
                + "        \"foreignKeyInstant\": \"2000-01-01T00:00:00Z\",\n"
                + "        \"foreignKeyLocalDate\": \"2000-01-01\"\n"
                + "      }\n"
                + "    ]\n"
                + "  }\n"
                + "]\n";

        Response response = client.target(
                String.format("http://localhost:%d/api/allKeyProperties", RULE.getLocalPort()))
                .request()
                .post(Entity.json(json));

        this.assertResponseStatus(response, Status.METHOD_NOT_ALLOWED);

        String jsonResponse = response.readEntity(String.class);

        //language=JSON
        String expected = ""
                + "{\n"
                + "  \"code\" : 405,\n"
                + "  \"message\" : \"HTTP 405 Method Not Allowed\"\n"
                + "}\n";
        JSONAssert.assertEquals(expected, jsonResponse, JSONCompareMode.STRICT);
    }

    @Test
    public void put() throws JSONException
    {
        Client client = this.getClient(
                "cool.klass.xample.coverage.dropwizard.test.AllKeyPropertiesTest.put");

        //language=JSON
        String json = ""
                + "[\n"
                + "  {\n"
                + "    \"keyString\": \"AllKeyProperties keyString 1 ☝\",\n"
                + "    \"keyInteger\": 1,\n"
                + "    \"keyLong\": 100000000000,\n"
                + "    \"keyDouble\": 1.0123456789,\n"
                + "    \"keyFloat\": 1.0123457,\n"
                + "    \"keyBoolean\": true,\n"
                + "    \"keyInstant\": \"1999-12-31T23:59:00Z\",\n"
                + "    \"keyLocalDate\": \"1999-12-31\",\n"
                + "    \"allForeignKeyProperties\": [\n"
                + "      {\n"
                + "        \"id\": 1,\n"
                + "        \"data\": \"edited\"\n"
                + "      },\n"
                + "      {\n"
                + "        \"data\": \"new\"\n"
                + "      }\n"
                + "    ]\n"
                + "  }\n"
                + "]\n";

        Response response = client.target(
                String.format("http://localhost:%d/api/allKeyProperties/AllKeyProperties keyString 1 ☝/1/1/1.0123456789/1.0123457/true/1999-12-31T23:59:00Z/1999-12-31/", RULE.getLocalPort()))
                .request()
                .put(Entity.json(json));

        this.assertResponseStatus(response, Status.OK);

        String jsonResponse = response.readEntity(String.class);

        // TODO: PUT shouldn't actually return anything
        //language=JSON
        String expectedJson = ""
                + "{\n"
                + "  \"keyString\": \"AllKeyProperties keyString 1 ☝\",\n"
                + "  \"keyInteger\": 1,\n"
                + "  \"keyLong\": 1,\n"
                + "  \"keyDouble\": 1.0123456789,\n"
                + "  \"keyFloat\": 1.0123457,\n"
                + "  \"keyBoolean\": true,\n"
                + "  \"keyInstant\": \"1999-12-31T23:59:00Z\",\n"
                + "  \"keyLocalDate\": \"1999-12-31\",\n"
                + "  \"allForeignKeyProperties\": [\n"
                + "    {\n"
                + "      \"id\": 1,\n"
                + "      \"foreignKeyString\": \"AllKeyProperties keyString 1 ☝\",\n"
                + "      \"foreignKeyInteger\": 1,\n"
                + "      \"foreignKeyLong\": 1,\n"
                + "      \"foreignKeyDouble\": 1.0123456789,\n"
                + "      \"foreignKeyFloat\": 1.0123457,\n"
                + "      \"foreignKeyBoolean\": true,\n"
                + "      \"foreignKeyInstant\": \"1999-12-31T23:59:00Z\",\n"
                + "      \"foreignKeyLocalDate\": \"1999-12-31\",\n"
                + "      \"data\": \"AllForeignKeyProperties data 1 ☝\"\n"
                + "    }\n"
                + "  ]\n"
                + "}\n";

        JSONAssert.assertEquals(expectedJson, jsonResponse, JSONCompareMode.STRICT);
    }
}
