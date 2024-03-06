package cool.klass.model.converter.compiler.state.service;

import javax.annotation.Nonnull;

import cool.klass.model.converter.compiler.state.criteria.AntlrCriteria;
import org.antlr.v4.runtime.ParserRuleContext;
import org.eclipse.collections.api.list.ImmutableList;
import org.eclipse.collections.api.list.MutableList;
import org.eclipse.collections.impl.factory.Lists;

public interface CriteriaOwner
{
    @Nonnull
    AntlrCriteria getCriteria();

    void setCriteria(@Nonnull AntlrCriteria criteria);

    void getParserRuleContexts(MutableList<ParserRuleContext> parserRuleContexts);

    default ImmutableList<ParserRuleContext> getParserRuleContexts()
    {
        MutableList<ParserRuleContext> result = Lists.mutable.empty();
        this.getParserRuleContexts(result);
        return result.toImmutable();
    }
}
