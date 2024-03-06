package cool.klass.model.converter.compiler.state;

import java.util.Objects;

import cool.klass.model.converter.compiler.CompilationUnit;
import cool.klass.model.converter.compiler.error.CompilerErrorHolder;
import cool.klass.model.meta.domain.DataType;
import cool.klass.model.meta.domain.DataTypeProperty.DataTypePropertyBuilder;
import org.antlr.v4.runtime.ParserRuleContext;
import org.eclipse.collections.api.list.ImmutableList;

public abstract class AntlrDataTypeProperty<T extends DataType> extends AntlrProperty<T>
{
    protected final boolean                              isOptional;
    protected final ImmutableList<AntlrPropertyModifier> modifiers;
    protected final AntlrClass                           owningClassState;

    protected AntlrDataTypeProperty(
            ParserRuleContext elementContext,
            CompilationUnit compilationUnit,
            boolean inferred,
            String name,
            ParserRuleContext nameContext,
            boolean isOptional,
            ImmutableList<AntlrPropertyModifier> modifiers,
            AntlrClass owningClassState)
    {
        super(elementContext, compilationUnit, inferred, name, nameContext);
        this.isOptional = isOptional;
        this.modifiers = Objects.requireNonNull(modifiers);
        this.owningClassState = owningClassState;
    }

    public boolean isKey()
    {
        return this.modifiers.anySatisfy(AntlrPropertyModifier::isKey);
    }

    public abstract boolean isTemporal();

    @Override
    public abstract DataTypePropertyBuilder<T, ?> build();

    @Override
    protected AntlrClass getOwningClassState()
    {
        return this.owningClassState;
    }

    public void reportErrors(CompilerErrorHolder compilerErrorHolder)
    {
        // TODO: Check for duplicate modifiers
        // TODO: Check for nullable key properties
        // TODO: Check that ID properties are key properties
    }
}
