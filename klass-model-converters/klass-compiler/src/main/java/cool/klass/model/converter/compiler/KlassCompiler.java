package cool.klass.model.converter.compiler;

import java.util.IdentityHashMap;
import java.util.Set;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import cool.klass.model.converter.compiler.error.CompilerErrorHolder;
import cool.klass.model.converter.compiler.phase.AssociationPhase;
import cool.klass.model.converter.compiler.phase.ClassPhase;
import cool.klass.model.converter.compiler.phase.ClassTemporalPropertyInferencePhase;
import cool.klass.model.converter.compiler.phase.DeclarationsByNamePhase;
import cool.klass.model.converter.compiler.phase.EnumerationsPhase;
import cool.klass.model.converter.compiler.phase.OrderByPhase;
import cool.klass.model.converter.compiler.phase.ParameterizedPropertyPhase;
import cool.klass.model.converter.compiler.phase.ProjectionPhase;
import cool.klass.model.converter.compiler.phase.ResolveTypeErrorsPhase;
import cool.klass.model.converter.compiler.phase.ResolveTypeReferencesPhase;
import cool.klass.model.converter.compiler.phase.ResolveTypesPhase;
import cool.klass.model.converter.compiler.phase.ServicePhase;
import cool.klass.model.converter.compiler.phase.VersionAssociationInferencePhase;
import cool.klass.model.converter.compiler.phase.VersionClassInferencePhase;
import cool.klass.model.converter.compiler.phase.VersionReferencePhase;
import cool.klass.model.converter.compiler.state.AntlrDomainModel;
import cool.klass.model.meta.domain.DomainModel;
import cool.klass.model.meta.domain.DomainModel.DomainModelBuilder;
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
                this.domainModelState,
                false);

        KlassListener phase2 = new ClassPhase(
                this.compilerErrorHolder,
                compilationUnitsByContext,
                this.domainModelState,
                false);

        KlassListener phase3 = new ClassTemporalPropertyInferencePhase(
                this.compilerErrorHolder,
                compilationUnitsByContext,
                this.domainModelState);

        KlassListener phase4 = new VersionClassInferencePhase(
                this.compilerErrorHolder,
                compilationUnitsByContext,
                this.domainModelState,
                compilationUnits);

        KlassListener phase5 = new AssociationPhase(
                this.compilerErrorHolder,
                compilationUnitsByContext,
                this.domainModelState,
                false);

        KlassListener phase6 = new VersionAssociationInferencePhase(
                this.compilerErrorHolder,
                compilationUnitsByContext,
                this.domainModelState,
                compilationUnits);

        KlassListener phase7 = new VersionReferencePhase(
                this.compilerErrorHolder,
                compilationUnitsByContext,
                this.domainModelState,
                false);

        KlassListener parameterizedPropertyPhase = new ParameterizedPropertyPhase(
                this.compilerErrorHolder,
                compilationUnitsByContext,
                false,
                this.domainModelState);

        // TODO: RelationshipPhase should probably be here, and come out of the association phase

        // TODO: Redo these 4 phases to use domainModelState
        DeclarationsByNamePhase    phase8  = new DeclarationsByNamePhase();
        ResolveTypeReferencesPhase phase9  = new ResolveTypeReferencesPhase(phase8);
        ResolveTypesPhase          phase10 = new ResolveTypesPhase(phase9);
        KlassListener phase11 = new ResolveTypeErrorsPhase(
                this.compilerErrorHolder,
                compilationUnitsByContext,
                phase10);

        KlassListener phase12 = new ProjectionPhase(
                this.compilerErrorHolder,
                compilationUnitsByContext,
                this.domainModelState,
                false);

        KlassListener phase13 = new ServicePhase(
                this.compilerErrorHolder,
                compilationUnitsByContext,
                this.domainModelState,
                false);

        KlassListener orderByPhase = new OrderByPhase(
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
                parameterizedPropertyPhase,
                phase8,
                phase9,
                phase10,
                phase11,
                phase12,
                phase13,
                orderByPhase);
    }

    @Nullable
    public DomainModel compile(CompilationUnit... compilationUnits)
    {
        return this.compile(Sets.mutable.with(compilationUnits));
    }
}
