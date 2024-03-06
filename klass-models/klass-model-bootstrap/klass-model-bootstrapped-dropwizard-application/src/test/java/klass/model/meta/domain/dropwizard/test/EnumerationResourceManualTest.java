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

public class EnumerationResourceManualTest
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
                "klass.model.meta.domain.dropwizard.test.EnumerationResourceManualTest.getAllMeta");

        Response response = client.target(
                String.format("http://localhost:%d/api/meta/enumeration", RULE.getLocalPort()))
                .request()
                .get();

        this.assertResponseStatus(response, Status.OK);

        String jsonResponse = response.readEntity(String.class);
        //language=JSON
        String expected = ""
                + "[\n"
                + "  {\n"
                + "    \"name\": \"InheritanceType\",\n"
                + "    \"packageName\": \"klass.model.meta.domain\",\n"
                + "    \"enumerationLiterals\": [\n"
                + "      {\n"
                + "        \"name\": \"TABLE_PER_SUBCLASS\",\n"
                + "        \"prettyName\": \"table-per-subclass\"\n"
                + "      },\n"
                + "      {\n"
                + "        \"name\": \"TABLE_FOR_ALL_SUBCLASSES\",\n"
                + "        \"prettyName\": \"table-for-all-subclasses\"\n"
                + "      },\n"
                + "      {\n"
                + "        \"name\": \"TABLE_PER_CLASS\",\n"
                + "        \"prettyName\": \"table-per-class\"\n"
                + "      },\n"
                + "      {\n"
                + "        \"name\": \"NONE\",\n"
                + "        \"prettyName\": \"none\"\n"
                + "      }\n"
                + "    ]\n"
                + "  },\n"
                + "  {\n"
                + "    \"name\": \"PrimitiveType\",\n"
                + "    \"packageName\": \"klass.model.meta.domain\",\n"
                + "    \"enumerationLiterals\": [\n"
                + "      {\n"
                + "        \"name\": \"INTEGER\",\n"
                + "        \"prettyName\": \"Integer\"\n"
                + "      },\n"
                + "      {\n"
                + "        \"name\": \"LONG\",\n"
                + "        \"prettyName\": \"Long\"\n"
                + "      },\n"
                + "      {\n"
                + "        \"name\": \"DOUBLE\",\n"
                + "        \"prettyName\": \"Double\"\n"
                + "      },\n"
                + "      {\n"
                + "        \"name\": \"FLOAT\",\n"
                + "        \"prettyName\": \"Float\"\n"
                + "      },\n"
                + "      {\n"
                + "        \"name\": \"BOOLEAN\",\n"
                + "        \"prettyName\": \"Boolean\"\n"
                + "      },\n"
                + "      {\n"
                + "        \"name\": \"STRING\",\n"
                + "        \"prettyName\": \"String\"\n"
                + "      },\n"
                + "      {\n"
                + "        \"name\": \"INSTANT\",\n"
                + "        \"prettyName\": \"Instant\"\n"
                + "      },\n"
                + "      {\n"
                + "        \"name\": \"LOCAL_DATE\",\n"
                + "        \"prettyName\": \"LocalDate\"\n"
                + "      },\n"
                + "      {\n"
                + "        \"name\": \"TEMPORAL_INSTANT\",\n"
                + "        \"prettyName\": \"TemporalInstant\"\n"
                + "      },\n"
                + "      {\n"
                + "        \"name\": \"TEMPORAL_RANGE\",\n"
                + "        \"prettyName\": \"TemporalRange\"\n"
                + "      }\n"
                + "    ]\n"
                + "  },\n"
                + "  {\n"
                + "    \"name\": \"Multiplicity\",\n"
                + "    \"packageName\": \"klass.model.meta.domain\",\n"
                + "    \"enumerationLiterals\": [\n"
                + "      {\n"
                + "        \"name\": \"ZERO_TO_ONE\",\n"
                + "        \"prettyName\": \"0..1\"\n"
                + "      },\n"
                + "      {\n"
                + "        \"name\": \"ONE_TO_ONE\",\n"
                + "        \"prettyName\": \"1..1\"\n"
                + "      },\n"
                + "      {\n"
                + "        \"name\": \"ZERO_TO_MANY\",\n"
                + "        \"prettyName\": \"0..*\"\n"
                + "      },\n"
                + "      {\n"
                + "        \"name\": \"ONE_TO_MANY\",\n"
                + "        \"prettyName\": \"1..*\"\n"
                + "      }\n"
                + "    ]\n"
                + "  },\n"
                + "  {\n"
                + "    \"name\": \"AssociationEndDirection\",\n"
                + "    \"packageName\": \"klass.model.meta.domain\",\n"
                + "    \"enumerationLiterals\": [\n"
                + "      {\n"
                + "        \"name\": \"SOURCE\",\n"
                + "        \"prettyName\": \"source\"\n"
                + "      },\n"
                + "      {\n"
                + "        \"name\": \"TARGET\",\n"
                + "        \"prettyName\": \"target\"\n"
                + "      }\n"
                + "    ]\n"
                + "  },\n"
                + "  {\n"
                + "    \"name\": \"Operator\",\n"
                + "    \"packageName\": \"klass.model.meta.domain\",\n"
                + "    \"enumerationLiterals\": [\n"
                + "      {\n"
                + "        \"name\": \"EQUALS\",\n"
                + "        \"prettyName\": \"==\"\n"
                + "      },\n"
                + "      {\n"
                + "        \"name\": \"NOT_EQUALS\",\n"
                + "        \"prettyName\": \"!=\"\n"
                + "      },\n"
                + "      {\n"
                + "        \"name\": \"LESS_THAN\",\n"
                + "        \"prettyName\": \"<\"\n"
                + "      },\n"
                + "      {\n"
                + "        \"name\": \"GREATER_THAN\",\n"
                + "        \"prettyName\": \">\"\n"
                + "      },\n"
                + "      {\n"
                + "        \"name\": \"LESS_THAN_EQUAL\",\n"
                + "        \"prettyName\": \"<=\"\n"
                + "      },\n"
                + "      {\n"
                + "        \"name\": \"GREATER_THAN_EQUAL\",\n"
                + "        \"prettyName\": \">=\"\n"
                + "      },\n"
                + "      {\n"
                + "        \"name\": \"IN\",\n"
                + "        \"prettyName\": \"in\"\n"
                + "      },\n"
                + "      {\n"
                + "        \"name\": \"CONTAINS\",\n"
                + "        \"prettyName\": \"contains\"\n"
                + "      },\n"
                + "      {\n"
                + "        \"name\": \"STARTS_WITH\",\n"
                + "        \"prettyName\": \"startsWith\"\n"
                + "      },\n"
                + "      {\n"
                + "        \"name\": \"ENDS_WITH\",\n"
                + "        \"prettyName\": \"endsWith\"\n"
                + "      }\n"
                + "    ]\n"
                + "  },\n"
                + "  {\n"
                + "    \"name\": \"OrderByDirection\",\n"
                + "    \"packageName\": \"klass.model.meta.domain\",\n"
                + "    \"enumerationLiterals\": [\n"
                + "      {\n"
                + "        \"name\": \"ASCENDING\",\n"
                + "        \"prettyName\": \"ascending\"\n"
                + "      },\n"
                + "      {\n"
                + "        \"name\": \"DESCENDING\",\n"
                + "        \"prettyName\": \"descending\"\n"
                + "      }\n"
                + "    ]\n"
                + "  },\n"
                + "  {\n"
                + "    \"name\": \"UrlParameterType\",\n"
                + "    \"packageName\": \"klass.model.meta.domain\",\n"
                + "    \"enumerationLiterals\": [\n"
                + "      {\n"
                + "        \"name\": \"QUERY\",\n"
                + "        \"prettyName\": \"query\"\n"
                + "      },\n"
                + "      {\n"
                + "        \"name\": \"PATH\",\n"
                + "        \"prettyName\": \"path\"\n"
                + "      }\n"
                + "    ]\n"
                + "  },\n"
                + "  {\n"
                + "    \"name\": \"ServiceMultiplicity\",\n"
                + "    \"packageName\": \"klass.model.meta.domain\",\n"
                + "    \"enumerationLiterals\": [\n"
                + "      {\n"
                + "        \"name\": \"ONE\",\n"
                + "        \"prettyName\": \"one\"\n"
                + "      },\n"
                + "      {\n"
                + "        \"name\": \"MANY\",\n"
                + "        \"prettyName\": \"many\"\n"
                + "      }\n"
                + "    ]\n"
                + "  },\n"
                + "  {\n"
                + "    \"name\": \"Verb\",\n"
                + "    \"packageName\": \"klass.model.meta.domain\",\n"
                + "    \"enumerationLiterals\": [\n"
                + "      {\n"
                + "        \"name\": \"GET\",\n"
                + "        \"prettyName\": null\n"
                + "      },\n"
                + "      {\n"
                + "        \"name\": \"POST\",\n"
                + "        \"prettyName\": null\n"
                + "      },\n"
                + "      {\n"
                + "        \"name\": \"PUT\",\n"
                + "        \"prettyName\": null\n"
                + "      },\n"
                + "      {\n"
                + "        \"name\": \"PATCH\",\n"
                + "        \"prettyName\": null\n"
                + "      },\n"
                + "      {\n"
                + "        \"name\": \"DELETE\",\n"
                + "        \"prettyName\": null\n"
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
