package cool.klass.dropwizard.bundle.graphql;

import java.util.List;

import graphql.GraphQLError;
import graphql.kickstart.execution.error.DefaultGraphQLErrorHandler;

public class KlassGraphQLErrorHandler
        extends DefaultGraphQLErrorHandler
{
    @Override
    public List<GraphQLError> processErrors(List<GraphQLError> errors)
    {
        return super.processErrors(errors);
    }

    @Override
    protected void logError(GraphQLError error)
    {
        super.logError(error);
    }

    @Override
    protected List<GraphQLError> filterGraphQLErrors(List<GraphQLError> errors)
    {
        return super.filterGraphQLErrors(errors);
    }

    @Override
    protected boolean isClientError(GraphQLError error)
    {
        return super.isClientError(error);
    }
}
