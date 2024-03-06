package cool.klass.model.converter.compiler.state;

import java.util.regex.Pattern;

import javax.annotation.Nonnull;

import cool.klass.model.converter.compiler.CompilationUnit;
import org.antlr.v4.runtime.ParserRuleContext;
import org.eclipse.collections.api.list.MutableList;

public abstract class AntlrPackageableElement extends AntlrNamedElement
{
    protected final String packageName;

    protected AntlrPackageableElement(
            @Nonnull ParserRuleContext elementContext,
            CompilationUnit compilationUnit,
            boolean inferred,
            @Nonnull ParserRuleContext nameContext,
            @Nonnull String name,
            int ordinal,
            String packageName)
    {
        super(elementContext, compilationUnit, inferred, nameContext, name, ordinal);
        this.packageName = packageName;
    }

    public String getPackageName()
    {
        return this.packageName;
    }

    @Nonnull
    @Override
    protected Pattern getNamePattern()
    {
        return TYPE_NAME_PATTERN;
    }

    @Override
    public final void getParserRuleContexts(@Nonnull MutableList<ParserRuleContext> parserRuleContexts)
    {
        parserRuleContexts.add(this.elementContext);
        parserRuleContexts.add(this.compilationUnit.getParserContext());
    }
}
