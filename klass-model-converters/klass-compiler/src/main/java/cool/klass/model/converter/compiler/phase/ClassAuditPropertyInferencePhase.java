package cool.klass.model.converter.compiler.phase;

import java.util.Optional;

import javax.annotation.Nonnull;

import cool.klass.model.converter.compiler.CompilerState;
import cool.klass.model.converter.compiler.state.AntlrClass;
import cool.klass.model.converter.compiler.state.AntlrClassModifier;
import cool.klass.model.converter.compiler.state.property.AntlrDataTypeProperty;
import cool.klass.model.meta.grammar.KlassParser;
import cool.klass.model.meta.grammar.KlassParser.ClassModifierContext;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.tree.ParseTreeListener;
import org.eclipse.collections.api.list.ImmutableList;

// TODO: Only put audit properties onto version types
public class ClassAuditPropertyInferencePhase extends AbstractCompilerPhase
{
    public ClassAuditPropertyInferencePhase(CompilerState compilerState)
    {
        super(compilerState);
    }

    @Nonnull
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

    private boolean hasAuditProperty()
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

        Optional<AntlrClass> userClassOptional = this.compilerState.getDomainModelState().getUserClassState();
        if (!userClassOptional.isPresent())
        {
            throw new AssertionError("TODO");
        }

        AntlrClass userClass = userClassOptional.get();

        ImmutableList<AntlrDataTypeProperty<?>> userIdProperties = userClass
                .getDataTypeProperties()
                .select(AntlrDataTypeProperty::isUserId);
        if (userIdProperties.size() != 1)
        {
            throw new AssertionError("TODO");
        }

        AntlrDataTypeProperty<?> userIdProperty = userIdProperties.getOnly();

        String createdBySourceCodeText = ""
                + "    createdBy(): " + userClass.getName() + "[1..1] createdBy\n"
                + "    {\n"
                + "        this.createdById == " + userClass.getName() + "." + userIdProperty.getName() + "\n"
                + "    }\n";

        String lastUpdatedBySourceCodeText = ""
                + "    lastUpdatedBy(): " + userClass.getName() + "[1..1] lastUpdatedBy\n"
                + "    {\n"
                + "        this.lastUpdatedById == " + userClass.getName() + "." + userIdProperty.getName() + "\n"
                + "    }\n";

        this.runCompilerMacro(ctx, createdBySourceCodeText);
        this.runCompilerMacro(ctx, lastUpdatedBySourceCodeText);
    }

    private void runCompilerMacro(@Nonnull ParserRuleContext ctx, @Nonnull String sourceCodeText)
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
