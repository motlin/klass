package cool.klass.model.converter.compiler.error;

import java.util.Objects;
import java.util.Optional;

import javax.annotation.Nonnull;

import cool.klass.model.converter.compiler.CompilationUnit;
import cool.klass.model.meta.grammar.KlassListener;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.Token;
import org.eclipse.collections.api.list.ImmutableList;
import org.eclipse.collections.api.list.MutableList;
import org.eclipse.collections.impl.factory.Lists;

import static org.fusesource.jansi.Ansi.Color.BLACK;
import static org.fusesource.jansi.Ansi.Color.CYAN;
import static org.fusesource.jansi.Ansi.ansi;

public abstract class AbstractCompilerError
{
    @Nonnull
    protected final CompilationUnit                  compilationUnit;
    @Nonnull
    protected final Optional<CauseCompilerError>     macroCause;
    @Nonnull
    protected final ImmutableList<ParserRuleContext> offendingContexts;
    @Nonnull
    protected final ImmutableList<ParserRuleContext> parserRuleContexts;

    public AbstractCompilerError(
            @Nonnull CompilationUnit compilationUnit, @Nonnull Optional<CauseCompilerError> macroCause,
            @Nonnull ImmutableList<ParserRuleContext> offendingContexts,
            @Nonnull ImmutableList<ParserRuleContext> parserRuleContexts)
    {
        this.macroCause = Objects.requireNonNull(macroCause);
        this.compilationUnit = Objects.requireNonNull(compilationUnit);
        this.offendingContexts = Objects.requireNonNull(offendingContexts);
        this.parserRuleContexts = Objects.requireNonNull(parserRuleContexts);

        if (offendingContexts.isEmpty())
        {
            throw new AssertionError();
        }
        if (!offendingContexts.noneSatisfy(offendingContext -> offendingContext.getStart() == null))
        {
            throw new AssertionError();
        }
    }

    protected AbstractCompilerError getUltimateCause()
    {
        return this.macroCause
                .map(AbstractCompilerError::getUltimateCause)
                .orElse(this);
    }

    public String getContextString()
    {
        ImmutableList<AbstractContextString> contextStrings = this.applyListenerToStack();

        int    maxLine         = contextStrings.getLast().getLine();
        String maxLineString   = String.valueOf(maxLine);
        int    lineNumberWidth = maxLineString.length();

        String entireContext = contextStrings
                .collect(contextString -> contextString.toString(lineNumberWidth))
                .makeString("", "\n", "\n");
        return ansi().bg(BLACK).a(entireContext).reset().toString();
    }

    @Nonnull
    protected abstract String getFilenameWithoutDirectory();

    protected String getShortLocationString()
    {
        return this.macroCause
                .map(ignore -> this.getLocationWithoutLine())
                .orElseGet(this::getLocationWithLine);
    }

    private String getLocationWithoutLine()
    {
        return String.format("(%s)", this.compilationUnit);
    }

    private String getLocationWithLine()
    {
        return String.format(
                "(%s:%d)",
                // TODO: This should be part of the source information
                this.getFilenameWithoutDirectory(),
                this.getLine());
    }

    protected ImmutableList<AbstractContextString> applyListenerToStack()
    {
        MutableList<AbstractContextString> contextualStrings = Lists.mutable.empty();

        KlassListener errorContextListener   = new ErrorContextListener(this.compilationUnit, contextualStrings);
        KlassListener errorUnderlineListener = new ErrorUnderlineListener(this.compilationUnit, contextualStrings);

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

    protected int getLine()
    {
        return this.getOffendingToken().getLine();
    }

    protected int getCharPositionInLine()
    {
        return this.getOffendingToken().getCharPositionInLine();
    }

    private Token getOffendingToken()
    {
        return this.offendingContexts.getFirst().getStart();
    }

    @Nonnull
    protected String getCauseString()
    {
        return this.macroCause
                .map(CauseCompilerError::toString)
                .orElse("");
    }

    @Nonnull
    protected String getOptionalLocationMessage()
    {
        return this.macroCause
                .map(ignored -> "")
                .orElseGet(this::getLocationMessage);
    }

    @Nonnull
    protected String getLocationMessage()
    {
        return ansi().a("\n")
                .fg(CYAN).a("Location:  ").reset().a(this.getFilenameWithoutDirectory()).a(":").a(this.getLine()).reset().a(
                        "\n")
                .fg(CYAN).a("File:      ").reset().a(this.compilationUnit).reset().a("\n")
                .fg(CYAN).a("Line:      ").reset().a(this.getLine()).reset().a("\n")
                .fg(CYAN).a("Character: ").reset().a(this.getCharPositionInLine() + 1).reset().a("\n")
                .toString();
    }
}
