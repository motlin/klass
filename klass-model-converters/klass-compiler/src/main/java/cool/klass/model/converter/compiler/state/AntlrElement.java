package cool.klass.model.converter.compiler.state;

import java.util.Objects;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import cool.klass.model.converter.compiler.CompilationUnit;
import org.antlr.v4.runtime.ParserRuleContext;

public abstract class AntlrElement
{
    @Nonnull
    protected final ParserRuleContext elementContext;
    @Nullable
    protected final CompilationUnit   compilationUnit;
    protected final boolean           inferred;

    protected AntlrElement(
            @Nonnull ParserRuleContext elementContext,
            @Nullable CompilationUnit compilationUnit,
            boolean inferred)
    {
        this.elementContext = Objects.requireNonNull(elementContext);
        this.compilationUnit = compilationUnit;
        this.inferred = inferred;
    }

    @Nonnull
    public ParserRuleContext getElementContext()
    {
        return this.elementContext;
    }

    @Nullable
    public CompilationUnit getCompilationUnit()
    {
        return this.compilationUnit;
    }
}
