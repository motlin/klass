package cool.klass.model.converter.compiler.error;

import java.util.regex.Pattern;

import javax.annotation.Nonnull;

import cool.klass.model.meta.grammar.KlassParser.AssociationEndModifierContext;
import cool.klass.model.meta.grammar.KlassParser.ClassReferenceContext;
import cool.klass.model.meta.grammar.KlassParser.CriteriaOperatorContext;
import cool.klass.model.meta.grammar.KlassParser.EnumerationLiteralContext;
import cool.klass.model.meta.grammar.KlassParser.EnumerationPrettyNameContext;
import cool.klass.model.meta.grammar.KlassParser.EnumerationPropertyContext;
import cool.klass.model.meta.grammar.KlassParser.EnumerationReferenceContext;
import cool.klass.model.meta.grammar.KlassParser.IdentifierContext;
import cool.klass.model.meta.grammar.KlassParser.InterfaceReferenceContext;
import cool.klass.model.meta.grammar.KlassParser.MultiplicityContext;
import cool.klass.model.meta.grammar.KlassParser.PackageNameContext;
import cool.klass.model.meta.grammar.KlassParser.PrimitivePropertyContext;
import cool.klass.model.meta.grammar.KlassParser.ProjectionReferenceContext;
import cool.klass.model.meta.grammar.KlassParser.PropertyModifierContext;
import cool.klass.model.meta.grammar.KlassParser.ServiceCriteriaDeclarationContext;
import cool.klass.model.meta.grammar.KlassParser.ServiceCriteriaKeywordContext;
import cool.klass.model.meta.grammar.KlassParser.VariableReferenceContext;
import cool.klass.model.meta.grammar.listener.KlassThrowingListener;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.Token;
import org.eclipse.collections.api.list.ImmutableList;
import org.eclipse.collections.api.list.MutableList;
import org.eclipse.collections.api.map.ImmutableMap;
import org.eclipse.collections.impl.factory.Lists;

public class ErrorUnderlineListener extends KlassThrowingListener
{
    private static final Pattern NEWLINE_PATTERN = Pattern.compile("\\r?\\n");

    private final MutableList<String> contextualStrings;

    public ErrorUnderlineListener(MutableList<String> contextualStrings)
    {
        this.contextualStrings = contextualStrings;
    }

    @Override
    public void enterPackageName(PackageNameContext ctx)
    {
        this.addUnderlinedRange(ctx);
    }

    @Override
    public void enterEnumerationLiteral(@Nonnull EnumerationLiteralContext ctx)
    {
        this.addUnderlinedToken(ctx.getStart());
    }

    @Override
    public void enterEnumerationPrettyName(@Nonnull EnumerationPrettyNameContext ctx)
    {
        this.addUnderlinedToken(ctx.getStart());
    }

    @Override
    public void enterServiceCriteriaKeyword(@Nonnull ServiceCriteriaKeywordContext ctx)
    {
        this.addUnderlinedToken(ctx.getStart());
    }

    @Override
    public void enterPrimitiveProperty(@Nonnull PrimitivePropertyContext ctx)
    {
        this.addUnderlinedToken(ctx.getStart());
    }

    @Override
    public void enterEnumerationProperty(@Nonnull EnumerationPropertyContext ctx)
    {
        this.addUnderlinedToken(ctx.getStart());
    }

    @Override
    public void enterMultiplicity(MultiplicityContext ctx)
    {
        this.addUnderlinedRange(ctx);
    }

    @Override
    public void enterPropertyModifier(@Nonnull PropertyModifierContext ctx)
    {
        this.addUnderlinedToken(ctx.getStart());
    }

    @Override
    public void enterAssociationEndModifier(@Nonnull AssociationEndModifierContext ctx)
    {
        this.addUnderlinedToken(ctx.getStart());
    }

    @Override
    public void enterCriteriaOperator(@Nonnull CriteriaOperatorContext ctx)
    {
        this.addUnderlinedRange(ctx);
    }

    @Override
    public void enterEnumerationReference(@Nonnull EnumerationReferenceContext ctx)
    {
        this.addUnderlinedToken(ctx.getStart());
    }

