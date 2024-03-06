package cool.klass.model.converter.compiler.state.value.literal;

import java.util.Objects;
import java.util.Optional;

import javax.annotation.Nonnull;

import cool.klass.model.converter.compiler.CompilationUnit;
import cool.klass.model.converter.compiler.annotation.CompilerAnnotationState;
import cool.klass.model.converter.compiler.state.AntlrPrimitiveType;
import cool.klass.model.converter.compiler.state.AntlrType;
import cool.klass.model.converter.compiler.state.IAntlrElement;
import cool.klass.model.converter.compiler.state.value.AntlrExpressionValueVisitor;
import cool.klass.model.meta.domain.value.literal.NullLiteralImpl.NullLiteralBuilder;
import cool.klass.model.meta.grammar.KlassParser.NullLiteralContext;
import org.eclipse.collections.api.list.ImmutableList;
import org.eclipse.collections.impl.factory.Lists;

public class AntlrNullLiteral
        extends AbstractAntlrLiteralValue
{
    private NullLiteralBuilder elementBuilder;

    public AntlrNullLiteral(
            @Nonnull NullLiteralContext elementContext,
            @Nonnull Optional<CompilationUnit> compilationUnit,
            @Nonnull IAntlrElement expressionValueOwner)
    {
        super(elementContext, compilationUnit, expressionValueOwner);
    }

    @Override
    public void reportErrors(@Nonnull CompilerAnnotationState compilerAnnotationHolder)
    {
    }

    @Nonnull
    @Override
    public NullLiteralBuilder build()
    {
        if (this.elementBuilder != null)
        {
            throw new IllegalStateException();
        }
        this.elementBuilder = new NullLiteralBuilder(
                (NullLiteralContext) this.elementContext,
                this.getMacroElementBuilder(),
                this.getSourceCodeBuilder());
        return this.elementBuilder;
    }

    @Nonnull
    @Override
    public NullLiteralBuilder getElementBuilder()
    {
        return Objects.requireNonNull(this.elementBuilder);
    }

    @Nonnull
    @Override
    public ImmutableList<AntlrType> getPossibleTypes()
    {
        return Lists.immutable.with(
                AntlrPrimitiveType.STRING,
                AntlrPrimitiveType.INTEGER,
                AntlrPrimitiveType.LONG,
                AntlrPrimitiveType.DOUBLE,
                AntlrPrimitiveType.FLOAT,
                AntlrPrimitiveType.BOOLEAN,
                AntlrPrimitiveType.INSTANT,
                AntlrPrimitiveType.LOCAL_DATE);
    }

    @Override
    public void visit(AntlrExpressionValueVisitor visitor)
    {
        visitor.visitNullLiteral(this);
    }
}
