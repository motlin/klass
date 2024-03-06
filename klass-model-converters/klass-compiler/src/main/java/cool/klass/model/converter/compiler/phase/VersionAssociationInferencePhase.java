package cool.klass.model.converter.compiler.phase;

import java.util.IdentityHashMap;

import javax.annotation.Nonnull;

import com.google.common.base.CaseFormat;
import cool.klass.model.converter.compiler.CompilationUnit;
import cool.klass.model.converter.compiler.KlassCompiler;
import cool.klass.model.converter.compiler.error.CompilerErrorHolder;
import cool.klass.model.converter.compiler.state.AntlrDomainModel;
import cool.klass.model.converter.compiler.state.property.AntlrDataTypeProperty;
import cool.klass.model.converter.compiler.state.property.AntlrProperty;
import cool.klass.model.meta.grammar.KlassListener;
import cool.klass.model.meta.grammar.KlassParser.ClassModifierContext;
import org.antlr.v4.runtime.ParserRuleContext;
import org.eclipse.collections.api.list.MutableList;
import org.eclipse.collections.api.map.MutableMap;
import org.eclipse.collections.api.set.MutableSet;
import org.eclipse.collections.impl.factory.Sets;
import org.eclipse.collections.impl.map.mutable.MapAdapter;

public class VersionAssociationInferencePhase extends AbstractDomainModelCompilerPhase
{
    private final MutableSet<CompilationUnit> compilationUnits;

    public VersionAssociationInferencePhase(
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

        MutableList<AntlrDataTypeProperty<?>> keyProperties = this.classState
                .getDataTypePropertyStates()
                .select(AntlrDataTypeProperty::isKey);

        if (keyProperties.isEmpty())
        {
            return;
        }

        String sourceName = this.getSourceName(ctx);
        String klassSourceCode = this.getSourceCode(keyProperties);

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

    private String getSourceName(@Nonnull ClassModifierContext ctx)
    {
        String contextMessage = this.getContextMessage(ctx.getStart());
        return String.format(
                "%s compiler macro (%s)",
                VersionAssociationInferencePhase.class.getSimpleName(),
                contextMessage);
    }

    @Nonnull
    private String getSourceCode(MutableList<AntlrDataTypeProperty<?>> keyProperties)
    {
        String className = this.classState.getName();

        String relationshipKeyClauses = keyProperties
                .collect(AntlrProperty::getName)
                .collect(each -> "this." + each + " == " + className + "Version." + each)
                .makeString("\n        ");

        String associationEndName = CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_CAMEL, className);

        //language=Klass
        return ""
                + "package " + this.classState.getPackageName() + "\n"
                + "\n"
                + "association " + className + "HasVersion\n"
                + "{\n"
                + "    " + associationEndName + ": " + className + "[1..1];\n"
                + "    version: " + className + "Version[1..1] owned version;\n"
                + "\n"
                + "    relationship " + relationshipKeyClauses + "\n"
                + "}\n";
    }

    private void runCompilerPhases(
            MutableSet<CompilationUnit> compilationUnits,
            MutableMap<ParserRuleContext, CompilationUnit> compilationUnitsByContext)
    {
        KlassListener associationPhase = new AssociationPhase(
                this.compilerErrorHolder,
                compilationUnitsByContext,
                this.isInference,
                this.domainModelState);

        KlassCompiler.executeCompilerPhase(associationPhase, compilationUnits);
    }
}
