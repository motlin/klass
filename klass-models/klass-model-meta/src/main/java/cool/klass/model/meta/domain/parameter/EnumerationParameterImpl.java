package cool.klass.model.meta.domain.parameter;

import java.util.Objects;

import javax.annotation.Nonnull;

import cool.klass.model.meta.domain.EnumerationImpl;
import cool.klass.model.meta.domain.EnumerationImpl.EnumerationBuilder;
import cool.klass.model.meta.domain.api.Multiplicity;
import cool.klass.model.meta.domain.api.parameter.EnumerationParameter;
import org.antlr.v4.runtime.ParserRuleContext;

public final class EnumerationParameterImpl extends AbstractParameter implements EnumerationParameter
{
    @Nonnull
    private final EnumerationImpl enumeration;

    private EnumerationParameterImpl(
            @Nonnull ParserRuleContext elementContext,
            boolean inferred,
            @Nonnull ParserRuleContext nameContext,
            @Nonnull String name,
            int ordinal,
            @Nonnull Multiplicity multiplicity,
            @Nonnull EnumerationImpl enumeration)
    {
        super(elementContext, inferred, nameContext, name, ordinal, multiplicity);
        this.enumeration = Objects.requireNonNull(enumeration);
    }

    @Override
    @Nonnull
    public EnumerationImpl getType()
    {
        return this.enumeration;
    }

    public static final class EnumerationParameterBuilder extends AbstractParameterBuilder<EnumerationParameterImpl>
    {
        @Nonnull
        private final EnumerationBuilder enumerationBuilder;

        public EnumerationParameterBuilder(
                @Nonnull ParserRuleContext elementContext,
                boolean inferred,
                @Nonnull ParserRuleContext nameContext,
                @Nonnull String name,
                int ordinal,
                @Nonnull Multiplicity multiplicity,
                @Nonnull EnumerationBuilder enumerationBuilder)
        {
            super(elementContext, inferred, nameContext, name, ordinal, multiplicity);
            this.enumerationBuilder = Objects.requireNonNull(enumerationBuilder);
        }

        @Override
        @Nonnull
        protected EnumerationParameterImpl buildUnsafe()
        {
            return new EnumerationParameterImpl(
                    this.elementContext,
                    this.inferred,
                    this.nameContext,
                    this.name,
                    this.ordinal,
                    this.multiplicity,
                    this.enumerationBuilder.getElement());
        }
    }
}
