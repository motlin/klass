package cool.klass.model.converter.compiler.state;

import java.util.Objects;
import java.util.Optional;
import java.util.regex.Pattern;

import javax.annotation.Nonnull;

import cool.klass.model.converter.compiler.CompilationUnit;
import cool.klass.model.converter.compiler.error.CompilerErrorState;
import org.antlr.v4.runtime.ParserRuleContext;
import org.eclipse.collections.api.list.MutableList;

public abstract class AntlrPackageableElement extends AntlrNamedElement
{
    @Nonnull
    protected final ParserRuleContext packageContext;
    @Nonnull
    protected final String            packageName;

    protected AntlrPackageableElement(
            @Nonnull ParserRuleContext elementContext,
            @Nonnull Optional<CompilationUnit> compilationUnit,
            @Nonnull ParserRuleContext nameContext,
            @Nonnull String name,
            int ordinal,
            @Nonnull ParserRuleContext packageContext,
            @Nonnull String packageName)
    {
        super(elementContext, compilationUnit, nameContext, name, ordinal);
        this.packageContext = Objects.requireNonNull(packageContext);
        this.packageName = Objects.requireNonNull(packageName);
    }

    @Nonnull
    public String getPackageName()
    {
        return this.packageName;
    }

    @Override
    public void reportNameErrors(@Nonnull CompilerErrorState compilerErrorHolder)
    {
        super.reportNameErrors(compilerErrorHolder);

        if (!PACKAGE_NAME_PATTERN.matcher(this.packageName).matches())
        {
            String message = String.format(
                    "Package name must match pattern %s but was '%s'.",
                    PACKAGE_NAME_PATTERN,
                    this.packageName);
            compilerErrorHolder.add(this.getCompilationUnit().get(), "ERR_PKG_PAT", message, this.packageContext);
        }
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
        parserRuleContexts.add(this.compilationUnit.get().getParserContext());
    }
}
