package cool.klass.dropwizard.bundle.api;

import cool.klass.dropwizard.bundle.prioritized.PrioritizedBundle;
import cool.klass.model.meta.domain.api.DomainModel;

public interface DataBundle extends PrioritizedBundle
{
    void initialize(DomainModel domainModel);
}
