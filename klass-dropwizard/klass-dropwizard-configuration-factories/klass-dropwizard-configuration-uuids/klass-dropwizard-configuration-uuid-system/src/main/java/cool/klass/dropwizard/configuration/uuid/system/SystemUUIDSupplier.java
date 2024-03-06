package cool.klass.dropwizard.configuration.uuid.system;

import java.util.UUID;
import java.util.function.Supplier;

public class SystemUUIDSupplier implements Supplier<UUID>
{
    @Override
    public UUID get()
    {
        return UUID.randomUUID();
    }
}
