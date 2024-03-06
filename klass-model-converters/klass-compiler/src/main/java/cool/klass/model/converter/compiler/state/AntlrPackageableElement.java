package cool.klass.model.converter.compiler.state;

import javax.annotation.Nonnull;

import cool.klass.model.converter.compiler.CompilationUnit;
import org.antlr.v4.runtime.ParserRuleContext;

public abstract class AntlrPackageableElement extends AntlrNamedElement
{
    protected final String packageName;

    protected AntlrPackageableElement(
            @Nonnull ParserRuleContext elementContext,
            CompilationUnit compilationUnit,
            boolean inferred,
            @Nonnull ParserRuleContext nameContext,
            @Nonnull String name,
            String packageName)
    {
        super(elementContext, compilationUnit, inferred, nameContext, name);
        this.packageName = packageName;
    }

    public String getPackageName()
    {
        return this.packageName;
    }
}
