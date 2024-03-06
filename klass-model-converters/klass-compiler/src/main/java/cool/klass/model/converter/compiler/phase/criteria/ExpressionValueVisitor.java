package cool.klass.model.converter.compiler.phase.criteria;

import java.util.Objects;
import java.util.Optional;

import javax.annotation.Nonnull;

import cool.klass.model.converter.compiler.CompilerState;
import cool.klass.model.converter.compiler.state.AntlrClass;
import cool.klass.model.converter.compiler.state.AntlrClassifier;
import cool.klass.model.converter.compiler.state.IAntlrElement;
import cool.klass.model.converter.compiler.state.property.AntlrAssociationEnd;
import cool.klass.model.converter.compiler.state.property.AntlrDataTypeProperty;
import cool.klass.model.converter.compiler.state.value.AntlrExpressionValue;
import cool.klass.model.converter.compiler.state.value.AntlrThisMemberReferencePath;
import cool.klass.model.converter.compiler.state.value.AntlrTypeMemberReferencePath;
import cool.klass.model.converter.compiler.state.value.AntlrVariableReference;
import cool.klass.model.converter.compiler.state.value.literal.AbstractAntlrLiteralValue;
import cool.klass.model.converter.compiler.state.value.literal.AntlrLiteralListValue;
import cool.klass.model.converter.compiler.state.value.literal.AntlrUserLiteral;
import cool.klass.model.meta.grammar.KlassBaseVisitor;
import cool.klass.model.meta.grammar.KlassParser.AssociationEndReferenceContext;
import cool.klass.model.meta.grammar.KlassParser.IdentifierContext;
import cool.klass.model.meta.grammar.KlassParser.LiteralContext;
import cool.klass.model.meta.grammar.KlassParser.LiteralListContext;
import cool.klass.model.meta.grammar.KlassParser.MemberReferenceContext;
import cool.klass.model.meta.grammar.KlassParser.NativeLiteralContext;
import cool.klass.model.meta.grammar.KlassParser.ThisMemberReferencePathContext;
import cool.klass.model.meta.grammar.KlassParser.TypeMemberReferencePathContext;
import cool.klass.model.meta.grammar.KlassParser.VariableReferenceContext;
import cool.klass.model.meta.grammar.KlassVisitor;
import org.antlr.v4.runtime.tree.TerminalNode;
import org.eclipse.collections.api.list.ImmutableList;
import org.eclipse.collections.api.list.MutableList;
import org.eclipse.collections.impl.factory.Lists;
import org.eclipse.collections.impl.list.mutable.ListAdapter;

public class ExpressionValueVisitor extends KlassBaseVisitor<AntlrExpressionValue>
{
    @Nonnull
    private final CompilerState compilerState;

    @Nonnull
    private final AntlrClassifier thisReference;
    @Nonnull
    private final IAntlrElement   expressionValueOwner;

    public ExpressionValueVisitor(
            @Nonnull CompilerState compilerState,
            @Nonnull AntlrClassifier thisReference,
            IAntlrElement expressionValueOwner)
    {
        this.compilerState        = Objects.requireNonNull(compilerState);
        this.thisReference        = Objects.requireNonNull(thisReference);
        this.expressionValueOwner = Objects.requireNonNull(expressionValueOwner);
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
        AntlrLiteralListValue literalListValue = new AntlrLiteralListValue(
                ctx,
                Optional.of(this.compilerState.getCompilerWalkState().getCurrentCompilationUnit()),
                this.expressionValueOwner);

        ImmutableList<AbstractAntlrLiteralValue> literalStates = ListAdapter.adapt(ctx.literal())
                .collectWith(this::getAntlrLiteralValue, literalListValue)
                .toImmutable();
        literalListValue.setLiteralStates(literalStates);

        return literalListValue;
    }

    private AbstractAntlrLiteralValue getAntlrLiteralValue(
            @Nonnull LiteralContext literalCtx,
            @Nonnull IAntlrElement expressionValueOwner)
    {
        // TODO: Recurse here using a different owner?
        KlassVisitor<AbstractAntlrLiteralValue> visitor = new LiteralValueVisitor(
                this.compilerState,
                expressionValueOwner);
        return visitor.visitLiteral(literalCtx);
    }

    @Nonnull
    @Override
    public AntlrExpressionValue visitNativeLiteral(@Nonnull NativeLiteralContext ctx)
    {
        String keyword = ctx.getText();
        switch (keyword)
        {
            case "user":
            {
                return new AntlrUserLiteral(
                        ctx,
                        Optional.of(this.compilerState.getCompilerWalkState().getCurrentCompilationUnit()),
                        this.expressionValueOwner,
                        this.compilerState.getDomainModelState().getUserClassState());
            }
            default:
            {
                throw new AssertionError(keyword);
            }
        }
    }

    @Nonnull
    @Override
    public AntlrVariableReference visitVariableReference(@Nonnull VariableReferenceContext ctx)
    {
        IdentifierContext identifier   = ctx.identifier();
        String            variableName = identifier.getText();
        return new AntlrVariableReference(
                ctx,
                Optional.of(this.compilerState.getCompilerWalkState().getCurrentCompilationUnit()),
                variableName,
                this.expressionValueOwner);
    }

    @Nonnull
    @Override
    public AntlrThisMemberReferencePath visitThisMemberReferencePath(@Nonnull ThisMemberReferencePathContext ctx)
    {
        MemberReferenceContext memberReferenceContext = ctx.memberReference();

        AntlrClass                       currentClassState    = (AntlrClass) this.thisReference;
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

        return new AntlrThisMemberReferencePath(
                ctx,
                Optional.of(this.compilerState.getCompilerWalkState().getCurrentCompilationUnit()),
                (AntlrClass) this.thisReference,
                associationEndStates.toImmutable(),
                dataTypePropertyState,
                this.expressionValueOwner);
    }

    @Nonnull
    @Override
    public AntlrTypeMemberReferencePath visitTypeMemberReferencePath(@Nonnull TypeMemberReferencePathContext ctx)
    {
        String     className  = ctx.classReference().identifier().getText();
        AntlrClass classState = this.compilerState.getDomainModelState().getClassByName(className);

        MemberReferenceContext memberReferenceContext = ctx.memberReference();

        AntlrClass                       currentClassState    = classState;
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

        return new AntlrTypeMemberReferencePath(
                ctx,
                Optional.of(this.compilerState.getCompilerWalkState().getCurrentCompilationUnit()),
                classState,
                associationEndStates.toImmutable(),
                dataTypePropertyState,
                this.expressionValueOwner);
    }

    @Override
    public AbstractAntlrLiteralValue visitLiteral(LiteralContext ctx)
    {
        return this.getAntlrLiteralValue(ctx, this.expressionValueOwner);
    }
}
