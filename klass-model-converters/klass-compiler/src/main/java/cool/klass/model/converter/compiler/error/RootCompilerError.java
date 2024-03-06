package cool.klass.model.converter.compiler.error;

import java.util.Comparator;
import java.util.Objects;
import java.util.Optional;

import javax.annotation.Nonnull;

import com.google.common.collect.Ordering;
import cool.klass.model.converter.compiler.CompilationUnit;
import cool.klass.model.converter.compiler.SourceContext;
import org.antlr.v4.runtime.ParserRuleContext;
import org.eclipse.collections.api.list.ImmutableList;

import static org.fusesource.jansi.Ansi.ansi;

public class RootCompilerError extends AbstractCompilerError implements Comparable<RootCompilerError>
{
    private static final Comparator<AbstractCompilerError> FRAME_COMPARATOR = Comparator
            .comparing(AbstractCompilerError::getSourceName)
            .thenComparing(AbstractCompilerError::getLine)
            .thenComparing(AbstractCompilerError::getCharPositionInLine);

    public static final  Ordering<Iterable<AbstractCompilerError>> STACK_COMPARATOR =
            Ordering.from(FRAME_COMPARATOR).lexicographical();

    @Nonnull
    private final String errorCode;
    @Nonnull
    private final String message;

    public RootCompilerError(
            @Nonnull CompilationUnit compilationUnit,
            @Nonnull Optional<CauseCompilerError> macroCause,
            @Nonnull ImmutableList<ParserRuleContext> offendingContexts,
            @Nonnull ImmutableList<SourceContext> sourceContexts,
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
        ImmutableList<AbstractCompilerError> thisReverseCauseChain = this.getCauseChain();
        ImmutableList<AbstractCompilerError> otherReverseCauseChain = other.getCauseChain();

        ImmutableList<AbstractCompilerError> thisReverseCauseChainTrimmed =
                thisReverseCauseChain.size() == 1 ? thisReverseCauseChain : thisReverseCauseChain.drop(1).toReversed();
        ImmutableList<AbstractCompilerError> otherReverseCauseChainTrimmed =
                otherReverseCauseChain.size() == 1 ? otherReverseCauseChain : otherReverseCauseChain.drop(1).toReversed();

        return STACK_COMPARATOR.compare(thisReverseCauseChainTrimmed, otherReverseCauseChainTrimmed);
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
