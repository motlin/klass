package cool.klass.model.graphql.domain;

import org.eclipse.collections.api.list.ImmutableList;

public class GraphQLClass extends GraphQLClassifier
{
    private final ImmutableList<String> interfaces;

    public GraphQLClass(
            String name,
            ImmutableList<GraphQLField> fields,
            ImmutableList<String> interfaces)
    {
        super(name, fields);
        this.interfaces = interfaces;
    }

    @Override
    public void visit(GraphQLElementVisitor visitor)
    {
        visitor.visitClass(this);
    }

    public ImmutableList<String> getInterfaces()
    {
        return this.interfaces;
    }
}
