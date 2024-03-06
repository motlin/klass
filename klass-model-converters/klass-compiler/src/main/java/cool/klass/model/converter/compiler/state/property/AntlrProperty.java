package cool.klass.model.converter.compiler.state.property;

import javax.annotation.Nonnull;

import cool.klass.model.converter.compiler.CompilationUnit;
import cool.klass.model.converter.compiler.state.AntlrClass;
import cool.klass.model.converter.compiler.state.AntlrNamedElement;
import cool.klass.model.converter.compiler.state.AntlrType;
import cool.klass.model.meta.domain.Type;
import cool.klass.model.meta.domain.property.Property.PropertyBuilder;
import org.antlr.v4.runtime.ParserRuleContext;

public abstract class AntlrProperty<T extends Type> extends AntlrNamedElement
{
    protected AntlrProperty(
            @Nonnull ParserRuleContext elementContext,
            CompilationUnit compilationUnit,
            boolean inferred,
            @Nonnull ParserRuleContext nameContext,
            @Nonnull String name,
            int ordinal)
    {
        super(elementContext, compilationUnit, inferred, nameContext, name, ordinal);
    }

    @Nonnull
    @Override
    public ParserRuleContext getNameContext()
    {
        return this.nameContext;
    }

    @Nonnull
    @Override
    public String getName()
    {
        return this.name;
    }

    @Nonnull
    public abstract AntlrType getType();

    public abstract PropertyBuilder<T, ?> build();

    protected abstract AntlrClass getOwningClassState();
}
