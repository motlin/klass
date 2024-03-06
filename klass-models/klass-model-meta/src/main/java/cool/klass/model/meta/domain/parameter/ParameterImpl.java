package cool.klass.model.meta.domain.parameter;

import java.util.Objects;
import java.util.Optional;

import javax.annotation.Nonnull;

import cool.klass.model.meta.domain.AbstractNamedElement;
import cool.klass.model.meta.domain.api.DataType;
import cool.klass.model.meta.domain.api.DataType.DataTypeGetter;
import cool.klass.model.meta.domain.api.Element;
import cool.klass.model.meta.domain.api.Multiplicity;
import cool.klass.model.meta.domain.api.parameter.Parameter;
import org.antlr.v4.runtime.ParserRuleContext;

public final class ParameterImpl extends AbstractNamedElement implements Parameter
{
    @Nonnull
    private final Multiplicity multiplicity;
    @Nonnull
    private final DataType     dataType;

    private ParameterImpl(
            @Nonnull ParserRuleContext elementContext,
            Optional<Element> macroElement,
            @Nonnull ParserRuleContext nameContext,
            @Nonnull String name,
            int ordinal,
            @Nonnull Multiplicity multiplicity,
            @Nonnull DataType dataType)
    {
        super(elementContext, macroElement, nameContext, name, ordinal);
        this.multiplicity = Objects.requireNonNull(multiplicity);
        this.dataType = Objects.requireNonNull(dataType);
    }

    @Override
    public String toString()
    {
        return String.format("{%s}", this.getName());
    }

    @Override
    @Nonnull
    public DataType getType()
    {
        return this.dataType;
    }

    @Override
    @Nonnull
    public Multiplicity getMultiplicity()
    {
        return this.multiplicity;
    }

    public static final class ParameterBuilder extends NamedElementBuilder<ParameterImpl>
    {
        @Nonnull
        private final DataTypeGetter dataTypeGetter;

        @Nonnull
        private final Multiplicity multiplicity;

        public ParameterBuilder(
                @Nonnull ParserRuleContext elementContext,
                Optional<ElementBuilder<?>> macroElement,
                @Nonnull ParserRuleContext nameContext,
                @Nonnull String name,
                int ordinal,
                @Nonnull DataTypeGetter dataType,
                @Nonnull Multiplicity multiplicity)
        {
            super(elementContext, macroElement, nameContext, name, ordinal);
            this.dataTypeGetter = Objects.requireNonNull(dataType);
            this.multiplicity = Objects.requireNonNull(multiplicity);
        }

        @Override
        @Nonnull
        protected ParameterImpl buildUnsafe()
        {
            return new ParameterImpl(
                    this.elementContext,
                    this.macroElement.map(ElementBuilder::getElement),
                    this.nameContext,
                    this.name,
                    this.ordinal,
                    this.multiplicity,
                    this.dataTypeGetter.getType());
        }
    }
}
