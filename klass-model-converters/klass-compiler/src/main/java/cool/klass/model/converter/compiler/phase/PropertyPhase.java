package cool.klass.model.converter.compiler.phase;

import java.util.Objects;
import java.util.Optional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import cool.klass.model.converter.compiler.CompilerState;
import cool.klass.model.converter.compiler.state.AntlrClassifier;
import cool.klass.model.converter.compiler.state.AntlrClassifierType;
import cool.klass.model.converter.compiler.state.AntlrDomainModel;
import cool.klass.model.converter.compiler.state.AntlrEnumeration;
import cool.klass.model.converter.compiler.state.AntlrMultiplicity;
import cool.klass.model.converter.compiler.state.AntlrPrimitiveType;
import cool.klass.model.converter.compiler.state.property.AntlrAssociationEndModifier;
import cool.klass.model.converter.compiler.state.property.AntlrAssociationEndSignature;
import cool.klass.model.converter.compiler.state.property.AntlrClassifierTypeOwner;
import cool.klass.model.converter.compiler.state.property.AntlrDataTypeProperty;
import cool.klass.model.converter.compiler.state.property.AntlrEnumerationProperty;
import cool.klass.model.converter.compiler.state.property.AntlrPrimitiveProperty;
import cool.klass.model.converter.compiler.state.property.AntlrPropertyModifier;
import cool.klass.model.converter.compiler.state.property.validation.AntlrMaxLengthPropertyValidation;
import cool.klass.model.converter.compiler.state.property.validation.AntlrMaxPropertyValidation;
import cool.klass.model.converter.compiler.state.property.validation.AntlrMinLengthPropertyValidation;
import cool.klass.model.converter.compiler.state.property.validation.AntlrMinPropertyValidation;
import cool.klass.model.meta.domain.api.PrimitiveType;
import cool.klass.model.meta.grammar.KlassParser.AssociationEndModifierContext;
import cool.klass.model.meta.grammar.KlassParser.AssociationEndSignatureContext;
import cool.klass.model.meta.grammar.KlassParser.ClassifierReferenceContext;
import cool.klass.model.meta.grammar.KlassParser.ClassifierTypeContext;
import cool.klass.model.meta.grammar.KlassParser.EnumerationPropertyContext;
import cool.klass.model.meta.grammar.KlassParser.IntegerLiteralContext;
import cool.klass.model.meta.grammar.KlassParser.MaxLengthValidationContext;
import cool.klass.model.meta.grammar.KlassParser.MaxValidationContext;
import cool.klass.model.meta.grammar.KlassParser.MinLengthValidationContext;
import cool.klass.model.meta.grammar.KlassParser.MinValidationContext;
import cool.klass.model.meta.grammar.KlassParser.MultiplicityContext;
import cool.klass.model.meta.grammar.KlassParser.PrimitivePropertyContext;
import cool.klass.model.meta.grammar.KlassParser.PropertyModifierContext;
import org.eclipse.collections.api.list.ImmutableList;
import org.eclipse.collections.impl.list.mutable.ListAdapter;

public class PropertyPhase extends AbstractCompilerPhase
{
    @Nullable
    private AntlrDataTypeProperty<?>     dataTypePropertyState;
    @Nullable
    private AntlrAssociationEndSignature associationEndSignatureState;
    @Nullable
    private AntlrClassifierType          classifierTypeState;

    @Nullable
    private AntlrClassifierTypeOwner classifierTypeOwnerState;

    public PropertyPhase(CompilerState compilerState)
    {
        super(compilerState);
    }

    @Override
    public void enterPrimitiveProperty(@Nonnull PrimitivePropertyContext ctx)
    {
        super.enterPrimitiveProperty(ctx);

        ImmutableList<AntlrPropertyModifier> propertyModifiers = ListAdapter.adapt(ctx.propertyModifier())
                .collectWithIndex(this::getAntlrPropertyModifier)
                .toImmutable();

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
                propertyModifiers,
                primitiveTypeState);

