package cool.klass.model.meta.domain.projection;

import java.util.Objects;

import javax.annotation.Nonnull;

import cool.klass.model.meta.domain.Klass;
import cool.klass.model.meta.domain.Klass.KlassBuilder;
import cool.klass.model.meta.domain.projection.ProjectionMember.ProjectionMemberBuilder;
import org.antlr.v4.runtime.ParserRuleContext;
import org.eclipse.collections.api.list.ImmutableList;

public final class Projection extends ProjectionParent
{
    @Nonnull
    private final String packageName;
    @Nonnull
    private final Klass  klass;

    private Projection(
            @Nonnull ParserRuleContext elementContext,
            @Nonnull ParserRuleContext nameContext,
            @Nonnull String name,
            @Nonnull String packageName,
            @Nonnull Klass klass)
    {
        super(elementContext, nameContext, name);
        this.packageName = Objects.requireNonNull(packageName);
        this.klass = Objects.requireNonNull(klass);
    }

    @Nonnull
    public Klass getKlass()
    {
        return this.klass;
    }

    public static final class ProjectionBuilder extends ProjectionParentBuilder
    {
        @Nonnull
        private final String       packageName;
        @Nonnull
        private final KlassBuilder klassBuilder;
        private       Projection   projection;

        public ProjectionBuilder(
                @Nonnull ParserRuleContext elementContext,
                @Nonnull ParserRuleContext nameContext,
                @Nonnull String name,
                @Nonnull String packageName,
                @Nonnull KlassBuilder klassBuilder)
        {
            super(elementContext, nameContext, name);
            this.packageName = Objects.requireNonNull(packageName);
            this.klassBuilder = Objects.requireNonNull(klassBuilder);
        }

        public Projection build()
        {
            if (this.projection != null)
            {
                throw new IllegalStateException();
            }
            this.projection = new Projection(
                    this.elementContext,
                    this.nameContext,
                    this.name,
                    this.packageName,
                    this.klassBuilder.getKlass());

            ImmutableList<ProjectionMember> projectionMembers = this.projectionMemberBuilders.collect(
                    ProjectionMemberBuilder::build);

            this.projection.setProjectionMembers(projectionMembers);

            return this.projection;
        }

        public Projection getProjection()
        {
            return Objects.requireNonNull(this.projection);
        }
    }
}
