package cool.klass.model.converter.compiler.phase;

import javax.annotation.Nonnull;

import cool.klass.model.converter.compiler.CompilerState;
import cool.klass.model.converter.compiler.state.AntlrClass;
import cool.klass.model.converter.compiler.state.AntlrNamedElement;
import cool.klass.model.converter.compiler.state.property.AntlrDataTypeProperty;
import cool.klass.model.converter.compiler.state.property.AntlrModifier;
import cool.klass.model.meta.grammar.KlassParser;
import cool.klass.model.meta.grammar.KlassParser.ClassifierModifierContext;
import org.antlr.v4.runtime.tree.ParseTreeListener;
import org.eclipse.collections.api.list.ImmutableList;
import org.eclipse.collections.api.list.ListIterable;
import org.eclipse.collections.impl.factory.Lists;

public class VersionClassInferencePhase
        extends AbstractCompilerPhase
{
    public VersionClassInferencePhase(@Nonnull CompilerState compilerState)
    {
        super(compilerState);
    }

    @Nonnull
    @Override
    public String getName()
    {
        return "Version class";
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

        String klassSourceCode = this.getSourceCode();

        AntlrModifier classifierModifierState = this.compilerState.getCompilerWalkState().getClassifierModifierState();

        ImmutableList<ParseTreeListener> compilerPhases = Lists.immutable.with(
                new TopLevelElementsPhase(this.compilerState),
                new ClassifierPhase(this.compilerState),
                new PropertyPhase(this.compilerState),
                new ClassTemporalPropertyInferencePhase(this.compilerState));

        this.compilerState.runRootCompilerMacro(
                classifierModifierState,
                this,
                klassSourceCode,
                KlassParser::compilationUnit,
                compilerPhases);
    }

    @Nonnull
    private String getSourceCode()
    {
        AntlrClass classState = this.compilerState.getCompilerWalkState().getClassState();
        String propertySourceCode = classState
                .getDataTypeProperties()
                .select(property -> property.isKey() || property.isValid() || property.isSystem() || property.isAudit())
                .collect(this::getSourceCode)
                .collect(each -> String.format("    %s\n", each))
                .makeString("");

        AntlrModifier auditedModifier   = classState.getModifierByName("audited");
        String        auditedSourceCode = auditedModifier == AntlrModifier.NOT_FOUND ? "" : " audited";

        // TODO: If main class is transient, version should also be transient, so copy classifier modifiers
        //language=Klass
        return "package " + classState.getPackageName() + "\n"
                + "\n"
                + "class " + classState.getName() + "Version systemTemporal" + auditedSourceCode + "\n"
                + "{\n"
                + propertySourceCode
                + "    number: Integer version;\n"
                + "}\n";
    }

    private String getSourceCode(@Nonnull AntlrDataTypeProperty<?> dataTypeProperty)
    {
        String isOptionalString = dataTypeProperty.isOptional() ? "?" : "";

        ListIterable<AntlrModifier> modifiers = dataTypeProperty.getModifiers().reject(AntlrModifier::isId);
        String modifierSourceCode = modifiers.isEmpty()
                ? ""
                : modifiers.collect(AntlrNamedElement::getName).makeString(" ", " ", "");

        return String.format(
                "%s: %s%s%s;",
                dataTypeProperty.getName(),
                dataTypeProperty.getType(),
                isOptionalString,
                modifierSourceCode);
    }
}
