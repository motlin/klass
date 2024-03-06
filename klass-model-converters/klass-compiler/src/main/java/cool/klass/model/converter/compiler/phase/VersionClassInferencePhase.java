package cool.klass.model.converter.compiler.phase;

import javax.annotation.Nonnull;

import cool.klass.model.converter.compiler.CompilerState;
import cool.klass.model.converter.compiler.state.AntlrClass;
import cool.klass.model.converter.compiler.state.AntlrClassifierModifier;
import cool.klass.model.converter.compiler.state.AntlrNamedElement;
import cool.klass.model.converter.compiler.state.property.AntlrDataTypeProperty;
import cool.klass.model.converter.compiler.state.property.AntlrDataTypePropertyModifier;
import cool.klass.model.meta.grammar.KlassParser;
import cool.klass.model.meta.grammar.KlassParser.ClassifierModifierContext;
import org.antlr.v4.runtime.tree.ParseTreeListener;
import org.eclipse.collections.api.list.ImmutableList;
import org.eclipse.collections.api.list.ListIterable;
import org.eclipse.collections.impl.factory.Lists;

public class VersionClassInferencePhase extends AbstractCompilerPhase
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

        AntlrClassifierModifier classifierModifierState = this.compilerState.getCompilerWalkState().getClassifierModifierState();

        ImmutableList<ParseTreeListener> compilerPhases = Lists.immutable.with(
                new TopLevelElementsPhase(this.compilerState),
                new ClassifierPhase(this.compilerState),
                new PropertyPhase(this.compilerState),
                new ClassTemporalPropertyInferencePhase(this.compilerState),
                new ClassAuditPropertyInferencePhase(this.compilerState));

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
        String keyPropertySourceCode = classState
                .getDataTypeProperties()
                .select(AntlrDataTypeProperty::isKey)
                .collect(this::getSourceCode)
                .collect(each -> String.format("    %s\n", each))
                .makeString("");

        AntlrClassifierModifier auditedModifier   = classState.getClassifierModifierByName("audited");
        String                  auditedSourceCode = auditedModifier == AntlrClassifierModifier.NOT_FOUND ? "" : " audited";

        // TODO: If main class is transient, version should also be transient, so copy classifier modifiers
        //language=Klass
        String sourceCode = "package " + classState.getPackageName() + "\n"
                + "\n"
                + "class " + classState.getName() + "Version systemTemporal" + auditedSourceCode + "\n"
                + "{\n"
                + keyPropertySourceCode
                + "    number: Integer version;\n"
                + "}\n";
        return sourceCode;
    }

    private String getSourceCode(@Nonnull AntlrDataTypeProperty<?> dataTypeProperty)
    {
        ListIterable<AntlrDataTypePropertyModifier> propertyModifiers = dataTypeProperty
                .getModifiers()
                .collect(AntlrDataTypePropertyModifier.class::cast)
                .reject(AntlrDataTypePropertyModifier::isID);
        String propertyModifierSourceCode = propertyModifiers.isEmpty()
                ? ""
                : propertyModifiers.collect(AntlrNamedElement::getName).makeString(" ", " ", "");
        return String.format(
                "%s: %s%s;",
                dataTypeProperty.getName(),
                dataTypeProperty.getType(),
                propertyModifierSourceCode);
    }
}
