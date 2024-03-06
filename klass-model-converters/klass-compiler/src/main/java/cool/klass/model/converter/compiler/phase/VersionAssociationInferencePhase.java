package cool.klass.model.converter.compiler.phase;

import java.util.IdentityHashMap;

import javax.annotation.Nonnull;

import cool.klass.model.converter.compiler.CompilationUnit;
import cool.klass.model.converter.compiler.KlassCompiler;
import cool.klass.model.converter.compiler.error.CompilerErrorHolder;
import cool.klass.model.converter.compiler.state.AntlrClass;
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

public class VersionAssociationInferencePhase extends AbstractCompilerPhase
{
    private final AntlrDomainModel domainModelState;
    private final MutableSet<CompilationUnit> compilationUnits;

    public VersionAssociationInferencePhase(
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
            this.addVersionTypes(classState);
        }
    }

    private void addVersionTypes(@Nonnull AntlrClass classState)
    {
        MutableList<AntlrDataTypeProperty<?>> keyProperties = classState
                .getDataTypeProperties()
                .select(AntlrDataTypeProperty::isKey);

        if (keyProperties.isEmpty())
        {
            return;
        }

        String className   = classState.getName();

        String relationshipKeyClauses = keyProperties
                .collect(AntlrProperty::getName)
                .collect(each -> "this." + each + " == " + className + "Version." + each)
                .makeString("\n        ");

        //language=Klass
        String klassSourceCode = "package " + classState.getPackageName() + "\n"
                + "\n"
                + "association " + className + "HasVersion versions(" + className + "Version)\n"
                + "{\n"
                + "    question: " + className + "[1..1]\n"
                + "    version : " + className + "Version[1..1]\n"
                + "\n"
                + "    relationship " + relationshipKeyClauses + "\n"
                + "}\n";

        CompilationUnit compilationUnit = CompilationUnit.createFromText(VersionAssociationInferencePhase.class.getSimpleName() + " compiler macro",
                klassSourceCode);

        MutableSet<CompilationUnit> compilationUnits = Sets.mutable.with(compilationUnit);
        MutableMap<ParserRuleContext, CompilationUnit> compilationUnitsByContext = compilationUnits.groupByUniqueKey(
                CompilationUnit::getParserContext,
                MapAdapter.adapt(new IdentityHashMap<>()));

        KlassListener associationPhase = new AssociationPhase(
                this.compilerErrorHolder,
                compilationUnitsByContext,
                this.domainModelState);

        KlassCompiler.executeCompilerPhase(associationPhase, compilationUnits);

        this.compilationUnits.add(compilationUnit);
        this.compilationUnitsByContext.put(compilationUnit.getParserContext(), compilationUnit);
    }
}
