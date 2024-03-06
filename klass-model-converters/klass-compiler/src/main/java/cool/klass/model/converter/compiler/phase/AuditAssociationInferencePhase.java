package cool.klass.model.converter.compiler.phase;

import java.util.Optional;

import javax.annotation.Nonnull;

import com.google.common.base.CaseFormat;
import cool.klass.model.converter.compiler.CompilerState;
import cool.klass.model.converter.compiler.state.AntlrClass;
import cool.klass.model.converter.compiler.state.property.AntlrDataTypeProperty;
import cool.klass.model.converter.compiler.state.property.AntlrModifier;
import cool.klass.model.converter.compiler.state.property.AntlrReferenceProperty;
import cool.klass.model.meta.grammar.KlassParser;
import cool.klass.model.meta.grammar.KlassParser.ClassifierModifierContext;
import org.antlr.v4.runtime.tree.ParseTreeListener;
import org.eclipse.collections.api.block.predicate.Predicate;
import org.eclipse.collections.api.list.ImmutableList;
import org.eclipse.collections.impl.factory.Lists;

public class AuditAssociationInferencePhase
        extends AbstractCompilerPhase
{
    public AuditAssociationInferencePhase(@Nonnull CompilerState compilerState)
    {
        super(compilerState);
    }

    @Nonnull
    @Override
    public String getName()
    {
        return "Audit association";
    }

    @Override
    public void enterClassifierModifier(@Nonnull ClassifierModifierContext ctx)
    {
        super.enterClassifierModifier(ctx);

        String modifierText = ctx.getText();
        if ("audited".equals(modifierText))
        {
            this.addAuditProperties();
        }
    }

    private boolean hasAuditReferenceProperty(Predicate<AntlrReferenceProperty> predicate)
    {
        return this.compilerState
                .getCompilerWalkState()
                .getClassState()
                .getProperties()
                .selectInstancesOf(AntlrReferenceProperty.class)
                .asLazy()
                .anySatisfy(predicate);
    }

    private boolean hasAuditDataTypeProperty(Predicate<AntlrDataTypeProperty> predicate)
    {
        return this.compilerState
                .getCompilerWalkState()
                .getClassState()
                .getDataTypeProperties()
                .count(predicate) == 1;
    }

    private void addAuditProperties()
    {
        Optional<AntlrClass> maybeUserClass = this.compilerState.getDomainModelState().getUserClassState();
        if (maybeUserClass.isEmpty())
        {
            return;
        }
        AntlrClass userClass        = maybeUserClass.get();
        int        userIdProperties = userClass.getDataTypeProperties().count(AntlrDataTypeProperty::isUserId);
        if (userIdProperties != 1)
        {
            return;
        }

        if (!this.hasAuditReferenceProperty(AntlrReferenceProperty::isCreatedBy) && this.hasAuditDataTypeProperty(AntlrDataTypeProperty::isCreatedBy))
        {
            this.runCompilerMacro(this.getSourceCode(userClass, "createdBy", AntlrDataTypeProperty::isCreatedBy));
        }
        if (!this.hasAuditReferenceProperty(AntlrReferenceProperty::isLastUpdatedBy) && this.hasAuditDataTypeProperty(AntlrDataTypeProperty::isLastUpdatedBy))
        {
            this.runCompilerMacro(this.getSourceCode(userClass, "lastUpdatedBy", AntlrDataTypeProperty::isLastUpdatedBy));
        }
    }

    private void runCompilerMacro(String sourceCode)
    {
        AntlrModifier classifierModifierState =
                this.compilerState.getCompilerWalkState().getClassifierModifierState();

        ImmutableList<ParseTreeListener> compilerPhases = Lists.immutable.with(
                new TopLevelElementsPhase(this.compilerState),
                new AssociationPhase(this.compilerState));

        this.compilerState.runRootCompilerMacro(
                classifierModifierState,
                this,
                sourceCode,
                KlassParser::compilationUnit,
                compilerPhases);
    }

    @Nonnull
    private String getSourceCode(
            AntlrClass userClass,
            String modifier,
            Predicate<AntlrDataTypeProperty> predicate)
    {
        String suffix = CaseFormat.LOWER_CAMEL.to(CaseFormat.UPPER_CAMEL, modifier);

        AntlrClass classState         = this.compilerState.getCompilerWalkState().getClassState();
        String     className          = classState.getName();
        String     associationEndName = CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_CAMEL, className);

        String userIdPropertyName = userClass
                .getDataTypeProperties()
                .detect(AntlrDataTypeProperty::isUserId)
                .getName();

        AntlrDataTypeProperty<?> auditProperty = classState.getDataTypeProperties().detect(predicate);

        //language=Klass
        return ""
                + "package " + classState.getPackageName() + "\n"
                + "\n"
                + "association " + className + "Has" + suffix + "\n"
                + "{\n"
                + "    " + associationEndName + suffix + ": " + className + "[0..*];\n"
                + "    " + modifier + ": " + userClass.getName() + "[1..1] " + modifier + ";\n"
                + "\n"
                + "    relationship this." + auditProperty.getName() + " == " + userClass.getName() + "." + userIdPropertyName
                + "\n"
                + "}\n";
    }
}
