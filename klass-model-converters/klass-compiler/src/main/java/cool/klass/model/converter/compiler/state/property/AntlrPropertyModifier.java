package cool.klass.model.converter.compiler.state.property;

import java.util.Objects;

import javax.annotation.Nonnull;

import cool.klass.model.converter.compiler.CompilationUnit;
import cool.klass.model.converter.compiler.state.AntlrElement;
import org.antlr.v4.runtime.ParserRuleContext;

public class AntlrPropertyModifier extends AntlrElement
{
    @Nonnull
    private final String name;

    public AntlrPropertyModifier(
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

    public boolean isKey()
    {
        return this.name.equals("key");
    }

    public boolean isVersionNumber()
    {
        return this.name.equals("version");
    }
}
