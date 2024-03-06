package cool.klass.model.meta.domain.api.source.projection;

import javax.annotation.Nonnull;

import cool.klass.model.meta.domain.api.projection.ProjectionDataTypeProperty;
import cool.klass.model.meta.domain.api.source.NamedElementWithSourceCode;
import cool.klass.model.meta.domain.api.source.property.DataTypePropertyWithSourceCode;
import cool.klass.model.meta.grammar.KlassParser.ProjectionPrimitiveMemberContext;

public interface ProjectionDataTypePropertyWithSourceCode
        extends ProjectionDataTypeProperty, NamedElementWithSourceCode
{
    @Override
    ProjectionPrimitiveMemberContext getElementContext();

    @Nonnull
    @Override
    DataTypePropertyWithSourceCode getProperty();
}
