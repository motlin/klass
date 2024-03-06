package cool.klass.model.converter.compiler.phase.criteria;

import cool.klass.model.converter.compiler.CompilationUnit;
import cool.klass.model.converter.compiler.state.operator.AntlrEqualityOperator;
import cool.klass.model.converter.compiler.state.operator.AntlrInOperator;
import cool.klass.model.converter.compiler.state.operator.AntlrInequalityOperator;
import cool.klass.model.converter.compiler.state.operator.AntlrOperator;
import cool.klass.model.converter.compiler.state.operator.AntlrStringOperator;
import cool.klass.model.meta.grammar.KlassBaseVisitor;
import cool.klass.model.meta.grammar.KlassParser.EqualityOperatorContext;
import cool.klass.model.meta.grammar.KlassParser.InOperatorContext;
import cool.klass.model.meta.grammar.KlassParser.InequalityOperatorContext;
import cool.klass.model.meta.grammar.KlassParser.StringOperatorContext;
import org.antlr.v4.runtime.tree.TerminalNode;

public class OperatorVisitor extends KlassBaseVisitor<AntlrOperator>
{
    private final CompilationUnit compilationUnit;

    public OperatorVisitor(CompilationUnit compilationUnit)
    {
        this.compilationUnit = compilationUnit;
    }

    @Override
    public AntlrOperator visitTerminal(TerminalNode node)
    {
        throw new AssertionError();
    }

    @Override
    public AntlrEqualityOperator visitEqualityOperator(EqualityOperatorContext ctx)
    {
        return new AntlrEqualityOperator(ctx, this.compilationUnit, false, ctx.getText());
    }

    @Override
    public AntlrInequalityOperator visitInequalityOperator(InequalityOperatorContext ctx)
    {
        return new AntlrInequalityOperator(ctx, this.compilationUnit, false, ctx.getText());
    }

    @Override
    public AntlrInOperator visitInOperator(InOperatorContext ctx)
    {
        return new AntlrInOperator(ctx, this.compilationUnit, false, ctx.getText());
    }

    @Override
    public AntlrStringOperator visitStringOperator(StringOperatorContext ctx)
    {
        return new AntlrStringOperator(ctx, this.compilationUnit, false, ctx.getText());
    }
}
