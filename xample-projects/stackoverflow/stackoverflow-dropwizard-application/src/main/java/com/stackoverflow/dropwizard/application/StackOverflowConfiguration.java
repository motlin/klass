package com.stackoverflow.dropwizard.application;

import javax.annotation.Nonnull;
import javax.validation.Valid;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.smoketurner.dropwizard.graphql.GraphQLFactory;
import io.dropwizard.Configuration;

public class StackOverflowConfiguration extends Configuration
{
    @Nonnull
    private @Valid GraphQLFactory graphQL = new GraphQLFactory();

    @JsonProperty("graphQL")
    public GraphQLFactory getGraphQLFactory()
    {
        return this.graphQL;
    }

    @JsonProperty("graphQL")
    public void setGraphQLFactory(GraphQLFactory factory)
    {
        this.graphQL = factory;
    }

    // TODO: implement service configuration
}
