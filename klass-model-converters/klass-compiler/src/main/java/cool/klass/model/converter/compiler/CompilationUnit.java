package cool.klass.model.converter.compiler;

import java.io.InputStream;
import java.util.Objects;
import java.util.Scanner;
import java.util.regex.Pattern;

import javax.annotation.Nonnull;

import cool.klass.model.converter.compiler.phase.AbstractCompilerPhase;
import cool.klass.model.meta.grammar.KlassLexer;
import cool.klass.model.meta.grammar.KlassParser;
import org.antlr.v4.runtime.ANTLRErrorListener;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CodePointCharStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.TokenStream;
import org.eclipse.collections.api.block.function.Function;

public final class CompilationUnit
{
    private static final Pattern NEWLINE_PATTERN = Pattern.compile("\\r?\\n");

    @Nonnull
    private final String            sourceName;
    @Nonnull
    private final String[]          lines;
    @Nonnull
    private final CharStream        charStream;
    @Nonnull
    private final TokenStream       tokenStream;
    @Nonnull
    private final ParserRuleContext parserContext;

    private CompilationUnit(
            @Nonnull String sourceName,
            @Nonnull String[] lines,
            @Nonnull CharStream charStream,
            @Nonnull TokenStream tokenStream,
            @Nonnull ParserRuleContext parserRuleContext)
    {
        this.sourceName = Objects.requireNonNull(sourceName);
        this.lines = Objects.requireNonNull(lines);
        this.charStream = Objects.requireNonNull(charStream);
        this.tokenStream = Objects.requireNonNull(tokenStream);
        this.parserContext = Objects.requireNonNull(parserRuleContext);
    }

    @Nonnull
    public static CompilationUnit createFromClasspathLocation(String classpathLocation, ClassLoader classLoader)
    {
        String sourceCodeText = slurp(classpathLocation, classLoader);
        return CompilationUnit.createFromText(classpathLocation, sourceCodeText);
    }

    public static CompilationUnit createFromClasspathLocation(@Nonnull String classpathLocation)
    {
        return createFromClasspathLocation(classpathLocation, CompilationUnit.class.getClassLoader());
    }

    @Nonnull
    public static String slurp(String classpathLocation, ClassLoader classLoader)
    {
        InputStream inputStream = classLoader.getResourceAsStream(classpathLocation);
        Objects.requireNonNull(inputStream);
        return CompilationUnit.slurp(inputStream);
    }

    public static CompilationUnit createFromText(
            @Nonnull String sourceName,
            @Nonnull String sourceCodeText)
    {
        return createFromText(sourceName, sourceCodeText, KlassParser::compilationUnit);
    }

    public static CompilationUnit createFromText(
            String sourceName,
            @Nonnull String sourceCodeText,
            Function<KlassParser, ? extends ParserRuleContext> parserRule)
    {
        String[]            lines             = NEWLINE_PATTERN.split(sourceCodeText);
        ANTLRErrorListener  errorListener     = new ThrowingErrorListener(sourceName, lines);
        CodePointCharStream charStream        = CharStreams.fromString(sourceCodeText, sourceName);
        KlassLexer          lexer             = getKlassLexer(errorListener, charStream);
        CommonTokenStream   tokenStream       = new CommonTokenStream(lexer);
        KlassParser         parser            = CompilationUnit.getParser(errorListener, tokenStream);
        ParserRuleContext   parserRuleContext = parserRule.apply(parser);
        return new CompilationUnit(
                sourceName,
                lines,
                charStream,
                tokenStream,
                parserRuleContext);
    }

    @Nonnull
    private static KlassLexer getKlassLexer(@Nonnull ANTLRErrorListener errorListener, CodePointCharStream charStream)
    {
        KlassLexer lexer = new KlassLexer(charStream);
        lexer.addErrorListener(errorListener);
        return lexer;
    }

    @Nonnull
    private static String slurp(@Nonnull InputStream inputStream)
    {
        try (Scanner scanner = new Scanner(inputStream).useDelimiter("\\A"))
        {
            return scanner.hasNext() ? scanner.next() : "";
        }
    }

    @Nonnull
    private static KlassParser getParser(@Nonnull ANTLRErrorListener errorListener, CommonTokenStream tokenStream)
    {
        KlassParser parser = new KlassParser(tokenStream);
        parser.removeErrorListeners();
        parser.addErrorListener(errorListener);
        return parser;
    }

    @Nonnull
    public static CompilationUnit getMacroCompilationUnit(
            @Nonnull AbstractCompilerPhase macroExpansionCompilerPhase,
            @Nonnull String sourceCodeText,
            @Nonnull Function<KlassParser, ? extends ParserRuleContext> parserRule)
    {
        String sourceName = macroExpansionCompilerPhase.getName() + " macro";
        return createFromText(sourceName, sourceCodeText, parserRule);
    }

    @Nonnull
    public ParserRuleContext getParserContext()
    {
        return this.parserContext;
    }

    @Nonnull
    public String getSourceName()
    {
        return this.sourceName;
    }

    @Override
    public String toString()
    {
        // TODO: Remove charStream field if this assertion never fails
        if (!this.sourceName.equals(this.charStream.getSourceName()))
        {
            throw new AssertionError(this.sourceName + ", " + this.charStream.getSourceName());
        }
        return this.charStream.getSourceName();
    }

    @Nonnull
    public TokenStream getTokenStream()
    {
        return this.tokenStream;
    }

    public String getLine(int index)
    {
        return this.lines[index];
    }
}
