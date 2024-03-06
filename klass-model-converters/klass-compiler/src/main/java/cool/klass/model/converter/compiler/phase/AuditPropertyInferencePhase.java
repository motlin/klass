package cool.klass.model.converter.compiler.phase;

import java.util.Optional;

import javax.annotation.Nonnull;

import cool.klass.model.converter.compiler.CompilerState;
import cool.klass.model.converter.compiler.state.AntlrClass;
import cool.klass.model.converter.compiler.state.property.AntlrDataTypeProperty;
import cool.klass.model.converter.compiler.state.property.AntlrModifier;
import cool.klass.model.converter.compiler.state.property.validation.AbstractAntlrPropertyValidation;
import cool.klass.model.meta.grammar.KlassParser;
import cool.klass.model.meta.grammar.KlassParser.ClassifierModifierContext;
import org.antlr.v4.runtime.tree.ParseTreeListener;
import org.eclipse.collections.api.block.predicate.Predicate;
import org.eclipse.collections.api.list.ImmutableList;
import org.eclipse.collections.api.list.ListIterable;

// TODO: Only put audit properties onto version types
public class AuditPropertyInferencePhase
        extends AbstractCompilerPhase
{
    public AuditPropertyInferencePhase(@Nonnull CompilerState compilerState)
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
    public void enterClassifierModifier(@Nonnull ClassifierModifierContext ctx)
    {
        super.enterClassifierModifier(ctx);

        String modifierText = ctx.getText();
        if ("audited".equals(modifierText))
        {
            this.addAuditProperties();
        }
    }

    private boolean hasAuditProperty(Predicate<AntlrDataTypeProperty> predicate)
    {
        return this.compilerState
                .getCompilerWalk()
                .getKlass()
                .getAllDataTypeProperties()
                .anySatisfy(predicate);
    }

    /*
    private boolean hasAuditReferenceProperty(Predicate<AntlrParameterizedProperty> predicate)
    {
        return this.compilerState
                .getCompilerWalkState()
                .getKlass()
                .getProperties()
                .selectInstancesOf(AntlrParameterizedProperty.class)
                .anySatisfy(predicate);
    }
    */

    private void addAuditProperties()
    {
        Optional<AntlrClass> maybeUserClass = this.compilerState.getDomainModel().getUserClass();
        if (maybeUserClass.isEmpty())
        {
            return;
        }
        AntlrClass userClass        = maybeUserClass.get();
        ImmutableList<AntlrDataTypeProperty<?>> userIdProperties = userClass
                .getAllDataTypeProperties()
                .select(AntlrDataTypeProperty::isUserId);
        if (userIdProperties.size() != 1)
        {
            return;
        }
        AntlrDataTypeProperty<?> userIdProperty = userIdProperties.getOnly();

        ListIterable<AntlrModifier> modifiers = userIdProperty
                .getModifiers()
                .reject(AntlrModifier::isUserId)
                .reject(AntlrModifier::isKey)
                .reject(AntlrModifier::isId);
        if (modifiers.notEmpty())
        {
            // TODO: Add test coverage for this line
            throw new AssertionError(modifiers);
        }
        ListIterable<AbstractAntlrPropertyValidation> validations = userIdProperty.getValidations();
        String validationSourceCode = validations.isEmpty() ? "" : validations.makeString(" ", " ", "");

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("{\n");

        // TODO: Add validation that any userId is joined to another property that's also userId
        if (!this.hasAuditProperty(AntlrDataTypeProperty::isCreatedBy))
        {
            stringBuilder.append("    createdById    : String createdBy private userId final"  + validationSourceCode + ";\n");
        }
        if (!this.hasAuditProperty(AntlrDataTypeProperty::isCreatedOn))
        {
            stringBuilder.append("    createdOn      : Instant createdOn final;\n");
        }
        if (!this.hasAuditProperty(AntlrDataTypeProperty::isLastUpdatedBy))
        {
            stringBuilder.append("    lastUpdatedById: String lastUpdatedBy private userId"  + validationSourceCode + ";\n");
        }

        stringBuilder.append("}\n");
        this.runCompilerMacro(stringBuilder.toString());

        /*
        if (!this.hasAuditReferenceProperty(AntlrReferenceProperty::isCreatedBy))
        {
            String createdBySourceCodeText = ""
                    + "    createdBy(): " + userClass.getName() + "[1..1] createdBy\n"
                    + "    {\n"
                    + "        this.createdById == " + userClass.getName() + "." + userIdProperty.getName() + "\n"
                    + "    }\n";

            this.runCompilerMacro(createdBySourceCodeText);
        }
        */
        /*
        if (!this.hasAuditReferenceProperty(AntlrReferenceProperty::isLastUpdatedBy))
        {
            String lastUpdatedBySourceCodeText = ""
                    + "    lastUpdatedBy(): " + userClass.getName() + "[1..1] lastUpdatedBy\n"
                    + "    {\n"
                    + "        this.lastUpdatedById == " + userClass.getName() + "." + userIdProperty.getName() + "\n"
                    + "    }\n";

            this.runCompilerMacro(lastUpdatedBySourceCodeText);
        }
        */
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
