package klass.model.meta.domain.dropwizard.test;

import java.util.concurrent.TimeUnit;

import javax.annotation.Nonnull;
import javax.ws.rs.client.Client;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import cool.klass.model.converter.bootstrap.writer.KlassBootstrapWriter;
import cool.klass.reladomo.test.rule.ReladomoTestRule;
import io.dropwizard.client.JerseyClientBuilder;
import io.dropwizard.client.JerseyClientConfiguration;
import io.dropwizard.testing.ResourceHelpers;
import io.dropwizard.testing.junit.DropwizardAppRule;
import io.dropwizard.util.Duration;
import klass.model.meta.domain.dropwizard.application.KlassBootstrappedMetaModelApplication;
import klass.model.meta.domain.dropwizard.application.KlassBootstrappedMetaModelConfiguration;
import org.json.JSONException;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;
import org.skyscreamer.jsonassert.JSONAssert;
import org.skyscreamer.jsonassert.JSONCompareMode;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class InterfaceResourceManualTest
{
    @ClassRule
    public static final DropwizardAppRule<KlassBootstrappedMetaModelConfiguration> RULE = new DropwizardAppRule<>(
            KlassBootstrappedMetaModelApplication.class,
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

    @Before
    public void bootstrap()
    {
        KlassBootstrappedMetaModelApplication application = RULE.getApplication();

        KlassBootstrapWriter klassBootstrapWriter = new KlassBootstrapWriter(
                application.getDomainModel(),
                application.getDataStore());
        klassBootstrapWriter.bootstrapMetaModel();
    }

    @Test
    public void getAllMeta() throws JSONException
    {
        Client client = this.getClient(
                "klass.model.meta.domain.dropwizard.test.InterfaceResourceManualTest.getAllMeta");

        Response response = client.target(
                String.format("http://localhost:%d/api/meta/interface", RULE.getLocalPort()))
                .request()
                .get();

        this.assertResponseStatus(response, Status.OK);

        String jsonResponse = response.readEntity(String.class);
        //language=JSON
        String expected = ""
                + "[\n"
                + "  {\n"
                + "    \"name\": \"Element\",\n"
                + "    \"packageName\": \"klass.model.meta.domain\",\n"
                + "    \"primitiveProperties\": [\n"
                + "      {\n"
                + "        \"name\": \"inferred\",\n"
                + "        \"primitiveType\": \"Boolean\",\n"
                + "        \"optional\": false,\n"
                + "        \"key\": false,\n"
                + "        \"id\": false\n"
                + "      },\n"
                + "      {\n"
                + "        \"name\": \"sourceCode\",\n"
                + "        \"primitiveType\": \"String\",\n"
                + "        \"optional\": false,\n"
                + "        \"key\": false,\n"
                + "        \"id\": false,\n"
                + "        \"maxLengthValidation\": {\n"
                + "          \"number\": 100000\n"
                + "        }\n"
                + "      },\n"
                + "      {\n"
                + "        \"name\": \"sourceCodeWithInference\",\n"
                + "        \"primitiveType\": \"String\",\n"
                + "        \"optional\": false,\n"
                + "        \"key\": false,\n"
                + "        \"id\": false,\n"
                + "        \"maxLengthValidation\": {\n"
                + "          \"number\": 100000\n"
                + "        }\n"
                + "      }\n"
                + "    ]\n"
                + "  },\n"
                + "  {\n"
                + "    \"name\": \"NamedElement\",\n"
                + "    \"packageName\": \"klass.model.meta.domain\",\n"
                + "    \"superInterfaces\": [\n"
                + "      {\n"
                + "        \"superInterface\": {\n"
                + "          \"name\": \"Element\"\n"
                + "        }\n"
                + "      }\n"
                + "    ],\n"
                + "    \"primitiveProperties\": [\n"
                + "      {\n"
                + "        \"name\": \"name\",\n"
                + "        \"primitiveType\": \"String\",\n"
                + "        \"optional\": false,\n"
                + "        \"key\": true,\n"
                + "        \"id\": false,\n"
                + "        \"propertyModifiers\": [\n"
                + "          {\n"
                + "            \"name\": \"key\"\n"
                + "          }\n"
                + "        ]\n"
                + "      },\n"
                + "      {\n"
                + "        \"name\": \"ordinal\",\n"
                + "        \"primitiveType\": \"Integer\",\n"
                + "        \"optional\": false,\n"
                + "        \"key\": false,\n"
                + "        \"id\": false\n"
                + "      }\n"
                + "    ]\n"
                + "  },\n"
                + "  {\n"
                + "    \"name\": \"PackageableElement\",\n"
                + "    \"packageName\": \"klass.model.meta.domain\",\n"
                + "    \"superInterfaces\": [\n"
                + "      {\n"
                + "        \"superInterface\": {\n"
                + "          \"name\": \"NamedElement\"\n"
                + "        }\n"
                + "      }\n"
                + "    ],\n"
                + "    \"primitiveProperties\": [\n"
                + "      {\n"
                + "        \"name\": \"packageName\",\n"
                + "        \"primitiveType\": \"String\",\n"
                + "        \"optional\": false,\n"
                + "        \"key\": false,\n"
                + "        \"id\": false\n"
                + "      }\n"
                + "    ]\n"
                + "  },\n"
                + "  {\n"
                + "    \"name\": \"PropertyValidation\",\n"
                + "    \"packageName\": \"klass.model.meta.domain\",\n"
                + "    \"superInterfaces\": [\n"
                + "      {\n"
                + "        \"superInterface\": {\n"
                + "          \"name\": \"Element\"\n"
                + "        }\n"
                + "      }\n"
                + "    ]\n"
                + "  }\n"
                + "]";
        JSONAssert.assertEquals(expected, jsonResponse, JSONCompareMode.STRICT);
    }

    public void assertResponseStatus(@Nonnull Response response, Status status)
    {
        response.bufferEntity();
        String entityAsString = response.readEntity(String.class);
        assertThat(entityAsString, response.getStatusInfo(), is(status));
    }
}
