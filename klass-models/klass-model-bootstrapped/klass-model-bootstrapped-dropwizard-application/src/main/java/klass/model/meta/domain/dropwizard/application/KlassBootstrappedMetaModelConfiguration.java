package klass.model.meta.domain.dropwizard.application;

import javax.annotation.Nonnull;
import javax.validation.Valid;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.smoketurner.dropwizard.graphql.GraphQLFactory;
import cool.klass.dropwizard.configuration.AbstractKlassConfiguration;
import io.liftwizard.dropwizard.configuration.graphql.GraphQLFactoryProvider;
import io.liftwizard.servlet.config.spa.SPARedirectFilterFactory;
import io.liftwizard.servlet.config.spa.SPARedirectFilterFactoryProvider;

public class KlassBootstrappedMetaModelConfiguration
        extends AbstractKlassConfiguration
        implements SPARedirectFilterFactoryProvider,
        GraphQLFactoryProvider
{
    @Nonnull
    private @Valid SPARedirectFilterFactory spaRedirectFilterFactory = new SPARedirectFilterFactory();

    @Nonnull
    private @Valid GraphQLFactory graphQLFactory = new GraphQLFactory();

    @Override
    @JsonProperty("spaRedirectFilter")
    public SPARedirectFilterFactory getSPARedirectFilterFactory()
    {
        return this.spaRedirectFilterFactory;
    }

    @JsonProperty("spaRedirectFilter")
    public void setSPARedirectFilterFactory(SPARedirectFilterFactory spaRedirectFilterFactory)
    {
        this.spaRedirectFilterFactory = spaRedirectFilterFactory;
    }

    @Override
    @Nonnull
    @JsonProperty("graphQL")
    public GraphQLFactory getGraphQLFactory()
    {
        return this.graphQLFactory;
    }

    @JsonProperty("graphQL")
    public void setGraphQLFactory(@Nonnull GraphQLFactory factory)
    {
        this.graphQLFactory = factory;
    }
}
