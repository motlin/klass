package cool.klass.model.meta.domain.api;

import javax.annotation.Nonnull;

import cool.klass.model.meta.domain.api.projection.Projection;
import cool.klass.model.meta.domain.api.service.ServiceGroup;
import org.eclipse.collections.api.list.ImmutableList;

public interface DomainModel
{
    @Nonnull
    ImmutableList<PackageableElement> getTopLevelElements();

    @Nonnull
    ImmutableList<Enumeration> getEnumerations();

    @Nonnull
    ImmutableList<Interface> getInterfaces();

    @Nonnull
    ImmutableList<Klass> getKlasses();

    @Nonnull
    ImmutableList<Association> getAssociations();

    @Nonnull
    ImmutableList<Projection> getProjections();

    @Nonnull
    ImmutableList<ServiceGroup> getServiceGroups();

    Enumeration getEnumerationByName(String name);

    Interface getInterfaceByName(String name);

    Klass getKlassByName(String name);

    Association getAssociationByName(String name);

    Projection getProjectionByName(String name);
}
