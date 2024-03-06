package cool.klass.model.meta.domain.projection;

import java.util.Objects;
import java.util.Optional;

import javax.annotation.Nonnull;

import cool.klass.model.meta.domain.AbstractClassifier;
import cool.klass.model.meta.domain.AbstractClassifier.ClassifierBuilder;
import cool.klass.model.meta.domain.TopLevelElement;
import cool.klass.model.meta.domain.api.Element;
import cool.klass.model.meta.domain.api.projection.Projection;
import cool.klass.model.meta.domain.api.projection.ProjectionParent;
import cool.klass.model.meta.domain.api.source.SourceCode;
import cool.klass.model.meta.domain.api.source.SourceCode.SourceCodeBuilder;
import org.antlr.v4.runtime.ParserRuleContext;

public final class ProjectionImpl
        extends AbstractProjectionParent
        implements TopLevelElement, Projection
{
    @Nonnull
    private final String             packageName;
    @Nonnull
    private final AbstractClassifier classifier;

    private ProjectionImpl(
            @Nonnull ParserRuleContext elementContext,
            @Nonnull Optional<Element> macroElement,
            @Nonnull Optional<SourceCode> sourceCode,
            @Nonnull ParserRuleContext nameContext,
            @Nonnull String name,
            int ordinal,
            @Nonnull String packageName,
            @Nonnull AbstractClassifier classifier)
    {
        super(elementContext, macroElement, sourceCode, nameContext, name, ordinal);
        this.packageName = Objects.requireNonNull(packageName);
        this.classifier  = Objects.requireNonNull(classifier);
    }

    @Override
    public Optional<ProjectionParent> getParent()
    {
        return Optional.empty();
    }

    @Override
    @Nonnull
    public AbstractClassifier getClassifier()
    {
        return this.classifier;
    }

    @Nonnull
    @Override
    public String getPackageName()
    {
        return this.packageName;
    }

    public static final class ProjectionBuilder
            extends AbstractProjectionParentBuilder<ProjectionImpl>
            implements TopLevelElementBuilder
    {
        @Nonnull
        private final String               packageName;
        @Nonnull
        private final ClassifierBuilder<?> classifierBuilder;

        public ProjectionBuilder(
                @Nonnull ParserRuleContext elementContext,
                @Nonnull Optional<ElementBuilder<?>> macroElement,
                @Nonnull Optional<SourceCodeBuilder> sourceCode,
                @Nonnull ParserRuleContext nameContext,
                @Nonnull String name,
                int ordinal,
                @Nonnull String packageName,
                @Nonnull ClassifierBuilder<?> classifierBuilder)
        {
            super(elementContext, macroElement, sourceCode, nameContext, name, ordinal);
            this.packageName       = Objects.requireNonNull(packageName);
            this.classifierBuilder = Objects.requireNonNull(classifierBuilder);
        }

        @Override
        @Nonnull
        protected ProjectionImpl buildUnsafe()
        {
            return new ProjectionImpl(
                    this.elementContext,
                    this.macroElement.map(ElementBuilder::getElement),
                    this.sourceCode.map(SourceCodeBuilder::build),
                    this.nameContext,
                    this.name,
                    this.ordinal,
                    this.packageName,
                    this.classifierBuilder.getElement());
        }
    }
}
