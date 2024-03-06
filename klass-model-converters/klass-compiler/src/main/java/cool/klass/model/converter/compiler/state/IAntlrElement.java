package cool.klass.model.converter.compiler.state;

import java.util.Optional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import cool.klass.model.converter.compiler.CompilationUnit;
import org.antlr.v4.runtime.ParserRuleContext;
import org.eclipse.collections.api.list.ImmutableList;
import org.eclipse.collections.api.list.MutableList;
import org.eclipse.collections.impl.factory.Lists;

public interface IAntlrElement
{
    @Nonnull
    ParserRuleContext getElementContext();

    @Nonnull
    ImmutableList<ParserRuleContext> getParserRuleContexts();

    void getParserRuleContexts(@Nonnull MutableList<ParserRuleContext> parserRuleContexts);

    boolean omitParentFromSurroundingElements();

    Optional<IAntlrElement> getSurroundingElement();

    default ImmutableList<IAntlrElement> getSurroundingElements()
    {
        MutableList<IAntlrElement> result = Lists.mutable.empty();
        boolean omitParent = this.omitParentFromSurroundingElements();
        this.getSurroundingElement().ifPresent(element -> element.gatherSurroundingElements(result, omitParent));
        return result.toImmutable();
    }

    default void gatherSurroundingElements(@Nonnull MutableList<IAntlrElement> result, boolean omitThis)
    {
        if (!omitThis)
        {
            result.add(this);
        }
        this.getSurroundingElement().ifPresent(element -> element.gatherSurroundingElements(
                result,
                this.omitParentFromSurroundingElements()));
    }

    @Nullable
    CompilationUnit getCompilationUnit();

    String getSourceCode();
}
