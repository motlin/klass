package cool.klass.model.meta.domain.parameter;

import java.util.Objects;

import javax.annotation.Nonnull;

import cool.klass.model.meta.domain.Enumeration;
import cool.klass.model.meta.domain.Enumeration.EnumerationBuilder;
import cool.klass.model.meta.domain.Multiplicity;
import org.antlr.v4.runtime.ParserRuleContext;

public final class EnumerationParameter extends Parameter
{
    @Nonnull
    private final Enumeration enumeration;

    private EnumerationParameter(
            @Nonnull ParserRuleContext elementContext,
            boolean inferred,
            @Nonnull ParserRuleContext nameContext,
            @Nonnull String name,
            int ordinal,
            @Nonnull Multiplicity multiplicity,
            @Nonnull Enumeration enumeration)
    {
        super(elementContext, inferred, nameContext, name, ordinal, multiplicity);
        this.enumeration = Objects.requireNonNull(enumeration);
    }

    @Override
    @Nonnull
    public Enumeration getType()
    {
        return this.enumeration;
    }

    public static final class EnumerationParameterBuilder extends ParameterBuilder
    {
        @Nonnull
        private final EnumerationBuilder   enumerationBuilder;
        private       EnumerationParameter enumerationParameter;

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

        @Nonnull
        @Override
        public EnumerationParameter build()
        {
            if (this.enumerationParameter != null)
            {
                throw new IllegalStateException();
            }
            this.enumerationParameter = new EnumerationParameter(
                    this.elementContext,
                    this.inferred,
                    this.nameContext,
                    this.name,
                    this.ordinal,
                    this.multiplicity,
                    this.enumerationBuilder.getEnumeration());
            return this.enumerationParameter;
        }

        @Override
        @Nonnull
        public EnumerationParameter getParameter()
        {
            return Objects.requireNonNull(this.enumerationParameter);
        }
    }
}
