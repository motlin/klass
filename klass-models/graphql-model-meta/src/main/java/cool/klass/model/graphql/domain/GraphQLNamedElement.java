package cool.klass.model.graphql.domain;

import java.util.Objects;

public abstract class GraphQLNamedElement
{
    protected final String name;

    protected GraphQLNamedElement(String name)
    {
        this.name = Objects.requireNonNull(name);
    }

    public String getName()
    {
        return this.name;
    }
}
