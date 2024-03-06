package cool.klass.model.converter.compiler.state.property;

import java.util.Objects;
import java.util.Optional;

import javax.annotation.Nonnull;

import cool.klass.model.converter.compiler.CompilationUnit;
import cool.klass.model.converter.compiler.error.CompilerErrorState;
import cool.klass.model.converter.compiler.state.AntlrClass;
import cool.klass.model.converter.compiler.state.AntlrClassifier;
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
            Optional.empty(),
            AbstractElement.NO_CONTEXT,
            "ambiguous primitive property name",
            -1,
            AntlrClass.AMBIGUOUS,
            false,
            Lists.immutable.empty(),
            AntlrPrimitiveType.AMBIGUOUS);

    @Nonnull
    private final AntlrPrimitiveType antlrPrimitiveType;

    private PrimitivePropertyBuilder elementBuilder;

    public AntlrPrimitiveProperty(
            @Nonnull ParserRuleContext elementContext,
            @Nonnull Optional<CompilationUnit> compilationUnit,
            @Nonnull ParserRuleContext nameContext,
            @Nonnull String name,
            int ordinal,
            @Nonnull AntlrClassifier owningClassifierState,
            boolean isOptional,
            @Nonnull ImmutableList<AntlrPropertyModifier> propertyModifierStates,
            @Nonnull AntlrPrimitiveType antlrPrimitiveType)
    {
        super(
                elementContext,
                compilationUnit,
                nameContext,
                name,
                ordinal,
                owningClassifierState,
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
        return this.modifierStates.anySatisfy(AntlrPropertyModifier::isSystem);
    }

    @Override
    public boolean isValid()
    {
        return this.modifierStates.anySatisfy(AntlrPropertyModifier::isValid);
    }

    @Nonnull
    @Override
    public PrimitivePropertyBuilder build()
    {
        if (this.elementBuilder != null)
        {
            throw new IllegalStateException();
        }

        ImmutableList<PropertyModifierBuilder> propertyModifierBuilders =
                this.modifierStates.collect(AntlrPropertyModifier::build);

        this.elementBuilder = new PrimitivePropertyBuilder(
                this.elementContext,
                this.getMacroElementBuilder(),
                this.nameContext,
                this.name,
                this.ordinal,
                this.antlrPrimitiveType.getPrimitiveType(),
                this.owningClassifierState.getElementBuilder(),
                propertyModifierBuilders,
                this.isOptional);

        this.buildValidations();

        return this.elementBuilder;
    }

    @Nonnull
    @Override
    public PrimitivePropertyBuilder getElementBuilder()
    {
        return Objects.requireNonNull(this.elementBuilder);
    }

    @Override
    public void reportErrors(@Nonnull CompilerErrorState compilerErrorHolder)
    {
        super.reportErrors(compilerErrorHolder);

        this.reportInvalidStringValidations(compilerErrorHolder);
        this.reportInvalidNumericValidations(compilerErrorHolder);
    }

    @Override
    protected void reportInvalidIdProperties(@Nonnull CompilerErrorState compilerErrorHolder)
    {
        PrimitiveType primitiveType = this.antlrPrimitiveType.getPrimitiveType();
        if (primitiveType.isId())
        {
            this.reportNonKeyIdProperty(compilerErrorHolder);
        }
        else
        {
            this.reportInvalidTypeIdProperty(compilerErrorHolder);
        }
    }

    private void reportNonKeyIdProperty(@Nonnull CompilerErrorState compilerErrorHolder)
    {
        if (this.isKey())
        {
            return;
        }

        ImmutableList<AntlrPropertyModifier> idModifiers = this.modifierStates.select(AntlrPropertyModifier::isID);
        for (AntlrPropertyModifier idModifier : idModifiers)
        {
            ParserRuleContext offendingToken = idModifier.getElementContext();
            String message = "Properties with the 'id' modifier must also have the 'key' modifier.";
            compilerErrorHolder.add("ERR_NKY_IDP", message, this, offendingToken);
        }
    }

    private void reportInvalidTypeIdProperty(@Nonnull CompilerErrorState compilerErrorHolder)
    {
        PrimitiveType primitiveType = this.antlrPrimitiveType.getPrimitiveType();

        ImmutableList<AntlrPropertyModifier> idModifiers = this.modifierStates.select(AntlrPropertyModifier::isID);
        for (AntlrPropertyModifier idModifier : idModifiers)
        {
            ParserRuleContext offendingToken = idModifier.getElementContext();
            String message = String.format(
                    "Primitive properties with type %s may not be auto-generated ids. Only types %s may be id properties.",
                    primitiveType.getPrettyName(),
                    PrimitiveType.ID_PRIMITIVE_TYPES);
            compilerErrorHolder.add("ERR_PRP_IDP", message, this, offendingToken);
        }
    }

    private void reportInvalidStringValidations(@Nonnull CompilerErrorState compilerErrorHolder)
    {
        PrimitiveType primitiveType = this.antlrPrimitiveType.getPrimitiveType();
        if (primitiveType == PrimitiveType.STRING)
        {
            return;
        }

        this.minLengthValidationStates.each(each -> each.reportInvalidType(compilerErrorHolder, primitiveType));
        this.maxLengthValidationStates.each(each -> each.reportInvalidType(compilerErrorHolder, primitiveType));
    }

    private void reportInvalidNumericValidations(@Nonnull CompilerErrorState compilerErrorHolder)
    {
        PrimitiveType primitiveType = this.antlrPrimitiveType.getPrimitiveType();
        if (primitiveType.isNumeric())
        {
            return;
        }

        this.minValidationStates.each(each -> each.reportInvalidType(compilerErrorHolder, primitiveType));
        this.maxValidationStates.each(each -> each.reportInvalidType(compilerErrorHolder, primitiveType));
    }
}
