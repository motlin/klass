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
    private                 int                 retryCount = 1;

    private DataStore dataStore;

    @Nonnull
    @Override
    public UUIDSupplierFactory getUuidFactory()
    {
        return this.uuidFactory;
    }

    @JsonProperty
    public void setUuid(@Nonnull UUIDSupplierFactory uuidFactory)
    {
        this.uuidFactory = uuidFactory;
    }

    @JsonProperty
    public void setRetryCount(int retryCount)
    {
        this.retryCount = retryCount;
    }

    @Override
    public DataStore createDataStore()
    {
        if (this.dataStore != null)
        {
            return this.dataStore;
        }

        Supplier<UUID> uuidSupplier = this.uuidFactory.createUUIDSupplier();
        this.dataStore = new ReladomoDataStore(uuidSupplier, this.retryCount);
        return this.dataStore;
    }
}
