package cool.klass.model.converter.compiler.error;

import cool.klass.model.converter.compiler.CompilationUnit;
import cool.klass.model.meta.grammar.KlassParser.ClassReferenceContext;
import cool.klass.model.meta.grammar.KlassParser.EnumerationPropertyContext;
import cool.klass.model.meta.grammar.KlassParser.EnumerationReferenceContext;
import cool.klass.model.meta.grammar.KlassParser.EscapedIdentifierContext;
import cool.klass.model.meta.grammar.KlassParser.IdentifierContext;
import cool.klass.model.meta.grammar.KlassParser.KeywordValidAsIdentifierContext;
import cool.klass.model.meta.grammar.KlassParser.PrimitivePropertyContext;
import cool.klass.model.meta.grammar.KlassParser.ProjectionReferenceContext;
import cool.klass.model.meta.grammar.KlassParser.PropertyModifierContext;
import org.antlr.v4.runtime.Token;
import org.eclipse.collections.api.list.MutableList;

public class ErrorUnderlineListener extends BaseErrorListener
{
    public ErrorUnderlineListener(CompilationUnit compilationUnit, MutableList<String> contextualStrings)
    {
        super(compilationUnit, contextualStrings);
    }

    @Override
    public void enterPrimitiveProperty(PrimitivePropertyContext ctx)
    {
        this.handleEscapedIdentifier(ctx.escapedIdentifier());
    }

    @Override
    public void enterEnumerationProperty(EnumerationPropertyContext ctx)
    {
        this.handleEscapedIdentifier(ctx.escapedIdentifier());
    }

    @Override
    public void enterPropertyModifier(PropertyModifierContext ctx)
    {
        this.addUnderlinedToken(ctx.getStart());
    }

    @Override
    public void enterIdentifier(IdentifierContext ctx)
    {
        this.addUnderlinedToken(ctx.getStart());
    }

    @Override
    public void enterClassReference(ClassReferenceContext ctx)
    {
        this.addUnderlinedToken(ctx.getStart());
    }

    @Override
    public void enterEnumerationReference(EnumerationReferenceContext ctx)
    {
        this.addUnderlinedToken(ctx.getStart());
    }

    @Override
    public void enterProjectionReference(ProjectionReferenceContext ctx)
    {
        this.addUnderlinedToken(ctx.getStart());
    }

    protected void handleEscapedIdentifier(EscapedIdentifierContext escapedIdentifierContext)
    {
        IdentifierContext identifier = escapedIdentifierContext.identifier();
        if (identifier != null)
        {
            this.addUnderlinedToken(identifier.getStart());
            return;
        }
        KeywordValidAsIdentifierContext keyword = escapedIdentifierContext.keywordValidAsIdentifier();
        if (keyword != null)
        {
            this.addUnderlinedToken(keyword.getStart());
            return;
        }

        throw new AssertionError();
    }

    protected void addUnderlinedToken(Token offendingToken)
    {
        int line = offendingToken.getLine();
        String errorLine = this.compilationUnit.getLines()[line - 1];

        String errorStringUnderlined = ErrorUnderlineListener.getErrorLineStringUnderlined(offendingToken, errorLine);
        this.contextualStrings.add(errorLine);
        this.contextualStrings.add(errorStringUnderlined);
    }

    public static String getErrorLineStringUnderlined(Token offendingToken, String errorLine)
    {
        // replace tabs with single space so that charPositionInLine gives us the column to start underlining.
        String errorLineWithoutTabs = errorLine.replaceAll("\t", " ");
        String formatString = "%" + errorLineWithoutTabs.length() + "s";
        StringBuilder underLine = new StringBuilder(String.format(formatString, ""));
        int start = offendingToken.getStartIndex();
        int stop = offendingToken.getStopIndex();
        if (start >= 0 && stop >= 0)
        {
            for (int i = 0; i <= (stop - start); i++)
            {
                underLine.setCharAt(offendingToken.getCharPositionInLine() + i, '^');
            }
        }
        return underLine.toString();
    }
}
