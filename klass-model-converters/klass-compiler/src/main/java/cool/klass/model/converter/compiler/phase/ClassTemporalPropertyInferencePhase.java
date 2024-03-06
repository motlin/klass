package cool.klass.model.converter.compiler.phase;

import javax.annotation.Nonnull;

import cool.klass.model.converter.compiler.CompilerState;
import cool.klass.model.converter.compiler.state.AntlrClassModifier;
import cool.klass.model.converter.compiler.state.property.AntlrDataTypeProperty;
import cool.klass.model.meta.grammar.KlassParser;
import cool.klass.model.meta.grammar.KlassParser.ClassModifierContext;
import org.antlr.v4.runtime.tree.ParseTreeListener;
import org.eclipse.collections.api.block.predicate.Predicate;

public class ClassTemporalPropertyInferencePhase extends AbstractCompilerPhase
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
    public void enterClassModifier(@Nonnull ClassModifierContext ctx)
    {
        super.enterClassModifier(ctx);

        String modifierText = ctx.getText();
        // TODO: Inference happens for batches of three properties. It could check whether it needs to add each of the three individually
        if (!this.hasTemporalProperty(AntlrDataTypeProperty::isValid)
                && ("validTemporal".equals(modifierText) || "bitemporal".equals(modifierText)))
        {
            this.addTemporalProperties(ctx, "valid");
        }

        if (!this.hasTemporalProperty(AntlrDataTypeProperty::isSystem)
                && ("systemTemporal".equals(modifierText) || "bitemporal".equals(modifierText)))
        {
            this.addTemporalProperties(ctx, "system");
        }
    }

    private boolean hasTemporalProperty(Predicate<AntlrDataTypeProperty<?>> predicate)
    {
        return this.compilerState
                .getCompilerWalkState()
                .getClassState()
                .getDataTypeProperties()
                .asLazy()
                .select(AntlrDataTypeProperty::isTemporal)
                .anySatisfy(predicate);
    }

    private void addTemporalProperties(
            @Nonnull ClassModifierContext ctx,
            @Nonnull String prefix)
    {
        this.runCompilerMacro(prefix + "    : TemporalRange?   " + prefix + ";");
        this.runCompilerMacro(prefix + "From: TemporalInstant? " + prefix + " from;");
        this.runCompilerMacro(prefix + "To  : TemporalInstant? " + prefix + " to;");
    }

    private void runCompilerMacro(@Nonnull String sourceCodeText)
    {
        AntlrClassModifier classModifierState = this.compilerState.getCompilerWalkState().getClassModifierState();
        ParseTreeListener  compilerPhase      = new PropertyPhase(this.compilerState);

        this.compilerState.runNonRootCompilerMacro(
                classModifierState,
                this,
                sourceCodeText,
                KlassParser::classMember,
                compilerPhase);
    }
}