    @Override
    public void enterInterfaceReference(InterfaceReferenceContext ctx)
    {
        this.addUnderlinedToken(ctx.getStart());
    }

    @Override
    public void enterClassReference(@Nonnull ClassReferenceContext ctx)
    {
        this.addUnderlinedToken(ctx.getStart());
    }

    @Override
    public void enterProjectionReference(@Nonnull ProjectionReferenceContext ctx)
    {
        this.addUnderlinedToken(ctx.getStart());
    }

    @Override
    public void enterVariableReference(@Nonnull VariableReferenceContext ctx)
    {
        ServiceCriteriaDeclarationContext serviceCriteriaDeclarationContext = this.getParentOfType(
                ctx,
                ServiceCriteriaDeclarationContext.class);
        if (serviceCriteriaDeclarationContext == null)
        {
            throw new AssertionError();
        }

        this.addUnderlinedToken(serviceCriteriaDeclarationContext, Lists.immutable.with(ctx.getStart()));
    }

    @Override
    public void enterIdentifier(@Nonnull IdentifierContext ctx)
    {
        this.addUnderlinedToken(ctx.getStart());
    }

    private void addUnderlinedRange(ParserRuleContext ctx)
    {
        Token startToken = ctx.getStart();
        Token stopToken  = ctx.getStop();
        int   startLine  = startToken.getLine();
        int   stopLine   = stopToken.getLine();

        if (startLine == stopLine)
        {
            this.addUnderlinedContext(startToken, stopToken, startLine);
        }
        else
        {
            throw new UnsupportedOperationException(this.getClass().getSimpleName()
                    + ".addUnderlinedRange() not implemented yet");
        }
    }

    private void addUnderlinedToken(@Nonnull Token offendingToken)
    {
        this.addUnderlinedContext(offendingToken, offendingToken, offendingToken.getLine());
    }

    private void addUnderlinedContext(@Nonnull Token startToken, @Nonnull Token stopToken, int startLine)
    {
        String   sourceCodeText        = startToken.getInputStream().toString();
        String[] lines                 = NEWLINE_PATTERN.split(sourceCodeText);
        String   errorLine             = lines[startLine - 1];
        String   errorStringUnderlined = ErrorUnderlineListener.getErrorLineStringUnderlined(startToken, stopToken);

        this.contextualStrings.add(errorLine);
        this.contextualStrings.add(errorStringUnderlined);
    }

    @Nonnull
    private static String getErrorLineStringUnderlined(Token startToken, Token stopToken)
    {
        int start              = startToken.getStartIndex();
        int stop               = stopToken.getStopIndex();
        int charPositionInLine = startToken.getCharPositionInLine();

        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < charPositionInLine; i++)
        {
            stringBuilder.append(' ');
        }
        for (int i = start; i <= stop; i++)
        {
            stringBuilder.append('^');
        }
        return stringBuilder.toString();
    }

    private void addUnderlinedToken(@Nonnull ParserRuleContext ctx, @Nonnull ImmutableList<Token> offendingTokens)
    {
        ImmutableMap<Integer, Token> tokenByLine = offendingTokens.groupByUniqueKey(Token::getLine);

        Token    startToken     = ctx.getStart();
        Token    stopToken      = ctx.getStop();
        int      startLine      = startToken.getLine();
        int      stopLine       = stopToken.getLine();
        String   sourceCodeText = startToken.getInputStream().toString();
        String[] lines          = NEWLINE_PATTERN.split(sourceCodeText);

        for (int lineNumber = startLine; lineNumber <= stopLine; lineNumber++)
        {
            if (tokenByLine.containsKey(lineNumber))
            {
                Token offendingToken = tokenByLine.get(lineNumber);
                this.addUnderlinedContext(offendingToken, offendingToken, lineNumber);
            }
            else
            {
                this.contextualStrings.add(lines[lineNumber - 1]);
            }
        }
    }

    private <T> T getParentOfType(@Nonnull ParserRuleContext ctx, Class<T> aClass /* klass? */)
    {
        if (aClass.isInstance(ctx))
        {
            return aClass.cast(ctx);
        }

        ParserRuleContext parent = ctx.getParent();
        if (parent == null)
        {
            return null;
        }

        return this.getParentOfType(parent, aClass);
    }
}
