package cool.klass.model.converter.compiler;

import cool.klass.model.meta.grammar.KlassParser.EscapedIdentifierContext;
import cool.klass.model.meta.grammar.KlassParser.IdentifierContext;
import cool.klass.model.meta.grammar.KlassParser.KeywordValidAsIdentifierContext;

public final class EscapedIdentifierVisitor
{
    private EscapedIdentifierVisitor()
    {
        throw new AssertionError("Suppress default constructor for noninstantiability");
    }

    public static String get(EscapedIdentifierContext escapedIdentifierContext)
    {
        IdentifierContext identifier = escapedIdentifierContext.identifier();
        if (identifier != null)
        {
            return identifier.getText();
        }
        KeywordValidAsIdentifierContext keyword = escapedIdentifierContext.keywordValidAsIdentifier();
        if (keyword != null)
        {
            return keyword.getText();
        }
        throw new AssertionError(escapedIdentifierContext);
    }
}
