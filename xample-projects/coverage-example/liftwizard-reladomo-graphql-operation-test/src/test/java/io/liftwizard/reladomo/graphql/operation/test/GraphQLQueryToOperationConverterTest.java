package io.liftwizard.reladomo.graphql.operation.test;

import java.io.InputStream;
import java.util.List;

import javax.annotation.Nonnull;

import cool.klass.xample.coverage.PropertiesRequiredFinder;
import graphql.ExecutionInput;
import graphql.ExecutionResult;
import graphql.GraphQL;
import graphql.GraphQLError;
import graphql.Scalars;
import graphql.schema.GraphQLSchema;
import graphql.schema.idl.RuntimeWiring;
import graphql.schema.idl.SchemaGenerator;
import graphql.schema.idl.SchemaParser;
import graphql.schema.idl.TypeDefinitionRegistry;
import io.liftwizard.graphql.scalar.temporal.GraphQLLocalDateScalar;
import io.liftwizard.graphql.scalar.temporal.GraphQLTemporalScalar;
import io.liftwizard.junit.rule.log.marker.LogMarkerTestRule;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestRule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GraphQLQueryToOperationConverterTest
{
    private static final Logger LOGGER = LoggerFactory.getLogger(GraphQLQueryToOperationConverterTest.class);

    @Rule
    public final TestRule logMarkerTestRule = new LogMarkerTestRule();

    @Test
    public void convertQueries()
    {
        RuntimeWiring runtimeWiring = this.getRuntimeWiring();

        TypeDefinitionRegistry typeRegistry = this.getTypeDefinitionRegistry();

        var           schemaGenerator = new SchemaGenerator();
        GraphQLSchema graphQLSchema   = schemaGenerator.makeExecutableSchema(typeRegistry, runtimeWiring);

        GraphQL graphQL = GraphQL.newGraphQL(graphQLSchema).build();

        ExecutionInput executionInput = ExecutionInput.newExecutionInput().query("query { hero { name } }").build();

        ExecutionResult executionResult = graphQL.execute(executionInput);

        Object data = executionResult.getData();
        LOGGER.info("data = {}", data);
        List<GraphQLError> errors = executionResult.getErrors();
        LOGGER.info("errors = {}", errors);
    }

    private RuntimeWiring getRuntimeWiring()
    {
        var propertiesRequiredDataFetcher = new FakeDataFetcher<>(PropertiesRequiredFinder.getFinderInstance());

        return RuntimeWiring.newRuntimeWiring()
                .scalar(new GraphQLTemporalScalar("Instant"))
                .scalar(new GraphQLTemporalScalar("TemporalInstant"))
                .scalar(new GraphQLTemporalScalar("TemporalRange"))
                .scalar(Scalars.GraphQLLong)
                .scalar(new GraphQLLocalDateScalar())

                .type(
                        "Query",
                        typeWiring -> typeWiring.dataFetcher("propertiesRequired", propertiesRequiredDataFetcher))
                .build();
    }

    @Nonnull
    private TypeDefinitionRegistry getTypeDefinitionRegistry()
    {
        InputStream querySchemaFile = this.getClass().getResourceAsStream(
                "/cool/klass/xample/coverage/graphql/schema/CoverageExampleQuery.graphqls");
        InputStream schemaFile = this.getClass().getResourceAsStream(
                "/cool/klass/xample/coverage/graphql/schema/CoverageExample.graphqls");

        var                    schemaParser      = new SchemaParser();
        TypeDefinitionRegistry queryTypeRegistry = schemaParser.parse(querySchemaFile);
        TypeDefinitionRegistry typeRegistry      = schemaParser.parse(schemaFile);
        typeRegistry.merge(queryTypeRegistry);
        return typeRegistry;
    }
}
