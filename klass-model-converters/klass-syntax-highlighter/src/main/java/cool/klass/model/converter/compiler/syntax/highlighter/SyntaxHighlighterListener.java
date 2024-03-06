package cool.klass.model.converter.compiler.syntax.highlighter;

import java.util.Objects;

import javax.annotation.Nonnull;

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
import org.antlr.v4.runtime.TokenStreamRewriter;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeListener;
import org.antlr.v4.runtime.tree.ParseTreeWalker;
import org.antlr.v4.runtime.tree.TerminalNode;
import org.eclipse.collections.api.map.MutableMapIterable;

public class SyntaxHighlighterListener
        extends KlassBaseListener
{
    @Nonnull
    private final MutableMapIterable<Token, Color> parserColors;
    @Nonnull
    private final ColorScheme                      colorScheme;

    public SyntaxHighlighterListener(
            @Nonnull MutableMapIterable<Token, Color> parserColors,
            @Nonnull ColorScheme colorScheme)
    {
        this.parserColors = Objects.requireNonNull(parserColors);
        this.colorScheme  = Objects.requireNonNull(colorScheme);
    }

    public static void getParserColors(
            @Nonnull ParseTree parseTree,
            @Nonnull MutableMapIterable<Token, Color> parserColors,
            @Nonnull ColorScheme colorScheme)
    {
        ParseTreeWalker parseTreeWalker = new ParseTreeWalker();
        ParseTreeListener listener = new SyntaxHighlighterListener(
                parserColors,
                colorScheme);
        parseTreeWalker.walk(listener, parseTree);
    }

    public static void colorize(Color color, Token token, TokenStreamRewriter tokenStreamRewriter)
    {
        tokenStreamRewriter.insertBefore(token, color.getBefore());
        color.getAfter().ifPresent(after -> tokenStreamRewriter.insertAfter(token, after));
    }

    @Override
    public void enterPackageDeclaration(PackageDeclarationContext ctx)
    {
        this.put(
                ctx.KEYWORD_PACKAGE().getSymbol(),
                this.colorScheme.getPackageKeyword());
    }

    @Override
    public void enterPackageName(PackageNameContext ctx)
    {
        this.put(
                ctx.identifier().getStart(),
                this.colorScheme.getPackageName());
    }

    @Override
    public void enterClassOrUser(ClassOrUserContext ctx)
    {
        this.put(
                ctx.getStart(),
                this.colorScheme.getKeyword());
    }

    @Override
    public void enterClassHeader(ClassHeaderContext ctx)
    {
        // TODO: Background color for user class name and reference
        this.put(
                ctx.identifier().getStart(),
                this.colorScheme.getClassName());
    }

    @Override
    public void enterAbstractDeclaration(AbstractDeclarationContext ctx)
    {
        this.put(
                ctx.KEYWORD_ABSTRACT().getSymbol(),
                this.colorScheme.getKeyword());
    }

    @Override
    public void enterInheritanceType(InheritanceTypeContext ctx)
    {
        this.put(
                ctx.getStart(),
                this.colorScheme.getKeyword());
    }

    @Override
    public void enterExtendsDeclaration(ExtendsDeclarationContext ctx)
    {
        this.put(
                ctx.KEYWORD_EXTENDS().getSymbol(),
                this.colorScheme.getKeyword());
    }

    @Override
    public void enterImplementsDeclaration(ImplementsDeclarationContext ctx)
    {
        this.put(
                ctx.KEYWORD_IMPLEMENTS().getSymbol(),
                this.colorScheme.getKeyword());
    }

    @Override
    public void enterAssociationDeclaration(AssociationDeclarationContext ctx)
    {
        this.put(
                ctx.KEYWORD_ASSOCIATION().getSymbol(),
                this.colorScheme.getKeyword());
        this.put(
                ctx.identifier().getStart(),
                this.colorScheme.getAssociationName());
    }

    @Override
    public void enterAssociationEnd(AssociationEndContext ctx)
    {
        this.put(
                ctx.identifier().getStart(),
                this.colorScheme.getAssociationEndName());
    }

    @Override
    public void enterAssociationEndSignature(AssociationEndSignatureContext ctx)
    {
        this.put(
                ctx.identifier().getStart(),
                this.colorScheme.getAssociationEndName());
    }

    @Override
    public void enterClassReference(ClassReferenceContext ctx)
    {
        // TODO: Background color for user class name and reference
        this.put(
                ctx.identifier().getStart(),
                this.colorScheme.getClassReference());
    }

    @Override
    public void enterInterfaceReference(InterfaceReferenceContext ctx)
    {
        this.put(
                ctx.identifier().getStart(),
                this.colorScheme.getInterfaceReference());
    }

    @Override
    public void enterEnumerationReference(EnumerationReferenceContext ctx)
    {
        this.put(
                ctx.identifier().getStart(),
                this.colorScheme.getEnumerationReference());
    }

    @Override
    public void enterProjectionReference(ProjectionReferenceContext ctx)
    {
        this.put(
                ctx.identifier().getStart(),
                this.colorScheme.getProjectionReference());
    }

    @Override
    public void enterAssociationEndReference(AssociationEndReferenceContext ctx)
    {
        this.put(
                ctx.identifier().getStart(),
                this.colorScheme.getAssociationEndReference());
    }

    @Override
    public void enterMemberReference(MemberReferenceContext ctx)
    {
        // TODO: This could factor in what kind of property is referenced
        this.put(
                ctx.identifier().getStart(),
                this.colorScheme.getMemberReference());
    }

    // TODO: Rename Variable to Parameter
    @Override
    public void enterVariableReference(VariableReferenceContext ctx)
    {
        this.put(
                ctx.identifier().getStart(),
                this.colorScheme.getParameterReference());
    }

    @Override
    public void enterServiceCategoryModifier(ServiceCategoryModifierContext ctx)
    {
        this.put(
                ctx.getStart(),
                this.colorScheme.getServiceCategoryModifier());
    }

    @Override
    public void enterClassModifier(ClassModifierContext ctx)
    {
        this.put(
                ctx.getStart(),
                this.colorScheme.getClassModifier());
    }

    @Override
    public void enterDataTypePropertyModifier(DataTypePropertyModifierContext ctx)
    {
        this.put(
                ctx.getStart(),
                this.colorScheme.getPropertyModifier());
    }

    @Override
    public void enterAssociationEndModifier(AssociationEndModifierContext ctx)
    {
        this.put(
                ctx.getStart(),
                this.colorScheme.getAssociationEndModifier());
    }

    @Override
    public void enterParameterizedPropertyModifier(ParameterizedPropertyModifierContext ctx)
    {
        this.put(
                ctx.getStart(),
                this.colorScheme.getParameterizedPropertyModifier());
    }

    @Override
    public void enterParameterModifier(ParameterModifierContext ctx)
    {
        this.put(
                ctx.getStart(),
                this.colorScheme.getParameterModifier());
    }

    @Override
    public void enterPrimitiveProperty(PrimitivePropertyContext ctx)
    {
        this.put(
                ctx.identifier().getStart(),
                this.colorScheme.getPrimitivePropertyName());
        this.put(
                ctx.primitiveType().getStart(),
                this.colorScheme.getPrimitiveType());
    }

    @Override
    public void enterEnumerationProperty(EnumerationPropertyContext ctx)
    {
        this.put(
                ctx.identifier().getStart(),
                this.colorScheme.getEnumerationPropertyName());
    }

    @Override
    public void enterParameterizedProperty(ParameterizedPropertyContext ctx)
    {
        this.put(
                ctx.identifier().getStart(),
                this.colorScheme.getParameterizedPropertyName());
    }

    @Override
    public void enterParameterizedPropertySignature(ParameterizedPropertySignatureContext ctx)
    {
        this.put(
                ctx.identifier().getStart(),
                this.colorScheme.getParameterizedPropertyName());
    }

    @Override
    public void enterMultiplicityBody(MultiplicityBodyContext ctx)
    {
        TerminalNode terminalNode = ctx.PUNCTUATION_ASTERISK();
        if (terminalNode == null)
        {
            return;
        }

        // TODO: Asterisk literal
        this.put(
                terminalNode.getSymbol(),
                this.colorScheme.getIntegerLiteral());
    }

    @Override
    public void enterEnumerationDeclaration(EnumerationDeclarationContext ctx)
    {
        this.put(
                ctx.KEYWORD_ENUMERATION().getSymbol(),
                this.colorScheme.getKeyword());
        this.put(
                ctx.identifier().getStart(),
                this.colorScheme.getEnumerationName());
    }

    @Override
    public void enterEnumerationLiteral(EnumerationLiteralContext ctx)
    {
        this.put(
                ctx.identifier().getStart(),
                this.colorScheme.getEnumerationLiteralName());
    }

    @Override
    public void enterInterfaceHeader(InterfaceHeaderContext ctx)
    {
        this.put(
                ctx.KEYWORD_INTERFACE().getSymbol(),
                this.colorScheme.getKeyword());
        this.put(
                ctx.identifier().getStart(),
                this.colorScheme.getInterfaceName());
    }

    @Override
    public void enterProjectionDeclaration(ProjectionDeclarationContext ctx)
    {
        this.put(
                ctx.KEYWORD_PROJECTION().getSymbol(),
                this.colorScheme.getKeyword());
        this.put(
                ctx.KEYWORD_ON().getSymbol(),
                this.colorScheme.getKeyword());
        this.put(
                ctx.identifier().getStart(),
                this.colorScheme.getProjectionName());
    }

    // TODO: Rename Primitive to Data
    @Override
    public void enterProjectionPrimitiveMember(ProjectionPrimitiveMemberContext ctx)
    {
        this.put(
                ctx.identifier().getStart(),
                this.colorScheme.getDataTypePropertyReference());
    }

    @Override
    public void enterProjectionAssociationEnd(ProjectionAssociationEndContext ctx)
    {
        this.put(
                ctx.identifier().getStart(),
                this.colorScheme.getAssociationEndReference());
    }

    @Override
    public void enterProjectionProjectionReference(ProjectionProjectionReferenceContext ctx)
    {
        // TODO: ReferencePropertyReference
        this.put(
                ctx.identifier().getStart(),
                this.colorScheme.getAssociationEndReference());
    }

    @Override
    public void enterProjectionParameterizedProperty(ProjectionParameterizedPropertyContext ctx)
    {
        this.put(
                ctx.identifier().getStart(),
                this.colorScheme.getParameterizedPropertyReference());
    }

    @Override
    public void enterPrimitiveParameterDeclaration(PrimitiveParameterDeclarationContext ctx)
    {
        this.put(
                ctx.identifier().getStart(),
                this.colorScheme.getParameterName());
        this.put(
                ctx.primitiveType().getStart(),
                this.colorScheme.getPrimitiveType());
    }

    @Override
    public void enterEnumerationParameterDeclaration(EnumerationParameterDeclarationContext ctx)
    {
        this.put(
                ctx.identifier().getStart(),
                this.colorScheme.getParameterName());
    }

    @Override
    public void enterInvalidParameterDeclaration(InvalidParameterDeclarationContext ctx)
    {
        this.put(
                ctx.identifier().getStart(),
                this.colorScheme.getParameterName());
    }

    @Override
    public void enterRelationship(RelationshipContext ctx)
    {
        this.put(
                ctx.KEYWORD_RELATIONSHIP().getSymbol(),
                this.colorScheme.getKeyword());
    }

    @Override
    public void enterOrderByDeclaration(OrderByDeclarationContext ctx)
    {
        this.put(
                ctx.KEYWORD_ORDER_BY().getSymbol(),
                this.colorScheme.getKeyword());
    }

    @Override
    public void enterThisMemberReferencePath(ThisMemberReferencePathContext ctx)
    {
        this.put(
                ctx.LITERAL_THIS().getSymbol(),
                this.colorScheme.getKeyword());
    }

    @Override
    public void enterOrderByDirection(OrderByDirectionContext ctx)
    {
        this.put(
                ctx.getStart(),
                this.colorScheme.getKeyword());
    }

    @Override
    public void enterInOperator(InOperatorContext ctx)
    {
        this.put(
                ctx.getStart(),
                this.colorScheme.getKeyword());
    }

    @Override
    public void enterStringOperator(StringOperatorContext ctx)
    {
        this.put(
                ctx.getStart(),
                this.colorScheme.getKeyword());
    }

    @Override
    public void enterNativeLiteral(NativeLiteralContext ctx)
    {
        this.put(
                ctx.getStart(),
                this.colorScheme.getKeyword());
    }

    @Override
    public void enterServiceGroupDeclaration(ServiceGroupDeclarationContext ctx)
    {
        this.put(
                ctx.KEYWORD_SERVICE().getSymbol(),
                this.colorScheme.getKeyword());
    }

    @Override
    public void enterServiceMultiplicityDeclaration(ServiceMultiplicityDeclarationContext ctx)
    {
        this.put(
                ctx.KEYWORD_MULTIPLICITY().getSymbol(),
                this.colorScheme.getKeyword());
    }

    @Override
    public void enterServiceMultiplicity(ServiceMultiplicityContext ctx)
    {
        this.put(
                ctx.getStart(),
                this.colorScheme.getKeyword());
    }

    @Override
    public void enterServiceCriteriaKeyword(ServiceCriteriaKeywordContext ctx)
    {
        this.put(
                ctx.getStart(),
                this.colorScheme.getKeyword());
    }

    @Override
    public void enterServiceProjectionDispatch(ServiceProjectionDispatchContext ctx)
    {
        this.put(
                ctx.KEYWORD_PROJECTION().getSymbol(),
                this.colorScheme.getKeyword());
    }

    private void put(Token token, Color color)
    {
        this.parserColors.put(token, color);
    }
}
