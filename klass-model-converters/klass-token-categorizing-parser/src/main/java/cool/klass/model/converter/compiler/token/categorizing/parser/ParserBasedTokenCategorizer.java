package cool.klass.model.converter.compiler.token.categorizing.parser;

import java.util.LinkedHashMap;

import javax.annotation.Nonnull;

import cool.klass.model.converter.compiler.token.categories.TokenCategory;
import cool.klass.model.meta.grammar.KlassBaseListener;
import cool.klass.model.meta.grammar.KlassParser.AbstractDeclarationContext;
import cool.klass.model.meta.grammar.KlassParser.AssociationDeclarationContext;
import cool.klass.model.meta.grammar.KlassParser.AssociationEndContext;
import cool.klass.model.meta.grammar.KlassParser.AssociationEndModifierContext;
import cool.klass.model.meta.grammar.KlassParser.AssociationEndReferenceContext;
import cool.klass.model.meta.grammar.KlassParser.AssociationEndSignatureContext;
import cool.klass.model.meta.grammar.KlassParser.ClassHeaderContext;
import cool.klass.model.meta.grammar.KlassParser.ClassModifierContext;
import cool.klass.model.meta.grammar.KlassParser.ClassOrUserContext;
import cool.klass.model.meta.grammar.KlassParser.ClassReferenceContext;
import cool.klass.model.meta.grammar.KlassParser.DataTypePropertyModifierContext;
import cool.klass.model.meta.grammar.KlassParser.EnumerationDeclarationContext;
import cool.klass.model.meta.grammar.KlassParser.EnumerationLiteralContext;
import cool.klass.model.meta.grammar.KlassParser.EnumerationParameterDeclarationContext;
import cool.klass.model.meta.grammar.KlassParser.EnumerationPropertyContext;
import cool.klass.model.meta.grammar.KlassParser.EnumerationReferenceContext;
import cool.klass.model.meta.grammar.KlassParser.ExtendsDeclarationContext;
import cool.klass.model.meta.grammar.KlassParser.ImplementsDeclarationContext;
import cool.klass.model.meta.grammar.KlassParser.InOperatorContext;
import cool.klass.model.meta.grammar.KlassParser.InheritanceTypeContext;
import cool.klass.model.meta.grammar.KlassParser.InterfaceHeaderContext;
import cool.klass.model.meta.grammar.KlassParser.InterfaceReferenceContext;
import cool.klass.model.meta.grammar.KlassParser.InvalidParameterDeclarationContext;
import cool.klass.model.meta.grammar.KlassParser.MemberReferenceContext;
import cool.klass.model.meta.grammar.KlassParser.MultiplicityBodyContext;
import cool.klass.model.meta.grammar.KlassParser.NativeLiteralContext;
import cool.klass.model.meta.grammar.KlassParser.OrderByDeclarationContext;
import cool.klass.model.meta.grammar.KlassParser.OrderByDirectionContext;
import cool.klass.model.meta.grammar.KlassParser.PackageDeclarationContext;
import cool.klass.model.meta.grammar.KlassParser.PackageNameContext;
import cool.klass.model.meta.grammar.KlassParser.ParameterModifierContext;
import cool.klass.model.meta.grammar.KlassParser.ParameterizedPropertyContext;
import cool.klass.model.meta.grammar.KlassParser.ParameterizedPropertyModifierContext;
import cool.klass.model.meta.grammar.KlassParser.ParameterizedPropertySignatureContext;
import cool.klass.model.meta.grammar.KlassParser.PrimitiveParameterDeclarationContext;
import cool.klass.model.meta.grammar.KlassParser.PrimitivePropertyContext;
import cool.klass.model.meta.grammar.KlassParser.ProjectionAssociationEndContext;
import cool.klass.model.meta.grammar.KlassParser.ProjectionDeclarationContext;
import cool.klass.model.meta.grammar.KlassParser.ProjectionParameterizedPropertyContext;
import cool.klass.model.meta.grammar.KlassParser.ProjectionPrimitiveMemberContext;
import cool.klass.model.meta.grammar.KlassParser.ProjectionProjectionReferenceContext;
import cool.klass.model.meta.grammar.KlassParser.ProjectionReferenceContext;
import cool.klass.model.meta.grammar.KlassParser.RelationshipContext;
import cool.klass.model.meta.grammar.KlassParser.ServiceCategoryModifierContext;
import cool.klass.model.meta.grammar.KlassParser.ServiceCriteriaKeywordContext;
import cool.klass.model.meta.grammar.KlassParser.ServiceGroupDeclarationContext;
import cool.klass.model.meta.grammar.KlassParser.ServiceMultiplicityContext;
import cool.klass.model.meta.grammar.KlassParser.ServiceMultiplicityDeclarationContext;
import cool.klass.model.meta.grammar.KlassParser.ServiceProjectionDispatchContext;
import cool.klass.model.meta.grammar.KlassParser.StringOperatorContext;
import cool.klass.model.meta.grammar.KlassParser.ThisMemberReferencePathContext;
import cool.klass.model.meta.grammar.KlassParser.VariableReferenceContext;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeWalker;
import org.antlr.v4.runtime.tree.TerminalNode;
import org.eclipse.collections.api.map.MapIterable;
import org.eclipse.collections.api.map.MutableMapIterable;
import org.eclipse.collections.impl.map.ordered.mutable.OrderedMapAdapter;

