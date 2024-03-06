package cool.klass.model.converter.compiler.error;

import java.util.regex.Pattern;

import javax.annotation.Nonnull;

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
import cool.klass.model.meta.grammar.KlassParser.DataTypePropertyContext;
import cool.klass.model.meta.grammar.KlassParser.EnumerationBodyContext;
import cool.klass.model.meta.grammar.KlassParser.EnumerationDeclarationContext;
import cool.klass.model.meta.grammar.KlassParser.EnumerationLiteralContext;
import cool.klass.model.meta.grammar.KlassParser.EnumerationParameterDeclarationContext;
import cool.klass.model.meta.grammar.KlassParser.EnumerationPrettyNameContext;
import cool.klass.model.meta.grammar.KlassParser.EnumerationPropertyContext;
import cool.klass.model.meta.grammar.KlassParser.EnumerationReferenceContext;
import cool.klass.model.meta.grammar.KlassParser.EqualityOperatorContext;
import cool.klass.model.meta.grammar.KlassParser.ExpressionValueContext;
import cool.klass.model.meta.grammar.KlassParser.HeaderContext;
import cool.klass.model.meta.grammar.KlassParser.IdentifierContext;
import cool.klass.model.meta.grammar.KlassParser.InOperatorContext;
import cool.klass.model.meta.grammar.KlassParser.InequalityOperatorContext;
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
import cool.klass.model.meta.grammar.KlassParser.PrimitiveParameterDeclarationContext;
import cool.klass.model.meta.grammar.KlassParser.PrimitivePropertyContext;
import cool.klass.model.meta.grammar.KlassParser.PrimitiveTypeContext;
import cool.klass.model.meta.grammar.KlassParser.ProjectionAssociationEndContext;
import cool.klass.model.meta.grammar.KlassParser.ProjectionBodyContext;
import cool.klass.model.meta.grammar.KlassParser.ProjectionDeclarationContext;
import cool.klass.model.meta.grammar.KlassParser.ProjectionMemberContext;
import cool.klass.model.meta.grammar.KlassParser.ProjectionParameterizedPropertyContext;
import cool.klass.model.meta.grammar.KlassParser.ProjectionPrimitiveMemberContext;
import cool.klass.model.meta.grammar.KlassParser.ProjectionReferenceContext;
import cool.klass.model.meta.grammar.KlassParser.PropertyModifierContext;
import cool.klass.model.meta.grammar.KlassParser.QueryParameterListContext;
import cool.klass.model.meta.grammar.KlassParser.RelationshipContext;
import cool.klass.model.meta.grammar.KlassParser.ServiceCategoryModifierContext;
import cool.klass.model.meta.grammar.KlassParser.ServiceCriteriaDeclarationContext;
import cool.klass.model.meta.grammar.KlassParser.ServiceCriteriaKeywordContext;
import cool.klass.model.meta.grammar.KlassParser.ServiceDeclarationBodyContext;
import cool.klass.model.meta.grammar.KlassParser.ServiceDeclarationContext;
import cool.klass.model.meta.grammar.KlassParser.ServiceGroupDeclarationBodyContext;
import cool.klass.model.meta.grammar.KlassParser.ServiceGroupDeclarationContext;
import cool.klass.model.meta.grammar.KlassParser.ServiceMultiplicityContext;
import cool.klass.model.meta.grammar.KlassParser.ServiceMultiplicityDeclarationContext;
import cool.klass.model.meta.grammar.KlassParser.ServiceProjectionDispatchContext;
import cool.klass.model.meta.grammar.KlassParser.StringOperatorContext;
import cool.klass.model.meta.grammar.KlassParser.ThisMemberReferenceContext;
import cool.klass.model.meta.grammar.KlassParser.TopLevelDeclarationContext;
import cool.klass.model.meta.grammar.KlassParser.TypeMemberReferenceContext;
import cool.klass.model.meta.grammar.KlassParser.UrlConstantContext;
import cool.klass.model.meta.grammar.KlassParser.UrlContext;
import cool.klass.model.meta.grammar.KlassParser.UrlDeclarationContext;
import cool.klass.model.meta.grammar.KlassParser.UrlParameterDeclarationContext;
import cool.klass.model.meta.grammar.KlassParser.UrlPathSegmentContext;
import cool.klass.model.meta.grammar.KlassParser.VariableReferenceContext;
import cool.klass.model.meta.grammar.KlassParser.VerbContext;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.tree.ErrorNode;
import org.antlr.v4.runtime.tree.TerminalNode;
import org.eclipse.collections.api.list.MutableList;

