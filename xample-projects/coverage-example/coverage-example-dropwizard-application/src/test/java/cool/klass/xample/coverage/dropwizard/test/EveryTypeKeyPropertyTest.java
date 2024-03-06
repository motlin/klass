package cool.klass.xample.coverage.dropwizard.test;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import io.liftwizard.junit.rule.match.file.FileMatchRule;
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

        String jsonName = this.getClass().getSimpleName() + ".post.json5";
        String json     = FileMatchRule.slurp(jsonName, this.getClass());

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
            String jsonName = this.getClass().getSimpleName() + ".put.json5";
            String json     = FileMatchRule.slurp(jsonName, this.getClass());

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
