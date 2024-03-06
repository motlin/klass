package cool.klass.model.converter.compiler.error;

import javax.annotation.Nonnull;

import cool.klass.model.converter.compiler.AntlrUtils;
import cool.klass.model.converter.compiler.CompilationUnit;
import cool.klass.model.meta.grammar.KlassParser.AssociationEndModifierContext;
import cool.klass.model.meta.grammar.KlassParser.ClassHeaderContext;
import cool.klass.model.meta.grammar.KlassParser.ClassReferenceContext;
import cool.klass.model.meta.grammar.KlassParser.ClassifierModifierContext;
import cool.klass.model.meta.grammar.KlassParser.ClassifierReferenceContext;
import cool.klass.model.meta.grammar.KlassParser.CriteriaOperatorContext;
import cool.klass.model.meta.grammar.KlassParser.DataTypePropertyModifierContext;
import cool.klass.model.meta.grammar.KlassParser.EnumerationLiteralContext;
import cool.klass.model.meta.grammar.KlassParser.EnumerationPrettyNameContext;
import cool.klass.model.meta.grammar.KlassParser.EnumerationPropertyContext;
import cool.klass.model.meta.grammar.KlassParser.EnumerationReferenceContext;
import cool.klass.model.meta.grammar.KlassParser.IdentifierContext;
import cool.klass.model.meta.grammar.KlassParser.InterfaceHeaderContext;
import cool.klass.model.meta.grammar.KlassParser.InterfaceReferenceContext;
import cool.klass.model.meta.grammar.KlassParser.MaxLengthValidationContext;
import cool.klass.model.meta.grammar.KlassParser.MaxValidationContext;
import cool.klass.model.meta.grammar.KlassParser.MinLengthValidationContext;
import cool.klass.model.meta.grammar.KlassParser.MinValidationContext;
import cool.klass.model.meta.grammar.KlassParser.MultiplicityContext;
import cool.klass.model.meta.grammar.KlassParser.PackageNameContext;
import cool.klass.model.meta.grammar.KlassParser.PrimitivePropertyContext;
import cool.klass.model.meta.grammar.KlassParser.PrimitiveTypeContext;
import cool.klass.model.meta.grammar.KlassParser.ProjectionReferenceContext;
import cool.klass.model.meta.grammar.KlassParser.ServiceCriteriaDeclarationContext;
import cool.klass.model.meta.grammar.KlassParser.ServiceCriteriaKeywordContext;
import cool.klass.model.meta.grammar.KlassParser.ServiceProjectionDispatchContext;
import cool.klass.model.meta.grammar.KlassParser.VariableReferenceContext;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.Token;
import org.eclipse.collections.api.list.ImmutableList;
import org.eclipse.collections.api.list.MutableList;
import org.eclipse.collections.api.map.ImmutableMap;
import org.eclipse.collections.impl.factory.Lists;
import org.eclipse.collections.impl.list.mutable.ListAdapter;

import static org.fusesource.jansi.Ansi.Color.BLACK;
import static org.fusesource.jansi.Ansi.Color.GREEN;
import static org.fusesource.jansi.Ansi.Color.RED;
import static org.fusesource.jansi.Ansi.ansi;

