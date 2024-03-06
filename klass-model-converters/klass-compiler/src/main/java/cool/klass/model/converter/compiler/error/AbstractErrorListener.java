package cool.klass.model.converter.compiler.error;

import java.util.Objects;

import javax.annotation.Nonnull;

import cool.klass.model.converter.compiler.CompilationUnit;
import cool.klass.model.meta.grammar.KlassLexer;
import cool.klass.model.meta.grammar.listener.KlassThrowingListener;
import org.antlr.v4.runtime.Token;
import org.eclipse.collections.api.list.MutableList;
import org.fusesource.jansi.Ansi.Color;

import static org.fusesource.jansi.Ansi.Color.BLACK;
import static org.fusesource.jansi.Ansi.Color.BLUE;
import static org.fusesource.jansi.Ansi.Color.CYAN;
import static org.fusesource.jansi.Ansi.Color.GREEN;
import static org.fusesource.jansi.Ansi.Color.MAGENTA;
import static org.fusesource.jansi.Ansi.Color.RED;
import static org.fusesource.jansi.Ansi.Color.WHITE;
import static org.fusesource.jansi.Ansi.Color.YELLOW;
import static org.fusesource.jansi.Ansi.ansi;

public class AbstractErrorListener extends KlassThrowingListener
{
    @Nonnull
    protected final CompilationUnit                    compilationUnit;
    @Nonnull
    protected final MutableList<AbstractContextString> contextualStrings;

    protected AbstractErrorListener(
            @Nonnull CompilationUnit compilationUnit,
            @Nonnull MutableList<AbstractContextString> contextualStrings)
    {
        this.compilationUnit = Objects.requireNonNull(compilationUnit);
        this.contextualStrings = Objects.requireNonNull(contextualStrings);
    }

