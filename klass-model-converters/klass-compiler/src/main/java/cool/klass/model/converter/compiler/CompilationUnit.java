package cool.klass.model.converter.compiler;

import java.io.InputStream;
import java.util.Objects;
import java.util.Optional;
import java.util.Scanner;
import java.util.regex.Pattern;

import javax.annotation.Nonnull;

import cool.klass.model.converter.compiler.phase.AbstractCompilerPhase;
import cool.klass.model.converter.compiler.state.AntlrElement;
import cool.klass.model.converter.compiler.token.categories.TokenCategory;
import cool.klass.model.converter.compiler.token.categorizing.lexer.LexerBasedTokenCategorizer;
import cool.klass.model.converter.compiler.token.categorizing.parser.ParserBasedTokenCategorizer;
import cool.klass.model.meta.domain.SourceCodeImpl.SourceCodeBuilderImpl;
import cool.klass.model.meta.domain.api.source.SourceCode.SourceCodeBuilder;
import cool.klass.model.meta.grammar.KlassLexer;
import cool.klass.model.meta.grammar.KlassParser;
import org.antlr.v4.runtime.ANTLRErrorListener;
import org.antlr.v4.runtime.BufferedTokenStream;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CodePointCharStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.Token;
import org.eclipse.collections.api.block.function.Function;
import org.eclipse.collections.api.map.MapIterable;

public final class CompilationUnit
{
    private static final Pattern NEWLINE_PATTERN = Pattern.compile("\\r?\\n");

    private final int                               ordinal;
    @Nonnull
    private final Optional<AntlrElement>            macroElement;
    @Nonnull
    private final String                            sourceName;
    @Nonnull
    private final String                            sourceCodeText;
    @Nonnull
    private final String[]                          lines;
    @Nonnull
    private final CharStream                        charStream;
    @Nonnull
    private final BufferedTokenStream               tokenStream;
    @Nonnull
    private final ParserRuleContext                 parserContext;
    @Nonnull
    private final MapIterable<Token, TokenCategory> tokenCategoriesFromLexer;
    @Nonnull
    private final MapIterable<Token, TokenCategory> tokenCategoriesFromParser;

    private SourceCodeBuilder sourceCodeBuilder;

    private CompilationUnit(
            int ordinal,
            @Nonnull Optional<AntlrElement> macroElement,
            @Nonnull String sourceName,
            @Nonnull String sourceCodeText,
            @Nonnull String[] lines,
            @Nonnull CharStream charStream,
            @Nonnull BufferedTokenStream tokenStream,
            @Nonnull ParserRuleContext parserRuleContext)
    {
        this.ordinal        = ordinal;
        this.macroElement   = Objects.requireNonNull(macroElement);
        this.sourceName     = Objects.requireNonNull(sourceName);
        this.sourceCodeText = Objects.requireNonNull(sourceCodeText);
        this.lines          = Objects.requireNonNull(lines);
        this.charStream     = Objects.requireNonNull(charStream);
        this.tokenStream    = Objects.requireNonNull(tokenStream);
        this.parserContext  = Objects.requireNonNull(parserRuleContext);

        if (macroElement.isPresent() && !sourceName.contains("macro"))
        {
            throw new AssertionError(sourceName);
        }

        this.tokenCategoriesFromLexer  = LexerBasedTokenCategorizer.findTokenCategoriesFromLexer(tokenStream);
        this.tokenCategoriesFromParser = ParserBasedTokenCategorizer.findTokenCategoriesFromParser(this.parserContext);
    }

    public int getOrdinal()
    {
        return this.ordinal;
    }

