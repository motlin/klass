package cool.klass.model.converter.compiler.error;

import java.util.Comparator;
import java.util.Objects;
import java.util.Optional;

import javax.annotation.Nonnull;

import cool.klass.model.converter.compiler.CompilationUnit;
import cool.klass.model.converter.compiler.state.IAntlrElement;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.Token;
import org.eclipse.collections.api.list.ImmutableList;
import org.eclipse.collections.api.tuple.Pair;

import static org.fusesource.jansi.Ansi.ansi;

public class RootCompilerError
        extends AbstractCompilerError
        implements Comparable<RootCompilerError>
{
    private static final Comparator<RootCompilerError> COMPILER_ERROR_COMPARATOR = Comparator.comparingInt(
            (RootCompilerError each) -> each.getCompilationUnit().getOrdinal())
            .thenComparing(AbstractCompilerError::getLine)
            .thenComparing(AbstractCompilerError::getCharPositionInLine);

    @Nonnull
    private final String errorCode;
    @Nonnull
    private final String message;

    public RootCompilerError(
            @Nonnull CompilationUnit compilationUnit,
            @Nonnull Optional<CauseCompilerError> macroCause,
            @Nonnull ImmutableList<ParserRuleContext> offendingContexts,
            @Nonnull ImmutableList<IAntlrElement> sourceContexts,
            @Nonnull String errorCode,
            @Nonnull String message)
    {
        super(compilationUnit, macroCause, offendingContexts, sourceContexts);
        this.errorCode = Objects.requireNonNull(errorCode);
        this.message   = Objects.requireNonNull(message);
    }

    @Override
    public int compareTo(@Nonnull RootCompilerError other)
    {
        return COMPILER_ERROR_COMPARATOR.compare(this, other);
    }

    @Nonnull
    @Override
    public String toString()
    {
        String contextString   = this.getContextString();
        String locationMessage = this.getOptionalLocationMessage();
        String causeString     = this.getCauseString();

        String ansi = String.format(
                ""
                        + "@|yellow ════════════════════════════════════════|@ @|magenta %s|@ @|yellow ════════════════════════════════════════|@\n"
                        + "@|red Error: %s|@\n"
                        + "\n"
                        + "At %s\n"
                        + "\n"
                        + "%s%s%s\n"
                        + "@|yellow ═════════════════════════════════════════════════════════════════════════════════════════════|@\n",
                this.errorCode,
                this.message,
                this.getShortLocationString(),
                contextString,
                locationMessage,
                causeString);

        return ansi().render(ansi).toString();
    }

    @Override
    public String toGitHubAnnotation()
    {
        String contextString   = this.getContextString();
        String locationMessage = this.getOptionalLocationMessage();
        String causeString     = this.getCauseString();

        ImmutableList<AbstractContextString> contextStrings = this.applyListenerToStack();

        Pair<Token, Token> firstAndLastToken = this.getFirstAndLastToken();
        Token startToken = firstAndLastToken.getOne();
        Token endToken   = firstAndLastToken.getTwo();

        String sourceName = this.compilationUnit.getSourceName();
        return "::error file=%s,line=%d,endLine=%d,col=%d,endColumn=%d,title=%s::%s".formatted(
                sourceName,
                startToken.getLine(),
                endToken.getLine(),
                startToken.getCharPositionInLine(),
                endToken.getCharPositionInLine(),
                this.errorCode,
                this.message);
    }
}
