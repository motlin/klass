package cool.klass.model.converter.compiler.phase.criteria;

import java.util.Objects;

import javax.annotation.Nonnull;

import cool.klass.model.converter.compiler.CompilerState;
import cool.klass.model.converter.compiler.state.IAntlrElement;
import cool.klass.model.converter.compiler.state.criteria.AllAntlrCriteria;
import cool.klass.model.converter.compiler.state.criteria.AntlrAndCriteria;
import cool.klass.model.converter.compiler.state.criteria.AntlrCriteria;
import cool.klass.model.converter.compiler.state.criteria.AntlrOrCriteria;
import cool.klass.model.converter.compiler.state.criteria.EdgePointAntlrCriteria;
import cool.klass.model.converter.compiler.state.criteria.OperatorAntlrCriteria;
import cool.klass.model.converter.compiler.state.operator.AntlrOperator;
import cool.klass.model.converter.compiler.state.value.AntlrExpressionValue;
import cool.klass.model.converter.compiler.state.value.AntlrMemberReferencePath;
import cool.klass.model.meta.grammar.KlassBaseVisitor;
import cool.klass.model.meta.grammar.KlassParser.CriteriaAllContext;
import cool.klass.model.meta.grammar.KlassParser.CriteriaEdgePointContext;
import cool.klass.model.meta.grammar.KlassParser.CriteriaExpressionAndContext;
import cool.klass.model.meta.grammar.KlassParser.CriteriaExpressionGroupContext;
import cool.klass.model.meta.grammar.KlassParser.CriteriaExpressionOrContext;
import cool.klass.model.meta.grammar.KlassParser.CriteriaNativeContext;
import cool.klass.model.meta.grammar.KlassParser.CriteriaOperatorContext;
import cool.klass.model.meta.grammar.KlassParser.EqualityOperatorContext;
import cool.klass.model.meta.grammar.KlassParser.InOperatorContext;
import cool.klass.model.meta.grammar.KlassParser.InequalityOperatorContext;
import cool.klass.model.meta.grammar.KlassParser.LiteralContext;
import cool.klass.model.meta.grammar.KlassParser.LiteralListContext;
import cool.klass.model.meta.grammar.KlassParser.NativeLiteralContext;
import cool.klass.model.meta.grammar.KlassParser.StringOperatorContext;
import cool.klass.model.meta.grammar.KlassParser.TypeMemberReferencePathContext;
import cool.klass.model.meta.grammar.KlassParser.VariableReferenceContext;
import cool.klass.model.meta.grammar.KlassVisitor;
import org.antlr.v4.runtime.tree.TerminalNode;

public class CriteriaVisitor extends KlassBaseVisitor<AntlrCriteria>
{
    @Nonnull
    private final CompilerState compilerState;
    @Nonnull
    private final IAntlrElement criteriaOwner;

    public CriteriaVisitor(
            @Nonnull CompilerState compilerState,
            @Nonnull IAntlrElement criteriaOwner)
    {
        this.compilerState = Objects.requireNonNull(compilerState);
        this.criteriaOwner = Objects.requireNonNull(criteriaOwner);
    }

    @Nonnull
    @Override
    public AntlrCriteria visitCriteriaEdgePoint(@Nonnull CriteriaEdgePointContext ctx)
    {
        EdgePointAntlrCriteria edgePointAntlrCriteria = new EdgePointAntlrCriteria(
                ctx,
                this.compilerState.getCompilerWalkState().getCurrentCompilationUnit(),
                this.compilerState.getCompilerInputState().isInference(),
                this.criteriaOwner);

        KlassVisitor<AntlrExpressionValue> expressionValueVisitor = this.getExpressionValueVisitor(
                edgePointAntlrCriteria);
        AntlrMemberReferencePath memberExpressionValue =
                (AntlrMemberReferencePath) expressionValueVisitor.visitExpressionMemberReference(ctx.expressionMemberReference());
        edgePointAntlrCriteria.setMemberExpressionValue(memberExpressionValue);
        return edgePointAntlrCriteria;
    }

    @Nonnull
    @Override
    public AntlrCriteria visitCriteriaExpressionAnd(@Nonnull CriteriaExpressionAndContext ctx)
    {
        AntlrAndCriteria andCriteriaState = new AntlrAndCriteria(
                ctx,
                this.compilerState.getCompilerWalkState().getCurrentCompilationUnit(),
                this.compilerState.getCompilerInputState().isInference(),
                this.criteriaOwner);

        KlassVisitor<AntlrCriteria> criteriaVisitor = new CriteriaVisitor(
                this.compilerState,
                andCriteriaState);

        AntlrCriteria left  = criteriaVisitor.visit(ctx.left);
        AntlrCriteria right = criteriaVisitor.visit(ctx.right);

        andCriteriaState.setLeft(left);
        andCriteriaState.setRight(right);

        return andCriteriaState;
    }

