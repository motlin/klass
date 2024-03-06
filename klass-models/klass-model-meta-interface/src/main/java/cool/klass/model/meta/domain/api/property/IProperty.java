package cool.klass.model.meta.domain.api.property;

import javax.annotation.Nonnull;

import cool.klass.model.meta.domain.api.IKlass;
import cool.klass.model.meta.domain.api.ITypedElement;
import cool.klass.model.meta.domain.api.Type;

public interface IProperty<T extends Type> extends ITypedElement<T>
{
    @Nonnull
    IKlass getOwningKlass();
}
