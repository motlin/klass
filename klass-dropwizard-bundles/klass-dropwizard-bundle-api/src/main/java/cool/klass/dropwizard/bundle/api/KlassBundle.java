package cool.klass.dropwizard.bundle.api;

import cool.klass.data.store.DataStore;
import cool.klass.model.meta.domain.api.DomainModel;
import io.dropwizard.Bundle;

public interface KlassBundle extends Bundle
{
    void initialize(DomainModel domainModel, DataStore dataStore);
}
