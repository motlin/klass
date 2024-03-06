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
import static org.junit.Assert.assertThat;

public class EveryTypeKeyPropertyTest extends AbstractCoverageTest
{
    @Test
    public void get() throws JSONException
    {
        Client client = this.getClient(
                "cool.klass.xample.coverage.dropwizard.test.EveryTypeKeyPropertyTest.get");

        Response response = client.target(
                String.format("http://localhost:%d/api/everyTypeKeyProperty", this.appRule.getLocalPort()))
                .request()
                .get();

        this.assertResponseStatus(response, Status.OK);

        String jsonResponse = response.readEntity(String.class);
        //language=JSON
        String expected = """
                [
                  {
                    "keyString": "EveryTypeKeyProperty keyString 1 ☝",
                    "keyInteger": 1,
                    "keyLong": 1,
                    "keyDouble": 1.0123456789,
                    "keyFloat": 1.0123457,
                    "keyBoolean": true,
                    "keyInstant": "1999-12-31T23:59:00Z",
                    "keyLocalDate": "1999-12-31",
                    "everyTypeForeignKeyProperties": [
                      {
                        "id": 1,
                        "foreignKeyString": "EveryTypeKeyProperty keyString 1 ☝",
                        "foreignKeyInteger": 1,
                        "foreignKeyLong": 1,
                        "foreignKeyDouble": 1.0123456789,
                        "foreignKeyFloat": 1.0123457,
                        "foreignKeyBoolean": true,
                        "foreignKeyInstant": "1999-12-31T23:59:00Z",
                        "foreignKeyLocalDate": "1999-12-31",
                        "data": "EveryTypeForeignKeyProperty data 1 ☝"
                      }
                    ],
                    "version": {
                      "number": 1
                    }
                  },
                  {
                    "keyString": "EveryTypeKeyProperty keyString 2 ✌",
                    "keyInteger": 2,
                    "keyLong": 2,
                    "keyDouble": 2.0123456789,
                    "keyFloat": 2.0123456,
                    "keyBoolean": false,
                    "keyInstant": "2000-01-01T00:00:00Z",
                    "keyLocalDate": "2000-01-01",
                    "everyTypeForeignKeyProperties": [
                      {
                        "id": 2,
                        "foreignKeyString": "EveryTypeKeyProperty keyString 2 ✌",
                        "foreignKeyInteger": 2,
                        "foreignKeyLong": 2,
                        "foreignKeyDouble": 2.0123456789,
                        "foreignKeyFloat": 2.0123456,
                        "foreignKeyBoolean": false,
                        "foreignKeyInstant": "2000-01-01T00:00:00Z",
                        "foreignKeyLocalDate": "2000-01-01",
                        "data": "EveryTypeForeignKeyProperty data 2 ✌"
                      }
                    ],
                    "version": {
                      "number": 1
                    }
                  }
                ]
                """;

        JSONAssert.assertEquals(jsonResponse, expected, jsonResponse, JSONCompareMode.STRICT);
    }

    @Test
    public void post() throws JSONException
    {
        Client client = this.getClient(
                "cool.klass.xample.coverage.dropwizard.test.EveryTypeKeyPropertyTest.post");

        //language=JSON
        String json = """
                [
                  {
                    "keyString": "EveryTypeKeyProperty keyString 1 ☝",
                    "keyInteger": 1,
                    "keyLong": 100000000000,
                    "keyDouble": 1.0123456789,
                    "keyFloat": 1.0123457,
                    "keyBoolean": true,
                    "keyInstant": "1999-12-31T23:59:00Z",
                    "keyLocalDate": "1999-12-31",
                    "everyTypeForeignKeyProperties": [
                      {
                        "id": 1,
                        "foreignKeyString": "EveryTypeKeyProperty keyString 1 ☝",
                        "foreignKeyInteger": 1,
                        "foreignKeyLong": 100000000000,
                        "foreignKeyDouble": 1.0123456789,
                        "foreignKeyFloat": 1.0123457,
                        "foreignKeyBoolean": true,
                        "foreignKeyInstant": "1999-12-31T23:59:00Z",
                        "foreignKeyLocalDate": "1999-12-31"
                      }
                    ]
                  },
                  {
                    "keyString": "EveryTypeKeyProperty keyString 2 ✌",
                    "keyInteger": 2,
                    "keyLong": 200000000000,
                    "keyDouble": 2.0123456789,
                    "keyFloat": 2.0123456,
                    "keyBoolean": false,
                    "keyInstant": "2000-01-01T00:00:00Z",
                    "keyLocalDate": "2000-01-01",
                    "everyTypeForeignKeyProperties": [
                      {
                        "id": 2,
                        "foreignKeyString": "EveryTypeKeyProperty keyString 2 ✌",
                        "foreignKeyInteger": 2,
                        "foreignKeyLong": 200000000000,
                        "foreignKeyDouble": 2.0123456789,
                        "foreignKeyFloat": 2.0123456,
                        "foreignKeyBoolean": false,
                        "foreignKeyInstant": "2000-01-01T00:00:00Z",
                        "foreignKeyLocalDate": "2000-01-01"
                      }
                    ]
                  }
                ]
                """;

        Response response = client.target(
                String.format("http://localhost:%d/api/everyTypeKeyProperty", this.appRule.getLocalPort()))
                .request()
                .post(Entity.json(json));

        this.assertResponseStatus(response, Status.METHOD_NOT_ALLOWED);

        String jsonResponse = response.readEntity(String.class);

        //language=JSON
        String expected = """
                {
                  "code" : 405,
                  "message" : "HTTP 405 Method Not Allowed"
                }
                """;
        JSONAssert.assertEquals(jsonResponse, expected, jsonResponse, JSONCompareMode.STRICT);
    }

