package cool.klass.dropwizard.bundle.graphql;

import java.util.Map;
import java.util.Objects;

import com.gs.fw.common.mithra.MithraList;
import com.gs.fw.common.mithra.finder.Operation;
import com.gs.fw.common.mithra.finder.RelatedFinder;
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
import io.liftwizard.graphql.exception.LiftwizardGraphQLException;
import io.liftwizard.model.reladomo.operation.compiler.ReladomoOperationCompiler;
import org.eclipse.collections.api.factory.Lists;

public class ByOperationDataFetcher
        implements DataFetcher<Object>
{
    private final Klass                        klass;
    private final ReladomoDataStore            dataStore;
    private final ReladomoTreeGraphqlConverter reladomoTreeGraphqlConverter;

    private final RelatedFinder<?> finder;

    public ByOperationDataFetcher(
            Klass klass,
            ReladomoDataStore dataStore,
            ReladomoTreeGraphqlConverter reladomoTreeGraphqlConverter)
    {
        this.klass                        = Objects.requireNonNull(klass);
        this.dataStore                    = Objects.requireNonNull(dataStore);
        this.reladomoTreeGraphqlConverter = Objects.requireNonNull(reladomoTreeGraphqlConverter);

        this.finder = this.dataStore.getRelatedFinder(klass);
    }

    @Override
    public Object get(DataFetchingEnvironment environment)
            throws Exception
    {
        Map<String, Object> arguments      = environment.getArguments();
        String              inputOperation = (String) arguments.get("operation");
        Operation           operation      = this.compileOperation(this.finder, inputOperation);
        MithraList<?>       result         = this.finder.findMany(operation);

        DataFetchingFieldSelectionSet selectionSet = environment.getSelectionSet();
        RootReladomoTreeNode rootReladomoTreeNode = this.reladomoTreeGraphqlConverter.convert(
                this.klass,
                selectionSet);

        var deepFetcherListener = new ReladomoTreeNodeDeepFetcherListener(
                this.dataStore,
                result,
                this.klass);
        rootReladomoTreeNode.walk(deepFetcherListener);

        var serializerVisitor = new ReladomoTreeObjectToMapSerializerListener(
                this.dataStore,
                result,
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

    private Operation compileOperation(RelatedFinder<?> relatedFinder, String inputOperation)
    {
        try
        {
            var compiler = new ReladomoOperationCompiler();
            return compiler.compile(relatedFinder, inputOperation);
        }
        catch (RuntimeException e)
        {
            throw new LiftwizardGraphQLException(e.getMessage(), Lists.immutable.with(inputOperation), e);
        }
    }
}
