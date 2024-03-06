package cool.klass.model.converter.compiler.state.projection;

import javax.annotation.Nonnull;

import cool.klass.model.converter.compiler.error.CompilerErrorHolder;
import cool.klass.model.meta.domain.projection.ProjectionMember.ProjectionMemberBuilder;
import org.antlr.v4.runtime.ParserRuleContext;
import org.eclipse.collections.api.list.MutableList;
import org.eclipse.collections.impl.factory.Lists;

public interface AntlrProjectionMember
{
    ProjectionMemberBuilder build();

    default MutableList<ParserRuleContext> getParserRuleContexts()
    {
        MutableList<ParserRuleContext> result = Lists.mutable.empty();
        this.getParent().getParserRuleContexts(result);
        return result;
    }

    @Nonnull
    AntlrProjectionParent getParent();

    @Nonnull
    String getName();

    void reportDuplicateMemberName(CompilerErrorHolder compilerErrorHolder);

    void reportErrors(CompilerErrorHolder compilerErrorHolder);
}
