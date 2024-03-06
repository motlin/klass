package cool.klass.model.meta.domain;

import java.util.Objects;

import cool.klass.model.meta.domain.AssociationEnd.AssociationEndBuilder;
import cool.klass.model.meta.domain.criteria.Criteria;
import cool.klass.model.meta.domain.criteria.Criteria.CriteriaBuilder;
import org.antlr.v4.runtime.ParserRuleContext;
import org.eclipse.collections.api.list.ImmutableList;

public final class Association extends PackageableElement
{
    private final Criteria criteria;

    private ImmutableList<AssociationEnd> associationEnds;

    private Association(
            ParserRuleContext elementContext,
            ParserRuleContext nameContext,
            String name,
            String packageName,
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
        this.associationEnds = Objects.requireNonNull(associationEnds);
    }

    public static class AssociationBuilder extends PackageableElementBuilder
    {
        private final CriteriaBuilder criteriaBuilder;

        private ImmutableList<AssociationEndBuilder> associationEndBuilders;

        public AssociationBuilder(
                ParserRuleContext elementContext,
                ParserRuleContext nameContext,
                String name,
                String packageName,
                CriteriaBuilder criteriaBuilder)
        {
            super(elementContext, nameContext, name, packageName);
            this.criteriaBuilder = Objects.requireNonNull(criteriaBuilder);
        }

        public void setAssociationEndBuilders(ImmutableList<AssociationEndBuilder> associationEndBuilders)
        {
            this.associationEndBuilders = Objects.requireNonNull(associationEndBuilders);
        }

        public Association build()
        {
            Association association = new Association(
                    this.elementContext,
                    this.nameContext,
                    this.name,
                    this.packageName,
                    this.criteriaBuilder.build());

            ImmutableList<AssociationEnd> associationEnds = this.associationEndBuilders
                    .collect(AssociationEndBuilder::getAssociationEnd);

            association.setAssociationEnds(associationEnds);
            return association;
        }
    }
}
