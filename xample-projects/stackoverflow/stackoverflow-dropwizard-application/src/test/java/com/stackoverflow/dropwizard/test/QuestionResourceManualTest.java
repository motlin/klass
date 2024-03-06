package com.stackoverflow.dropwizard.test;

import java.util.List;

import javax.annotation.Nonnull;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import com.liftwizard.reladomo.test.rule.ReladomoTestFile;
import com.liftwizard.reladomo.test.rule.ReladomoTestRule;
import com.stackoverflow.dropwizard.application.StackOverflowApplication;
import com.stackoverflow.dropwizard.application.StackOverflowConfiguration;
import io.dropwizard.client.JerseyClientBuilder;
import io.dropwizard.client.JerseyClientConfiguration;
import io.dropwizard.testing.ResourceHelpers;
import io.dropwizard.testing.junit.DropwizardAppRule;
import io.dropwizard.util.Duration;
import org.apache.commons.text.StringEscapeUtils;
import org.eclipse.collections.impl.factory.Lists;
import org.eclipse.collections.impl.list.mutable.ListAdapter;
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
public class QuestionResourceManualTest
{
    @ClassRule
    public static final DropwizardAppRule<StackOverflowConfiguration> RULE = new DropwizardAppRule<>(
            StackOverflowApplication.class,
            ResourceHelpers.resourceFilePath("config-test.yml"));

    @Rule
    public final ReladomoTestRule reladomoTestRule = new ReladomoTestRule(
            "reladomo-runtime-configuration/TestReladomoRuntimeConfiguration.xml");

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
    public void get_smoke_test() throws JSONException
    {
        Client client = this.getClient("com.stackoverflow.dropwizard.test.QuestionResourceManualTest.get_smoke_test");

        this.assertQuestion1Unchanged(client);
    }

    protected void assertQuestion1Unchanged(@Nonnull Client client) throws JSONException
    {
        Response response = client.target(
                String.format("http://localhost:%d/manual/api/question/{id}", RULE.getLocalPort()))
                .resolveTemplate("id", 1)
                .request()
                .get();

        this.assertResponseStatus(response, Status.OK);

        String jsonResponse = response.readEntity(String.class);
        //language=JSON
        String expected = ""
                + "{\n"
                + "  \"id\": 1,\n"
                + "  \"title\": \"test title 1\",\n"
                + "  \"body\": \"test body 1\",\n"
                + "  \"status\": \"Open\",\n"
                + "  \"deleted\": false,\n"
                + "  \"systemFrom\": \"1999-12-31T23:59:59.999Z\",\n"
                + "  \"systemTo\": null,\n"
                + "  \"createdById\": \"test user 1\",\n"
                + "  \"createdOn\": \"1999-12-31T23:59:59.999Z\",\n"
                + "  \"lastUpdatedById\": \"test user 1\",\n"
                + "  \"tags\": [\n"
                + "    {\n"
                + "      \"tag\": {\n"
                + "        \"name\": \"test tag 1\"\n"
                + "      }\n"
                + "    },\n"
                + "    {\n"
                + "      \"tag\": {\n"
                + "        \"name\": \"test tag 2\"\n"
                + "      }\n"
                + "    }\n"
                + "  ],\n"
                + "  \"version\": {\n"
                + "    \"number\": 2\n"
                + "  }\n"
                + "}\n";
        JSONAssert.assertEquals(jsonResponse, expected, jsonResponse, JSONCompareMode.STRICT);
    }

