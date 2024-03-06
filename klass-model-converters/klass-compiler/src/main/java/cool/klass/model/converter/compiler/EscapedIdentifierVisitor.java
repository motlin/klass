package cool.klass.model.converter.compiler;

import cool.klass.model.meta.grammar.KlassParser.EscapedIdentifierContext;
import cool.klass.model.meta.grammar.KlassParser.IdentifierContext;
import cool.klass.model.meta.grammar.KlassParser.KeywordValidAsIdentifierContext;
import org.antlr.v4.runtime.ParserRuleContext;

public final class EscapedIdentifierVisitor
{
    private EscapedIdentifierVisitor()
    {
        throw new AssertionError("Suppress default constructor for noninstantiability");
    }

    public static ParserRuleContext getNameContext(EscapedIdentifierContext escapedIdentifierContext)
    {
        IdentifierContext identifier = escapedIdentifierContext.identifier();
        if (identifier != null)
        {
            return identifier;
        }
        KeywordValidAsIdentifierContext keyword = escapedIdentifierContext.keywordValidAsIdentifier();
        if (keyword != null)
        {
            return keyword;
        }
        throw new AssertionError(escapedIdentifierContext);
    }

    public static String getName(EscapedIdentifierContext escapedIdentifierContext)
    {
        return EscapedIdentifierVisitor.getNameContext(escapedIdentifierContext).getText();
    }
}
