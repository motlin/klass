package cool.klass.dropwizard.configuration.domain.model.loader;

import javax.annotation.Nonnull;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeInfo.Id;
import com.google.auto.service.AutoService;
import cool.klass.model.meta.domain.api.DomainModel;
import io.dropwizard.jackson.Discoverable;

@JsonTypeInfo(use = Id.NAME, property = "type")
@AutoService(Discoverable.class)
public interface DomainModelFactory
        extends Discoverable
{
    @Nonnull
    DomainModel createDomainModel();
}
