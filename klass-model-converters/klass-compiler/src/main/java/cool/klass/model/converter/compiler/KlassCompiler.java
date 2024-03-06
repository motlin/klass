package cool.klass.model.converter.compiler;

import java.util.IdentityHashMap;
import java.util.Set;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import cool.klass.model.converter.compiler.error.CompilerErrorHolder;
import cool.klass.model.converter.compiler.phase.AssociationPhase;
import cool.klass.model.converter.compiler.phase.ClassPhase;
import cool.klass.model.converter.compiler.phase.ClassTemporalPropertyInferencePhase;
import cool.klass.model.converter.compiler.phase.EnumerationsPhase;
import cool.klass.model.converter.compiler.phase.OrderByPhase;
import cool.klass.model.converter.compiler.phase.ParameterizedPropertyPhase;
import cool.klass.model.converter.compiler.phase.ProjectionPhase;
import cool.klass.model.converter.compiler.phase.RelationshipPhase;
import cool.klass.model.converter.compiler.phase.ServicePhase;
import cool.klass.model.converter.compiler.phase.VersionAssociationInferencePhase;
import cool.klass.model.converter.compiler.phase.VersionClassInferencePhase;
import cool.klass.model.converter.compiler.state.AntlrDomainModel;
import cool.klass.model.meta.domain.DomainModelImpl.DomainModelBuilder;
import cool.klass.model.meta.domain.api.DomainModel;
import cool.klass.model.meta.grammar.KlassListener;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.tree.ParseTreeWalker;
import org.eclipse.collections.api.list.ImmutableList;
import org.eclipse.collections.api.map.MutableMap;
import org.eclipse.collections.api.set.ImmutableSet;
import org.eclipse.collections.api.set.MutableSet;
import org.eclipse.collections.impl.factory.Lists;
import org.eclipse.collections.impl.factory.Sets;
import org.eclipse.collections.impl.map.mutable.MapAdapter;
import org.eclipse.collections.impl.set.mutable.SetAdapter;

public class KlassCompiler
{
    private final CompilerErrorHolder compilerErrorHolder;
    private final AntlrDomainModel    domainModelState = new AntlrDomainModel();

    public KlassCompiler(CompilerErrorHolder compilerErrorHolder)
    {
        this.compilerErrorHolder = compilerErrorHolder;
    }

    public static void executeCompilerPhase(
            KlassListener compilerPhase,
            @Nonnull MutableSet<CompilationUnit> compilationUnits)
    {
        ParseTreeWalker parseTreeWalker = new ParseTreeWalker();
        // Compiler macros may add new compilation units within a compiler phase
        ImmutableSet<CompilationUnit> immutableCompilationUnits = compilationUnits.toImmutable();
        for (CompilationUnit compilationUnit : immutableCompilationUnits)
        {
            parseTreeWalker.walk(compilerPhase, compilationUnit.getParserContext());
        }
    }

    @Nullable
    public DomainModel compile(@Nonnull Set<String> classpathLocations)
    {
        MutableSet<CompilationUnit> compilationUnits =
                SetAdapter.adapt(classpathLocations).collect(CompilationUnit::createFromClasspathLocation);

        return this.compile(compilationUnits);
    }

    @Nullable
    public DomainModel compile(@Nonnull MutableSet<CompilationUnit> compilationUnits)
    {
        MutableMap<ParserRuleContext, CompilationUnit> compilationUnitsByContext =
                compilationUnits.groupByUniqueKey(
                        CompilationUnit::getParserContext,
                        MapAdapter.adapt(new IdentityHashMap<>()));

        ImmutableList<KlassListener> phases = this.getCompilerPhases(compilationUnitsByContext, compilationUnits);

        phases.forEachWith(KlassCompiler::executeCompilerPhase, compilationUnits);

        this.domainModelState.reportErrors(this.compilerErrorHolder);

        if (!this.compilerErrorHolder.hasCompilerErrors())
        {
            DomainModelBuilder domainModelBuilder = this.domainModelState.build();
            return domainModelBuilder.build();
        }
        return null;
    }

    private ImmutableList<KlassListener> getCompilerPhases(
            @Nonnull MutableMap<ParserRuleContext, CompilationUnit> compilationUnitsByContext,
            MutableSet<CompilationUnit> compilationUnits)
    {
        // TODO: Move isInference before this.domainModelState
        KlassListener phase1 = new EnumerationsPhase(
                this.compilerErrorHolder,
                compilationUnitsByContext,
                false,
                this.domainModelState);

        KlassListener phase2 = new ClassPhase(
                this.compilerErrorHolder,
                compilationUnitsByContext,
                this.domainModelState,
                false);

        KlassListener phase3 = new ClassTemporalPropertyInferencePhase(
                this.compilerErrorHolder,
                compilationUnitsByContext,
                this.domainModelState,
                compilationUnits);

        KlassListener phase4 = new VersionClassInferencePhase(
                this.compilerErrorHolder,
                compilationUnitsByContext,
                this.domainModelState,
                compilationUnits);

        KlassListener phase5 = new AssociationPhase(
                this.compilerErrorHolder,
                compilationUnitsByContext,
                false,
                this.domainModelState);

        KlassListener phase6 = new VersionAssociationInferencePhase(
                this.compilerErrorHolder,
                compilationUnitsByContext,
                this.domainModelState,
                compilationUnits);

        KlassListener phase7 = new ParameterizedPropertyPhase(
                this.compilerErrorHolder,
                compilationUnitsByContext,
                false,
                this.domainModelState);

        KlassListener phase8 = new RelationshipPhase(
                this.compilerErrorHolder,
                compilationUnitsByContext,
                false,
                this.domainModelState);

        KlassListener phase9 = new ProjectionPhase(
                this.compilerErrorHolder,
                compilationUnitsByContext,
                this.domainModelState,
                false);

        KlassListener phase10 = new ServicePhase(
                this.compilerErrorHolder,
                compilationUnitsByContext,
                this.domainModelState,
                false);

        KlassListener phase11 = new OrderByPhase(
                this.compilerErrorHolder,
                compilationUnitsByContext,
                this.domainModelState,
                false);

        return Lists.immutable.with(
                phase1,
                phase2,
                phase3,
                phase4,
                phase5,
                phase6,
                phase7,
                phase8,
                phase9,
                phase10,
                phase11);
    }

    @Nullable
    public DomainModel compile(CompilationUnit... compilationUnits)
    {
        return this.compile(Sets.mutable.with(compilationUnits));
    }
}
