package cool.klass.reladomo.persistent.writer;

import com.fasterxml.jackson.databind.node.ObjectNode;
import cool.klass.data.store.DataStore;
import cool.klass.model.meta.domain.api.projection.Projection;
import org.eclipse.collections.api.list.MutableList;
import org.eclipse.collections.impl.factory.Stacks;

public class IncomingUpdateDataModelValidator
{
    private final DataStore           dataStore;
    private final Object              persistentInstance;
    private final ObjectNode          objectNode;
    private final Projection          projection;
    private final MutableList<String> errors;

    public IncomingUpdateDataModelValidator(
            DataStore dataStore,
            Object persistentInstance,
            ObjectNode objectNode,
            Projection projection,
            MutableList<String> errors)
    {
        this.dataStore = dataStore;
        this.persistentInstance = persistentInstance;
        this.objectNode = objectNode;
        this.projection = projection;
        this.errors = errors;
    }

    public static void validate(
            DataStore dataStore,
            Object persistentInstance,
            ObjectNode objectNode,
            Projection projection,
            MutableList<String> errors)
    {
        IncomingUpdateDataModelValidator incomingDataValidator = new IncomingUpdateDataModelValidator(
                dataStore,
                persistentInstance,
                objectNode,
                projection,
                errors);
        incomingDataValidator.validateIncomingData();
    }

    public void validateIncomingData()
    {
        IncomingUpdateDataModelListener listener = new IncomingUpdateDataModelListener(
                this.dataStore,
                this.persistentInstance,
                this.objectNode,
                this.projection,
                this.errors,
                Stacks.mutable.empty(),
                true);
        this.projection.visit(listener);
    }
}
