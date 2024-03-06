package cool.klass.model.converter.compiler.state.property;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import cool.klass.model.converter.compiler.CompilationUnit;
import cool.klass.model.converter.compiler.error.CompilerErrorHolder;
import cool.klass.model.converter.compiler.state.AntlrNamedElement;
import cool.klass.model.meta.domain.property.PropertyModifier.PropertyModifierBuilder;
import org.antlr.v4.runtime.ParserRuleContext;

public class AntlrPropertyModifier extends AntlrNamedElement
{
    public AntlrPropertyModifier(
            @Nonnull ParserRuleContext elementContext,
            @Nullable CompilationUnit compilationUnit,
            boolean inferred,
            @Nonnull ParserRuleContext nameContext,
            @Nonnull String name,
            int ordinal)
    {
        super(elementContext, compilationUnit, inferred, nameContext, name, ordinal);
    }

    public boolean isKey()
    {
        return this.name.equals("key");
    }

    public boolean isID()
    {
        return this.name.equals("id");
    }

    public boolean isVersionNumber()
    {
        return this.name.equals("version");
    }

    public PropertyModifierBuilder build()
    {
        return new PropertyModifierBuilder(
                this.elementContext,
                this.inferred,
                this.nameContext,
                this.name,
                this.ordinal);
    }

    @Override
    public void reportNameErrors(@Nonnull CompilerErrorHolder compilerErrorHolder)
    {
        // intentionally blank
    }
}
