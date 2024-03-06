package cool.klass.model.meta.domain.api.property;

import javax.annotation.Nonnull;

import cool.klass.model.meta.domain.api.Klass;
import cool.klass.model.meta.domain.api.TypedElement;

public interface Property extends TypedElement
{
    @Nonnull
    Klass getOwningKlass();

    void visit(PropertyVisitor visitor);
}
