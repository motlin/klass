package cool.klass.model.meta.domain.property;

import java.util.Optional;

import javax.annotation.Nonnull;

import cool.klass.model.meta.domain.AbstractClassifier;
import cool.klass.model.meta.domain.AbstractClassifier.ClassifierBuilder;
import cool.klass.model.meta.domain.EnumerationImpl;
import cool.klass.model.meta.domain.EnumerationImpl.EnumerationBuilder;
import cool.klass.model.meta.domain.api.Element;
import cool.klass.model.meta.domain.api.property.EnumerationProperty;
import cool.klass.model.meta.domain.property.PropertyModifierImpl.PropertyModifierBuilder;
import org.antlr.v4.runtime.ParserRuleContext;
import org.eclipse.collections.api.list.ImmutableList;

public final class EnumerationPropertyImpl extends AbstractDataTypeProperty<EnumerationImpl> implements EnumerationProperty
{
    private EnumerationPropertyImpl(
            @Nonnull ParserRuleContext elementContext,
            @Nonnull Optional<Element> macroElement,
            @Nonnull ParserRuleContext nameContext,
            @Nonnull String name,
            int ordinal,
            @Nonnull EnumerationImpl enumeration,
            @Nonnull AbstractClassifier owningClassifier,
            boolean isOptional)
    {
        super(
                elementContext,
                macroElement,
                nameContext,
                name,
                ordinal,
                enumeration,
                owningClassifier,
                isOptional);
    }

    @Override
    public boolean isTemporalRange()
    {
        return false;
    }

    @Override
    public boolean isTemporalInstant()
    {
        return false;
    }

    @Override
    public boolean isTemporal()
    {
        return false;
    }

    public static final class EnumerationPropertyBuilder extends DataTypePropertyBuilder<EnumerationImpl, EnumerationBuilder, EnumerationPropertyImpl>
    {
        public EnumerationPropertyBuilder(
                @Nonnull ParserRuleContext elementContext,
                @Nonnull Optional<ElementBuilder<?>> macroElement,
                @Nonnull ParserRuleContext nameContext,
                @Nonnull String name,
                int ordinal,
                @Nonnull EnumerationBuilder enumerationBuilder,
                @Nonnull ClassifierBuilder<?> owningClassifierBuilder,
                @Nonnull ImmutableList<PropertyModifierBuilder> propertyModifierBuilders,
                boolean isOptional)
        {
            super(
                    elementContext,
                    macroElement,
                    nameContext,
                    name,
                    ordinal,
                    enumerationBuilder,
                    owningClassifierBuilder,
                    propertyModifierBuilders,
                    isOptional);
        }

        @Override
        @Nonnull
        protected EnumerationPropertyImpl buildUnsafe()
        {
            return new EnumerationPropertyImpl(
                    this.elementContext,
                    this.macroElement.map(ElementBuilder::getElement),
                    this.nameContext,
                    this.name,
                    this.ordinal,
                    this.typeBuilder.getElement(),
                    this.owningClassifierBuilder.getElement(),
                    this.isOptional);
        }
    }
}
