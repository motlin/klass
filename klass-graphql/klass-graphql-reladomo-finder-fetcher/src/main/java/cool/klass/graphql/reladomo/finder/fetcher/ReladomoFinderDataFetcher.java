package cool.klass.graphql.reladomo.finder.fetcher;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import com.codahale.metrics.annotation.ExceptionMetered;
import com.codahale.metrics.annotation.Metered;
import com.codahale.metrics.annotation.Timed;
import com.gs.fw.common.mithra.finder.AbstractRelatedFinder;
import com.gs.fw.common.mithra.finder.Operation;
import com.gs.fw.common.mithra.finder.orderby.OrderBy;
import com.gs.fw.finder.DomainList;
import cool.klass.reladomo.graphql.deep.fetcher.GraphQLDeepFetcher;
import graphql.schema.DataFetcher;
import graphql.schema.DataFetchingEnvironment;
import io.liftwizard.graphql.exception.LiftwizardGraphQLException;
import io.liftwizard.reladomo.graphql.operation.GraphQLQueryToOperationConverter;
import io.liftwizard.reladomo.graphql.operation.LiftwizardGraphQLContextException;
import io.liftwizard.reladomo.graphql.orderby.GraphQLQueryToOrderByConverter;

public class ReladomoFinderDataFetcher<T>
        implements DataFetcher<List<T>>
{
    private final String                               className;
    private final AbstractRelatedFinder<T, ?, ?, ?, ?> finder;
    private final GraphQLDeepFetcher                   deepFetcher;

    public ReladomoFinderDataFetcher(
            String className, AbstractRelatedFinder<T, ?, ?, ?, ?> finder, GraphQLDeepFetcher deepFetcher)
    {
        this.className   = Objects.requireNonNull(className);
        this.finder      = Objects.requireNonNull(finder);
        this.deepFetcher = Objects.requireNonNull(deepFetcher);
    }

    @Timed
    @Metered
    @ExceptionMetered
    @Override
    public List<T> get(DataFetchingEnvironment environment)
    {
        Map<String, Object> arguments      = environment.getArguments();
        Object              inputOperation = arguments.get("operation");
        Operation           operation      = this.getOperation((Map<?, ?>) inputOperation);
        Object              inputOrderBy   = arguments.get("orderBy");
        Optional<OrderBy>   orderBys       = this.getOrderBys((List<Map<String, ?>>) inputOrderBy);
        DomainList<T>       result         = (DomainList<T>) this.finder.findMany(operation);
        orderBys.ifPresent(result::setOrderBy);
        this.deepFetcher.deepFetch(result, this.className, this.finder, environment.getSelectionSet());
        return result;
    }

    public Operation getOperation(Map<?, ?> inputOperation)
    {
        try
        {
            var converter = new GraphQLQueryToOperationConverter();
            return converter.convert(this.finder, inputOperation);
        }
        catch (LiftwizardGraphQLContextException e)
        {
            throw new LiftwizardGraphQLException(e.getMessage(), e.getContext(), e);
        }
    }

    public Optional<OrderBy> getOrderBys(List<Map<String, ?>> inputOrderBy)
    {
        try
        {
            return GraphQLQueryToOrderByConverter.convertOrderByList(this.finder, inputOrderBy);
        }
        catch (LiftwizardGraphQLContextException e)
        {
            throw new LiftwizardGraphQLException(e.getMessage(), e.getContext(), e);
        }
    }
}
