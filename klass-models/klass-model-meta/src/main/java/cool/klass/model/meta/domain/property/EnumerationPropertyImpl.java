package cool.klass.model.meta.domain.property;

import java.util.Optional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import cool.klass.model.meta.domain.AbstractClassifier;
import cool.klass.model.meta.domain.AbstractClassifier.ClassifierBuilder;
import cool.klass.model.meta.domain.EnumerationImpl;
import cool.klass.model.meta.domain.EnumerationImpl.EnumerationBuilder;
import cool.klass.model.meta.domain.api.Element;
import cool.klass.model.meta.domain.api.property.EnumerationProperty;
import cool.klass.model.meta.domain.api.source.SourceCode;
import cool.klass.model.meta.domain.api.source.SourceCode.SourceCodeBuilder;
import cool.klass.model.meta.grammar.KlassParser.EnumerationPropertyContext;
import cool.klass.model.meta.grammar.KlassParser.IdentifierContext;

public final class EnumerationPropertyImpl
        extends AbstractDataTypeProperty<EnumerationImpl>
        implements EnumerationProperty
{
    private EnumerationPropertyImpl(
            @Nonnull EnumerationPropertyContext elementContext,
            @Nonnull Optional<Element> macroElement,
            @Nullable SourceCode sourceCode,
            int ordinal,
            @Nonnull IdentifierContext nameContext,
            @Nonnull EnumerationImpl enumeration,
            @Nonnull AbstractClassifier owningClassifier,
            boolean isOptional)
    {
        super(
                elementContext,
                macroElement,
                sourceCode,
                ordinal,
                nameContext,
                enumeration,
                owningClassifier,
                isOptional);
    }

    @Nonnull
    @Override
    public EnumerationPropertyContext getElementContext()
    {
        return (EnumerationPropertyContext) super.getElementContext();
    }

    public static final class EnumerationPropertyBuilder
            extends DataTypePropertyBuilder<EnumerationImpl, EnumerationBuilder, EnumerationPropertyImpl>
    {
        public EnumerationPropertyBuilder(
                @Nonnull EnumerationPropertyContext elementContext,
                @Nonnull Optional<ElementBuilder<?>> macroElement,
                @Nullable SourceCodeBuilder sourceCode,
                int ordinal,
                @Nonnull IdentifierContext nameContext,
                @Nonnull EnumerationBuilder enumerationBuilder,
                @Nonnull ClassifierBuilder<?> owningClassifierBuilder,
                boolean isOptional)
        {
            super(
                    elementContext,
                    macroElement,
                    sourceCode,
                    ordinal,
                    nameContext,
                    enumerationBuilder,
                    owningClassifierBuilder,
                    isOptional);
        }

        @Override
        @Nonnull
        protected EnumerationPropertyImpl buildUnsafe()
        {
            return new EnumerationPropertyImpl(
                    (EnumerationPropertyContext) this.elementContext,
                    this.macroElement.map(ElementBuilder::getElement),
                    this.sourceCode.build(),
                    this.ordinal,
                    this.getNameContext(),
                    this.typeBuilder.getElement(),
                    this.owningClassifierBuilder.getElement(),
                    this.isOptional);
        }
    }
}
