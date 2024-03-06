package cool.klass.model.meta.domain.projection;

import java.util.Objects;
import java.util.Optional;

import javax.annotation.Nonnull;

import cool.klass.model.meta.domain.KlassImpl;
import cool.klass.model.meta.domain.KlassImpl.KlassBuilder;
import cool.klass.model.meta.domain.TopLevelElement;
import cool.klass.model.meta.domain.api.Element;
import cool.klass.model.meta.domain.api.projection.Projection;
import cool.klass.model.meta.domain.api.projection.ProjectionParent;
import org.antlr.v4.runtime.ParserRuleContext;

public final class ProjectionImpl extends AbstractProjectionParent implements TopLevelElement, Projection
{
    @Nonnull
    private final String    packageName;
    @Nonnull
    private final KlassImpl klass;

    private ProjectionImpl(
            @Nonnull ParserRuleContext elementContext,
            @Nonnull Optional<Element> macroElement,
            @Nonnull ParserRuleContext nameContext,
            @Nonnull String name,
            int ordinal,
            @Nonnull String packageName,
            @Nonnull KlassImpl klass)
    {
        super(elementContext, macroElement, nameContext, name, ordinal);
        this.packageName = Objects.requireNonNull(packageName);
        this.klass = Objects.requireNonNull(klass);
    }

    @Override
    public Optional<ProjectionParent> getParent()
    {
        return Optional.empty();
    }

    @Override
    @Nonnull
    public KlassImpl getKlass()
    {
        return this.klass;
    }

    @Nonnull
    @Override
    public String getPackageName()
    {
        return this.packageName;
    }

    public static final class ProjectionBuilder extends AbstractProjectionParentBuilder<ProjectionImpl> implements TopLevelElementBuilder
    {
        @Nonnull
        private final String       packageName;
        @Nonnull
        private final KlassBuilder klassBuilder;

        public ProjectionBuilder(
                @Nonnull ParserRuleContext elementContext,
                @Nonnull Optional<ElementBuilder<?>> macroElement,
                @Nonnull ParserRuleContext nameContext,
                @Nonnull String name,
                int ordinal,
                @Nonnull String packageName,
                @Nonnull KlassBuilder klassBuilder)
        {
            super(elementContext, macroElement, nameContext, name, ordinal);
            this.packageName = Objects.requireNonNull(packageName);
            this.klassBuilder = Objects.requireNonNull(klassBuilder);
        }

        @Override
        @Nonnull
        protected ProjectionImpl buildUnsafe()
        {
            return new ProjectionImpl(
                    this.elementContext,
                    this.macroElement.map(ElementBuilder::getElement),
                    this.nameContext,
                    this.name,
                    this.ordinal,
                    this.packageName,
                    this.klassBuilder.getElement());
        }
    }
}
