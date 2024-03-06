package cool.klass.model.meta.domain.projection;

import java.util.Objects;
import java.util.Optional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import cool.klass.model.meta.domain.AbstractIdentifierElement;
import cool.klass.model.meta.domain.api.Element;
import cool.klass.model.meta.domain.api.projection.ProjectionChild;
import cool.klass.model.meta.domain.api.projection.ProjectionParent;
import cool.klass.model.meta.domain.api.source.SourceCode;
import cool.klass.model.meta.domain.api.source.SourceCode.SourceCodeBuilder;
import cool.klass.model.meta.grammar.KlassParser.IdentifierContext;
import org.antlr.v4.runtime.ParserRuleContext;
import org.eclipse.collections.api.list.ImmutableList;

public abstract class AbstractProjectionParent
        extends AbstractIdentifierElement
        implements AbstractProjectionElement, ProjectionParent
{
    private ImmutableList<ProjectionChild> children;

    protected AbstractProjectionParent(
            @Nonnull ParserRuleContext elementContext,
            @Nonnull Optional<Element> macroElement,
            @Nullable SourceCode sourceCode,
            int ordinal,
            @Nonnull IdentifierContext nameContext)
    {
        super(elementContext, macroElement, sourceCode, ordinal, nameContext);
    }

    @Override
    public ImmutableList<? extends ProjectionChild> getChildren()
    {
        return Objects.requireNonNull(this.children);
    }

    protected void setChildren(@Nonnull ImmutableList<ProjectionChild> children)
    {
        this.children = Objects.requireNonNull(children);
    }

    public abstract static class AbstractProjectionParentBuilder<BuiltElement extends AbstractProjectionParent>
            extends IdentifierElementBuilder<BuiltElement>
            implements ProjectionElementBuilder
    {
        protected ImmutableList<ProjectionChildBuilder> childBuilders;

        protected AbstractProjectionParentBuilder(
                @Nonnull ParserRuleContext elementContext,
                @Nonnull Optional<ElementBuilder<?>> macroElement,
                @Nullable SourceCodeBuilder sourceCode,
                int ordinal,
                @Nonnull IdentifierContext nameContext)
        {
            super(elementContext, macroElement, sourceCode, ordinal, nameContext);
        }

        public void setChildBuilders(@Nonnull ImmutableList<ProjectionChildBuilder> projectionChildrenBuilders)
        {
            this.childBuilders = Objects.requireNonNull(projectionChildrenBuilders);
        }

        @Override
        protected void buildChildren()
        {
            ImmutableList<ProjectionChild> children = this.childBuilders.collect(ProjectionChildBuilder::build);
            this.getElement().setChildren(children);
        }

        @Override
        public void build2()
        {
            this.childBuilders.each(ProjectionElementBuilder::build2);
        }
    }
}
