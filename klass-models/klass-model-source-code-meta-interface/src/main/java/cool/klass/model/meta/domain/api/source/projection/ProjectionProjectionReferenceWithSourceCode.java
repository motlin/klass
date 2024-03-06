package cool.klass.model.meta.domain.api.source.projection;

import javax.annotation.Nonnull;

import cool.klass.model.meta.domain.api.projection.ProjectionProjectionReference;
import cool.klass.model.meta.domain.api.source.ClassifierWithSourceCode;
import cool.klass.model.meta.domain.api.source.NamedElementWithSourceCode;
import cool.klass.model.meta.domain.api.source.property.ReferencePropertyWithSourceCode;
import cool.klass.model.meta.grammar.KlassParser.ProjectionProjectionReferenceContext;

public interface ProjectionProjectionReferenceWithSourceCode
        extends ProjectionProjectionReference, NamedElementWithSourceCode
{
    @Override
    ProjectionProjectionReferenceContext getElementContext();

    @Override
    ProjectionWithSourceCode getProjection();

    @Nonnull
    @Override
    default ClassifierWithSourceCode getClassifier()
    {
        return this.getProjection().getClassifier();
    }

    @Nonnull
    @Override
    ReferencePropertyWithSourceCode getProperty();
}
