package cool.klass.model.meta.domain;

import java.util.Objects;

import javax.annotation.Nonnull;

import cool.klass.model.meta.domain.api.Association;
import cool.klass.model.meta.domain.api.property.AssociationEnd;
import cool.klass.model.meta.domain.criteria.AbstractCriteria;
import cool.klass.model.meta.domain.criteria.AbstractCriteria.CriteriaBuilder;
import cool.klass.model.meta.domain.property.AssociationEndImpl.AssociationEndBuilder;
import org.antlr.v4.runtime.ParserRuleContext;
import org.eclipse.collections.api.list.ImmutableList;

public final class AssociationImpl extends AbstractPackageableElement implements TopLevelElement, Association
{
    private final AbstractCriteria criteria;

    private ImmutableList<AssociationEnd> associationEnds;
    private AssociationEnd                sourceAssociationEnd;
    private AssociationEnd                targetAssociationEnd;

    private AssociationImpl(
            @Nonnull ParserRuleContext elementContext,
            boolean inferred,
            @Nonnull ParserRuleContext nameContext,
            @Nonnull String name,
            int ordinal,
            @Nonnull String packageName,
            AbstractCriteria criteria)
    {
        super(elementContext, inferred, nameContext, name, ordinal, packageName);
        this.criteria = criteria;
    }

    @Override
    public AbstractCriteria getCriteria()
    {
        return this.criteria;
    }

    @Override
    public ImmutableList<AssociationEnd> getAssociationEnds()
    {
        return this.associationEnds;
    }

    private void setAssociationEnds(ImmutableList<AssociationEnd> associationEnds)
    {
        if (associationEnds.size() != 2)
        {
            throw new IllegalArgumentException(String.valueOf(associationEnds.size()));
        }
        this.associationEnds = Objects.requireNonNull(associationEnds);
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

    public static class AssociationBuilder extends PackageableElementBuilder implements TopLevelElementBuilder
    {
        @Nonnull
        private final CriteriaBuilder criteriaBuilder;

        private ImmutableList<AssociationEndBuilder> associationEndBuilders;
        private AssociationImpl                      association;

        public AssociationBuilder(
                @Nonnull ParserRuleContext elementContext,
                boolean inferred,
                @Nonnull ParserRuleContext nameContext,
                @Nonnull String name,
                int ordinal,
                @Nonnull String packageName,
                @Nonnull CriteriaBuilder criteriaBuilder)
        {
            super(elementContext, inferred, nameContext, name, ordinal, packageName);
            this.criteriaBuilder = Objects.requireNonNull(criteriaBuilder);
        }

        public void setAssociationEndBuilders(@Nonnull ImmutableList<AssociationEndBuilder> associationEndBuilders)
        {
            this.associationEndBuilders = Objects.requireNonNull(associationEndBuilders);
        }

        public AssociationImpl build()
        {
            if (this.association != null)
            {
                throw new IllegalStateException();
            }
            this.association = new AssociationImpl(
                    this.elementContext,
                    this.inferred,
                    this.nameContext,
                    this.name,
                    this.ordinal,
                    this.packageName,
                    this.criteriaBuilder.build());

            ImmutableList<AssociationEnd> associationEnds = this.associationEndBuilders
                    .collect(AssociationEndBuilder::build);

            this.association.setAssociationEnds(associationEnds);
            return this.association;
        }

        @Override
        @Nonnull
        public AssociationImpl getElement()
        {
            return Objects.requireNonNull(this.association);
        }
    }
}
