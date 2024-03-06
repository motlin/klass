package cool.klass.model.graphql.domain;

import java.util.Objects;

import org.eclipse.collections.api.list.ImmutableList;

public abstract class GraphQLClassifier extends GraphQLNamedElement implements GraphQLElement
{
    protected final ImmutableList<GraphQLField> fields;

    protected GraphQLClassifier(
            String name,
            ImmutableList<GraphQLField> fields)
    {
        super(name);
        this.fields = Objects.requireNonNull(fields);
    }

    public ImmutableList<GraphQLField> getFields()
    {
        return this.fields;
    }
}