public class ErrorUnderlineListener extends BaseErrorListener
{
    private static final Pattern NEWLINE_PATTERN = Pattern.compile("\\r?\\n");

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
    public void enterProjectionPrimitiveMember(ProjectionPrimitiveMemberContext ctx)
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName()
                + ".enterProjectionPrimitiveMember() not implemented yet");
    }

    @Override
    public void enterProjectionAssociationEnd(ProjectionAssociationEndContext ctx)
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName()
                + ".enterProjectionAssociationEnd() not implemented yet");
    }

    @Override
    public void enterProjectionParameterizedProperty(ProjectionParameterizedPropertyContext ctx)
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName()
                + ".enterProjectionParameterizedProperty() not implemented yet");
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
    public void enterServiceGroupDeclarationBody(ServiceGroupDeclarationBodyContext ctx)
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName()
                + ".enterServiceGroupDeclarationBody() not implemented yet");
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
    public void enterUrlConstant(UrlConstantContext ctx)
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName()
                + ".enterUrlConstant() not implemented yet");
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
    public void enterServiceDeclarationBody(ServiceDeclarationBodyContext ctx)
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName()
                + ".enterServiceDeclarationBody() not implemented yet");
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
    public void enterServiceCriteriaKeyword(@Nonnull ServiceCriteriaKeywordContext ctx)
    {
        this.addUnderlinedToken(ctx.getStart());
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
    public void enterPrimitiveParameterDeclaration(PrimitiveParameterDeclarationContext ctx)
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName()
                + ".enterPrimitiveParameterDeclaration() not implemented yet");
    }

    @Override
    public void enterEnumerationParameterDeclaration(EnumerationParameterDeclarationContext ctx)
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName()
                + ".enterEnumerationParameterDeclaration() not implemented yet");
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
    public void enterClassModifier(@Nonnull ClassModifierContext ctx)
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
                        offendingToken);
                this.contextualStrings.add(errorStringUnderlined);
            }
        }
    }

    @Override
    public void enterPropertyModifier(@Nonnull PropertyModifierContext ctx)
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
    public void enterCriteriaOperator(@Nonnull CriteriaOperatorContext ctx)
    {
        this.addUnderlinedRange(ctx);
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
    public void enterEqualityOperator(EqualityOperatorContext ctx)
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName()
                + ".enterEqualityOperator() not implemented yet");
    }

    @Override
    public void enterInequalityOperator(InequalityOperatorContext ctx)
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName()
                + ".enterInequalityOperator() not implemented yet");
    }

    @Override
    public void enterInOperator(InOperatorContext ctx)
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName()
                + ".enterInOperator() not implemented yet");
    }

    @Override
    public void enterStringOperator(StringOperatorContext ctx)
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName()
                + ".enterStringOperator() not implemented yet");
    }

    @Override
    public void enterClassType(ClassTypeContext ctx)
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName()
                + ".enterClassType() not implemented yet");
    }

    @Override
    public void enterClassReference(@Nonnull ClassReferenceContext ctx)
    {
        this.addUnderlinedToken(ctx.getStart());
    }

    @Override
    public void enterEnumerationReference(@Nonnull EnumerationReferenceContext ctx)
    {
        this.addUnderlinedToken(ctx.getStart());
    }

    @Override
    public void enterProjectionReference(@Nonnull ProjectionReferenceContext ctx)
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
    public void enterVariableReference(@Nonnull VariableReferenceContext ctx)
    {
        ServiceCriteriaDeclarationContext serviceCriteriaDeclarationContext = this.getParentOfType(
                ctx,
                ServiceCriteriaDeclarationContext.class);
        if (serviceCriteriaDeclarationContext == null)
        {
            throw new AssertionError();
        }

        this.addUnderlinedToken(serviceCriteriaDeclarationContext, ctx.getStart());
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
    public void enterIdentifier(@Nonnull IdentifierContext ctx)
    {
        this.addUnderlinedToken(ctx.getStart());
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

    private void addUnderlinedToken(@Nonnull ParserRuleContext ctx, @Nonnull Token offendingToken)
    {
        Token    startToken     = ctx.getStart();
        Token    stopToken      = ctx.getStop();
        int      startLine      = startToken.getLine();
        int      stopLine       = stopToken.getLine();
        String   sourceCodeText = startToken.getInputStream().toString();
        String[] lines          = NEWLINE_PATTERN.split(sourceCodeText);

        for (int lineNumber = startLine; lineNumber <= stopLine; lineNumber++)
        {
            if (lineNumber == offendingToken.getLine())
            {
                this.addUnderlinedContext(offendingToken, offendingToken, offendingToken.getLine());
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
