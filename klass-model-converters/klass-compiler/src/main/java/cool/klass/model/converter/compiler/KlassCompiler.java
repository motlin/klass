package cool.klass.model.converter.compiler;

import java.util.function.Function;

import javax.annotation.Nullable;

import cool.klass.model.converter.compiler.phase.AssociationPhase;
import cool.klass.model.converter.compiler.phase.ClassTemporalPropertyInferencePhase;
import cool.klass.model.converter.compiler.phase.ClassifierPhase;
import cool.klass.model.converter.compiler.phase.EnumerationsPhase;
import cool.klass.model.converter.compiler.phase.InheritancePhase;
import cool.klass.model.converter.compiler.phase.OrderByPhase;
import cool.klass.model.converter.compiler.phase.ParameterizedPropertyPhase;
import cool.klass.model.converter.compiler.phase.ProjectionPhase;
import cool.klass.model.converter.compiler.phase.RelationshipPhase;
import cool.klass.model.converter.compiler.phase.ServiceCriteriaInferencePhase;
import cool.klass.model.converter.compiler.phase.ServiceCriteriaPhase;
import cool.klass.model.converter.compiler.phase.ServicePhase;
import cool.klass.model.converter.compiler.phase.ServiceVersionInferencePhase;
import cool.klass.model.converter.compiler.phase.UrlParameterPhase;
import cool.klass.model.converter.compiler.phase.VariableResolutionPhase;
import cool.klass.model.converter.compiler.phase.VersionAssociationInferencePhase;
import cool.klass.model.converter.compiler.phase.VersionClassInferencePhase;
import cool.klass.model.meta.domain.api.DomainModel;
import cool.klass.model.meta.grammar.KlassListener;
import org.antlr.v4.runtime.tree.ParseTreeWalker;
import org.eclipse.collections.api.list.ImmutableList;
import org.eclipse.collections.api.set.ImmutableSet;
import org.eclipse.collections.impl.factory.Lists;

public class KlassCompiler
{
    public static final ImmutableList<Function<CompilerState, KlassListener>> COMPILER_PHASE_BUILDERS = Lists.immutable.with(
            EnumerationsPhase::new,
            ClassifierPhase::new,
            InheritancePhase::new,
            ClassTemporalPropertyInferencePhase::new,
            VersionClassInferencePhase::new,
            AssociationPhase::new,
            VersionAssociationInferencePhase::new,
            ParameterizedPropertyPhase::new,
            RelationshipPhase::new,
            ProjectionPhase::new,
            ServicePhase::new,
            UrlParameterPhase::new,
            ServiceVersionInferencePhase::new,
            ServiceCriteriaInferencePhase::new,
            ServiceCriteriaPhase::new,
            VariableResolutionPhase::new,
            OrderByPhase::new);

    private final CompilerState compilerState;

    public KlassCompiler(CompilerState compilerState)
    {
        this.compilerState = compilerState;
    }

    public void executeCompilerPhase(KlassListener compilerPhase)
    {
        // Compiler macros may add new compilation units within a compiler phase, so take an immutable copy
        ImmutableSet<CompilationUnit> immutableCompilationUnits =
                this.compilerState.getCompilerInputState().getCompilationUnits().toImmutable();

        ParseTreeWalker parseTreeWalker = new ParseTreeWalker();
        for (CompilationUnit compilationUnit : immutableCompilationUnits)
        {
            this.compilerState.withCompilationUnit(
                    compilationUnit,
                    () -> parseTreeWalker.walk(compilerPhase, compilationUnit.getParserContext()));
        }
    }

    @Nullable
    public DomainModel compile()
    {
        ImmutableList<KlassListener> compilerPhases =
                COMPILER_PHASE_BUILDERS.collectWith(Function::apply, this.compilerState);
        compilerPhases.forEach(this::executeCompilerPhase);
        this.compilerState.reportErrors();
        return this.compilerState.buildDomainModel();
    }
}
