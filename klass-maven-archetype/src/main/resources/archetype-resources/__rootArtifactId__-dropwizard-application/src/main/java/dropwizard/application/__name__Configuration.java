package ${package}.dropwizard.application;

import javax.annotation.Nonnull;
import javax.validation.Valid;

import com.fasterxml.jackson.annotation.JsonProperty;
import cool.klass.dropwizard.configuration.AbstractKlassConfiguration;
import io.liftwizard.dropwizard.configuration.graphql.GraphQLFactoryProvider;
import com.smoketurner.dropwizard.graphql.GraphQLFactory;

public class ${name}Configuration
        extends AbstractKlassConfiguration
        implements GraphQLFactoryProvider
{
    @Nonnull
    private @Valid GraphQLFactory graphQL = new GraphQLFactory();

    @Override
    @Nonnull
    @JsonProperty("graphQL")
    public GraphQLFactory getGraphQLFactory()
    {
        return this.graphQL;
    }

    @JsonProperty("graphQL")
    public void setGraphQLFactory(@Nonnull GraphQLFactory factory)
    {
        this.graphQL = factory;
    }

    // TODO: implement service configuration
}
