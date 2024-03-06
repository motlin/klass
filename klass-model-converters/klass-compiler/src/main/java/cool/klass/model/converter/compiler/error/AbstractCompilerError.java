package cool.klass.model.converter.compiler.error;

import java.util.Objects;
import java.util.Optional;

import javax.annotation.Nonnull;

import cool.klass.model.converter.compiler.CompilationUnit;
import cool.klass.model.converter.compiler.SourceContext;
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
    private final   Optional<CauseCompilerError>     macroCause;
    // TODO: Change type of offendingContexts to also be SourceContexts
    @Nonnull
    private final   ImmutableList<ParserRuleContext> offendingContexts;
    @Nonnull
    private final   ImmutableList<SourceContext>     sourceContexts;

    protected AbstractCompilerError(
            @Nonnull CompilationUnit compilationUnit,
            @Nonnull Optional<CauseCompilerError> macroCause,
            @Nonnull ImmutableList<ParserRuleContext> offendingContexts,
            @Nonnull ImmutableList<SourceContext> sourceContexts)
    {
        this.macroCause        = Objects.requireNonNull(macroCause);
        this.compilationUnit   = Objects.requireNonNull(compilationUnit);
        this.offendingContexts = Objects.requireNonNull(offendingContexts);
        this.sourceContexts    = Objects.requireNonNull(sourceContexts);

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
    protected CompilationUnit getCompilationUnit()
    {
        return this.compilationUnit;
    }

    protected String getContextString()
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
    private String getFilenameWithoutDirectory()
    {
        String sourceName = this.compilationUnit.getSourceName();
        return this.macroCause
                .map(ignore -> sourceName)
                .orElseGet(() -> sourceName.substring(sourceName.lastIndexOf('/') + 1));
    }

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

    private ImmutableList<AbstractContextString> applyListenerToStack()
    {
        MutableList<AbstractContextString> contextualStrings = Lists.mutable.empty();

        ImmutableList<SourceContext> reversedContext = this.sourceContexts.toReversed();
        for (SourceContext sourceContext : reversedContext)
        {
            ParserRuleContext elementContext  = sourceContext.getElementContext();
            CompilationUnit   compilationUnit = sourceContext.getCompilationUnit();
            elementContext.enterRule(new ErrorContextListener(compilationUnit, contextualStrings));
        }

        KlassListener errorUnderlineListener = new ErrorUnderlineListener(
                this.compilationUnit,
                contextualStrings,
                this instanceof RootCompilerError);
        this.offendingContexts.forEachWith(ParserRuleContext::enterRule, errorUnderlineListener);

        for (SourceContext sourceContext : this.sourceContexts)
        {
            ParserRuleContext elementContext  = sourceContext.getElementContext();
            CompilationUnit   compilationUnit = sourceContext.getCompilationUnit();
            elementContext.exitRule(new ErrorContextListener(compilationUnit, contextualStrings));
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
    private String getLocationMessage()
    {
        return ansi().a("\n")
                .fg(CYAN).a("Location:  ").reset().a(this.getFilenameWithoutDirectory()).a(":").a(this.getLine()).reset().a("\n")
                .fg(CYAN).a("File:      ").reset().a(this.compilationUnit).reset().a("\n")
                .fg(CYAN).a("Line:      ").reset().a(this.getLine()).reset().a("\n")
                .fg(CYAN).a("Character: ").reset().a(this.getCharPositionInLine() + 1).reset().a("\n")
                .toString();
    }

    public AbstractCompilerError getUltimateCause()
    {
        return this.macroCause
                .map(AbstractCompilerError::getUltimateCause)
                .orElse(this);
    }

    @Nonnull
    public String getSourceName()
    {
        return this.compilationUnit.getSourceName();
    }

    public ImmutableList<AbstractCompilerError> getCauseChain()
    {
        return this.populateCauseChain(Lists.mutable.empty()).toImmutable();
    }

    protected final MutableList<AbstractCompilerError> populateCauseChain(MutableList<AbstractCompilerError> list)
    {
        list.add(this);
        this.macroCause.ifPresent(each -> each.populateCauseChain(list));
        return list;
    }
}
