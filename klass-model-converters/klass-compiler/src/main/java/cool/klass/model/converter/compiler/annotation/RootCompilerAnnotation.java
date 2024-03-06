package cool.klass.model.converter.compiler.annotation;

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

public class RootCompilerAnnotation
        extends AbstractCompilerAnnotation
        implements Comparable<RootCompilerAnnotation>
{
    private static final Comparator<RootCompilerAnnotation> COMPILER_ANNOTATION_COMPARATOR = Comparator.comparingInt(
            (RootCompilerAnnotation each) -> each.getCompilationUnit().getOrdinal())
            .thenComparing(AbstractCompilerAnnotation::getLine)
            .thenComparing(AbstractCompilerAnnotation::getCharPositionInLine);

    @Nonnull
    private final String annotationCode;
    @Nonnull
    private final String message;

    public RootCompilerAnnotation(
            @Nonnull CompilationUnit compilationUnit,
            @Nonnull Optional<CauseCompilerAnnotation> macroCause,
            @Nonnull ImmutableList<ParserRuleContext> offendingContexts,
            @Nonnull ImmutableList<IAntlrElement> sourceContexts,
            @Nonnull String annotationCode,
            @Nonnull String message,
            @Nonnull AnnotationSeverity severity)
    {
        super(compilationUnit, macroCause, offendingContexts, sourceContexts, severity);
        this.annotationCode = Objects.requireNonNull(annotationCode);
        this.message        = Objects.requireNonNull(message);
    }

    @Override
    public int compareTo(@Nonnull RootCompilerAnnotation other)
    {
        return COMPILER_ANNOTATION_COMPARATOR.compare(this, other);
    }

    @Nonnull
    @Override
    public String toString()
    {
        String contextString   = this.getContextString();
        String locationMessage = this.getOptionalLocationMessage();
        String causeString     = this.getCauseString();
        String severityColor   = this.severity == AnnotationSeverity.ERROR ? "red" : "yellow";
        String severityName    = this.severity == AnnotationSeverity.ERROR ? "Error" : "Warning";

        String format = ""
                + "════════════════════════════════════════ @|magenta %s|@ ════════════════════════════════════════\n"
                + "@|%s %s: %s|@\n"
                + "\n"
                + "At %s\n"
                + "\n"
                + "%s%s%s\n"
                + "═════════════════════════════════════════════════════════════════════════════════════════════\n";

        String ansi = String.format(
                format,
                this.annotationCode,
                severityColor,
                severityName,
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
                this.annotationCode,
                this.message);
    }
}