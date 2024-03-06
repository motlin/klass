package cool.klass.model.converter.compiler;

import java.util.IdentityHashMap;

import javax.annotation.Nonnull;

import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.tree.ParseTreeListener;
import org.antlr.v4.runtime.tree.ParseTreeWalker;
import org.eclipse.collections.api.collection.ImmutableCollection;
import org.eclipse.collections.api.list.ImmutableList;
import org.eclipse.collections.api.list.MutableList;
import org.eclipse.collections.api.map.MutableMap;
import org.eclipse.collections.impl.map.mutable.MapAdapter;

public class CompilerInputState
{
    @Nonnull
    private final MutableList<CompilationUnit>                   compilationUnits;
    private final MutableMap<ParserRuleContext, CompilationUnit> compilationUnitsByContext;

    public CompilerInputState(@Nonnull ImmutableCollection<CompilationUnit> compilationUnits)
    {
        this.compilationUnits          = compilationUnits.toList();
        this.compilationUnitsByContext = compilationUnits.groupByUniqueKey(
                CompilationUnit::getParserContext,
                MapAdapter.adapt(new IdentityHashMap<>()));
    }

    private void addCompilationUnit(@Nonnull CompilationUnit compilationUnit)
    {
        this.compilationUnits.add(compilationUnit);
        this.compilationUnitsByContext.put(compilationUnit.getParserContext(), compilationUnit);
    }

    public void runCompilerMacro(
            @Nonnull CompilationUnit compilationUnit,
            @Nonnull ImmutableList<ParseTreeListener> listeners)
    {
        this.addCompilationUnit(compilationUnit);
        ParseTreeWalker parseTreeWalker = new ParseTreeWalker();
        for (ParseTreeListener listener : listeners)
        {
            parseTreeWalker.walk(listener, compilationUnit.getParserContext());
        }
    }

    public void runInPlaceCompilerMacro(
            @Nonnull CompilationUnit compilationUnit,
            @Nonnull ImmutableList<ParseTreeListener> listeners)
    {
        ParseTreeWalker parseTreeWalker = new ParseTreeWalker();
        for (ParseTreeListener listener : listeners)
        {
            parseTreeWalker.walk(listener, compilationUnit.getParserContext());
        }
    }

    public MutableList<CompilationUnit> getCompilationUnits()
    {
        return this.compilationUnits.asUnmodifiable();
    }

    public CompilationUnit getCompilationUnitByContext(ParserRuleContext ctx)
    {
        return this.compilationUnitsByContext.get(ctx);
    }
}
