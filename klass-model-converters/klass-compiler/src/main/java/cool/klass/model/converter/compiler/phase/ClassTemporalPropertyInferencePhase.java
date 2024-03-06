package cool.klass.model.converter.compiler.phase;

import java.util.IdentityHashMap;

import javax.annotation.Nonnull;

import cool.klass.model.converter.compiler.CompilationUnit;
import cool.klass.model.converter.compiler.KlassCompiler;
import cool.klass.model.converter.compiler.error.CompilerErrorHolder;
import cool.klass.model.converter.compiler.state.AntlrClass;
import cool.klass.model.converter.compiler.state.AntlrDomainModel;
import cool.klass.model.converter.compiler.state.property.AntlrDataTypeProperty;
import cool.klass.model.meta.grammar.KlassListener;
import cool.klass.model.meta.grammar.KlassParser.ClassModifierContext;
import org.antlr.v4.runtime.ParserRuleContext;
import org.eclipse.collections.api.map.MutableMap;
import org.eclipse.collections.api.set.MutableSet;
import org.eclipse.collections.impl.factory.Sets;
import org.eclipse.collections.impl.map.mutable.MapAdapter;

public class ClassTemporalPropertyInferencePhase extends AbstractDomainModelCompilerPhase
{
    private final MutableSet<CompilationUnit> compilationUnits;

    public ClassTemporalPropertyInferencePhase(
            @Nonnull CompilerErrorHolder compilerErrorHolder,
            @Nonnull MutableMap<ParserRuleContext, CompilationUnit> compilationUnitsByContext,
            AntlrDomainModel domainModelState,
            MutableSet<CompilationUnit> compilationUnits)
    {
        super(compilerErrorHolder, compilationUnitsByContext, true, domainModelState);
        this.compilationUnits = compilationUnits;
    }

    @Override
    public void enterClassModifier(@Nonnull ClassModifierContext ctx)
    {
        String modifierText = ctx.getText();
        // TODO: Inference happens for batches of three properties. It could check whether it needs to add each of the three individually
        if (!this.hasTemporalProperty(classState)
                && ("validTemporal".equals(modifierText) || "bitemporal".equals(modifierText)))
        {
            this.addTemporalProperties(ctx, "valid");
        }

        if (!this.hasTemporalProperty(classState)
                && ("systemTemporal".equals(modifierText) || "bitemporal".equals(modifierText)))
        {
            this.addTemporalProperties(ctx, "system");
        }
    }

    private boolean hasTemporalProperty(AntlrClass classState)
    {
        return classState
                .getDataTypeProperties()
                .anySatisfy(AntlrDataTypeProperty::isTemporal);
    }

    private void addTemporalProperties(
            @Nonnull ClassModifierContext ctx,
            @Nonnull String prefix)
    {
        this.runCompilerMacro(ctx, prefix + "    : TemporalRange   " + prefix + ";");
        this.runCompilerMacro(ctx, prefix + "From: TemporalInstant " + prefix + " from;");
        this.runCompilerMacro(ctx, prefix + "To  : TemporalInstant " + prefix + " to;");
    }

    private void runCompilerMacro(@Nonnull ParserRuleContext ctx, String sourceCodeText)
    {
        String sourceName = this.getSourceName(ctx);

        CompilationUnit compilationUnit = CompilationUnit.createFromText(
                sourceName,
                sourceCodeText);

        MutableSet<CompilationUnit> compilationUnits = Sets.mutable.with(compilationUnit);
        MutableMap<ParserRuleContext, CompilationUnit> compilationUnitsByContext = compilationUnits.groupByUniqueKey(
                CompilationUnit::getParserContext,
                MapAdapter.adapt(new IdentityHashMap<>()));

        this.runCompilerPhases(compilationUnits, compilationUnitsByContext);

        this.compilationUnits.add(compilationUnit);
        this.compilationUnitsByContext.put(compilationUnit.getParserContext(), compilationUnit);
    }

    // TODO: Extract new commonality in newer compiler macros
    private String getSourceName(@Nonnull ParserRuleContext ctx)
    {
        String contextMessage = this.getContextMessage(ctx.getStart());
        return String.format(
                "%s compiler macro (%s)",
                ClassTemporalPropertyInferencePhase.class.getSimpleName(),
                contextMessage);
    }

    private void runCompilerPhases(
            MutableSet<CompilationUnit> compilationUnits,
            MutableMap<ParserRuleContext, CompilationUnit> compilationUnitsByContext)
    {
        KlassListener classPhase = new ClassPhase(
                this.compilerErrorHolder,
                compilationUnitsByContext,
                this.domainModelState,
                this.isInference);

        KlassCompiler.executeCompilerPhase(classPhase, compilationUnits);
    }
}
