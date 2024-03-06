package cool.klass.model.converter.compiler.phase;

import javax.annotation.Nonnull;

import cool.klass.model.converter.compiler.CompilerState;
import cool.klass.model.converter.compiler.state.AntlrClass;
import cool.klass.model.converter.compiler.state.AntlrClassModifier;
import cool.klass.model.converter.compiler.state.AntlrEnumeration;
import cool.klass.model.converter.compiler.state.AntlrPrimitiveType;
import cool.klass.model.converter.compiler.state.property.AntlrEnumerationProperty;
import cool.klass.model.converter.compiler.state.property.AntlrPrimitiveProperty;
import cool.klass.model.converter.compiler.state.property.AntlrPropertyModifier;
import cool.klass.model.meta.domain.api.PrimitiveType;
import cool.klass.model.meta.grammar.KlassParser.ClassDeclarationContext;
import cool.klass.model.meta.grammar.KlassParser.ClassModifierContext;
import cool.klass.model.meta.grammar.KlassParser.EnumerationPropertyContext;
import cool.klass.model.meta.grammar.KlassParser.EnumerationReferenceContext;
import cool.klass.model.meta.grammar.KlassParser.IdentifierContext;
import cool.klass.model.meta.grammar.KlassParser.OptionalMarkerContext;
import cool.klass.model.meta.grammar.KlassParser.PrimitivePropertyContext;
import cool.klass.model.meta.grammar.KlassParser.PrimitiveTypeContext;
import cool.klass.model.meta.grammar.KlassParser.PropertyModifierContext;
import org.eclipse.collections.api.list.ImmutableList;
import org.eclipse.collections.impl.list.mutable.ListAdapter;

public class ClassPhase extends AbstractCompilerPhase
{
    public ClassPhase(CompilerState compilerState)
    {
        super(compilerState);
    }

    @Override
    public void enterClassDeclaration(@Nonnull ClassDeclarationContext ctx)
    {
        super.enterClassDeclaration(ctx);

        String classOrUserKeyword = ctx.classOrUser().getText();

        AntlrClass classState = new AntlrClass(
                ctx,
                this.compilerState.getCompilerWalkState().getCurrentCompilationUnit(),
                this.compilerState.getCompilerInputState().isInference(),
                ctx.identifier(),
                ctx.identifier().getText(),
                this.compilerState.getDomainModelState().getNumTopLevelElements() + 1,
                this.compilerState.getAntlrWalkState().getPackageContext(),
                this.compilerState.getCompilerWalkState().getPackageName(),
                classOrUserKeyword.equals("user"));
        this.compilerState.getCompilerWalkState().defineClass(classState);
    }

    @Override
    public void enterClassModifier(@Nonnull ClassModifierContext ctx)
    {
        super.enterClassModifier(ctx);
        int ordinal = this.compilerState.getCompilerWalkState().getClassState().getNumClassModifiers();
        AntlrClassModifier classModifierState = new AntlrClassModifier(
                ctx,
                this.compilerState.getCompilerWalkState().getCurrentCompilationUnit(),
                this.compilerState.getCompilerInputState().isInference(),
                ctx,
                ctx.getText(),
                ordinal + 1,
                this.compilerState.getCompilerWalkState().getClassState());
        this.compilerState.getCompilerWalkState().getClassState().enterClassModifier(classModifierState);
    }

    @Override
    public void enterPrimitiveProperty(@Nonnull PrimitivePropertyContext ctx)
    {
        super.enterPrimitiveProperty(ctx);
        IdentifierContext     identifier            = ctx.identifier();
        PrimitiveTypeContext  primitiveTypeContext  = ctx.primitiveType();
        OptionalMarkerContext optionalMarkerContext = ctx.optionalMarker();

        String  propertyName = identifier.getText();
        boolean isOptional   = optionalMarkerContext != null;

        PrimitiveType      primitiveType      = PrimitiveType.byPrettyName(primitiveTypeContext.getText());
        AntlrPrimitiveType primitiveTypeState = AntlrPrimitiveType.valueOf(primitiveType);

        ImmutableList<AntlrPropertyModifier> propertyModifiers = ListAdapter.adapt(ctx.propertyModifier())
                .collectWithIndex(this::getAntlrPropertyModifier)
                .toImmutable();

        AntlrPrimitiveProperty primitivePropertyState = new AntlrPrimitiveProperty(
                ctx,
                this.compilerState.getCompilerWalkState().getCurrentCompilationUnit(),
                this.compilerState.getCompilerInputState().isInference(),
                identifier,
                propertyName,
                this.compilerState.getCompilerWalkState().getClassState().getNumMembers() + 1,
                this.compilerState.getCompilerWalkState().getClassState(),
                isOptional,
                propertyModifiers,
                primitiveTypeState);

        this.compilerState.getCompilerWalkState().getClassState().enterDataTypeProperty(primitivePropertyState);
    }

    @Override
    public void enterEnumerationProperty(@Nonnull EnumerationPropertyContext ctx)
    {
        super.enterEnumerationProperty(ctx);

        IdentifierContext           identifier                  = ctx.identifier();
        EnumerationReferenceContext enumerationReferenceContext = ctx.enumerationReference();
        OptionalMarkerContext       optionalMarkerContext       = ctx.optionalMarker();

        String           propertyName     = identifier.getText();
        AntlrEnumeration enumerationState = this.compilerState.getDomainModelState().getEnumerationByName(enumerationReferenceContext.getText());
        boolean          isOptional       = optionalMarkerContext != null;

        // TODO: Superclass above all modifiers. Modifiers hold their owners.
        ImmutableList<AntlrPropertyModifier> propertyModifiers = ListAdapter.adapt(ctx.propertyModifier())
                .collectWithIndex(this::getAntlrPropertyModifier)
                .toImmutable();

        AntlrEnumerationProperty primitivePropertyState = new AntlrEnumerationProperty(
                ctx,
                this.compilerState.getCompilerWalkState().getCurrentCompilationUnit(),
                this.compilerState.getCompilerInputState().isInference(),
                identifier,
                propertyName,
                this.compilerState.getCompilerWalkState().getClassState().getNumMembers() + 1,
                this.compilerState.getCompilerWalkState().getClassState(),
                isOptional,
                propertyModifiers,
                enumerationState);

        this.compilerState.getCompilerWalkState().getClassState().enterDataTypeProperty(primitivePropertyState);
    }

    @Nonnull
    public AntlrPropertyModifier getAntlrPropertyModifier(@Nonnull PropertyModifierContext context, int ordinal)
    {
        return new AntlrPropertyModifier(
                context,
                this.compilerState.getCompilerWalkState().getCurrentCompilationUnit(),
                this.compilerState.getCompilerInputState().isInference(),
                context,
                context.getText(),
                ordinal + 1);
    }
}
