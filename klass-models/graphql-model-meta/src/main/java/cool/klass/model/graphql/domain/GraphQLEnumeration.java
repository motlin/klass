package cool.klass.model.graphql.domain;

import java.util.Objects;

import javax.annotation.Nonnull;

import org.eclipse.collections.api.list.ImmutableList;

public class GraphQLEnumeration extends GraphQLNamedElement implements GraphQLElement
{
    private final ImmutableList<GraphQLEnumerationLiteral> enumerationLiterals;

    public GraphQLEnumeration(
            String name,
            ImmutableList<GraphQLEnumerationLiteral> enumerationLiterals)
    {
        super(name);
        this.enumerationLiterals = Objects.requireNonNull(enumerationLiterals);
    }

    @Override
    public void visit(@Nonnull GraphQLElementVisitor visitor)
    {
        visitor.visitEnumeration(this);
    }

    public ImmutableList<GraphQLEnumerationLiteral> getEnumerationLiterals()
    {
        return this.enumerationLiterals;
    }
}
