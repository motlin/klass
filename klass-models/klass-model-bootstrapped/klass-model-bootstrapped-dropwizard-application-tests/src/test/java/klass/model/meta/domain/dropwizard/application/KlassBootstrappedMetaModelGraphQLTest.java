package klass.model.meta.domain.dropwizard.application;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import io.liftwizard.junit.rule.match.file.FileMatchRule;
import org.eclipse.collections.impl.factory.Maps;
import org.junit.Test;

public class KlassBootstrappedMetaModelGraphQLTest
        extends AbstractKlassBootstrappedMetaModelApplicationTest
{
    @Test
    public void packageableElements()
    {
        this.assertGraphQL("packageableElements");
    }

    @Test
    public void enumerations()
    {
        this.assertGraphQL("enumerations");
    }

    @Test
    public void interfaces()
    {
        this.assertGraphQL("interfaces");
    }

    @Test
    public void classes()
    {
        this.assertGraphQL("classes");
    }

    @Test
    public void associations()
    {
        this.assertGraphQL("associations");
    }

    @Test
    public void projectionElements()
    {
        this.assertGraphQL("projectionElements");
    }

    @Test
    public void namedProjections()
    {
        this.assertGraphQL("namedProjections");
    }

    @Test
    public void expressionValue()
    {
        this.assertGraphQL("expressionValue");
    }

    @Test
    public void criteria()
    {
        this.assertGraphQL("criteria");
    }

    @Test
    public void serviceGroups()
    {
        this.assertGraphQL("serviceGroups");
    }

    private void assertGraphQL(String testName)
    {
        Client client   = this.getClient(testName);

        Class<?> callingClass   = this.getClass();
        String   sourceName     = callingClass.getSimpleName() + "." + testName + ".graphql";
        String   query = FileMatchRule.slurp(sourceName, callingClass);

        Response response = client.target("http://localhost:{port}/graphql")
                .resolveTemplate("port", this.appRule.getLocalPort())
                .request()
                .post(Entity.json(Maps.mutable.with("query", query)));

        this.assertResponse(testName, Status.OK, response);
    }
}
