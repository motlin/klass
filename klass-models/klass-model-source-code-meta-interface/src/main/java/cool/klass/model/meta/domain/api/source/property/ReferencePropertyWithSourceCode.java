package cool.klass.model.meta.domain.api.source.property;

import javax.annotation.Nonnull;

import cool.klass.model.meta.domain.api.property.ReferenceProperty;
import cool.klass.model.meta.domain.api.source.ClassifierWithSourceCode;
import cool.klass.model.meta.domain.api.source.NamedElementWithSourceCode;

public interface ReferencePropertyWithSourceCode
        extends ReferenceProperty, NamedElementWithSourceCode
{
    @Nonnull
    @Override
    ClassifierWithSourceCode getType();
}
