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

public class MetaResourceManualTest
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
    public void get_smoke_test() throws JSONException
    {
        Client client = this.getClient(
                "klass.model.meta.domain.dropwizard.test.MetaResourceManualTest.get_smoke_test");

        Response response = client.target(
                String.format("http://localhost:%d/api/meta/class/", RULE.getLocalPort()))
                .request()
                .get();

        this.assertResponseStatus(response, Status.OK);

        String jsonResponse = response.readEntity(String.class);
        //language=JSON
        String expected = ""
                + "{\n"
                + "  \"name\": \"Klass\",\n"
                + "  \"inferred\": false,\n"
                + "  \"packageName\": \"klass.model.meta.domain\",\n"
                + "  \"ordinal\": 15,\n"
                + "  \"sourceCode\": \"class Klass\\n    extends Classifier\\n{\\n    superClassName: String? private;\\n    inheritanceType: InheritanceType;\\n}\",\n"
                + "  \"sourceCodeWithInference\": \"class Klass\\n{\\n    superClassName: String? private;\\n    inheritanceType: InheritanceType;\\n    name      : String key;\\n    inferred  : Boolean;\\n    packageName: String;\\n    ordinal: Integer;\\n    sourceCode: String maximumLength(100000);\\n    sourceCodeWithInference: String maximumLength(100000);\\n    subClasses: Klass[0..*];\\n    superClass: Klass[0..1];\\n    associationEnds: AssociationEnd[0..*];\\n    associationEndsResultTypeOf: AssociationEnd[0..*];\\n    parameterizedProperties: ParameterizedProperty[0..*] owned\\n        orderBy: this.ordinal;\\n    parameterizedPropertiesResultTypeOf: ParameterizedProperty[0..*];\\n    serviceGroup: ServiceGroup[0..1];\\n    superInterfaces: ClassifierInterfaceMapping[0..*] owned;\\n    dataTypeProperties: DataTypeProperty[0..*] owned\\n        orderBy: this.ordinal;\\n    primitiveProperties: PrimitiveProperty[0..*] owned\\n        orderBy: this.ordinal;\\n    enumerationProperties: EnumerationProperty[0..*] owned\\n        orderBy: this.ordinal;\\n    classifierModifiers: ClassifierModifier[0..*]\\n        orderBy: this.ordinal;\\n}\\n\",\n"
                + "  \"superClass\": {\n"
                + "    \"name\": \"Classifier\",\n"
                + "    \"packageName\": \"klass.model.meta.domain\",\n"
                + "    \"superInterfaces\": [\n"
                + "      {\n"
                + "        \"superInterface\": {\n"
                + "          \"name\": \"PackageableElement\",\n"
                + "          \"packageName\": \"klass.model.meta.domain\"\n"
                + "        }\n"
                + "      }\n"
                + "    ]\n"
                + "  },\n"
                + "  \"superInterfaces\": [],\n"
                + "  \"classifierModifiers\": [],\n"
                + "  \"dataTypeProperties\": [\n"
                + "    {\n"
                + "      \"name\": \"name\",\n"
                + "      \"inferred\": false,\n"
                + "      \"optional\": false,\n"
                + "      \"key\": true,\n"
                + "      \"ordinal\": 1,\n"
                + "      \"propertyModifiers\": [\n"
                + "        {\n"
                + "          \"name\": \"key\",\n"
                + "          \"inferred\": false,\n"
                + "          \"ordinal\": 1\n"
                + "        }\n"
                + "      ]\n"
                + "    },\n"
                + "    {\n"
                + "      \"name\": \"superClassName\",\n"
                + "      \"inferred\": false,\n"
                + "      \"optional\": true,\n"
                + "      \"key\": false,\n"
                + "      \"ordinal\": 1,\n"
                + "      \"propertyModifiers\": [\n"
                + "        {\n"
                + "          \"name\": \"private\",\n"
                + "          \"inferred\": false,\n"
                + "          \"ordinal\": 1\n"
                + "        }\n"
                + "      ]\n"
                + "    },\n"
                + "    {\n"
                + "      \"name\": \"inferred\",\n"
                + "      \"inferred\": false,\n"
                + "      \"optional\": false,\n"
                + "      \"key\": false,\n"
                + "      \"ordinal\": 2,\n"
                + "      \"propertyModifiers\": []\n"
                + "    },\n"
                + "    {\n"
                + "      \"name\": \"inheritanceType\",\n"
                + "      \"inferred\": false,\n"
                + "      \"optional\": false,\n"
                + "      \"key\": false,\n"
                + "      \"ordinal\": 2,\n"
                + "      \"propertyModifiers\": []\n"
                + "    },\n"
                + "    {\n"
                + "      \"name\": \"packageName\",\n"
                + "      \"inferred\": false,\n"
                + "      \"optional\": false,\n"
                + "      \"key\": false,\n"
                + "      \"ordinal\": 3,\n"
                + "      \"propertyModifiers\": []\n"
                + "    },\n"
                + "    {\n"
                + "      \"name\": \"sourceCodeWithInference\",\n"
                + "      \"inferred\": false,\n"
                + "      \"optional\": false,\n"
                + "      \"key\": false,\n"
                + "      \"ordinal\": 3,\n"
                + "      \"propertyModifiers\": []\n"
                + "    },\n"
                + "    {\n"
                + "      \"name\": \"ordinal\",\n"
                + "      \"inferred\": false,\n"
                + "      \"optional\": false,\n"
                + "      \"key\": false,\n"
                + "      \"ordinal\": 4,\n"
                + "      \"propertyModifiers\": []\n"
                + "    },\n"
                + "    {\n"
                + "      \"name\": \"sourceCode\",\n"
                + "      \"inferred\": false,\n"
                + "      \"optional\": false,\n"
                + "      \"key\": false,\n"
                + "      \"ordinal\": 5,\n"
                + "      \"propertyModifiers\": []\n"
                + "    }\n"
                + "  ],\n"
                + "  \"primitiveProperties\": [\n"
                + "    {\n"
                + "      \"name\": \"name\",\n"
                + "      \"inferred\": false,\n"
                + "      \"primitiveType\": \"String\",\n"
                + "      \"optional\": false,\n"
                + "      \"key\": true,\n"
                + "      \"id\": false,\n"
                + "      \"ordinal\": 1,\n"
                + "      \"propertyModifiers\": [\n"
                + "        {\n"
                + "          \"name\": \"key\",\n"
                + "          \"inferred\": false,\n"
                + "          \"ordinal\": 1\n"
                + "        }\n"
                + "      ]\n"
                + "    },\n"
                + "    {\n"
                + "      \"name\": \"superClassName\",\n"
                + "      \"inferred\": false,\n"
                + "      \"primitiveType\": \"String\",\n"
                + "      \"optional\": true,\n"
                + "      \"key\": false,\n"
                + "      \"id\": false,\n"
                + "      \"ordinal\": 1,\n"
                + "      \"propertyModifiers\": [\n"
                + "        {\n"
                + "          \"name\": \"private\",\n"
                + "          \"inferred\": false,\n"
                + "          \"ordinal\": 1\n"
                + "        }\n"
                + "      ]\n"
                + "    },\n"
                + "    {\n"
                + "      \"name\": \"inferred\",\n"
                + "      \"inferred\": false,\n"
                + "      \"primitiveType\": \"Boolean\",\n"
                + "      \"optional\": false,\n"
                + "      \"key\": false,\n"
                + "      \"id\": false,\n"
                + "      \"ordinal\": 2,\n"
                + "      \"propertyModifiers\": []\n"
                + "    },\n"
                + "    {\n"
                + "      \"name\": \"packageName\",\n"
                + "      \"inferred\": false,\n"
                + "      \"primitiveType\": \"String\",\n"
                + "      \"optional\": false,\n"
                + "      \"key\": false,\n"
                + "      \"id\": false,\n"
                + "      \"ordinal\": 3,\n"
                + "      \"propertyModifiers\": []\n"
                + "    },\n"
                + "    {\n"
                + "      \"name\": \"sourceCodeWithInference\",\n"
                + "      \"inferred\": false,\n"
                + "      \"primitiveType\": \"String\",\n"
                + "      \"optional\": false,\n"
                + "      \"key\": false,\n"
                + "      \"id\": false,\n"
                + "      \"ordinal\": 3,\n"
                + "      \"propertyModifiers\": []\n"
                + "    },\n"
                + "    {\n"
                + "      \"name\": \"ordinal\",\n"
                + "      \"inferred\": false,\n"
                + "      \"primitiveType\": \"Integer\",\n"
                + "      \"optional\": false,\n"
                + "      \"key\": false,\n"
                + "      \"id\": false,\n"
                + "      \"ordinal\": 4,\n"
                + "      \"propertyModifiers\": []\n"
                + "    },\n"
                + "    {\n"
                + "      \"name\": \"sourceCode\",\n"
                + "      \"inferred\": false,\n"
                + "      \"primitiveType\": \"String\",\n"
                + "      \"optional\": false,\n"
                + "      \"key\": false,\n"
                + "      \"id\": false,\n"
                + "      \"ordinal\": 5,\n"
                + "      \"propertyModifiers\": []\n"
                + "    }\n"
                + "  ],\n"
                + "  \"enumerationProperties\": [\n"
                + "    {\n"
                + "      \"name\": \"inheritanceType\",\n"
                + "      \"inferred\": false,\n"
                + "      \"optional\": false,\n"
                + "      \"key\": false,\n"
                + "      \"ordinal\": 2,\n"
                + "      \"enumeration\": {\n"
                + "        \"name\": \"InheritanceType\",\n"
                + "        \"inferred\": false\n"
                + "      },\n"
                + "      \"propertyModifiers\": []\n"
                + "    }\n"
                + "  ],\n"
                + "  \"associationEnds\": [\n"
                + "    {\n"
                + "      \"name\": \"associationEnds\",\n"
                + "      \"inferred\": false,\n"
                + "      \"direction\": \"target\",\n"
                + "      \"multiplicity\": \"0..*\",\n"
                + "      \"resultType\": {\n"
                + "        \"name\": \"AssociationEnd\"\n"
                + "      },\n"
                + "      \"owningAssociation\": {\n"
                + "        \"name\": \"ClassHasAssociationEnds\"\n"
                + "      },\n"
                + "      \"associationEndModifiers\": []\n"
                + "    },\n"
                + "    {\n"
                + "      \"name\": \"associationEndsResultTypeOf\",\n"
                + "      \"inferred\": false,\n"
                + "      \"direction\": \"source\",\n"
                + "      \"multiplicity\": \"0..*\",\n"
                + "      \"resultType\": {\n"
                + "        \"name\": \"AssociationEnd\"\n"
                + "      },\n"
                + "      \"owningAssociation\": {\n"
                + "        \"name\": \"AssociationEndHasResultType\"\n"
                + "      },\n"
                + "      \"associationEndModifiers\": []\n"
                + "    },\n"
                + "    {\n"
                + "      \"name\": \"parameterizedProperties\",\n"
                + "      \"inferred\": false,\n"
                + "      \"direction\": \"target\",\n"
                + "      \"multiplicity\": \"0..*\",\n"
                + "      \"resultType\": {\n"
                + "        \"name\": \"ParameterizedProperty\"\n"
                + "      },\n"
                + "      \"owningAssociation\": {\n"
                + "        \"name\": \"ClassHasParameterizedProperties\"\n"
                + "      },\n"
                + "      \"associationEndModifiers\": [\n"
                + "        {\n"
                + "          \"name\": \"owned\",\n"
                + "          \"inferred\": false,\n"
                + "          \"ordinal\": 1\n"
                + "        }\n"
                + "      ]\n"
                + "    },\n"
                + "    {\n"
                + "      \"name\": \"parameterizedPropertiesResultTypeOf\",\n"
                + "      \"inferred\": false,\n"
                + "      \"direction\": \"source\",\n"
                + "      \"multiplicity\": \"0..*\",\n"
                + "      \"resultType\": {\n"
                + "        \"name\": \"ParameterizedProperty\"\n"
                + "      },\n"
                + "      \"owningAssociation\": {\n"
                + "        \"name\": \"ParameterizedPropertyHasResultType\"\n"
                + "      },\n"
                + "      \"associationEndModifiers\": []\n"
                + "    },\n"
                + "    {\n"
                + "      \"name\": \"serviceGroup\",\n"
                + "      \"inferred\": false,\n"
                + "      \"direction\": \"source\",\n"
                + "      \"multiplicity\": \"0..1\",\n"
                + "      \"resultType\": {\n"
                + "        \"name\": \"ServiceGroup\"\n"
                + "      },\n"
                + "      \"owningAssociation\": {\n"
                + "        \"name\": \"ServiceGroupHasClass\"\n"
                + "      },\n"
                + "      \"associationEndModifiers\": []\n"
                + "    },\n"
                + "    {\n"
                + "      \"name\": \"subClasses\",\n"
                + "      \"inferred\": false,\n"
                + "      \"direction\": \"source\",\n"
                + "      \"multiplicity\": \"0..*\",\n"
                + "      \"resultType\": {\n"
                + "        \"name\": \"Klass\"\n"
                + "      },\n"
                + "      \"owningAssociation\": {\n"
                + "        \"name\": \"ClassHasSuperClass\"\n"
                + "      },\n"
                + "      \"associationEndModifiers\": []\n"
                + "    },\n"
                + "    {\n"
                + "      \"name\": \"superClass\",\n"
                + "      \"inferred\": false,\n"
                + "      \"direction\": \"target\",\n"
                + "      \"multiplicity\": \"0..1\",\n"
                + "      \"resultType\": {\n"
                + "        \"name\": \"Klass\"\n"
                + "      },\n"
                + "      \"owningAssociation\": {\n"
                + "        \"name\": \"ClassHasSuperClass\"\n"
                + "      },\n"
                + "      \"associationEndModifiers\": []\n"
                + "    }\n"
                + "  ]\n"
                + "}";
        JSONAssert.assertEquals(expected, jsonResponse, JSONCompareMode.STRICT);
    }

    public void assertResponseStatus(@Nonnull Response response, Status status)
    {
        response.bufferEntity();
        String entityAsString = response.readEntity(String.class);
        assertThat(entityAsString, response.getStatusInfo(), is(status));
    }
}