    @Test
    @ReladomoTestFile("test-data/existing-question.txt")
    public void post_invalid_data()
    {
        Client client = this.getClient("com.stackoverflow.dropwizard.test.QuestionResourceManualTest.post_invalid_data");

        //language=JSON
        String invalidJson = ""
                + "{\n"
                + "  \"title\": 1,\n"
                + "  \"status\": \"Invalid Choice\",\n"
                + "  \"deleted\": [],\n"
                + "  \"extra\": \"extra\",\n"
                + "  \"answers\": [\n"
                + "    {\n"
                + "      \"body\": 2,\n"
                + "      \"nestedExtra\": \"nestedExtra\",\n"
                + "      \"nestedExtraNull\": null\n"
                + "    }\n"
                + "  ],\n"
                + "  \"tags\": [\n"
                + "    {\n"
                + "      \"name\": [\n"
                + "        {}\n"
                + "      ],\n"
                + "      \"tag\": []\n"
                + "    },\n"
                + "    {\n"
                + "      \"name\": {\n"
                + "        \"name\": {}\n"
                + "      },\n"
                + "      \"tag\": {}\n"
                + "    },\n"
                + "    \"oops\"\n"
                + "  ],\n"
                + "  \"version\": {\n"
                + "    \"number\": 20000000000\n"
                + "  }\n"
                + "}\n";

        Response response = client.target(
                String.format("http://localhost:%d/manual/api/question/", RULE.getLocalPort()))
                .request()
                .post(Entity.json(invalidJson));

        this.assertResponseStatus(response, Status.BAD_REQUEST);

        List<String> errors = response.readEntity(new GenericType<List<String>>() {});
        String message = ListAdapter.adapt(errors)
                .collect(String::toString)
                .collect(StringEscapeUtils::escapeJava)
                .makeString("\n\"", "\",\n\"", "\"\n");
        assertThat(
                message,
                errors,
                is(Lists.immutable.with(
                        "Error at Question.title. Expected property with type 'Question.title: String' but got '1' with type 'number'.",
                        "Error at Question.status. Expected enumerated property with type 'Question.status: Status' but got \"Invalid Choice\" with type 'string'. Expected one of \"Open\", \"On hold\", \"Closed\".",
                        "Error at Question.deleted. Expected property with type 'Question.deleted: Boolean' but got '[]' with type 'array'.",
                        "Error at Question.extra. No such property 'Question.extra' but got \"extra\". Expected properties: body, id, title, status, deleted, system, systemFrom, systemTo, createdById, createdOn, lastUpdatedById, answers, votes, tags, version.",
                        "Error at Question.answers[0].body. Expected property with type 'Document.body: String' but got '2' with type 'number'.",
                        "Error at Question.answers[0].nestedExtra. No such property 'Answer.nestedExtra' but got \"nestedExtra\". Expected properties: body, id, deleted, questionId, system, systemFrom, systemTo, question, version.",
                        "Error at Question.answers[0].nestedExtraNull. No such property 'Answer.nestedExtraNull' but got null. Expected properties: body, id, deleted, questionId, system, systemFrom, systemTo, question, version.",
                        "Error at Question.tags[0].name. No such property 'QuestionTagMapping.name' but got [{}]. Expected properties: questionId, tagName, system, systemFrom, systemTo, question, tag.",
                        "Error at Question.tags[0].tag. Expected json object but value was array.",
                        "Error at Question.tags[1].name. No such property 'QuestionTagMapping.name' but got {\"name\":{}}. Expected properties: questionId, tagName, system, systemFrom, systemTo, question, tag.",
                        "Error at Question.tags[2]. Expected json object but value was string.",
                        "Error at Question.version.number. Expected property with type 'QuestionVersion.number: Integer' but got '20000000000' with type 'number'.",
                        "Error at Question.body. Expected value for required property 'Document.body: String' but value was missing.")));
        //</editor-fold>
    }

    @Test
    @ReladomoTestFile("test-data/existing-question.txt")
    public void post_valid_data() throws JSONException
    {
        Client client = this.getClient("com.stackoverflow.dropwizard.test.QuestionResourceManualTest.post_valid_data");

        //<editor-fold desc="POST valid json, status: CREATED">
        {
            //language=JSON
            String validJson = ""
                    + "{\n"
                    + "  \"title\": \"example title 2\",\n"
                    + "  \"body\": \"example body 2\",\n"
                    + "  \"status\": \"Open\",\n"
                    + "  \"deleted\": false,\n"
                    + "  \"createdById\": \"TODO\",\n"
                    + "  \"lastUpdatedById\": \"TODO\",\n"
                    + "  \"answers\": [],\n"
                    + "  \"tags\": [\n"
                    + "    {\n"
                    + "      \"tag\": {\n"
                    + "        \"name\": \"test tag 1\"\n"
                    + "      }\n"
                    + "    },\n"
                    + "    {\n"
                    + "      \"tag\": {\n"
                    + "        \"name\": \"test tag 3\"\n"
                    + "      }\n"
                    + "    }\n"
                    + "  ]\n"
                    + "}\n";

            Response response = client.target(
                    String.format("http://localhost:%d/manual/api/question/", RULE.getLocalPort()))
                    .request()
                    .post(Entity.json(validJson));

            this.assertResponseStatus(response, Status.CREATED);

            String body = response.readEntity(String.class);
            assertThat(body, is(""));

            assertThat(response.getLocation().getPath(), is("/manual/api/question/2"));
        }
        //</editor-fold>

        this.assertQuestion1Unchanged(client);

        //<editor-fold desc="GET id: 2, status: ok">
        {
            Response response = client.target(
                    String.format("http://localhost:%d/manual/api/question/{id}", RULE.getLocalPort()))
                    .resolveTemplate("id", 2)
                    .request()
                    .get();

            this.assertResponseStatus(response, Status.OK);

            String jsonResponse = response.readEntity(String.class);
            //language=JSON
            String expected = ""
                    + "{\n"
                    + "  \"id\": 2,\n"
                    + "  \"title\": \"example title 2\",\n"
                    + "  \"body\": \"example body 2\",\n"
                    + "  \"status\": \"Open\",\n"
                    + "  \"deleted\": false,\n"
                    + "  \"systemTo\": null,\n"
                    + "  \"createdById\": \"TODO\",\n"
                    + "  \"lastUpdatedById\": \"TODO\",\n"
                    + "  \"tags\": [\n"
                    + "    {\n"
                    + "      \"tag\": {\n"
                    + "        \"name\": \"test tag 1\"\n"
                    + "      }\n"
                    + "    },\n"
                    + "    {\n"
                    + "      \"tag\": {\n"
                    + "        \"name\": \"test tag 3\"\n"
                    + "      }\n"
                    + "    }\n"
                    + "  ],\n"
                    + "  \"version\": {\n"
                    + "    \"number\": 1\n"
                    + "  }\n"
                    + "}\n";
            JSONAssert.assertEquals(jsonResponse, expected, jsonResponse, JSONCompareMode.STRICT_ORDER);
        }
        //</editor-fold>
    }

