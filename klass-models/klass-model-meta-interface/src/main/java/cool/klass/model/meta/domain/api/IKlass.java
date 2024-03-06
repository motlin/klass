package cool.klass.model.meta.domain.api;

import java.util.Optional;

import javax.annotation.Nonnull;

import cool.klass.model.meta.domain.api.property.IAssociationEnd;
import cool.klass.model.meta.domain.api.property.IDataTypeProperty;
import org.eclipse.collections.api.list.ImmutableList;

public interface IKlass extends Type
{
    ImmutableList<IDataTypeProperty<?>> getDataTypeProperties();

    @Nonnull
    Optional<IKlass> getVersionClass();

    @Nonnull
    Optional<IKlass> getVersionedClass();

    ImmutableList<IAssociationEnd> getAssociationEnds();

    @Nonnull
    ImmutableList<IClassModifier> getClassModifiers();

    boolean isUser();

    boolean isTransient();
}
