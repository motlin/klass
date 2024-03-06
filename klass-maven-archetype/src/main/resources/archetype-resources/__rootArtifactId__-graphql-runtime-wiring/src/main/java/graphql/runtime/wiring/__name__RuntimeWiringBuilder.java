package ${package}.graphql.runtime.wiring;

import cool.klass.graphql.scalar.temporal.GraphQLTemporalScalar;
import ${package}.graphql.data.fetcher.all.*;
import ${package}.graphql.data.fetcher.key.*;
import ${package}.graphql.type.runtime.wiring.*;
import graphql.schema.idl.RuntimeWiring;

public class ${name}RuntimeWiringBuilder
{
    public RuntimeWiring buildRuntimeWiring()
    {
        return RuntimeWiring.newRuntimeWiring()
                .scalar(new GraphQLTemporalScalar("Instant"))
                .scalar(new GraphQLTemporalScalar("TemporalInstant"))
                .scalar(new GraphQLTemporalScalar("TemporalRange"))
                .type(
                        "Query",
                        typeWiring -> typeWiring
                )
                .build();
    }
}
