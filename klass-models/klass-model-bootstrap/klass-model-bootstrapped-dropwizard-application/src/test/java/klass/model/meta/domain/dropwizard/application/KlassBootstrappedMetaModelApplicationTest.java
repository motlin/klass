package klass.model.meta.domain.dropwizard.application;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import io.liftwizard.junit.rule.match.file.FileMatchRule;
import org.eclipse.collections.impl.factory.Maps;
import org.junit.Test;

public class KlassBootstrappedMetaModelApplicationTest
        extends AbstractKlassBootstrappedMetaModelApplicationTest
{
    @Test
    public void graphqlSmokeTest()
    {
        Client client = this.getClient("graphqlSmokeTest");

        Class<?> callingClass   = this.getClass();
        String   testName       = callingClass.getSimpleName();
        String   sourceName     = testName + ".graphql";
        String   query = FileMatchRule.slurp(sourceName, callingClass);

        Response response = client.target("http://localhost:{port}/graphql")
                .resolveTemplate("port", this.appRule.getLocalPort())
                .request()
                .post(Entity.json(Maps.mutable.with("query", query)));

        this.assertResponse("graphqlSmokeTest", Status.OK, response);
    }
}
