package cool.klass.model.converter.compiler.error;

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
import org.antlr.v4.runtime.tree.ErrorNode;
import org.antlr.v4.runtime.tree.TerminalNode;
import org.eclipse.collections.api.list.MutableList;

public class ErrorContextListener extends BaseErrorListener
{
    public ErrorContextListener(CompilationUnit compilationUnit, MutableList<String> contextualStrings)
    {
        super(compilationUnit, contextualStrings);
    }

    @Override
    public void enterCompilationUnit(@Nonnull CompilationUnitContext ctx)
    {
        this.addTextInclusive("", ctx.getStart(), ctx.packageDeclaration().getStop());
    }

    @Override
    public void enterPackageDeclaration(PackageDeclarationContext ctx)
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName()
                + ".enterPackageDeclaration() not implemented yet");
    }

    @Override
    public void exitPackageDeclaration(PackageDeclarationContext ctx)
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName()
                + ".exitPackageDeclaration() not implemented yet");
    }

    @Override
    public void enterPackageName(PackageNameContext ctx)
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName()
                + ".enterPackageName() not implemented yet");
    }

    @Override
    public void exitPackageName(PackageNameContext ctx)
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName()
                + ".exitPackageName() not implemented yet");
    }

    @Override
    public void enterTopLevelDeclaration(TopLevelDeclarationContext ctx)
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName()
                + ".enterTopLevelDeclaration() not implemented yet");
    }

    @Override
    public void exitTopLevelDeclaration(TopLevelDeclarationContext ctx)
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName()
                + ".exitTopLevelDeclaration() not implemented yet");
    }

    @Override
    public void enterClassDeclaration(@Nonnull ClassDeclarationContext ctx)
    {
        this.addTextInclusive("", ctx.getStart(), ctx.classBody().getStart());
    }

    @Override
    public void exitClassDeclaration(ClassDeclarationContext ctx)
    {
        this.contextualStrings.add("}");
    }

    @Override
    public void enterClassOrUser(ClassOrUserContext ctx)
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName()
                + ".enterClassOrUser() not implemented yet");
    }

    @Override
    public void exitClassOrUser(ClassOrUserContext ctx)
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName()
                + ".exitClassOrUser() not implemented yet");
    }

    @Override
    public void enterClassServiceModifier(ClassServiceModifierContext ctx)
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName()
                + ".enterClassServiceModifier() not implemented yet");
    }

    @Override
    public void exitClassServiceModifier(ClassServiceModifierContext ctx)
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName()
                + ".exitClassServiceModifier() not implemented yet");
    }

    @Override
    public void enterServiceCategoryModifier(ServiceCategoryModifierContext ctx)
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName()
                + ".enterServiceCategoryModifier() not implemented yet");
    }

    @Override
    public void exitServiceCategoryModifier(ServiceCategoryModifierContext ctx)
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName()
                + ".exitServiceCategoryModifier() not implemented yet");
    }

    @Override
    public void enterClassBody(ClassBodyContext ctx)
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName()
                + ".enterClassBody() not implemented yet");
    }

    @Override
    public void exitClassBody(ClassBodyContext ctx)
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName()
                + ".exitClassBody() not implemented yet");
    }

    @Override
    public void enterEnumerationDeclaration(@Nonnull EnumerationDeclarationContext ctx)
    {
        this.addTextInclusive("", ctx.getStart(), ctx.enumerationBody().getStart());
    }

    @Override
    public void exitEnumerationDeclaration(EnumerationDeclarationContext ctx)
    {
        this.contextualStrings.add("}");
    }

    @Override
    public void enterEnumerationBody(EnumerationBodyContext ctx)
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName()
                + ".enterEnumerationBody() not implemented yet");
    }

    @Override
    public void exitEnumerationBody(EnumerationBodyContext ctx)
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName()
                + ".exitEnumerationBody() not implemented yet");
    }

    @Override
    public void enterEnumerationLiteral(EnumerationLiteralContext ctx)
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName()
                + ".enterEnumerationLiteral() not implemented yet");
    }

    @Override
    public void exitEnumerationLiteral(EnumerationLiteralContext ctx)
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName()
                + ".exitEnumerationLiteral() not implemented yet");
    }

    @Override
    public void enterEnumerationPrettyName(EnumerationPrettyNameContext ctx)
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName()
                + ".enterEnumerationPrettyName() not implemented yet");
    }

    @Override
    public void exitEnumerationPrettyName(EnumerationPrettyNameContext ctx)
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName()
                + ".exitEnumerationPrettyName() not implemented yet");
    }

    @Override
    public void enterAssociationDeclaration(@Nonnull AssociationDeclarationContext ctx)
    {
        this.addTextInclusive("", ctx.getStart(), ctx.associationBody().getStart());
    }

    @Override
    public void exitAssociationDeclaration(AssociationDeclarationContext ctx)
    {
        this.contextualStrings.add("}");
    }

    @Override
    public void enterAssociationBody(AssociationBodyContext ctx)
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName()
                + ".enterAssociationBody() not implemented yet");
    }

    @Override
    public void exitAssociationBody(AssociationBodyContext ctx)
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName()
                + ".exitAssociationBody() not implemented yet");
    }

    @Override
    public void enterAssociationEnd(AssociationEndContext ctx)
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName()
                + ".enterAssociationEnd() not implemented yet");
    }

    @Override
    public void exitAssociationEnd(AssociationEndContext ctx)
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName()
                + ".exitAssociationEnd() not implemented yet");
    }

    @Override
    public void enterRelationship(RelationshipContext ctx)
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName()
                + ".enterRelationship() not implemented yet");
    }

    @Override
    public void exitRelationship(RelationshipContext ctx)
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName()
                + ".exitRelationship() not implemented yet");
    }

    @Override
    public void enterProjectionDeclaration(@Nonnull ProjectionDeclarationContext ctx)
    {
        this.addTextInclusive("", ctx.getStart(), ctx.projectionBody().getStart());
    }

    @Override
    public void exitProjectionDeclaration(ProjectionDeclarationContext ctx)
    {
        this.contextualStrings.add("}");
    }

    @Override
    public void enterProjectionBody(ProjectionBodyContext ctx)
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName()
                + ".enterProjectionBody() not implemented yet");
    }

    @Override
    public void exitProjectionBody(ProjectionBodyContext ctx)
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName()
                + ".exitProjectionBody() not implemented yet");
    }

    @Override
    public void enterProjectionMember(ProjectionMemberContext ctx)
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName()
                + ".enterProjectionMember() not implemented yet");
    }

    @Override
    public void exitProjectionMember(ProjectionMemberContext ctx)
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName()
                + ".exitProjectionMember() not implemented yet");
    }

    @Override
    public void enterProjectionPrimitiveMember(ProjectionPrimitiveMemberContext ctx)
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName()
                + ".enterProjectionPrimitiveMember() not implemented yet");
    }

    @Override
    public void exitProjectionPrimitiveMember(ProjectionPrimitiveMemberContext ctx)
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName()
                + ".exitProjectionPrimitiveMember() not implemented yet");
    }

    @Override
    public void enterProjectionAssociationEnd(ProjectionAssociationEndContext ctx)
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName()
                + ".enterProjectionAssociationEnd() not implemented yet");
    }

    @Override
    public void exitProjectionAssociationEnd(ProjectionAssociationEndContext ctx)
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName()
                + ".exitProjectionAssociationEnd() not implemented yet");
    }

    @Override
    public void enterProjectionParameterizedProperty(ProjectionParameterizedPropertyContext ctx)
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName()
                + ".enterProjectionParameterizedProperty() not implemented yet");
    }

    @Override
    public void exitProjectionParameterizedProperty(ProjectionParameterizedPropertyContext ctx)
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName()
                + ".exitProjectionParameterizedProperty() not implemented yet");
    }

    @Override
    public void enterHeader(HeaderContext ctx)
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName() + ".enterHeader() not implemented yet");
    }

    @Override
    public void exitHeader(HeaderContext ctx)
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName() + ".exitHeader() not implemented yet");
    }

    @Override
    public void enterServiceGroupDeclaration(@Nonnull ServiceGroupDeclarationContext ctx)
    {
        this.addTextInclusive("", ctx.getStart(), ctx.serviceGroupDeclarationBody().getStart());
    }

    @Override
    public void exitServiceGroupDeclaration(ServiceGroupDeclarationContext ctx)
    {
        this.contextualStrings.add("}");
    }

    @Override
    public void enterServiceGroupDeclarationBody(ServiceGroupDeclarationBodyContext ctx)
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName()
                + ".enterServiceGroupDeclarationBody() not implemented yet");
    }

    @Override
    public void exitServiceGroupDeclarationBody(ServiceGroupDeclarationBodyContext ctx)
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName()
                + ".exitServiceGroupDeclarationBody() not implemented yet");
    }

    @Override
    public void enterUrlDeclaration(@Nonnull UrlDeclarationContext ctx)
    {
        this.addTextInclusive("    ", ctx.getStart(), ctx.url().getStop());
    }

    @Override
    public void enterUrl(UrlContext ctx)
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName() + ".enterUrl() not implemented yet");
    }

    @Override
    public void exitUrl(UrlContext ctx)
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName() + ".exitUrl() not implemented yet");
    }

    @Override
    public void enterUrlPathSegment(UrlPathSegmentContext ctx)
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName()
                + ".enterUrlPathSegment() not implemented yet");
    }

    @Override
    public void exitUrlPathSegment(UrlPathSegmentContext ctx)
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName()
                + ".exitUrlPathSegment() not implemented yet");
    }

    @Override
    public void enterUrlConstant(UrlConstantContext ctx)
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName()
                + ".enterUrlConstant() not implemented yet");
    }

    @Override
    public void exitUrlConstant(UrlConstantContext ctx)
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName()
                + ".exitUrlConstant() not implemented yet");
    }

    @Override
    public void enterQueryParameterList(QueryParameterListContext ctx)
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName()
                + ".enterQueryParameterList() not implemented yet");
    }

    @Override
    public void exitQueryParameterList(QueryParameterListContext ctx)
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName()
                + ".exitQueryParameterList() not implemented yet");
    }

    @Override
    public void enterUrlParameterDeclaration(UrlParameterDeclarationContext ctx)
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName()
                + ".enterUrlParameterDeclaration() not implemented yet");
    }

    @Override
    public void exitUrlParameterDeclaration(UrlParameterDeclarationContext ctx)
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName()
                + ".exitUrlParameterDeclaration() not implemented yet");
    }

    @Override
    public void enterServiceDeclaration(@Nonnull ServiceDeclarationContext ctx)
    {
        this.addTextInclusive("        ", ctx.getStart(), ctx.serviceDeclarationBody().getStart());
    }

    @Override
    public void exitServiceDeclaration(ServiceDeclarationContext ctx)
    {
        this.contextualStrings.add("        }");
    }

    @Override
    public void enterServiceDeclarationBody(ServiceDeclarationBodyContext ctx)
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName()
                + ".enterServiceDeclarationBody() not implemented yet");
    }

    @Override
    public void exitServiceDeclarationBody(ServiceDeclarationBodyContext ctx)
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName()
                + ".exitServiceDeclarationBody() not implemented yet");
    }

    @Override
    public void enterServiceMultiplicityDeclaration(ServiceMultiplicityDeclarationContext ctx)
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName()
                + ".enterServiceMultiplicityDeclaration() not implemented yet");
    }

    @Override
    public void exitServiceMultiplicityDeclaration(ServiceMultiplicityDeclarationContext ctx)
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName()
                + ".exitServiceMultiplicityDeclaration() not implemented yet");
    }

    @Override
    public void enterServiceMultiplicity(ServiceMultiplicityContext ctx)
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName()
                + ".enterServiceMultiplicity() not implemented yet");
    }

    @Override
    public void exitServiceMultiplicity(ServiceMultiplicityContext ctx)
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName()
                + ".exitServiceMultiplicity() not implemented yet");
    }

    @Override
    public void enterServiceCriteriaDeclaration(ServiceCriteriaDeclarationContext ctx)
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName()
                + ".enterServiceCriteriaDeclaration() not implemented yet");
    }

    @Override
    public void exitServiceCriteriaDeclaration(ServiceCriteriaDeclarationContext ctx)
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName()
                + ".exitServiceCriteriaDeclaration() not implemented yet");
    }

    @Override
    public void enterServiceCriteriaKeyword(ServiceCriteriaKeywordContext ctx)
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName()
                + ".enterServiceCriteriaKeyword() not implemented yet");
    }

    @Override
    public void exitServiceCriteriaKeyword(ServiceCriteriaKeywordContext ctx)
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName()
                + ".exitServiceCriteriaKeyword() not implemented yet");
    }

    @Override
    public void enterServiceProjectionDispatch(ServiceProjectionDispatchContext ctx)
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName()
                + ".enterServiceProjectionDispatch() not implemented yet");
    }

    @Override
    public void exitServiceProjectionDispatch(ServiceProjectionDispatchContext ctx)
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName()
                + ".exitServiceProjectionDispatch() not implemented yet");
    }

    @Override
    public void enterVerb(VerbContext ctx)
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName() + ".enterVerb() not implemented yet");
    }

    @Override
    public void exitVerb(VerbContext ctx)
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName() + ".exitVerb() not implemented yet");
    }

    @Override
    public void enterClassMember(ClassMemberContext ctx)
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName()
                + ".enterClassMember() not implemented yet");
    }

    @Override
    public void exitClassMember(ClassMemberContext ctx)
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName()
                + ".exitClassMember() not implemented yet");
    }

    @Override
    public void enterDataTypeProperty(DataTypePropertyContext ctx)
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName()
                + ".enterDataTypeProperty() not implemented yet");
    }

    @Override
    public void exitDataTypeProperty(DataTypePropertyContext ctx)
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName()
                + ".exitDataTypeProperty() not implemented yet");
    }

    @Override
    public void enterPrimitiveProperty(PrimitivePropertyContext ctx)
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName()
                + ".enterPrimitiveProperty() not implemented yet");
    }

    @Override
    public void exitPrimitiveProperty(PrimitivePropertyContext ctx)
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName()
                + ".exitPrimitiveProperty() not implemented yet");
    }

    @Override
    public void enterEnumerationProperty(EnumerationPropertyContext ctx)
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName()
                + ".enterEnumerationProperty() not implemented yet");
    }

    @Override
    public void exitEnumerationProperty(EnumerationPropertyContext ctx)
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName()
                + ".exitEnumerationProperty() not implemented yet");
    }

    @Override
    public void enterParameterizedProperty(ParameterizedPropertyContext ctx)
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName()
                + ".enterParameterizedProperty() not implemented yet");
    }

    @Override
    public void exitParameterizedProperty(ParameterizedPropertyContext ctx)
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName()
                + ".exitParameterizedProperty() not implemented yet");
    }

    @Override
    public void enterOptionalMarker(OptionalMarkerContext ctx)
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName()
                + ".enterOptionalMarker() not implemented yet");
    }

    @Override
    public void exitOptionalMarker(OptionalMarkerContext ctx)
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName()
                + ".exitOptionalMarker() not implemented yet");
    }

    @Override
    public void enterParameterDeclaration(ParameterDeclarationContext ctx)
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName()
                + ".enterParameterDeclaration() not implemented yet");
    }

    @Override
    public void exitParameterDeclaration(ParameterDeclarationContext ctx)
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName()
                + ".exitParameterDeclaration() not implemented yet");
    }

    @Override
    public void enterPrimitiveParameterDeclaration(PrimitiveParameterDeclarationContext ctx)
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName()
                + ".enterPrimitiveParameterDeclaration() not implemented yet");
    }

    @Override
    public void exitPrimitiveParameterDeclaration(PrimitiveParameterDeclarationContext ctx)
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName()
                + ".exitPrimitiveParameterDeclaration() not implemented yet");
    }

    @Override
    public void enterEnumerationParameterDeclaration(EnumerationParameterDeclarationContext ctx)
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName()
                + ".enterEnumerationParameterDeclaration() not implemented yet");
    }

    @Override
    public void exitEnumerationParameterDeclaration(EnumerationParameterDeclarationContext ctx)
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName()
                + ".exitEnumerationParameterDeclaration() not implemented yet");
    }

    @Override
    public void enterParameterDeclarationList(ParameterDeclarationListContext ctx)
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName()
                + ".enterParameterDeclarationList() not implemented yet");
    }

    @Override
    public void exitParameterDeclarationList(ParameterDeclarationListContext ctx)
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName()
                + ".exitParameterDeclarationList() not implemented yet");
    }

    @Override
    public void enterArgumentList(ArgumentListContext ctx)
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName()
                + ".enterArgumentList() not implemented yet");
    }

    @Override
    public void exitArgumentList(ArgumentListContext ctx)
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName()
                + ".exitArgumentList() not implemented yet");
    }

    @Override
    public void enterArgument(ArgumentContext ctx)
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName()
                + ".enterArgument() not implemented yet");
    }

    @Override
    public void exitArgument(ArgumentContext ctx)
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName()
                + ".exitArgument() not implemented yet");
    }

    @Override
    public void enterMultiplicity(MultiplicityContext ctx)
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName()
                + ".enterMultiplicity() not implemented yet");
    }

    @Override
    public void exitMultiplicity(MultiplicityContext ctx)
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName()
                + ".exitMultiplicity() not implemented yet");
    }

    @Override
    public void enterMultiplicityBody(MultiplicityBodyContext ctx)
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName()
                + ".enterMultiplicityBody() not implemented yet");
    }

    @Override
    public void exitMultiplicityBody(MultiplicityBodyContext ctx)
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName()
                + ".exitMultiplicityBody() not implemented yet");
    }

    @Override
    public void enterPrimitiveType(PrimitiveTypeContext ctx)
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName()
                + ".enterPrimitiveType() not implemented yet");
    }

    @Override
    public void exitPrimitiveType(PrimitiveTypeContext ctx)
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName()
                + ".exitPrimitiveType() not implemented yet");
    }

    @Override
    public void enterClassModifier(ClassModifierContext ctx)
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName()
                + ".enterClassModifier() not implemented yet");
    }

    @Override
    public void exitClassModifier(ClassModifierContext ctx)
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName()
                + ".exitClassModifier() not implemented yet");
    }

    @Override
    public void enterPropertyModifier(PropertyModifierContext ctx)
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName()
                + ".enterPropertyModifier() not implemented yet");
    }

    @Override
    public void exitPropertyModifier(PropertyModifierContext ctx)
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName()
                + ".exitPropertyModifier() not implemented yet");
    }

    @Override
    public void enterAssociationEndModifier(AssociationEndModifierContext ctx)
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName()
                + ".enterAssociationEndModifier() not implemented yet");
    }

    @Override
    public void exitAssociationEndModifier(AssociationEndModifierContext ctx)
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName()
                + ".exitAssociationEndModifier() not implemented yet");
    }

    @Override
    public void enterOrderByDeclaration(OrderByDeclarationContext ctx)
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName()
                + ".enterOrderByDeclaration() not implemented yet");
    }

    @Override
    public void exitOrderByDeclaration(OrderByDeclarationContext ctx)
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName()
                + ".exitOrderByDeclaration() not implemented yet");
    }

    @Override
    public void enterOrderByList(OrderByListContext ctx)
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName()
                + ".enterOrderByList() not implemented yet");
    }

    @Override
    public void exitOrderByList(OrderByListContext ctx)
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName()
                + ".exitOrderByList() not implemented yet");
    }

    @Override
    public void enterOrderByProperty(OrderByPropertyContext ctx)
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName()
                + ".enterOrderByProperty() not implemented yet");
    }

    @Override
    public void exitOrderByProperty(OrderByPropertyContext ctx)
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName()
                + ".exitOrderByProperty() not implemented yet");
    }

    @Override
    public void enterOrderByDirection(OrderByDirectionContext ctx)
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName()
                + ".enterOrderByDirection() not implemented yet");
    }

    @Override
    public void exitOrderByDirection(OrderByDirectionContext ctx)
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName()
                + ".exitOrderByDirection() not implemented yet");
    }

    @Override
    public void enterCriteriaExpressionAnd(CriteriaExpressionAndContext ctx)
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName()
                + ".enterCriteriaExpressionAnd() not implemented yet");
    }

    @Override
    public void exitCriteriaExpressionAnd(CriteriaExpressionAndContext ctx)
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName()
                + ".exitCriteriaExpressionAnd() not implemented yet");
    }

    @Override
    public void enterCriteriaNative(CriteriaNativeContext ctx)
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName()
                + ".enterCriteriaNative() not implemented yet");
    }

    @Override
    public void exitCriteriaNative(CriteriaNativeContext ctx)
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName()
                + ".exitCriteriaNative() not implemented yet");
    }

    @Override
    public void enterCriteriaExpressionGroup(CriteriaExpressionGroupContext ctx)
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName()
                + ".enterCriteriaExpressionGroup() not implemented yet");
    }

    @Override
    public void exitCriteriaExpressionGroup(CriteriaExpressionGroupContext ctx)
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName()
                + ".exitCriteriaExpressionGroup() not implemented yet");
    }

    @Override
    public void enterCriteriaOperator(CriteriaOperatorContext ctx)
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName()
                + ".enterCriteriaOperator() not implemented yet");
    }

    @Override
    public void exitCriteriaOperator(CriteriaOperatorContext ctx)
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName()
                + ".exitCriteriaOperator() not implemented yet");
    }

    @Override
    public void enterCriteriaExpressionOr(CriteriaExpressionOrContext ctx)
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName()
                + ".enterCriteriaExpressionOr() not implemented yet");
    }

    @Override
    public void exitCriteriaExpressionOr(CriteriaExpressionOrContext ctx)
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName()
                + ".exitCriteriaExpressionOr() not implemented yet");
    }

    @Override
    public void enterExpressionValue(ExpressionValueContext ctx)
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName()
                + ".enterExpressionValue() not implemented yet");
    }

    @Override
    public void exitExpressionValue(ExpressionValueContext ctx)
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName()
                + ".exitExpressionValue() not implemented yet");
    }

    @Override
    public void enterLiteralList(LiteralListContext ctx)
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName()
                + ".enterLiteralList() not implemented yet");
    }

    @Override
    public void exitLiteralList(LiteralListContext ctx)
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName()
                + ".exitLiteralList() not implemented yet");
    }

    @Override
    public void enterNativeLiteral(NativeLiteralContext ctx)
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName()
                + ".enterNativeLiteral() not implemented yet");
    }

    @Override
    public void exitNativeLiteral(NativeLiteralContext ctx)
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName()
                + ".exitNativeLiteral() not implemented yet");
    }

    @Override
    public void enterOperator(OperatorContext ctx)
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName()
                + ".enterOperator() not implemented yet");
    }

    @Override
    public void exitOperator(OperatorContext ctx)
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName()
                + ".exitOperator() not implemented yet");
    }

    @Override
    public void enterEqualityOperator(EqualityOperatorContext ctx)
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName()
                + ".enterEqualityOperator() not implemented yet");
    }

    @Override
    public void exitEqualityOperator(EqualityOperatorContext ctx)
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName()
                + ".exitEqualityOperator() not implemented yet");
    }

    @Override
    public void enterInequalityOperator(InequalityOperatorContext ctx)
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName()
                + ".enterInequalityOperator() not implemented yet");
    }

    @Override
    public void exitInequalityOperator(InequalityOperatorContext ctx)
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName()
                + ".exitInequalityOperator() not implemented yet");
    }

    @Override
    public void enterInOperator(InOperatorContext ctx)
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName()
                + ".enterInOperator() not implemented yet");
    }

    @Override
    public void exitInOperator(InOperatorContext ctx)
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName()
                + ".exitInOperator() not implemented yet");
    }

    @Override
    public void enterStringOperator(StringOperatorContext ctx)
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName()
                + ".enterStringOperator() not implemented yet");
    }

    @Override
    public void exitStringOperator(StringOperatorContext ctx)
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName()
                + ".exitStringOperator() not implemented yet");
    }

    @Override
    public void enterClassType(ClassTypeContext ctx)
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName()
                + ".enterClassType() not implemented yet");
    }

    @Override
    public void exitClassType(ClassTypeContext ctx)
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName()
                + ".exitClassType() not implemented yet");
    }

    @Override
    public void enterClassReference(ClassReferenceContext ctx)
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName()
                + ".enterClassReference() not implemented yet");
    }

    @Override
    public void exitClassReference(ClassReferenceContext ctx)
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName()
                + ".exitClassReference() not implemented yet");
    }

    @Override
    public void enterEnumerationReference(EnumerationReferenceContext ctx)
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName()
                + ".enterEnumerationReference() not implemented yet");
    }

    @Override
    public void exitEnumerationReference(EnumerationReferenceContext ctx)
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName()
                + ".exitEnumerationReference() not implemented yet");
    }

    @Override
    public void enterProjectionReference(ProjectionReferenceContext ctx)
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName()
                + ".enterProjectionReference() not implemented yet");
    }

    @Override
    public void exitProjectionReference(ProjectionReferenceContext ctx)
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName()
                + ".exitProjectionReference() not implemented yet");
    }

    @Override
    public void enterMemberReference(MemberReferenceContext ctx)
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName()
                + ".enterMemberReference() not implemented yet");
    }

    @Override
    public void exitMemberReference(MemberReferenceContext ctx)
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName()
                + ".exitMemberReference() not implemented yet");
    }

    @Override
    public void enterVariableReference(VariableReferenceContext ctx)
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName()
                + ".enterVariableReference() not implemented yet");
    }

    @Override
    public void exitVariableReference(VariableReferenceContext ctx)
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName()
                + ".exitVariableReference() not implemented yet");
    }

    @Override
    public void enterThisMemberReference(ThisMemberReferenceContext ctx)
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName()
                + ".enterThisMemberReference() not implemented yet");
    }

    @Override
    public void exitThisMemberReference(ThisMemberReferenceContext ctx)
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName()
                + ".exitThisMemberReference() not implemented yet");
    }

    @Override
    public void enterTypeMemberReference(TypeMemberReferenceContext ctx)
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName()
                + ".enterTypeMemberReference() not implemented yet");
    }

    @Override
    public void exitTypeMemberReference(TypeMemberReferenceContext ctx)
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName()
                + ".exitTypeMemberReference() not implemented yet");
    }

    @Override
    public void enterIdentifier(IdentifierContext ctx)
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName()
                + ".enterIdentifier() not implemented yet");
    }

    @Override
    public void exitIdentifier(IdentifierContext ctx)
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName()
                + ".exitIdentifier() not implemented yet");
    }

    @Override
    public void enterKeywordValidAsIdentifier(KeywordValidAsIdentifierContext ctx)
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName()
                + ".enterKeywordValidAsIdentifier() not implemented yet");
    }

    @Override
    public void exitKeywordValidAsIdentifier(KeywordValidAsIdentifierContext ctx)
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName()
                + ".exitKeywordValidAsIdentifier() not implemented yet");
    }

    @Override
    public void enterLiteral(LiteralContext ctx)
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName()
                + ".enterLiteral() not implemented yet");
    }

    @Override
    public void exitLiteral(LiteralContext ctx)
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName() + ".exitLiteral() not implemented yet");
    }

    @Override
    public void enterEveryRule(ParserRuleContext ctx)
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName()
                + ".enterEveryRule() not implemented yet");
    }

    @Override
    public void exitEveryRule(ParserRuleContext ctx)
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName()
                + ".exitEveryRule() not implemented yet");
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
}
