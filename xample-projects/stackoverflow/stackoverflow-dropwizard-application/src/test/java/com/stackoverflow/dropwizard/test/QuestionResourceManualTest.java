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
import io.dropwizard.testing.ResourceHelpers;
import io.dropwizard.testing.junit.DropwizardAppRule;
import io.dropwizard.util.Duration;
import io.liftwizard.junit.rule.log.marker.LogMarkerTestRule;
import io.liftwizard.junit.rule.match.file.FileMatchRule;
import io.liftwizard.junit.rule.match.json.JsonMatchRule;
import io.liftwizard.reladomo.test.rule.ReladomoLoadDataTestRule;
import io.liftwizard.reladomo.test.rule.ReladomoTestFile;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.RuleChain;
import org.junit.rules.TestRule;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;

public class QuestionResourceManualTest
{
    @Rule
    public final JsonMatchRule jsonMatchRule = new JsonMatchRule(this.getClass());

    protected final DropwizardAppRule<StackOverflowConfiguration> appRule = new DropwizardAppRule<>(
            StackOverflowApplication.class,
            ResourceHelpers.resourceFilePath("config-test.json5"));

    protected final TestRule reladomoLoadDataTestRule = new ReladomoLoadDataTestRule();
    protected final TestRule logMarkerTestRule        = new LogMarkerTestRule();

    @Rule
    public final TestRule rule = RuleChain
            .outerRule(this.appRule)
            .around(this.reladomoLoadDataTestRule)
            .around(this.logMarkerTestRule);

    protected Client getClient(String testName)
    {
        var jerseyClientConfiguration = new JerseyClientConfiguration();
        jerseyClientConfiguration.setTimeout(Duration.minutes(5));

        String className  = this.getClass().getCanonicalName();
        String clientName = className + "." + testName;

        return new JerseyClientBuilder(this.appRule.getEnvironment())
                .using(jerseyClientConfiguration)
                .build(clientName);
    }

    protected void assertEmptyResponse(Status expectedStatus, Response actualResponse)
    {
        assertFalse(actualResponse.hasEntity());
        assertThat(actualResponse.getStatusInfo(), is(expectedStatus));
    }

    protected void assertResponse(String testName, Status expectedStatus, Response actualResponse)
    {
        this.assertResponseStatus(actualResponse, expectedStatus);
        String actualJsonResponse = actualResponse.readEntity(String.class);

        String expectedResponseClassPathLocation = this.getClass().getSimpleName() + "." + testName + ".json";

        this.jsonMatchRule.assertFileContents(expectedResponseClassPathLocation, actualJsonResponse);
    }

    protected void assertResponseStatus(@Nonnull Response response, Status status)
    {
        response.bufferEntity();
        String entityAsString = response.readEntity(String.class);
        assertThat(entityAsString, response.getStatusInfo(), is(status));
    }

    @Test
    @ReladomoTestFile("test-data/existing-question.txt")
    public void get_smoke_test()
    {
        Client client = this.getClient("get_smoke_test");

        this.assertQuestion1Unchanged(client, "assertQuestion1Unchanged_get_smoke_test");
    }

    protected void assertQuestion1Unchanged(@Nonnull Client client, String testName)
    {
        Response response = client
                .target("http://localhost:{port}/api/manual/question/{id}")
                .resolveTemplate("port", this.appRule.getLocalPort())
                .resolveTemplate("id", 1)
                .request()
                .get();

        this.assertResponse(testName, Status.OK, response);
    }

    @Test
    @ReladomoTestFile("test-data/existing-question.txt")
    public void post_invalid_data()
    {
        Client client = this.getClient("post_invalid_data");

        String invalidJson = FileMatchRule.slurp(
                this.getClass().getSimpleName() + ".invalid_data.json5",
                this.getClass());

        Response response = client
                .target("http://localhost:{port}/api/manual/question/")
                .resolveTemplate("port", this.appRule.getLocalPort())
                .request()
                .header("Authorization", "Impersonation test user 1")
                .post(Entity.json(invalidJson));

        this.assertResponse("post_invalid_data", Status.BAD_REQUEST, response);
    }

    @Test
    @ReladomoTestFile("test-data/existing-question.txt")
    public void post_valid_data()
    {
        Client client = this.getClient("post_valid_data");

        //<editor-fold desc="POST valid json, status: CREATED">
        {
            String validJson = FileMatchRule.slurp(
                    this.getClass().getSimpleName() + ".create_data.json5",
                    this.getClass());

            Response response = client
                    .target("http://localhost:{port}/api/manual/question/")
                    .resolveTemplate("port", this.appRule.getLocalPort())
                    .request()
                    .header("Authorization", "Impersonation test user 1")
                    .post(Entity.json(validJson));

            this.assertResponse("post_valid_data", Status.CREATED, response);
            assertThat(response.getLocation().getPath(), is("/api/manual/question/2"));
        }
        //</editor-fold>

        this.assertQuestion1Unchanged(client, "assertQuestion1Unchanged_post_valid_data");

        //<editor-fold desc="GET id: 2, status: ok">
        {
            Response response = client
                    .target("http://localhost:{port}/api/manual/question/{id}")
                    .resolveTemplate("port", this.appRule.getLocalPort())
                    .resolveTemplate("id", 2)
                    .request()
                    .get();

            this.assertResponse("post_valid_data_get", Status.OK, response);
        }
        //</editor-fold>
    }

