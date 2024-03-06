package cool.klass.model.converter.compiler.syntax.highlighter;

import java.time.Duration;

import com.google.common.base.Stopwatch;
import cool.klass.model.converter.compiler.syntax.highlighter.ansi.AnsiTokenColorizer;
import cool.klass.model.converter.compiler.syntax.highlighter.ansi.scheme.AnsiColorScheme;
import cool.klass.model.converter.compiler.syntax.highlighter.ansi.scheme.DarkAnsiColorScheme;
import cool.klass.model.converter.compiler.syntax.highlighter.ansi.scheme.LightAnsiColorScheme;
import cool.klass.model.converter.compiler.token.categories.TokenCategory;
import cool.klass.model.converter.compiler.token.categorizing.lexer.LexerBasedTokenCategorizer;
import cool.klass.model.converter.compiler.token.categorizing.parser.ParserBasedTokenCategorizer;
import cool.klass.model.meta.grammar.KlassLexer;
import cool.klass.model.meta.grammar.KlassParser;
import io.liftwizard.junit.rule.log.marker.LogMarkerTestRule;
import io.liftwizard.junit.rule.match.file.FileMatchRule;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CodePointCharStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.tree.ParseTree;
import org.eclipse.collections.api.map.MapIterable;
import org.fusesource.jansi.Ansi;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestRule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SyntaxHighlighterListenerTest
{
    private static final Logger LOGGER = LoggerFactory.getLogger(SyntaxHighlighterListenerTest.class);

    @Rule
    public final TestRule logMarkerTestRule = new LogMarkerTestRule();

    @Test
    public void lightColorScheme()
    {
        this.testColorScheme(LightAnsiColorScheme.INSTANCE);
    }

    @Test
    public void darkColorScheme()
    {
        this.testColorScheme(DarkAnsiColorScheme.INSTANCE);
    }

    private void testColorScheme(AnsiColorScheme colorScheme)
    {
        Stopwatch           lexerStopwatch = Stopwatch.createStarted();
        String              sourceCodeText = FileMatchRule.slurp("/com/stackoverflow/stackoverflow.klass", this.getClass());
        String              sourceName     = "example.klass";
        CodePointCharStream charStream     = CharStreams.fromString(sourceCodeText, sourceName);
        KlassLexer          lexer          = new KlassLexer(charStream);
        CommonTokenStream   tokenStream    = new CommonTokenStream(lexer);

        MapIterable<Token, TokenCategory> tokenCategoriesFromLexer =
                LexerBasedTokenCategorizer.findTokenCategoriesFromLexer(tokenStream);

        lexerStopwatch.stop();
        Duration elapsedLexer = lexerStopwatch.elapsed();
        LOGGER.info("elapsedLexer = {}", elapsedLexer);

        Stopwatch   parserStopwatch = Stopwatch.createStarted();
        KlassParser parser          = new KlassParser(tokenStream);
        ParseTree   parseTree       = parser.compilationUnit();
        MapIterable<Token, TokenCategory> tokenCategoriesFromParser =
                ParserBasedTokenCategorizer.findTokenCategoriesFromParser(parseTree);
        parserStopwatch.stop();
        Duration elapsedParser = parserStopwatch.elapsed();
        LOGGER.info("elapsedParser = {}", elapsedParser);

        Stopwatch rewriteStopwatch = Stopwatch.createStarted();
        AnsiTokenColorizer ansiTokenColorizer = new AnsiTokenColorizer(
                colorScheme,
                tokenCategoriesFromLexer,
                tokenCategoriesFromParser);

        Ansi ansi = Ansi.ansi();
        colorScheme.background(ansi);

        tokenStream
                .getTokens()
                .forEach(token -> ansiTokenColorizer.colorizeText(ansi, token));
        ansi.reset();

        rewriteStopwatch.stop();
        Duration elapsedRewrite = rewriteStopwatch.elapsed();
        LOGGER.info("elapsedRewrite = {}", elapsedRewrite);

        LOGGER.info("rewriteText =\n{}", ansi);
    }
}
