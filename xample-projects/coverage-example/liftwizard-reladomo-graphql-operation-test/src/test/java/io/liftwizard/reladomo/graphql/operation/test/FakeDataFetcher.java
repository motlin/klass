package io.liftwizard.reladomo.graphql.operation.test;

import java.util.Objects;

import com.gs.fw.common.mithra.finder.RelatedFinder;
import graphql.schema.DataFetcher;
import graphql.schema.DataFetchingEnvironment;
import org.junit.Assert;

public class FakeDataFetcher<T> implements DataFetcher<T>
{
    private final RelatedFinder<T> finder;

    public FakeDataFetcher(RelatedFinder<T> finder)
    {
        this.finder = Objects.requireNonNull(finder);
    }

    @Override
    public T get(DataFetchingEnvironment environment)
    {
        Assert.fail();
        return null;
    }
}
