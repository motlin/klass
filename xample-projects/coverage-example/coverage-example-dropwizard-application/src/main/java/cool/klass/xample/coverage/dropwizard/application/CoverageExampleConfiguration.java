package cool.klass.xample.coverage.dropwizard.application;

import javax.annotation.Nonnull;
import javax.validation.Valid;

import com.fasterxml.jackson.annotation.JsonProperty;
import cool.klass.dropwizard.configuration.AbstractKlassConfiguration;
import com.smoketurner.dropwizard.graphql.GraphQLFactory;
import io.liftwizard.dropwizard.configuration.graphql.GraphQLFactoryProvider;
import io.liftwizard.servlet.config.spa.SPARedirectFilterFactory;
import io.liftwizard.servlet.config.spa.SPARedirectFilterFactoryProvider;

public class CoverageExampleConfiguration
        extends AbstractKlassConfiguration
        implements GraphQLFactoryProvider,
        SPARedirectFilterFactoryProvider
{
    @Nonnull
    private @Valid GraphQLFactory graphQL = new GraphQLFactory();

    private SPARedirectFilterFactory spaRedirectFilterFactory = new SPARedirectFilterFactory();

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
}
