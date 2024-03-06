package cool.klass.model.converter.compiler.phase.criteria;

import java.util.Objects;

import javax.annotation.Nonnull;

import cool.klass.model.converter.compiler.CompilationUnit;
import cool.klass.model.converter.compiler.state.AntlrClass;
import cool.klass.model.converter.compiler.state.AntlrDomainModel;
import cool.klass.model.converter.compiler.state.property.AntlrAssociationEnd;
import cool.klass.model.converter.compiler.state.property.AntlrDataTypeProperty;
import cool.klass.model.converter.compiler.state.service.url.AntlrEnumerationUrlPathParameter;
import cool.klass.model.converter.compiler.state.service.url.AntlrUrlParameter;
import cool.klass.model.converter.compiler.state.value.AntlrExpressionValue;
import cool.klass.model.converter.compiler.state.value.AntlrThisMemberValue;
import cool.klass.model.converter.compiler.state.value.AntlrTypeMemberValue;
import cool.klass.model.converter.compiler.state.value.AntlrVariableReference;
import cool.klass.model.converter.compiler.state.value.literal.AntlrLiteralListValue;
import cool.klass.model.converter.compiler.state.value.literal.AntlrLiteralValue;
import cool.klass.model.converter.compiler.state.value.literal.AntlrUserLiteral;
import cool.klass.model.meta.grammar.KlassBaseVisitor;
import cool.klass.model.meta.grammar.KlassParser.AssociationEndReferenceContext;
import cool.klass.model.meta.grammar.KlassParser.ClassReferenceContext;
import cool.klass.model.meta.grammar.KlassParser.IdentifierContext;
import cool.klass.model.meta.grammar.KlassParser.LiteralContext;
import cool.klass.model.meta.grammar.KlassParser.LiteralListContext;
import cool.klass.model.meta.grammar.KlassParser.MemberReferenceContext;
import cool.klass.model.meta.grammar.KlassParser.NativeLiteralContext;
import cool.klass.model.meta.grammar.KlassParser.ThisMemberReferenceContext;
import cool.klass.model.meta.grammar.KlassParser.TypeMemberReferenceContext;
import cool.klass.model.meta.grammar.KlassParser.VariableReferenceContext;
import cool.klass.model.meta.grammar.KlassVisitor;
import org.antlr.v4.runtime.tree.TerminalNode;
import org.eclipse.collections.api.list.ImmutableList;
import org.eclipse.collections.api.list.MutableList;
import org.eclipse.collections.api.map.OrderedMap;
import org.eclipse.collections.impl.factory.Lists;
import org.eclipse.collections.impl.list.mutable.ListAdapter;

public class ResolveVariableExpressionValueVisitor extends KlassBaseVisitor<AntlrExpressionValue>
{
    @Nonnull
    private final CompilationUnit                       compilationUnit;
    @Nonnull
    private final AntlrClass                            thisReference;
    @Nonnull
    private final AntlrDomainModel                      domainModelState;
    @Nonnull
    private final OrderedMap<String, AntlrUrlParameter> formalParametersByName;

    public ResolveVariableExpressionValueVisitor(
            @Nonnull CompilationUnit compilationUnit,
            @Nonnull AntlrClass thisReference,
            @Nonnull AntlrDomainModel domainModelState,
            @Nonnull OrderedMap<String, AntlrUrlParameter> formalParametersByName)
    {
        this.compilationUnit = Objects.requireNonNull(compilationUnit);
        this.thisReference = Objects.requireNonNull(thisReference);
        this.domainModelState = Objects.requireNonNull(domainModelState);
        this.formalParametersByName = Objects.requireNonNull(formalParametersByName);
    }

    @Nonnull
    @Override
    public AntlrExpressionValue visitTerminal(TerminalNode node)
    {
        throw new AssertionError();
    }

