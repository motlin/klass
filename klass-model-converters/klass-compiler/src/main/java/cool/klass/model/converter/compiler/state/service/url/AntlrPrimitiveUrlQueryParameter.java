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
import org.eclipse.collections.api.list.ImmutableList;
import org.eclipse.collections.impl.factory.Lists;

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
            AntlrUrl.AMBIGUOUS,
            Lists.immutable.empty());

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
            @Nonnull AntlrUrl urlState,
            ImmutableList<AntlrParameterModifier> parameterModifiers)
    {
        super(
                elementContext,
                compilationUnit,
                inferred,
                nameContext,
                name,
                multiplicityState,
                urlState,
                parameterModifiers);
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
    public PrimitiveUrlQueryParameterBuilder getUrlParameterBuilder()
    {
        return Objects.requireNonNull(this.primitiveUrlQueryParameterBuilder);
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
