package cool.klass.model.converter.compiler.error;

import java.util.Objects;

import javax.annotation.Nonnull;

import cool.klass.model.meta.grammar.KlassListener;
import cool.klass.model.meta.grammar.KlassParser.EqualityOperatorContext;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.Token;
import org.eclipse.collections.api.list.ImmutableList;
import org.eclipse.collections.api.list.MutableList;
import org.eclipse.collections.impl.factory.Lists;

public class CompilerError implements Comparable<CompilerError>
{
    @Nonnull
    private final String                           message;
    @Nonnull
    private final ParserRuleContext                offendingParserRuleContext;
    // TODO: Change the list of contexts into a list of AntlrElements
    private final ImmutableList<ParserRuleContext> parserRuleContexts;
    private final Token                            offendingToken;

    public CompilerError(
            @Nonnull String message,
            @Nonnull ParserRuleContext offendingParserRuleContext,
            ParserRuleContext... parserRuleContexts)
    {
        this.message = Objects.requireNonNull(message);
        this.offendingParserRuleContext = Objects.requireNonNull(offendingParserRuleContext);
        if (offendingParserRuleContext instanceof EqualityOperatorContext)
        {
            throw new AssertionError();
        }
        this.parserRuleContexts = Lists.immutable.with(parserRuleContexts);
        this.offendingToken = this.offendingParserRuleContext.getStart();
    }

    @Nonnull
    @Override
    public String toString()
    {
        String                shortErrorMessage = this.getShortErrorMessage();
        ImmutableList<String> contextualStrings = this.applyListenerToStack();
        String                context           = contextualStrings.makeString("", "\n", "\n");
        return shortErrorMessage + '\n' + context;
    }

    private String getShortErrorMessage()
    {
        String sourceName         = this.getSourceName();
        int    line               = this.getLine();
        int    charPositionInLine = this.getCharPositionInLine();
        return String.format(
                "File: %s Line: %d Char: %d Error: %s",
                sourceName,
                line,
                charPositionInLine + 1,
                this.message);
    }

    private ImmutableList<String> applyListenerToStack()
    {
        MutableList<String> contextualStrings = Lists.mutable.empty();

        KlassListener errorContextListener   = new ErrorContextListener(contextualStrings);
        KlassListener errorUnderlineListener = new ErrorUnderlineListener(contextualStrings);

        ImmutableList<ParserRuleContext> reversedContext = this.parserRuleContexts.toReversed();
        for (ParserRuleContext ruleContext : reversedContext)
        {
            ruleContext.enterRule(errorContextListener);
        }

        this.offendingParserRuleContext.enterRule(errorUnderlineListener);

        for (ParserRuleContext ruleContext : this.parserRuleContexts)
        {
            ruleContext.exitRule(errorContextListener);
        }
        return contextualStrings.toImmutable();
    }

    private String getSourceName()
    {
        return this.offendingToken.getInputStream().getSourceName();
    }

    private int getLine()
    {
        return this.offendingToken.getLine();
    }

    private int getCharPositionInLine()
    {
        return this.offendingToken.getCharPositionInLine();
    }

    @Override
    public int compareTo(@Nonnull CompilerError other)
    {
        int sourceNameCompareTo = this.getSourceName().compareTo(other.getSourceName());
        if (sourceNameCompareTo != 0)
        {
            return sourceNameCompareTo;
        }

        int lineCompareTo = Integer.compare(this.getLine(), other.getLine());
        if (lineCompareTo != 0)
        {
            return lineCompareTo;
        }

        return Integer.compare(this.getCharPositionInLine(), other.getCharPositionInLine());
    }
}
