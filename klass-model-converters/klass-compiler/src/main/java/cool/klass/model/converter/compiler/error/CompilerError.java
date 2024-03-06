package cool.klass.model.converter.compiler.error;

import java.util.Objects;

import javax.annotation.Nonnull;

import cool.klass.model.meta.grammar.KlassListener;
import org.antlr.v4.runtime.CharStream;
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
    private final ImmutableList<ParserRuleContext> offendingContexts;
    @Nonnull
    private final ImmutableList<ParserRuleContext> parserRuleContexts;

    public CompilerError(
            @Nonnull String message,
            @Nonnull ImmutableList<ParserRuleContext> offendingContexts,
            @Nonnull ImmutableList<ParserRuleContext> parserRuleContexts)
    {
        this.message = Objects.requireNonNull(message);
        this.offendingContexts = offendingContexts;
        this.parserRuleContexts = parserRuleContexts;
        if (offendingContexts.isEmpty())
        {
            throw new AssertionError();
        }
        if (!offendingContexts.noneSatisfy(offendingContext -> offendingContext.getStart() == null))
        {
            throw new AssertionError();
        }
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
        // TODO: This should be part of the source information
        String fileNameWithoutDirectory = sourceName.substring(sourceName.lastIndexOf('/') + 1);

        return String.format(
                "Error: %s (%s:%d) %s:%d:%d",
                this.message,
                fileNameWithoutDirectory,
                line,
                sourceName,
                line,
                charPositionInLine + 1);
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

        this.offendingContexts.forEachWith(ParserRuleContext::enterRule, errorUnderlineListener);

        for (ParserRuleContext ruleContext : this.parserRuleContexts)
        {
            ruleContext.exitRule(errorContextListener);
        }
        return contextualStrings.toImmutable();
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

    private String getSourceName()
    {
        Token offendingToken = this.getOffendingToken();
        if (offendingToken == null)
        {
            throw new AssertionError();
        }
        CharStream inputStream = offendingToken.getInputStream();
        return inputStream.getSourceName();
    }

    private int getLine()
    {
        return this.getOffendingToken().getLine();
    }

    private int getCharPositionInLine()
    {
        return this.getOffendingToken().getCharPositionInLine();
    }

    private Token getOffendingToken()
    {
        return this.offendingContexts.getFirst().getStart();
    }
}
