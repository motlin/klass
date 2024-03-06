package cool.klass.model.meta.domain.parameter;

import java.util.Objects;
import java.util.Optional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import cool.klass.model.meta.domain.AbstractIdentifierElement;
import cool.klass.model.meta.domain.api.DataType;
import cool.klass.model.meta.domain.api.DataType.DataTypeGetter;
import cool.klass.model.meta.domain.api.Element;
import cool.klass.model.meta.domain.api.Multiplicity;
import cool.klass.model.meta.domain.api.parameter.Parameter;
import cool.klass.model.meta.domain.api.source.SourceCode;
import cool.klass.model.meta.domain.api.source.SourceCode.SourceCodeBuilder;
import cool.klass.model.meta.grammar.KlassParser.IdentifierContext;
import org.antlr.v4.runtime.ParserRuleContext;

public final class ParameterImpl
        extends AbstractIdentifierElement
        implements Parameter
{
    @Nonnull
    private final Multiplicity multiplicity;
    @Nonnull
    private final DataType     dataType;

    private ParameterImpl(
            @Nonnull ParserRuleContext elementContext,
            @Nonnull Optional<Element> macroElement,
            @Nullable SourceCode sourceCode,
            int ordinal,
            @Nonnull IdentifierContext nameContext,
            @Nonnull Multiplicity multiplicity,
            @Nonnull DataType dataType)
    {
        super(elementContext, macroElement, sourceCode, ordinal, nameContext);
        this.multiplicity = Objects.requireNonNull(multiplicity);
        this.dataType     = Objects.requireNonNull(dataType);
    }

    @Nonnull
    @Override
    public ParserRuleContext getElementContext()
    {
        return super.getElementContext();
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

    public static final class ParameterBuilder
            extends IdentifierElementBuilder<ParameterImpl>
    {
        @Nonnull
        private final DataTypeGetter dataTypeGetter;

        @Nonnull
        private final Multiplicity multiplicity;

        public ParameterBuilder(
                @Nonnull ParserRuleContext elementContext,
                @Nonnull Optional<ElementBuilder<?>> macroElement,
                @Nullable SourceCodeBuilder sourceCode,
                int ordinal,
                @Nonnull IdentifierContext nameContext,
                @Nonnull DataTypeGetter dataType,
                @Nonnull Multiplicity multiplicity)
        {
            super(elementContext, macroElement, sourceCode, ordinal, nameContext);
            this.dataTypeGetter = Objects.requireNonNull(dataType);
            this.multiplicity   = Objects.requireNonNull(multiplicity);
        }

        @Override
        @Nonnull
        protected ParameterImpl buildUnsafe()
        {
            return new ParameterImpl(
                    this.elementContext,
                    this.macroElement.map(ElementBuilder::getElement),
                    this.sourceCode.build(),
                    this.ordinal,
                    this.getNameContext(),
                    this.multiplicity,
                    this.dataTypeGetter.getType());
        }
    }
}
