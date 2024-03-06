package cool.klass.model.converter.compiler.state.property;

import java.util.Objects;

import javax.annotation.Nonnull;

import cool.klass.model.converter.compiler.CompilationUnit;
import cool.klass.model.converter.compiler.error.CompilerErrorHolder;
import cool.klass.model.converter.compiler.state.AntlrClass;
import cool.klass.model.meta.domain.DataType;
import cool.klass.model.meta.domain.property.DataTypeProperty.DataTypePropertyBuilder;
import cool.klass.model.meta.grammar.KlassParser.ClassModifierContext;
import org.antlr.v4.runtime.ParserRuleContext;
import org.eclipse.collections.api.list.ImmutableList;

public abstract class AntlrDataTypeProperty<T extends DataType> extends AntlrProperty<T>
{
    protected final boolean                              isOptional;
    @Nonnull
    protected final ImmutableList<AntlrPropertyModifier> modifiers;
    protected final AntlrClass                           owningClassState;

    protected AntlrDataTypeProperty(
            @Nonnull ParserRuleContext elementContext,
            CompilationUnit compilationUnit,
            boolean inferred,
            @Nonnull ParserRuleContext nameContext,
            @Nonnull String name,
            boolean isOptional,
            @Nonnull ImmutableList<AntlrPropertyModifier> modifiers,
            AntlrClass owningClassState)
    {
        super(elementContext, compilationUnit, inferred, nameContext, name);
        this.isOptional = isOptional;
        this.modifiers = Objects.requireNonNull(modifiers);
        this.owningClassState = owningClassState;
    }

    public boolean isKey()
    {
        return this.modifiers.anySatisfy(AntlrPropertyModifier::isKey);
    }

    public boolean isOptional()
    {
        return this.isOptional;
    }

    public boolean isID()
    {
        return this.modifiers.anySatisfy(AntlrPropertyModifier::isID);
    }

    public abstract boolean isTemporal();

    @Nonnull
    public ImmutableList<AntlrPropertyModifier> getModifiers()
    {
        return this.modifiers;
    }

    @Override
    public abstract DataTypePropertyBuilder<T, ?> build();

    @Override
    protected AntlrClass getOwningClassState()
    {
        return this.owningClassState;
    }

    @Nonnull
    public abstract DataTypePropertyBuilder<T, ?> getPropertyBuilder();

    public void reportErrors(CompilerErrorHolder compilerErrorHolder)
    {
        // TODO: Check for duplicate modifiers
        // TODO: Check for nullable key properties
        // TODO: Check that ID properties are key properties
        // TODO: Only Integer and Long may be ID (no enums either)
    }

    public void reportDuplicateMemberName(@Nonnull CompilerErrorHolder compilerErrorHolder)
    {
        String message = String.format("ERR_DUP_MEM: Duplicate member: '%s'.", this.name);

        ParserRuleContext[] parserRuleContexts = this.elementContext instanceof ClassModifierContext
                ? new ParserRuleContext[]{}
                : new ParserRuleContext[]{this.getOwningClassState().getElementContext()};
        compilerErrorHolder.add(
                message,
                this.nameContext,
                parserRuleContexts);
    }
}