public class ErrorUnderlineListener
        extends AbstractErrorListener
{
    private final boolean isRoot;

    public ErrorUnderlineListener(
            @Nonnull CompilationUnit compilationUnit,
            @Nonnull MutableList<AbstractContextString> contextualStrings,
            boolean isRoot)
    {
        super(compilationUnit, contextualStrings);
        this.isRoot = isRoot;
    }

    @Override
    public void enterPackageName(@Nonnull PackageNameContext ctx)
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
    public void enterServiceProjectionDispatch(@Nonnull ServiceProjectionDispatchContext ctx)
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
    public void enterMinLengthValidation(@Nonnull MinLengthValidationContext ctx)
    {
        this.addUnderlinedToken(ctx.getStart());
    }

    @Override
    public void enterMaxLengthValidation(@Nonnull MaxLengthValidationContext ctx)
    {
        this.addUnderlinedToken(ctx.getStart());
    }

    @Override
    public void enterMinValidation(@Nonnull MinValidationContext ctx)
    {
        this.addUnderlinedToken(ctx.getStart());
    }

    @Override
    public void enterMaxValidation(@Nonnull MaxValidationContext ctx)
    {
        this.addUnderlinedToken(ctx.getStart());
    }

    @Override
    public void enterMultiplicity(@Nonnull MultiplicityContext ctx)
    {
        this.addUnderlinedRange(ctx);
    }

    @Override
    public void enterPrimitiveType(@Nonnull PrimitiveTypeContext ctx)
    {
        this.addUnderlinedToken(ctx.getStart());
    }

    @Override
    public void enterDataTypePropertyModifier(DataTypePropertyModifierContext ctx)
    {
        this.addUnderlinedToken(ctx.getStart());
    }

    @Override
    public void enterAssociationEndModifier(@Nonnull AssociationEndModifierContext ctx)
    {
        this.addUnderlinedToken(ctx.getStart());
    }

    @Override
    public void enterClassifierModifier(@Nonnull ClassifierModifierContext ctx)
    {
        InterfaceHeaderContext interfaceDeclarationHeaderContext = AntlrUtils.getParentOfType(
                ctx,
                InterfaceHeaderContext.class);
        if (interfaceDeclarationHeaderContext != null)
        {
            this.addUnderlinedToken(interfaceDeclarationHeaderContext, Lists.immutable.with(ctx.getStart()));
            return;
        }

        ClassHeaderContext classDeclarationHeaderContext = AntlrUtils.getParentOfType(
                ctx,
                ClassHeaderContext.class);
        if (classDeclarationHeaderContext != null)
        {
            this.addUnderlinedToken(classDeclarationHeaderContext, Lists.immutable.with(ctx.getStart()));
            return;
        }

        throw new AssertionError();
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
    public void enterInterfaceReference(@Nonnull InterfaceReferenceContext ctx)
    {
        this.addUnderlinedToken(ctx.getStart());
    }

    @Override
    public void enterClassReference(@Nonnull ClassReferenceContext ctx)
    {
        this.addUnderlinedToken(ctx.getStart());
    }

    @Override
    public void enterClassifierReference(@Nonnull ClassifierReferenceContext ctx)
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
        ServiceCriteriaDeclarationContext serviceCriteriaDeclarationContext = AntlrUtils.getParentOfType(
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

    private void addUnderlinedRange(@Nonnull ParserRuleContext ctx)
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

    private void addUnderlinedToken(@Nonnull ParserRuleContext ctx, @Nonnull ImmutableList<Token> offendingTokens)
    {
        ImmutableMap<Integer, Token> tokenByLine = offendingTokens.groupByUniqueKey(Token::getLine);

        Token startToken = ctx.getStart();
        Token stopToken  = ctx.getStop();
        int   startLine  = startToken.getLine();
        int   stopLine   = stopToken.getLine();

        for (int lineNumber = startLine; lineNumber <= stopLine; lineNumber++)
        {
            if (tokenByLine.containsKey(lineNumber))
            {
                Token offendingToken = tokenByLine.get(lineNumber);
                this.addUnderlinedContext(offendingToken, offendingToken, lineNumber);
            }
            else
            {
                // TODO: Move the decrement inside getLine?
                int           adjustedLineNumber = lineNumber - 1;
                String        sourceCodeLine     = this.compilationUnit.getLine(adjustedLineNumber);
                ContextString contextString      = new ContextString(lineNumber, sourceCodeLine);
                this.contextualStrings.add(contextString);
            }
        }
    }

    private void addUnderlinedToken(@Nonnull Token offendingToken)
    {
        this.addUnderlinedContext(offendingToken, offendingToken, offendingToken.getLine());
    }

    private void addUnderlinedContext(@Nonnull Token startToken, @Nonnull Token stopToken, int startLine)
    {
        if (startToken.getLine() != startLine)
        {
            throw new AssertionError();
        }

        if (stopToken.getLine() != startLine)
        {
            throw new AssertionError();
        }

        CommonTokenStream tokenStream     = (CommonTokenStream) this.compilationUnit.getTokenStream();
        int               startTokenIndex = startToken.getTokenIndex();
        int               stopTokenIndex  = stopToken.getTokenIndex();

        int beginTokenIndex = startTokenIndex;
        while (beginTokenIndex > 0 && tokenStream.get(beginTokenIndex - 1).getLine() == startLine)
        {
            beginTokenIndex -= 1;
        }

        int endTokenIndex = stopTokenIndex;
        while (endTokenIndex + 1 < tokenStream.size() && tokenStream.get(endTokenIndex + 1).getLine() == startLine)
        {
            endTokenIndex += 1;
        }

        MutableList<Token> tokens = ListAdapter.adapt(tokenStream.get(
                beginTokenIndex,
                endTokenIndex));

        String errorLine = tokens
                .collect(AbstractErrorListener::colorize)
                .makeString("");

        String errorStringUnderlined = this.getErrorLineStringUnderlined(startToken, stopToken);

        this.contextualStrings.add(new ContextString(startLine, errorLine));
        // TODO: Something about this
        this.contextualStrings.add(new UnderlineContextString(startLine, errorStringUnderlined));
    }

    @Nonnull
    private String getErrorLineStringUnderlined(@Nonnull Token startToken, @Nonnull Token stopToken)
    {
        int start              = startToken.getStartIndex();
        int stop               = stopToken.getStopIndex();
        int charPositionInLine = startToken.getCharPositionInLine();

        StringBuilder whitespaceBuffer = new StringBuilder();
        for (int i = 0; i < charPositionInLine; i++)
        {
            whitespaceBuffer.append(' ');
        }

        StringBuilder caretBuffer = new StringBuilder();
        for (int i = start; i <= stop; i++)
        {
            caretBuffer.append('^');
        }

        return ansi()
                .a(whitespaceBuffer.toString())
                .bg(BLACK).fgBright(this.isRoot ? RED : GREEN).a(caretBuffer.toString()).toString();
    }
}
