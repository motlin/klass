package cool.klass.model.graphql.domain;

import java.util.Objects;

public class GraphQLField extends GraphQLNamedElement
{
    private final String  type;
    private final boolean isMany;
    private final boolean isRequired;

    public GraphQLField(String name, String type, boolean isMany, boolean isRequired)
    {
        super(name);
        this.type       = Objects.requireNonNull(type);
        this.isMany     = isMany;
        this.isRequired = isRequired;
    }

    public String getType()
    {
        return this.type;
    }

    public boolean isMany()
    {
        return this.isMany;
    }

    public boolean isRequired()
    {
        return this.isRequired;
    }
}
