package com.stackoverflow.dropwizard.test;

import java.util.concurrent.TimeUnit;

import javax.annotation.Nonnull;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import cool.klass.reladomo.test.rule.ReladomoTestFile;
import cool.klass.reladomo.test.rule.ReladomoTestRule;
import com.stackoverflow.dropwizard.application.StackOverflowApplication;
import com.stackoverflow.dropwizard.application.StackOverflowConfiguration;
import com.stackoverflow.graphql.StackOverflowGraphQL;
import io.dropwizard.client.JerseyClientBuilder;
import io.dropwizard.client.JerseyClientConfiguration;
import io.dropwizard.testing.ResourceHelpers;
import io.dropwizard.testing.junit.DropwizardAppRule;
import io.dropwizard.util.Duration;
import org.eclipse.collections.impl.factory.Maps;
import org.json.JSONException;
import org.junit.ClassRule;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.skyscreamer.jsonassert.JSONAssert;
import org.skyscreamer.jsonassert.JSONCompareMode;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

@Ignore("TODO: graphql.schema.idl.errors.SchemaProblem: errors=[There is no type resolver defined for interface / union 'Document' type, There is no type resolver defined for interface / union 'Vote' type]")
public class GraphQLTest
{
    @ClassRule
    public static final DropwizardAppRule<StackOverflowConfiguration> RULE = new DropwizardAppRule<>(
            StackOverflowApplication.class,
            ResourceHelpers.resourceFilePath("config-test.yml"));

    @Rule
    public final ReladomoTestRule reladomoTestRule = new ReladomoTestRule(
            "reladomo-runtime-configuration/TestReladomoRuntimeConfiguration.xml")
            .transactionTimeout(5, TimeUnit.MINUTES);

    protected Client getClient(String clientName)
    {
        JerseyClientConfiguration jerseyClientConfiguration = new JerseyClientConfiguration();
        jerseyClientConfiguration.setTimeout(Duration.minutes(5));

        return new JerseyClientBuilder(RULE.getEnvironment())
                .using(jerseyClientConfiguration)
                .build(clientName);
    }

    @Test
    @ReladomoTestFile("test-data/existing-question.txt")
    public void smokeTest() throws JSONException
    {
        StackOverflowGraphQL.runSampleQuery();

        Client client = this.getClient("com.stackoverflow.dropwizard.test.GraphQLTest.smokeTest");

        //language=GraphQL
        String query = ""
                + "query {\n"
                + "    question(id: 1) {\n"
                + "        id,\n"
                + "        title,\n"
                + "        body,\n"
                + "        status,\n"
                + "        deleted,\n"
                + "        systemFrom,\n"
                + "        systemTo,\n"
                + "        createdById,\n"
                + "        createdOn,\n"
                + "        lastUpdatedById,\n"
                + "        tags {\n"
                + "            name,\n"
                + "        },\n"
                + "        version {\n"
                + "            number,\n"
                + "        },\n"
                + "    }\n"
                + "}";

        //language=JSON
        String expected = ""
                + "{\n"
                + "  \"data\": {\n"
                + "    \"question\": {\n"
                + "      \"id\": 1,\n"
                + "      \"title\": \"test title 1\",\n"
                + "      \"body\": \"test body 1\",\n"
                + "      \"status\": \"OPEN\",\n"
                + "      \"deleted\": false,\n"
                + "      \"systemFrom\": \"1999-12-31T23:59:59.999Z\",\n"
                + "      \"systemTo\": null,\n"
                + "      \"createdById\": \"test user 1\",\n"
                + "      \"createdOn\": \"1999-12-31T23:59:59.999Z\",\n"
                + "      \"lastUpdatedById\": \"test user 1\",\n"
                + "      \"tags\": [\n"
                + "        {\n"
                + "          \"name\": \"test tag 1\"\n"
                + "        },\n"
                + "        {\n"
                + "          \"name\": \"test tag 2\"\n"
                + "        }\n"
                + "      ],\n"
                + "      \"version\": {\n"
                + "        \"number\": 2\n"
                + "      }\n"
                + "    }\n"
                + "  }\n"
                + "}";

        {
            // POST a body with field `query`
            Response response = client.target(
                    String.format("http://localhost:%d/graphql", RULE.getLocalPort()))
                    .request()
                    .post(Entity.json(Maps.mutable.with("query", query)));
            this.assertResponseStatus(response, Status.OK);
            String jsonResponse = response.readEntity(String.class);
            JSONAssert.assertEquals(jsonResponse, expected, jsonResponse, JSONCompareMode.STRICT);
        }

        {
            // GET with query param `query`
            Response response = client.target(
                    String.format("http://localhost:%d/graphql?query={query}", RULE.getLocalPort()))
                    .resolveTemplate("query", query)
                    .request()
                    .get();
            this.assertResponseStatus(response, Status.OK);
            String jsonResponse = response.readEntity(String.class);
            JSONAssert.assertEquals(jsonResponse, expected, jsonResponse, JSONCompareMode.STRICT);
        }
    }

    public void assertResponseStatus(@Nonnull Response response, Status status)
    {
        response.bufferEntity();
        String entityAsString = response.readEntity(String.class);
        assertThat(entityAsString, response.getStatusInfo(), is(status));
    }
}
