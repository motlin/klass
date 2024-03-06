package cool.klass.model.converter.compiler.syntax.highlighter;

import java.time.Duration;
import java.util.LinkedHashMap;
import java.util.Optional;

import com.google.common.base.Stopwatch;
import cool.klass.model.meta.grammar.KlassLexer;
import cool.klass.model.meta.grammar.KlassParser;
import cool.klass.test.constants.KlassTestConstants;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CodePointCharStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.tree.ParseTree;
import org.eclipse.collections.api.map.MutableMapIterable;
import org.eclipse.collections.impl.map.ordered.mutable.OrderedMapAdapter;
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
        String              sourceCodeText = KlassTestConstants.STACK_OVERFLOW_SOURCE_CODE_TEXT;
        String              sourceName     = "example.klass";
        CodePointCharStream charStream     = CharStreams.fromString(sourceCodeText, sourceName);
        KlassLexer          lexer          = new KlassLexer(charStream);
        CommonTokenStream   tokenStream    = new CommonTokenStream(lexer);
        KlassParser         parser         = new KlassParser(tokenStream);
        ParseTree           parseTree      = parser.compilationUnit();

        MutableMapIterable<Token, Color> lexerColors  = OrderedMapAdapter.adapt(new LinkedHashMap<>());
        MutableMapIterable<Token, Color> parserColors = OrderedMapAdapter.adapt(new LinkedHashMap<>());

        Stopwatch lexerStopwatch = Stopwatch.createStarted();
        SyntaxHighlighterListenerTest.findLexerColor(tokenStream, lexerColors, colorScheme);
        lexerStopwatch.stop();
        Duration elapsedLexer = lexerStopwatch.elapsed();
        LOGGER.info("elapsedLexer = {}", elapsedLexer);

        Stopwatch parserStopwatch = Stopwatch.createStarted();
        SyntaxHighlighterListener.getParserColors(parseTree, parserColors, colorScheme);
        parserStopwatch.stop();
        Duration elapsedParser = parserStopwatch.elapsed();
        LOGGER.info("elapsedParser = {}", elapsedParser);

        Stopwatch rewriteStopwatch = Stopwatch.createStarted();
        String    rewriteText      = getColorizedText(tokenStream, lexerColors, parserColors);
        rewriteStopwatch.stop();
        Duration elapsedRewrite = rewriteStopwatch.elapsed();
        LOGGER.info("elapsedRewrite = {}", elapsedRewrite);

        Color  background = colorScheme.getBackground();
        String before     = background.getBefore();
        String after      = background.getAfter().orElse("");
        LOGGER.info("rewriteText =\n{}{}{}", before, rewriteText, after);
    }

    private static void findLexerColor(
            CommonTokenStream tokenStream,
            MutableMapIterable<Token, Color> lexerColors,
            ColorScheme colorScheme)
    {
        for (Token token : tokenStream.getTokens())
        {
            SyntaxHighlighterListenerTest.findLexerColor(token, lexerColors, colorScheme);
        }
    }

    private static void findLexerColor(
            Token token,
            MutableMapIterable<Token, Color> lexerColors,
            ColorScheme colorScheme)
    {
        Color color = SyntaxHighlighterListenerTest.getLexerColor(colorScheme, token);
        if (color == null)
        {
            return;
        }

        Color duplicate = lexerColors.put(token, color);
        if (duplicate != null)
        {
            throw new AssertionError(token);
        }
    }

    private static Color getLexerColor(ColorScheme colorScheme, Token token)
    {
        int channel = token.getChannel();
        if (channel == KlassLexer.COMMENTS_CHANNEL)
        {
            return colorScheme.getBlockComment();
        }
        if (channel == KlassLexer.LINE_COMMENTS_CHANNEL)
        {
            return colorScheme.getLineComment();
        }

        switch (token.getType())
        {
            case KlassLexer.StringLiteral:
            {
                return colorScheme.getStringLiteral();
            }
            case KlassLexer.IntegerLiteral:
            {
                return colorScheme.getIntegerLiteral();
            }
            case KlassLexer.BooleanLiteral:
            {
                return colorScheme.getBooleanLiteral();
            }
            case KlassLexer.CharacterLiteral:
            {
                return colorScheme.getCharacterLiteral();
            }
            case KlassLexer.FloatingPointLiteral:
            {
                return colorScheme.getFloatingPointLiteral();
            }
            case KlassLexer.PUNCTUATION_LPAREN:
            case KlassLexer.PUNCTUATION_RPAREN:
            case KlassLexer.PUNCTUATION_LBRACE:
            case KlassLexer.PUNCTUATION_RBRACE:
            case KlassLexer.PUNCTUATION_LBRACK:
            case KlassLexer.PUNCTUATION_RBRACK:
            case KlassLexer.PUNCTUATION_COLON:
            case KlassLexer.PUNCTUATION_SLASH:
            case KlassLexer.PUNCTUATION_QUESTION:
            {
                return colorScheme.getPunctuation();
            }
            case KlassLexer.PUNCTUATION_COMMA:
            {
                return colorScheme.getComma();
            }
            case KlassLexer.PUNCTUATION_DOTDOT:
            {
                return colorScheme.getDotDot();
            }
            case KlassLexer.PUNCTUATION_DOT:
            {
                return colorScheme.getDot();
            }
            case KlassLexer.PUNCTUATION_SEMI:
            {
                return colorScheme.getSemi();
            }
            case KlassLexer.OPERATOR_EQ:
            case KlassLexer.OPERATOR_NE:
            case KlassLexer.OPERATOR_LT:
            case KlassLexer.OPERATOR_GT:
            case KlassLexer.OPERATOR_LE:
            case KlassLexer.OPERATOR_GE:
            {
                return colorScheme.getOperator();
            }
            case KlassLexer.VERB_GET:
            case KlassLexer.VERB_POST:
            case KlassLexer.VERB_PUT:
            case KlassLexer.VERB_PATCH:
            case KlassLexer.VERB_DELETE:
            {
                return colorScheme.getVerb();
            }
            default:
            {
                return null;
            }
        }
    }

    private static String getColorizedText(
            CommonTokenStream tokenStream,
            MutableMapIterable<Token, Color> lexerColors,
            MutableMapIterable<Token, Color> parserColors)
    {
        StringBuilder result = new StringBuilder();
        for (Token token : tokenStream.getTokens())
        {
            Optional<Color> color = getColor(token, lexerColors, parserColors);
            color.map(Color::getBefore).ifPresent(result::append);
            result.append(token.getText());
            color.flatMap(Color::getAfter).ifPresent(result::append);
        }
        return result.toString();
    }

    private static Optional<Color> getColor(
            Token token,
            MutableMapIterable<Token, Color> lexerColors,
            MutableMapIterable<Token, Color> parserColors)
    {
        Color lexerColor  = lexerColors.get(token);
        Color parserColor = parserColors.get(token);
        if (lexerColor != null && parserColor != null)
        {
            throw new AssertionError(token);
        }
        if (lexerColor != null)
        {
            return Optional.of(lexerColor);
        }
        if (parserColor != null)
        {
            return Optional.of(parserColor);
        }
        return Optional.empty();
    }
}
