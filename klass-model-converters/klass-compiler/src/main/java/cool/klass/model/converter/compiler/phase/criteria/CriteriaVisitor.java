package cool.klass.model.converter.compiler.phase.criteria;

import java.util.Objects;

import javax.annotation.Nonnull;

import cool.klass.model.converter.compiler.CompilationUnit;
import cool.klass.model.converter.compiler.state.AntlrClass;
import cool.klass.model.converter.compiler.state.AntlrDomainModel;
import cool.klass.model.converter.compiler.state.criteria.AllAntlrCriteria;
import cool.klass.model.converter.compiler.state.criteria.AndAntlrCriteria;
import cool.klass.model.converter.compiler.state.criteria.AntlrCriteria;
import cool.klass.model.converter.compiler.state.criteria.EdgePointAntlrCriteria;
import cool.klass.model.converter.compiler.state.criteria.OperatorAntlrCriteria;
import cool.klass.model.converter.compiler.state.criteria.OrAntlrCriteria;
import cool.klass.model.converter.compiler.state.operator.AntlrOperator;
import cool.klass.model.converter.compiler.state.service.AntlrCriteriaOwner;
import cool.klass.model.converter.compiler.state.value.AntlrExpressionValue;
import cool.klass.model.converter.compiler.state.value.AntlrMemberExpressionValue;
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
    private final CompilationUnit    compilationUnit;
    @Nonnull
    private final AntlrDomainModel   domainModelState;
    @Nonnull
    private final AntlrCriteriaOwner criteriaOwner;
    @Nonnull
    private final AntlrClass         thisReference;

    public CriteriaVisitor(
            @Nonnull CompilationUnit compilationUnit,
            @Nonnull AntlrDomainModel domainModelState,
            @Nonnull AntlrCriteriaOwner criteriaOwner,
            @Nonnull AntlrClass thisReference)
    {
        this.compilationUnit = Objects.requireNonNull(compilationUnit);
        this.domainModelState = Objects.requireNonNull(domainModelState);
        this.criteriaOwner = Objects.requireNonNull(criteriaOwner);
        this.thisReference = Objects.requireNonNull(thisReference);
    }

    @Nonnull
    @Override
    public AntlrCriteria visitCriteriaEdgePoint(@Nonnull CriteriaEdgePointContext ctx)
    {
        KlassVisitor<AntlrExpressionValue> expressionValueVisitor = this.getExpressionValueVisitor();

        AntlrMemberExpressionValue memberExpressionValue =
                (AntlrMemberExpressionValue) expressionValueVisitor.visitExpressionMemberReference(ctx.expressionMemberReference());

        EdgePointAntlrCriteria edgePointAntlrCriteria = new EdgePointAntlrCriteria(
                ctx,
                this.compilationUnit,
                false,
                this.criteriaOwner,
                memberExpressionValue);
        this.criteriaOwner.setCriteria(edgePointAntlrCriteria);
        return edgePointAntlrCriteria;
    }

    @Nonnull
    @Override
    public AntlrCriteria visitCriteriaExpressionAnd(@Nonnull CriteriaExpressionAndContext ctx)
    {
        AntlrCriteria left  = this.visit(ctx.left);
        AntlrCriteria right = this.visit(ctx.right);
        AndAntlrCriteria andAntlrCriteria = new AndAntlrCriteria(
                ctx,
                this.compilationUnit,
                false,
                this.criteriaOwner,
                left,
                right);
        this.criteriaOwner.setCriteria(andAntlrCriteria);
        return andAntlrCriteria;
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

    @Override
    public AntlrCriteria visitCriteriaAll(CriteriaAllContext ctx)
    {
        return new AllAntlrCriteria(ctx, this.compilationUnit, false, this.criteriaOwner);
    }

    @Nonnull
    @Override
    public OperatorAntlrCriteria visitCriteriaOperator(@Nonnull CriteriaOperatorContext ctx)
    {
        KlassVisitor<AntlrOperator> operatorVisitor = new OperatorVisitor(this.compilationUnit);
        AntlrOperator               operator        = operatorVisitor.visitOperator(ctx.operator());

        KlassVisitor<AntlrExpressionValue> expressionValueVisitor = this.getExpressionValueVisitor();

        AntlrExpressionValue sourceValue = expressionValueVisitor.visitExpressionValue(ctx.source);
        AntlrExpressionValue targetValue = expressionValueVisitor.visitExpressionValue(ctx.target);

        OperatorAntlrCriteria operatorAntlrCriteria = new OperatorAntlrCriteria(
                ctx,
                this.compilationUnit,
                false,
                this.criteriaOwner,
                operator,
                sourceValue,
                targetValue);
        this.criteriaOwner.setCriteria(operatorAntlrCriteria);
        return operatorAntlrCriteria;
    }

    @Nonnull
    @Override
    public AntlrCriteria visitCriteriaExpressionOr(@Nonnull CriteriaExpressionOrContext ctx)
    {
        AntlrCriteria left  = this.visit(ctx.left);
        AntlrCriteria right = this.visit(ctx.right);
        OrAntlrCriteria orAntlrCriteria = new OrAntlrCriteria(
                ctx,
                this.compilationUnit,
                false,
                this.criteriaOwner,
                left,
                right);
        this.criteriaOwner.setCriteria(orAntlrCriteria);
        return orAntlrCriteria;
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
    private ExpressionValueVisitor getExpressionValueVisitor()
    {
        return new ExpressionValueVisitor(
                this.compilationUnit,
                this.thisReference,
                this.domainModelState);
    }

    @Nonnull
    @Override
    public AntlrCriteria visitTerminal(TerminalNode node)
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName()
                + ".visitTerminal() not implemented yet");
    }
}
