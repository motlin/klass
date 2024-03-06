package cool.klass.model.converter.compiler.syntax.highlighter;

import java.time.Duration;
import java.util.Optional;

import com.google.common.base.Stopwatch;
import cool.klass.model.converter.compiler.token.categories.TokenCategory;
import cool.klass.model.converter.compiler.token.categorizing.lexer.LexerBasedTokenCategorizer;
import cool.klass.model.converter.compiler.token.categorizing.parser.ParserBasedTokenCategorizer;
import cool.klass.model.meta.grammar.KlassLexer;
import cool.klass.model.meta.grammar.KlassParser;
import cool.klass.test.constants.KlassTestConstants;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CodePointCharStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.tree.ParseTree;
import org.eclipse.collections.api.map.MapIterable;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SyntaxHighlighterListenerTest
{
    private static final Logger LOGGER = LoggerFactory.getLogger(SyntaxHighlighterListenerTest.class);

    @Test
    public void lightColorScheme()
    {
        this.testColorScheme(LightColorScheme.INSTANCE);
    }

    @Test
    public void darkColorScheme()
    {
        this.testColorScheme(DarkColorScheme.INSTANCE);
    }

    private void testColorScheme(ColorScheme colorScheme)
    {
        Stopwatch lexerStopwatch = Stopwatch.createStarted();
        String              sourceCodeText = KlassTestConstants.STACK_OVERFLOW_SOURCE_CODE_TEXT;
        String              sourceName     = "example.klass";
        CodePointCharStream charStream     = CharStreams.fromString(sourceCodeText, sourceName);
        KlassLexer          lexer          = new KlassLexer(charStream);
        CommonTokenStream   tokenStream    = new CommonTokenStream(lexer);
        MapIterable<Token, TokenCategory> tokenCategoriesFromLexer =
                LexerBasedTokenCategorizer.findTokenCategoriesFromLexer(tokenStream);

        lexerStopwatch.stop();
        Duration elapsedLexer = lexerStopwatch.elapsed();
        LOGGER.info("elapsedLexer = {}", elapsedLexer);

        Stopwatch parserStopwatch = Stopwatch.createStarted();
        KlassParser         parser         = new KlassParser(tokenStream);
        ParseTree           parseTree      = parser.compilationUnit();
        MapIterable<Token, TokenCategory> tokenCategoriesFromParser =
                ParserBasedTokenCategorizer.findTokenCategoriesFromParser(parseTree);
        parserStopwatch.stop();
        Duration elapsedParser = parserStopwatch.elapsed();
        LOGGER.info("elapsedParser = {}", elapsedParser);

        Stopwatch rewriteStopwatch = Stopwatch.createStarted();
        String rewriteText = SyntaxHighlighterListenerTest.getColorizedText(
                tokenStream,
                colorScheme,
                tokenCategoriesFromLexer,
                tokenCategoriesFromParser);
        rewriteStopwatch.stop();
        Duration elapsedRewrite = rewriteStopwatch.elapsed();
        LOGGER.info("elapsedRewrite = {}", elapsedRewrite);

        Color  background = colorScheme.getBackground();
        String before     = background.getBefore();
        String after      = background.getAfter().orElse("");
        LOGGER.info("rewriteText =\n{}{}{}", before, rewriteText, after);
    }

    private static String getColorizedText(
            CommonTokenStream tokenStream,
            ColorScheme colorScheme,
            MapIterable<Token, TokenCategory> tokenCategoriesFromLexer,
            MapIterable<Token, TokenCategory> tokenCategoriesFromParser)
    {
        StringBuilder result = new StringBuilder();
        for (Token token : tokenStream.getTokens())
        {
            Optional<TokenCategory> tokenCategory = SyntaxHighlighterListenerTest.getTokenCategory(
                    token,
                    tokenCategoriesFromLexer,
                    tokenCategoriesFromParser);
            Optional<Color> color = tokenCategory.map(justTokenCategory -> TokenCategoryToColor.getColor(justTokenCategory, colorScheme));
            color.map(Color::getBefore).ifPresent(result::append);
            result.append(token.getText());
            color.flatMap(Color::getAfter).ifPresent(result::append);
        }
        return result.toString();
    }

    private static Optional<TokenCategory> getTokenCategory(
            Token token,
            MapIterable<Token, TokenCategory> tokenCategoriesFromLexer,
            MapIterable<Token, TokenCategory> tokenCategoriesFromParser)
    {
        TokenCategory lexerCategory  = tokenCategoriesFromLexer.get(token);
        TokenCategory parserCategory = tokenCategoriesFromParser.get(token);
        if (lexerCategory != null && parserCategory != null)
        {
            throw new AssertionError(token);
        }
        if (lexerCategory != null)
        {
            return Optional.of(lexerCategory);
        }
        if (parserCategory != null)
        {
            return Optional.of(parserCategory);
        }
        return Optional.empty();
    }
}