    @Ignore("TODO: Add an error when the id in the body doesn't match the id in the url. #1650")
    @Test
    @ReladomoTestFile("test-data/existing-question.txt")
    public void put_invalid_id()
    {
        Client client = this.getClient("put_invalid_id");

        String json = FileMatchRule.slurp(
                this.getClass().getSimpleName() + ".invalid_id_data.json5",
                this.getClass());

        Response response = client
                .target("http://localhost:{port}/api/manual/question/{id}")
                .resolveTemplate("port", this.appRule.getLocalPort())
                .resolveTemplate("id", 1)
                .queryParam("version", "2")
                .request()
                .header("Authorization", "Impersonation test user 1")
                .put(Entity.json(json));

        this.assertEmptyResponse(Status.NO_CONTENT, response);
        this.assertQuestion1Unchanged(client, "assertQuestion1Unchanged_put_invalid_id");
    }

    @Test
    @ReladomoTestFile("test-data/existing-question.txt")
    public void put_conflict()
    {
        Client client = this.getClient("put_conflict");

        String validJson = FileMatchRule.slurp(
                this.getClass().getSimpleName() + ".valid_versioned_put_data.json5",
                this.getClass());

        Response response = client
                .target("http://localhost:{port}/api/manual/question/{id}")
                .resolveTemplate("port", this.appRule.getLocalPort())
                .resolveTemplate("id", 1)
                .queryParam("version", "1")
                .request()
                .header("Authorization", "Impersonation test user 1")
                .put(Entity.json(validJson));

        this.assertResponse("put_conflict", Status.CONFLICT, response);

        this.assertQuestion1Unchanged(client, "assertQuestion1Unchanged_put_conflict");
    }

    @Test
    @ReladomoTestFile("test-data/existing-question.txt")
    public void put()
    {
        Client client = this.getClient("put");

        //<editor-fold desc="PUT id: 1, version: 2, status: NO_CONTENT">
        {
            String validJson = FileMatchRule.slurp(
                    this.getClass().getSimpleName() + ".valid_versioned_put_data.json5",
                    this.getClass());

            Response response = client
                    .target("http://localhost:{port}/api/manual/question/{id}")
                    .resolveTemplate("port", this.appRule.getLocalPort())
                    .resolveTemplate("id", 1)
                    .queryParam("version", "2")
                    .request()
                    .header("Authorization", "Impersonation test user 1")
                    .put(Entity.json(validJson));

            this.assertResponse("put", Status.OK, response);
        }
        //</editor-fold>

        Response response = client
                .target("http://localhost:{port}/api/manual/question/{id}")
                .resolveTemplate("port", this.appRule.getLocalPort())
                .resolveTemplate("id", 1)
                .request()
                .get();

        this.assertResponse("put2", Status.OK, response);

        // TODO: PUT with owned children, with all four cases of unchanged, created, updated, deleted
    }

    @Test
    @ReladomoTestFile("test-data/existing-question.txt")
    public void put_unchanged()
    {
        Client client = this.getClient("put_unchanged");

        String jsonName = this.getClass().getSimpleName() + ".put_unchanged.json5";
        String json     = FileMatchRule.slurp(jsonName, this.getClass());

        Response response = client
                .target("http://localhost:{port}/api/manual/question/{id}")
                .resolveTemplate("port", this.appRule.getLocalPort())
                .resolveTemplate("id", 1)
                .queryParam("version", "2")
                .request()
                .header("Authorization", "Impersonation test user 1")
                .put(Entity.json(json));

        this.assertResponse("put_unchanged", Status.OK, response);

        this.assertQuestion1Unchanged(client, "assertQuestion1Unchanged_put_unchanged");
    }

    @Test
    public void restSet()
    {
        Client client = this.getClient("restSet");

        {
            Response response = client
                    .target("http://localhost:{port}/api/manual/set")
                    .resolveTemplate("port", this.appRule.getLocalPort())
                    .request()
                    .get();

            this.assertResponse("restSet", Status.OK, response);
        }

        {
            Response response = client
                    .target("http://localhost:{port}/api/manual/map")
                    .resolveTemplate("port", this.appRule.getLocalPort())
                    .request()
                    .get();

            this.assertResponse("restMap", Status.OK, response);
        }
    }

    // TODO: Should PUT return the version number as an indicator that something changed? Or some other HTTP code?
}
