package cool.klass.dropwizard.configuration.data.store.reladomo;

import java.util.UUID;
import java.util.function.Supplier;

import javax.annotation.Nonnull;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.google.auto.service.AutoService;
import cool.klass.data.store.DataStore;
import cool.klass.data.store.reladomo.ReladomoDataStore;
import cool.klass.dropwizard.configuration.data.store.DataStoreFactory;
import cool.klass.dropwizard.configuration.uuid.UUIDSupplierFactory;

@JsonTypeName("reladomo")
@AutoService(DataStoreFactory.class)
public class ReladomoDataStoreFactory implements DataStoreFactory
{
    private @NotNull @Valid UUIDSupplierFactory uuidFactory;

    private DataStore dataStore;

    @Override
    public UUIDSupplierFactory getUuidFactory()
    {
        return this.uuidFactory;
    }

    @JsonProperty("uuid")
    public void setUuidFactory(@Nonnull UUIDSupplierFactory uuidFactory)
    {
        this.uuidFactory = uuidFactory;
    }

    @Override
    public DataStore getDataStore()
    {
        if (this.dataStore != null)
        {
            return this.dataStore;
        }
        this.dataStore = this.createDataStore();
        return this.dataStore;
    }

    @Nonnull
    public DataStore createDataStore()
    {
        Supplier<UUID> uuidSupplier = this.uuidFactory.createUUIDSupplier();
        return new ReladomoDataStore(uuidSupplier);
    }
}
