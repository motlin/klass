package cool.klass.dropwizard.configuration.uuid.system;

import java.util.UUID;
import java.util.function.Supplier;

import com.fasterxml.jackson.annotation.JsonTypeName;
import com.google.auto.service.AutoService;
import cool.klass.dropwizard.configuration.uuid.UUIDSupplierFactory;

@JsonTypeName("system")
@AutoService(UUIDSupplierFactory.class)
public class SystemUUIDSupplierFactory implements UUIDSupplierFactory
{
    @Override
    public Supplier<UUID> createUUIDSupplier()
    {
        return new SystemUUIDSupplier();
    }
}
