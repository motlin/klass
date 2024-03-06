package cool.klass.model.converter.compiler.error;

import javax.annotation.Nonnull;

import cool.klass.model.converter.compiler.CompilationUnit;
import cool.klass.model.meta.grammar.KlassBaseListener;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.misc.Interval;
import org.eclipse.collections.api.list.MutableList;

public class BaseErrorListener extends KlassBaseListener
{
    protected final MutableList<String> contextualStrings;
    protected final CompilationUnit compilationUnit;

    protected BaseErrorListener(CompilationUnit compilationUnit, MutableList<String> contextualStrings)
    {
        this.compilationUnit = compilationUnit;
        this.contextualStrings = contextualStrings;
    }

    protected void addTextInclusive(@Nonnull Token start, @Nonnull Token stop)
    {
        String text = this.getTextInclusive(start, stop);
        this.contextualStrings.add(text);
    }

    protected String getTextInclusive(@Nonnull Token startToken, @Nonnull Token stopToken)
    {
        Interval interval = new Interval(startToken.getStartIndex(), stopToken.getStopIndex());
        return this.compilationUnit.getCharStream().getText(interval);
    }

    @Nonnull
    protected Interval getIntervalMinusOne(@Nonnull Token startToken, @Nonnull Token onePastStopToken)
    {
        int startIndex = startToken.getStartIndex();
        int endTokenIndex = onePastStopToken.getTokenIndex();
        Token endToken = this.compilationUnit.getTokenStream().get(endTokenIndex - 1);
        int stopIndex = endToken.getStopIndex();
        return new Interval(startIndex, stopIndex);
    }
}
