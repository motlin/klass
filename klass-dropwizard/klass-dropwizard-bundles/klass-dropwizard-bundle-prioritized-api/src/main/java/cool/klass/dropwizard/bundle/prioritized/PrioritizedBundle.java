package cool.klass.dropwizard.bundle.prioritized;

import cool.klass.dropwizard.configuration.AbstractKlassConfiguration;
import io.dropwizard.ConfiguredBundle;

public interface PrioritizedBundle
        extends ConfiguredBundle<AbstractKlassConfiguration>
{
    default int getPriority()
    {
        return 0;
    }
}
