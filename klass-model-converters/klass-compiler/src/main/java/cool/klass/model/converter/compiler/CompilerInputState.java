package cool.klass.model.converter.compiler;

import java.util.IdentityHashMap;

import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.tree.ParseTreeListener;
import org.antlr.v4.runtime.tree.ParseTreeWalker;
import org.eclipse.collections.api.list.ImmutableList;
import org.eclipse.collections.api.map.MutableMap;
import org.eclipse.collections.api.set.MutableSet;
import org.eclipse.collections.impl.map.mutable.MapAdapter;

public class CompilerInputState
{
    private final MutableSet<CompilationUnit>                    compilationUnits;
    private final MutableMap<ParserRuleContext, CompilationUnit> compilationUnitsByContext;

    private boolean isInference;

    public CompilerInputState(MutableSet<CompilationUnit> compilationUnits)
    {
        this.compilationUnits = compilationUnits;
        this.compilationUnitsByContext = compilationUnits.groupByUniqueKey(
                CompilationUnit::getParserContext,
                MapAdapter.adapt(new IdentityHashMap<>()));
    }

    private void addCompilationUnit(CompilationUnit compilationUnit)
    {
        this.compilationUnits.add(compilationUnit);
        this.compilationUnitsByContext.put(compilationUnit.getParserContext(), compilationUnit);
    }

    private void withInference(Runnable runnable)
    {
        boolean oldIsInference = this.isInference;

        try
        {
            this.isInference = true;
            runnable.run();
        }
        finally
        {
            this.isInference = oldIsInference;
        }
    }

    public void runCompilerMacro(
            CompilationUnit compilationUnit,
            ImmutableList<ParseTreeListener> listeners)
    {
        this.addCompilationUnit(compilationUnit);
        this.withInference(() ->
        {
            ParseTreeWalker parseTreeWalker = new ParseTreeWalker();
            for (ParseTreeListener listener : listeners)
            {
                parseTreeWalker.walk(listener, compilationUnit.getParserContext());
            }
        });
    }

    public MutableSet<CompilationUnit> getCompilationUnits()
    {
        return this.compilationUnits.asUnmodifiable();
    }

    public boolean isInference()
    {
        return this.isInference;
    }

    public CompilationUnit getCompilationUnitByContext(ParserRuleContext ctx)
    {
        return this.compilationUnitsByContext.get(ctx);
    }
}