    protected static String colorize(@Nonnull Token token)
    {
        String text = token.getText();

        if (token.getChannel() == KlassLexer.WHITESPACE_CHANNEL)
        {
            return text;
        }

        switch (token.getType())
        {
            case KlassLexer.MODIFIER_CLASS_SYSTEM_TEMPORAL:
            case KlassLexer.MODIFIER_CLASS_VALID_TEMPORAL:
            case KlassLexer.MODIFIER_CLASS_BITEMPORAL:
            case KlassLexer.MODIFIER_CLASS_VERSIONED:
            case KlassLexer.MODIFIER_CLASS_AUDITED:
            case KlassLexer.MODIFIER_CLASS_OPTIMISTICALLY_LOCKED:
            case KlassLexer.MODIFIER_CLASS_TRANSIENT:
            case KlassLexer.MODIFIER_PROPERTY_KEY:
            case KlassLexer.MODIFIER_PROPERTY_PRIVATE:
            case KlassLexer.MODIFIER_PROPERTY_VALID:
            case KlassLexer.MODIFIER_PROPERTY_SYSTEM:
            case KlassLexer.MODIFIER_PROPERTY_FROM:
            case KlassLexer.MODIFIER_PROPERTY_TO:
            case KlassLexer.MODIFIER_PROPERTY_CREATED_BY:
            case KlassLexer.MODIFIER_PROPERTY_CREATED_ON:
            case KlassLexer.MODIFIER_PROPERTY_LAST_UPDATED_BY:
            case KlassLexer.MODIFIER_PROPERTY_DERIVED:
                return getStringDim(text, YELLOW);
            case KlassLexer.MODIFIER_ASSOCIATION_END_OWNED:
            case KlassLexer.MODIFIER_ASSOCIATION_END_FINAL:
                return getStringBright(text, YELLOW);
            case KlassLexer.MODIFIER_VERSION:
            case KlassLexer.MODIFIER_USER_ID:
            case KlassLexer.MODIFIER_ID:
                return getStringDim(text, YELLOW);
            case KlassLexer.VALIDATION_MIN_LENGTH:
            case KlassLexer.VALIDATION_MINIMUM_LENGTH:
            case KlassLexer.VALIDATION_MAX_LENGTH:
            case KlassLexer.VALIDATION_MAXIMUM_LENGTH:
            case KlassLexer.VALIDATION_MIN:
            case KlassLexer.VALIDATION_MINIMUM:
            case KlassLexer.VALIDATION_MAX:
            case KlassLexer.VALIDATION_MAXIMUM:
                return getStringDim(text, YELLOW);
            // Keywords
            case KlassLexer.KEYWORD_PACKAGE:
            case KlassLexer.KEYWORD_ENUMERATION:
            case KlassLexer.KEYWORD_INTERFACE:
            case KlassLexer.KEYWORD_CLASS:
            case KlassLexer.KEYWORD_ASSOCIATION:
            case KlassLexer.KEYWORD_PROJECTION:
            case KlassLexer.KEYWORD_SERVICE:
                return getStringDim(text, MAGENTA);
            case KlassLexer.KEYWORD_USER:
            case KlassLexer.KEYWORD_NATIVE:
            case KlassLexer.KEYWORD_RELATIONSHIP:
            case KlassLexer.KEYWORD_MULTIPLICITY:
            case KlassLexer.KEYWORD_ORDER_BY:
            case KlassLexer.KEYWORD_CRITERIA:
            case KlassLexer.KEYWORD_ON:
            case KlassLexer.KEYWORD_ABSTRACT:
            case KlassLexer.KEYWORD_EXTENDS:
            case KlassLexer.KEYWORD_IMPLEMENTS:
            case KlassLexer.KEYWORD_TABLE_PER_CLASS:
            case KlassLexer.KEYWORD_TABLE_PER_SUBCLASS:
            case KlassLexer.KEYWORD_TABLE_FOR_ALL_SUBCLASSES:
            case KlassLexer.LITERAL_NULL:
            case KlassLexer.LITERAL_THIS:
                return getStringBright(text, MAGENTA);
            // Primitives
            case KlassLexer.PRIMITIVE_TYPE_BOOLEAN:
            case KlassLexer.PRIMITIVE_TYPE_INTEGER:
            case KlassLexer.PRIMITIVE_TYPE_LONG:
            case KlassLexer.PRIMITIVE_TYPE_DOUBLE:
            case KlassLexer.PRIMITIVE_TYPE_FLOAT:
            case KlassLexer.PRIMITIVE_TYPE_STRING:
            case KlassLexer.PRIMITIVE_TYPE_INSTANT:
            case KlassLexer.PRIMITIVE_TYPE_LOCAL_DATE:
            case KlassLexer.PRIMITIVE_TYPE_TEMPORAL_INSTANT:
            case KlassLexer.PRIMITIVE_TYPE_TEMPORAL_RANGE:
                return getStringBright(text, MAGENTA);
            // Literals
            case KlassLexer.StringLiteral:
            case KlassLexer.IntegerLiteral:
            case KlassLexer.BooleanLiteral:
            case KlassLexer.CharacterLiteral:
            case KlassLexer.FloatingPointLiteral:
            case KlassLexer.PUNCTUATION_ASTERISK:
                return getStringBright(text, BLUE);
            case KlassLexer.Identifier:
                return getStringBright(text, WHITE);
            case KlassLexer.PUNCTUATION_LPAREN:
            case KlassLexer.PUNCTUATION_RPAREN:
            case KlassLexer.PUNCTUATION_LBRACE:
            case KlassLexer.PUNCTUATION_RBRACE:
            case KlassLexer.PUNCTUATION_LBRACK:
            case KlassLexer.PUNCTUATION_RBRACK:
            case KlassLexer.PUNCTUATION_SEMI:
            case KlassLexer.PUNCTUATION_COLON:
            case KlassLexer.PUNCTUATION_COMMA:
            case KlassLexer.PUNCTUATION_DOT:
            case KlassLexer.PUNCTUATION_DOTDOT:
            case KlassLexer.PUNCTUATION_SLASH:
                return getStringDim(text, CYAN);
            // Operators
            case KlassLexer.OPERATOR_EQ:
                return getStringBright(text, YELLOW);
            // Verbs
            case KlassLexer.VERB_GET:
            case KlassLexer.VERB_POST:
            case KlassLexer.VERB_PUT:
            case KlassLexer.VERB_PATCH:
            case KlassLexer.VERB_DELETE:
                return getStringBright(text, GREEN);
            default:
                return getStringBright(text, RED);
        }
    }

    private static String getStringBright(String text, @Nonnull Color color)
    {
        return ansi().bg(BLACK).fgBright(color).a(text).toString();
    }

    private static String getStringDim(String text, @Nonnull Color color)
    {
        return ansi().bg(BLACK).fg(color).a(text).toString();
    }
}
