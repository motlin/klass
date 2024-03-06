package cool.klass.model.converter.compiler.state;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import cool.klass.model.converter.compiler.CompilationUnit;
import cool.klass.model.converter.compiler.error.CompilerErrorHolder;
import cool.klass.model.meta.domain.ClassModifier.ClassModifierBuilder;
import org.antlr.v4.runtime.ParserRuleContext;

public class AntlrClassModifier extends AntlrNamedElement
{
    public AntlrClassModifier(
            @Nonnull ParserRuleContext elementContext,
            @Nullable CompilationUnit compilationUnit,
            boolean inferred,
            @Nonnull ParserRuleContext nameContext,
            @Nonnull String name,
            int ordinal)
    {
        super(elementContext, compilationUnit, inferred, nameContext, name, ordinal);
    }

    public boolean isVersioned()
    {
        return this.name.equals("versioned");
    }

    public boolean isTransient()
    {
        return this.name.equals("transient");
    }

    public ClassModifierBuilder build()
    {
        return new ClassModifierBuilder(this.elementContext, this.nameContext, this.name, this.ordinal);
    }

    @Override
    public void reportNameErrors(@Nonnull CompilerErrorHolder compilerErrorHolder)
    {
        // intentionally blank
    }
}
