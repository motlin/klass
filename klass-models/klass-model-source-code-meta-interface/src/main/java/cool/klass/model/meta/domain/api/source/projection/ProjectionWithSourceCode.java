package cool.klass.model.meta.domain.api.source.projection;

import javax.annotation.Nonnull;

import cool.klass.model.meta.domain.api.projection.Projection;
import cool.klass.model.meta.domain.api.source.ClassifierWithSourceCode;
import cool.klass.model.meta.domain.api.source.TopLevelElementWithSourceCode;
import cool.klass.model.meta.grammar.KlassParser.ProjectionDeclarationContext;

public interface ProjectionWithSourceCode
        extends Projection, TopLevelElementWithSourceCode
{
    @Override
    ProjectionDeclarationContext getElementContext();

    @Nonnull
    @Override
    ClassifierWithSourceCode getClassifier();
}
