package cool.klass.model.converter.compiler;

import java.io.InputStream;
import java.util.Objects;
import java.util.Scanner;
import java.util.regex.Pattern;

import javax.annotation.Nonnull;

import cool.klass.model.meta.grammar.KlassLexer;
import cool.klass.model.meta.grammar.KlassParser;
import cool.klass.model.meta.grammar.KlassParser.CompilationUnitContext;
import org.antlr.v4.runtime.ANTLRErrorListener;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CodePointCharStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.TokenStream;

public final class CompilationUnit
{
    private static final Pattern NEWLINE_PATTERN = Pattern.compile("\\r?\\n");

    @Nonnull
    private final String[]               lines;
    @Nonnull
    private final CharStream             charStream;
    @Nonnull
    private final TokenStream            tokenStream;
    @Nonnull
    private final CompilationUnitContext compilationUnitContext;

    private CompilationUnit(
            @Nonnull String[] lines,
            @Nonnull CharStream charStream,
            @Nonnull TokenStream tokenStream,
            @Nonnull CompilationUnitContext compilationUnitContext)
    {
        this.lines = Objects.requireNonNull(lines);
        this.charStream = Objects.requireNonNull(charStream);
        this.tokenStream = Objects.requireNonNull(tokenStream);
        this.compilationUnitContext = Objects.requireNonNull(compilationUnitContext);
    }

    public static CompilationUnit createFromClasspathLocation(String classpathLocation)
    {
        String sourceCodeText = CompilationUnit.slurp(classpathLocation);
        return CompilationUnit.createFromText(classpathLocation, sourceCodeText);
    }

    @Nonnull
    private static String slurp(String classpathLocation)
    {
        InputStream inputStream = KlassCompiler.class.getClassLoader().getResourceAsStream(classpathLocation);
        Objects.requireNonNull(inputStream);
        return CompilationUnit.slurp(inputStream);
    }

    public static CompilationUnit createFromText(String sourceName, @Nonnull String sourceCodeText)
    {
        String[]               lines                  = NEWLINE_PATTERN.split(sourceCodeText);
        ANTLRErrorListener     errorListener          = new ThrowingErrorListener(sourceName, lines);
        CodePointCharStream    charStream             = CharStreams.fromString(sourceCodeText, sourceName);
        CommonTokenStream      tokenStream            = CompilationUnit.getTokenStream(charStream, errorListener);
        KlassParser            parser                 = CompilationUnit.getParser(errorListener, tokenStream);
        CompilationUnitContext compilationUnitContext = parser.compilationUnit();
        return new CompilationUnit(
                lines,
                charStream,
                tokenStream,
                compilationUnitContext);
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
    public String[] getLines()
    {
        return this.lines;
    }

    @Nonnull
    public CharStream getCharStream()
    {
        return this.charStream;
    }

    @Nonnull
    public TokenStream getTokenStream()
    {
        return this.tokenStream;
    }

    @Nonnull
    public CompilationUnitContext getCompilationUnitContext()
    {
        return this.compilationUnitContext;
    }

    @Override
    public String toString()
    {
        return this.charStream.getSourceName();
    }
}