    @Test
    @ReladomoTestFile("test-data/existing-question.txt")
    public void put_invalid_id() throws JSONException
    {
        Client client = this.getClient("com.stackoverflow.dropwizard.test.QuestionResourceManualTest.put_invalid_id");

        //language=JSON
        String json = ""
                + "{\n"
                + "  \"id\": 2,\n"
                + "  \"title\": \"edited title 1\",\n"
                + "  \"body\": \"edited body 1\",\n"
                + "  \"status\": \"On hold\",\n"
                + "  \"deleted\": true,\n"
                + "  \"systemFrom\": \"1999-12-31T23:59:59.999Z\",\n"
                + "  \"systemTo\": null,\n"
                + "  \"createdById\": \"test user 1\",\n"
                + "  \"createdOn\": \"2000-01-01T04:59:59.999Z\",\n"
                + "  \"lastUpdatedById\": \"test user 1\",\n"
                + "  \"answers\": [],\n"
                + "  \"tags\": [\n"
                + "    {\n"
                + "      \"tag\": {\n"
                + "        \"name\": \"test tag 1\"\n"
                + "      }\n"
                + "    },\n"
                + "    {\n"
                + "      \"tag\": {\n"
                + "        \"name\": \"test tag 3\"\n"
                + "      }\n"
                + "    }\n"
                + "  ],\n"
                + "  \"version\": {\n"
                + "    \"number\": 2\n"
                + "  }\n"
                + "}\n";

        Response response = client.target(
                String.format("http://localhost:%d/manual/api/question/{id}", RULE.getLocalPort()))
                .resolveTemplate("id", 1)
                .queryParam("version", "2")
                .request()
                .put(Entity.json(json));

        this.assertResponseStatus(response, Status.BAD_REQUEST);
        this.assertQuestion1Unchanged(client);
    }

    @Test
    @ReladomoTestFile("test-data/existing-question.txt")
    public void put_conflict() throws JSONException
    {
        Client client = this.getClient("com.stackoverflow.dropwizard.test.QuestionResourceManualTest.put_conflict");

        //language=JSON
        String validJson = ""
                + "{\n"
                + "  \"title\": \"edited title 1\",\n"
                + "  \"body\": \"edited body 1\",\n"
                + "  \"status\": \"Open\",\n"
                + "  \"deleted\": false,\n"
                + "  \"answers\": [],\n"
                + "  \"tags\": [],\n"
                + "  \"version\": {\n"
                + "    \"number\": 2\n"
                + "  }\n"
                + "}\n";

        Response response = client.target(
                String.format("http://localhost:%d/manual/api/question/{id}", RULE.getLocalPort()))
                .resolveTemplate("id", 1)
                .queryParam("version", "1")
                .request()
                .put(Entity.json(validJson));

        this.assertResponseStatus(response, Status.CONFLICT);

        this.assertQuestion1Unchanged(client);
    }

