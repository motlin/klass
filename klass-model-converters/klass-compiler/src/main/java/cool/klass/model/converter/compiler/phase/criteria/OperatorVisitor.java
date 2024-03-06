package cool.klass.model.converter.compiler.phase.criteria;

import java.util.Objects;

import javax.annotation.Nonnull;

import cool.klass.model.converter.compiler.CompilerState;
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
    private final CompilerState compilerState;

    public OperatorVisitor(CompilerState compilerState)
    {
        this.compilerState = Objects.requireNonNull(compilerState);
    }

    @Nonnull
    @Override
    public AntlrOperator visitTerminal(TerminalNode node)
    {
        throw new AssertionError();
    }

    @Nonnull
    @Override
    public AntlrEqualityOperator visitEqualityOperator(@Nonnull EqualityOperatorContext ctx)
    {
        return new AntlrEqualityOperator(
                ctx,
                this.compilerState.getCompilerWalkState().getCurrentCompilationUnit(),
                this.compilerState.getCompilerInputState().getMacroElement(),
                ctx.getText());
    }

    @Nonnull
    @Override
    public AntlrInequalityOperator visitInequalityOperator(@Nonnull InequalityOperatorContext ctx)
    {
        return new AntlrInequalityOperator(
                ctx,
                this.compilerState.getCompilerWalkState().getCurrentCompilationUnit(),
                this.compilerState.getCompilerInputState().getMacroElement(),
                ctx.getText());
    }

    @Nonnull
    @Override
    public AntlrInOperator visitInOperator(@Nonnull InOperatorContext ctx)
    {
        return new AntlrInOperator(
                ctx,
                this.compilerState.getCompilerWalkState().getCurrentCompilationUnit(),
                this.compilerState.getCompilerInputState().getMacroElement(),
                ctx.getText());
    }

    @Nonnull
    @Override
    public AntlrStringOperator visitStringOperator(@Nonnull StringOperatorContext ctx)
    {
        return new AntlrStringOperator(
                ctx,
                this.compilerState.getCompilerWalkState().getCurrentCompilationUnit(),
                this.compilerState.getCompilerInputState().getMacroElement(),
                ctx.getText());
    }
}
