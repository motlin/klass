package cool.klass.model.meta.domain.criteria;

import java.util.Optional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import cool.klass.model.meta.domain.api.Element;
import cool.klass.model.meta.domain.api.criteria.AllCriteria;
import cool.klass.model.meta.domain.api.source.SourceCode;
import cool.klass.model.meta.domain.api.source.SourceCode.SourceCodeBuilder;
import cool.klass.model.meta.grammar.KlassParser.CriteriaAllContext;

public final class AllCriteriaImpl
        extends AbstractCriteria
        implements AllCriteria
{
    public AllCriteriaImpl(
            @Nonnull CriteriaAllContext elementContext,
            @Nonnull Optional<Element> macroElement,
            @Nullable SourceCode sourceCode)
    {
        super(elementContext, macroElement, sourceCode);
    }

    @Nonnull
    @Override
    public CriteriaAllContext getElementContext()
    {
        return (CriteriaAllContext) super.getElementContext();
    }

    public static final class AllCriteriaBuilder
            extends AbstractCriteriaBuilder<AllCriteriaImpl>
    {
        public AllCriteriaBuilder(
                @Nonnull CriteriaAllContext elementContext,
                @Nonnull Optional<ElementBuilder<?>> macroElement,
                @Nullable SourceCodeBuilder sourceCode)
        {
            super(elementContext, macroElement, sourceCode);
        }

        @Nonnull
        @Override
        protected AllCriteriaImpl buildUnsafe()
        {
            return new AllCriteriaImpl(
                    (CriteriaAllContext) this.elementContext,
                    this.macroElement.map(ElementBuilder::getElement),
                    this.sourceCode.build());
        }
    }
}