    @Test
    @ReladomoTestFile("test-data/existing-question.txt")
    public void put() throws JSONException
    {
        Client client = this.getClient("com.stackoverflow.dropwizard.test.QuestionResourceManualTest.put");

        //<editor-fold desc="PUT id: 1, version: 2, status: NO_CONTENT">
        {
            //language=JSON
            String validJson = ""
                    + "{\n"
                    + "  \"id\": 1,\n"
                    + "  \"title\": \"edited title 1\",\n"
                    + "  \"body\": \"edited body 1\",\n"
                    + "  \"status\": \"On hold\",\n"
                    + "  \"deleted\": true,\n"
                    + "  \"systemFrom\": \"1999-12-31T23:59:59.999Z\",\n"
                    + "  \"systemTo\": null,\n"
                    + "  \"createdById\": \"test user 1\",\n"
                    + "  \"createdOn\": \"1999-12-31T23:59:59.999Z\",\n"
                    + "  \"lastUpdatedById\": \"test user 1\",\n"
                    + "  \"tags\": [\n"
                    + "    {\n"
                    + "      \"tag\": {\n"
                    + "        \"name\": \"test tag 1\"\n"
                    + "      }\n"
                    + "    },\n"
                    + "    {\n"
                    + "      \"tag\": {\n"
                    + "        \"name\": \"test tag 3\"\n"
                    + "      }\n"
                    + "    }\n"
                    + "  ],\n"
                    + "  \"version\": {\n"
                    + "    \"number\": 2\n"
                    + "  }\n"
                    + "}\n";

            Response response = client.target(
                    String.format("http://localhost:%d/manual/api/question/{id}", RULE.getLocalPort()))
                    .resolveTemplate("id", 1)
                    .queryParam("version", "2")
                    .request()
                    .put(Entity.json(validJson));

            this.assertResponseStatus(response, Status.NO_CONTENT);
        }
        //</editor-fold>

        Response response = client.target(
                String.format("http://localhost:%d/manual/api/question/{id}", RULE.getLocalPort()))
                .resolveTemplate("id", 1)
                .request()
                .get();

        this.assertResponseStatus(response, Status.OK);

        String jsonResponse = response.readEntity(String.class);
        //language=JSON
        String expected = ""
                + "{\n"
                + "  \"id\": 1,\n"
                + "  \"title\": \"edited title 1\",\n"
                + "  \"body\": \"edited body 1\",\n"
                + "  \"status\": \"On hold\",\n"
                + "  \"deleted\": true,\n"
                + "  \"systemTo\": null,\n"
                + "  \"createdById\": \"test user 1\",\n"
                + "  \"createdOn\": \"1999-12-31T23:59:59.999Z\",\n"
                + "  \"lastUpdatedById\": \"test user 1\",\n"
                + "  \"tags\": [\n"
                + "    {\n"
                + "      \"tag\": {\n"
                + "        \"name\": \"test tag 1\"\n"
                + "      }\n"
                + "    },\n"
                + "    {\n"
                + "      \"tag\": {\n"
                + "        \"name\": \"test tag 3\"\n"
                + "      }\n"
                + "    }\n"
                + "  ],\n"
                + "  \"version\": {\n"
                + "    \"number\": 3\n"
                + "  }\n"
                + "}";
        JSONAssert.assertEquals(jsonResponse, expected, jsonResponse, JSONCompareMode.STRICT_ORDER);

        // TODO: PUT with owned children, with all four cases of unchanged, created, updated, deleted
    }

    @Ignore("TODO: Only increment version number when data actually changes.")
    @Test
    @ReladomoTestFile("test-data/existing-question.txt")
    public void put_unchanged() throws JSONException
    {
        Client client = this.getClient("com.stackoverflow.dropwizard.test.QuestionResourceManualTest.put_unchanged");

        //language=JSON
        String json = ""
                + "{\n"
                + "  \"id\": 1,\n"
                + "  \"title\": \"test title 1\",\n"
                + "  \"body\": \"test body 1\",\n"
                + "  \"status\": \"Open\",\n"
                + "  \"deleted\": false,\n"
                + "  \"systemFrom\": \"1999-12-31T23:59:59.999Z\",\n"
                + "  \"systemTo\": null,\n"
                + "  \"createdById\": \"test user 1\",\n"
                + "  \"createdOn\": \"1999-12-31T23:59:59.999Z\",\n"
                + "  \"lastUpdatedById\": \"test user 1\",\n"
                + "  \"tags\": [\n"
                + "    {\n"
                + "      \"tag\": {\n"
                + "        \"name\": \"test tag 1\"\n"
                + "      }\n"
                + "    },\n"
                + "    {\n"
                + "      \"tag\": {\n"
                + "        \"name\": \"test tag 2\"\n"
                + "      }\n"
                + "    }\n"
                + "  ],\n"
                + "  \"version\": {\n"
                + "    \"number\": 2\n"
                + "  }\n"
                + "}\n";

        Response response = client.target(
                String.format("http://localhost:%d/manual/api/question/{id}", RULE.getLocalPort()))
                .resolveTemplate("id", 1)
                .queryParam("version", "2")
                .request()
                .put(Entity.json(json));

        this.assertResponseStatus(response, Status.NO_CONTENT);

        this.assertQuestion1Unchanged(client);
    }

    // TODO: Should PUT return the version number as an indicator that something changed? Or some other HTTP code?

    public void assertResponseStatus(@Nonnull Response response, Status status)
    {
        response.bufferEntity();
        String entityAsString = response.readEntity(String.class);
        assertThat(entityAsString, response.getStatusInfo(), is(status));
    }
}
