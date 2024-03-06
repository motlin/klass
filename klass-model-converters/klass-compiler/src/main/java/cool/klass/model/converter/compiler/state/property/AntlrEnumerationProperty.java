package cool.klass.model.converter.compiler.state.property;

import java.util.Objects;

import javax.annotation.Nonnull;

import cool.klass.model.converter.compiler.CompilationUnit;
import cool.klass.model.converter.compiler.error.CompilerErrorState;
import cool.klass.model.converter.compiler.state.AntlrClass;
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
            null,
            true,
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

    private EnumerationPropertyBuilder enumerationPropertyBuilder;

    public AntlrEnumerationProperty(
            @Nonnull ParserRuleContext elementContext,
            CompilationUnit compilationUnit,
            boolean inferred,
            @Nonnull ParserRuleContext nameContext,
            @Nonnull String name,
            int ordinal,
            AntlrClass owningClassState,
            boolean isOptional,
            @Nonnull ImmutableList<AntlrPropertyModifier> modifiers,
            @Nonnull AntlrEnumeration enumerationState)
    {
        super(
                elementContext,
                compilationUnit,
                inferred,
                nameContext,
                name,
                ordinal,
                owningClassState,
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
        if (this.enumerationPropertyBuilder != null)
        {
            throw new IllegalStateException();
        }

        ImmutableList<PropertyModifierBuilder> propertyModifierBuilders =
                this.propertyModifierStates.collect(AntlrPropertyModifier::build);

        this.enumerationPropertyBuilder = new EnumerationPropertyBuilder(
                this.elementContext,
                this.inferred,
                this.nameContext,
                this.name,
                this.ordinal,
                this.enumerationState.getElementBuilder(),
                this.owningClassState.getElementBuilder(),
                propertyModifierBuilders,
                this.isOptional);
        return this.enumerationPropertyBuilder;
    }

    @Nonnull
    @Override
    public EnumerationPropertyBuilder getPropertyBuilder()
    {
        return Objects.requireNonNull(this.enumerationPropertyBuilder);
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
                "ERR_ENM_PRP: Cannot find enumeration '%s'.",
                offendingToken.getText());
        compilerErrorHolder.add(message, this, offendingToken);
    }

    @Nonnull
    @Override
    public EnumerationPropertyContext getElementContext()
    {
        return (EnumerationPropertyContext) super.getElementContext();
    }
}
