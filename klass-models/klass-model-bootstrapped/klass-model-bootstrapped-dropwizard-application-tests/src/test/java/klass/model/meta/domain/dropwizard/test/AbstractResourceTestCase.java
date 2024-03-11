package klass.model.meta.domain.dropwizard.test;

import javax.annotation.Nonnull;
import javax.ws.rs.client.Client;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import io.dropwizard.testing.ResourceHelpers;
import io.dropwizard.testing.junit.DropwizardAppRule;
import io.liftwizard.dropwizard.testing.junit.AbstractDropwizardAppTest;
import klass.model.meta.domain.dropwizard.application.KlassBootstrappedMetaModelApplication;

public abstract class AbstractResourceTestCase
        extends AbstractDropwizardAppTest
{
    @Nonnull
    @Override
    protected DropwizardAppRule getDropwizardAppRule()
    {
        return new DropwizardAppRule<>(
                KlassBootstrappedMetaModelApplication.class,
                ResourceHelpers.resourceFilePath("config-test.json5"));
    }

    protected void assertUrlReturns(@Nonnull String testName, @Nonnull String url)
    {
        Class<?> klass      = this.getClass();
        String   clientName = klass.getPackage().getName() + '.' + klass.getSimpleName() + '.' + testName;
        Client   client     = this.getClient(clientName);
        Response response = client
                .target("http://localhost:{port}/api/" + url)
                .resolveTemplate("port", this.appRule.getLocalPort())
                .request()
                .get();

        this.assertResponseStatus(response, Status.OK);
        String jsonResponse = response.readEntity(String.class);

        String resourceClassPathLocation = klass.getSimpleName() + '.' + testName + ".json";
        this.jsonMatchRule.assertFileContents(resourceClassPathLocation, jsonResponse);
    }
}
