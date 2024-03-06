package cool.klass.model.meta.domain;

import java.util.Objects;
import java.util.Optional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import cool.klass.model.meta.domain.api.Association;
import cool.klass.model.meta.domain.api.Element;
import cool.klass.model.meta.domain.api.property.AssociationEnd;
import cool.klass.model.meta.domain.api.source.SourceCode;
import cool.klass.model.meta.domain.api.source.SourceCode.SourceCodeBuilder;
import cool.klass.model.meta.domain.api.source.TopLevelElementWithSourceCode;
import cool.klass.model.meta.domain.criteria.AbstractCriteria;
import cool.klass.model.meta.domain.criteria.AbstractCriteria.AbstractCriteriaBuilder;
import cool.klass.model.meta.domain.property.AssociationEndImpl.AssociationEndBuilder;
import org.antlr.v4.runtime.ParserRuleContext;
import org.eclipse.collections.api.list.ImmutableList;

public final class AssociationImpl
        extends AbstractPackageableElement
        implements Association, TopLevelElementWithSourceCode
{
    @Nonnull
    private final AbstractCriteria criteria;

    private ImmutableList<AssociationEnd> associationEnds;
    private AssociationEnd                sourceAssociationEnd;
    private AssociationEnd                targetAssociationEnd;

    private AssociationImpl(
            @Nonnull ParserRuleContext elementContext,
            @Nonnull Optional<Element> macroElement,
            @Nullable SourceCode sourceCode,
            @Nonnull ParserRuleContext nameContext,
            @Nonnull String name,
            int ordinal,
            @Nonnull String packageName,
            @Nonnull AbstractCriteria criteria)
    {
        super(elementContext, macroElement, sourceCode, nameContext, name, ordinal, packageName);
        this.criteria = Objects.requireNonNull(criteria);
    }

    @Override
    @Nonnull
    public AbstractCriteria getCriteria()
    {
        return this.criteria;
    }

    @Override
    public ImmutableList<AssociationEnd> getAssociationEnds()
    {
        return this.associationEnds;
    }

    private void setAssociationEnds(@Nonnull ImmutableList<AssociationEnd> associationEnds)
    {
        if (associationEnds.size() != 2)
        {
            throw new IllegalArgumentException(String.valueOf(associationEnds.size()));
        }
        this.associationEnds      = Objects.requireNonNull(associationEnds);
        this.sourceAssociationEnd = associationEnds.get(0);
        this.targetAssociationEnd = associationEnds.get(1);
    }

    @Override
    public AssociationEnd getSourceAssociationEnd()
    {
        return this.sourceAssociationEnd;
    }

    @Override
    public AssociationEnd getTargetAssociationEnd()
    {
        return this.targetAssociationEnd;
    }

    public static final class AssociationBuilder
            extends PackageableElementBuilder<AssociationImpl>
            implements TopLevelElementBuilderWithSourceCode
    {
        @Nonnull
        private final AbstractCriteriaBuilder<?> criteriaBuilder;

        private ImmutableList<AssociationEndBuilder> associationEndBuilders;

        public AssociationBuilder(
                @Nonnull ParserRuleContext elementContext,
                @Nonnull Optional<ElementBuilder<?>> macroElement,
                @Nullable SourceCodeBuilder sourceCode,
                @Nonnull ParserRuleContext nameContext,
                @Nonnull String name,
                int ordinal,
                @Nonnull String packageName,
                @Nonnull AbstractCriteriaBuilder<?> criteriaBuilder)
        {
            super(elementContext, macroElement, sourceCode, nameContext, name, ordinal, packageName);
            this.criteriaBuilder = Objects.requireNonNull(criteriaBuilder);
        }

        public void setAssociationEndBuilders(@Nonnull ImmutableList<AssociationEndBuilder> associationEndBuilders)
        {
            this.associationEndBuilders = Objects.requireNonNull(associationEndBuilders);
        }

        @Nonnull
        @Override
        protected AssociationImpl buildUnsafe()
        {
            return new AssociationImpl(
                    this.elementContext,
                    this.macroElement.map(ElementBuilder::getElement),
                    this.sourceCode.build(),
                    this.nameContext,
                    this.name,
                    this.ordinal,
                    this.packageName,
                    this.criteriaBuilder.build());
        }

        @Override
        protected void buildChildren()
        {
            ImmutableList<AssociationEnd> associationEnds = this.associationEndBuilders
                    .collect(AssociationEndBuilder::build);
            this.element.setAssociationEnds(associationEnds);
        }
    }
}
