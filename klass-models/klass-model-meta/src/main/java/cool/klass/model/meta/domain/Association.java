package cool.klass.model.meta.domain;

import java.util.Objects;

import javax.annotation.Nonnull;

import cool.klass.model.meta.domain.criteria.Criteria;
import cool.klass.model.meta.domain.criteria.Criteria.CriteriaBuilder;
import cool.klass.model.meta.domain.property.AssociationEnd;
import cool.klass.model.meta.domain.property.AssociationEnd.AssociationEndBuilder;
import org.antlr.v4.runtime.ParserRuleContext;
import org.eclipse.collections.api.list.ImmutableList;

public final class Association extends PackageableElement
{
    private final Criteria criteria;

    private ImmutableList<AssociationEnd> associationEnds;
    private AssociationEnd                sourceAssociationEnd;
    private AssociationEnd                targetAssociationEnd;

    private Association(
            @Nonnull ParserRuleContext elementContext,
            @Nonnull ParserRuleContext nameContext,
            @Nonnull String name,
            @Nonnull String packageName,
            Criteria criteria)
    {
        super(elementContext, nameContext, name, packageName);
        this.criteria = criteria;
    }

    public Criteria getCriteria()
    {
        return this.criteria;
    }

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

    public AssociationEnd getSourceAssociationEnd()
    {
        return this.sourceAssociationEnd;
    }

    public AssociationEnd getTargetAssociationEnd()
    {
        return this.targetAssociationEnd;
    }

    public static class AssociationBuilder extends PackageableElementBuilder
    {
        @Nonnull
        private final CriteriaBuilder criteriaBuilder;

        private ImmutableList<AssociationEndBuilder> associationEndBuilders;
        private Association                          association;

        public AssociationBuilder(
                @Nonnull ParserRuleContext elementContext,
                @Nonnull ParserRuleContext nameContext,
                @Nonnull String name,
                @Nonnull String packageName,
                @Nonnull CriteriaBuilder criteriaBuilder)
        {
            super(elementContext, nameContext, name, packageName);
            this.criteriaBuilder = Objects.requireNonNull(criteriaBuilder);
        }

        public void setAssociationEndBuilders(@Nonnull ImmutableList<AssociationEndBuilder> associationEndBuilders)
        {
            this.associationEndBuilders = Objects.requireNonNull(associationEndBuilders);
        }

        public Association build()
        {
            if (this.association != null)
            {
                throw new IllegalStateException();
            }
            this.association = new Association(
                    this.elementContext,
                    this.nameContext,
                    this.name,
                    this.packageName,
                    this.criteriaBuilder.build());

            ImmutableList<AssociationEnd> associationEnds = this.associationEndBuilders
                    .collect(AssociationEndBuilder::build);

            this.association.setAssociationEnds(associationEnds);
            return this.association;
        }

        @Nonnull
        public Association getAssociation()
        {
            return Objects.requireNonNull(this.association);
        }
    }
}