        this.getClassifierState().enterDataTypeProperty(this.dataTypePropertyState);
    }

    @Override
    public void exitPrimitiveProperty(PrimitivePropertyContext ctx)
    {
        Objects.requireNonNull(this.dataTypePropertyState);
        this.dataTypePropertyState = null;
        super.exitPrimitiveProperty(ctx);
    }

    @Override
    public void enterEnumerationProperty(@Nonnull EnumerationPropertyContext ctx)
    {
        super.enterEnumerationProperty(ctx);

        // TODO: Superclass above all modifiers. Modifiers hold their owners.
        ImmutableList<AntlrPropertyModifier> propertyModifiers = ListAdapter.adapt(ctx.propertyModifier())
                .collectWithIndex(this::getAntlrPropertyModifier)
                .toImmutable();

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
                propertyModifiers,
                enumerationState);

        this.getClassifierState().enterDataTypeProperty(this.dataTypePropertyState);
    }

    @Override
    public void exitEnumerationProperty(EnumerationPropertyContext ctx)
    {
        super.exitEnumerationProperty(ctx);
        Objects.requireNonNull(this.dataTypePropertyState);
        this.dataTypePropertyState = null;
    }

    @Override
    public void enterMinLengthValidation(@Nonnull MinLengthValidationContext ctx)
    {
        super.enterMinLengthValidation(ctx);

        IntegerLiteralContext integerLiteralContext = ctx.integerLiteral();
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

        IntegerLiteralContext integerLiteralContext = ctx.integerLiteral();
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

        IntegerLiteralContext integerLiteralContext = ctx.integerLiteral();
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

        IntegerLiteralContext integerLiteralContext = ctx.integerLiteral();
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

    @Nonnull
    private AntlrPropertyModifier getAntlrPropertyModifier(@Nonnull PropertyModifierContext context, int ordinal)
    {
        return new AntlrPropertyModifier(
                context,
                Optional.of(this.compilerState.getCompilerWalkState().getCurrentCompilationUnit()),
                context,
                context.getText(),
                ordinal + 1);
    }

    @Nonnull
    private AntlrPropertyModifier getAntlrAssociationEndModifier(
            @Nonnull AssociationEndModifierContext context,
            int ordinal)
    {
        return new AntlrPropertyModifier(
                context,
                Optional.of(this.compilerState.getCompilerWalkState().getCurrentCompilationUnit()),
                context,
                context.getText(),
                ordinal + 1);
    }

    @Override
    public void enterAssociationEndSignature(@Nonnull AssociationEndSignatureContext ctx)
    {
        super.enterAssociationEndSignature(ctx);

        String associationEndSignatureName = ctx.identifier().getText();

        if (this.associationEndSignatureState != null)
        {
            throw new IllegalStateException();
        }
        if (this.classifierTypeOwnerState != null)
        {
            throw new IllegalStateException();
        }

        this.associationEndSignatureState = new AntlrAssociationEndSignature(
                ctx,
                Optional.of(this.compilerState.getCompilerWalkState().getCurrentCompilationUnit()),
                ctx.identifier(),
                associationEndSignatureName,
                this.getClassifierState().getNumMembers() + 1,
                this.getClassifierState());
        this.classifierTypeOwnerState     = this.associationEndSignatureState;

        this.getClassifierState().enterAssociationEndSignature(this.associationEndSignatureState);
    }

    @Override
    public void exitAssociationEndSignature(AssociationEndSignatureContext ctx)
    {
        Objects.requireNonNull(this.associationEndSignatureState);
        this.associationEndSignatureState = null;
        this.classifierTypeOwnerState     = null;

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

        AntlrAssociationEndModifier antlrAssociationEndModifier = new AntlrAssociationEndModifier(
                ctx,
                Optional.of(this.compilerState.getCompilerWalkState().getCurrentCompilationUnit()),
                ctx,
                ctx.getText(),
                this.associationEndSignatureState.getNumModifiers() + 1,
                this.associationEndSignatureState);
        this.associationEndSignatureState.enterAssociationEndModifier(antlrAssociationEndModifier);
    }

    @Nullable
    private AntlrClassifier getClassifierState()
    {
        return this.compilerState.getCompilerWalkState().getClassifierState();
    }


    @Override
    public void enterClassifierType(ClassifierTypeContext ctx)
    {
        super.enterClassifierType(ctx);

        if (this.classifierTypeState != null)
        {
            throw new AssertionError();
        }

        if (this.classifierTypeOwnerState == null)
        {
            return;
        }

        this.classifierTypeState = new AntlrClassifierType(
                ctx,
                Optional.of(this.compilerState.getCompilerWalkState().getCurrentCompilationUnit()),
                this.classifierTypeOwnerState);
        this.classifierTypeOwnerState.enterClassifierType(this.classifierTypeState);
    }

    @Override
    public void exitClassifierType(ClassifierTypeContext ctx)
    {
        this.classifierTypeState = null;

        super.exitClassifierType(ctx);
    }

    @Override
    public void enterClassifierReference(ClassifierReferenceContext ctx)
    {
        super.enterClassifierReference(ctx);

        if (this.classifierTypeState == null)
        {
            return;
        }

        String           classifierName        = ctx.identifier().getText();
        AntlrDomainModel domainModelState = this.compilerState.getDomainModelState();
        AntlrClassifier       classifierState       = domainModelState.getClassifierByName(classifierName);

        this.classifierTypeState.enterClassifierReference(classifierState);
    }

    @Override
    public void enterMultiplicity(MultiplicityContext ctx)
    {
        super.enterMultiplicity(ctx);

        if (this.classifierTypeState == null)
        {
            return;
        }

        AntlrMultiplicity multiplicityState = new AntlrMultiplicity(
                ctx,
                Optional.of(this.compilerState.getCompilerWalkState().getCurrentCompilationUnit()),
                this.classifierTypeState);

        this.classifierTypeState.enterMultiplicity(multiplicityState);
    }
}
