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

        ImmutableList<AntlrDataTypeProperty<?>> dataTypeProperties = this.compilerState
                .getCompilerWalkState()
                .getClassState()
                .getDataTypeProperties();

        if ("validTemporal".equals(modifierText) || "bitemporal".equals(modifierText))
        {
            if (dataTypeProperties.noneSatisfy(AntlrDataTypeProperty::isValidRange))
            {
                this.runCompilerMacro("valid    : TemporalRange?   valid;");
            }
            if (dataTypeProperties.noneSatisfy(AntlrDataTypeProperty::isValidFrom))
            {
                this.runCompilerMacro("validFrom: TemporalInstant? valid from;");
            }
            if (dataTypeProperties.noneSatisfy(AntlrDataTypeProperty::isValidTo))
            {
                this.runCompilerMacro("validTo  : TemporalInstant? valid to;");
            }
        }

        if ("systemTemporal".equals(modifierText) || "bitemporal".equals(modifierText))
        {
            if (dataTypeProperties.noneSatisfy(AntlrDataTypeProperty::isSystemRange))
            {
                this.runCompilerMacro("system    : TemporalRange?   system;");
            }
            if (dataTypeProperties.noneSatisfy(AntlrDataTypeProperty::isSystemFrom))
            {
                this.runCompilerMacro("systemFrom: TemporalInstant? system from;");
            }
            if (dataTypeProperties.noneSatisfy(AntlrDataTypeProperty::isSystemTo))
            {
                this.runCompilerMacro("systemTo  : TemporalInstant? system to;");
            }
        }
    }

    private void runCompilerMacro(@Nonnull String sourceCodeText)
    {
        AntlrModifier classifierModifierState =
                this.compilerState.getCompilerWalkState().getClassifierModifierState();
        ParseTreeListener compilerPhase = new PropertyPhase(this.compilerState);

        this.compilerState.runNonRootCompilerMacro(
                classifierModifierState,
                this,
                sourceCodeText,
                KlassParser::classMember,
                compilerPhase);
    }
}
