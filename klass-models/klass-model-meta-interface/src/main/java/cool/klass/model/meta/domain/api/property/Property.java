package cool.klass.model.meta.domain.api.property;

import javax.annotation.Nonnull;

import cool.klass.model.meta.domain.api.Classifier;
import cool.klass.model.meta.domain.api.TypedElement;

public interface Property extends TypedElement
{
    @Nonnull
    Classifier getOwningClassifier();

    void visit(PropertyVisitor visitor);
}