    @Nonnull
    @Override
    public AntlrLiteralListValue visitLiteralList(@Nonnull LiteralListContext ctx)
    {
        ImmutableList<AntlrLiteralValue> literalStates = ListAdapter.adapt(ctx.literal())
                .collect(literalCtx -> new LiteralValueVisitor(
                        this.compilationUnit,
                        this.domainModelState).visitLiteral(literalCtx))
                .toImmutable();
        return new AntlrLiteralListValue(ctx, this.compilationUnit, false, literalStates);
    }

    @Nonnull
    @Override
    public AntlrExpressionValue visitNativeLiteral(@Nonnull NativeLiteralContext ctx)
    {
        String keyword = ctx.getText();
        switch (keyword)
        {
            case "user":
                return new AntlrUserLiteral(ctx, this.compilationUnit, false);
            default:
                throw new AssertionError(keyword);
        }
    }

    @Nonnull
    @Override
    public AntlrVariableReference visitVariableReference(@Nonnull VariableReferenceContext ctx)
    {
        IdentifierContext identifier   = ctx.identifier();
        String            variableName = identifier.getText();
        AntlrUrlParameter antlrUrlParameter = this.formalParametersByName.getIfAbsentValue(
                variableName,
                AntlrEnumerationUrlPathParameter.NOT_FOUND);
        return new AntlrVariableReference(ctx, this.compilationUnit, false, variableName);
    }

    @Nonnull
    @Override
    public AntlrExpressionValue visitThisMemberReference(@Nonnull ThisMemberReferenceContext ctx)
    {
        MemberReferenceContext memberReferenceContext = ctx.memberReference();

        AntlrClass                       currentClassState    = this.thisReference;
        MutableList<AntlrAssociationEnd> associationEndStates = Lists.mutable.empty();
        for (AssociationEndReferenceContext associationEndReferenceContext : ctx.associationEndReference())
        {
            // TODO: Or parameterizedPropertyName?
            String              associationEndName  = associationEndReferenceContext.identifier().getText();
            AntlrAssociationEnd associationEndState = currentClassState.getAssociationEndByName(associationEndName);
            associationEndStates.add(associationEndState);
            currentClassState = associationEndState.getType();
        }

        String                   memberName            = memberReferenceContext.identifier().getText();
        AntlrDataTypeProperty<?> dataTypePropertyState = currentClassState.getDataTypePropertyByName(memberName);

        return new AntlrThisMemberValue(
                ctx,
                this.compilationUnit,
                false,
                this.thisReference,
                associationEndStates.toImmutable(),
                dataTypePropertyState);
    }

    @Nonnull
    @Override
    public AntlrTypeMemberValue visitTypeMemberReference(@Nonnull TypeMemberReferenceContext ctx)
    {
        ClassReferenceContext classReferenceContext = ctx.classReference();
        String                className             = classReferenceContext.identifier().getText();
        AntlrClass            classState            = this.domainModelState.getClassByName(className);

        MemberReferenceContext memberReferenceContext = ctx.memberReference();

        AntlrClass                       currentClassState     = classState;
        MutableList<AntlrAssociationEnd> associationEndStates = Lists.mutable.empty();
        for (AssociationEndReferenceContext associationEndReferenceContext : ctx.associationEndReference())
        {
            // TODO: Or parameterizedPropertyName?
            String              associationEndName  = associationEndReferenceContext.identifier().getText();
            AntlrAssociationEnd associationEndState = currentClassState.getAssociationEndByName(associationEndName);
            associationEndStates.add(associationEndState);
            currentClassState = associationEndState.getType();
        }

        String                   memberName            = memberReferenceContext.identifier().getText();
        AntlrDataTypeProperty<?> dataTypePropertyState = currentClassState.getDataTypePropertyByName(memberName);

        return new AntlrTypeMemberValue(
                ctx,
                this.compilationUnit,
                false,
                classState,
                associationEndStates.toImmutable(),
                dataTypePropertyState);
    }

    @Override
    public AntlrLiteralValue visitLiteral(LiteralContext ctx)
    {
        KlassVisitor<AntlrLiteralValue> literalValueVisitor = new LiteralValueVisitor(
                this.compilationUnit,
                this.domainModelState);
        return literalValueVisitor.visitLiteral(ctx);
    }
}
