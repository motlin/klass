package cool.klass.model.converter.compiler.state.parameter;

import java.util.Objects;

import javax.annotation.Nonnull;

import cool.klass.model.converter.compiler.CompilationUnit;
import cool.klass.model.converter.compiler.state.AntlrElement;
import org.antlr.v4.runtime.ParserRuleContext;

public class AntlrParameterModifier extends AntlrElement
{
    @Nonnull
    private final String name;

    public AntlrParameterModifier(
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

    public boolean isUserId()
    {
        return this.name.equals("userId");
    }

    public boolean isId()
    {
        return this.name.equals("id");
    }

    public boolean isVersionNumber()
    {
        return this.name.equals("version");
    }
}
