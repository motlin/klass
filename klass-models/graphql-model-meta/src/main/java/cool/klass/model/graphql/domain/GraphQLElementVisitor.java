package cool.klass.model.graphql.domain;

public interface GraphQLElementVisitor
{
    void visitEnumeration(GraphQLEnumeration graphQLEnumeration);

    void visitInterface(GraphQLInterface graphQLInterface);

    void visitClass(GraphQLClass graphQLClass);
}
