package cool.klass.dropwizard.bundle.prioritized;

import io.dropwizard.Bundle;

public interface PrioritizedBundle extends Bundle
{
    default int getPriority()
    {
        return 0;
    }
}
