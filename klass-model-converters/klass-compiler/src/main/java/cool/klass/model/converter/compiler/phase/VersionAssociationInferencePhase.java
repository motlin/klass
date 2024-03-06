package cool.klass.model.converter.compiler.phase;

import javax.annotation.Nonnull;

import com.google.common.base.CaseFormat;
import cool.klass.model.converter.compiler.CompilerState;
import cool.klass.model.converter.compiler.state.AntlrClass;
import cool.klass.model.converter.compiler.state.property.AntlrDataTypeProperty;
import cool.klass.model.converter.compiler.state.property.AntlrModifier;
import cool.klass.model.converter.compiler.state.property.AntlrProperty;
import cool.klass.model.meta.grammar.KlassParser;
import cool.klass.model.meta.grammar.KlassParser.ClassifierModifierContext;
import org.antlr.v4.runtime.tree.ParseTreeListener;
import org.eclipse.collections.api.list.ImmutableList;
import org.eclipse.collections.impl.factory.Lists;

public class VersionAssociationInferencePhase extends AbstractCompilerPhase
{
    public VersionAssociationInferencePhase(@Nonnull CompilerState compilerState)
    {
        super(compilerState);
    }

    @Nonnull
    @Override
    public String getName()
    {
        return "Version association";
    }

    @Override
    public void enterClassifierModifier(@Nonnull ClassifierModifierContext ctx)
    {
        super.enterClassifierModifier(ctx);
        String modifierText = ctx.getText();
        if (!"versioned".equals(modifierText))
        {
            return;
        }

        AntlrClass classState = this.compilerState.getCompilerWalkState().getClassState();
        ImmutableList<AntlrDataTypeProperty<?>> keyProperties = classState
                .getDataTypeProperties()
                .select(AntlrDataTypeProperty::isKey);

        if (keyProperties.isEmpty())
        {
            return;
        }

        AntlrModifier classifierModifierState =
                this.compilerState.getCompilerWalkState().getClassifierModifierState();
        String                  klassSourceCode         = this.getSourceCode(keyProperties);

        ImmutableList<ParseTreeListener> compilerPhases = Lists.immutable.with(
                new TopLevelElementsPhase(this.compilerState),
                new AssociationPhase(this.compilerState));

        this.compilerState.runRootCompilerMacro(
                classifierModifierState,
                this,
                klassSourceCode,
                KlassParser::compilationUnit,
                compilerPhases);
    }

    @Nonnull
    private String getSourceCode(@Nonnull ImmutableList<AntlrDataTypeProperty<?>> keyProperties)
    {
        AntlrClass classState = this.compilerState.getCompilerWalkState().getClassState();
        String     className  = classState.getName();

        String relationshipKeyClauses = keyProperties
                .collect(AntlrProperty::getName)
                .collect(each -> "this." + each + " == " + className + "Version." + each)
                .makeString("\n        && ");

        String associationEndName = CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_CAMEL, className);

        //language=Klass
        return ""
                + "package " + classState.getPackageName() + "\n"
                + "\n"
                + "association " + className + "HasVersion\n"
                + "{\n"
                + "    " + associationEndName + ": " + className + "[1..1];\n"
                + "    version: " + className + "Version[1..1] owned version;\n"
                + "\n"
                + "    relationship " + relationshipKeyClauses + "\n"
                + "}\n";
    }
}
