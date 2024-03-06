package cool.klass.graphql.type.runtime.wiring.provider;

import java.util.function.Supplier;

import graphql.schema.idl.TypeRuntimeWiring.Builder;

public interface GraphQLTypeRuntimeWiringProvider extends Supplier<Builder>
{
}
