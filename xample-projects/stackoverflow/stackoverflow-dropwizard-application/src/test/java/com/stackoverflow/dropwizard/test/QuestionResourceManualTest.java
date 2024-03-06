package com.stackoverflow.dropwizard.test;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import cool.klass.reladomo.test.rule.ReladomoTestRule;
import com.stackoverflow.dropwizard.application.StackOverflowApplication;
import com.stackoverflow.dropwizard.application.StackOverflowConfiguration;
import io.dropwizard.client.JerseyClientBuilder;
import io.dropwizard.testing.ResourceHelpers;
import io.dropwizard.testing.junit.DropwizardAppRule;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;

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
    public final ReladomoTestRule reladomoTestRule = new ReladomoTestRule("reladomo-runtime-configuration/TestReladomoRuntimeConfiguration.xml");

    @Test
    public void put()
    {
        Client client = new JerseyClientBuilder(RULE.getEnvironment()).build("test client");

        //language=JSON
        String json = "{}";

        Response response = client.target(
                String.format("http://localhost:%d/manual/api/question/1", RULE.getLocalPort()))
                .request()
                .put(Entity.json(json));

        assertThat(response.getStatusInfo(), is(Status.BAD_REQUEST));
    }
}
