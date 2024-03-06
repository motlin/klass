package cool.klass.model.graphql.domain;

import java.util.Objects;

import org.eclipse.collections.api.list.ImmutableList;

public class GraphQLDomainModel
{
    private final ImmutableList<GraphQLElement> topLevelElements;

    public GraphQLDomainModel(ImmutableList<GraphQLElement> topLevelElements)
    {
        this.topLevelElements = Objects.requireNonNull(topLevelElements);
    }

    public ImmutableList<GraphQLElement> getTopLevelElements()
    {
        return this.topLevelElements;
    }
}
