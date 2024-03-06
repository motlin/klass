package cool.klass.model.converter.compiler.phase;

import javax.annotation.Nonnull;

import cool.klass.model.converter.compiler.CompilerState;
import cool.klass.model.converter.compiler.state.AntlrClass;
import cool.klass.model.converter.compiler.state.AntlrNamedElement;
import cool.klass.model.converter.compiler.state.property.AntlrDataTypeProperty;
import cool.klass.model.converter.compiler.state.property.AntlrPropertyModifier;
import cool.klass.model.meta.grammar.KlassParser;
import cool.klass.model.meta.grammar.KlassParser.ClassModifierContext;
import org.antlr.v4.runtime.tree.ParseTreeListener;
import org.eclipse.collections.api.list.ImmutableList;
import org.eclipse.collections.impl.factory.Lists;

public class VersionClassInferencePhase extends AbstractCompilerPhase
{
    public VersionClassInferencePhase(CompilerState compilerState)
    {
        super(compilerState);
    }

    @Override
    public void enterClassModifier(@Nonnull ClassModifierContext ctx)
    {
        super.enterClassModifier(ctx);
        String modifierText = ctx.getText();
        if (!"versioned".equals(modifierText))
        {
            return;
        }

        String klassSourceCode = this.getSourceCode();

        ImmutableList<ParseTreeListener> compilerPhases = Lists.immutable.with(
                new ClassPhase(this.compilerState),
                new ClassTemporalPropertyInferencePhase(this.compilerState));

        this.compilerState.runRootCompilerMacro(
                ctx,
                VersionClassInferencePhase.class,
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

        //language=Klass
        String sourceCode = "package " + classState.getPackageName() + "\n"
                + "\n"
                + "class " + classState.getName() + "Version systemTemporal\n"
                + "{\n"
                + keyPropertySourceCode
                + "    number: Integer version;\n"
                + "}\n";
        return sourceCode;
    }

    private String getSourceCode(AntlrDataTypeProperty<?> dataTypeProperty)
    {
        ImmutableList<AntlrPropertyModifier> propertyModifiers = dataTypeProperty.getPropertyModifiers()
                .reject(AntlrPropertyModifier::isID);
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
