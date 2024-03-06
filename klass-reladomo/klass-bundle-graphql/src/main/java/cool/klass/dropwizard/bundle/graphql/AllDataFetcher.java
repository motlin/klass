package cool.klass.dropwizard.bundle.graphql;

import java.util.List;
import java.util.Objects;

import com.gs.fw.finder.DomainList;
import cool.klass.data.store.reladomo.ReladomoDataStore;
import cool.klass.model.meta.domain.api.Klass;
import cool.klass.model.reladomo.tree.RootReladomoTreeNode;
import cool.klass.model.reladomo.tree.converter.graphql.ReladomoTreeGraphqlConverter;
import cool.klass.reladomo.tree.deep.fetcher.ReladomoTreeNodeDeepFetcherListener;
import cool.klass.reladomo.tree.serializer.ReladomoTreeObjectToDTOSerializerListener;
import graphql.schema.DataFetcher;
import graphql.schema.DataFetchingEnvironment;
import graphql.schema.DataFetchingFieldSelectionSet;
import org.eclipse.collections.api.list.MutableList;

public class AllDataFetcher
        implements DataFetcher<Object>
{
    private final Klass                        klass;
    private final ReladomoDataStore            dataStore;
    private final ReladomoTreeGraphqlConverter reladomoTreeGraphqlConverter;

    public AllDataFetcher(
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
        List<Object> data = this.dataStore.findAll(this.klass);

        DataFetchingFieldSelectionSet selectionSet = environment.getSelectionSet();
        RootReladomoTreeNode rootReladomoTreeNode = this.reladomoTreeGraphqlConverter.convert(
                this.klass,
                selectionSet);

        var deepFetcherListener = new ReladomoTreeNodeDeepFetcherListener(
                this.dataStore,
                (DomainList) data,
                this.klass);
        rootReladomoTreeNode.walk(deepFetcherListener);

        var serializerVisitor = new ReladomoTreeObjectToDTOSerializerListener(
                this.dataStore,
                (DomainList) data,
                this.klass);
        rootReladomoTreeNode.toManyAwareWalk(serializerVisitor);

        MutableList<Object> result = serializerVisitor.getResult();
        return result;
    }
}
