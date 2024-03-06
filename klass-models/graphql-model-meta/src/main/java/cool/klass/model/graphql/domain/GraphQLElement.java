package cool.klass.model.graphql.domain;

public interface GraphQLElement
{
    void visit(GraphQLElementVisitor visitor);
}
