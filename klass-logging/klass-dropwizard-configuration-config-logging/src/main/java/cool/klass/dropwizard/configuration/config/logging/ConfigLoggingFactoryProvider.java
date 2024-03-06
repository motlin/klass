package cool.klass.dropwizard.configuration.config.logging;

import cool.klass.dropwizard.configuration.enabled.EnabledFactory;

public interface ConfigLoggingFactoryProvider
{
    EnabledFactory getConfigLoggingFactory();
}
