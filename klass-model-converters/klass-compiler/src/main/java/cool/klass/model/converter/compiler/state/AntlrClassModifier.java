package cool.klass.model.converter.compiler.state;

import java.util.Objects;

import javax.annotation.Nonnull;

import cool.klass.model.converter.compiler.CompilationUnit;
import org.antlr.v4.runtime.ParserRuleContext;

public class AntlrClassModifier extends AntlrElement
{
    @Nonnull
    private final String name;

    public AntlrClassModifier(
            @Nonnull ParserRuleContext context,
            CompilationUnit compilationUnit,
            boolean inferred,
            String name)
    {
        super(context, compilationUnit, inferred);
        this.name = Objects.requireNonNull(name);
    }

    @Nonnull
    public String getName()
    {
        return this.name;
    }

    public boolean isVersioned()
    {
        return this.name.equals("versioned");
    }

    public boolean isTransient()
    {
        return this.name.equals("transient");
    }
}
