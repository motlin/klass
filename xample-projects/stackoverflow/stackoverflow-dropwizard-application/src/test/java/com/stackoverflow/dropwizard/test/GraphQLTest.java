package com.stackoverflow.dropwizard.test;

import javax.annotation.Nonnull;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import com.stackoverflow.dropwizard.application.StackOverflowApplication;
import com.stackoverflow.dropwizard.application.StackOverflowConfiguration;
import io.dropwizard.client.JerseyClientBuilder;
import io.dropwizard.client.JerseyClientConfiguration;
import io.dropwizard.setup.Environment;
import io.dropwizard.testing.ResourceHelpers;
import io.dropwizard.testing.junit.DropwizardAppRule;
import io.dropwizard.util.Duration;
import io.liftwizard.reladomo.test.rule.ReladomoTestFile;
import io.liftwizard.reladomo.test.rule.ReladomoTestRuleBuilder;
import org.eclipse.collections.impl.factory.Maps;
import org.json.JSONException;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestRule;
import org.skyscreamer.jsonassert.JSONAssert;
import org.skyscreamer.jsonassert.JSONCompareMode;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

@Ignore("TODO: graphql.schema.idl.errors.SchemaProblem: errors=[There is no type resolver defined for interface / union 'Document' type, There is no type resolver defined for interface / union 'Vote' type]")
public class GraphQLTest
{
    @Rule
    public final DropwizardAppRule<StackOverflowConfiguration> rule = new DropwizardAppRule<>(
            StackOverflowApplication.class,
            ResourceHelpers.resourceFilePath("config-test.json5"));

    @Rule
    public final TestRule reladomoTestRule = new ReladomoTestRuleBuilder()
            .setRuntimeConfigurationPath("reladomo-runtime-configuration/ReladomoRuntimeConfiguration.xml")
            .setConnectionSupplier(() ->
            {
                StackOverflowConfiguration configuration = this.rule.getConfiguration();
                Environment                environment   = this.rule.getEnvironment();
                return configuration
                        .getConnectionManagersFactory()
                        .getConnectionManagersByName(configuration, environment)
                        .get("h2-tcp")
                        .getConnection();
            })
            .build();

    protected Client getClient(String clientName)
    {
        var jerseyClientConfiguration = new JerseyClientConfiguration();
        jerseyClientConfiguration.setTimeout(Duration.minutes(5));

        return new JerseyClientBuilder(this.rule.getEnvironment())
                .using(jerseyClientConfiguration)
                .build(clientName);
    }

    @Test
    @ReladomoTestFile("test-data/existing-question.txt")
    public void smokeTest() throws JSONException
    {
        Client client = this.getClient("com.stackoverflow.dropwizard.test.GraphQLTest.smokeTest");

        //language=GraphQL
        String query = """
                query {
                    question(id: 1) {
                        id,
                        title,
                        body,
                        status,
                        deleted,
                        systemFrom,
                        systemTo,
                        createdById,
                        createdOn,
                        lastUpdatedById,
                        tags {
                            name,
                        },
                        version {
                            number,
                        },
                    }
                }""";

        //language=JSON
        String expected = """
                {
                  "data": {
                    "question": {
                      "id": 1,
                      "title": "test title 1",
                      "body": "test body 1",
                      "status": "OPEN",
                      "deleted": false,
                      "systemFrom": "1999-12-31T23:59:59.999Z",
                      "systemTo": null,
                      "createdById": "test user 1",
                      "createdOn": "1999-12-31T23:59:59.999Z",
                      "lastUpdatedById": "test user 1",
                      "tags": [
                        {
                          "name": "test tag 1"
                        },
                        {
                          "name": "test tag 2"
                        }
                      ],
                      "version": {
                        "number": 2
                      }
                    }
                  }
                }""";

        {
            // POST a body with field `query`
            Response response = client.target(
                    String.format("http://localhost:%d/graphql", this.rule.getLocalPort()))
                    .request()
                    .post(Entity.json(Maps.mutable.with("query", query)));
            this.assertResponseStatus(response, Status.OK);
            String jsonResponse = response.readEntity(String.class);
            JSONAssert.assertEquals(jsonResponse, expected, jsonResponse, JSONCompareMode.STRICT);
        }

        // GET with query param `query`
        Response response = client.target(
                String.format("http://localhost:%d/graphql?query={query}", this.rule.getLocalPort()))
                .resolveTemplate("query", query)
                .request()
                .get();
        this.assertResponseStatus(response, Status.OK);
        String jsonResponse = response.readEntity(String.class);
        JSONAssert.assertEquals(jsonResponse, expected, jsonResponse, JSONCompareMode.STRICT);
    }

    private void assertResponseStatus(@Nonnull Response response, Status status)
    {
        response.bufferEntity();
        String entityAsString = response.readEntity(String.class);
        assertThat(entityAsString, response.getStatusInfo(), is(status));
    }
}
