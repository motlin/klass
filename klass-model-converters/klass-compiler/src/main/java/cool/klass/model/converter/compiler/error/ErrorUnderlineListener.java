package cool.klass.model.converter.compiler.error;

import java.util.regex.Pattern;

import cool.klass.model.converter.compiler.CompilationUnit;
import cool.klass.model.meta.grammar.KlassParser.ArgumentContext;
import cool.klass.model.meta.grammar.KlassParser.ArgumentListContext;
import cool.klass.model.meta.grammar.KlassParser.AssociationBodyContext;
import cool.klass.model.meta.grammar.KlassParser.AssociationDeclarationContext;
import cool.klass.model.meta.grammar.KlassParser.AssociationEndContext;
import cool.klass.model.meta.grammar.KlassParser.AssociationEndModifierContext;
import cool.klass.model.meta.grammar.KlassParser.ClassBodyContext;
import cool.klass.model.meta.grammar.KlassParser.ClassDeclarationContext;
import cool.klass.model.meta.grammar.KlassParser.ClassMemberContext;
import cool.klass.model.meta.grammar.KlassParser.ClassModifierContext;
import cool.klass.model.meta.grammar.KlassParser.ClassOrUserContext;
import cool.klass.model.meta.grammar.KlassParser.ClassReferenceContext;
import cool.klass.model.meta.grammar.KlassParser.ClassServiceModifierContext;
import cool.klass.model.meta.grammar.KlassParser.ClassTypeContext;
import cool.klass.model.meta.grammar.KlassParser.CompilationUnitContext;
import cool.klass.model.meta.grammar.KlassParser.CriteriaExpressionAndContext;
import cool.klass.model.meta.grammar.KlassParser.CriteriaExpressionGroupContext;
import cool.klass.model.meta.grammar.KlassParser.CriteriaExpressionOrContext;
import cool.klass.model.meta.grammar.KlassParser.CriteriaNativeContext;
import cool.klass.model.meta.grammar.KlassParser.CriteriaOperatorContext;
import cool.klass.model.meta.grammar.KlassParser.DataTypeContext;
import cool.klass.model.meta.grammar.KlassParser.DataTypeDeclarationContext;
import cool.klass.model.meta.grammar.KlassParser.DataTypePropertyContext;
import cool.klass.model.meta.grammar.KlassParser.EnumerationBodyContext;
import cool.klass.model.meta.grammar.KlassParser.EnumerationDeclarationContext;
import cool.klass.model.meta.grammar.KlassParser.EnumerationLiteralContext;
import cool.klass.model.meta.grammar.KlassParser.EnumerationPrettyNameContext;
import cool.klass.model.meta.grammar.KlassParser.EnumerationPropertyContext;
import cool.klass.model.meta.grammar.KlassParser.EnumerationReferenceContext;
import cool.klass.model.meta.grammar.KlassParser.EscapedIdentifierContext;
import cool.klass.model.meta.grammar.KlassParser.ExpressionValueContext;
import cool.klass.model.meta.grammar.KlassParser.HeaderContext;
import cool.klass.model.meta.grammar.KlassParser.IdentifierContext;
import cool.klass.model.meta.grammar.KlassParser.KeywordValidAsIdentifierContext;
import cool.klass.model.meta.grammar.KlassParser.LiteralContext;
import cool.klass.model.meta.grammar.KlassParser.LiteralListContext;
import cool.klass.model.meta.grammar.KlassParser.MemberReferenceContext;
import cool.klass.model.meta.grammar.KlassParser.MultiplicityBodyContext;
import cool.klass.model.meta.grammar.KlassParser.MultiplicityContext;
import cool.klass.model.meta.grammar.KlassParser.NativeLiteralContext;
import cool.klass.model.meta.grammar.KlassParser.OperatorContext;
import cool.klass.model.meta.grammar.KlassParser.OptionalMarkerContext;
import cool.klass.model.meta.grammar.KlassParser.OrderByDeclarationContext;
import cool.klass.model.meta.grammar.KlassParser.OrderByDirectionContext;
import cool.klass.model.meta.grammar.KlassParser.OrderByListContext;
import cool.klass.model.meta.grammar.KlassParser.OrderByPropertyContext;
import cool.klass.model.meta.grammar.KlassParser.PackageDeclarationContext;
import cool.klass.model.meta.grammar.KlassParser.PackageNameContext;
import cool.klass.model.meta.grammar.KlassParser.ParameterDeclarationContext;
import cool.klass.model.meta.grammar.KlassParser.ParameterDeclarationListContext;
import cool.klass.model.meta.grammar.KlassParser.ParameterizedPropertyContext;
import cool.klass.model.meta.grammar.KlassParser.PrimitivePropertyContext;
import cool.klass.model.meta.grammar.KlassParser.PrimitiveTypeContext;
import cool.klass.model.meta.grammar.KlassParser.ProjectionBodyContext;
import cool.klass.model.meta.grammar.KlassParser.ProjectionDeclarationContext;
import cool.klass.model.meta.grammar.KlassParser.ProjectionMemberContext;
import cool.klass.model.meta.grammar.KlassParser.ProjectionMemberWithBodyContext;
import cool.klass.model.meta.grammar.KlassParser.ProjectionPrimitiveMemberContext;
import cool.klass.model.meta.grammar.KlassParser.ProjectionReferenceContext;
import cool.klass.model.meta.grammar.KlassParser.PropertyModifierContext;
import cool.klass.model.meta.grammar.KlassParser.QueryParameterListContext;
import cool.klass.model.meta.grammar.KlassParser.RelationshipContext;
import cool.klass.model.meta.grammar.KlassParser.ServiceCategoryModifierContext;
import cool.klass.model.meta.grammar.KlassParser.ServiceCriteriaDeclarationContext;
import cool.klass.model.meta.grammar.KlassParser.ServiceCriteriaKeywordContext;
import cool.klass.model.meta.grammar.KlassParser.ServiceDeclarationContext;
import cool.klass.model.meta.grammar.KlassParser.ServiceGroupDeclarationContext;
import cool.klass.model.meta.grammar.KlassParser.ServiceMultiplicityContext;
import cool.klass.model.meta.grammar.KlassParser.ServiceMultiplicityDeclarationContext;
import cool.klass.model.meta.grammar.KlassParser.ServiceProjectionDispatchContext;
import cool.klass.model.meta.grammar.KlassParser.ThisMemberReferenceContext;
import cool.klass.model.meta.grammar.KlassParser.TopLevelDeclarationContext;
import cool.klass.model.meta.grammar.KlassParser.TypeMemberReferenceContext;
import cool.klass.model.meta.grammar.KlassParser.TypeReferenceContext;
import cool.klass.model.meta.grammar.KlassParser.UrlContext;
import cool.klass.model.meta.grammar.KlassParser.UrlDeclarationContext;
import cool.klass.model.meta.grammar.KlassParser.UrlParameterDeclarationContext;
import cool.klass.model.meta.grammar.KlassParser.UrlPathSegmentContext;
import cool.klass.model.meta.grammar.KlassParser.VariableReferenceContext;
import cool.klass.model.meta.grammar.KlassParser.VerbContext;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.misc.Interval;
import org.antlr.v4.runtime.tree.ErrorNode;
import org.antlr.v4.runtime.tree.TerminalNode;
import org.eclipse.collections.api.list.MutableList;

