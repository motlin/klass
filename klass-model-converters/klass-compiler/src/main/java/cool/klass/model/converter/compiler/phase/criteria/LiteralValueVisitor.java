package cool.klass.model.converter.compiler.phase.criteria;

import java.util.Objects;
import java.util.Optional;

import javax.annotation.Nonnull;

import cool.klass.model.converter.compiler.CompilerState;
import cool.klass.model.converter.compiler.state.IAntlrElement;
import cool.klass.model.converter.compiler.state.value.literal.AbstractAntlrLiteralValue;
import cool.klass.model.converter.compiler.state.value.literal.AntlrIntegerLiteralValue;
import cool.klass.model.converter.compiler.state.value.literal.AntlrNullLiteral;
import cool.klass.model.converter.compiler.state.value.literal.AntlrStringLiteralValue;
import cool.klass.model.meta.grammar.KlassBaseVisitor;
import cool.klass.model.meta.grammar.KlassParser.BooleanLiteralContext;
import cool.klass.model.meta.grammar.KlassParser.CharacterLiteralContext;
import cool.klass.model.meta.grammar.KlassParser.FloatingPointLiteralContext;
import cool.klass.model.meta.grammar.KlassParser.IntegerLiteralContext;
import cool.klass.model.meta.grammar.KlassParser.NullLiteralContext;
import cool.klass.model.meta.grammar.KlassParser.StringLiteralContext;

public class LiteralValueVisitor extends KlassBaseVisitor<AbstractAntlrLiteralValue>
{
    @Nonnull
    private final CompilerState compilerState;
    @Nonnull
    private final IAntlrElement expressionValueOwner;

    public LiteralValueVisitor(
            @Nonnull CompilerState compilerState,
            @Nonnull IAntlrElement expressionValueOwner)
    {
        this.compilerState        = Objects.requireNonNull(compilerState);
        this.expressionValueOwner = Objects.requireNonNull(expressionValueOwner);
    }

    @Nonnull
    @Override
    public AntlrIntegerLiteralValue visitIntegerLiteral(@Nonnull IntegerLiteralContext ctx)
    {
        int value = Integer.parseInt(ctx.getText());
        return new AntlrIntegerLiteralValue(
                ctx,
                Optional.of(this.compilerState.getCompilerWalkState().getCurrentCompilationUnit()),
                value,
                this.expressionValueOwner);
    }

    @Nonnull
    @Override
    public AbstractAntlrLiteralValue visitFloatingPointLiteral(FloatingPointLiteralContext ctx)
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName()
                + ".visitFloatingPointLiteral() not implemented yet");
    }

    @Nonnull
    @Override
    public AbstractAntlrLiteralValue visitBooleanLiteral(BooleanLiteralContext ctx)
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName()
                + ".visitBooleanLiteral() not implemented yet");
    }

    @Nonnull
    @Override
    public AbstractAntlrLiteralValue visitCharacterLiteral(CharacterLiteralContext ctx)
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName()
                + ".visitCharacterLiteral() not implemented yet");
    }

    @Nonnull
    @Override
    public AbstractAntlrLiteralValue visitStringLiteral(@Nonnull StringLiteralContext ctx)
    {
        String quotedText = ctx.getText();
        String text       = quotedText.substring(1, quotedText.length() - 1);
        return new AntlrStringLiteralValue(
                ctx,
                Optional.of(this.compilerState.getCompilerWalkState().getCurrentCompilationUnit()),
                text,
                this.expressionValueOwner);
    }

    @Nonnull
    @Override
    public AbstractAntlrLiteralValue visitNullLiteral(@Nonnull NullLiteralContext ctx)
    {
        return new AntlrNullLiteral(
                ctx,
                Optional.of(this.compilerState.getCompilerWalkState().getCurrentCompilationUnit()),
                this.expressionValueOwner);
    }
}
