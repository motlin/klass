package com.stackoverflow.dropwizard.test;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

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

        //language=GraphQL
        String query = """
                query {
                    question(id: 1) {
                        id
                        system
                        systemFrom
                        systemTo
                        createdById
                        createdOn
                        lastUpdatedById
                        body
                        title
                        status
                        deleted
                        tags {
                            tag {
                                name
                                system
                                systemFrom
                                systemTo
                                createdById
                                createdOn
                                lastUpdatedById
                                description
                            }
                        },
                        version {
                            number,
                        },
                    }
                }
                """;

        Response response = client
                .target("http://localhost:{port}/graphql")
                .resolveTemplate("port", this.appRule.getLocalPort())
                .request()
                .post(Entity.json(Maps.mutable.with("query", query)));

        this.assertResponse("graphqlSmokeTest", Status.OK, response);
    }
}