    @Nonnull
    public Optional<AntlrElement> getMacroElement()
    {
        return this.macroElement;
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

    @Nonnull
    public BufferedTokenStream getTokenStream()
    {
        return this.tokenStream;
    }

    public String getLine(int index)
    {
        return this.lines[index];
    }

    @Nonnull
    public static CompilationUnit createFromClasspathLocation(
            int ordinal,
            @Nonnull String classpathLocation,
            @Nonnull ClassLoader classLoader)
    {
        String sourceCodeText = CompilationUnit.slurp(classpathLocation, classLoader);
        return CompilationUnit.createFromText(ordinal, Optional.empty(), classpathLocation, sourceCodeText);
    }

    @Nonnull
    public static CompilationUnit createFromClasspathLocation(int ordinal, @Nonnull String classpathLocation)
    {
        return CompilationUnit.createFromClasspathLocation(
                ordinal,
                classpathLocation,
                CompilationUnit.class.getClassLoader());
    }

    @Nonnull
    private static String slurp(@Nonnull String classpathLocation, @Nonnull ClassLoader classLoader)
    {
        InputStream inputStream = classLoader.getResourceAsStream(classpathLocation);
        Objects.requireNonNull(inputStream);
        return CompilationUnit.slurp(inputStream);
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
    public static CompilationUnit createFromText(
            int ordinal,
            @Nonnull Optional<AntlrElement> macroElement,
            @Nonnull String sourceName,
            @Nonnull String sourceCodeText)
    {
        return CompilationUnit.createFromText(
                ordinal,
                macroElement,
                sourceName,
                sourceCodeText,
                KlassParser::compilationUnit);
    }

    @Nonnull
    private static CompilationUnit createFromText(
            int ordinal,
            @Nonnull Optional<AntlrElement> macroElement,
            @Nonnull String sourceName,
            @Nonnull String sourceCodeText,
            @Nonnull Function<KlassParser, ? extends ParserRuleContext> parserRule)
    {
        String[]            lines             = NEWLINE_PATTERN.split(sourceCodeText);
        ANTLRErrorListener  errorListener     = new ThrowingErrorListener(sourceName, lines);
        CodePointCharStream charStream        = CharStreams.fromString(sourceCodeText, sourceName);
        KlassLexer          lexer             = CompilationUnit.getKlassLexer(errorListener, charStream);
        CommonTokenStream   tokenStream       = new CommonTokenStream(lexer);
        KlassParser         parser            = CompilationUnit.getParser(errorListener, tokenStream);
        ParserRuleContext   parserRuleContext = parserRule.apply(parser);
        return new CompilationUnit(
                ordinal,
                macroElement,
                sourceName,
                sourceCodeText,
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
    private static KlassParser getParser(@Nonnull ANTLRErrorListener errorListener, CommonTokenStream tokenStream)
    {
        KlassParser parser = new KlassParser(tokenStream);
        parser.removeErrorListeners();
        parser.addErrorListener(errorListener);
        return parser;
    }

    @Nonnull
    public static CompilationUnit getMacroCompilationUnit(
            int ordinal,
            @Nonnull AntlrElement macroElement,
            @Nonnull AbstractCompilerPhase macroExpansionCompilerPhase,
            @Nonnull String sourceCodeText,
            @Nonnull Function<KlassParser, ? extends ParserRuleContext> parserRule)
    {
        String sourceName = macroExpansionCompilerPhase.getName() + " macro";
        return CompilationUnit.createFromText(
                ordinal,
                Optional.of(macroElement),
                sourceName,
                sourceCodeText,
                parserRule);
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

    public SourceCodeBuilder build()
    {
        if (this.sourceCodeBuilder == null)
        {
            Optional<SourceCodeBuilder> macroSourceCodeBuilder = this.macroElement
                    .flatMap(AntlrElement::getCompilationUnit)
                    .map(CompilationUnit::build);

            this.sourceCodeBuilder = getSourceCodeBuilder(macroSourceCodeBuilder);
        }
        return this.sourceCodeBuilder;
    }

    private SourceCodeBuilder getSourceCodeBuilder(Optional<SourceCodeBuilder> macroSourceCodeBuilder)
    {
        return new SourceCodeBuilderImpl(
                this.sourceName,
                this.sourceCodeText,
                this.tokenStream,
                this.parserContext,
                macroSourceCodeBuilder,
                this.tokenCategoriesFromLexer,
                this.tokenCategoriesFromParser);
    }
}
