package cool.klass.model.converter.compiler.phase;

import java.util.IdentityHashMap;

import javax.annotation.Nonnull;

import cool.klass.model.converter.compiler.CompilationUnit;
import cool.klass.model.converter.compiler.KlassCompiler;
import cool.klass.model.converter.compiler.error.CompilerErrorHolder;
import cool.klass.model.converter.compiler.state.AntlrDomainModel;
import cool.klass.model.converter.compiler.state.AntlrElement;
import cool.klass.model.converter.compiler.state.property.AntlrDataTypeProperty;
import cool.klass.model.meta.grammar.KlassListener;
import cool.klass.model.meta.grammar.KlassParser.ClassModifierContext;
import org.antlr.v4.runtime.ParserRuleContext;
import org.eclipse.collections.api.map.MutableMap;
import org.eclipse.collections.api.set.MutableSet;
import org.eclipse.collections.impl.factory.Sets;
import org.eclipse.collections.impl.map.mutable.MapAdapter;

public class VersionClassInferencePhase extends AbstractDomainModelCompilerPhase
{
    private final MutableSet<CompilationUnit> compilationUnits;

    public VersionClassInferencePhase(
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
        if (!"versioned".equals(modifierText))
        {
            return;
        }

        String klassSourceCode = this.getSourceCode();
        String sourceName = this.getSourceName(ctx);

        CompilationUnit compilationUnit = CompilationUnit.createFromText(
                sourceName,
                klassSourceCode);

        MutableSet<CompilationUnit> compilationUnits = Sets.mutable.with(compilationUnit);
        MutableMap<ParserRuleContext, CompilationUnit> compilationUnitsByContext = compilationUnits.groupByUniqueKey(
                CompilationUnit::getParserContext,
                MapAdapter.adapt(new IdentityHashMap<>()));

        this.runCompilerPhases(compilationUnits, compilationUnitsByContext);

        this.compilationUnits.add(compilationUnit);
        this.compilationUnitsByContext.put(compilationUnit.getParserContext(), compilationUnit);
    }

    private String getSourceName(@Nonnull ParserRuleContext ctx)
    {
        String contextMessage = this.getContextMessage(ctx.getStart());
        return String.format(
                "%s compiler macro (%s)",
                VersionClassInferencePhase.class.getSimpleName(),
                contextMessage);
    }

    @Nonnull
    private String getSourceCode()
    {
        String keyPropertySourceCode = this.classState
                .getDataTypeProperties()
                .select(AntlrDataTypeProperty::isKey)
                .collect(AntlrElement::getSourceCode)
                .collect(each -> String.format("    %s\n", each))
                .makeString("");

        //language=Klass
        return "package " + this.classState.getPackageName() + "\n"
                + "\n"
                + "class " + this.classState.getName() + "Version systemTemporal\n"
                + "{\n"
                + keyPropertySourceCode
                + "    number: Integer version;\n"
                + "}\n";
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

        KlassListener temporalPropertyInferencePhase = new ClassTemporalPropertyInferencePhase(
                this.compilerErrorHolder,
                compilationUnitsByContext,
                this.domainModelState,
                this.compilationUnits);

        KlassCompiler.executeCompilerPhase(classPhase, compilationUnits);
        KlassCompiler.executeCompilerPhase(temporalPropertyInferencePhase, compilationUnits);
    }
}
