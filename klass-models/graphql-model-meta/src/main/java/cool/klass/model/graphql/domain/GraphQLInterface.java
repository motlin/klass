package cool.klass.model.graphql.domain;

import javax.annotation.Nonnull;

import org.eclipse.collections.api.list.ImmutableList;

public class GraphQLInterface extends GraphQLClassifier
{
    public GraphQLInterface(String name, ImmutableList<GraphQLField> fields)
    {
        super(name, fields);
    }

    @Override
    public void visit(@Nonnull GraphQLElementVisitor visitor)
    {
        visitor.visitInterface(this);
    }
}
