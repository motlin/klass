package cool.klass.model.meta.domain.api;

import javax.annotation.Nonnull;

import cool.klass.model.meta.domain.api.projection.IProjection;
import cool.klass.model.meta.domain.api.service.IServiceGroup;
import org.eclipse.collections.api.list.ImmutableList;

public interface IDomainModel
{
    @Nonnull
    ImmutableList<IEnumeration> getEnumerations();

    @Nonnull
    ImmutableList<IKlass> getKlasses();

    @Nonnull
    ImmutableList<IAssociation> getAssociations();

    @Nonnull
    ImmutableList<IProjection> getProjections();

    @Nonnull
    ImmutableList<IServiceGroup> getServiceGroups();

    IEnumeration getEnumerationByName(String name);

    IKlass getKlassByName(String name);

    IAssociation getAssociationByName(String name);

    IProjection getProjectionByName(String name);
}
