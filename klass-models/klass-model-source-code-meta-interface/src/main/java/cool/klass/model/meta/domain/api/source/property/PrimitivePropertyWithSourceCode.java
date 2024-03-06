package cool.klass.model.meta.domain.api.source.property;

import cool.klass.model.meta.domain.api.property.PrimitiveProperty;
import cool.klass.model.meta.grammar.KlassParser.PrimitivePropertyContext;

public interface PrimitivePropertyWithSourceCode
        extends PrimitiveProperty, DataTypePropertyWithSourceCode
{
    @Override
    PrimitivePropertyContext getElementContext();
}
