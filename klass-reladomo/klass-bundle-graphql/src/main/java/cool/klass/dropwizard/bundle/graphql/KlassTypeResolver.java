package cool.klass.dropwizard.bundle.graphql;

import java.util.Map;
import java.util.Objects;

import cool.klass.model.meta.domain.api.Klass;
import graphql.TypeResolutionEnvironment;
import graphql.schema.GraphQLInterfaceType;
import graphql.schema.GraphQLObjectType;
import graphql.schema.GraphQLType;
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
        if (!(env.getObject() instanceof Map))
        {
            throw new AssertionError("Expected Map but got " + env.getObject());
        }

        Map<String, Object> map = env.getObject();
        if (map.containsKey("__typeName"))
        {
            String            typeName = (String) map.get("__typeName");
            GraphQLType       graphQLType = env.getSchema().getTypeAs(typeName);
            if (graphQLType instanceof GraphQLObjectType objectType)
            {
                return objectType;
            }
            if (graphQLType instanceof GraphQLInterfaceType)
            {
                Klass detect = this.klass
                        .getSubClassChain()
                        .detect(subClass -> subClass.getSubClasses().isEmpty());
                String subClassName = detect.getName();
                GraphQLObjectType result = env.getSchema().getObjectType(subClassName);
                return result;
            }

            throw new AssertionError("Expected GraphQLObjectType or GraphQLInterfaceType but got " + graphQLType);
        }

        String            klassName = this.klass.getName();
        GraphQLObjectType result    = env.getSchema().getObjectType(klassName);
        return result;
    }
}
