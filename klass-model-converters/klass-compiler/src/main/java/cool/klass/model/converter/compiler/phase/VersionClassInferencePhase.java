package cool.klass.model.converter.compiler.phase;

import java.util.IdentityHashMap;

import javax.annotation.Nonnull;

import cool.klass.model.converter.compiler.CompilationUnit;
import cool.klass.model.converter.compiler.KlassCompiler;
import cool.klass.model.converter.compiler.error.CompilerErrorHolder;
import cool.klass.model.converter.compiler.state.AntlrClass;
import cool.klass.model.converter.compiler.state.AntlrDomainModel;
import cool.klass.model.converter.compiler.state.AntlrElement;
import cool.klass.model.converter.compiler.state.property.AntlrDataTypeProperty;
import cool.klass.model.meta.grammar.KlassListener;
import cool.klass.model.meta.grammar.KlassParser.ClassModifierContext;
import org.antlr.v4.runtime.ParserRuleContext;
import org.eclipse.collections.api.list.MutableList;
import org.eclipse.collections.api.map.MutableMap;
import org.eclipse.collections.api.set.MutableSet;
import org.eclipse.collections.impl.factory.Sets;
import org.eclipse.collections.impl.map.mutable.MapAdapter;

public class VersionClassInferencePhase extends AbstractCompilerPhase
{
    private final AntlrDomainModel            domainModelState;
    private final MutableSet<CompilationUnit> compilationUnits;

    public VersionClassInferencePhase(
            @Nonnull CompilerErrorHolder compilerErrorHolder,
            @Nonnull MutableMap<ParserRuleContext, CompilationUnit> compilationUnitsByContext,
            AntlrDomainModel domainModelState,
            MutableSet<CompilationUnit> compilationUnits)
    {
        super(compilerErrorHolder, compilationUnitsByContext);
        this.domainModelState = domainModelState;
        this.compilationUnits = compilationUnits;
    }

    @Override
    public void enterClassModifier(@Nonnull ClassModifierContext ctx)
    {
        AntlrClass classState = this.domainModelState.getClassByContext(this.classDeclarationContext);

        String modifierText = ctx.getText();
        if ("versioned".equals(modifierText))
        {
            this.addVersionTypes(ctx, classState);
        }
    }

    private void addVersionTypes(
            ClassModifierContext ctx,
            @Nonnull AntlrClass classState)
    {
        String packageName = classState.getPackageName();
        String className   = classState.getName();

        MutableList<AntlrDataTypeProperty<?>> keyProperties = classState
                .getDataTypeProperties()
                .select(AntlrDataTypeProperty::isKey);

        String keyPropertySourceCode = keyProperties
                .collect(AntlrElement::getSourceCode)
                .collect(each -> String.format("    %s\n", each))
                .makeString("");

        //language=Klass
        String klassSourceCode = "package " + packageName + "\n"
                + "\n"
                + "class " + className + "Version systemTemporal versions(" + className + ")\n"
                + "{\n"
                + keyPropertySourceCode
                + "    number: Integer;\n"
                + "}\n";

        String contextMessage = this.getContextMessage(ctx.getStart());
        String sourceName = String.format(
                "%s compiler macro (%s)",
                VersionClassInferencePhase.class.getSimpleName(),
                contextMessage);

        CompilationUnit compilationUnit = CompilationUnit.createFromText(
                sourceName,
                klassSourceCode);

        MutableSet<CompilationUnit> compilationUnits = Sets.mutable.with(compilationUnit);
        MutableMap<ParserRuleContext, CompilationUnit> compilationUnitsByContext = compilationUnits.groupByUniqueKey(
                CompilationUnit::getParserContext,
                MapAdapter.adapt(new IdentityHashMap<>()));

        KlassListener classPhase = new ClassPhase(
                this.compilerErrorHolder,
                compilationUnitsByContext,
                this.domainModelState);

        KlassListener temporalPropertyInferencePhase = new ClassTemporalPropertyInferencePhase(
                this.compilerErrorHolder,
                compilationUnitsByContext,
                this.domainModelState);

        KlassCompiler.executeCompilerPhase(classPhase, compilationUnits);
        KlassCompiler.executeCompilerPhase(temporalPropertyInferencePhase, compilationUnits);

        this.compilationUnits.add(compilationUnit);
        this.compilationUnitsByContext.put(compilationUnit.getParserContext(), compilationUnit);
    }
}
