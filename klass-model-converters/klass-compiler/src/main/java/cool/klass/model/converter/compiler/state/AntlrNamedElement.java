package cool.klass.model.converter.compiler.state;

import java.util.Objects;

import cool.klass.model.converter.compiler.CompilationUnit;
import org.antlr.v4.runtime.ParserRuleContext;

public abstract class AntlrNamedElement extends AntlrElement
{
    protected final ParserRuleContext nameContext;
    protected final String            name;

    protected AntlrNamedElement(
            ParserRuleContext elementContext,
            CompilationUnit compilationUnit,
            boolean inferred,
            String name,
            ParserRuleContext nameContext)
    {
        super(elementContext, compilationUnit, inferred);
        this.name = Objects.requireNonNull(name);
        this.nameContext = Objects.requireNonNull(nameContext);
    }

    public ParserRuleContext getNameContext()
    {
        return this.nameContext;
    }

    public String getName()
    {
        return this.name;
    }
}
