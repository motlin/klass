package cool.klass.model.converter.compiler.state.property;

import java.util.Objects;
import java.util.Optional;

import javax.annotation.Nonnull;

import cool.klass.model.converter.compiler.CompilationUnit;
import cool.klass.model.converter.compiler.error.CompilerErrorState;
import cool.klass.model.converter.compiler.state.AntlrClass;
import cool.klass.model.converter.compiler.state.AntlrClassifier;
import cool.klass.model.converter.compiler.state.AntlrEnumeration;
import cool.klass.model.meta.domain.EnumerationImpl;
import cool.klass.model.meta.domain.property.EnumerationPropertyImpl.EnumerationPropertyBuilder;
import cool.klass.model.meta.domain.property.PropertyModifierImpl.PropertyModifierBuilder;
import cool.klass.model.meta.grammar.KlassParser.EnumerationPropertyContext;
import cool.klass.model.meta.grammar.KlassParser.EnumerationReferenceContext;
import org.antlr.v4.runtime.ParserRuleContext;
import org.eclipse.collections.api.list.ImmutableList;
import org.eclipse.collections.impl.factory.Lists;

public class AntlrEnumerationProperty extends AntlrDataTypeProperty<EnumerationImpl>
{
    @Nonnull
    public static final AntlrEnumerationProperty NOT_FOUND = new AntlrEnumerationProperty(
            new ParserRuleContext(),
            Optional.empty(),
            new ParserRuleContext(),
            "not found enumeration property",
            -1,
            AntlrClass.NOT_FOUND,
            false,
            Lists.immutable.empty(),
            AntlrEnumeration.NOT_FOUND);

    // TODO: Check that it's not NOT_FOUND
    @Nonnull
    private final AntlrEnumeration enumerationState;

    private EnumerationPropertyBuilder elementBuilder;

    public AntlrEnumerationProperty(
            @Nonnull ParserRuleContext elementContext,
            @Nonnull Optional<CompilationUnit> compilationUnit,
            @Nonnull ParserRuleContext nameContext,
            @Nonnull String name,
            int ordinal,
            @Nonnull AntlrClassifier owningClassifierState,
            boolean isOptional,
            @Nonnull ImmutableList<AntlrPropertyModifier> modifiers,
            @Nonnull AntlrEnumeration enumerationState)
    {
        super(
                elementContext,
                compilationUnit,
                nameContext,
                name,
                ordinal,
                owningClassifierState,
                modifiers,
                isOptional);
        // TODO: is this nullable?
        this.enumerationState = Objects.requireNonNull(enumerationState);
    }

    @Nonnull
    @Override
    public AntlrEnumeration getType()
    {
        return this.enumerationState;
    }

    @Override
    public boolean isTemporal()
    {
        return false;
    }

    // TODO: Test that error is thrown for system, valid, to, from on enum properties
    @Override
    public boolean isSystem()
    {
        return false;
    }

    @Override
    public boolean isValid()
    {
        return false;
    }

    @Nonnull
    @Override
    public EnumerationPropertyBuilder build()
    {
        if (this.elementBuilder != null)
        {
            throw new IllegalStateException();
        }

        ImmutableList<PropertyModifierBuilder> propertyModifierBuilders =
                this.modifierStates.collect(AntlrPropertyModifier::build);

        this.elementBuilder = new EnumerationPropertyBuilder(
                this.elementContext,
                this.getMacroElementBuilder(),
                this.nameContext,
                this.name,
                this.ordinal,
                this.enumerationState.getElementBuilder(),
                this.owningClassifierState.getElementBuilder(),
                propertyModifierBuilders,
                this.isOptional);

        this.buildValidations();

        return this.elementBuilder;
    }

    @Nonnull
    @Override
    public EnumerationPropertyBuilder getElementBuilder()
    {
        return Objects.requireNonNull(this.elementBuilder);
    }

    @Override
    public void reportErrors(@Nonnull CompilerErrorState compilerErrorHolder)
    {
        super.reportErrors(compilerErrorHolder);

        if (this.enumerationState != AntlrEnumeration.NOT_FOUND)
        {
            return;
        }

        EnumerationReferenceContext offendingToken = this.getElementContext().enumerationReference();
        String message = String.format(
                "Cannot find enumeration '%s'.",
                offendingToken.getText());
        compilerErrorHolder.add("ERR_ENM_PRP", message, this, offendingToken);
    }

    @Nonnull
    @Override
    public EnumerationPropertyContext getElementContext()
    {
        return (EnumerationPropertyContext) super.getElementContext();
    }
}