public class ParserBasedTokenCategorizer
        extends KlassBaseListener
{
    @Nonnull
    private final MutableMapIterable<Token, TokenCategory> tokenCategories =
            OrderedMapAdapter.adapt(new LinkedHashMap<>());

    @Nonnull
    public static MapIterable<Token, TokenCategory> findTokenCategoriesFromParser(@Nonnull ParseTree parseTree)
    {
        ParseTreeWalker             parseTreeWalker = new ParseTreeWalker();
        ParserBasedTokenCategorizer listener        = new ParserBasedTokenCategorizer();
        parseTreeWalker.walk(listener, parseTree);
        return listener.tokenCategories.asUnmodifiable();
    }

    @Override
    public void enterPackageDeclaration(PackageDeclarationContext ctx)
    {
        this.put(
                ctx.KEYWORD_PACKAGE().getSymbol(),
                TokenCategory.PACKAGE_KEYWORD);
    }

    @Override
    public void enterPackageName(PackageNameContext ctx)
    {
        this.put(
                ctx.identifier().getStart(),
                TokenCategory.PACKAGE_NAME);
    }

    @Override
    public void enterClassOrUser(ClassOrUserContext ctx)
    {
        if (ctx.KEYWORD_USER() != null)
        {
            this.put(
                    ctx.getStart(),
                    TokenCategory.KEYWORD_USER);
        }
        else if (ctx.KEYWORD_CLASS() != null)
        {
            this.put(
                    ctx.getStart(),
                    TokenCategory.KEYWORD_CLASS);
        }
        else
        {
            throw new AssertionError(ctx);
        }
    }

    @Override
    public void enterClassHeader(ClassHeaderContext ctx)
    {
        // TODO: Consider a different category for user class names.
        this.put(
                ctx.identifier().getStart(),
                TokenCategory.CLASS_NAME);
    }

    @Override
    public void enterAbstractDeclaration(AbstractDeclarationContext ctx)
    {
        this.put(
                ctx.KEYWORD_ABSTRACT().getSymbol(),
                TokenCategory.KEYWORD_ABSTRACT);
    }

    @Override
    public void enterInheritanceType(InheritanceTypeContext ctx)
    {
        this.put(
                ctx.getStart(),
                TokenCategory.KEYWORD_INHERITANCE_TYPE);
    }

    @Override
    public void enterExtendsDeclaration(ExtendsDeclarationContext ctx)
    {
        this.put(
                ctx.KEYWORD_EXTENDS().getSymbol(),
                TokenCategory.KEYWORD_EXTENDS);
    }

    @Override
    public void enterImplementsDeclaration(ImplementsDeclarationContext ctx)
    {
        this.put(
                ctx.KEYWORD_IMPLEMENTS().getSymbol(),
                TokenCategory.KEYWORD_IMPLEMENTS);
    }

    @Override
    public void enterAssociationDeclaration(AssociationDeclarationContext ctx)
    {
        this.put(
                ctx.KEYWORD_ASSOCIATION().getSymbol(),
                TokenCategory.KEYWORD_ASSOCIATION);
        this.put(
                ctx.identifier().getStart(),
                TokenCategory.ASSOCIATION_NAME);
    }

    @Override
    public void enterAssociationEnd(AssociationEndContext ctx)
    {
        this.put(
                ctx.identifier().getStart(),
                TokenCategory.ASSOCIATION_END_NAME);
    }

    @Override
    public void enterAssociationEndSignature(AssociationEndSignatureContext ctx)
    {
        this.put(
                ctx.identifier().getStart(),
                TokenCategory.ASSOCIATION_END_NAME);
    }

    @Override
    public void enterClassReference(ClassReferenceContext ctx)
    {
        // TODO: Consider a different category for user class names.
        this.put(
                ctx.identifier().getStart(),
                TokenCategory.CLASS_REFERENCE);
    }

    @Override
    public void enterInterfaceReference(InterfaceReferenceContext ctx)
    {
        this.put(
                ctx.identifier().getStart(),
                TokenCategory.INTERFACE_REFERENCE);
    }

    @Override
    public void enterEnumerationReference(EnumerationReferenceContext ctx)
    {
        this.put(
                ctx.identifier().getStart(),
                TokenCategory.ENUMERATION_REFERENCE);
    }

    @Override
    public void enterProjectionReference(ProjectionReferenceContext ctx)
    {
        this.put(
                ctx.identifier().getStart(),
                TokenCategory.PROJECTION_REFERENCE);
    }

    @Override
    public void enterAssociationEndReference(AssociationEndReferenceContext ctx)
    {
        this.put(
                ctx.identifier().getStart(),
                TokenCategory.ASSOCIATION_END_REFERENCE);
    }

    @Override
    public void enterMemberReference(MemberReferenceContext ctx)
    {
        // TODO: This could factor in what kind of property is referenced
        this.put(
                ctx.identifier().getStart(),
                TokenCategory.PROPERTY_REFERENCE);
    }

    // TODO: Rename Variable to Parameter
    @Override
    public void enterVariableReference(VariableReferenceContext ctx)
    {
        this.put(
                ctx.identifier().getStart(),
                TokenCategory.PARAMETER_REFERENCE);
    }

    @Override
    public void enterServiceCategoryModifier(ServiceCategoryModifierContext ctx)
    {
        this.put(
                ctx.getStart(),
                TokenCategory.SERVICE_CATEGORY_MODIFIER);
    }

    @Override
    public void enterClassModifier(ClassModifierContext ctx)
    {
        this.put(
                ctx.getStart(),
                TokenCategory.CLASS_MODIFIER);
    }

    @Override
    public void enterDataTypePropertyModifier(DataTypePropertyModifierContext ctx)
    {
        this.put(
                ctx.getStart(),
                TokenCategory.DATA_TYPE_PROPERTY_MODIFIER);
    }

    @Override
    public void enterAssociationEndModifier(AssociationEndModifierContext ctx)
    {
        this.put(
                ctx.getStart(),
                TokenCategory.ASSOCIATION_END_MODIFIER);
    }

    @Override
    public void enterParameterizedPropertyModifier(ParameterizedPropertyModifierContext ctx)
    {
        this.put(
                ctx.getStart(),
                TokenCategory.PARAMETERIZED_PROPERTY_MODIFIER);
    }

    @Override
    public void enterParameterModifier(ParameterModifierContext ctx)
    {
        this.put(
                ctx.getStart(),
                TokenCategory.PARAMETER_MODIFIER);
    }

    @Override
    public void enterPrimitiveProperty(PrimitivePropertyContext ctx)
    {
        this.put(
                ctx.identifier().getStart(),
                TokenCategory.PRIMITIVE_PROPERTY_NAME);
        this.put(
                ctx.primitiveType().getStart(),
                TokenCategory.PRIMITIVE_TYPE);
    }

    @Override
    public void enterEnumerationProperty(EnumerationPropertyContext ctx)
    {
        this.put(
                ctx.identifier().getStart(),
                TokenCategory.ENUMERATION_PROPERTY_NAME);
    }

    @Override
    public void enterParameterizedProperty(ParameterizedPropertyContext ctx)
    {
        this.put(
                ctx.identifier().getStart(),
                TokenCategory.PARAMETERIZED_PROPERTY_NAME);
    }

    @Override
    public void enterParameterizedPropertySignature(ParameterizedPropertySignatureContext ctx)
    {
        this.put(
                ctx.identifier().getStart(),
                TokenCategory.PARAMETERIZED_PROPERTY_NAME);
    }

    @Override
    public void enterMultiplicityBody(MultiplicityBodyContext ctx)
    {
        TerminalNode terminalNode = ctx.PUNCTUATION_ASTERISK();
        if (terminalNode == null)
        {
            return;
        }

        this.put(
                terminalNode.getSymbol(),
                TokenCategory.ASTERISK_LITERAL);
    }

    @Override
    public void enterEnumerationDeclaration(EnumerationDeclarationContext ctx)
    {
        this.put(
                ctx.KEYWORD_ENUMERATION().getSymbol(),
                TokenCategory.KEYWORD_ENUMERATION);
        this.put(
                ctx.identifier().getStart(),
                TokenCategory.ENUMERATION_NAME);
    }

    @Override
    public void enterEnumerationLiteral(EnumerationLiteralContext ctx)
    {
        this.put(
                ctx.identifier().getStart(),
                TokenCategory.ENUMERATION_LITERAL_NAME);
    }

    @Override
    public void enterInterfaceHeader(InterfaceHeaderContext ctx)
    {
        this.put(
                ctx.KEYWORD_INTERFACE().getSymbol(),
                TokenCategory.KEYWORD_INTERFACE);
        this.put(
                ctx.identifier().getStart(),
                TokenCategory.INTERFACE_NAME);
    }

    @Override
    public void enterProjectionDeclaration(ProjectionDeclarationContext ctx)
    {
        this.put(
                ctx.KEYWORD_PROJECTION().getSymbol(),
                TokenCategory.KEYWORD_PROJECTION);
        this.put(
                ctx.KEYWORD_ON().getSymbol(),
                TokenCategory.KEYWORD_ON);
        this.put(
                ctx.identifier().getStart(),
                TokenCategory.PROJECTION_NAME);
    }

    // TODO: Rename Primitive to Data
    @Override
    public void enterProjectionPrimitiveMember(ProjectionPrimitiveMemberContext ctx)
    {
        this.put(
                ctx.identifier().getStart(),
                TokenCategory.DATA_TYPE_PROPERTY_REFERENCE);
    }

    @Override
    public void enterProjectionAssociationEnd(ProjectionAssociationEndContext ctx)
    {
        this.put(
                ctx.identifier().getStart(),
                TokenCategory.ASSOCIATION_END_REFERENCE);
    }

    @Override
    public void enterProjectionProjectionReference(ProjectionProjectionReferenceContext ctx)
    {
        // TODO: ReferencePropertyReference
        this.put(
                ctx.identifier().getStart(),
                TokenCategory.ASSOCIATION_END_REFERENCE);
    }

    @Override
    public void enterProjectionParameterizedProperty(ProjectionParameterizedPropertyContext ctx)
    {
        this.put(
                ctx.identifier().getStart(),
                TokenCategory.PARAMETERIZED_PROPERTY_REFERENCE);
    }

    @Override
    public void enterPrimitiveParameterDeclaration(PrimitiveParameterDeclarationContext ctx)
    {
        this.put(
                ctx.identifier().getStart(),
                TokenCategory.PARAMETER_NAME);
        this.put(
                ctx.primitiveType().getStart(),
                TokenCategory.PRIMITIVE_TYPE);
    }

    @Override
    public void enterEnumerationParameterDeclaration(EnumerationParameterDeclarationContext ctx)
    {
        this.put(
                ctx.identifier().getStart(),
                TokenCategory.PARAMETER_NAME);
    }

    @Override
    public void enterInvalidParameterDeclaration(InvalidParameterDeclarationContext ctx)
    {
        this.put(
                ctx.identifier().getStart(),
                TokenCategory.PARAMETER_NAME);
    }

    @Override
    public void enterRelationship(RelationshipContext ctx)
    {
        this.put(
                ctx.KEYWORD_RELATIONSHIP().getSymbol(),
                TokenCategory.KEYWORD_RELATIONSHIP);
    }

    @Override
    public void enterOrderByDeclaration(OrderByDeclarationContext ctx)
    {
        this.put(
                ctx.KEYWORD_ORDER_BY().getSymbol(),
                TokenCategory.KEYWORD_ORDER_BY);
    }

    @Override
    public void enterThisMemberReferencePath(ThisMemberReferencePathContext ctx)
    {
        this.put(
                ctx.LITERAL_THIS().getSymbol(),
                TokenCategory.LITERAL_THIS);
    }

    @Override
    public void enterOrderByDirection(OrderByDirectionContext ctx)
    {
        this.put(
                ctx.getStart(),
                TokenCategory.KEYWORD_ORDER_BY_DIRECTION);
    }

    @Override
    public void enterInOperator(InOperatorContext ctx)
    {
        this.put(
                ctx.getStart(),
                TokenCategory.OPERATOR_IN);
    }

    @Override
    public void enterStringOperator(StringOperatorContext ctx)
    {
        this.put(
                ctx.getStart(),
                TokenCategory.OPERATOR_STRING);
    }

    @Override
    public void enterNativeLiteral(NativeLiteralContext ctx)
    {
        this.put(
                ctx.getStart(),
                TokenCategory.LITERAL_NATIVE);
    }

    @Override
    public void enterServiceGroupDeclaration(ServiceGroupDeclarationContext ctx)
    {
        this.put(
                ctx.KEYWORD_SERVICE().getSymbol(),
                TokenCategory.KEYWORD_SERVICE);
    }

    @Override
    public void enterServiceMultiplicityDeclaration(ServiceMultiplicityDeclarationContext ctx)
    {
        this.put(
                ctx.KEYWORD_MULTIPLICITY().getSymbol(),
                TokenCategory.KEYWORD_MULTIPLICITY);
    }

    @Override
    public void enterServiceMultiplicity(ServiceMultiplicityContext ctx)
    {
        this.put(
                ctx.getStart(),
                TokenCategory.KEYWORD_MULTIPLICITY_CHOICE);
    }

    @Override
    public void enterServiceCriteriaKeyword(ServiceCriteriaKeywordContext ctx)
    {
        this.put(
                ctx.getStart(),
                TokenCategory.KEYWORD_SERVICE_CRITERIA);
    }

    @Override
    public void enterServiceProjectionDispatch(ServiceProjectionDispatchContext ctx)
    {
        this.put(
                ctx.KEYWORD_PROJECTION().getSymbol(),
                TokenCategory.KEYWORD_PROJECTION);
    }

    private void put(Token token, TokenCategory tokenCategory)
    {
        this.tokenCategories.put(token, tokenCategory);
    }
}
