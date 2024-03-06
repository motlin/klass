package cool.klass.model.converter.compiler.state;

import java.util.Objects;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import cool.klass.model.converter.compiler.CompilationUnit;
import org.antlr.v4.runtime.ParserRuleContext;

public abstract class AntlrNamedElement extends AntlrElement
{
    @Nonnull
    protected final ParserRuleContext nameContext;
    @Nonnull
    protected final String            name;

    protected AntlrNamedElement(
            @Nonnull ParserRuleContext elementContext,
            @Nullable CompilationUnit compilationUnit,
            boolean inferred,
            @Nonnull ParserRuleContext nameContext,
            @Nonnull String name)
    {
        super(elementContext, compilationUnit, inferred);
        this.name = Objects.requireNonNull(name);
        this.nameContext = Objects.requireNonNull(nameContext);
    }

    @Nonnull
    public ParserRuleContext getNameContext()
    {
        return this.nameContext;
    }

    @Nonnull
    public String getName()
    {
        return this.name;
    }
}
