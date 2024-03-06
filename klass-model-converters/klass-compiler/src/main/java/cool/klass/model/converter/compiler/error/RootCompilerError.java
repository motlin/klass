package cool.klass.model.converter.compiler.error;

import java.util.Objects;
import java.util.Optional;

import javax.annotation.Nonnull;

import cool.klass.model.converter.compiler.CompilationUnit;
import org.antlr.v4.runtime.ParserRuleContext;
import org.eclipse.collections.api.list.ImmutableList;

import static org.fusesource.jansi.Ansi.ansi;

public class RootCompilerError extends AbstractCompilerError implements Comparable<RootCompilerError>
{
    @Nonnull
    private final String errorCode;
    @Nonnull
    private final String message;

    public RootCompilerError(
            @Nonnull CompilationUnit compilationUnit,
            @Nonnull Optional<CauseCompilerError> macroCause,
            @Nonnull ImmutableList<ParserRuleContext> offendingContexts,
            @Nonnull ImmutableList<ParserRuleContext> parserRuleContexts,
            @Nonnull String errorCode,
            @Nonnull String message)
    {
        super(compilationUnit, macroCause, offendingContexts, parserRuleContexts);
        this.errorCode = Objects.requireNonNull(errorCode);
        this.message = Objects.requireNonNull(message);
    }

    @Override
    @Nonnull
    protected String getFilenameWithoutDirectory()
    {
        String sourceName = this.compilationUnit.getSourceName();
        return sourceName.substring(sourceName.lastIndexOf('/') + 1);
    }

    @Override
    public int compareTo(@Nonnull RootCompilerError other)
    {
        int sourceNameCompareTo = this.compilationUnit.getSourceName().compareTo(other.compilationUnit.getSourceName());
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
                        + "@|red %s|@\n"
                        + "\n"
                        + "Error at location. %s\n"
                        + "%s%s%s",
                this.errorCode,
                this.message,
                this.getShortLocationString(),
                contextString,
                locationMessage,
                causeString);

        return ansi().render(ansi).toString();
    }
}
