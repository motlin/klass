package cool.klass.graphql.data.fetcher;

import java.util.Objects;
import java.util.function.Function;

import graphql.schema.DataFetcher;
import graphql.schema.DataFetchingEnvironment;

/**
 * @see graphql.schema.PropertyDataFetcher
 */
public class FunctionDataFetcher<Input, Output> implements DataFetcher<Output>
{
    private final Function<Input, Output> function;

    public FunctionDataFetcher(Function<Input, Output> function)
    {
        this.function = Objects.requireNonNull(function);
    }

    @Override
    public Output get(DataFetchingEnvironment environment)
    {
        Input source = environment.getSource();
        if (source == null)
        {
            return null;
        }

        return this.function.apply(source);
    }
}
