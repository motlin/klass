package cool.klass.model.meta.domain.projection;

import java.util.Objects;
import java.util.Optional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import cool.klass.model.meta.domain.AbstractClassifier;
import cool.klass.model.meta.domain.AbstractClassifier.ClassifierBuilder;
import cool.klass.model.meta.domain.api.Element;
import cool.klass.model.meta.domain.api.projection.Projection;
import cool.klass.model.meta.domain.api.projection.ProjectionParent;
import cool.klass.model.meta.domain.api.source.SourceCode;
import cool.klass.model.meta.domain.api.source.SourceCode.SourceCodeBuilder;
import cool.klass.model.meta.domain.api.source.TopLevelElementWithSourceCode;
import org.antlr.v4.runtime.ParserRuleContext;

public final class ProjectionImpl
        extends AbstractProjectionParent
        implements Projection, TopLevelElementWithSourceCode
{
    @Nonnull
    private final String             packageName;
    @Nonnull
    private final AbstractClassifier classifier;

    private ProjectionImpl(
            @Nonnull ParserRuleContext elementContext,
            @Nonnull Optional<Element> macroElement,
            @Nullable SourceCode sourceCode,
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
            implements TopLevelElementBuilderWithSourceCode
    {
        @Nonnull
        private final String               packageName;
        @Nonnull
        private final ClassifierBuilder<?> classifierBuilder;

        public ProjectionBuilder(
                @Nonnull ParserRuleContext elementContext,
                @Nonnull Optional<ElementBuilder<?>> macroElement,
                @Nullable SourceCodeBuilder sourceCode,
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
                    this.sourceCode.build(),
                    this.nameContext,
                    this.name,
                    this.ordinal,
                    this.packageName,
                    this.classifierBuilder.getElement());
        }
    }
}
