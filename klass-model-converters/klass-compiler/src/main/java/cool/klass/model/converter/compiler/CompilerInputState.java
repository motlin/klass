package cool.klass.model.converter.compiler;

import java.util.IdentityHashMap;
import java.util.Optional;

import cool.klass.model.converter.compiler.state.AntlrElement;
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

    private Optional<AntlrElement> macroElement = Optional.empty();

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

    private void withInference(AntlrElement macroElement, Runnable runnable)
    {
        Optional<AntlrElement> oldMacroElement = this.macroElement;

        try
        {
            this.macroElement = Optional.of(macroElement);
            runnable.run();
        }
        finally
        {
            this.macroElement = oldMacroElement;
        }
    }

    public void runCompilerMacro(
            AntlrElement macroElement,
            CompilationUnit compilationUnit,
            ImmutableList<ParseTreeListener> listeners)
    {
        this.addCompilationUnit(compilationUnit);
        this.withInference(macroElement, () ->
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

    public Optional<AntlrElement> getMacroElement()
    {
        return this.macroElement;
    }

    public CompilationUnit getCompilationUnitByContext(ParserRuleContext ctx)
    {
        return this.compilationUnitsByContext.get(ctx);
    }
}
