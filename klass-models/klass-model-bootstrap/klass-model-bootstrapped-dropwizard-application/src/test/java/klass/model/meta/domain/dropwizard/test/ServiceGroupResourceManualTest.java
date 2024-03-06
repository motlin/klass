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

public class ServiceGroupResourceManualTest
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
                "klass.model.meta.domain.dropwizard.test.ServiceGroupResourceManualTest.getAllMeta");

        Response response = client.target(
                String.format("http://localhost:%d/api/meta/serviceGroup", RULE.getLocalPort()))
                .request()
                .get();

        this.assertResponseStatus(response, Status.OK);

        String jsonResponse = response.readEntity(String.class);
        //language=JSON
        String expected = "[\n"
                + "  {\n"
                + "    \"name\": \"Enumeration\",\n"
                + "    \"packageName\": \"klass.model.meta.domain\",\n"
                + "    \"urls\": [\n"
                + "      {\n"
                + "        \"url\": \"/api/meta/enumeration\",\n"
                + "        \"services\": [\n"
                + "          {\n"
                + "            \"verb\": \"GET\",\n"
                + "            \"serviceMultiplicity\": \"many\",\n"
                + "            \"projection\": {\n"
                + "              \"name\": \"EnumerationSummaryProjection\"\n"
                + "            }\n"
                + "          }\n"
                + "        ]\n"
                + "      },\n"
                + "      {\n"
                + "        \"url\": \"/api/meta/enumeration/{enumerationName}\",\n"
                + "        \"services\": [\n"
                + "          {\n"
                + "            \"verb\": \"GET\",\n"
                + "            \"serviceMultiplicity\": \"one\",\n"
                + "            \"projection\": {\n"
                + "              \"name\": \"EnumerationProjection\"\n"
                + "            }\n"
                + "          }\n"
                + "        ]\n"
                + "      },\n"
                + "      {\n"
                + "        \"url\": \"/api/meta/enumeration/{enumerationName}/summary\",\n"
                + "        \"services\": [\n"
                + "          {\n"
                + "            \"verb\": \"GET\",\n"
                + "            \"serviceMultiplicity\": \"one\",\n"
                + "            \"projection\": {\n"
                + "              \"name\": \"EnumerationSummaryProjection\"\n"
                + "            }\n"
                + "          }\n"
                + "        ]\n"
                + "      }\n"
                + "    ]\n"
                + "  },\n"
                + "  {\n"
                + "    \"name\": \"Interface\",\n"
                + "    \"packageName\": \"klass.model.meta.domain\",\n"
                + "    \"urls\": [\n"
                + "      {\n"
                + "        \"url\": \"/api/meta/interface\",\n"
                + "        \"services\": [\n"
                + "          {\n"
                + "            \"verb\": \"GET\",\n"
                + "            \"serviceMultiplicity\": \"many\",\n"
                + "            \"projection\": {\n"
                + "              \"name\": \"InterfaceSummaryProjection\"\n"
                + "            }\n"
                + "          }\n"
                + "        ]\n"
                + "      },\n"
                + "      {\n"
                + "        \"url\": \"/api/meta/interface/{interfaceName}\",\n"
                + "        \"services\": [\n"
                + "          {\n"
                + "            \"verb\": \"GET\",\n"
                + "            \"serviceMultiplicity\": \"one\",\n"
                + "            \"projection\": {\n"
                + "              \"name\": \"InterfaceProjection\"\n"
                + "            }\n"
                + "          }\n"
                + "        ]\n"
                + "      },\n"
                + "      {\n"
                + "        \"url\": \"/api/meta/interface/{interfaceName}/summary\",\n"
                + "        \"services\": [\n"
                + "          {\n"
                + "            \"verb\": \"GET\",\n"
                + "            \"serviceMultiplicity\": \"one\",\n"
                + "            \"projection\": {\n"
                + "              \"name\": \"InterfaceSummaryProjection\"\n"
                + "            }\n"
                + "          }\n"
                + "        ]\n"
                + "      }\n"
                + "    ]\n"
                + "  },\n"
                + "  {\n"
                + "    \"name\": \"Klass\",\n"
                + "    \"packageName\": \"klass.model.meta.domain\",\n"
                + "    \"urls\": [\n"
                + "      {\n"
                + "        \"url\": \"/api/meta/class\",\n"
                + "        \"services\": [\n"
                + "          {\n"
                + "            \"verb\": \"GET\",\n"
                + "            \"serviceMultiplicity\": \"many\",\n"
                + "            \"projection\": {\n"
                + "              \"name\": \"ClassSummaryProjection\"\n"
                + "            }\n"
                + "          }\n"
                + "        ]\n"
                + "      },\n"
                + "      {\n"
                + "        \"url\": \"/api/meta/class/{className}\",\n"
                + "        \"services\": [\n"
                + "          {\n"
                + "            \"verb\": \"GET\",\n"
                + "            \"serviceMultiplicity\": \"one\",\n"
                + "            \"projection\": {\n"
                + "              \"name\": \"ClassProjection\"\n"
                + "            }\n"
                + "          }\n"
                + "        ]\n"
                + "      },\n"
                + "      {\n"
                + "        \"url\": \"/api/meta/class/{className}/summary\",\n"
                + "        \"services\": [\n"
                + "          {\n"
                + "            \"verb\": \"GET\",\n"
                + "            \"serviceMultiplicity\": \"one\",\n"
                + "            \"projection\": {\n"
                + "              \"name\": \"ClassSummaryProjection\"\n"
                + "            }\n"
                + "          }\n"
                + "        ]\n"
                + "      }\n"
                + "    ]\n"
                + "  },\n"
                + "  {\n"
                + "    \"name\": \"Classifier\",\n"
                + "    \"packageName\": \"klass.model.meta.domain\",\n"
                + "    \"urls\": [\n"
                + "      {\n"
                + "        \"url\": \"/api/meta/classifier\",\n"
                + "        \"services\": [\n"
                + "          {\n"
                + "            \"verb\": \"GET\",\n"
                + "            \"serviceMultiplicity\": \"many\",\n"
                + "            \"projection\": {\n"
                + "              \"name\": \"ClassifierSummaryProjection\"\n"
                + "            }\n"
                + "          }\n"
                + "        ]\n"
                + "      },\n"
                + "      {\n"
                + "        \"url\": \"/api/meta/classifier/{classifierName}\",\n"
                + "        \"services\": [\n"
                + "          {\n"
                + "            \"verb\": \"GET\",\n"
                + "            \"serviceMultiplicity\": \"one\",\n"
                + "            \"projection\": {\n"
                + "              \"name\": \"ClassifierSummaryProjection\"\n"
                + "            }\n"
                + "          }\n"
                + "        ]\n"
                + "      }\n"
                + "    ]\n"
                + "  },\n"
                + "  {\n"
                + "    \"name\": \"Association\",\n"
                + "    \"packageName\": \"klass.model.meta.domain\",\n"
                + "    \"urls\": [\n"
                + "      {\n"
                + "        \"url\": \"/api/meta/association\",\n"
                + "        \"services\": [\n"
                + "          {\n"
                + "            \"verb\": \"GET\",\n"
                + "            \"serviceMultiplicity\": \"many\",\n"
                + "            \"projection\": {\n"
                + "              \"name\": \"AssociationSummaryProjection\"\n"
                + "            }\n"
                + "          }\n"
                + "        ]\n"
                + "      },\n"
                + "      {\n"
                + "        \"url\": \"/api/meta/association/{associationName}\",\n"
                + "        \"services\": [\n"
                + "          {\n"
                + "            \"verb\": \"GET\",\n"
                + "            \"serviceMultiplicity\": \"one\",\n"
                + "            \"projection\": {\n"
                + "              \"name\": \"AssociationProjection\"\n"
                + "            }\n"
                + "          }\n"
                + "        ]\n"
                + "      },\n"
                + "      {\n"
                + "        \"url\": \"/api/meta/association/{associationName}/summary\",\n"
                + "        \"services\": [\n"
                + "          {\n"
                + "            \"verb\": \"GET\",\n"
                + "            \"serviceMultiplicity\": \"one\",\n"
                + "            \"projection\": {\n"
                + "              \"name\": \"AssociationSummaryProjection\"\n"
                + "            }\n"
                + "          }\n"
                + "        ]\n"
                + "      }\n"
                + "    ]\n"
                + "  },\n"
                + "  {\n"
                + "    \"name\": \"ServiceProjection\",\n"
                + "    \"packageName\": \"klass.model.meta.domain\",\n"
                + "    \"urls\": [\n"
                + "      {\n"
                + "        \"url\": \"/api/meta/projection\",\n"
                + "        \"services\": [\n"
                + "          {\n"
                + "            \"verb\": \"GET\",\n"
                + "            \"serviceMultiplicity\": \"many\",\n"
                + "            \"projection\": {\n"
                + "              \"name\": \"ProjectionElementSummaryProjection\"\n"
                + "            }\n"
                + "          }\n"
                + "        ]\n"
                + "      },\n"
                + "      {\n"
                + "        \"url\": \"/api/meta/projection/{projectionName}\",\n"
                + "        \"services\": [\n"
                + "          {\n"
                + "            \"verb\": \"GET\",\n"
                + "            \"serviceMultiplicity\": \"one\",\n"
                + "            \"projection\": {\n"
                + "              \"name\": \"ProjectionElementProjection\"\n"
                + "            }\n"
                + "          }\n"
                + "        ]\n"
                + "      },\n"
                + "      {\n"
                + "        \"url\": \"/api/meta/projection/{projectionName}/summary\",\n"
                + "        \"services\": [\n"
                + "          {\n"
                + "            \"verb\": \"GET\",\n"
                + "            \"serviceMultiplicity\": \"one\",\n"
                + "            \"projection\": {\n"
                + "              \"name\": \"ProjectionElementSummaryProjection\"\n"
                + "            }\n"
                + "          }\n"
                + "        ]\n"
                + "      }\n"
                + "    ]\n"
                + "  },\n"
                + "  {\n"
                + "    \"name\": \"ServiceGroup\",\n"
                + "    \"packageName\": \"klass.model.meta.domain\",\n"
                + "    \"urls\": [\n"
                + "      {\n"
                + "        \"url\": \"/api/meta/serviceGroup\",\n"
                + "        \"services\": [\n"
                + "          {\n"
                + "            \"verb\": \"GET\",\n"
                + "            \"serviceMultiplicity\": \"many\",\n"
                + "            \"projection\": {\n"
                + "              \"name\": \"ServiceGroupSummaryProjection\"\n"
                + "            }\n"
                + "          }\n"
                + "        ]\n"
                + "      },\n"
                + "      {\n"
                + "        \"url\": \"/api/meta/serviceGroup/{serviceGroupName}\",\n"
                + "        \"services\": [\n"
                + "          {\n"
                + "            \"verb\": \"GET\",\n"
                + "            \"serviceMultiplicity\": \"one\",\n"
                + "            \"projection\": {\n"
                + "              \"name\": \"ServiceGroupProjection\"\n"
                + "            }\n"
                + "          }\n"
                + "        ]\n"
                + "      },\n"
                + "      {\n"
                + "        \"url\": \"/api/meta/serviceGroup/{serviceGroupName}/summary\",\n"
                + "        \"services\": [\n"
                + "          {\n"
                + "            \"verb\": \"GET\",\n"
                + "            \"serviceMultiplicity\": \"one\",\n"
                + "            \"projection\": {\n"
                + "              \"name\": \"ServiceGroupSummaryProjection\"\n"
                + "            }\n"
                + "          }\n"
                + "        ]\n"
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
