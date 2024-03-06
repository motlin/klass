package cool.klass.xample.coverage.dropwizard.test;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.junit.Test;

public class EveryTypeKeyPropertyTest
        extends AbstractCoverageTest
{
    @Test
    public void get()
    {
        Client client = this.getClient("get");

        Response response = client
                .target("http://localhost:{port}/api/everyTypeKeyProperty")
                .resolveTemplate("port", this.appRule.getLocalPort())
                .request()
                .get();

        this.assertResponse("get", Status.OK, response);
    }

    @Test
    public void post()
    {
        Client client = this.getClient("post");

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

        Response response = client
                .target("http://localhost:{port}/api/everyTypeKeyProperty")
                .resolveTemplate("port", this.appRule.getLocalPort())
                .request()
                .post(Entity.json(json));

        this.assertResponse("post", Status.METHOD_NOT_ALLOWED, response);
    }

    @Test
    public void put()
    {
        Client client = this.getClient("put");

        {
            Response response = client
                    .target("http://localhost:{port}/api/everyTypeKeyProperty/EveryTypeKeyProperty keyString 1 ☝/1/1/1.0123456789/1.0123457/true/1999-12-31T23:59:00Z/1999-12-31")
                    .resolveTemplate("port", this.appRule.getLocalPort())
                    .request()
                    .get();

            this.assertResponse("put1", Status.OK, response);
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

            Response response = client
                    .target("http://localhost:{port}/api/everyTypeKeyProperty/EveryTypeKeyProperty keyString 1 ☝/1/1/1.0123456789/1.0123457/true/1999-12-31T23:59:00Z/1999-12-31")
                    .resolveTemplate("port", this.appRule.getLocalPort())
                    .request()
                    .put(Entity.json(json));

            this.assertEmptyResponse(Status.NO_CONTENT, response);
        }

        Response response = client
                .target("http://localhost:{port}/api/everyTypeKeyProperty/EveryTypeKeyProperty keyString 1 ☝/1/1/1.0123456789/1.0123457/true/1999-12-31T23:59:00Z/1999-12-31")
                .resolveTemplate("port", this.appRule.getLocalPort())
                .request()
                .get();

        this.assertResponse("put3", Status.OK, response);
    }
}
