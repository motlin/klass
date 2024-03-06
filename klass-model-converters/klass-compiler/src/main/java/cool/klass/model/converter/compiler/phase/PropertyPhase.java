package cool.klass.model.converter.compiler.phase;

import java.util.Objects;
import java.util.Optional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import cool.klass.model.converter.compiler.CompilerState;
import cool.klass.model.converter.compiler.state.AntlrClassifier;
import cool.klass.model.converter.compiler.state.AntlrClassifierReference;
import cool.klass.model.converter.compiler.state.AntlrClassifierReferenceOwner;
import cool.klass.model.converter.compiler.state.AntlrDomainModel;
import cool.klass.model.converter.compiler.state.AntlrEnumeration;
import cool.klass.model.converter.compiler.state.AntlrMultiplicity;
import cool.klass.model.converter.compiler.state.AntlrMultiplicityOwner;
import cool.klass.model.converter.compiler.state.AntlrPrimitiveType;
import cool.klass.model.converter.compiler.state.property.AntlrAssociationEndSignature;
import cool.klass.model.converter.compiler.state.property.AntlrDataTypeProperty;
import cool.klass.model.converter.compiler.state.property.AntlrEnumerationProperty;
import cool.klass.model.converter.compiler.state.property.AntlrModifier;
import cool.klass.model.converter.compiler.state.property.AntlrPrimitiveProperty;
import cool.klass.model.converter.compiler.state.property.validation.AntlrMaxLengthPropertyValidation;
import cool.klass.model.converter.compiler.state.property.validation.AntlrMaxPropertyValidation;
import cool.klass.model.converter.compiler.state.property.validation.AntlrMinLengthPropertyValidation;
import cool.klass.model.converter.compiler.state.property.validation.AntlrMinPropertyValidation;
import cool.klass.model.meta.domain.api.PrimitiveType;
import cool.klass.model.meta.grammar.KlassParser.AssociationEndModifierContext;
import cool.klass.model.meta.grammar.KlassParser.AssociationEndSignatureContext;
import cool.klass.model.meta.grammar.KlassParser.ClassifierReferenceContext;
import cool.klass.model.meta.grammar.KlassParser.DataTypePropertyModifierContext;
import cool.klass.model.meta.grammar.KlassParser.EnumerationPropertyContext;
import cool.klass.model.meta.grammar.KlassParser.IntegerLiteralContext;
import cool.klass.model.meta.grammar.KlassParser.MaxLengthValidationContext;
import cool.klass.model.meta.grammar.KlassParser.MaxValidationContext;
import cool.klass.model.meta.grammar.KlassParser.MinLengthValidationContext;
import cool.klass.model.meta.grammar.KlassParser.MinValidationContext;
import cool.klass.model.meta.grammar.KlassParser.MultiplicityContext;
import cool.klass.model.meta.grammar.KlassParser.PrimitivePropertyContext;

