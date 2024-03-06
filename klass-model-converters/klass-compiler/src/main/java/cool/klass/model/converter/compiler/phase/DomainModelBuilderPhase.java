package cool.klass.model.converter.compiler.phase;

import cool.klass.model.converter.compiler.CompilationUnit;
import cool.klass.model.converter.compiler.error.CompilerErrorHolder;
import cool.klass.model.meta.domain.DomainModel.DomainModelBuilder;
import cool.klass.model.meta.grammar.KlassParser.CompilationUnitContext;
import org.eclipse.collections.api.map.MapIterable;

public class DomainModelBuilderPhase extends AbstractCompilerPhase
{
    public DomainModelBuilderPhase(
            CompilerErrorHolder compilerErrorHolder,
            MapIterable<CompilationUnitContext, CompilationUnit> compilationUnitsByContext,
            DomainModelBuilder domainModelBuilder)
    {
        super(compilerErrorHolder, compilationUnitsByContext);
    }
}
