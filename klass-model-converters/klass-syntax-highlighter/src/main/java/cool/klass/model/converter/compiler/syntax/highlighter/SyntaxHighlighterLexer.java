package cool.klass.model.converter.compiler.syntax.highlighter;

import cool.klass.model.meta.grammar.KlassLexer;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.Token;
import org.eclipse.collections.api.map.MutableMapIterable;

public final class SyntaxHighlighterLexer
{
    private SyntaxHighlighterLexer()
    {
        throw new AssertionError("Suppress default constructor for noninstantiability");
    }

    public static void findLexerColor(
            CommonTokenStream tokenStream,
            MutableMapIterable<Token, Color> lexerColors,
            ColorScheme colorScheme)
    {
        for (Token token : tokenStream.getTokens())
        {
            SyntaxHighlighterLexer.findLexerColor(token, lexerColors, colorScheme);
        }
    }

    private static void findLexerColor(
            Token token,
            MutableMapIterable<Token, Color> lexerColors,
            ColorScheme colorScheme)
    {
        Color color = SyntaxHighlighterLexer.getLexerColor(colorScheme, token);
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
}
