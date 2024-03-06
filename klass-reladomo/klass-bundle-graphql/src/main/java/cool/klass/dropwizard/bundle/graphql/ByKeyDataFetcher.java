package cool.klass.dropwizard.bundle.graphql;

import java.util.List;
import java.util.Objects;

import com.gs.fw.finder.DomainList;
import cool.klass.data.store.reladomo.ReladomoDataStore;
import cool.klass.model.meta.domain.api.Klass;
import cool.klass.model.meta.domain.api.property.DataTypeProperty;
import cool.klass.model.reladomo.tree.RootReladomoTreeNode;
import cool.klass.model.reladomo.tree.converter.graphql.ReladomoTreeGraphqlConverter;
import cool.klass.reladomo.tree.deep.fetcher.ReladomoTreeNodeDeepFetcherListener;
import cool.klass.reladomo.tree.serializer.ReladomoTreeObjectToMapSerializerListener;
import graphql.schema.DataFetcher;
import graphql.schema.DataFetchingEnvironment;
import graphql.schema.DataFetchingFieldSelectionSet;
import org.eclipse.collections.api.map.MutableMap;

public class ByKeyDataFetcher
        implements DataFetcher<Object>
{
    private final Klass                        klass;
    private final ReladomoDataStore            dataStore;
    private final ReladomoTreeGraphqlConverter reladomoTreeGraphqlConverter;

    public ByKeyDataFetcher(
            Klass klass,
            ReladomoDataStore dataStore,
            ReladomoTreeGraphqlConverter reladomoTreeGraphqlConverter)
    {
        this.klass                        = Objects.requireNonNull(klass);
        this.dataStore                    = Objects.requireNonNull(dataStore);
        this.reladomoTreeGraphqlConverter = Objects.requireNonNull(reladomoTreeGraphqlConverter);
    }

    @Override
    public Object get(DataFetchingEnvironment environment)
            throws Exception
    {
        MutableMap<DataTypeProperty, Object> keys = this.klass
                .getKeyProperties()
                .tap(keyProperty -> this.assertEnvironmentContains(environment, keyProperty))
                .toMap(each -> each, keyProperty -> environment.getArgument(keyProperty.getName()));

        List<Object> result = this.dataStore.findByKeyReturningList(this.klass, keys);

        DataFetchingFieldSelectionSet selectionSet = environment.getSelectionSet();
        RootReladomoTreeNode rootReladomoTreeNode = this.reladomoTreeGraphqlConverter.convert(
                this.klass,
                selectionSet);

        var deepFetcherListener = new ReladomoTreeNodeDeepFetcherListener(
                this.dataStore,
                (DomainList) result,
                this.klass);
        rootReladomoTreeNode.walk(deepFetcherListener);

        var serializerVisitor = new ReladomoTreeObjectToMapSerializerListener(
                this.dataStore,
                (DomainList) result,
                this.klass);
        rootReladomoTreeNode.toManyAwareWalk(serializerVisitor);

        return serializerVisitor.getResult();
    }

    private void assertEnvironmentContains(DataFetchingEnvironment environment, DataTypeProperty keyProperty)
    {
        if (environment.containsArgument(keyProperty.getName()))
        {
            return;
        }

        String detailMessage = "Argument " + keyProperty.getName() + " is required for " + this.klass.getName();
        throw new IllegalArgumentException(detailMessage);
    }
}
