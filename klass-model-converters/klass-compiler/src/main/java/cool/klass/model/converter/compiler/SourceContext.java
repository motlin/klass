package cool.klass.model.converter.compiler;

import java.util.Objects;

import org.antlr.v4.runtime.ParserRuleContext;

public class SourceContext
{
    private final CompilationUnit   compilationUnit;
    private final ParserRuleContext elementContext;

    public SourceContext(CompilationUnit compilationUnit, ParserRuleContext elementContext)
    {
        this.compilationUnit = Objects.requireNonNull(compilationUnit);
        this.elementContext  = Objects.requireNonNull(elementContext);
    }

    public CompilationUnit getCompilationUnit()
    {
        return this.compilationUnit;
    }

    public ParserRuleContext getElementContext()
    {
        return this.elementContext;
    }

    @Override
    public String toString()
    {
        return "SourceContext{"
                + "compilationUnit=" + this.compilationUnit
                + ", elementContext=" + this.elementContext.getText()
                + '}';
    }
}
