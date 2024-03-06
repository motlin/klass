package cool.klass.model.converter.compiler.state.service.url;

import java.util.Objects;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import cool.klass.model.converter.compiler.CompilationUnit;
import cool.klass.model.converter.compiler.state.AntlrEnumeration;
import cool.klass.model.converter.compiler.state.AntlrMultiplicity;
import cool.klass.model.converter.compiler.state.AntlrType;
import cool.klass.model.meta.domain.service.url.EnumerationUrlQueryParameterImpl.EnumerationUrlQueryParameterBuilder;
import org.antlr.v4.runtime.ParserRuleContext;
import org.eclipse.collections.api.list.ImmutableList;
import org.eclipse.collections.impl.factory.Lists;

public class AntlrEnumerationUrlQueryParameter extends AntlrUrlQueryParameter
{
    @Nonnull
    public static final AntlrEnumerationUrlQueryParameter AMBIGUOUS = new AntlrEnumerationUrlQueryParameter(
            new ParserRuleContext(),
            null,
            true,
            new ParserRuleContext(),
            "ambiguous enumeration url parameter",
            -1,
            AntlrEnumeration.AMBIGUOUS,
            AntlrMultiplicity.AMBIGUOUS,
            AntlrUrl.AMBIGUOUS,
            Lists.immutable.empty());

    @Nonnull
    public static final AntlrEnumerationUrlQueryParameter NOT_FOUND = new AntlrEnumerationUrlQueryParameter(
            new ParserRuleContext(),
            null,
            true,
            new ParserRuleContext(),
            "not found enumeration url parameter",
            -1,
            AntlrEnumeration.NOT_FOUND,
            AntlrMultiplicity.AMBIGUOUS,
            AntlrUrl.AMBIGUOUS,
            Lists.immutable.empty());

    @Nonnull
    private final AntlrEnumeration enumerationState;

    public AntlrEnumerationUrlQueryParameter(
            @Nonnull ParserRuleContext elementContext,
            @Nullable CompilationUnit compilationUnit,
            boolean inferred,
            @Nonnull ParserRuleContext nameContext,
            @Nonnull String name,
            int ordinal,
            @Nonnull AntlrEnumeration enumerationState,
            @Nonnull AntlrMultiplicity multiplicityState,
            @Nonnull AntlrUrl url,
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
                url,
                parameterModifiers);
        this.enumerationState = Objects.requireNonNull(enumerationState);
    }

    @Nonnull
    @Override
    public AntlrType getType()
    {
        return this.enumerationState;
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
                this.inferred,
                this.nameContext,
                this.name,
                this.ordinal,
                this.multiplicityState.getMultiplicity(),
                this.urlState.getUrlBuilder(),
                this.enumerationState.getElementBuilder());
    }
}