    @Nonnull
    @Override
    public AntlrCriteria visitCriteriaNative(CriteriaNativeContext ctx)
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName()
                + ".visitCriteriaNative() not implemented yet");
    }

    @Nonnull
    @Override
    public AntlrCriteria visitCriteriaExpressionGroup(CriteriaExpressionGroupContext ctx)
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName()
                + ".visitCriteriaExpressionGroup() not implemented yet");
    }

    @Nonnull
    @Override
    public AntlrCriteria visitCriteriaAll(@Nonnull CriteriaAllContext ctx)
    {
        return new AllAntlrCriteria(
                ctx,
                this.compilerState.getCompilerWalkState().getCurrentCompilationUnit(),
                this.compilerState.getCompilerInputState().isInference(),
                this.criteriaOwner);
    }

    @Nonnull
    @Override
    public OperatorAntlrCriteria visitCriteriaOperator(@Nonnull CriteriaOperatorContext ctx)
    {
        KlassVisitor<AntlrOperator> operatorVisitor = new OperatorVisitor(this.compilerState);
        AntlrOperator               operator        = operatorVisitor.visitOperator(ctx.operator());

        OperatorAntlrCriteria operatorAntlrCriteria = new OperatorAntlrCriteria(
                ctx,
                this.compilerState.getCompilerWalkState().getCurrentCompilationUnit(),
                this.compilerState.getCompilerInputState().isInference(),
                this.criteriaOwner,
                operator);

        // TODO: This is probably backwards
        operator.setOwningOperatorAntlrCriteria(operatorAntlrCriteria);

        KlassVisitor<AntlrExpressionValue> expressionValueVisitor = this.getExpressionValueVisitor(operatorAntlrCriteria);

        AntlrExpressionValue sourceValue = expressionValueVisitor.visitExpressionValue(ctx.source);
        AntlrExpressionValue targetValue = expressionValueVisitor.visitExpressionValue(ctx.target);

        operatorAntlrCriteria.setSourceValue(sourceValue);
        operatorAntlrCriteria.setTargetValue(targetValue);

        return operatorAntlrCriteria;
    }

    @Nonnull
    @Override
    public AntlrCriteria visitCriteriaExpressionOr(@Nonnull CriteriaExpressionOrContext ctx)
    {
        AntlrOrCriteria orCriteriaState = new AntlrOrCriteria(
                ctx,
                this.compilerState.getCompilerWalkState().getCurrentCompilationUnit(),
                this.compilerState.getCompilerInputState().isInference(),
                this.criteriaOwner);

        KlassVisitor<AntlrCriteria> criteriaVisitor = new CriteriaVisitor(
                this.compilerState,
                orCriteriaState);

        AntlrCriteria left  = criteriaVisitor.visit(ctx.left);
        AntlrCriteria right = criteriaVisitor.visit(ctx.right);

        orCriteriaState.setLeft(left);
        orCriteriaState.setRight(right);

        return orCriteriaState;
    }

    @Nonnull
    @Override
    public AntlrCriteria visitLiteralList(LiteralListContext ctx)
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName()
                + ".visitLiteralList() not implemented yet");
    }

    @Nonnull
    @Override
    public AntlrCriteria visitNativeLiteral(NativeLiteralContext ctx)
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName()
                + ".visitNativeLiteral() not implemented yet");
    }

    @Nonnull
    @Override
    public AntlrCriteria visitEqualityOperator(EqualityOperatorContext ctx)
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName()
                + ".visitEqualityOperator() not implemented yet");
    }

    @Nonnull
    @Override
    public AntlrCriteria visitInequalityOperator(InequalityOperatorContext ctx)
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName()
                + ".visitInequalityOperator() not implemented yet");
    }

    @Nonnull
    @Override
    public AntlrCriteria visitInOperator(InOperatorContext ctx)
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName()
                + ".visitInOperator() not implemented yet");
    }

    @Nonnull
    @Override
    public AntlrCriteria visitStringOperator(StringOperatorContext ctx)
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName()
                + ".visitStringOperator() not implemented yet");
    }

    @Nonnull
    @Override
    public AntlrCriteria visitVariableReference(VariableReferenceContext ctx)
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName()
                + ".visitVariableReference() not implemented yet");
    }

    @Nonnull
    @Override
    public AntlrCriteria visitTypeMemberReferencePath(TypeMemberReferencePathContext ctx)
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName()
                + ".visitTypeMemberReferencePath() not implemented yet");
    }

    @Nonnull
    @Override
    public AntlrCriteria visitLiteral(LiteralContext ctx)
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName()
                + ".visitLiteral() not implemented yet");
    }

    @Nonnull
    private ExpressionValueVisitor getExpressionValueVisitor(IAntlrElement expressionValueOwner)
    {
        return new ExpressionValueVisitor(
                this.compilerState,
                this.compilerState.getCompilerWalkState().getThisReference(),
                expressionValueOwner);
    }

    @Nonnull
    @Override
    public AntlrCriteria visitTerminal(TerminalNode node)
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName()
                + ".visitTerminal() not implemented yet");
    }
}
