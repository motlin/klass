package cool.klass.dropwizard.bundle.graphql;

import java.util.Objects;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import graphql.kickstart.servlet.GraphQLConfiguration;
import graphql.kickstart.servlet.GraphQLHttpServlet;

public class ConfiguredGraphQLHttpServlet
        extends GraphQLHttpServlet
{
    private final GraphQLConfiguration config;

    public ConfiguredGraphQLHttpServlet(GraphQLConfiguration config)
    {
        this.config = Objects.requireNonNull(config);
    }

    @Override
    protected GraphQLConfiguration getConfiguration()
    {
        return this.config;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
    {
        super.doGet(req, resp);
    }
}