public class PropertyPhase
        extends AbstractCompilerPhase
{
    @Nullable
    private AntlrDataTypeProperty<?>      dataTypePropertyState;
    @Nullable
    private AntlrAssociationEndSignature  associationEndSignatureState;
    @Nullable
    private AntlrClassifierReferenceOwner classifierReferenceOwnerState;
    @Nullable
    private AntlrMultiplicityOwner        multiplicityOwnerState;

    public PropertyPhase(@Nonnull CompilerState compilerState)
    {
        super(compilerState);
    }

    @Override
    public void enterPrimitiveProperty(@Nonnull PrimitivePropertyContext ctx)
    {
        super.enterPrimitiveProperty(ctx);

        String             propertyName       = ctx.identifier().getText();
        boolean            isOptional         = ctx.optionalMarker() != null;
        String             primitiveTypeName  = ctx.primitiveType().getText();
        PrimitiveType      primitiveType      = PrimitiveType.byPrettyName(primitiveTypeName);
        AntlrPrimitiveType primitiveTypeState = AntlrPrimitiveType.valueOf(primitiveType);

        if (this.dataTypePropertyState != null)
        {
            throw new IllegalStateException();
        }
        this.dataTypePropertyState = new AntlrPrimitiveProperty(
                ctx,
                Optional.of(this.compilerState.getCompilerWalkState().getCurrentCompilationUnit()),
                ctx.identifier(),
                propertyName,
                this.getClassifierState().getNumMembers() + 1,
                this.getClassifierState(),
                isOptional,
                primitiveTypeState);

        this.getClassifierState().enterDataTypeProperty(this.dataTypePropertyState);
    }

    @Override
    public void exitPrimitiveProperty(@Nonnull PrimitivePropertyContext ctx)
    {
        Objects.requireNonNull(this.dataTypePropertyState);
        this.dataTypePropertyState = null;
        super.exitPrimitiveProperty(ctx);
    }

    @Override
    public void enterEnumerationProperty(@Nonnull EnumerationPropertyContext ctx)
    {
        super.enterEnumerationProperty(ctx);

        String           propertyName     = ctx.identifier().getText();
        boolean          isOptional       = ctx.optionalMarker() != null;
        AntlrDomainModel domainModelState = this.compilerState.getDomainModelState();
        String           enumerationName  = ctx.enumerationReference().getText();
        AntlrEnumeration enumerationState = domainModelState.getEnumerationByName(enumerationName);

        if (this.dataTypePropertyState != null)
        {
            throw new IllegalStateException();
        }
        this.dataTypePropertyState = new AntlrEnumerationProperty(
                ctx,
                Optional.of(this.compilerState.getCompilerWalkState().getCurrentCompilationUnit()),
                ctx.identifier(),
                propertyName,
                this.getClassifierState().getNumMembers() + 1,
                this.getClassifierState(),
                isOptional,
                enumerationState);

        this.getClassifierState().enterDataTypeProperty(this.dataTypePropertyState);
    }

    @Override
    public void exitEnumerationProperty(@Nonnull EnumerationPropertyContext ctx)
    {
        Objects.requireNonNull(this.dataTypePropertyState);
        this.dataTypePropertyState = null;

        super.exitEnumerationProperty(ctx);
    }

    @Override
    public void enterMinLengthValidation(@Nonnull MinLengthValidationContext ctx)
    {
        super.enterMinLengthValidation(ctx);

        IntegerLiteralContext integerLiteralContext = ctx.integerValidationParameter().integerLiteral();
        int                   length                = this.getIntegerFromLiteral(integerLiteralContext);
        AntlrMinLengthPropertyValidation minLengthValidationState = new AntlrMinLengthPropertyValidation(
                ctx,
                Optional.of(this.compilerState.getCompilerWalkState().getCurrentCompilationUnit()),
                this.dataTypePropertyState,
                length);
        this.dataTypePropertyState.addMinLengthValidationState(minLengthValidationState);
    }

    @Override
    public void enterMaxLengthValidation(@Nonnull MaxLengthValidationContext ctx)
    {
        super.enterMaxLengthValidation(ctx);

        IntegerLiteralContext integerLiteralContext = ctx.integerValidationParameter().integerLiteral();
        int                   length                = this.getIntegerFromLiteral(integerLiteralContext);
        AntlrMaxLengthPropertyValidation maxLengthValidationState = new AntlrMaxLengthPropertyValidation(
                ctx,
                Optional.of(this.compilerState.getCompilerWalkState().getCurrentCompilationUnit()),
                this.dataTypePropertyState,
                length);
        this.dataTypePropertyState.addMaxLengthValidationState(maxLengthValidationState);
    }

    @Override
    public void enterMinValidation(@Nonnull MinValidationContext ctx)
    {
        super.enterMinValidation(ctx);

        IntegerLiteralContext integerLiteralContext = ctx.integerValidationParameter().integerLiteral();
        int                   minimum               = this.getIntegerFromLiteral(integerLiteralContext);
        AntlrMinPropertyValidation minValidationState = new AntlrMinPropertyValidation(
                ctx,
                Optional.of(this.compilerState.getCompilerWalkState().getCurrentCompilationUnit()),
                this.dataTypePropertyState,
                minimum);
        this.dataTypePropertyState.addMinValidationState(minValidationState);
    }

    @Override
    public void enterMaxValidation(@Nonnull MaxValidationContext ctx)
    {
        super.enterMaxValidation(ctx);

        IntegerLiteralContext integerLiteralContext = ctx.integerValidationParameter().integerLiteral();
        int                   maximum               = this.getIntegerFromLiteral(integerLiteralContext);
        AntlrMaxPropertyValidation maxValidationState = new AntlrMaxPropertyValidation(
                ctx,
                Optional.of(this.compilerState.getCompilerWalkState().getCurrentCompilationUnit()),
                this.dataTypePropertyState,
                maximum);
        this.dataTypePropertyState.addMaxValidationState(maxValidationState);
    }

    private int getIntegerFromLiteral(@Nonnull IntegerLiteralContext integerLiteralContext)
    {
        String integerText        = integerLiteralContext.getText();
        String withoutUnderscores = integerText.replaceAll("_", "");
        return Integer.decode(withoutUnderscores);
    }

    @Override
    public void enterDataTypePropertyModifier(DataTypePropertyModifierContext ctx)
    {
        AntlrModifier modifierState = new AntlrModifier(
                ctx,
                Optional.of(this.compilerState.getCompilerWalkState().getCurrentCompilationUnit()),
                ctx,
                ctx.getText(),
                this.dataTypePropertyState.getNumModifiers() + 1,
                this.dataTypePropertyState);
        this.dataTypePropertyState.enterModifier(modifierState);
    }

    @Override
    public void enterAssociationEndSignature(@Nonnull AssociationEndSignatureContext ctx)
    {
        super.enterAssociationEndSignature(ctx);

        if (this.associationEndSignatureState != null)
        {
            throw new IllegalStateException();
        }
        if (this.classifierReferenceOwnerState != null)
        {
            throw new IllegalStateException();
        }
        if (this.multiplicityOwnerState != null)
        {
            throw new IllegalStateException();
        }

        String associationEndSignatureName = ctx.identifier().getText();
        this.associationEndSignatureState  = new AntlrAssociationEndSignature(
                ctx,
                Optional.of(this.compilerState.getCompilerWalkState().getCurrentCompilationUnit()),
                ctx.identifier(),
                associationEndSignatureName,
                this.getClassifierState().getNumMembers() + 1,
                this.getClassifierState());
        this.classifierReferenceOwnerState = this.associationEndSignatureState;
        this.multiplicityOwnerState        = this.associationEndSignatureState;
        this.getClassifierState().enterAssociationEndSignature(this.associationEndSignatureState);
    }

    @Override
    public void exitAssociationEndSignature(@Nonnull AssociationEndSignatureContext ctx)
    {
        Objects.requireNonNull(this.associationEndSignatureState);
        this.associationEndSignatureState  = null;
        this.classifierReferenceOwnerState = null;
        this.multiplicityOwnerState        = null;
        super.exitAssociationEndSignature(ctx);
    }

    @Override
    public void enterAssociationEndModifier(@Nonnull AssociationEndModifierContext ctx)
    {
        super.enterAssociationEndModifier(ctx);

        if (this.associationEndSignatureState == null)
        {
            return;
        }

        AntlrModifier antlrAssociationEndModifier = new AntlrModifier(
                ctx,
                Optional.of(this.compilerState.getCompilerWalkState().getCurrentCompilationUnit()),
                ctx,
                ctx.getText(),
                this.associationEndSignatureState.getNumModifiers() + 1,
                this.associationEndSignatureState);
        this.associationEndSignatureState.enterModifier(antlrAssociationEndModifier);
    }

    @Nullable
    private AntlrClassifier getClassifierState()
    {
        return this.compilerState.getCompilerWalkState().getClassifierState();
    }

    @Override
    public void enterClassifierReference(@Nonnull ClassifierReferenceContext ctx)
    {
        super.enterClassifierReference(ctx);

        if (this.classifierReferenceOwnerState == null)
        {
            return;
        }

        String           classifierName   = ctx.identifier().getText();
        AntlrDomainModel domainModelState = this.compilerState.getDomainModelState();
        AntlrClassifier  classifierState  = domainModelState.getClassifierByName(classifierName);
        AntlrClassifierReference classifierReferenceState = new AntlrClassifierReference(
                ctx,
                Optional.of(this.compilerState.getCompilerWalkState().getCurrentCompilationUnit()),
                this.classifierReferenceOwnerState,
                classifierState);

        this.classifierReferenceOwnerState.enterClassifierReference(classifierReferenceState);
    }

    @Override
    public void enterMultiplicity(@Nonnull MultiplicityContext ctx)
    {
        super.enterMultiplicity(ctx);

        if (this.multiplicityOwnerState == null)
        {
            return;
        }

        AntlrMultiplicity multiplicityState = new AntlrMultiplicity(
                ctx,
                Optional.of(this.compilerState.getCompilerWalkState().getCurrentCompilationUnit()),
                this.multiplicityOwnerState);

        this.associationEndSignatureState.enterMultiplicity(multiplicityState);
    }
}
