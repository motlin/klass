package cool.klass.model.converter.compiler;

import cool.klass.model.meta.grammar.KlassBaseVisitor;
import cool.klass.model.meta.grammar.KlassParser.IdentifierContext;
import cool.klass.model.meta.grammar.KlassParser.KeywordValidAsIdentifierContext;

public final class EscapedIdentifierVisitor extends KlassBaseVisitor<String>
{
    public static final EscapedIdentifierVisitor INSTANCE = new EscapedIdentifierVisitor();

    private EscapedIdentifierVisitor()
    {
    }

    @Override
    public String visitIdentifier(IdentifierContext ctx)
    {
        return ctx.getText();
    }

    @Override
    public String visitKeywordValidAsIdentifier(KeywordValidAsIdentifierContext ctx)
    {
        return ctx.getText();
    }
}
