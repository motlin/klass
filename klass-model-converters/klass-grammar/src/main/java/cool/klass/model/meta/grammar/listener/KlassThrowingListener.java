package cool.klass.model.meta.grammar.listener;

import cool.klass.model.meta.grammar.KlassListener;
import cool.klass.model.meta.grammar.KlassParser.AbstractDeclarationContext;
import cool.klass.model.meta.grammar.KlassParser.ArgumentContext;
import cool.klass.model.meta.grammar.KlassParser.ArgumentListContext;
import cool.klass.model.meta.grammar.KlassParser.AssociationBodyContext;
import cool.klass.model.meta.grammar.KlassParser.AssociationDeclarationContext;
import cool.klass.model.meta.grammar.KlassParser.AssociationEndContext;
import cool.klass.model.meta.grammar.KlassParser.AssociationEndModifierContext;
import cool.klass.model.meta.grammar.KlassParser.AssociationEndReferenceContext;
import cool.klass.model.meta.grammar.KlassParser.AssociationEndSignatureContext;
import cool.klass.model.meta.grammar.KlassParser.BooleanLiteralContext;
import cool.klass.model.meta.grammar.KlassParser.CharacterLiteralContext;
import cool.klass.model.meta.grammar.KlassParser.ClassBodyContext;
import cool.klass.model.meta.grammar.KlassParser.ClassDeclarationContext;
import cool.klass.model.meta.grammar.KlassParser.ClassHeaderContext;
import cool.klass.model.meta.grammar.KlassParser.ClassMemberContext;
import cool.klass.model.meta.grammar.KlassParser.ClassModifierContext;
import cool.klass.model.meta.grammar.KlassParser.ClassOrUserContext;
import cool.klass.model.meta.grammar.KlassParser.ClassReferenceContext;
import cool.klass.model.meta.grammar.KlassParser.ClassServiceModifierContext;
import cool.klass.model.meta.grammar.KlassParser.ClassTypeContext;
import cool.klass.model.meta.grammar.KlassParser.ClassifierReferenceContext;
import cool.klass.model.meta.grammar.KlassParser.CompilationUnitContext;
import cool.klass.model.meta.grammar.KlassParser.CriteriaAllContext;
import cool.klass.model.meta.grammar.KlassParser.CriteriaEdgePointContext;
import cool.klass.model.meta.grammar.KlassParser.CriteriaExpressionAndContext;
import cool.klass.model.meta.grammar.KlassParser.CriteriaExpressionGroupContext;
import cool.klass.model.meta.grammar.KlassParser.CriteriaExpressionOrContext;
import cool.klass.model.meta.grammar.KlassParser.CriteriaNativeContext;
import cool.klass.model.meta.grammar.KlassParser.CriteriaOperatorContext;
import cool.klass.model.meta.grammar.KlassParser.DataTypePropertyContext;
import cool.klass.model.meta.grammar.KlassParser.DataTypePropertyValidationContext;
import cool.klass.model.meta.grammar.KlassParser.EnumerationBodyContext;
import cool.klass.model.meta.grammar.KlassParser.EnumerationDeclarationContext;
import cool.klass.model.meta.grammar.KlassParser.EnumerationLiteralContext;
import cool.klass.model.meta.grammar.KlassParser.EnumerationParameterDeclarationContext;
import cool.klass.model.meta.grammar.KlassParser.EnumerationPrettyNameContext;
import cool.klass.model.meta.grammar.KlassParser.EnumerationPropertyContext;
import cool.klass.model.meta.grammar.KlassParser.EnumerationReferenceContext;
import cool.klass.model.meta.grammar.KlassParser.EqualityOperatorContext;
import cool.klass.model.meta.grammar.KlassParser.ExpressionMemberReferenceContext;
import cool.klass.model.meta.grammar.KlassParser.ExpressionValueContext;
import cool.klass.model.meta.grammar.KlassParser.ExtendsDeclarationContext;
import cool.klass.model.meta.grammar.KlassParser.FloatingPointLiteralContext;
import cool.klass.model.meta.grammar.KlassParser.HeaderContext;
import cool.klass.model.meta.grammar.KlassParser.IdentifierContext;
import cool.klass.model.meta.grammar.KlassParser.ImplementsDeclarationContext;
import cool.klass.model.meta.grammar.KlassParser.InOperatorContext;
import cool.klass.model.meta.grammar.KlassParser.InequalityOperatorContext;
import cool.klass.model.meta.grammar.KlassParser.InheritanceTypeContext;
import cool.klass.model.meta.grammar.KlassParser.IntegerLiteralContext;
import cool.klass.model.meta.grammar.KlassParser.InterfaceBodyContext;
import cool.klass.model.meta.grammar.KlassParser.InterfaceDeclarationContext;
import cool.klass.model.meta.grammar.KlassParser.InterfaceHeaderContext;
import cool.klass.model.meta.grammar.KlassParser.InterfaceMemberContext;
import cool.klass.model.meta.grammar.KlassParser.InterfaceReferenceContext;
import cool.klass.model.meta.grammar.KlassParser.KeywordValidAsIdentifierContext;
import cool.klass.model.meta.grammar.KlassParser.LiteralContext;
import cool.klass.model.meta.grammar.KlassParser.LiteralListContext;
import cool.klass.model.meta.grammar.KlassParser.MaxLengthValidationContext;
import cool.klass.model.meta.grammar.KlassParser.MaxValidationContext;
import cool.klass.model.meta.grammar.KlassParser.MemberReferenceContext;
import cool.klass.model.meta.grammar.KlassParser.MinLengthValidationContext;
import cool.klass.model.meta.grammar.KlassParser.MinValidationContext;
import cool.klass.model.meta.grammar.KlassParser.MultiplicityBodyContext;
import cool.klass.model.meta.grammar.KlassParser.MultiplicityContext;
import cool.klass.model.meta.grammar.KlassParser.NativeLiteralContext;
import cool.klass.model.meta.grammar.KlassParser.NullLiteralContext;
import cool.klass.model.meta.grammar.KlassParser.OperatorContext;
import cool.klass.model.meta.grammar.KlassParser.OptionalMarkerContext;
import cool.klass.model.meta.grammar.KlassParser.OrderByDeclarationContext;
import cool.klass.model.meta.grammar.KlassParser.OrderByDirectionContext;
import cool.klass.model.meta.grammar.KlassParser.OrderByMemberReferencePathContext;
import cool.klass.model.meta.grammar.KlassParser.PackageDeclarationContext;
import cool.klass.model.meta.grammar.KlassParser.PackageNameContext;
import cool.klass.model.meta.grammar.KlassParser.ParameterDeclarationContext;
import cool.klass.model.meta.grammar.KlassParser.ParameterDeclarationListContext;
import cool.klass.model.meta.grammar.KlassParser.ParameterModifierContext;
import cool.klass.model.meta.grammar.KlassParser.ParameterizedPropertyContext;
import cool.klass.model.meta.grammar.KlassParser.ParameterizedPropertyModifierContext;
import cool.klass.model.meta.grammar.KlassParser.ParameterizedPropertySignatureContext;
import cool.klass.model.meta.grammar.KlassParser.PrimitiveParameterDeclarationContext;
import cool.klass.model.meta.grammar.KlassParser.PrimitivePropertyContext;
import cool.klass.model.meta.grammar.KlassParser.PrimitiveTypeContext;
import cool.klass.model.meta.grammar.KlassParser.ProjectionAssociationEndContext;
import cool.klass.model.meta.grammar.KlassParser.ProjectionBodyContext;
import cool.klass.model.meta.grammar.KlassParser.ProjectionDeclarationContext;
import cool.klass.model.meta.grammar.KlassParser.ProjectionMemberContext;
import cool.klass.model.meta.grammar.KlassParser.ProjectionParameterizedPropertyContext;
import cool.klass.model.meta.grammar.KlassParser.ProjectionPrimitiveMemberContext;
import cool.klass.model.meta.grammar.KlassParser.ProjectionProjectionReferenceContext;
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
import cool.klass.model.meta.grammar.KlassParser.ServiceOrderByDeclarationContext;
import cool.klass.model.meta.grammar.KlassParser.ServiceProjectionDispatchContext;
import cool.klass.model.meta.grammar.KlassParser.StringLiteralContext;
import cool.klass.model.meta.grammar.KlassParser.StringOperatorContext;
import cool.klass.model.meta.grammar.KlassParser.ThisMemberReferencePathContext;
import cool.klass.model.meta.grammar.KlassParser.TopLevelDeclarationContext;
import cool.klass.model.meta.grammar.KlassParser.TypeMemberReferencePathContext;
import cool.klass.model.meta.grammar.KlassParser.UrlConstantContext;
import cool.klass.model.meta.grammar.KlassParser.UrlContext;
import cool.klass.model.meta.grammar.KlassParser.UrlDeclarationContext;
import cool.klass.model.meta.grammar.KlassParser.UrlParameterDeclarationContext;
import cool.klass.model.meta.grammar.KlassParser.UrlParameterDeclarationEOFContext;
import cool.klass.model.meta.grammar.KlassParser.UrlPathSegmentContext;
import cool.klass.model.meta.grammar.KlassParser.VariableReferenceContext;
import cool.klass.model.meta.grammar.KlassParser.VerbContext;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.tree.ErrorNode;
import org.antlr.v4.runtime.tree.TerminalNode;

