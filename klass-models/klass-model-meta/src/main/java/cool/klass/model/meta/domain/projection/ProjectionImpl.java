package cool.klass.model.meta.domain.projection;

import java.util.Objects;

import javax.annotation.Nonnull;

import cool.klass.model.meta.domain.KlassImpl;
import cool.klass.model.meta.domain.KlassImpl.KlassBuilder;
import cool.klass.model.meta.domain.TopLevelElement;
import cool.klass.model.meta.domain.api.projection.Projection;
import cool.klass.model.meta.domain.api.projection.ProjectionElement;
import org.antlr.v4.runtime.ParserRuleContext;
import org.eclipse.collections.api.list.ImmutableList;

public final class ProjectionImpl extends AbstractProjectionParent implements TopLevelElement, Projection
{
    @Nonnull
    private final String    packageName;
    @Nonnull
    private final KlassImpl klass;

    private ProjectionImpl(
            @Nonnull ParserRuleContext elementContext,
            boolean inferred,
            @Nonnull ParserRuleContext nameContext,
            @Nonnull String name,
            int ordinal,
            @Nonnull String packageName,
            @Nonnull KlassImpl klass)
    {
        super(elementContext, inferred, nameContext, name, ordinal);
        this.packageName = Objects.requireNonNull(packageName);
        this.klass = Objects.requireNonNull(klass);
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

    public static final class ProjectionBuilder extends ProjectionParentBuilder implements TopLevelElementBuilder
    {
        @Nonnull
        private final String         packageName;
        @Nonnull
        private final KlassBuilder   klassBuilder;
        private       ProjectionImpl projection;

        public ProjectionBuilder(
                @Nonnull ParserRuleContext elementContext,
                boolean inferred,
                @Nonnull ParserRuleContext nameContext,
                @Nonnull String name,
                int ordinal,
                @Nonnull String packageName,
                @Nonnull KlassBuilder klassBuilder)
        {
            super(elementContext, inferred, nameContext, name, ordinal);
            this.packageName = Objects.requireNonNull(packageName);
            this.klassBuilder = Objects.requireNonNull(klassBuilder);
        }

        public ProjectionImpl build()
        {
            if (this.projection != null)
            {
                throw new IllegalStateException();
            }
            this.projection = new ProjectionImpl(
                    this.elementContext,
                    this.inferred,
                    this.nameContext,
                    this.name,
                    this.ordinal,
                    this.packageName,
                    this.klassBuilder.getElement());

            ImmutableList<ProjectionElement> children = this.childBuilders.collect(ProjectionElementBuilder::build);

            this.projection.setChildren(children);

            return this.projection;
        }

        @Override
        public ProjectionImpl getElement()
        {
            return Objects.requireNonNull(this.projection);
        }
    }
}
