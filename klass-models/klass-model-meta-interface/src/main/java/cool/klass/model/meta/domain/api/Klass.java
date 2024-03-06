package cool.klass.model.meta.domain.api;

import java.util.Optional;

import javax.annotation.Nonnull;

import cool.klass.model.meta.domain.api.property.AssociationEnd;
import cool.klass.model.meta.domain.api.property.DataTypeProperty;
import org.eclipse.collections.api.list.ImmutableList;

public interface Klass extends Type, PackageableElement
{
    ImmutableList<DataTypeProperty> getDataTypeProperties();

    @Nonnull
    Optional<Klass> getVersionClass();

    @Nonnull
    Optional<Klass> getVersionedClass();

    ImmutableList<AssociationEnd> getAssociationEnds();

    @Nonnull
    ImmutableList<ClassModifier> getClassModifiers();

    boolean isUser();

    boolean isTransient();
}
