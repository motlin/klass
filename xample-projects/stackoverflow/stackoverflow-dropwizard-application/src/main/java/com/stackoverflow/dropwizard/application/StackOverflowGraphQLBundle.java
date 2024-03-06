package com.stackoverflow.dropwizard.application;

import javax.annotation.Nonnull;

import cool.klass.data.store.DataStore;
import com.smoketurner.dropwizard.graphql.GraphQLBundle;
import com.smoketurner.dropwizard.graphql.GraphQLFactory;
import com.stackoverflow.graphql.runtime.wiring.StackOverflowRuntimeWiringBuilder;
import graphql.schema.idl.RuntimeWiring;
import io.dropwizard.assets.AssetsBundle;
import io.dropwizard.setup.Bootstrap;

public class StackOverflowGraphQLBundle extends GraphQLBundle<StackOverflowConfiguration>
{
    private final DataStore dataStore;

    public StackOverflowGraphQLBundle(DataStore dataStore)
    {
        this.dataStore = dataStore;
    }

    @Override
    public void initialize(@Nonnull Bootstrap<?> bootstrap)
    {
        // TODO: Need trailing slash after /graphiql?
        AssetsBundle assetsBundle = new AssetsBundle("/assets", "/graphiql", "index.htm", "graphiql");
        bootstrap.addBundle(assetsBundle);
    }

    @Nonnull
    @Override
    public GraphQLFactory getGraphQLFactory(@Nonnull StackOverflowConfiguration configuration)
    {
        // the RuntimeWiring must be configured prior to the run()
        // methods being called so the schema is connected properly.

        GraphQLFactory                    factory              = configuration.getGraphQLFactory();
        StackOverflowRuntimeWiringBuilder runtimeWiringBuilder = new StackOverflowRuntimeWiringBuilder();
        RuntimeWiring                     runtimeWiring        = runtimeWiringBuilder.buildRuntimeWiring();
        factory.setRuntimeWiring(runtimeWiring);
        return factory;
    }
}
