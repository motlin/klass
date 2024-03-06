package cool.klass.model.meta.domain.criteria;

import java.util.Optional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import cool.klass.model.meta.domain.api.Element;
import cool.klass.model.meta.domain.api.criteria.OrCriteria;
import cool.klass.model.meta.domain.api.source.SourceCode;
import cool.klass.model.meta.domain.api.source.SourceCode.SourceCodeBuilder;
import org.antlr.v4.runtime.ParserRuleContext;

public final class OrCriteriaImpl
        extends AbstractBinaryCriteria
        implements OrCriteria
{
    private OrCriteriaImpl(
            @Nonnull ParserRuleContext elementContext,
            @Nonnull Optional<Element> macroElement,
            @Nullable SourceCode sourceCode,
            @Nonnull AbstractCriteria left,
            @Nonnull AbstractCriteria right)
    {
        super(elementContext, macroElement, sourceCode, left, right);
    }

    public static final class OrCriteriaBuilder
            extends AbstractBinaryCriteriaBuilder<OrCriteriaImpl>
    {
        public OrCriteriaBuilder(
                @Nonnull ParserRuleContext elementContext,
                @Nonnull Optional<ElementBuilder<?>> macroElement,
                @Nullable SourceCodeBuilder sourceCode,
                @Nonnull AbstractCriteriaBuilder<?> left,
                @Nonnull AbstractCriteriaBuilder<?> right)
        {
            super(elementContext, macroElement, sourceCode, left, right);
        }

        @Override
        @Nonnull
        protected OrCriteriaImpl buildUnsafe()
        {
            return new OrCriteriaImpl(
                    this.elementContext,
                    this.macroElement.map(ElementBuilder::getElement),
                    this.sourceCode.build(),
                    this.left.build(),
                    this.right.build());
        }
    }
}
