package cool.klass.model.converter.compiler;

import java.io.InputStream;
import java.util.Objects;
import java.util.Scanner;
import java.util.regex.Pattern;

import javax.annotation.Nonnull;

import cool.klass.model.meta.grammar.KlassLexer;
import cool.klass.model.meta.grammar.KlassParser;
import org.antlr.v4.runtime.ANTLRErrorListener;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CodePointCharStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.TokenStream;
import org.eclipse.collections.api.block.function.Function;

public final class CompilationUnit
{
    private static final Pattern NEWLINE_PATTERN = Pattern.compile("\\r?\\n");

    @Nonnull
    private final String[]          lines;
    @Nonnull
    private final CharStream        charStream;
    @Nonnull
    private final TokenStream       tokenStream;
    @Nonnull
    private final ParserRuleContext parserContext;

    private CompilationUnit(
            @Nonnull String[] lines,
            @Nonnull CharStream charStream,
            @Nonnull TokenStream tokenStream,
            @Nonnull ParserRuleContext parserRuleContext)
    {
        this.lines = Objects.requireNonNull(lines);
        this.charStream = Objects.requireNonNull(charStream);
        this.tokenStream = Objects.requireNonNull(tokenStream);
        this.parserContext = Objects.requireNonNull(parserRuleContext);
    }

    public static CompilationUnit createFromClasspathLocation(@Nonnull String classpathLocation)
    {
        String sourceCodeText = CompilationUnit.slurp(classpathLocation);
        return CompilationUnit.createFromText(classpathLocation, sourceCodeText);
    }

    @Nonnull
    private static String slurp(@Nonnull String classpathLocation)
    {
        InputStream inputStream = KlassCompiler.class.getClassLoader().getResourceAsStream(classpathLocation);
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
        CommonTokenStream   tokenStream       = CompilationUnit.getTokenStream(charStream, errorListener);
        KlassParser         parser            = CompilationUnit.getParser(errorListener, tokenStream);
        ParserRuleContext   parserRuleContext = parserRule.apply(parser);
        return new CompilationUnit(
                lines,
                charStream,
                tokenStream,
                parserRuleContext);
    }

    @Nonnull
    private static String slurp(@Nonnull InputStream inputStream)
    {
        try (Scanner scanner = new Scanner(inputStream).useDelimiter("\\A"))
        {
            return scanner.hasNext() ? scanner.next() : "";
        }
    }

    private static CommonTokenStream getTokenStream(
            CodePointCharStream charStream,
            @Nonnull ANTLRErrorListener errorListener)
    {
        KlassLexer lexer = new KlassLexer(charStream);
        lexer.addErrorListener(errorListener);
        return new CommonTokenStream(lexer);
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
            @Nonnull ParserRuleContext parserContext,
            @Nonnull Class<?> macroContextClass,
            @Nonnull String sourceCodeText,
            @Nonnull Function<KlassParser, ? extends ParserRuleContext> parserRule)
    {
        String sourceName = getMacroSourceName(parserContext, macroContextClass);
        return createFromText(sourceName, sourceCodeText, parserRule);
    }

    private static String getMacroSourceName(
            @Nonnull ParserRuleContext parserContext,
            @Nonnull Class<?> macroContextClass)
    {
        Token  contextToken   = parserContext.getStart();
        String contextMessage = getContextMessage(contextToken);
        return String.format(
                "%s compiler macro (%s)",
                macroContextClass.getSimpleName(),
                contextMessage);
    }

    public static String getContextMessage(@Nonnull Token contextToken)
    {
        String sourceName         = contextToken.getInputStream().getSourceName();
        int    line               = contextToken.getLine();
        int    charPositionInLine = contextToken.getCharPositionInLine();

        return String.format(
                "File: %s Line: %d Char: %d",
                sourceName,
                line,
                charPositionInLine + 1);
    }

    @Nonnull
    public ParserRuleContext getParserContext()
    {
        return this.parserContext;
    }

    @Override
    public String toString()
    {
        return this.charStream.getSourceName();
    }
}
