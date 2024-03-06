package cool.klass.dropwizard.configuration.data.store;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeInfo.Id;
import com.google.auto.service.AutoService;
import cool.klass.data.store.DataStore;
import io.dropwizard.jackson.Discoverable;
import io.liftwizard.dropwizard.configuration.uuid.UUIDSupplierFactory;

@JsonTypeInfo(use = Id.NAME, property = "type")
@AutoService(Discoverable.class)
public interface DataStoreFactory extends Discoverable
{
    UUIDSupplierFactory getUuidFactory();

    DataStore createDataStore();
}
