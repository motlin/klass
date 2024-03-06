package cool.klass.model.meta.domain.property;

import java.util.Optional;

import javax.annotation.Nonnull;

import cool.klass.model.meta.domain.AbstractClassifier;
import cool.klass.model.meta.domain.AbstractClassifier.ClassifierBuilder;
import cool.klass.model.meta.domain.api.Element;
import cool.klass.model.meta.domain.api.PrimitiveType;
import cool.klass.model.meta.domain.api.property.PrimitiveProperty;
import cool.klass.model.meta.domain.property.PropertyModifierImpl.PropertyModifierBuilder;
import org.antlr.v4.runtime.ParserRuleContext;
import org.eclipse.collections.api.list.ImmutableList;

public final class PrimitivePropertyImpl
        extends AbstractDataTypeProperty<PrimitiveType>
        implements PrimitiveProperty
{
    private PrimitivePropertyImpl(
            @Nonnull ParserRuleContext elementContext,
            Optional<Element> macroElement,
            @Nonnull ParserRuleContext nameContext,
            @Nonnull String name,
            int ordinal,
            @Nonnull PrimitiveType primitiveType,
            @Nonnull AbstractClassifier owningClassifier,
            boolean isOptional)
    {
        super(
                elementContext,
                macroElement,
                nameContext,
                name,
                ordinal,
                primitiveType,
                owningClassifier,
                isOptional);
    }

    public static final class PrimitivePropertyBuilder extends DataTypePropertyBuilder<PrimitiveType, PrimitiveType, PrimitivePropertyImpl>
    {
        public PrimitivePropertyBuilder(
                @Nonnull ParserRuleContext elementContext,
                Optional<ElementBuilder<?>> macroElement,
                @Nonnull ParserRuleContext nameContext,
                @Nonnull String name,
                int ordinal,
                @Nonnull PrimitiveType primitiveType,
                @Nonnull ClassifierBuilder<?> owningClassifierBuilder,
                ImmutableList<PropertyModifierBuilder> propertyModifierBuilders,
                boolean isOptional)
        {
            super(
                    elementContext,
                    macroElement,
                    nameContext,
                    name,
                    ordinal,
                    primitiveType,
                    owningClassifierBuilder,
                    propertyModifierBuilders,
                    isOptional);
        }

        @Override
        @Nonnull
        protected PrimitivePropertyImpl buildUnsafe()
        {
            return new PrimitivePropertyImpl(
                    this.elementContext,
                    this.macroElement.map(ElementBuilder::getElement),
                    this.nameContext,
                    this.name,
                    this.ordinal,
                    this.typeBuilder,
                    this.owningClassifierBuilder.getElement(),
                    this.isOptional);
        }
    }
}
