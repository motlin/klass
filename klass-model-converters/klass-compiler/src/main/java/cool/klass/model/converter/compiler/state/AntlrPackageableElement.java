package cool.klass.model.converter.compiler.state;

import cool.klass.model.converter.compiler.CompilationUnit;
import org.antlr.v4.runtime.ParserRuleContext;

public abstract class AntlrPackageableElement extends AntlrNamedElement
{
    protected final String packageName;

    protected AntlrPackageableElement(
            ParserRuleContext elementContext,
            CompilationUnit compilationUnit,
            ParserRuleContext nameContext,
            String name,
            String packageName)
    {
        super(elementContext, compilationUnit, name, nameContext);
        this.packageName = packageName;
    }

    public String getPackageName()
    {
        return this.packageName;
    }
}
