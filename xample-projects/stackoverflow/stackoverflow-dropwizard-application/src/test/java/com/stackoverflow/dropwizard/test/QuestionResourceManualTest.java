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
import io.liftwizard.junit.rule.match.json.JsonMatchRule;
import io.liftwizard.reladomo.test.rule.ReladomoLoadDataTestRule;
import io.liftwizard.reladomo.test.rule.ReladomoTestFile;
import org.json.JSONException;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.RuleChain;
import org.junit.rules.TestRule;
import org.skyscreamer.jsonassert.JSONAssert;
import org.skyscreamer.jsonassert.JSONCompareMode;

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
            throws JSONException
    {
        Client client = this.getClient("get_smoke_test");

        this.assertQuestion1Unchanged(client);
    }

    protected void assertQuestion1Unchanged(@Nonnull Client client)
            throws JSONException
    {
        Response response = client
                .target("http://localhost:{port}/api/manual/question/{id}")
                .resolveTemplate("port", this.appRule.getLocalPort())
                .resolveTemplate("id", 1)
                .request()
                .get();

        this.assertResponseStatus(response, Status.OK);

        String jsonResponse = response.readEntity(String.class);
        //language=JSON
        String expected = """
                {
                  "id": 1,
                  "title": "test title 1",
                  "body": "test body 1",
                  "status": "Open",
                  "deleted": false,
                  "systemFrom": "1999-12-31T23:59:59.999Z",
                  "systemTo": null,
                  "createdOn": "1999-12-31T23:59:59.999Z",
                  "answers": [],
                  "tags": [
                    {
                      "tag": {
                        "name": "test tag 1"
                      }
                    },
                    {
                      "tag": {
                        "name": "test tag 2"
                      }
                    }
                  ],
                  "version": {
                    "number": 2
                  }
                }
                """;
        JSONAssert.assertEquals(jsonResponse, expected, jsonResponse, JSONCompareMode.STRICT);
    }

    @Test
    @ReladomoTestFile("test-data/existing-question.txt")
    public void post_invalid_data()
    {
        Client client = this.getClient("post_invalid_data");

        //language=JSON
        String invalidJson = """
                {
                  "title": 1,
                  "status": "Invalid Choice",
                  "deleted": [],
                  "extra": "extra",
                  "answers": [
                    {
                      "body": 2,
                      "nestedExtra": "nestedExtra",
                      "nestedExtraNull": null
                    }
                  ],
                  "tags": [
                    {
                      "name": [
                        {}
                      ],
                      "tag": []
                    },
                    {
                      "name": {
                        "name": {}
                      },
                      "tag": {}
                    },
                    "oops"
                  ],
                  "version": {
                    "number": 20000000000
                  }
                }
                """;

        Response response = client
                .target("http://localhost:{port}/api/manual/question/")
                .resolveTemplate("port", this.appRule.getLocalPort())
                .request()
                .header("Authorization", "Impersonation User ID")
                .post(Entity.json(invalidJson));

        this.assertResponse("post_invalid_data", Status.BAD_REQUEST, response);
    }

    @Test
    @ReladomoTestFile("test-data/existing-question.txt")
    public void post_valid_data()
            throws JSONException
    {
        Client client = this.getClient("post_valid_data");

        //<editor-fold desc="POST valid json, status: CREATED">
        {
            //language=JSON
            String validJson = """
                    {
                      "title": "example title 2",
                      "body": "example body 2",
                      "status": "Open",
                      "deleted": false,
                      "createdById": "TODO",
                      "lastUpdatedById": "TODO",
                      "answers": [],
                      "tags": [
                        {
                          "tag": {
                            "name": "test tag 1"
                          }
                        },
                        {
                          "tag": {
                            "name": "test tag 3"
                          }
                        }
                      ]
                    }
                    """;

            Response response = client
                    .target("http://localhost:{port}/api/manual/question/")
                    .resolveTemplate("port", this.appRule.getLocalPort())
                    .request()
                    .header("Authorization", "Impersonation User ID")
                    .post(Entity.json(validJson));

            this.assertEmptyResponse(Status.CREATED, response);

            String body = response.readEntity(String.class);
            assertThat(body, is(""));

            assertThat(response.getLocation().getPath(), is("/api/manual/question/2"));
        }
        //</editor-fold>

        this.assertQuestion1Unchanged(client);

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
            throws JSONException
    {
        Client client = this.getClient("put_invalid_id");

        //language=JSON
        String json = """
                {
                  "id": 2,
                  "title": "edited title 1",
                  "body": "edited body 1",
                  "status": "On hold",
                  "deleted": true,
                  "systemFrom": "1999-12-31T23:59:59.999Z",
                  "systemTo": null,
                  "createdById": "test user 1",
                  "createdOn": "1999-12-31T23:59:59.999Z",
                  "lastUpdatedById": "test user 1",
                  "answers": [],
                  "tags": [
                    {
                      "tag": {
                        "name": "test tag 1"
                      }
                    },
                    {
                      "tag": {
                        "name": "test tag 3"
                      }
                    }
                  ],
                  "version": {
                    "number": 2
                  }
                }
                """;

        Response response = client
                .target("http://localhost:{port}/api/manual/question/{id}")
                .resolveTemplate("port", this.appRule.getLocalPort())
                .resolveTemplate("id", 1)
                .queryParam("version", "2")
                .request()
                .header("Authorization", "Impersonation User ID")
                .put(Entity.json(json));

        this.assertResponseStatus(response, Status.NO_CONTENT);
        this.assertQuestion1Unchanged(client);
    }

    @Test
    @ReladomoTestFile("test-data/existing-question.txt")
    public void put_conflict()
            throws JSONException
    {
        Client client = this.getClient("put_conflict");

        //language=JSON
        String validJson = """
                {
                  "title": "edited title 1",
                  "body": "edited body 1",
                  "status": "Open",
                  "deleted": false,
                  "answers": [],
                  "tags": [],
                  "version": {
                    "number": 2
                  }
                }
                """;

        Response response = client
                .target("http://localhost:{port}/api/manual/question/{id}")
                .resolveTemplate("port", this.appRule.getLocalPort())
                .resolveTemplate("id", 1)
                .queryParam("version", "1")
                .request()
                .header("Authorization", "Impersonation User ID")
                .put(Entity.json(validJson));

        this.assertResponseStatus(response, Status.CONFLICT);

        this.assertQuestion1Unchanged(client);
    }

    @Test
    @ReladomoTestFile("test-data/existing-question.txt")
    public void put()
    {
        Client client = this.getClient("put");

        //<editor-fold desc="PUT id: 1, version: 2, status: NO_CONTENT">
        {
            //language=JSON
            String validJson = """
                    {
                      "id": 1,
                      "title": "edited title 1",
                      "body": "edited body 1",
                      "status": "On hold",
                      "deleted": true,
                      "systemFrom": "1999-12-31T23:59:59.999Z",
                      "systemTo": null,
                      "createdById": "test user 1",
                      "createdOn": "1999-12-31T23:59:59.999Z",
                      "lastUpdatedById": "test user 1",
                      "tags": [
                        {
                          "tag": {
                            "name": "test tag 1"
                          }
                        },
                        {
                          "tag": {
                            "name": "test tag 3"
                          }
                        }
                      ],
                      "version": {
                        "number": 2
                      }
                    }
                    """;

            Response response = client
                    .target("http://localhost:{port}/api/manual/question/{id}")
                    .resolveTemplate("port", this.appRule.getLocalPort())
                    .resolveTemplate("id", 1)
                    .queryParam("version", "2")
                    .request()
                    .header("Authorization", "Impersonation User ID")
                    .put(Entity.json(validJson));

            this.assertEmptyResponse(Status.NO_CONTENT, response);
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
            throws JSONException
    {
        Client client = this.getClient("put_unchanged");

        //language=JSON
        String json = """
                {
                  "id": 1,
                  "title": "test title 1",
                  "body": "test body 1",
                  "status": "Open",
                  "deleted": false,
                  "systemFrom": "1999-12-31T23:59:59.999Z",
                  "systemTo": null,
                  "createdById": "test user 1",
                  "createdOn": "1999-12-31T23:59:59.999Z",
                  "lastUpdatedById": "test user 1",
                  "tags": [
                    {
                      "tag": {
                        "name": "test tag 1"
                      }
                    },
                    {
                      "tag": {
                        "name": "test tag 2"
                      }
                    }
                  ],
                  "version": {
                    "number": 2
                  }
                }
                """;

        Response response = client
                .target("http://localhost:{port}/api/manual/question/{id}")
                .resolveTemplate("port", this.appRule.getLocalPort())
                .resolveTemplate("id", 1)
                .queryParam("version", "2")
                .request()
                .header("Authorization", "Impersonation User ID")
                .put(Entity.json(json));

        this.assertEmptyResponse(Status.NO_CONTENT, response);

        this.assertQuestion1Unchanged(client);
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