// Deliberately not abstract, since I want it to implement every method of KlassListener.
public class KlassThrowingListener implements KlassListener
{
    @Override
    public void enterCompilationUnit(CompilationUnitContext ctx)
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName()
                + ".enterCompilationUnit() not implemented yet");
    }

    @Override
    public void exitCompilationUnit(CompilationUnitContext ctx)
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName()
                + ".exitCompilationUnit() not implemented yet");
    }

    @Override
    public void enterUrlParameterDeclarationEOF(UrlParameterDeclarationEOFContext ctx)
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName()
                + ".enterUrlParameterDeclarationEOF() not implemented yet");
    }

    @Override
    public void exitUrlParameterDeclarationEOF(UrlParameterDeclarationEOFContext ctx)
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName()
                + ".exitUrlParameterDeclarationEOF() not implemented yet");
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
    public void enterInterfaceDeclaration(InterfaceDeclarationContext ctx)
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName()
                + ".enterInterfaceDeclaration() not implemented yet");
    }

    @Override
    public void exitInterfaceDeclaration(InterfaceDeclarationContext ctx)
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName()
                + ".exitInterfaceDeclaration() not implemented yet");
    }

    @Override
    public void enterInterfaceHeader(InterfaceHeaderContext ctx)
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName()
                + ".enterInterfaceHeader() not implemented yet");
    }

    @Override
    public void exitInterfaceHeader(InterfaceHeaderContext ctx)
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName()
                + ".exitInterfaceHeader() not implemented yet");
    }

    @Override
    public void enterInterfaceBody(InterfaceBodyContext ctx)
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName()
                + ".enterInterfaceBody() not implemented yet");
    }

    @Override
    public void exitInterfaceBody(InterfaceBodyContext ctx)
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName()
                + ".exitInterfaceBody() not implemented yet");
    }

    @Override
    public void enterClassDeclaration(ClassDeclarationContext ctx)
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName()
                + ".enterClassDeclaration() not implemented yet");
    }

    @Override
    public void exitClassDeclaration(ClassDeclarationContext ctx)
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName()
                + ".exitClassDeclaration() not implemented yet");
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
    public void enterExtendsDeclaration(ExtendsDeclarationContext ctx)
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName()
                + ".enterExtendsDeclaration() not implemented yet");
    }

    @Override
    public void exitExtendsDeclaration(ExtendsDeclarationContext ctx)
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName()
                + ".exitExtendsDeclaration() not implemented yet");
    }

    @Override
    public void enterAbstractDeclaration(AbstractDeclarationContext ctx)
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName()
                + ".enterAbstractDeclaration() not implemented yet");
    }

    @Override
    public void exitAbstractDeclaration(AbstractDeclarationContext ctx)
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName()
                + ".exitAbstractDeclaration() not implemented yet");
    }

    @Override
    public void enterInheritanceType(InheritanceTypeContext ctx)
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName()
                + ".enterInheritanceType() not implemented yet");
    }

    @Override
    public void exitInheritanceType(InheritanceTypeContext ctx)
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName()
                + ".exitInheritanceType() not implemented yet");
    }

    @Override
    public void enterImplementsDeclaration(ImplementsDeclarationContext ctx)
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName()
                + ".enterImplementsDeclaration() not implemented yet");
    }

    @Override
    public void exitImplementsDeclaration(ImplementsDeclarationContext ctx)
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName()
                + ".exitImplementsDeclaration() not implemented yet");
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
    public void enterClassHeader(ClassHeaderContext ctx)
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName()
                + ".enterClassHeader() not implemented yet");
    }

    @Override
    public void exitClassHeader(ClassHeaderContext ctx)
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName()
                + ".exitClassHeader() not implemented yet");
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
    public void enterEnumerationDeclaration(EnumerationDeclarationContext ctx)
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName()
                + ".enterEnumerationDeclaration() not implemented yet");
    }

    @Override
    public void exitEnumerationDeclaration(EnumerationDeclarationContext ctx)
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName()
                + ".exitEnumerationDeclaration() not implemented yet");
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
    public void enterAssociationDeclaration(AssociationDeclarationContext ctx)
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName()
                + ".enterAssociationDeclaration() not implemented yet");
    }

    @Override
    public void exitAssociationDeclaration(AssociationDeclarationContext ctx)
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName()
                + ".exitAssociationDeclaration() not implemented yet");
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
    public void enterAssociationEndSignature(AssociationEndSignatureContext ctx)
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName()
                + ".enterAssociationEndSignature() not implemented yet");
    }

    @Override
    public void exitAssociationEndSignature(AssociationEndSignatureContext ctx)
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName()
                + ".exitAssociationEndSignature() not implemented yet");
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
    public void enterProjectionDeclaration(ProjectionDeclarationContext ctx)
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName()
                + ".enterProjectionDeclaration() not implemented yet");
    }

    @Override
    public void exitProjectionDeclaration(ProjectionDeclarationContext ctx)
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName()
                + ".exitProjectionDeclaration() not implemented yet");
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
    public void enterProjectionProjectionReference(ProjectionProjectionReferenceContext ctx)
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName()
                + ".enterProjectionProjectionReference() not implemented yet");
    }

    @Override
    public void exitProjectionProjectionReference(ProjectionProjectionReferenceContext ctx)
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName()
                + ".exitProjectionProjectionReference() not implemented yet");
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
    public void enterParameterizedPropertySignature(ParameterizedPropertySignatureContext ctx)
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName()
                + ".enterParameterizedPropertySignature() not implemented yet");
    }

    @Override
    public void exitParameterizedPropertySignature(ParameterizedPropertySignatureContext ctx)
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName()
                + ".exitParameterizedPropertySignature() not implemented yet");
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
    public void enterServiceGroupDeclaration(ServiceGroupDeclarationContext ctx)
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName()
                + ".enterServiceGroupDeclaration() not implemented yet");
    }

    @Override
    public void exitServiceGroupDeclaration(ServiceGroupDeclarationContext ctx)
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName()
                + ".exitServiceGroupDeclaration() not implemented yet");
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
    public void enterUrlDeclaration(UrlDeclarationContext ctx)
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName()
                + ".enterUrlDeclaration() not implemented yet");
    }

    @Override
    public void exitUrlDeclaration(UrlDeclarationContext ctx)
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName()
                + ".exitUrlDeclaration() not implemented yet");
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
    public void enterServiceDeclaration(ServiceDeclarationContext ctx)
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName()
                + ".enterServiceDeclaration() not implemented yet");
    }

    @Override
    public void exitServiceDeclaration(ServiceDeclarationContext ctx)
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName()
                + ".exitServiceDeclaration() not implemented yet");
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
    public void enterServiceOrderByDeclaration(ServiceOrderByDeclarationContext ctx)
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName()
                + ".enterServiceOrderByDeclaration() not implemented yet");
    }

    @Override
    public void exitServiceOrderByDeclaration(ServiceOrderByDeclarationContext ctx)
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName()
                + ".exitServiceOrderByDeclaration() not implemented yet");
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
    public void enterInterfaceMember(InterfaceMemberContext ctx)
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName()
                + ".enterInterfaceMember() not implemented yet");
    }

    @Override
    public void exitInterfaceMember(InterfaceMemberContext ctx)
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName()
                + ".exitInterfaceMember() not implemented yet");
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
    public void enterDataTypePropertyValidation(DataTypePropertyValidationContext ctx)
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName()
                + ".enterDataTypePropertyValidation() not implemented yet");
    }

    @Override
    public void exitDataTypePropertyValidation(DataTypePropertyValidationContext ctx)
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName()
                + ".exitDataTypePropertyValidation() not implemented yet");
    }

    @Override
    public void enterMinLengthValidation(MinLengthValidationContext ctx)
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName()
                + ".enterMinLengthValidation() not implemented yet");
    }

    @Override
    public void exitMinLengthValidation(MinLengthValidationContext ctx)
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName()
                + ".exitMinLengthValidation() not implemented yet");
    }

    @Override
    public void enterMaxLengthValidation(MaxLengthValidationContext ctx)
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName()
                + ".enterMaxLengthValidation() not implemented yet");
    }

    @Override
    public void exitMaxLengthValidation(MaxLengthValidationContext ctx)
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName()
                + ".exitMaxLengthValidation() not implemented yet");
    }

    @Override
    public void enterMinValidation(MinValidationContext ctx)
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName()
                + ".enterMinValidation() not implemented yet");
    }

    @Override
    public void exitMinValidation(MinValidationContext ctx)
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName()
                + ".exitMinValidation() not implemented yet");
    }

    @Override
    public void enterMaxValidation(MaxValidationContext ctx)
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName()
                + ".enterMaxValidation() not implemented yet");
    }

    @Override
    public void exitMaxValidation(MaxValidationContext ctx)
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName()
                + ".exitMaxValidation() not implemented yet");
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
    public void enterParameterizedPropertyModifier(ParameterizedPropertyModifierContext ctx)
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName()
                + ".enterParameterizedPropertyModifier() not implemented yet");
    }

    @Override
    public void exitParameterizedPropertyModifier(ParameterizedPropertyModifierContext ctx)
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName()
                + ".exitParameterizedPropertyModifier() not implemented yet");
    }

    @Override
    public void enterParameterModifier(ParameterModifierContext ctx)
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName()
                + ".enterParameterModifier() not implemented yet");
    }

    @Override
    public void exitParameterModifier(ParameterModifierContext ctx)
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName()
                + ".exitParameterModifier() not implemented yet");
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
    public void enterOrderByMemberReferencePath(OrderByMemberReferencePathContext ctx)
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName()
                + ".enterOrderByMemberReferencePath() not implemented yet");
    }

    @Override
    public void exitOrderByMemberReferencePath(OrderByMemberReferencePathContext ctx)
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName()
                + ".exitOrderByMemberReferencePath() not implemented yet");
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
    public void enterCriteriaEdgePoint(CriteriaEdgePointContext ctx)
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName()
                + ".enterCriteriaEdgePoint() not implemented yet");
    }

    @Override
    public void exitCriteriaEdgePoint(CriteriaEdgePointContext ctx)
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName()
                + ".exitCriteriaEdgePoint() not implemented yet");
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
    public void enterCriteriaAll(CriteriaAllContext ctx)
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName()
                + ".enterCriteriaAll() not implemented yet");
    }

    @Override
    public void exitCriteriaAll(CriteriaAllContext ctx)
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName()
                + ".exitCriteriaAll() not implemented yet");
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
    public void enterExpressionMemberReference(ExpressionMemberReferenceContext ctx)
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName()
                + ".enterExpressionMemberReference() not implemented yet");
    }

    @Override
    public void exitExpressionMemberReference(ExpressionMemberReferenceContext ctx)
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName()
                + ".exitExpressionMemberReference() not implemented yet");
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
    public void enterInterfaceReference(InterfaceReferenceContext ctx)
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName()
                + ".enterInterfaceReference() not implemented yet");
    }

    @Override
    public void exitInterfaceReference(InterfaceReferenceContext ctx)
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName()
                + ".exitInterfaceReference() not implemented yet");
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
    public void enterClassifierReference(ClassifierReferenceContext ctx)
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName()
                + ".enterClassifierReference() not implemented yet");
    }

    @Override
    public void exitClassifierReference(ClassifierReferenceContext ctx)
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName()
                + ".exitClassifierReference() not implemented yet");
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
    public void enterAssociationEndReference(AssociationEndReferenceContext ctx)
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName()
                + ".enterAssociationEndReference() not implemented yet");
    }

    @Override
    public void exitAssociationEndReference(AssociationEndReferenceContext ctx)
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName()
                + ".exitAssociationEndReference() not implemented yet");
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
    public void enterThisMemberReferencePath(ThisMemberReferencePathContext ctx)
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName()
                + ".enterThisMemberReferencePath() not implemented yet");
    }

    @Override
    public void exitThisMemberReferencePath(ThisMemberReferencePathContext ctx)
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName()
                + ".exitThisMemberReferencePath() not implemented yet");
    }

    @Override
    public void enterTypeMemberReferencePath(TypeMemberReferencePathContext ctx)
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName()
                + ".enterTypeMemberReferencePath() not implemented yet");
    }

    @Override
    public void exitTypeMemberReferencePath(TypeMemberReferencePathContext ctx)
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName()
                + ".exitTypeMemberReferencePath() not implemented yet");
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
    public void enterIntegerLiteral(IntegerLiteralContext ctx)
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName()
                + ".enterIntegerLiteral() not implemented yet");
    }

    @Override
    public void exitIntegerLiteral(IntegerLiteralContext ctx)
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName()
                + ".exitIntegerLiteral() not implemented yet");
    }

    @Override
    public void enterFloatingPointLiteral(FloatingPointLiteralContext ctx)
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName()
                + ".enterFloatingPointLiteral() not implemented yet");
    }

    @Override
    public void exitFloatingPointLiteral(FloatingPointLiteralContext ctx)
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName()
                + ".exitFloatingPointLiteral() not implemented yet");
    }

    @Override
    public void enterBooleanLiteral(BooleanLiteralContext ctx)
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName()
                + ".enterBooleanLiteral() not implemented yet");
    }

    @Override
    public void exitBooleanLiteral(BooleanLiteralContext ctx)
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName()
                + ".exitBooleanLiteral() not implemented yet");
    }

    @Override
    public void enterCharacterLiteral(CharacterLiteralContext ctx)
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName()
                + ".enterCharacterLiteral() not implemented yet");
    }

    @Override
    public void exitCharacterLiteral(CharacterLiteralContext ctx)
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName()
                + ".exitCharacterLiteral() not implemented yet");
    }

    @Override
    public void enterStringLiteral(StringLiteralContext ctx)
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName()
                + ".enterStringLiteral() not implemented yet");
    }

    @Override
    public void exitStringLiteral(StringLiteralContext ctx)
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName()
                + ".exitStringLiteral() not implemented yet");
    }

    @Override
    public void enterNullLiteral(NullLiteralContext ctx)
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName()
                + ".enterNullLiteral() not implemented yet");
    }

    @Override
    public void exitNullLiteral(NullLiteralContext ctx)
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName()
                + ".exitNullLiteral() not implemented yet");
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
}
