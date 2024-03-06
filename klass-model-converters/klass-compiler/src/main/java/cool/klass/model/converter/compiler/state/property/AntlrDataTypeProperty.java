package cool.klass.model.converter.compiler.state.property;

import java.util.Objects;
import java.util.Optional;

import javax.annotation.Nonnull;

import cool.klass.model.converter.compiler.CompilationUnit;
import cool.klass.model.converter.compiler.error.CompilerErrorHolder;
import cool.klass.model.converter.compiler.state.AntlrClass;
import cool.klass.model.converter.compiler.state.IAntlrElement;
import cool.klass.model.meta.domain.api.DataType;
import cool.klass.model.meta.domain.property.AbstractDataTypeProperty.DataTypePropertyBuilder;
import cool.klass.model.meta.grammar.KlassParser.ClassModifierContext;
import org.antlr.v4.runtime.ParserRuleContext;
import org.eclipse.collections.api.list.ImmutableList;
import org.eclipse.collections.api.list.MutableList;

public abstract class AntlrDataTypeProperty<T extends DataType> extends AntlrProperty<T>
{
    protected final boolean                              isOptional;
    @Nonnull
    protected final ImmutableList<AntlrPropertyModifier> propertyModifierStates;
    @Nonnull
    protected final AntlrClass                           owningClassState;

    protected AntlrDataTypeProperty(
            @Nonnull ParserRuleContext elementContext,
            CompilationUnit compilationUnit,
            boolean inferred,
            @Nonnull ParserRuleContext nameContext,
            @Nonnull String name,
            int ordinal,
            AntlrClass owningClassState,
            @Nonnull ImmutableList<AntlrPropertyModifier> propertyModifierStates,
            boolean isOptional)
    {
        super(elementContext, compilationUnit, inferred, nameContext, name, ordinal);
        this.isOptional = isOptional;
        this.propertyModifierStates = Objects.requireNonNull(propertyModifierStates);
        this.owningClassState = Objects.requireNonNull(owningClassState);
    }

    @Nonnull
    @Override
    public Optional<IAntlrElement> getSurroundingElement()
    {
        return Optional.of(this.owningClassState);
    }

    public boolean isKey()
    {
        return this.propertyModifierStates.anySatisfy(AntlrPropertyModifier::isKey);
    }

    public boolean isID()
    {
        return this.propertyModifierStates.anySatisfy(AntlrPropertyModifier::isID);
    }

    public boolean isAudit()
    {
        return this.propertyModifierStates.anySatisfy(AntlrPropertyModifier::isAudit);
    }

    public boolean isOptional()
    {
        return this.isOptional;
    }

    public abstract boolean isTemporal();

    @Nonnull
    @Override
    public abstract DataTypePropertyBuilder<T, ?, ?> build();

    @Nonnull
    @Override
    protected AntlrClass getOwningClassState()
    {
        return this.owningClassState;
    }

    @Nonnull
    public abstract DataTypePropertyBuilder<T, ?, ?> getPropertyBuilder();

    @Override
    public void getParserRuleContexts(@Nonnull MutableList<ParserRuleContext> parserRuleContexts)
    {
        if (this.elementContext instanceof ClassModifierContext)
        {
            return;
        }
        this.owningClassState.getParserRuleContexts(parserRuleContexts);
    }

    public void reportErrors(CompilerErrorHolder compilerErrorHolder)
    {
        // TODO: Check for duplicate modifiers
        // TODO: Check for nullable key properties
        // TODO: Check that ID properties are key properties
        // TODO: Only Integer and Long may be ID (no enums either)
    }
}
