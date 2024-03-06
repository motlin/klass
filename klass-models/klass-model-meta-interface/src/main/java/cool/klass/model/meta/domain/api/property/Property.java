package cool.klass.model.meta.domain.api.property;

import javax.annotation.Nonnull;

import cool.klass.model.meta.domain.api.Classifier;
import cool.klass.model.meta.domain.api.TypedElement;
import cool.klass.model.meta.domain.api.modifier.ModifierOwner;

public interface Property
        extends TypedElement, ModifierOwner
{
    @Nonnull
    Classifier getOwningClassifier();

    boolean isRequired();

    boolean isDerived();

    boolean isPrivate();

    void visit(PropertyVisitor visitor);
}
