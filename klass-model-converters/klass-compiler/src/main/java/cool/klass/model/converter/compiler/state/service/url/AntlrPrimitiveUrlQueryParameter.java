package cool.klass.model.converter.compiler.state.service.url;

import java.util.Objects;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import cool.klass.model.converter.compiler.CompilationUnit;
import cool.klass.model.converter.compiler.state.AntlrMultiplicity;
import cool.klass.model.converter.compiler.state.AntlrPrimitiveType;
import cool.klass.model.converter.compiler.state.AntlrType;
import cool.klass.model.meta.domain.service.url.PrimitiveUrlQueryParameter.PrimitiveUrlQueryParameterBuilder;
import org.antlr.v4.runtime.ParserRuleContext;

public class AntlrPrimitiveUrlQueryParameter extends AntlrUrlQueryParameter
{
    @Nonnull
    public static final AntlrPrimitiveUrlQueryParameter AMBIGUOUS = new AntlrPrimitiveUrlQueryParameter(
            new ParserRuleContext(),
            null,
            true,
            new ParserRuleContext(),
            "ambiguous primitive url parameter",
            AntlrPrimitiveType.AMBIGUOUS,
            AntlrMultiplicity.AMBIGUOUS,
            AntlrUrl.AMBIGUOUS);

    @Nonnull
    private final AntlrPrimitiveType                primitiveTypeState;
    private       PrimitiveUrlQueryParameterBuilder primitiveUrlQueryParameterBuilder;

    public AntlrPrimitiveUrlQueryParameter(
            @Nonnull ParserRuleContext elementContext,
            @Nullable CompilationUnit compilationUnit,
            boolean inferred,
            @Nonnull ParserRuleContext nameContext,
            @Nonnull String name,
            @Nonnull AntlrPrimitiveType primitiveTypeState,
            @Nonnull AntlrMultiplicity multiplicityState,
            @Nonnull AntlrUrl urlState)
    {
        super(elementContext, compilationUnit, inferred, nameContext, name, multiplicityState, urlState);
        this.primitiveTypeState = Objects.requireNonNull(primitiveTypeState);
    }

    @Nonnull
    @Override
    public PrimitiveUrlQueryParameterBuilder getUrlParameterBuilder()
    {
        return Objects.requireNonNull(this.primitiveUrlQueryParameterBuilder);
    }

    @Nonnull
    @Override
    public AntlrType getType()
    {
        return this.primitiveTypeState;
    }

    @Override
    public PrimitiveUrlQueryParameterBuilder build()
    {
        if (this.primitiveUrlQueryParameterBuilder != null)
        {
            throw new IllegalStateException();
        }
        this.primitiveUrlQueryParameterBuilder = new PrimitiveUrlQueryParameterBuilder(
                this.elementContext,
                this.nameContext,
                this.name,
                this.multiplicityState.getMultiplicity(),
                this.urlState.getUrlBuilder(),
                this.primitiveTypeState.getPrimitiveType());
        return this.primitiveUrlQueryParameterBuilder;
    }
}
