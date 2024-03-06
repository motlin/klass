package cool.klass.model.meta.domain.api.source.property;

import javax.annotation.Nonnull;

import cool.klass.model.meta.domain.api.property.ParameterizedProperty;
import cool.klass.model.meta.domain.api.source.KlassWithSourceCode;
import cool.klass.model.meta.grammar.KlassParser.ParameterizedPropertyContext;

public interface ParameterizedPropertyWithSourceCode
        extends ParameterizedProperty, ReferencePropertyWithSourceCode
{
    @Nonnull
    @Override
    KlassWithSourceCode getType();

    @Override
    ParameterizedPropertyContext getElementContext();
}
