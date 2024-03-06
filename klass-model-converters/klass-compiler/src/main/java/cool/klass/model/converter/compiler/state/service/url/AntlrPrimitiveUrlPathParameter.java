package cool.klass.model.converter.compiler.state.service.url;

import java.util.Objects;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import cool.klass.model.converter.compiler.CompilationUnit;
import cool.klass.model.converter.compiler.state.AntlrMultiplicity;
import cool.klass.model.converter.compiler.state.AntlrPrimitiveType;
import cool.klass.model.converter.compiler.state.AntlrType;
import cool.klass.model.meta.domain.service.url.PrimitiveUrlPathParameterImpl.PrimitiveUrlPathParameterBuilder;
import org.antlr.v4.runtime.ParserRuleContext;
import org.eclipse.collections.api.list.ImmutableList;
import org.eclipse.collections.impl.factory.Lists;

public class AntlrPrimitiveUrlPathParameter extends AntlrUrlPathParameter
{
    @Nonnull
    public static final AntlrPrimitiveUrlPathParameter AMBIGUOUS = new AntlrPrimitiveUrlPathParameter(
            new ParserRuleContext(),
            null,
            true,
            new ParserRuleContext(),
            "ambiguous primitive url parameter",
            -1,
            AntlrPrimitiveType.AMBIGUOUS,
            AntlrMultiplicity.AMBIGUOUS,
            AntlrUrl.AMBIGUOUS,
            Lists.immutable.empty());

    @Nonnull
    private final AntlrPrimitiveType               primitiveTypeState;
    private       PrimitiveUrlPathParameterBuilder primitiveUrlPathParameterBuilder;

    public AntlrPrimitiveUrlPathParameter(
            @Nonnull ParserRuleContext elementContext,
            @Nullable CompilationUnit compilationUnit,
            boolean inferred,
            @Nonnull ParserRuleContext nameContext,
            @Nonnull String name,
            int ordinal,
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
                ordinal,
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
    public PrimitiveUrlPathParameterBuilder getUrlParameterBuilder()
    {
        return Objects.requireNonNull(this.primitiveUrlPathParameterBuilder);
    }

    @Override
    public PrimitiveUrlPathParameterBuilder build()
    {
        if (this.primitiveUrlPathParameterBuilder != null)
        {
            throw new IllegalStateException();
        }
        this.primitiveUrlPathParameterBuilder = new PrimitiveUrlPathParameterBuilder(
                this.elementContext,
                this.inferred,
                this.nameContext,
                this.name,
                this.ordinal,
                this.multiplicityState.getMultiplicity(),
                this.urlState.getUrlBuilder(),
                this.primitiveTypeState.getPrimitiveType());
        return this.primitiveUrlPathParameterBuilder;
    }
}
