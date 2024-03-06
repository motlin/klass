package cool.klass.model.converter.compiler.state;

import java.util.Objects;

import cool.klass.model.converter.compiler.CompilationUnit;
import org.antlr.v4.runtime.ParserRuleContext;

public abstract class AntlrElement
{
    protected final ParserRuleContext elementContext;
    protected final CompilationUnit   compilationUnit;

    protected AntlrElement(ParserRuleContext elementContext, CompilationUnit compilationUnit)
    {
        this.elementContext = Objects.requireNonNull(elementContext);
        this.compilationUnit = compilationUnit;
    }

    public ParserRuleContext getElementContext()
    {
        return this.elementContext;
    }

    public CompilationUnit getCompilationUnit()
    {
        return this.compilationUnit;
    }
}
