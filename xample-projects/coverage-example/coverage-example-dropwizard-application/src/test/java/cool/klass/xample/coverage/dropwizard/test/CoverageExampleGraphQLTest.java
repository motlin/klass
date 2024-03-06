package cool.klass.xample.coverage.dropwizard.test;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.eclipse.collections.impl.factory.Maps;
import org.junit.Test;

public class CoverageExampleGraphQLTest
        extends AbstractCoverageTest
{
    @Test
    public void graphqlSmokeTest()
    {
        Client client = this.getClient("graphqlSmokeTest");

        //language=GraphQL
        String query = """
                query {
                  everyTypeKeyProperties {
                    keyString
                    keyInteger
                    keyLong
                    keyDouble
                    keyFloat
                    keyBoolean
                    keyInstant
                    keyLocalDate
                    everyTypeForeignKeyProperties {
                      id
                      foreignKeyString
                      foreignKeyInteger
                      foreignKeyLong
                      foreignKeyDouble
                      foreignKeyFloat
                      foreignKeyBoolean
                      foreignKeyInstant
                      foreignKeyLocalDate
                      data
                    }
                    version {
                      number
                    }
                  }
                  propertiesOptionals {
                    propertiesOptionalId
                    optionalLong
                    optionalInteger
                    optionalDouble
                    optionalFloat
                    optionalString
                    optionalBoolean
                    systemFrom
                    systemTo
                    lastUpdatedBy {
                      email
                    }
                    lastUpdatedById
                    createdOn
                    createdBy {
                      email
                    }
                    createdById
                    version {
                      number
                    }
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
