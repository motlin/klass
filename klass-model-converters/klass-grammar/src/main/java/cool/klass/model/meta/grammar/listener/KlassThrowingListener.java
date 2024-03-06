package cool.klass.model.meta.grammar.listener;

import javax.annotation.Nonnull;

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
import cool.klass.model.meta.grammar.KlassParser.ClassifierTypeContext;
import cool.klass.model.meta.grammar.KlassParser.CompilationUnitContext;
import cool.klass.model.meta.grammar.KlassParser.CriteriaAllContext;
import cool.klass.model.meta.grammar.KlassParser.CriteriaEdgePointContext;
import cool.klass.model.meta.grammar.KlassParser.CriteriaExpressionAndContext;
import cool.klass.model.meta.grammar.KlassParser.CriteriaExpressionGroupContext;
import cool.klass.model.meta.grammar.KlassParser.CriteriaExpressionOrContext;
import cool.klass.model.meta.grammar.KlassParser.CriteriaNativeContext;
import cool.klass.model.meta.grammar.KlassParser.CriteriaOperatorContext;
import cool.klass.model.meta.grammar.KlassParser.DataTypePropertyContext;
import cool.klass.model.meta.grammar.KlassParser.DataTypePropertyModifierContext;
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
import cool.klass.model.meta.grammar.KlassParser.InvalidParameterDeclarationContext;
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
import cool.klass.model.meta.grammar.KlassParser.ProjectionBodyContext;
import cool.klass.model.meta.grammar.KlassParser.ProjectionDeclarationContext;
import cool.klass.model.meta.grammar.KlassParser.ProjectionMemberContext;
import cool.klass.model.meta.grammar.KlassParser.ProjectionParameterizedPropertyContext;
import cool.klass.model.meta.grammar.KlassParser.ProjectionPrimitiveMemberContext;
import cool.klass.model.meta.grammar.KlassParser.ProjectionProjectionReferenceContext;
import cool.klass.model.meta.grammar.KlassParser.ProjectionReferenceContext;
import cool.klass.model.meta.grammar.KlassParser.ProjectionReferencePropertyContext;
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
    public void enterCompilationUnit(@Nonnull CompilationUnitContext ctx)
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName()
                + ".enterCompilationUnit() not implemented yet");
    }

    @Override
    public void exitCompilationUnit(@Nonnull CompilationUnitContext ctx)
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName()
                + ".exitCompilationUnit() not implemented yet");
    }

    @Override
    public void enterUrlParameterDeclarationEOF(@Nonnull UrlParameterDeclarationEOFContext ctx)
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName()
                + ".enterUrlParameterDeclarationEOF() not implemented yet");
    }

    @Override
    public void exitUrlParameterDeclarationEOF(@Nonnull UrlParameterDeclarationEOFContext ctx)
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName()
                + ".exitUrlParameterDeclarationEOF() not implemented yet");
    }

    @Override
    public void enterPackageDeclaration(@Nonnull PackageDeclarationContext ctx)
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName()
                + ".enterPackageDeclaration() not implemented yet");
    }

    @Override
    public void exitPackageDeclaration(@Nonnull PackageDeclarationContext ctx)
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName()
                + ".exitPackageDeclaration() not implemented yet");
    }

    @Override
    public void enterPackageName(@Nonnull PackageNameContext ctx)
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName()
                + ".enterPackageName() not implemented yet");
    }

    @Override
    public void exitPackageName(@Nonnull PackageNameContext ctx)
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName()
                + ".exitPackageName() not implemented yet");
    }

    @Override
    public void enterTopLevelDeclaration(@Nonnull TopLevelDeclarationContext ctx)
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName()
                + ".enterTopLevelDeclaration() not implemented yet");
    }

    @Override
    public void exitTopLevelDeclaration(@Nonnull TopLevelDeclarationContext ctx)
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName()
                + ".exitTopLevelDeclaration() not implemented yet");
    }

    @Override
    public void enterInterfaceDeclaration(@Nonnull InterfaceDeclarationContext ctx)
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName()
                + ".enterInterfaceDeclaration() not implemented yet");
    }

    @Override
    public void exitInterfaceDeclaration(@Nonnull InterfaceDeclarationContext ctx)
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName()
                + ".exitInterfaceDeclaration() not implemented yet");
    }

    @Override
    public void enterInterfaceHeader(@Nonnull InterfaceHeaderContext ctx)
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName()
                + ".enterInterfaceHeader() not implemented yet");
    }

    @Override
    public void exitInterfaceHeader(@Nonnull InterfaceHeaderContext ctx)
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName()
                + ".exitInterfaceHeader() not implemented yet");
    }

    @Override
    public void enterInterfaceBody(@Nonnull InterfaceBodyContext ctx)
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName()
                + ".enterInterfaceBody() not implemented yet");
    }

    @Override
    public void exitInterfaceBody(@Nonnull InterfaceBodyContext ctx)
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName()
                + ".exitInterfaceBody() not implemented yet");
    }

    @Override
    public void enterClassDeclaration(@Nonnull ClassDeclarationContext ctx)
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName()
                + ".enterClassDeclaration() not implemented yet");
    }

    @Override
    public void exitClassDeclaration(@Nonnull ClassDeclarationContext ctx)
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName()
                + ".exitClassDeclaration() not implemented yet");
    }

    @Override
    public void enterClassOrUser(@Nonnull ClassOrUserContext ctx)
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName()
                + ".enterClassOrUser() not implemented yet");
    }

    @Override
    public void exitClassOrUser(@Nonnull ClassOrUserContext ctx)
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName()
                + ".exitClassOrUser() not implemented yet");
    }

    @Override
    public void enterExtendsDeclaration(@Nonnull ExtendsDeclarationContext ctx)
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName()
                + ".enterExtendsDeclaration() not implemented yet");
    }

    @Override
    public void exitExtendsDeclaration(@Nonnull ExtendsDeclarationContext ctx)
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName()
                + ".exitExtendsDeclaration() not implemented yet");
    }

    @Override
    public void enterAbstractDeclaration(@Nonnull AbstractDeclarationContext ctx)
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName()
                + ".enterAbstractDeclaration() not implemented yet");
    }

    @Override
    public void exitAbstractDeclaration(@Nonnull AbstractDeclarationContext ctx)
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName()
                + ".exitAbstractDeclaration() not implemented yet");
    }

    @Override
    public void enterInheritanceType(@Nonnull InheritanceTypeContext ctx)
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName()
                + ".enterInheritanceType() not implemented yet");
    }

    @Override
    public void exitInheritanceType(@Nonnull InheritanceTypeContext ctx)
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName()
                + ".exitInheritanceType() not implemented yet");
    }

    @Override
    public void enterImplementsDeclaration(@Nonnull ImplementsDeclarationContext ctx)
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName()
                + ".enterImplementsDeclaration() not implemented yet");
    }

    @Override
    public void exitImplementsDeclaration(@Nonnull ImplementsDeclarationContext ctx)
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName()
                + ".exitImplementsDeclaration() not implemented yet");
    }

    @Override
    public void enterClassServiceModifier(@Nonnull ClassServiceModifierContext ctx)
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName()
                + ".enterClassServiceModifier() not implemented yet");
    }

    @Override
    public void exitClassServiceModifier(@Nonnull ClassServiceModifierContext ctx)
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName()
                + ".exitClassServiceModifier() not implemented yet");
    }

    @Override
    public void enterServiceCategoryModifier(@Nonnull ServiceCategoryModifierContext ctx)
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName()
                + ".enterServiceCategoryModifier() not implemented yet");
    }

    @Override
    public void exitServiceCategoryModifier(@Nonnull ServiceCategoryModifierContext ctx)
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName()
                + ".exitServiceCategoryModifier() not implemented yet");
    }

    @Override
    public void enterClassHeader(@Nonnull ClassHeaderContext ctx)
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName()
                + ".enterClassHeader() not implemented yet");
    }

    @Override
    public void exitClassHeader(@Nonnull ClassHeaderContext ctx)
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName()
                + ".exitClassHeader() not implemented yet");
    }

    @Override
    public void enterClassBody(@Nonnull ClassBodyContext ctx)
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName()
                + ".enterClassBody() not implemented yet");
    }

    @Override
    public void exitClassBody(@Nonnull ClassBodyContext ctx)
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName()
                + ".exitClassBody() not implemented yet");
    }

    @Override
    public void enterEnumerationDeclaration(@Nonnull EnumerationDeclarationContext ctx)
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName()
                + ".enterEnumerationDeclaration() not implemented yet");
    }

    @Override
    public void exitEnumerationDeclaration(@Nonnull EnumerationDeclarationContext ctx)
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName()
                + ".exitEnumerationDeclaration() not implemented yet");
    }

    @Override
    public void enterEnumerationBody(@Nonnull EnumerationBodyContext ctx)
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName()
                + ".enterEnumerationBody() not implemented yet");
    }

    @Override
    public void exitEnumerationBody(@Nonnull EnumerationBodyContext ctx)
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName()
                + ".exitEnumerationBody() not implemented yet");
    }

    @Override
    public void enterEnumerationLiteral(@Nonnull EnumerationLiteralContext ctx)
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName()
                + ".enterEnumerationLiteral() not implemented yet");
    }

    @Override
    public void exitEnumerationLiteral(@Nonnull EnumerationLiteralContext ctx)
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName()
                + ".exitEnumerationLiteral() not implemented yet");
    }

    @Override
    public void enterEnumerationPrettyName(@Nonnull EnumerationPrettyNameContext ctx)
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName()
                + ".enterEnumerationPrettyName() not implemented yet");
    }

    @Override
    public void exitEnumerationPrettyName(@Nonnull EnumerationPrettyNameContext ctx)
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName()
                + ".exitEnumerationPrettyName() not implemented yet");
    }

    @Override
    public void enterAssociationDeclaration(@Nonnull AssociationDeclarationContext ctx)
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName()
                + ".enterAssociationDeclaration() not implemented yet");
    }

    @Override
    public void exitAssociationDeclaration(@Nonnull AssociationDeclarationContext ctx)
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName()
                + ".exitAssociationDeclaration() not implemented yet");
    }

    @Override
    public void enterAssociationBody(@Nonnull AssociationBodyContext ctx)
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName()
                + ".enterAssociationBody() not implemented yet");
    }

    @Override
    public void exitAssociationBody(@Nonnull AssociationBodyContext ctx)
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName()
                + ".exitAssociationBody() not implemented yet");
    }

    @Override
    public void enterAssociationEnd(@Nonnull AssociationEndContext ctx)
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName()
                + ".enterAssociationEnd() not implemented yet");
    }

    @Override
    public void exitAssociationEnd(@Nonnull AssociationEndContext ctx)
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName()
                + ".exitAssociationEnd() not implemented yet");
    }

    @Override
    public void enterAssociationEndSignature(@Nonnull AssociationEndSignatureContext ctx)
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName()
                + ".enterAssociationEndSignature() not implemented yet");
    }

    @Override
    public void exitAssociationEndSignature(@Nonnull AssociationEndSignatureContext ctx)
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName()
                + ".exitAssociationEndSignature() not implemented yet");
    }

    @Override
    public void enterRelationship(@Nonnull RelationshipContext ctx)
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName()
                + ".enterRelationship() not implemented yet");
    }

    @Override
    public void exitRelationship(@Nonnull RelationshipContext ctx)
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName()
                + ".exitRelationship() not implemented yet");
    }

    @Override
    public void enterProjectionDeclaration(@Nonnull ProjectionDeclarationContext ctx)
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName()
                + ".enterProjectionDeclaration() not implemented yet");
    }

    @Override
    public void exitProjectionDeclaration(@Nonnull ProjectionDeclarationContext ctx)
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName()
                + ".exitProjectionDeclaration() not implemented yet");
    }

    @Override
    public void enterProjectionBody(@Nonnull ProjectionBodyContext ctx)
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName()
                + ".enterProjectionBody() not implemented yet");
    }

    @Override
    public void exitProjectionBody(@Nonnull ProjectionBodyContext ctx)
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName()
                + ".exitProjectionBody() not implemented yet");
    }

    @Override
    public void enterProjectionMember(@Nonnull ProjectionMemberContext ctx)
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName()
                + ".enterProjectionMember() not implemented yet");
    }

    @Override
    public void exitProjectionMember(@Nonnull ProjectionMemberContext ctx)
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName()
                + ".exitProjectionMember() not implemented yet");
    }

    @Override
    public void enterProjectionPrimitiveMember(@Nonnull ProjectionPrimitiveMemberContext ctx)
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName()
                + ".enterProjectionPrimitiveMember() not implemented yet");
    }

    @Override
    public void exitProjectionPrimitiveMember(@Nonnull ProjectionPrimitiveMemberContext ctx)
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName()
                + ".exitProjectionPrimitiveMember() not implemented yet");
    }

    @Override
    public void enterProjectionReferenceProperty(@Nonnull ProjectionReferencePropertyContext ctx)
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName()
                + ".enterProjectionReferenceProperty() not implemented yet");
    }

    @Override
    public void exitProjectionReferenceProperty(@Nonnull ProjectionReferencePropertyContext ctx)
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName()
                + ".exitProjectionReferenceProperty() not implemented yet");
    }

    @Override
    public void enterProjectionProjectionReference(@Nonnull ProjectionProjectionReferenceContext ctx)
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName()
                + ".enterProjectionProjectionReference() not implemented yet");
    }

    @Override
    public void exitProjectionProjectionReference(@Nonnull ProjectionProjectionReferenceContext ctx)
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName()
                + ".exitProjectionProjectionReference() not implemented yet");
    }

    @Override
    public void enterProjectionParameterizedProperty(@Nonnull ProjectionParameterizedPropertyContext ctx)
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName()
                + ".enterProjectionParameterizedProperty() not implemented yet");
    }

    @Override
    public void exitProjectionParameterizedProperty(@Nonnull ProjectionParameterizedPropertyContext ctx)
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName()
                + ".exitProjectionParameterizedProperty() not implemented yet");
    }

    @Override
    public void enterParameterizedPropertySignature(@Nonnull ParameterizedPropertySignatureContext ctx)
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName()
                + ".enterParameterizedPropertySignature() not implemented yet");
    }

    @Override
    public void exitParameterizedPropertySignature(@Nonnull ParameterizedPropertySignatureContext ctx)
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName()
                + ".exitParameterizedPropertySignature() not implemented yet");
    }

    @Override
    public void enterHeader(@Nonnull HeaderContext ctx)
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName() + ".enterHeader() not implemented yet");
    }

    @Override
    public void exitHeader(@Nonnull HeaderContext ctx)
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName() + ".exitHeader() not implemented yet");
    }

    @Override
    public void enterServiceGroupDeclaration(@Nonnull ServiceGroupDeclarationContext ctx)
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName()
                + ".enterServiceGroupDeclaration() not implemented yet");
    }

    @Override
    public void exitServiceGroupDeclaration(@Nonnull ServiceGroupDeclarationContext ctx)
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName()
                + ".exitServiceGroupDeclaration() not implemented yet");
    }

    @Override
    public void enterServiceGroupDeclarationBody(@Nonnull ServiceGroupDeclarationBodyContext ctx)
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName()
                + ".enterServiceGroupDeclarationBody() not implemented yet");
    }

    @Override
    public void exitServiceGroupDeclarationBody(@Nonnull ServiceGroupDeclarationBodyContext ctx)
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName()
                + ".exitServiceGroupDeclarationBody() not implemented yet");
    }

    @Override
    public void enterUrlDeclaration(@Nonnull UrlDeclarationContext ctx)
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName()
                + ".enterUrlDeclaration() not implemented yet");
    }

    @Override
    public void exitUrlDeclaration(@Nonnull UrlDeclarationContext ctx)
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName()
                + ".exitUrlDeclaration() not implemented yet");
    }

    @Override
    public void enterUrl(@Nonnull UrlContext ctx)
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName() + ".enterUrl() not implemented yet");
    }

    @Override
    public void exitUrl(@Nonnull UrlContext ctx)
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName() + ".exitUrl() not implemented yet");
    }

    @Override
    public void enterUrlPathSegment(@Nonnull UrlPathSegmentContext ctx)
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName()
                + ".enterUrlPathSegment() not implemented yet");
    }

    @Override
    public void exitUrlPathSegment(@Nonnull UrlPathSegmentContext ctx)
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName()
                + ".exitUrlPathSegment() not implemented yet");
    }

    @Override
    public void enterUrlConstant(@Nonnull UrlConstantContext ctx)
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName()
                + ".enterUrlConstant() not implemented yet");
    }

    @Override
    public void exitUrlConstant(@Nonnull UrlConstantContext ctx)
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName()
                + ".exitUrlConstant() not implemented yet");
    }

    @Override
    public void enterQueryParameterList(@Nonnull QueryParameterListContext ctx)
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName()
                + ".enterQueryParameterList() not implemented yet");
    }

    @Override
    public void exitQueryParameterList(@Nonnull QueryParameterListContext ctx)
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName()
                + ".exitQueryParameterList() not implemented yet");
    }

    @Override
    public void enterUrlParameterDeclaration(@Nonnull UrlParameterDeclarationContext ctx)
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName()
                + ".enterUrlParameterDeclaration() not implemented yet");
    }

    @Override
    public void exitUrlParameterDeclaration(@Nonnull UrlParameterDeclarationContext ctx)
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName()
                + ".exitUrlParameterDeclaration() not implemented yet");
    }

    @Override
    public void enterServiceDeclaration(@Nonnull ServiceDeclarationContext ctx)
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName()
                + ".enterServiceDeclaration() not implemented yet");
    }

    @Override
    public void exitServiceDeclaration(@Nonnull ServiceDeclarationContext ctx)
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName()
                + ".exitServiceDeclaration() not implemented yet");
    }

    @Override
    public void enterServiceDeclarationBody(@Nonnull ServiceDeclarationBodyContext ctx)
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName()
                + ".enterServiceDeclarationBody() not implemented yet");
    }

    @Override
    public void exitServiceDeclarationBody(@Nonnull ServiceDeclarationBodyContext ctx)
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName()
                + ".exitServiceDeclarationBody() not implemented yet");
    }

    @Override
    public void enterServiceMultiplicityDeclaration(@Nonnull ServiceMultiplicityDeclarationContext ctx)
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName()
                + ".enterServiceMultiplicityDeclaration() not implemented yet");
    }

    @Override
    public void exitServiceMultiplicityDeclaration(@Nonnull ServiceMultiplicityDeclarationContext ctx)
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName()
                + ".exitServiceMultiplicityDeclaration() not implemented yet");
    }

    @Override
    public void enterServiceMultiplicity(@Nonnull ServiceMultiplicityContext ctx)
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName()
                + ".enterServiceMultiplicity() not implemented yet");
    }

    @Override
    public void exitServiceMultiplicity(@Nonnull ServiceMultiplicityContext ctx)
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName()
                + ".exitServiceMultiplicity() not implemented yet");
    }

    @Override
    public void enterServiceCriteriaDeclaration(@Nonnull ServiceCriteriaDeclarationContext ctx)
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName()
                + ".enterServiceCriteriaDeclaration() not implemented yet");
    }

    @Override
    public void exitServiceCriteriaDeclaration(@Nonnull ServiceCriteriaDeclarationContext ctx)
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName()
                + ".exitServiceCriteriaDeclaration() not implemented yet");
    }

    @Override
    public void enterServiceCriteriaKeyword(@Nonnull ServiceCriteriaKeywordContext ctx)
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName()
                + ".enterServiceCriteriaKeyword() not implemented yet");
    }

    @Override
    public void exitServiceCriteriaKeyword(@Nonnull ServiceCriteriaKeywordContext ctx)
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName()
                + ".exitServiceCriteriaKeyword() not implemented yet");
    }

    @Override
    public void enterServiceProjectionDispatch(@Nonnull ServiceProjectionDispatchContext ctx)
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName()
                + ".enterServiceProjectionDispatch() not implemented yet");
    }

    @Override
    public void exitServiceProjectionDispatch(@Nonnull ServiceProjectionDispatchContext ctx)
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName()
                + ".exitServiceProjectionDispatch() not implemented yet");
    }

    @Override
    public void enterServiceOrderByDeclaration(@Nonnull ServiceOrderByDeclarationContext ctx)
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName()
                + ".enterServiceOrderByDeclaration() not implemented yet");
    }

    @Override
    public void exitServiceOrderByDeclaration(@Nonnull ServiceOrderByDeclarationContext ctx)
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName()
                + ".exitServiceOrderByDeclaration() not implemented yet");
    }

    @Override
    public void enterVerb(@Nonnull VerbContext ctx)
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName() + ".enterVerb() not implemented yet");
    }

    @Override
    public void exitVerb(@Nonnull VerbContext ctx)
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName() + ".exitVerb() not implemented yet");
    }

    @Override
    public void enterInterfaceMember(@Nonnull InterfaceMemberContext ctx)
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName()
                + ".enterInterfaceMember() not implemented yet");
    }

    @Override
    public void exitInterfaceMember(@Nonnull InterfaceMemberContext ctx)
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName()
                + ".exitInterfaceMember() not implemented yet");
    }

    @Override
    public void enterClassMember(@Nonnull ClassMemberContext ctx)
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName()
                + ".enterClassMember() not implemented yet");
    }

    @Override
    public void exitClassMember(@Nonnull ClassMemberContext ctx)
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName()
                + ".exitClassMember() not implemented yet");
    }

    @Override
    public void enterDataTypeProperty(@Nonnull DataTypePropertyContext ctx)
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName()
                + ".enterDataTypeProperty() not implemented yet");
    }

    @Override
    public void exitDataTypeProperty(@Nonnull DataTypePropertyContext ctx)
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName()
                + ".exitDataTypeProperty() not implemented yet");
    }

    @Override
    public void enterPrimitiveProperty(@Nonnull PrimitivePropertyContext ctx)
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName()
                + ".enterPrimitiveProperty() not implemented yet");
    }

    @Override
    public void exitPrimitiveProperty(@Nonnull PrimitivePropertyContext ctx)
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName()
                + ".exitPrimitiveProperty() not implemented yet");
    }

    @Override
    public void enterEnumerationProperty(@Nonnull EnumerationPropertyContext ctx)
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName()
                + ".enterEnumerationProperty() not implemented yet");
    }

    @Override
    public void exitEnumerationProperty(@Nonnull EnumerationPropertyContext ctx)
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName()
                + ".exitEnumerationProperty() not implemented yet");
    }

    @Override
    public void enterParameterizedProperty(@Nonnull ParameterizedPropertyContext ctx)
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName()
                + ".enterParameterizedProperty() not implemented yet");
    }

    @Override
    public void exitParameterizedProperty(@Nonnull ParameterizedPropertyContext ctx)
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName()
                + ".exitParameterizedProperty() not implemented yet");
    }

    @Override
    public void enterOptionalMarker(@Nonnull OptionalMarkerContext ctx)
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName()
                + ".enterOptionalMarker() not implemented yet");
    }

    @Override
    public void exitOptionalMarker(@Nonnull OptionalMarkerContext ctx)
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName()
                + ".exitOptionalMarker() not implemented yet");
    }

    @Override
    public void enterDataTypePropertyValidation(@Nonnull DataTypePropertyValidationContext ctx)
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName()
                + ".enterDataTypePropertyValidation() not implemented yet");
    }

    @Override
    public void exitDataTypePropertyValidation(@Nonnull DataTypePropertyValidationContext ctx)
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName()
                + ".exitDataTypePropertyValidation() not implemented yet");
    }

    @Override
    public void enterMinLengthValidation(@Nonnull MinLengthValidationContext ctx)
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName()
                + ".enterMinLengthValidation() not implemented yet");
    }

    @Override
    public void exitMinLengthValidation(@Nonnull MinLengthValidationContext ctx)
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName()
                + ".exitMinLengthValidation() not implemented yet");
    }

    @Override
    public void enterMaxLengthValidation(@Nonnull MaxLengthValidationContext ctx)
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName()
                + ".enterMaxLengthValidation() not implemented yet");
    }

    @Override
    public void exitMaxLengthValidation(@Nonnull MaxLengthValidationContext ctx)
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName()
                + ".exitMaxLengthValidation() not implemented yet");
    }

    @Override
    public void enterMinValidation(@Nonnull MinValidationContext ctx)
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName()
                + ".enterMinValidation() not implemented yet");
    }

    @Override
    public void exitMinValidation(@Nonnull MinValidationContext ctx)
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName()
                + ".exitMinValidation() not implemented yet");
    }

    @Override
    public void enterMaxValidation(@Nonnull MaxValidationContext ctx)
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName()
                + ".enterMaxValidation() not implemented yet");
    }

    @Override
    public void exitMaxValidation(@Nonnull MaxValidationContext ctx)
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName()
                + ".exitMaxValidation() not implemented yet");
    }

    @Override
    public void enterParameterDeclaration(@Nonnull ParameterDeclarationContext ctx)
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName()
                + ".enterParameterDeclaration() not implemented yet");
    }

    @Override
    public void exitParameterDeclaration(@Nonnull ParameterDeclarationContext ctx)
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName()
                + ".exitParameterDeclaration() not implemented yet");
    }

    @Override
    public void enterPrimitiveParameterDeclaration(@Nonnull PrimitiveParameterDeclarationContext ctx)
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName()
                + ".enterPrimitiveParameterDeclaration() not implemented yet");
    }

    @Override
    public void exitPrimitiveParameterDeclaration(@Nonnull PrimitiveParameterDeclarationContext ctx)
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName()
                + ".exitPrimitiveParameterDeclaration() not implemented yet");
    }

    @Override
    public void enterEnumerationParameterDeclaration(@Nonnull EnumerationParameterDeclarationContext ctx)
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName()
                + ".enterEnumerationParameterDeclaration() not implemented yet");
    }

    @Override
    public void exitEnumerationParameterDeclaration(@Nonnull EnumerationParameterDeclarationContext ctx)
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName()
                + ".exitEnumerationParameterDeclaration() not implemented yet");
    }

    @Override
    public void enterInvalidParameterDeclaration(@Nonnull InvalidParameterDeclarationContext ctx)
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName()
                + ".enterInvalidParameterDeclaration() not implemented yet");
    }

    @Override
    public void exitInvalidParameterDeclaration(@Nonnull InvalidParameterDeclarationContext ctx)
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName()
                + ".exitInvalidParameterDeclaration() not implemented yet");
    }

    @Override
    public void enterParameterDeclarationList(@Nonnull ParameterDeclarationListContext ctx)
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName()
                + ".enterParameterDeclarationList() not implemented yet");
    }

    @Override
    public void exitParameterDeclarationList(@Nonnull ParameterDeclarationListContext ctx)
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName()
                + ".exitParameterDeclarationList() not implemented yet");
    }

    @Override
    public void enterArgumentList(@Nonnull ArgumentListContext ctx)
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName()
                + ".enterArgumentList() not implemented yet");
    }

    @Override
    public void exitArgumentList(@Nonnull ArgumentListContext ctx)
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName()
                + ".exitArgumentList() not implemented yet");
    }

    @Override
    public void enterArgument(@Nonnull ArgumentContext ctx)
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName()
                + ".enterArgument() not implemented yet");
    }

    @Override
    public void exitArgument(@Nonnull ArgumentContext ctx)
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName()
                + ".exitArgument() not implemented yet");
    }

    @Override
    public void enterMultiplicity(@Nonnull MultiplicityContext ctx)
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName()
                + ".enterMultiplicity() not implemented yet");
    }

    @Override
    public void exitMultiplicity(@Nonnull MultiplicityContext ctx)
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName()
                + ".exitMultiplicity() not implemented yet");
    }

    @Override
    public void enterMultiplicityBody(@Nonnull MultiplicityBodyContext ctx)
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName()
                + ".enterMultiplicityBody() not implemented yet");
    }

    @Override
    public void exitMultiplicityBody(@Nonnull MultiplicityBodyContext ctx)
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName()
                + ".exitMultiplicityBody() not implemented yet");
    }

    @Override
    public void enterPrimitiveType(@Nonnull PrimitiveTypeContext ctx)
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName()
                + ".enterPrimitiveType() not implemented yet");
    }

    @Override
    public void exitPrimitiveType(@Nonnull PrimitiveTypeContext ctx)
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName()
                + ".exitPrimitiveType() not implemented yet");
    }

    @Override
    public void enterClassModifier(@Nonnull ClassModifierContext ctx)
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName()
                + ".enterClassModifier() not implemented yet");
    }

    @Override
    public void exitClassModifier(@Nonnull ClassModifierContext ctx)
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName()
                + ".exitClassModifier() not implemented yet");
    }

    @Override
    public void enterDataTypePropertyModifier(DataTypePropertyModifierContext ctx)
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName()
                + ".enterDataTypePropertyModifier() not implemented yet");
    }

    @Override
    public void exitDataTypePropertyModifier(DataTypePropertyModifierContext ctx)
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName()
                + ".exitDataTypePropertyModifier() not implemented yet");
    }

    @Override
    public void enterAssociationEndModifier(@Nonnull AssociationEndModifierContext ctx)
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName()
                + ".enterAssociationEndModifier() not implemented yet");
    }

    @Override
    public void exitAssociationEndModifier(@Nonnull AssociationEndModifierContext ctx)
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName()
                + ".exitAssociationEndModifier() not implemented yet");
    }

    @Override
    public void enterParameterizedPropertyModifier(@Nonnull ParameterizedPropertyModifierContext ctx)
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName()
                + ".enterParameterizedPropertyModifier() not implemented yet");
    }

    @Override
    public void exitParameterizedPropertyModifier(@Nonnull ParameterizedPropertyModifierContext ctx)
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName()
                + ".exitParameterizedPropertyModifier() not implemented yet");
    }

    @Override
    public void enterParameterModifier(@Nonnull ParameterModifierContext ctx)
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName()
                + ".enterParameterModifier() not implemented yet");
    }

    @Override
    public void exitParameterModifier(@Nonnull ParameterModifierContext ctx)
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName()
                + ".exitParameterModifier() not implemented yet");
    }

    @Override
    public void enterOrderByDeclaration(@Nonnull OrderByDeclarationContext ctx)
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName()
                + ".enterOrderByDeclaration() not implemented yet");
    }

    @Override
    public void exitOrderByDeclaration(@Nonnull OrderByDeclarationContext ctx)
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName()
                + ".exitOrderByDeclaration() not implemented yet");
    }

    @Override
    public void enterOrderByMemberReferencePath(@Nonnull OrderByMemberReferencePathContext ctx)
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName()
                + ".enterOrderByMemberReferencePath() not implemented yet");
    }

    @Override
    public void exitOrderByMemberReferencePath(@Nonnull OrderByMemberReferencePathContext ctx)
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName()
                + ".exitOrderByMemberReferencePath() not implemented yet");
    }

    @Override
    public void enterOrderByDirection(@Nonnull OrderByDirectionContext ctx)
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName()
                + ".enterOrderByDirection() not implemented yet");
    }

    @Override
    public void exitOrderByDirection(@Nonnull OrderByDirectionContext ctx)
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName()
                + ".exitOrderByDirection() not implemented yet");
    }

    @Override
    public void enterCriteriaEdgePoint(@Nonnull CriteriaEdgePointContext ctx)
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName()
                + ".enterCriteriaEdgePoint() not implemented yet");
    }

    @Override
    public void exitCriteriaEdgePoint(@Nonnull CriteriaEdgePointContext ctx)
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName()
                + ".exitCriteriaEdgePoint() not implemented yet");
    }

    @Override
    public void enterCriteriaExpressionAnd(@Nonnull CriteriaExpressionAndContext ctx)
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName()
                + ".enterCriteriaExpressionAnd() not implemented yet");
    }

    @Override
    public void exitCriteriaExpressionAnd(@Nonnull CriteriaExpressionAndContext ctx)
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName()
                + ".exitCriteriaExpressionAnd() not implemented yet");
    }

    @Override
    public void enterCriteriaNative(@Nonnull CriteriaNativeContext ctx)
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName()
                + ".enterCriteriaNative() not implemented yet");
    }

    @Override
    public void exitCriteriaNative(@Nonnull CriteriaNativeContext ctx)
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName()
                + ".exitCriteriaNative() not implemented yet");
    }

    @Override
    public void enterCriteriaExpressionGroup(@Nonnull CriteriaExpressionGroupContext ctx)
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName()
                + ".enterCriteriaExpressionGroup() not implemented yet");
    }

    @Override
    public void exitCriteriaExpressionGroup(@Nonnull CriteriaExpressionGroupContext ctx)
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName()
                + ".exitCriteriaExpressionGroup() not implemented yet");
    }

    @Override
    public void enterCriteriaAll(@Nonnull CriteriaAllContext ctx)
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName()
                + ".enterCriteriaAll() not implemented yet");
    }

    @Override
    public void exitCriteriaAll(@Nonnull CriteriaAllContext ctx)
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName()
                + ".exitCriteriaAll() not implemented yet");
    }

    @Override
    public void enterCriteriaOperator(@Nonnull CriteriaOperatorContext ctx)
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName()
                + ".enterCriteriaOperator() not implemented yet");
    }

    @Override
    public void exitCriteriaOperator(@Nonnull CriteriaOperatorContext ctx)
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName()
                + ".exitCriteriaOperator() not implemented yet");
    }

    @Override
    public void enterCriteriaExpressionOr(@Nonnull CriteriaExpressionOrContext ctx)
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName()
                + ".enterCriteriaExpressionOr() not implemented yet");
    }

    @Override
    public void exitCriteriaExpressionOr(@Nonnull CriteriaExpressionOrContext ctx)
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName()
                + ".exitCriteriaExpressionOr() not implemented yet");
    }

    @Override
    public void enterExpressionValue(@Nonnull ExpressionValueContext ctx)
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName()
                + ".enterExpressionValue() not implemented yet");
    }

    @Override
    public void exitExpressionValue(@Nonnull ExpressionValueContext ctx)
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName()
                + ".exitExpressionValue() not implemented yet");
    }

    @Override
    public void enterExpressionMemberReference(@Nonnull ExpressionMemberReferenceContext ctx)
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName()
                + ".enterExpressionMemberReference() not implemented yet");
    }

    @Override
    public void exitExpressionMemberReference(@Nonnull ExpressionMemberReferenceContext ctx)
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName()
                + ".exitExpressionMemberReference() not implemented yet");
    }

    @Override
    public void enterLiteralList(@Nonnull LiteralListContext ctx)
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName()
                + ".enterLiteralList() not implemented yet");
    }

    @Override
    public void exitLiteralList(@Nonnull LiteralListContext ctx)
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName()
                + ".exitLiteralList() not implemented yet");
    }

    @Override
    public void enterNativeLiteral(@Nonnull NativeLiteralContext ctx)
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName()
                + ".enterNativeLiteral() not implemented yet");
    }

    @Override
    public void exitNativeLiteral(@Nonnull NativeLiteralContext ctx)
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName()
                + ".exitNativeLiteral() not implemented yet");
    }

    @Override
    public void enterOperator(@Nonnull OperatorContext ctx)
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName()
                + ".enterOperator() not implemented yet");
    }

    @Override
    public void exitOperator(@Nonnull OperatorContext ctx)
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName()
                + ".exitOperator() not implemented yet");
    }

    @Override
    public void enterEqualityOperator(@Nonnull EqualityOperatorContext ctx)
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName()
                + ".enterEqualityOperator() not implemented yet");
    }

    @Override
    public void exitEqualityOperator(@Nonnull EqualityOperatorContext ctx)
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName()
                + ".exitEqualityOperator() not implemented yet");
    }

    @Override
    public void enterInequalityOperator(@Nonnull InequalityOperatorContext ctx)
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName()
                + ".enterInequalityOperator() not implemented yet");
    }

    @Override
    public void exitInequalityOperator(@Nonnull InequalityOperatorContext ctx)
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName()
                + ".exitInequalityOperator() not implemented yet");
    }

    @Override
    public void enterInOperator(@Nonnull InOperatorContext ctx)
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName()
                + ".enterInOperator() not implemented yet");
    }

    @Override
    public void exitInOperator(@Nonnull InOperatorContext ctx)
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName()
                + ".exitInOperator() not implemented yet");
    }

    @Override
    public void enterStringOperator(@Nonnull StringOperatorContext ctx)
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName()
                + ".enterStringOperator() not implemented yet");
    }

    @Override
    public void exitStringOperator(@Nonnull StringOperatorContext ctx)
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName()
                + ".exitStringOperator() not implemented yet");
    }

    @Override
    public void enterClassType(@Nonnull ClassTypeContext ctx)
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName()
                + ".enterClassType() not implemented yet");
    }

    @Override
    public void exitClassType(@Nonnull ClassTypeContext ctx)
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName()
                + ".exitClassType() not implemented yet");
    }

    @Override
    public void enterClassifierType(@Nonnull ClassifierTypeContext ctx)
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName()
                + ".enterClassifierType() not implemented yet");
    }

    @Override
    public void exitClassifierType(@Nonnull ClassifierTypeContext ctx)
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName()
                + ".exitClassifierType() not implemented yet");
    }

    @Override
    public void enterInterfaceReference(@Nonnull InterfaceReferenceContext ctx)
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName()
                + ".enterInterfaceReference() not implemented yet");
    }

    @Override
    public void exitInterfaceReference(@Nonnull InterfaceReferenceContext ctx)
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName()
                + ".exitInterfaceReference() not implemented yet");
    }

    @Override
    public void enterClassReference(@Nonnull ClassReferenceContext ctx)
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName()
                + ".enterClassReference() not implemented yet");
    }

    @Override
    public void exitClassReference(@Nonnull ClassReferenceContext ctx)
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName()
                + ".exitClassReference() not implemented yet");
    }

    @Override
    public void enterClassifierReference(@Nonnull ClassifierReferenceContext ctx)
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName()
                + ".enterClassifierReference() not implemented yet");
    }

    @Override
    public void exitClassifierReference(@Nonnull ClassifierReferenceContext ctx)
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName()
                + ".exitClassifierReference() not implemented yet");
    }

    @Override
    public void enterEnumerationReference(@Nonnull EnumerationReferenceContext ctx)
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName()
                + ".enterEnumerationReference() not implemented yet");
    }

    @Override
    public void exitEnumerationReference(@Nonnull EnumerationReferenceContext ctx)
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName()
                + ".exitEnumerationReference() not implemented yet");
    }

    @Override
    public void enterProjectionReference(@Nonnull ProjectionReferenceContext ctx)
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName()
                + ".enterProjectionReference() not implemented yet");
    }

    @Override
    public void exitProjectionReference(@Nonnull ProjectionReferenceContext ctx)
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName()
                + ".exitProjectionReference() not implemented yet");
    }

    @Override
    public void enterMemberReference(@Nonnull MemberReferenceContext ctx)
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName()
                + ".enterMemberReference() not implemented yet");
    }

    @Override
    public void exitMemberReference(@Nonnull MemberReferenceContext ctx)
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName()
                + ".exitMemberReference() not implemented yet");
    }

    @Override
    public void enterAssociationEndReference(@Nonnull AssociationEndReferenceContext ctx)
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName()
                + ".enterAssociationEndReference() not implemented yet");
    }

    @Override
    public void exitAssociationEndReference(@Nonnull AssociationEndReferenceContext ctx)
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName()
                + ".exitAssociationEndReference() not implemented yet");
    }

    @Override
    public void enterVariableReference(@Nonnull VariableReferenceContext ctx)
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName()
                + ".enterVariableReference() not implemented yet");
    }

    @Override
    public void exitVariableReference(@Nonnull VariableReferenceContext ctx)
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName()
                + ".exitVariableReference() not implemented yet");
    }

    @Override
    public void enterThisMemberReferencePath(@Nonnull ThisMemberReferencePathContext ctx)
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName()
                + ".enterThisMemberReferencePath() not implemented yet");
    }

    @Override
    public void exitThisMemberReferencePath(@Nonnull ThisMemberReferencePathContext ctx)
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName()
                + ".exitThisMemberReferencePath() not implemented yet");
    }

    @Override
    public void enterTypeMemberReferencePath(@Nonnull TypeMemberReferencePathContext ctx)
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName()
                + ".enterTypeMemberReferencePath() not implemented yet");
    }

    @Override
    public void exitTypeMemberReferencePath(@Nonnull TypeMemberReferencePathContext ctx)
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName()
                + ".exitTypeMemberReferencePath() not implemented yet");
    }

    @Override
    public void enterIdentifier(@Nonnull IdentifierContext ctx)
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName()
                + ".enterIdentifier() not implemented yet");
    }

    @Override
    public void exitIdentifier(@Nonnull IdentifierContext ctx)
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName()
                + ".exitIdentifier() not implemented yet");
    }

    @Override
    public void enterKeywordValidAsIdentifier(@Nonnull KeywordValidAsIdentifierContext ctx)
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName()
                + ".enterKeywordValidAsIdentifier() not implemented yet");
    }

    @Override
    public void exitKeywordValidAsIdentifier(@Nonnull KeywordValidAsIdentifierContext ctx)
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName()
                + ".exitKeywordValidAsIdentifier() not implemented yet");
    }

    @Override
    public void enterLiteral(@Nonnull LiteralContext ctx)
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName()
                + ".enterLiteral() not implemented yet");
    }

    @Override
    public void exitLiteral(@Nonnull LiteralContext ctx)
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName() + ".exitLiteral() not implemented yet");
    }

    @Override
    public void enterIntegerLiteral(@Nonnull IntegerLiteralContext ctx)
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName()
                + ".enterIntegerLiteral() not implemented yet");
    }

    @Override
    public void exitIntegerLiteral(@Nonnull IntegerLiteralContext ctx)
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName()
                + ".exitIntegerLiteral() not implemented yet");
    }

    @Override
    public void enterFloatingPointLiteral(@Nonnull FloatingPointLiteralContext ctx)
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName()
                + ".enterFloatingPointLiteral() not implemented yet");
    }

    @Override
    public void exitFloatingPointLiteral(@Nonnull FloatingPointLiteralContext ctx)
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName()
                + ".exitFloatingPointLiteral() not implemented yet");
    }

    @Override
    public void enterBooleanLiteral(@Nonnull BooleanLiteralContext ctx)
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName()
                + ".enterBooleanLiteral() not implemented yet");
    }

    @Override
    public void exitBooleanLiteral(@Nonnull BooleanLiteralContext ctx)
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName()
                + ".exitBooleanLiteral() not implemented yet");
    }

    @Override
    public void enterCharacterLiteral(@Nonnull CharacterLiteralContext ctx)
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName()
                + ".enterCharacterLiteral() not implemented yet");
    }

    @Override
    public void exitCharacterLiteral(@Nonnull CharacterLiteralContext ctx)
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName()
                + ".exitCharacterLiteral() not implemented yet");
    }

    @Override
    public void enterStringLiteral(@Nonnull StringLiteralContext ctx)
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName()
                + ".enterStringLiteral() not implemented yet");
    }

    @Override
    public void exitStringLiteral(@Nonnull StringLiteralContext ctx)
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName()
                + ".exitStringLiteral() not implemented yet");
    }

    @Override
    public void enterNullLiteral(@Nonnull NullLiteralContext ctx)
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName()
                + ".enterNullLiteral() not implemented yet");
    }

    @Override
    public void exitNullLiteral(@Nonnull NullLiteralContext ctx)
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName()
                + ".exitNullLiteral() not implemented yet");
    }

    @Override
    public void visitTerminal(@Nonnull TerminalNode node)
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName()
                + ".visitTerminal() not implemented yet");
    }

    @Override
    public void visitErrorNode(@Nonnull ErrorNode node)
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName()
                + ".visitErrorNode() not implemented yet");
    }

    @Override
    public void enterEveryRule(@Nonnull ParserRuleContext ctx)
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName()
                + ".enterEveryRule() not implemented yet");
    }

    @Override
    public void exitEveryRule(@Nonnull ParserRuleContext ctx)
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName()
                + ".exitEveryRule() not implemented yet");
    }
}
