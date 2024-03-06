package cool.klass.model.meta.domain.api.source.property;

import javax.annotation.Nonnull;

import cool.klass.model.meta.domain.api.property.EnumerationProperty;
import cool.klass.model.meta.domain.api.source.EnumerationWithSourceCode;
import cool.klass.model.meta.grammar.KlassParser.EnumerationPropertyContext;

public interface EnumerationPropertyWithSourceCode
        extends EnumerationProperty, DataTypePropertyWithSourceCode
{
    @Override
    EnumerationPropertyContext getElementContext();

    @Nonnull
    @Override
    EnumerationWithSourceCode getType();
}
