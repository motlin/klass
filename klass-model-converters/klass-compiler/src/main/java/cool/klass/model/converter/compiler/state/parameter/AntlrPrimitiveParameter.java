package cool.klass.model.converter.compiler.state.parameter;

import java.util.Objects;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import cool.klass.model.converter.compiler.CompilationUnit;
import cool.klass.model.converter.compiler.state.AntlrMultiplicity;
import cool.klass.model.converter.compiler.state.AntlrPrimitiveType;
import cool.klass.model.converter.compiler.state.AntlrType;
import cool.klass.model.converter.compiler.state.property.AntlrParameterizedProperty;
import cool.klass.model.meta.domain.parameter.PrimitiveParameterImpl;
import cool.klass.model.meta.domain.parameter.PrimitiveParameterImpl.PrimitiveParameterBuilder;
import org.antlr.v4.runtime.ParserRuleContext;

public class AntlrPrimitiveParameter extends AntlrParameter<PrimitiveParameterImpl>
{
    @Nonnull
    public static final AntlrPrimitiveParameter AMBIGUOUS = new AntlrPrimitiveParameter(
            new ParserRuleContext(),
            null,
            true,
            new ParserRuleContext(),
            "ambiguous primitive url parameter",
            -1,
            AntlrPrimitiveType.AMBIGUOUS,
            AntlrMultiplicity.AMBIGUOUS,
            AntlrParameterizedProperty.AMBIGUOUS);

    @Nonnull
    private final AntlrPrimitiveType        primitiveTypeState;
    private       PrimitiveParameterBuilder primitiveParameterBuilder;

    public AntlrPrimitiveParameter(
            @Nonnull ParserRuleContext elementContext,
            @Nullable CompilationUnit compilationUnit,
            boolean inferred,
            @Nonnull ParserRuleContext nameContext,
            @Nonnull String name,
            int ordinal,
            @Nonnull AntlrPrimitiveType primitiveTypeState,
            @Nonnull AntlrMultiplicity multiplicityState,
            @Nonnull AntlrParameterOwner parameterOwner)
    {
        super(
                elementContext,
                compilationUnit,
                inferred,
                nameContext,
                name,
                ordinal,
                multiplicityState,
                parameterOwner);
        this.primitiveTypeState = Objects.requireNonNull(primitiveTypeState);
    }

    @Nonnull
    @Override
    public AntlrType getType()
    {
        return this.primitiveTypeState;
    }

    @Nonnull
    @Override
    public PrimitiveParameterBuilder build()
    {
        if (this.primitiveParameterBuilder != null)
        {
            throw new IllegalStateException();
        }
        this.primitiveParameterBuilder = new PrimitiveParameterBuilder(
                this.elementContext,
                this.inferred,
                this.nameContext,
                this.name,
                this.ordinal,
                this.multiplicityState.getMultiplicity(),
                this.primitiveTypeState.getPrimitiveType());
        return this.primitiveParameterBuilder;
    }

    @Nonnull
    @Override
    public PrimitiveParameterBuilder getElementBuilder()
    {
        return this.primitiveParameterBuilder;
    }
}
