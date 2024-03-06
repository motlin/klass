package cool.klass.model.converter.compiler.token.categorizing.lexer;

import java.util.LinkedHashMap;

import cool.klass.model.converter.compiler.token.categories.TokenCategory;
import cool.klass.model.meta.grammar.KlassLexer;
import org.antlr.v4.runtime.BufferedTokenStream;
import org.antlr.v4.runtime.Token;
import org.eclipse.collections.api.map.MapIterable;
import org.eclipse.collections.api.map.MutableMapIterable;
import org.eclipse.collections.impl.map.ordered.mutable.OrderedMapAdapter;

public final class LexerBasedTokenCategorizer
{
    private LexerBasedTokenCategorizer()
    {
        throw new AssertionError("Suppress default constructor for noninstantiability");
    }

    public static MapIterable<Token, TokenCategory> findTokenCategoriesFromLexer(BufferedTokenStream tokenStream)
    {
        MutableMapIterable<Token, TokenCategory> tokenCategories = OrderedMapAdapter.adapt(new LinkedHashMap<>());
        for (Token token : tokenStream.getTokens())
        {
            LexerBasedTokenCategorizer.findTokenCategoriesFromLexer(token, tokenCategories);
        }
        return tokenCategories.asUnmodifiable();
    }

    private static void findTokenCategoriesFromLexer(
            Token token,
            MutableMapIterable<Token, TokenCategory> tokenCategories)
    {
        TokenCategory tokenCategory = LexerBasedTokenCategorizer.getTokenCategory(token);
        if (tokenCategory == null)
        {
            return;
        }

        TokenCategory duplicate = tokenCategories.put(token, tokenCategory);
        if (duplicate != null)
        {
            throw new AssertionError(token);
        }
    }

    private static TokenCategory getTokenCategory(Token token)
    {
        int channel = token.getChannel();
        if (channel == KlassLexer.COMMENTS_CHANNEL)
        {
            return TokenCategory.BLOCK_COMMENT;
        }
        if (channel == KlassLexer.LINE_COMMENTS_CHANNEL)
        {
            return TokenCategory.LINE_COMMENT;
        }

        switch (token.getType())
        {
            case KlassLexer.StringLiteral:
            {
                return TokenCategory.STRING_LITERAL;
            }
            case KlassLexer.IntegerLiteral:
            {
                return TokenCategory.INTEGER_LITERAL;
            }
            case KlassLexer.BooleanLiteral:
            {
                return TokenCategory.BOOLEAN_LITERAL;
            }
            case KlassLexer.CharacterLiteral:
            {
                return TokenCategory.CHARACTER_LITERAL;
            }
            case KlassLexer.FloatingPointLiteral:
            {
                return TokenCategory.FLOATING_POINT_LITERAL;
            }
            case KlassLexer.PUNCTUATION_LPAREN:
            {
                return TokenCategory.PARENTHESIS_LEFT;
            }
            case KlassLexer.PUNCTUATION_RPAREN:
            {
                return TokenCategory.PARENTHESIS_RIGHT;
            }
            case KlassLexer.PUNCTUATION_LBRACE:
            {
                return TokenCategory.CURLY_LEFT;
            }
            case KlassLexer.PUNCTUATION_RBRACE:
            {
                return TokenCategory.CURLY_RIGHT;
            }
            case KlassLexer.PUNCTUATION_LBRACK:
            {
                return TokenCategory.SQUARE_BRACKET_LEFT;
            }
            case KlassLexer.PUNCTUATION_RBRACK:
            {
                return TokenCategory.SQUARE_BRACKET_RIGHT;
            }
            case KlassLexer.PUNCTUATION_COLON:
            {
                return TokenCategory.COLON;
            }
            case KlassLexer.PUNCTUATION_SLASH:
            {
                return TokenCategory.SLASH;
            }
            case KlassLexer.PUNCTUATION_QUESTION:
            {
                return TokenCategory.QUESTION;
            }
            case KlassLexer.PUNCTUATION_COMMA:
            {
                return TokenCategory.COMMA;
            }
            case KlassLexer.PUNCTUATION_DOTDOT:
            {
                return TokenCategory.DOTDOT;
            }
            case KlassLexer.PUNCTUATION_DOT:
            {
                return TokenCategory.DOT;
            }
            case KlassLexer.PUNCTUATION_SEMI:
            {
                return TokenCategory.SEMICOLON;
            }
            case KlassLexer.OPERATOR_EQ:
            {
                return TokenCategory.OPERATOR_EQ;
            }
            case KlassLexer.OPERATOR_NE:
            {
                return TokenCategory.OPERATOR_NE;
            }
            case KlassLexer.OPERATOR_LT:
            {
                return TokenCategory.OPERATOR_LT;
            }
            case KlassLexer.OPERATOR_GT:
            {
                return TokenCategory.OPERATOR_GT;
            }
            case KlassLexer.OPERATOR_LE:
            {
                return TokenCategory.OPERATOR_LE;
            }
            case KlassLexer.OPERATOR_GE:
            {
                return TokenCategory.OPERATOR_GE;
            }
            case KlassLexer.VERB_GET:
            {
                return TokenCategory.VERB_GET;
            }
            case KlassLexer.VERB_POST:
            {
                return TokenCategory.VERB_POST;
            }
            case KlassLexer.VERB_PUT:
            {
                return TokenCategory.VERB_PUT;
            }
            case KlassLexer.VERB_PATCH:
            {
                return TokenCategory.VERB_PATCH;
            }
            case KlassLexer.VERB_DELETE:
            {
                return TokenCategory.VERB_DELETE;
            }
            default:
            {
                return null;
            }
        }
    }
}
