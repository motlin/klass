package com.stackoverflow.dropwizard.test;

import java.util.List;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import cool.klass.reladomo.test.rule.ReladomoTestFile;
import cool.klass.reladomo.test.rule.ReladomoTestRule;
import com.stackoverflow.dropwizard.application.StackOverflowApplication;
import com.stackoverflow.dropwizard.application.StackOverflowConfiguration;
import io.dropwizard.client.JerseyClientBuilder;
import io.dropwizard.client.JerseyClientConfiguration;
import io.dropwizard.jersey.validation.ValidationErrorMessage;
import io.dropwizard.testing.ResourceHelpers;
import io.dropwizard.testing.junit.DropwizardAppRule;
import io.dropwizard.util.Duration;
import org.eclipse.collections.impl.factory.Lists;
import org.json.JSONException;
import org.junit.ClassRule;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.skyscreamer.jsonassert.JSONAssert;
import org.skyscreamer.jsonassert.JSONCompareMode;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class QuestionResourceManualTest
{
    @ClassRule
    public static final DropwizardAppRule<StackOverflowConfiguration> RULE =
            new DropwizardAppRule<StackOverflowConfiguration>(
                    StackOverflowApplication.class,
                    ResourceHelpers.resourceFilePath("config-test.yml"));

    @Rule
    public final ReladomoTestRule reladomoTestRule = new ReladomoTestRule(
            "reladomo-runtime-configuration/TestReladomoRuntimeConfiguration.xml");

    @Ignore
    @Test
    @ReladomoTestFile("test-data/existing-question.txt")
    public void put() throws JSONException
    {
        JerseyClientConfiguration jerseyClientConfiguration = new JerseyClientConfiguration();
        jerseyClientConfiguration.setTimeout(Duration.minutes(5));

        Client client = new JerseyClientBuilder(RULE.getEnvironment())
                .using(jerseyClientConfiguration)
                .build("test client");

        {
            Response response = client.target(
                    String.format("http://localhost:%d/manual/api/question/{id}", RULE.getLocalPort()))
                    .resolveTemplate("id", 1)
                    .request()
                    .get();

            assertThat(response.getStatusInfo(), is(Status.OK));
            response.bufferEntity();
            String jsonResponse = response.readEntity(String.class);
            String expected     = "{\n"
                    + "  \"id\" : 1,\n"
                    + "  \"title\" : \"test title 1\",\n"
                    + "  \"body\" : \"test body 1\",\n"
                    + "  \"status\" : \"Open\",\n"
                    + "  \"deleted\" : false,\n"
                    + "  \"systemFrom\" : \"1999-12-31T23:59:59.999Z\",\n"
                    + "  \"systemTo\" : null,\n"
                    + "  \"createdById\" : \"test user 1\",\n"
                    + "  \"createdOn\" : \"2000-01-01T04:59:59.999Z\",\n"
                    + "  \"lastUpdatedById\" : \"test user 1\",\n"
                    + "  \"answers\" : [ ],\n"
                    + "  \"version\" : {\n"
                    + "    \"number\" : 2\n"
                    + "  }\n"
                    + "}";
            JSONAssert.assertEquals(expected, jsonResponse, JSONCompareMode.STRICT);
        }

        //language=JSON
        String json = ""
                + "{\n"
                + "  \"title\" : \"edited title 1\",\n"
                + "  \"body\" : \"edited body 1\",\n"
                + "  \"status\" : \"Open\",\n"
                + "  \"deleted\" : false,\n"
                + "  \"answers\" : [ ],\n"
                + "  \"version\" : {\n"
                + "    \"number\" : 2\n"
                + "  }\n"
                + "}\n";
        {
            Response response = client.target(
                    String.format("http://localhost:%d/manual/api/question/{id}", RULE.getLocalPort()))
                    .resolveTemplate("id", 1)
                    .queryParam("version", "1")
                    .request()
                    .put(Entity.json(json));

            response.bufferEntity();
            ValidationErrorMessage validationErrorMessage = response.readEntity(ValidationErrorMessage.class);
            assertThat(
                    validationErrorMessage,
                    is(new ValidationErrorMessage(com.google.common.collect.ImmutableList.of(
                            "systemFrom may not be null",
                            "createdById may not be null",
                            "createdOn may not be null",
                            "systemTo may not be null",
                            "lastUpdatedById may not be null",
                            "system may not be null"))));

            assertThat(response.getStatusInfo(), is(Status.CONFLICT));
            assertThat(response.getStatusInfo().getReasonPhrase(), is(""));
            assertThat(response.getStatusInfo().getFamily(), is(""));
        }

        {
            Response response = client.target(
                    String.format("http://localhost:%d/manual/api/question/{id}", RULE.getLocalPort()))
                    .resolveTemplate("id", 1)
                    .queryParam("version", "2")
                    .request()
                    .put(Entity.json(json));

            assertThat(response.getStatusInfo().getStatusCode(), is(422));
            assertThat(response.getStatusInfo().getReasonPhrase(), is(""));
            assertThat(response.getStatusInfo().getFamily(), is(""));

            response.bufferEntity();
            List<String> errors = response.readEntity(new GenericType<List<String>>() {});
            assertThat(errors, is(Lists.immutable.with("asf")));
        }

        {
            Response response = client.target(
                    String.format("http://localhost:%d/manual/api/question/{id}", RULE.getLocalPort()))
                    .resolveTemplate("id", 1)
                    .request()
                    .get();

            assertThat(response.getStatusInfo(), is(Status.OK));
            response.bufferEntity();
            String jsonResponse = response.readEntity(String.class);
            String expected     = "{\n"
                    + "  \"id\" : 1,\n"
                    + "  \"title\" : \"test title 1\",\n"
                    + "  \"body\" : \"test body 1\",\n"
                    + "  \"status\" : \"Open\",\n"
                    + "  \"deleted\" : false,\n"
                    + "  \"systemFrom\" : \"1999-12-31T23:59:59.999Z\",\n"
                    + "  \"systemTo\" : null,\n"
                    + "  \"createdById\" : \"test user 1\",\n"
                    + "  \"createdOn\" : \"2000-01-01T04:59:59.999Z\",\n"
                    + "  \"lastUpdatedById\" : \"test user 1\",\n"
                    + "  \"answers\" : [ ],\n"
                    + "  \"version\" : {\n"
                    + "    \"number\" : 2\n"
                    + "  }\n"
                    + "}";
            JSONAssert.assertEquals(expected, jsonResponse, JSONCompareMode.STRICT);
        }
    }
}
