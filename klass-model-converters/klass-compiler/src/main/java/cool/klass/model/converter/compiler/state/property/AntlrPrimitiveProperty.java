package cool.klass.model.converter.compiler.state.property;

import java.util.Objects;

import javax.annotation.Nonnull;

import cool.klass.model.converter.compiler.CompilationUnit;
import cool.klass.model.converter.compiler.state.AntlrClass;
import cool.klass.model.converter.compiler.state.AntlrPrimitiveType;
import cool.klass.model.meta.domain.AbstractElement;
import cool.klass.model.meta.domain.api.PrimitiveType;
import cool.klass.model.meta.domain.property.PrimitivePropertyImpl.PrimitivePropertyBuilder;
import cool.klass.model.meta.domain.property.PropertyModifierImpl.PropertyModifierBuilder;
import cool.klass.model.meta.grammar.KlassParser.PrimitivePropertyContext;
import org.antlr.v4.runtime.ParserRuleContext;
import org.eclipse.collections.api.list.ImmutableList;
import org.eclipse.collections.impl.factory.Lists;

public class AntlrPrimitiveProperty extends AntlrDataTypeProperty<PrimitiveType>
{
    @Nonnull
    public static final AntlrPrimitiveProperty AMBIGUOUS = new AntlrPrimitiveProperty(
            new PrimitivePropertyContext(null, -1),
            null,
            true,
            AbstractElement.NO_CONTEXT,
            "ambiguous primitive property name",
            -1,
            AntlrClass.AMBIGUOUS,
            false,
            Lists.immutable.empty(),
            AntlrPrimitiveType.AMBIGUOUS);

    @Nonnull
    private final AntlrPrimitiveType antlrPrimitiveType;

    private PrimitivePropertyBuilder primitivePropertyBuilder;

    public AntlrPrimitiveProperty(
            @Nonnull ParserRuleContext elementContext,
            CompilationUnit compilationUnit,
            boolean inferred,
            @Nonnull ParserRuleContext nameContext,
            @Nonnull String name,
            int ordinal,
            AntlrClass owningClassState,
            boolean isOptional,
            @Nonnull ImmutableList<AntlrPropertyModifier> propertyModifierStates,
            @Nonnull AntlrPrimitiveType antlrPrimitiveType)
    {
        super(
                elementContext,
                compilationUnit,
                inferred,
                nameContext,
                name,
                ordinal,
                owningClassState,
                propertyModifierStates,
                isOptional);
        this.antlrPrimitiveType = Objects.requireNonNull(antlrPrimitiveType);
    }

    @Nonnull
    @Override
    public AntlrPrimitiveType getType()
    {
        return this.antlrPrimitiveType;
    }

    @Override
    public boolean isTemporal()
    {
        return this.antlrPrimitiveType.isTemporal();
    }

    @Override
    public boolean isSystem()
    {
        return this.propertyModifierStates.anySatisfy(AntlrPropertyModifier::isSystem);
    }

    @Override
    public boolean isValid()
    {
        return this.propertyModifierStates.anySatisfy(AntlrPropertyModifier::isValid);
    }

    @Nonnull
    @Override
    public PrimitivePropertyBuilder build()
    {
        if (this.primitivePropertyBuilder != null)
        {
            throw new IllegalStateException();
        }

        ImmutableList<PropertyModifierBuilder> propertyModifierBuilders =
                this.propertyModifierStates.collect(AntlrPropertyModifier::build);

        this.primitivePropertyBuilder = new PrimitivePropertyBuilder(
                this.elementContext,
                this.inferred,
                this.nameContext,
                this.name,
                this.ordinal,
                this.antlrPrimitiveType.getPrimitiveType(),
                this.owningClassState.getElementBuilder(),
                propertyModifierBuilders,
                this.isOptional);
        return this.primitivePropertyBuilder;
    }

    @Nonnull
    @Override
    public PrimitivePropertyBuilder getPropertyBuilder()
    {
        return Objects.requireNonNull(this.primitivePropertyBuilder);
    }
}
