package cool.klass.model.converter.compiler.state.service.url;

import java.util.Objects;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import cool.klass.model.converter.compiler.CompilationUnit;
import cool.klass.model.converter.compiler.state.AntlrEnumeration;
import cool.klass.model.converter.compiler.state.AntlrMultiplicity;
import cool.klass.model.converter.compiler.state.AntlrType;
import cool.klass.model.meta.domain.service.url.EnumerationUrlPathParameter.EnumerationUrlPathParameterBuilder;
import org.antlr.v4.runtime.ParserRuleContext;
import org.eclipse.collections.api.list.ImmutableList;
import org.eclipse.collections.impl.factory.Lists;

public class AntlrEnumerationUrlPathParameter extends AntlrUrlPathParameter
{
    @Nonnull
    public static final AntlrEnumerationUrlPathParameter AMBIGUOUS = new AntlrEnumerationUrlPathParameter(
            new ParserRuleContext(),
            null,
            true,
            new ParserRuleContext(),
            "ambiguous enumeration url parameter",
            AntlrEnumeration.AMBIGUOUS,
            AntlrMultiplicity.AMBIGUOUS,
            AntlrUrl.AMBIGUOUS,
            Lists.immutable.empty());

    @Nonnull
    public static final AntlrEnumerationUrlPathParameter NOT_FOUND = new AntlrEnumerationUrlPathParameter(
            new ParserRuleContext(),
            null,
            true,
            new ParserRuleContext(),
            "not found enumeration url parameter",
            AntlrEnumeration.NOT_FOUND,
            AntlrMultiplicity.AMBIGUOUS,
            AntlrUrl.AMBIGUOUS,
            Lists.immutable.empty());

    @Nonnull
    private final AntlrEnumeration antlrEnumeration;

    public AntlrEnumerationUrlPathParameter(
            @Nonnull ParserRuleContext elementContext,
            @Nullable CompilationUnit compilationUnit,
            boolean inferred,
            @Nonnull ParserRuleContext nameContext,
            @Nonnull String name,
            @Nonnull AntlrEnumeration antlrEnumeration,
            @Nonnull AntlrMultiplicity antlrMultiplicity,
            @Nonnull AntlrUrl url,
            ImmutableList<AntlrParameterModifier> parameterModifiers)
    {
        super(elementContext, compilationUnit, inferred, nameContext, name, antlrMultiplicity, url, parameterModifiers);
        this.antlrEnumeration = Objects.requireNonNull(antlrEnumeration);
    }

    @Nonnull
    @Override
    public EnumerationUrlPathParameterBuilder getUrlParameterBuilder()
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName()
                + ".getUrlParameterBuilder() not implemented yet");
    }

    @Nonnull
    @Override
    public AntlrType getType()
    {
        return this.antlrEnumeration;
    }

    @Nonnull
    @Override
    public EnumerationUrlPathParameterBuilder build()
    {
        return new EnumerationUrlPathParameterBuilder(
                this.elementContext,
                this.nameContext,
                this.name,
                this.multiplicityState.getMultiplicity(),
                this.urlState.getUrlBuilder(),
                this.antlrEnumeration.getEnumerationBuilder());
    }
}
