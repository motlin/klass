package cool.klass.model.meta.domain.criteria;

import java.util.Optional;

import javax.annotation.Nonnull;

import cool.klass.model.meta.domain.api.Element;
import cool.klass.model.meta.domain.api.criteria.AllCriteria;
import cool.klass.model.meta.domain.api.source.SourceCode;
import cool.klass.model.meta.domain.api.source.SourceCode.SourceCodeBuilder;
import org.antlr.v4.runtime.ParserRuleContext;

public final class AllCriteriaImpl
        extends AbstractCriteria
        implements AllCriteria
{
    // TODO: Make a distinction between macroElement and declaration
    public static final AllCriteriaImpl INSTANCE = new AllCriteriaImpl(
            new ParserRuleContext(),
            Optional.empty(),
            Optional.empty());

    private AllCriteriaImpl(
            @Nonnull ParserRuleContext elementContext,
            @Nonnull Optional<Element> macroElement,
            @Nonnull Optional<SourceCode> sourceCode)
    {
        super(elementContext, macroElement, sourceCode);
    }

    @Nonnull
    @Override
    public String getSourceCode()
    {
        return "all";
    }

    public static final class AllCriteriaBuilder
            extends AbstractCriteriaBuilder<AllCriteriaImpl>
    {
        public AllCriteriaBuilder(
                @Nonnull ParserRuleContext elementContext,
                @Nonnull Optional<ElementBuilder<?>> macroElement,
                @Nonnull Optional<SourceCodeBuilder> sourceCode)
        {
            super(elementContext, macroElement, sourceCode);
        }

        @Nonnull
        @Override
        protected AllCriteriaImpl buildUnsafe()
        {
            return INSTANCE;
        }
    }
}
