package cool.klass.model.converter.compiler.phase;

import javax.annotation.Nonnull;

import cool.klass.model.converter.compiler.CompilerState;
import cool.klass.model.converter.compiler.state.property.AntlrDataTypeProperty;
import cool.klass.model.converter.compiler.state.property.AntlrModifier;
import cool.klass.model.meta.grammar.KlassParser;
import cool.klass.model.meta.grammar.KlassParser.ClassifierModifierContext;
import org.antlr.v4.runtime.tree.ParseTreeListener;
import org.eclipse.collections.api.list.ImmutableList;

public class ClassTemporalPropertyInferencePhase
        extends AbstractCompilerPhase
{
    public ClassTemporalPropertyInferencePhase(@Nonnull CompilerState compilerState)
    {
        super(compilerState);
    }

    @Nonnull
    @Override
    public String getName()
    {
        return "Temporal modifier";
    }

    @Override
    public void enterClassifierModifier(@Nonnull ClassifierModifierContext ctx)
    {
        super.enterClassifierModifier(ctx);

        String modifierText = ctx.getText();

        ImmutableList<AntlrDataTypeProperty<?>> allDataTypeProperties = this.compilerState
                .getCompilerWalk()
                .getKlass()
                .getAllDataTypeProperties();

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("{\n");

        if ("validTemporal".equals(modifierText) || "bitemporal".equals(modifierText))
        {
            if (allDataTypeProperties.noneSatisfy(AntlrDataTypeProperty::isValidRange))
            {
                stringBuilder.append("    valid    : TemporalRange?   valid private;\n");
            }
            if (allDataTypeProperties.noneSatisfy(AntlrDataTypeProperty::isValidFrom))
            {
                stringBuilder.append("    validFrom: TemporalInstant? valid from;\n");
            }
            if (allDataTypeProperties.noneSatisfy(AntlrDataTypeProperty::isValidTo))
            {
                stringBuilder.append("    validTo  : TemporalInstant? valid to;\n");
            }
        }

        if ("systemTemporal".equals(modifierText) || "bitemporal".equals(modifierText))
        {
            if (allDataTypeProperties.noneSatisfy(AntlrDataTypeProperty::isSystemRange))
            {
                stringBuilder.append("    system    : TemporalRange?   system private;\n");
            }
            if (allDataTypeProperties.noneSatisfy(AntlrDataTypeProperty::isSystemFrom))
            {
                stringBuilder.append("    systemFrom: TemporalInstant? system from;\n");
            }
            if (allDataTypeProperties.noneSatisfy(AntlrDataTypeProperty::isSystemTo))
            {
                stringBuilder.append("    systemTo  : TemporalInstant? system to;\n");
            }

            stringBuilder.append("}\n");

            this.runCompilerMacro(stringBuilder.toString());
        }
    }

    private void runCompilerMacro(@Nonnull String sourceCodeText)
    {
        if (sourceCodeText.equals("{\n}\n"))
        {
            return;
        }
        AntlrModifier classifierModifierState =
                this.compilerState.getCompilerWalk().getClassifierModifier();
        ParseTreeListener compilerPhase = new PropertyPhase(this.compilerState);

        this.compilerState.runNonRootCompilerMacro(
                classifierModifierState,
                this,
                sourceCodeText,
                KlassParser::classBody,
                compilerPhase);
    }
}
