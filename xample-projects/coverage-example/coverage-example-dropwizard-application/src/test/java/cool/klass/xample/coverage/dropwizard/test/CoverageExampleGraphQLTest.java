package cool.klass.xample.coverage.dropwizard.test;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import io.liftwizard.junit.rule.match.file.FileMatchRule;
import org.eclipse.collections.impl.factory.Maps;
import org.junit.Test;

public class CoverageExampleGraphQLTest
        extends AbstractCoverageTest
{
    @Test
    public void graphqlSmokeTest()
    {
        Client client = this.getClient("graphqlSmokeTest");

        String graphqlQueryName = this.getClass().getSimpleName() + ".graphqlSmokeTest.graphql";

        String graphqlQuery     = FileMatchRule.slurp(graphqlQueryName, this.getClass());

        Response response = client
                .target("http://localhost:{port}/graphql")
                .resolveTemplate("port", this.appRule.getLocalPort())
                .request()
                .post(Entity.json(Maps.mutable.with("query", graphqlQuery)));

        this.assertResponse("graphqlSmokeTest", Status.OK, response);
    }
}
