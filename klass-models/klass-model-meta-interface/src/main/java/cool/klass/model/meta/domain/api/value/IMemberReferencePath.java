package cool.klass.model.meta.domain.api.value;

import javax.annotation.Nonnull;

import cool.klass.model.meta.domain.api.IKlass;
import cool.klass.model.meta.domain.api.property.IAssociationEnd;
import cool.klass.model.meta.domain.api.property.IDataTypeProperty;
import org.eclipse.collections.api.list.ImmutableList;

public interface IMemberReferencePath extends IExpressionValue
{
    @Nonnull
    IKlass getKlass();

    @Nonnull
    ImmutableList<IAssociationEnd> getAssociationEnds();

    @Nonnull
    IDataTypeProperty<?> getProperty();
}
