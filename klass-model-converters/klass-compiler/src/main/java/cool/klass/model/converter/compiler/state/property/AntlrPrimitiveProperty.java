package cool.klass.model.converter.compiler.state.property;

import java.util.Objects;
import java.util.Optional;

import javax.annotation.Nonnull;

import cool.klass.model.converter.compiler.CompilationUnit;
import cool.klass.model.converter.compiler.annotation.CompilerAnnotationState;
import cool.klass.model.converter.compiler.state.AntlrClass;
import cool.klass.model.converter.compiler.state.AntlrClassifier;
import cool.klass.model.converter.compiler.state.AntlrPrimitiveType;
import cool.klass.model.meta.domain.api.PrimitiveType;
import cool.klass.model.meta.domain.property.ModifierImpl.ModifierBuilder;
import cool.klass.model.meta.domain.property.PrimitivePropertyImpl.PrimitivePropertyBuilder;
import cool.klass.model.meta.grammar.KlassParser.IdentifierContext;
import cool.klass.model.meta.grammar.KlassParser.PrimitivePropertyContext;
import org.antlr.v4.runtime.ParserRuleContext;
import org.eclipse.collections.api.list.ImmutableList;
import org.eclipse.collections.api.list.ListIterable;

public class AntlrPrimitiveProperty
        extends AntlrDataTypeProperty<PrimitiveType>
{
    public static final AntlrPrimitiveProperty AMBIGUOUS = new AntlrPrimitiveProperty(
            new PrimitivePropertyContext(null, -1),
            Optional.empty(),
            -1,
            new IdentifierContext(null, -1),
            AntlrClassifier.AMBIGUOUS,
            false,
            AntlrPrimitiveType.AMBIGUOUS);

    @Nonnull
    private final AntlrPrimitiveType antlrPrimitiveType;

    private PrimitivePropertyBuilder elementBuilder;

    public AntlrPrimitiveProperty(
            @Nonnull PrimitivePropertyContext elementContext,
            @Nonnull Optional<CompilationUnit> compilationUnit,
            int ordinal,
            @Nonnull IdentifierContext nameContext,
            @Nonnull AntlrClassifier owningClassifierState,
            boolean isOptional,
            @Nonnull AntlrPrimitiveType antlrPrimitiveType)
    {
        super(
                elementContext,
                compilationUnit,
                ordinal,
                nameContext,
                owningClassifierState,
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
    protected ParserRuleContext getTypeParserRuleContext()
    {
        return this.getElementContext().primitiveType();
    }

    @Nonnull
    @Override
    public PrimitivePropertyBuilder build()
    {
        if (this.elementBuilder != null)
        {
            throw new IllegalStateException();
        }

        this.elementBuilder = new PrimitivePropertyBuilder(
                (PrimitivePropertyContext) this.elementContext,
                this.getMacroElementBuilder(),
                this.getSourceCodeBuilder(),
                this.ordinal,
                this.getNameContext(),
                this.antlrPrimitiveType.getPrimitiveType(),
                this.owningClassifierState.getElementBuilder(),
                this.isOptional);

        ImmutableList<ModifierBuilder> propertyModifierBuilders = this.getModifiers()
                .collect(AntlrModifier::build)
                .toImmutable();
        this.elementBuilder.setModifierBuilders(propertyModifierBuilders);

        this.buildValidations();

        return this.elementBuilder;
    }

    @Nonnull
    @Override
    public PrimitivePropertyContext getElementContext()
    {
        return (PrimitivePropertyContext) super.getElementContext();
    }

    @Nonnull
    @Override
    public PrimitivePropertyBuilder getElementBuilder()
    {
        return Objects.requireNonNull(this.elementBuilder);
    }

    @Override
    public String getTypeName()
    {
        return this.getElementContext().primitiveType().getText();
    }

    //<editor-fold desc="Report Compiler Errors">
    @Override
    public void reportErrors(@Nonnull CompilerAnnotationState compilerAnnotationHolder)
    {
        super.reportErrors(compilerAnnotationHolder);

        this.reportInvalidTemporalMultiplicity(compilerAnnotationHolder);
        this.reportInvalidStringValidations(compilerAnnotationHolder);
        this.reportInvalidNumericValidations(compilerAnnotationHolder);
    }

    @Override
    protected void reportInvalidIdProperties(@Nonnull CompilerAnnotationState compilerAnnotationHolder)
    {
        PrimitiveType primitiveType = this.antlrPrimitiveType.getPrimitiveType();
        if (primitiveType.isId())
        {
            this.reportNonKeyIdProperty(compilerAnnotationHolder);
            this.reportOverriddenIdProperty(compilerAnnotationHolder);
        }
        else
        {
            this.reportInvalidTypeIdProperty(compilerAnnotationHolder);
        }
    }

    private void reportNonKeyIdProperty(@Nonnull CompilerAnnotationState compilerAnnotationHolder)
    {
        if (this.isKey())
        {
            return;
        }

        ListIterable<AntlrModifier> idModifiers = this.getModifiersByName("id");
        for (AntlrModifier idModifier : idModifiers)
        {
            String            message        = "Properties with the 'id' modifier must also have the 'key' modifier.";
            compilerAnnotationHolder.add("ERR_NKY_IDP", message, idModifier);
        }
    }

    private void reportInvalidTypeIdProperty(@Nonnull CompilerAnnotationState compilerAnnotationHolder)
    {
        PrimitiveType primitiveType = this.antlrPrimitiveType.getPrimitiveType();

        ListIterable<AntlrModifier> idModifiers = this.getModifiersByName("id");
        for (AntlrModifier idModifier : idModifiers)
        {
            String message = String.format(
                    "Primitive properties with type %s may not be auto-generated ids. Only types %s may be id properties.",
                    primitiveType.getPrettyName(),
                    PrimitiveType.ID_PRIMITIVE_TYPES);
            compilerAnnotationHolder.add("ERR_PRP_IDP", message, idModifier);
        }
    }

    private void reportOverriddenIdProperty(CompilerAnnotationState compilerAnnotationHolder)
    {
        if (!(this.owningClassifierState instanceof AntlrClass owningClass))
        {
            return;
        }

        if (owningClass.getSuperClass().isEmpty())
        {
            return;
        }

        AntlrClass superClass = owningClass.getSuperClass().get();
        AntlrDataTypeProperty<?> overriddenProperty = superClass.getDataTypePropertyByName(this.getName());
        if (overriddenProperty == AntlrEnumerationProperty.NOT_FOUND)
        {
            return;
        }

        if (!(overriddenProperty.getOwningClassifierState() instanceof AntlrClass))
        {
            return;
        }

        AntlrClass overriddenPropertyOwningClass = (AntlrClass) overriddenProperty.getOwningClassifierState();
        String message = "'id' properties may not be overridden. The property '%s' in class '%s' overrides the 'id' property in class '%s'.".formatted(
                this.getName(),
                owningClass.getName(),
                overriddenPropertyOwningClass.getName());
        ListIterable<AntlrModifier> idModifiers = this.getModifiersByName("id");
        if (idModifiers.notEmpty())
        {
            for (AntlrModifier idModifier : idModifiers)
            {
                compilerAnnotationHolder.add("ERR_OVR_IDP", message, idModifier);
            }
        }
        else
        {
            if (overriddenProperty.isId())
            {
                compilerAnnotationHolder.add("ERR_OVR_IDP", message, this);
            }
        }
    }

    private void reportInvalidTemporalMultiplicity(CompilerAnnotationState compilerAnnotationHolder)
    {
        PrimitiveType primitiveType = this.antlrPrimitiveType.getPrimitiveType();
        if (!primitiveType.isTemporal())
        {
            return;
        }

        if (this.isOptional)
        {
            return;
        }

        String message = String.format(
                "Primitive properties with type %s may not be required.",
                primitiveType.getPrettyName());
        compilerAnnotationHolder.add("ERR_REQ_TMP", message, this, this.nameContext);
    }

    private void reportInvalidStringValidations(@Nonnull CompilerAnnotationState compilerAnnotationHolder)
    {
        PrimitiveType primitiveType = this.antlrPrimitiveType.getPrimitiveType();
        if (primitiveType == PrimitiveType.STRING)
        {
            return;
        }

        this.minLengthValidationStates.each(each -> each.reportInvalidType(compilerAnnotationHolder, primitiveType));
        this.maxLengthValidationStates.each(each -> each.reportInvalidType(compilerAnnotationHolder, primitiveType));
    }

    private void reportInvalidNumericValidations(@Nonnull CompilerAnnotationState compilerAnnotationHolder)
    {
        PrimitiveType primitiveType = this.antlrPrimitiveType.getPrimitiveType();
        if (primitiveType.isNumeric())
        {
            return;
        }

        this.minValidationStates.each(each -> each.reportInvalidType(compilerAnnotationHolder, primitiveType));
        this.maxValidationStates.each(each -> each.reportInvalidType(compilerAnnotationHolder, primitiveType));
    }
    //</editor-fold>
}