    @Test
    public void put() throws JSONException
    {
        Client client = this.getClient(
                "cool.klass.xample.coverage.dropwizard.test.EveryTypeKeyPropertyTest.put");

        {
            Response response = client.target(
                    String.format(
                            "http://localhost:%d/api/everyTypeKeyProperty/EveryTypeKeyProperty keyString 1 ☝/1/1/1.0123456789/1.0123457/true/1999-12-31T23:59:00Z/1999-12-31/",
                            this.appRule.getLocalPort()))
                    .request()
                    .get();

            this.assertResponseStatus(response, Status.OK);
            String jsonResponse = response.readEntity(String.class);

            //language=JSON
            String expectedJson = """
                    {
                      "keyString": "EveryTypeKeyProperty keyString 1 ☝",
                      "keyInteger": 1,
                      "keyLong": 1,
                      "keyDouble": 1.0123456789,
                      "keyFloat": 1.0123457,
                      "keyBoolean": true,
                      "keyInstant": "1999-12-31T23:59:00Z",
                      "keyLocalDate": "1999-12-31",
                      "everyTypeForeignKeyProperties": [
                        {
                          "id": 1,
                          "foreignKeyString": "EveryTypeKeyProperty keyString 1 ☝",
                          "foreignKeyInteger": 1,
                          "foreignKeyLong": 1,
                          "foreignKeyDouble": 1.0123456789,
                          "foreignKeyFloat": 1.0123457,
                          "foreignKeyBoolean": true,
                          "foreignKeyInstant": "1999-12-31T23:59:00Z",
                          "foreignKeyLocalDate": "1999-12-31",
                          "data": "EveryTypeForeignKeyProperty data 1 ☝"
                        }
                      ],
                      "version": {
                        "number": 1
                      }
                    }
                    """;

            JSONAssert.assertEquals(jsonResponse, expectedJson, jsonResponse, JSONCompareMode.STRICT);
        }

        {
            //language=JSON
            String json = """
                    {
                      "everyTypeForeignKeyProperties": [
                        {
                          "id": 1,
                          "data": "edited"
                        },
                        {
                          "data": "new"
                        }
                      ],
                      "version": {
                        "number": 1
                      }
                    }
                    """;

            Response response = client.target(
                    String.format(
                            "http://localhost:%d/api/everyTypeKeyProperty/EveryTypeKeyProperty keyString 1 ☝/1/1/1.0123456789/1.0123457/true/1999-12-31T23:59:00Z/1999-12-31/",
                            this.appRule.getLocalPort()))
                    .request()
                    .put(Entity.json(json));

            this.assertResponseStatus(response, Status.NO_CONTENT);
            String jsonResponse = response.readEntity(String.class);
            assertThat(jsonResponse, is(""));
        }

        Response response = client.target(
                String.format(
                        "http://localhost:%d/api/everyTypeKeyProperty/EveryTypeKeyProperty keyString 1 ☝/1/1/1.0123456789/1.0123457/true/1999-12-31T23:59:00Z/1999-12-31/",
                        this.appRule.getLocalPort()))
                .request()
                .get();

        this.assertResponseStatus(response, Status.OK);
        String jsonResponse = response.readEntity(String.class);

        //language=JSON
        String expectedJson = """
                {
                  "keyString": "EveryTypeKeyProperty keyString 1 ☝",
                  "keyInteger": 1,
                  "keyLong": 1,
                  "keyDouble": 1.0123456789,
                  "keyFloat": 1.0123457,
                  "keyBoolean": true,
                  "keyInstant": "1999-12-31T23:59:00Z",
                  "keyLocalDate": "1999-12-31",
                  "everyTypeForeignKeyProperties": [
                    {
                      "id": 1,
                      "foreignKeyString": "EveryTypeKeyProperty keyString 1 ☝",
                      "foreignKeyInteger": 1,
                      "foreignKeyLong": 1,
                      "foreignKeyDouble": 1.0123456789,
                      "foreignKeyFloat": 1.0123457,
                      "foreignKeyBoolean": true,
                      "foreignKeyInstant": "1999-12-31T23:59:00Z",
                      "foreignKeyLocalDate": "1999-12-31",
                      "data": "edited"
                    },
                    {
                      "id": 3,
                      "foreignKeyString": "EveryTypeKeyProperty keyString 1 ☝",
                      "foreignKeyInteger": 1,
                      "foreignKeyLong": 1,
                      "foreignKeyDouble": 1.0123456789,
                      "foreignKeyFloat": 1.0123457,
                      "foreignKeyBoolean": true,
                      "foreignKeyInstant": "1999-12-31T23:59:00Z",
                      "foreignKeyLocalDate": "1999-12-31",
                      "data": "new"
                    }
                  ],
                  "version": {
                    "number": 2
                  }
                }""";

        JSONAssert.assertEquals(jsonResponse, expectedJson, jsonResponse, JSONCompareMode.STRICT);
    }
}
