package com.stackoverflow.dropwizard.test;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import io.liftwizard.junit.rule.match.file.FileMatchRule;
import io.liftwizard.reladomo.test.rule.ReladomoTestFile;
import org.eclipse.collections.impl.factory.Maps;
import org.junit.Test;

public class StackOverflowGraphQLTest
        extends AbstractStackOverflowApplicationTest
{
    @Test
    @ReladomoTestFile("test-data/existing-question.txt")
    public void smokeTest()
    {
        Client client = this.getClient("graphqlSmokeTest");

        String queryName = this.getClass().getSimpleName() + ".smokeTest.graphql";
        String query     = FileMatchRule.slurp(queryName, this.getClass());

        Response response = client
                .target("http://localhost:{port}/graphql")
                .resolveTemplate("port", this.appRule.getLocalPort())
                .request()
                .post(Entity.json(Maps.mutable.with("query", query)));

        this.assertResponse("graphqlSmokeTest", Status.OK, response);
    }
}
