package cool.klass.model.converter.compiler.phase;

import javax.annotation.Nonnull;

import cool.klass.model.converter.compiler.CompilerState;
import cool.klass.model.converter.compiler.state.AntlrClass;
import cool.klass.model.converter.compiler.state.AntlrClassModifier;
import cool.klass.model.converter.compiler.state.AntlrClassifier;
import cool.klass.model.converter.compiler.state.AntlrDomainModel;
import cool.klass.model.converter.compiler.state.AntlrEnumeration;
import cool.klass.model.converter.compiler.state.AntlrInterface;
import cool.klass.model.converter.compiler.state.AntlrPrimitiveType;
import cool.klass.model.converter.compiler.state.property.AntlrEnumerationProperty;
import cool.klass.model.converter.compiler.state.property.AntlrPrimitiveProperty;
import cool.klass.model.converter.compiler.state.property.AntlrPropertyModifier;
import cool.klass.model.meta.domain.api.InheritanceType;
import cool.klass.model.meta.domain.api.PrimitiveType;
import cool.klass.model.meta.grammar.KlassParser.AbstractDeclarationContext;
import cool.klass.model.meta.grammar.KlassParser.ClassDeclarationContext;
import cool.klass.model.meta.grammar.KlassParser.ClassModifierContext;
import cool.klass.model.meta.grammar.KlassParser.EnumerationPropertyContext;
import cool.klass.model.meta.grammar.KlassParser.InheritanceTypeContext;
import cool.klass.model.meta.grammar.KlassParser.InterfaceDeclarationContext;
import cool.klass.model.meta.grammar.KlassParser.PrimitivePropertyContext;
import cool.klass.model.meta.grammar.KlassParser.PropertyModifierContext;
import org.eclipse.collections.api.list.ImmutableList;
import org.eclipse.collections.impl.list.mutable.ListAdapter;

public class ClassifierPhase extends AbstractCompilerPhase
{
    public ClassifierPhase(CompilerState compilerState)
    {
        super(compilerState);
    }

    @Override
    public void enterInterfaceDeclaration(InterfaceDeclarationContext ctx)
    {
        super.enterInterfaceDeclaration(ctx);

        AntlrInterface interfaceState = new AntlrInterface(
                ctx,
                this.compilerState.getCompilerWalkState().getCurrentCompilationUnit(),
                this.compilerState.getCompilerInputState().isInference(),
                ctx.identifier(),
                ctx.identifier().getText(),
                this.compilerState.getDomainModelState().getNumTopLevelElements() + 1,
                this.compilerState.getAntlrWalkState().getPackageContext(),
                this.compilerState.getCompilerWalkState().getPackageName());

        this.compilerState.getCompilerWalkState().defineInterface(interfaceState);
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
    public void enterAbstractDeclaration(AbstractDeclarationContext ctx)
    {
        super.enterAbstractDeclaration(ctx);

        AntlrClass classState = this.compilerState.getCompilerWalkState().getClassState();
        // TODO: InheritanceType
        classState.setInheritanceType(InheritanceType.TABLE_PER_SUBCLASS);
    }

    @Override
    public void enterInheritanceType(InheritanceTypeContext ctx)
    {
        super.enterInheritanceType(ctx);

        AntlrClass      classState      = this.compilerState.getCompilerWalkState().getClassState();
        InheritanceType inheritanceType = InheritanceType.byPrettyName(ctx.getText());
        // TODO: InheritanceType
        classState.setInheritanceType(inheritanceType);
    }

    @Override
    public void enterClassModifier(@Nonnull ClassModifierContext ctx)
    {
        super.enterClassModifier(ctx);

        AntlrClassifier classifierState = this.compilerState.getCompilerWalkState().getClassifierState();
        int             ordinal         = classifierState.getNumClassModifiers();

        AntlrClassModifier classModifierState = new AntlrClassModifier(
                ctx,
                this.compilerState.getCompilerWalkState().getCurrentCompilationUnit(),
                this.compilerState.getCompilerInputState().isInference(),
                ctx,
                ctx.getText(),
                ordinal + 1,
                classifierState);

        classifierState.enterClassModifier(classModifierState);
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

        AntlrClassifier classifierState = this.compilerState.getCompilerWalkState().getClassifierState();

        AntlrPrimitiveProperty primitivePropertyState = new AntlrPrimitiveProperty(
                ctx,
                this.compilerState.getCompilerWalkState().getCurrentCompilationUnit(),
                this.compilerState.getCompilerInputState().isInference(),
                ctx.identifier(),
                propertyName,
                classifierState.getNumMembers() + 1,
                classifierState,
                isOptional,
                propertyModifiers,
                primitiveTypeState);

        classifierState.enterDataTypeProperty(primitivePropertyState);
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
        AntlrClassifier  classifierState  = this.compilerState.getCompilerWalkState().getClassifierState();

        AntlrEnumerationProperty primitivePropertyState = new AntlrEnumerationProperty(
                ctx,
                this.compilerState.getCompilerWalkState().getCurrentCompilationUnit(),
                this.compilerState.getCompilerInputState().isInference(),
                ctx.identifier(),
                propertyName,
                classifierState.getNumMembers() + 1,
                classifierState,
                isOptional,
                propertyModifiers,
                enumerationState);

        classifierState.enterDataTypeProperty(primitivePropertyState);
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
