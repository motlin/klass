package cool.klass.dropwizard.configuration.reladomo;

import cool.klass.dropwizard.configuration.data.store.DataStoreFactory;
import cool.klass.dropwizard.configuration.domain.model.loader.DomainModelFactory;

public interface ReladomoFactoryProvider
{
    ReladomoFactory getReladomoFactory();

    DataStoreFactory getDataStoreFactory();

    DomainModelFactory getDomainModelFactory();
}
