package cool.klass.model.converter.compiler.error;

import cool.klass.model.converter.compiler.CompilationUnit;
import org.antlr.v4.runtime.ParserRuleContext;
import org.eclipse.collections.api.list.ImmutableList;
import org.eclipse.collections.api.list.MutableList;
import org.eclipse.collections.impl.factory.Lists;

public class CompilerErrorHolder
{
    private final MutableList<CompilerError> compilerErrors = Lists.mutable.empty();

    public void add(
            CompilationUnit compilationUnit,
            String message,
            ParserRuleContext offendingParserRuleContext,
            ParserRuleContext... parserRuleContexts)
    {
        CompilerError compilerError =
                new CompilerError(compilationUnit, message, offendingParserRuleContext, parserRuleContexts);
        this.compilerErrors.add(compilerError);
    }

    public ImmutableList<CompilerError> getCompilerErrors()
    {
        return this.compilerErrors.toImmutable();
    }

    public boolean hasCompilerErrors()
    {
        return this.compilerErrors.notEmpty();
    }
}
