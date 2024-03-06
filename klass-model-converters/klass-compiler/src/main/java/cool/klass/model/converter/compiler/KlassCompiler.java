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
import cool.klass.model.converter.compiler.phase.ProjectionPhase;
import cool.klass.model.converter.compiler.phase.ResolveTypeErrorsPhase;
import cool.klass.model.converter.compiler.phase.ResolveTypeReferencesPhase;
import cool.klass.model.converter.compiler.phase.ResolveTypesPhase;
import cool.klass.model.converter.compiler.state.AntlrDomainModel;
import cool.klass.model.meta.domain.DomainModel;
import cool.klass.model.meta.domain.DomainModel.DomainModelBuilder;
import cool.klass.model.meta.grammar.KlassListener;
import cool.klass.model.meta.grammar.KlassParser.CompilationUnitContext;
import org.antlr.v4.runtime.tree.ParseTreeWalker;
import org.eclipse.collections.api.list.ImmutableList;
import org.eclipse.collections.api.map.MapIterable;
import org.eclipse.collections.api.set.MutableSet;
import org.eclipse.collections.impl.factory.Lists;
import org.eclipse.collections.impl.factory.Sets;
import org.eclipse.collections.impl.map.mutable.MapAdapter;
import org.eclipse.collections.impl.set.mutable.SetAdapter;

public class KlassCompiler
{
    private final CompilerErrorHolder compilerErrorHolder;

    public KlassCompiler(CompilerErrorHolder compilerErrorHolder)
    {
        this.compilerErrorHolder = compilerErrorHolder;
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
        MapIterable<CompilationUnitContext, CompilationUnit> compilationUnitsByContext =
                compilationUnits.groupByUniqueKey(
                        CompilationUnit::getCompilationUnitContext,
                        MapAdapter.adapt(new IdentityHashMap<>()));

        DeclarationsByNamePhase    phase1 = new DeclarationsByNamePhase();
        ResolveTypeReferencesPhase phase2 = new ResolveTypeReferencesPhase(phase1);
        ResolveTypesPhase          phase3 = new ResolveTypesPhase(phase2);
        KlassListener phase4 = new ResolveTypeErrorsPhase(
                this.compilerErrorHolder,
                compilationUnitsByContext,
                phase3);

        AntlrDomainModel domainModelState = new AntlrDomainModel();

        KlassListener phase5 = new EnumerationsPhase(
                this.compilerErrorHolder,
                compilationUnitsByContext,
                domainModelState);

        KlassListener phase6 = new ClassPhase(
                this.compilerErrorHolder,
                compilationUnitsByContext,
                domainModelState);

        KlassListener phase7 = new ClassTemporalPropertyInferencePhase(
                this.compilerErrorHolder,
                compilationUnitsByContext,
                domainModelState);

        // TODO: Phase for inference on classes?
        // Like adding temporal and audit properties, and version types and version associations

        KlassListener phase8 = new AssociationPhase(
                this.compilerErrorHolder,
                compilationUnitsByContext,
                domainModelState);

        KlassListener phase9 = new ProjectionPhase(
                this.compilerErrorHolder,
                compilationUnitsByContext,
                domainModelState);

        ImmutableList<KlassListener> phases = Lists.immutable.with(
                phase1,
                phase2,
                phase3,
                phase4,
                phase5,
                phase6,
                phase7,
                phase8,
                phase9);

        phases.forEachWith(this::executeCompilerPhase, compilationUnits);

        domainModelState.reportErrors(this.compilerErrorHolder);

        if (!this.compilerErrorHolder.hasCompilerErrors())
        {
            DomainModelBuilder domainModelBuilder = domainModelState.build();
            return domainModelBuilder.build();
        }
        return null;
    }

    @Nullable
    public DomainModel compile(CompilationUnit... compilationUnits)
    {
        return this.compile(Sets.mutable.with(compilationUnits));
    }

    protected void executeCompilerPhase(
            KlassListener compilerPhase,
            @Nonnull MutableSet<CompilationUnit> compilationUnits)
    {
        ParseTreeWalker parseTreeWalker = new ParseTreeWalker();
        for (CompilationUnit compilationUnit : compilationUnits)
        {
            CompilationUnitContext compilationUnitContext = compilationUnit.getCompilationUnitContext();
            parseTreeWalker.walk(compilerPhase, compilationUnitContext);
        }
    }
}
