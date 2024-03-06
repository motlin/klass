package cool.klass.model.converter.compiler.phase;

import javax.annotation.Nonnull;

import cool.klass.model.converter.compiler.CompilerState;
import cool.klass.model.converter.compiler.state.AntlrClassModifier;
import cool.klass.model.converter.compiler.state.property.AntlrDataTypeProperty;
import cool.klass.model.meta.grammar.KlassParser;
import cool.klass.model.meta.grammar.KlassParser.ClassModifierContext;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.tree.ParseTreeListener;

public class ClassAuditPropertyInferencePhase extends AbstractCompilerPhase
{
    public ClassAuditPropertyInferencePhase(CompilerState compilerState)
    {
        super(compilerState);
    }

    @Override
    public String getName()
    {
        return "Audit modifier";
    }

    @Override
    public void enterClassModifier(@Nonnull ClassModifierContext ctx)
    {
        super.enterClassModifier(ctx);

        String modifierText = ctx.getText();
        // TODO: Inference happens for batches of three properties. It could check whether it needs to add each of the three individually
        if ("audited".equals(modifierText) && !this.hasAuditProperty())
        {
            this.addAuditProperties(ctx);
        }
    }

    public boolean hasAuditProperty()
    {
        return this.compilerState
                .getCompilerWalkState()
                .getClassState()
                .getDataTypeProperties()
                .asLazy()
                .anySatisfy(AntlrDataTypeProperty::isAudit);
    }

    private void addAuditProperties(@Nonnull ClassModifierContext ctx)
    {
        this.runCompilerMacro(ctx, "    createdById    : String private createdBy;\n");
        this.runCompilerMacro(ctx, "    createdOn      : Instant createdOn;\n");
        this.runCompilerMacro(ctx, "    lastUpdatedById: String private lastUpdatedBy;\n");

        this.runCompilerMacro(ctx, ""
                + "    createdBy(): User[1..1] createdBy\n"
                + "    {\n"
                + "        this.createdById == User.userId\n"
                + "    }\n");

        this.runCompilerMacro(ctx, ""
                + "    lastUpdatedBy(): User[1..1] lastUpdatedBy\n"
                + "    {\n"
                + "        this.lastUpdatedById == User.userId\n"
                + "    }\n");
    }

    private void runCompilerMacro(@Nonnull ParserRuleContext ctx, String sourceCodeText)
    {
        AntlrClassModifier classModifierState = this.compilerState.getCompilerWalkState().getClassModifierState();
        ParseTreeListener  compilerPhase      = new ClassifierPhase(this.compilerState);

        this.compilerState.runNonRootCompilerMacro(
                classModifierState,
                ctx,
                this,
                sourceCodeText,
                KlassParser::classMember,
                compilerPhase);
    }
}
