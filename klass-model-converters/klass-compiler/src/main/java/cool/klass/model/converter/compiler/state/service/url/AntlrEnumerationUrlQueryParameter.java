package cool.klass.model.converter.compiler.state.service.url;

import java.util.Objects;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import cool.klass.model.converter.compiler.CompilationUnit;
import cool.klass.model.converter.compiler.state.AntlrEnumeration;
import cool.klass.model.converter.compiler.state.AntlrMultiplicity;
import cool.klass.model.converter.compiler.state.AntlrType;
import cool.klass.model.meta.domain.service.url.EnumerationUrlQueryParameter.EnumerationUrlQueryParameterBuilder;
import org.antlr.v4.runtime.ParserRuleContext;

public class AntlrEnumerationUrlQueryParameter extends AntlrUrlQueryParameter
{
    @Nonnull
    public static final AntlrEnumerationUrlQueryParameter AMBIGUOUS = new AntlrEnumerationUrlQueryParameter(
            new ParserRuleContext(),
            null,
            true,
            new ParserRuleContext(),
            "ambiguous enumeration url parameter",
            AntlrEnumeration.AMBIGUOUS,
            AntlrMultiplicity.AMBIGUOUS,
            AntlrUrl.AMBIGUOUS);

    @Nonnull
    public static final AntlrEnumerationUrlQueryParameter NOT_FOUND = new AntlrEnumerationUrlQueryParameter(
            new ParserRuleContext(),
            null,
            true,
            new ParserRuleContext(),
            "not found enumeration url parameter",
            AntlrEnumeration.NOT_FOUND,
            AntlrMultiplicity.AMBIGUOUS,
            AntlrUrl.AMBIGUOUS);

    @Nonnull
    private final AntlrEnumeration antlrEnumeration;

    public AntlrEnumerationUrlQueryParameter(
            @Nonnull ParserRuleContext elementContext,
            @Nullable CompilationUnit compilationUnit,
            boolean inferred,
            @Nonnull ParserRuleContext nameContext,
            @Nonnull String name,
            @Nonnull AntlrEnumeration antlrEnumeration,
            @Nonnull AntlrMultiplicity antlrMultiplicity,
            @Nonnull AntlrUrl url)
    {
        super(elementContext, compilationUnit, inferred, nameContext, name, antlrMultiplicity, url);
        this.antlrEnumeration = Objects.requireNonNull(antlrEnumeration);
    }

    @Nonnull
    @Override
    public AntlrType getType()
    {
        return this.antlrEnumeration;
    }

    @Nonnull
    @Override
    public EnumerationUrlQueryParameterBuilder getUrlParameterBuilder()
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName()
                + ".getUrlParameterBuilder() not implemented yet");
    }

    @Nonnull
    @Override
    public EnumerationUrlQueryParameterBuilder build()
    {
        return new EnumerationUrlQueryParameterBuilder(
                this.elementContext,
                this.nameContext,
                this.name,
                this.multiplicityState.getMultiplicity(),
                this.urlState.getUrlBuilder(),
                this.antlrEnumeration.getEnumerationBuilder());
    }
}
