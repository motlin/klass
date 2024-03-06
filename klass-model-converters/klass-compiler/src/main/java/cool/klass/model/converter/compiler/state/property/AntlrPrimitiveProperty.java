package cool.klass.model.converter.compiler.state.property;

import java.util.Objects;
import java.util.Optional;

import javax.annotation.Nonnull;

import cool.klass.model.converter.compiler.CompilationUnit;
import cool.klass.model.converter.compiler.error.CompilerErrorState;
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
    @Nonnull
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
    public void reportErrors(@Nonnull CompilerErrorState compilerErrorHolder)
    {
        super.reportErrors(compilerErrorHolder);

        this.reportInvalidTemporalMultiplicity(compilerErrorHolder);
        this.reportInvalidStringValidations(compilerErrorHolder);
        this.reportInvalidNumericValidations(compilerErrorHolder);
    }

    @Override
    public String getTypeName()
    {
        return this.getElementContext().primitiveType().getText();
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

        ListIterable<AntlrModifier> idModifiers = this.getModifiersByName("id");
        for (AntlrModifier idModifier : idModifiers)
        {
            String            message        = "Properties with the 'id' modifier must also have the 'key' modifier.";
            compilerErrorHolder.add("ERR_NKY_IDP", message, idModifier);
        }
    }

    private void reportInvalidTypeIdProperty(@Nonnull CompilerErrorState compilerErrorHolder)
    {
        PrimitiveType primitiveType = this.antlrPrimitiveType.getPrimitiveType();

        ListIterable<AntlrModifier> idModifiers = this.getModifiersByName("id");
        for (AntlrModifier idModifier : idModifiers)
        {
            String message = String.format(
                    "Primitive properties with type %s may not be auto-generated ids. Only types %s may be id properties.",
                    primitiveType.getPrettyName(),
                    PrimitiveType.ID_PRIMITIVE_TYPES);
            compilerErrorHolder.add("ERR_PRP_IDP", message, idModifier);
        }
    }

    private void reportInvalidTemporalMultiplicity(CompilerErrorState compilerErrorHolder)
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
        compilerErrorHolder.add("ERR_REQ_TMP", message, this, this.nameContext);
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
