package cool.klass.model.converter.compiler.phase.criteria;

import java.util.Objects;

import javax.annotation.Nonnull;

import cool.klass.model.converter.compiler.CompilationUnit;
import cool.klass.model.converter.compiler.state.AntlrDomainModel;
import cool.klass.model.converter.compiler.state.IAntlrElement;
import cool.klass.model.converter.compiler.state.value.literal.AntlrIntegerLiteralValue;
import cool.klass.model.converter.compiler.state.value.literal.AntlrLiteralValue;
import cool.klass.model.converter.compiler.state.value.literal.AntlrStringLiteralValue;
import cool.klass.model.meta.grammar.KlassBaseVisitor;
import cool.klass.model.meta.grammar.KlassParser.BooleanLiteralContext;
import cool.klass.model.meta.grammar.KlassParser.CharacterLiteralContext;
import cool.klass.model.meta.grammar.KlassParser.FloatingPointLiteralContext;
import cool.klass.model.meta.grammar.KlassParser.IntegerLiteralContext;
import cool.klass.model.meta.grammar.KlassParser.NullLiteralContext;
import cool.klass.model.meta.grammar.KlassParser.StringLiteralContext;

public class LiteralValueVisitor extends KlassBaseVisitor<AntlrLiteralValue>
{
    @Nonnull
    private final CompilationUnit  compilationUnit;
    @Nonnull
    private final AntlrDomainModel domainModelState;
    private final IAntlrElement    expressionValueOwner;

    public LiteralValueVisitor(
            @Nonnull CompilationUnit compilationUnit,
            @Nonnull AntlrDomainModel domainModelState,
            IAntlrElement expressionValueOwner)
    {
        this.compilationUnit = Objects.requireNonNull(compilationUnit);
        this.domainModelState = Objects.requireNonNull(domainModelState);
        this.expressionValueOwner = Objects.requireNonNull(expressionValueOwner);
    }

    @Nonnull
    @Override
    public AntlrIntegerLiteralValue visitIntegerLiteral(@Nonnull IntegerLiteralContext ctx)
    {
        int value = Integer.parseInt(ctx.getText());
        return new AntlrIntegerLiteralValue(ctx, this.compilationUnit, false, value, this.expressionValueOwner);
    }

    @Nonnull
    @Override
    public AntlrLiteralValue visitFloatingPointLiteral(FloatingPointLiteralContext ctx)
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName()
                + ".visitFloatingPointLiteral() not implemented yet");
    }

    @Nonnull
    @Override
    public AntlrLiteralValue visitBooleanLiteral(BooleanLiteralContext ctx)
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName()
                + ".visitBooleanLiteral() not implemented yet");
    }

    @Nonnull
    @Override
    public AntlrLiteralValue visitCharacterLiteral(CharacterLiteralContext ctx)
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName()
                + ".visitCharacterLiteral() not implemented yet");
    }

    @Nonnull
    @Override
    public AntlrLiteralValue visitStringLiteral(@Nonnull StringLiteralContext ctx)
    {
        String quotedText = ctx.getText();
        String text       = quotedText.substring(1, quotedText.length() - 1);
        return new AntlrStringLiteralValue(ctx, this.compilationUnit, false, text, this.expressionValueOwner);
    }

    @Nonnull
    @Override
    public AntlrLiteralValue visitNullLiteral(NullLiteralContext ctx)
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName()
                + ".visitNullLiteral() not implemented yet");
    }
}
