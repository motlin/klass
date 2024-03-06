package cool.klass.model.meta.domain.api.value;

import javax.annotation.Nonnull;

import cool.klass.model.meta.domain.api.Klass;
import cool.klass.model.meta.domain.api.property.AssociationEnd;
import cool.klass.model.meta.domain.api.property.DataTypeProperty;
import org.eclipse.collections.api.list.ImmutableList;

public interface MemberReferencePath extends ExpressionValue
{
    @Nonnull
    Klass getKlass();

    @Nonnull
    ImmutableList<AssociationEnd> getAssociationEnds();

    @Nonnull
    DataTypeProperty getProperty();
}
