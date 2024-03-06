package com.stackoverflow.dropwizard.test;

import javax.annotation.Nonnull;
import javax.ws.rs.client.Client;
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
import org.junit.Rule;
import org.junit.rules.RuleChain;
import org.junit.rules.TestRule;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class AbstractStackOverflowApplicationTest
{
    @Rule
    public final JsonMatchRule jsonMatchRule = new JsonMatchRule(this.getClass());

    protected final DropwizardAppRule<StackOverflowConfiguration> appRule = new DropwizardAppRule<>(
            StackOverflowApplication.class,
            ResourceHelpers.resourceFilePath("config-test.json5"));

    protected final TestRule reladomoLoadDataTestRule = new ReladomoLoadDataTestRule();
    protected final TestRule logMarkerTestRule = new LogMarkerTestRule();

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
}
