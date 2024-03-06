package cool.klass.dropwizard.bundle.graphql;

import java.util.Objects;

import cool.klass.model.meta.domain.api.Klass;
import graphql.TypeResolutionEnvironment;
import graphql.schema.GraphQLObjectType;
import graphql.schema.TypeResolver;

public class KlassTypeResolver
        implements TypeResolver
{
    private final Klass klass;

    public KlassTypeResolver(Klass klass)
    {
        this.klass = Objects.requireNonNull(klass);
    }

    @Override
    public GraphQLObjectType getType(TypeResolutionEnvironment env)
    {
        // TODO: Assert that the resolved type is a subclass of this.klass
        String simpleName = env.getObject().getClass().getSimpleName();
        if (simpleName.endsWith("DTO"))
        {
            // Chop off "DTO" from the end
            String truncated = simpleName.substring(0, simpleName.length() - 3);
            return env.getSchema().getObjectType(truncated);
        }
        throw new AssertionError("Expected DTO but got " + simpleName);
    }
}
