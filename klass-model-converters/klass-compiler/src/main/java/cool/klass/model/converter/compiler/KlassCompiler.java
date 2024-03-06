package cool.klass.model.converter.compiler;

import java.util.IdentityHashMap;
import java.util.Set;

import cool.klass.model.converter.compiler.error.CompilerErrorHolder;
import cool.klass.model.converter.compiler.phase.BuildAntlrStatePhase;
import cool.klass.model.converter.compiler.phase.CompilerPhaseEnumerationsAndClasses;
import cool.klass.model.converter.compiler.phase.DeclarationsByNamePhase;
import cool.klass.model.converter.compiler.phase.DomainModelBuilderPhase;
import cool.klass.model.converter.compiler.phase.MembersByNamePhase;
import cool.klass.model.converter.compiler.phase.ResolveTypeErrorsPhase;
import cool.klass.model.converter.compiler.phase.ResolveTypeReferencesPhase;
import cool.klass.model.converter.compiler.phase.ResolveTypesPhase;
import cool.klass.model.converter.compiler.phase.TopLevelElementNameCountPhase;
import cool.klass.model.converter.compiler.phase.TopLevelElementNameDuplicatePhase;
import cool.klass.model.meta.domain.DomainModel.DomainModelBuilder;
import cool.klass.model.meta.grammar.KlassListener;
import cool.klass.model.meta.grammar.KlassParser.CompilationUnitContext;
import org.antlr.v4.runtime.tree.ParseTreeWalker;
import org.eclipse.collections.api.list.ImmutableList;
import org.eclipse.collections.api.map.MapIterable;
import org.eclipse.collections.api.set.MutableSet;
import org.eclipse.collections.impl.factory.Lists;
import org.eclipse.collections.impl.map.mutable.MapAdapter;
import org.eclipse.collections.impl.set.mutable.SetAdapter;

public class KlassCompiler
{
    private final DomainModelBuilder  domainModelBuilder;
    private final CompilerErrorHolder compilerErrorHolder;

    public KlassCompiler(
            DomainModelBuilder domainModelBuilder,
            CompilerErrorHolder compilerErrorHolder)
    {
        this.domainModelBuilder = domainModelBuilder;
        this.compilerErrorHolder = compilerErrorHolder;
    }

    public void compile(Set<String> classpathLocations)
    {
        MutableSet<CompilationUnit> compilationUnits =
                SetAdapter.adapt(classpathLocations).collect(CompilationUnit::getCompilationUnit);

        MapIterable<CompilationUnitContext, CompilationUnit> compilationUnitsByContext =
                compilationUnits.groupByUniqueKey(
                        CompilationUnit::getCompilationUnitContext,
                        MapAdapter.adapt(new IdentityHashMap<>()));

        TopLevelElementNameCountPhase phase1 = new TopLevelElementNameCountPhase();
        KlassListener phase2 = new TopLevelElementNameDuplicatePhase(
                compilationUnitsByContext,
                this.compilerErrorHolder,
                phase1);
        DeclarationsByNamePhase    phase3 = new DeclarationsByNamePhase();
        ResolveTypeReferencesPhase phase4 = new ResolveTypeReferencesPhase(phase3);
        ResolveTypesPhase          phase5 = new ResolveTypesPhase(phase4);
        KlassListener phase6 = new ResolveTypeErrorsPhase(
                this.compilerErrorHolder,
                compilationUnitsByContext,
                phase5);

        BuildAntlrStatePhase buildAntlrStatePhase = new BuildAntlrStatePhase(
                this.compilerErrorHolder,
                compilationUnitsByContext,
                phase5);

        MembersByNamePhase phase7 = new MembersByNamePhase(
                this.compilerErrorHolder,
                compilationUnitsByContext,
                phase3,
                phase4,
                phase5);

        CompilerPhaseEnumerationsAndClasses phase8 = new CompilerPhaseEnumerationsAndClasses(
                compilationUnitsByContext,
                this.compilerErrorHolder,
                this.domainModelBuilder,
                phase5);

        DomainModelBuilderPhase phase9 = new DomainModelBuilderPhase(
                this.compilerErrorHolder,
                compilationUnitsByContext,
                this.domainModelBuilder);

        this.executeCompilerPhase(compilationUnits, phase1);
        this.executeCompilerPhase(compilationUnits, phase2);
        this.executeCompilerPhase(compilationUnits, phase3);
        this.executeCompilerPhase(compilationUnits, phase4);
        this.executeCompilerPhase(compilationUnits, phase5);
        this.executeCompilerPhase(compilationUnits, phase6);
        this.executeCompilerPhase(compilationUnits, buildAntlrStatePhase);
        this.executeCompilerPhase(compilationUnits, phase7);
        this.executeCompilerPhase(compilationUnits, phase8);
        this.executeCompilerPhase(compilationUnits, phase9);

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

        for (KlassListener phase : phases)
        {
        }

        this.compilerErrorHolder.logAll();
    }

    protected void executeCompilerPhase(
            MutableSet<CompilationUnit> compilationUnits,
            KlassListener compilerPhase)
    {
        ParseTreeWalker parseTreeWalker = new ParseTreeWalker();
        for (CompilationUnit compilationUnit : compilationUnits)
        {
            CompilationUnitContext compilationUnitContext = compilationUnit.getCompilationUnitContext();
            parseTreeWalker.walk(compilerPhase, compilationUnitContext);
        }
    }
}
