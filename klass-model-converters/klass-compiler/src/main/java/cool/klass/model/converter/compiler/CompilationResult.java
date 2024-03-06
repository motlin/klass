package cool.klass.model.converter.compiler;

import org.eclipse.collections.api.list.ImmutableList;

public interface CompilationResult
{
    ImmutableList<CompilationUnit> getCompilationUnits();
}