public class ErrorUnderlineListener extends BaseErrorListener
{
    private static final Pattern TAB_PATTERN = Pattern.compile("\t");

    public ErrorUnderlineListener(CompilationUnit compilationUnit, MutableList<String> contextualStrings)
    {
        super(compilationUnit, contextualStrings);
    }

    @Override
    public void enterCompilationUnit(CompilationUnitContext ctx)
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName()
                                                + ".enterCompilationUnit() not implemented yet");
    }

    @Override
    public void enterPackageDeclaration(PackageDeclarationContext ctx)
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName()
                                                + ".enterPackageDeclaration() not implemented yet");
    }

    @Override
    public void enterPackageName(PackageNameContext ctx)
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName()
                                                + ".enterPackageName() not implemented yet");
    }

    @Override
    public void enterTopLevelDeclaration(TopLevelDeclarationContext ctx)
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName()
                                                + ".enterTopLevelDeclaration() not implemented yet");
    }

    @Override
    public void enterClassDeclaration(ClassDeclarationContext ctx)
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName()
                                                + ".enterClassDeclaration() not implemented yet");
    }

    @Override
    public void enterClassOrUser(ClassOrUserContext ctx)
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName()
                                                + ".enterClassOrUser() not implemented yet");
    }

    @Override
    public void enterClassServiceModifier(ClassServiceModifierContext ctx)
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName()
                                                + ".enterClassServiceModifier() not implemented yet");
    }

    @Override
    public void enterServiceCategoryModifier(ServiceCategoryModifierContext ctx)
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName()
                                                + ".enterServiceCategoryModifier() not implemented yet");
    }

    @Override
    public void enterClassBody(ClassBodyContext ctx)
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName()
                                                + ".enterClassBody() not implemented yet");
    }

    @Override
    public void enterEnumerationDeclaration(EnumerationDeclarationContext ctx)
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName()
                                                + ".enterEnumerationDeclaration() not implemented yet");
    }

    @Override
    public void enterEnumerationBody(EnumerationBodyContext ctx)
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName()
                                                + ".enterEnumerationBody() not implemented yet");
    }

    @Override
    public void enterEnumerationLiteral(EnumerationLiteralContext ctx)
    {
        this.addUnderlinedToken(ctx.getStart());
    }

    @Override
    public void enterEnumerationPrettyName(EnumerationPrettyNameContext ctx)
    {
        this.addUnderlinedToken(ctx.getStart());
    }

    @Override
    public void enterAssociationDeclaration(AssociationDeclarationContext ctx)
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName()
                                                + ".enterAssociationDeclaration() not implemented yet");
    }

    @Override
    public void enterAssociationBody(AssociationBodyContext ctx)
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName()
                                                + ".enterAssociationBody() not implemented yet");
    }

    @Override
    public void enterAssociationEnd(AssociationEndContext ctx)
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName()
                                                + ".enterAssociationEnd() not implemented yet");
    }

    @Override
    public void enterRelationship(RelationshipContext ctx)
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName()
                                                + ".enterRelationship() not implemented yet");
    }

    @Override
    public void enterProjectionDeclaration(ProjectionDeclarationContext ctx)
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName()
                                                + ".enterProjectionDeclaration() not implemented yet");
    }

    @Override
    public void enterProjectionBody(ProjectionBodyContext ctx)
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName()
                                                + ".enterProjectionBody() not implemented yet");
    }

    @Override
    public void enterProjectionMember(ProjectionMemberContext ctx)
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName()
                                                + ".enterProjectionMember() not implemented yet");
    }

    @Override
    public void enterProjectionMemberWithBody(ProjectionMemberWithBodyContext ctx)
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName()
                                                + ".enterProjectionMemberWithBody() not implemented yet");
    }

    @Override
    public void enterProjectionPrimitiveMember(ProjectionPrimitiveMemberContext ctx)
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName()
                                                + ".enterProjectionPrimitiveMember() not implemented yet");
    }

    @Override
    public void enterHeader(HeaderContext ctx)
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName() + ".enterHeader() not implemented yet");
    }

    @Override
    public void enterServiceGroupDeclaration(ServiceGroupDeclarationContext ctx)
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName()
                                                + ".enterServiceGroupDeclaration() not implemented yet");
    }

    @Override
    public void enterUrlDeclaration(UrlDeclarationContext ctx)
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName()
                                                + ".enterUrlDeclaration() not implemented yet");
    }

    @Override
    public void enterUrl(UrlContext ctx)
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName() + ".enterUrl() not implemented yet");
    }

    @Override
    public void enterUrlPathSegment(UrlPathSegmentContext ctx)
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName()
                                                + ".enterUrlPathSegment() not implemented yet");
    }

    @Override
    public void enterQueryParameterList(QueryParameterListContext ctx)
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName()
                                                + ".enterQueryParameterList() not implemented yet");
    }

    @Override
    public void enterUrlParameterDeclaration(UrlParameterDeclarationContext ctx)
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName()
                                                + ".enterUrlParameterDeclaration() not implemented yet");
    }

    @Override
    public void enterServiceDeclaration(ServiceDeclarationContext ctx)
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName()
                                                + ".enterServiceDeclaration() not implemented yet");
    }

    @Override
    public void enterServiceMultiplicityDeclaration(ServiceMultiplicityDeclarationContext ctx)
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName()
                                                + ".enterServiceMultiplicityDeclaration() not implemented yet");
    }

    @Override
    public void enterServiceMultiplicity(ServiceMultiplicityContext ctx)
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName()
                                                + ".enterServiceMultiplicity() not implemented yet");
    }

    @Override
    public void enterServiceCriteriaDeclaration(ServiceCriteriaDeclarationContext ctx)
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName()
                                                + ".enterServiceCriteriaDeclaration() not implemented yet");
    }

    @Override
    public void enterServiceCriteriaKeyword(ServiceCriteriaKeywordContext ctx)
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName()
                                                + ".enterServiceCriteriaKeyword() not implemented yet");
    }

    @Override
    public void enterServiceProjectionDispatch(ServiceProjectionDispatchContext ctx)
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName()
                                                + ".enterServiceProjectionDispatch() not implemented yet");
    }

    @Override
    public void enterVerb(VerbContext ctx)
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName() + ".enterVerb() not implemented yet");
    }

    @Override
    public void enterClassMember(ClassMemberContext ctx)
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName()
                                                + ".enterClassMember() not implemented yet");
    }

    @Override
    public void enterDataTypeProperty(DataTypePropertyContext ctx)
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName()
                                                + ".enterDataTypeProperty() not implemented yet");
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
    public void enterParameterizedProperty(ParameterizedPropertyContext ctx)
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName()
                                                + ".enterParameterizedProperty() not implemented yet");
    }

    @Override
    public void enterOptionalMarker(OptionalMarkerContext ctx)
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName()
                                                + ".enterOptionalMarker() not implemented yet");
    }

    @Override
    public void enterParameterDeclaration(ParameterDeclarationContext ctx)
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName()
                                                + ".enterParameterDeclaration() not implemented yet");
    }

    @Override
    public void enterDataTypeDeclaration(DataTypeDeclarationContext ctx)
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName()
                                                + ".enterDataTypeDeclaration() not implemented yet");
    }

    @Override
    public void enterParameterDeclarationList(ParameterDeclarationListContext ctx)
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName()
                                                + ".enterParameterDeclarationList() not implemented yet");
    }

    @Override
    public void enterArgumentList(ArgumentListContext ctx)
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName()
                                                + ".enterArgumentList() not implemented yet");
    }

    @Override
    public void enterArgument(ArgumentContext ctx)
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName()
                                                + ".enterArgument() not implemented yet");
    }

    @Override
    public void enterMultiplicity(MultiplicityContext ctx)
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName()
                                                + ".enterMultiplicity() not implemented yet");
    }

    @Override
    public void enterMultiplicityBody(MultiplicityBodyContext ctx)
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName()
                                                + ".enterMultiplicityBody() not implemented yet");
    }

    @Override
    public void enterPrimitiveType(PrimitiveTypeContext ctx)
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName()
                                                + ".enterPrimitiveType() not implemented yet");
    }

    @Override
    public void enterClassModifier(ClassModifierContext ctx)
    {
        ClassDeclarationContext classDeclarationContext = (ClassDeclarationContext) ctx.parent;

        Token offendingToken   = ctx.getStart();
        Token startToken       = classDeclarationContext.getStart();
        Token onePastStopToken = classDeclarationContext.classBody().getStart();
        Token endToken         = this.compilationUnit.getTokenStream().get(onePastStopToken.getTokenIndex() - 1);

        int startLine     = startToken.getLine();
        int endLine       = endToken.getLine();
        int offendingLine = offendingToken.getLine();

        for (int i = startLine; i <= endLine; i++)
        {
            String errorLine = this.compilationUnit.getLines()[i - 1];
            this.contextualStrings.add(errorLine);

            if (i == offendingLine)
            {
                String errorStringUnderlined = ErrorUnderlineListener.getErrorLineStringUnderlined(
                        offendingToken,
                        errorLine);
                this.contextualStrings.add(errorStringUnderlined);
            }
        }
    }

    @Override
    public void enterPropertyModifier(PropertyModifierContext ctx)
    {
        this.addUnderlinedToken(ctx.getStart());
    }

    @Override
    public void enterAssociationEndModifier(AssociationEndModifierContext ctx)
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName()
                                                + ".enterAssociationEndModifier() not implemented yet");
    }

    @Override
    public void enterOrderByDeclaration(OrderByDeclarationContext ctx)
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName()
                                                + ".enterOrderByDeclaration() not implemented yet");
    }

    @Override
    public void enterOrderByList(OrderByListContext ctx)
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName()
                                                + ".enterOrderByList() not implemented yet");
    }

    @Override
    public void enterOrderByProperty(OrderByPropertyContext ctx)
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName()
                                                + ".enterOrderByProperty() not implemented yet");
    }

    @Override
    public void enterOrderByDirection(OrderByDirectionContext ctx)
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName()
                                                + ".enterOrderByDirection() not implemented yet");
    }

    @Override
    public void enterCriteriaExpressionAnd(CriteriaExpressionAndContext ctx)
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName()
                                                + ".enterCriteriaExpressionAnd() not implemented yet");
    }

    @Override
    public void enterCriteriaNative(CriteriaNativeContext ctx)
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName()
                                                + ".enterCriteriaNative() not implemented yet");
    }

    @Override
    public void enterCriteriaExpressionGroup(CriteriaExpressionGroupContext ctx)
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName()
                                                + ".enterCriteriaExpressionGroup() not implemented yet");
    }

    @Override
    public void enterCriteriaOperator(CriteriaOperatorContext ctx)
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName()
                                                + ".enterCriteriaOperator() not implemented yet");
    }

    @Override
    public void enterCriteriaExpressionOr(CriteriaExpressionOrContext ctx)
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName()
                                                + ".enterCriteriaExpressionOr() not implemented yet");
    }

    @Override
    public void enterExpressionValue(ExpressionValueContext ctx)
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName()
                                                + ".enterExpressionValue() not implemented yet");
    }

    @Override
    public void enterLiteralList(LiteralListContext ctx)
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName()
                                                + ".enterLiteralList() not implemented yet");
    }

    @Override
    public void enterNativeLiteral(NativeLiteralContext ctx)
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName()
                                                + ".enterNativeLiteral() not implemented yet");
    }

    @Override
    public void enterOperator(OperatorContext ctx)
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName()
                                                + ".enterOperator() not implemented yet");
    }

    @Override
    public void enterClassType(ClassTypeContext ctx)
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName()
                                                + ".enterClassType() not implemented yet");
    }

    @Override
    public void enterDataType(DataTypeContext ctx)
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName()
                                                + ".enterDataType() not implemented yet");
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

    @Override
    public void enterMemberReference(MemberReferenceContext ctx)
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName()
                                                + ".enterMemberReference() not implemented yet");
    }

    @Override
    public void enterVariableReference(VariableReferenceContext ctx)
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName()
                                                + ".enterVariableReference() not implemented yet");
    }

    @Override
    public void enterThisMemberReference(ThisMemberReferenceContext ctx)
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName()
                                                + ".enterThisMemberReference() not implemented yet");
    }

    @Override
    public void enterTypeMemberReference(TypeMemberReferenceContext ctx)
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName()
                                                + ".enterTypeMemberReference() not implemented yet");
    }

    @Override
    public void enterTypeReference(TypeReferenceContext ctx)
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName()
                                                + ".enterTypeReference() not implemented yet");
    }

    @Override
    public void enterIdentifier(IdentifierContext ctx)
    {
        this.addUnderlinedToken(ctx.getStart());
    }

    @Override
    public void enterEscapedIdentifier(EscapedIdentifierContext ctx)
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName()
                                                + ".enterEscapedIdentifier() not implemented yet");
    }

    @Override
    public void enterKeywordValidAsIdentifier(KeywordValidAsIdentifierContext ctx)
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName()
                                                + ".enterKeywordValidAsIdentifier() not implemented yet");
    }

    @Override
    public void enterLiteral(LiteralContext ctx)
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName()
                                                + ".enterLiteral() not implemented yet");
    }

    @Override
    public void enterEveryRule(ParserRuleContext ctx)
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName()
                                                + ".enterEveryRule() not implemented yet");
    }

    @Override
    public void visitTerminal(TerminalNode node)
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName()
                                                + ".visitTerminal() not implemented yet");
    }

    @Override
    public void visitErrorNode(ErrorNode node)
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName()
                                                + ".visitErrorNode() not implemented yet");
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
        int    line      = offendingToken.getLine();
        String errorLine = this.compilationUnit.getLines()[line - 1];

        String errorStringUnderlined = ErrorUnderlineListener.getErrorLineStringUnderlined(offendingToken, errorLine);
        this.contextualStrings.add(errorLine);
        this.contextualStrings.add(errorStringUnderlined);
    }

    public static String getErrorLineStringUnderlined(Token offendingToken, String errorLine)
    {
        // replace tabs with single space so that charPositionInLine gives us the column to start underlining.
        String        errorLineWithoutTabs = TAB_PATTERN.matcher(errorLine).replaceAll(" ");
        String        formatString         = "%" + errorLineWithoutTabs.length() + 's';
        StringBuilder underLine            = new StringBuilder(String.format(formatString, ""));
        int           start                = offendingToken.getStartIndex();
        int           stop                 = offendingToken.getStopIndex();
        if (start >= 0 && stop >= 0)
        {
            for (int i = 0; i <= (stop - start); i++)
            {
                underLine.setCharAt(offendingToken.getCharPositionInLine() + i, '^');
            }
        }
        return underLine.toString();
    }

    @Override
    protected Interval getIntervalMinusOne(Token startToken, Token onePastStopToken)
    {
        int   startIndex    = startToken.getStartIndex();
        int   endTokenIndex = onePastStopToken.getTokenIndex();
        Token endToken      = this.compilationUnit.getTokenStream().get(endTokenIndex - 1);
        int   stopIndex     = endToken.getStopIndex();
        return new Interval(startIndex, stopIndex);
    }
}
