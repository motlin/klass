package cool.klass.dropwizard.configuration.sample.data;

import cool.klass.dropwizard.configuration.data.store.DataStoreFactory;
import cool.klass.dropwizard.configuration.domain.model.loader.DomainModelFactory;

public interface SampleDataFactoryProvider
{
    SampleDataFactory getSampleDataFactory();

    DataStoreFactory getDataStoreFactory();

    DomainModelFactory getDomainModelFactory();
}
