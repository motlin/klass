package cool.klass.model.meta.domain.api.source.projection;

import javax.annotation.Nonnull;

import cool.klass.model.meta.domain.api.projection.ProjectionReferenceProperty;
import cool.klass.model.meta.domain.api.source.ClassifierWithSourceCode;
import cool.klass.model.meta.domain.api.source.NamedElementWithSourceCode;
import cool.klass.model.meta.domain.api.source.property.ReferencePropertyWithSourceCode;
import cool.klass.model.meta.grammar.KlassParser.ProjectionReferencePropertyContext;

public interface ProjectionReferencePropertyWithSourceCode
        extends ProjectionReferenceProperty, NamedElementWithSourceCode
{
    @Override
    ProjectionReferencePropertyContext getElementContext();

    @Nonnull
    @Override
    ReferencePropertyWithSourceCode getProperty();

    @Nonnull
    @Override
    default ClassifierWithSourceCode getClassifier()
    {
        return this.getProperty().getType();
    }
}
