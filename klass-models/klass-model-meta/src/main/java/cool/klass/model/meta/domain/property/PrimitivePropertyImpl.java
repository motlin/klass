package cool.klass.model.meta.domain.property;

import java.util.Optional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import cool.klass.model.meta.domain.AbstractClassifier;
import cool.klass.model.meta.domain.AbstractClassifier.ClassifierBuilder;
import cool.klass.model.meta.domain.api.Element;
import cool.klass.model.meta.domain.api.PrimitiveType;
import cool.klass.model.meta.domain.api.source.SourceCode;
import cool.klass.model.meta.domain.api.source.SourceCode.SourceCodeBuilder;
import cool.klass.model.meta.domain.api.source.property.PrimitivePropertyWithSourceCode;
import cool.klass.model.meta.grammar.KlassParser.IdentifierContext;
import cool.klass.model.meta.grammar.KlassParser.PrimitivePropertyContext;

public final class PrimitivePropertyImpl
        extends AbstractDataTypeProperty<PrimitiveType>
        implements PrimitivePropertyWithSourceCode
{
    private PrimitivePropertyImpl(
            @Nonnull PrimitivePropertyContext elementContext,
            @Nonnull Optional<Element> macroElement,
            @Nullable SourceCode sourceCode,
            int ordinal,
            @Nonnull IdentifierContext nameContext,
            @Nonnull PrimitiveType primitiveType,
            @Nonnull AbstractClassifier owningClassifier,
            boolean isOptional)
    {
        super(
                elementContext,
                macroElement,
                sourceCode,
                ordinal,
                nameContext,
                primitiveType,
                owningClassifier,
                isOptional);
    }

    @Nonnull
    @Override
    public PrimitivePropertyContext getElementContext()
    {
        return (PrimitivePropertyContext) super.getElementContext();
    }

    public static final class PrimitivePropertyBuilder
            extends DataTypePropertyBuilder<PrimitiveType, PrimitiveType, PrimitivePropertyImpl>
    {
        public PrimitivePropertyBuilder(
                @Nonnull PrimitivePropertyContext elementContext,
                @Nonnull Optional<ElementBuilder<?>> macroElement,
                @Nullable SourceCodeBuilder sourceCode,
                int ordinal,
                @Nonnull IdentifierContext nameContext,
                @Nonnull PrimitiveType primitiveType,
                @Nonnull ClassifierBuilder<?> owningClassifierBuilder,
                boolean isOptional)
        {
            super(
                    elementContext,
                    macroElement,
                    sourceCode,
                    ordinal,
                    nameContext,
                    primitiveType,
                    owningClassifierBuilder,
                    isOptional);
        }

        @Override
        @Nonnull
        protected PrimitivePropertyImpl buildUnsafe()
        {
            return new PrimitivePropertyImpl(
                    (PrimitivePropertyContext) this.elementContext,
                    this.macroElement.map(ElementBuilder::getElement),
                    this.sourceCode.build(),
                    this.ordinal,
                    this.getNameContext(),
                    this.typeBuilder,
                    this.owningClassifierBuilder.getElement(),
                    this.isOptional);
        }
    }
}
