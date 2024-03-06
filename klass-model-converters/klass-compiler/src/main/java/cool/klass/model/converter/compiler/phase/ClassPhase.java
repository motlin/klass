package cool.klass.model.converter.compiler.phase;

import javax.annotation.Nonnull;

import cool.klass.model.converter.compiler.CompilationUnit;
import cool.klass.model.converter.compiler.error.CompilerErrorHolder;
import cool.klass.model.converter.compiler.state.AntlrClass;
import cool.klass.model.converter.compiler.state.AntlrClassModifier;
import cool.klass.model.converter.compiler.state.AntlrDomainModel;
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
import org.antlr.v4.runtime.ParserRuleContext;
import org.eclipse.collections.api.list.ImmutableList;
import org.eclipse.collections.api.map.MutableMap;
import org.eclipse.collections.impl.list.mutable.ListAdapter;

public class ClassPhase extends AbstractDomainModelCompilerPhase
{
    public ClassPhase(
            @Nonnull CompilerErrorHolder compilerErrorHolder,
            @Nonnull MutableMap<ParserRuleContext, CompilationUnit> compilationUnitsByContext,
            AntlrDomainModel domainModelState,
            boolean isInference)
    {
        super(compilerErrorHolder, compilationUnitsByContext, isInference, domainModelState);
    }

    @Override
    public void enterClassDeclaration(@Nonnull ClassDeclarationContext ctx)
    {
        super.enterClassDeclaration(ctx);

        String classOrUserKeyword = ctx.classOrUser().getText();

        this.classState = new AntlrClass(
                ctx,
                this.currentCompilationUnit,
                this.isInference,
                ctx.identifier(),
                ctx.identifier().getText(),
                this.domainModelState.getNumTopLevelElements() + 1,
                this.packageContext,
                this.packageName,
                classOrUserKeyword.equals("user"));
    }

    @Override
    public void exitClassDeclaration(ClassDeclarationContext ctx)
    {
        this.domainModelState.exitClassDeclaration(this.classState);
        super.exitClassDeclaration(ctx);
    }

    @Override
    public void enterClassModifier(@Nonnull ClassModifierContext ctx)
    {
        int ordinal = this.classState.getNumClassModifiers();
        AntlrClassModifier classModifierState = new AntlrClassModifier(
                ctx,
                this.currentCompilationUnit,
                this.isInference,
                ctx,
                ctx.getText(),
                ordinal + 1,
                this.classState);
        this.classState.enterClassModifier(classModifierState);
    }

    @Override
    public void enterPrimitiveProperty(@Nonnull PrimitivePropertyContext ctx)
    {
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
                this.currentCompilationUnit,
                this.isInference,
                identifier,
                propertyName,
                this.classState.getNumMembers() + 1,
                this.classState,
                isOptional,
                propertyModifiers,
                primitiveTypeState);

        this.classState.enterDataTypeProperty(primitivePropertyState);
    }

    @Override
    public void enterEnumerationProperty(@Nonnull EnumerationPropertyContext ctx)
    {
        IdentifierContext           identifier                  = ctx.identifier();
        EnumerationReferenceContext enumerationReferenceContext = ctx.enumerationReference();
        OptionalMarkerContext       optionalMarkerContext       = ctx.optionalMarker();

        String           propertyName     = identifier.getText();
        AntlrEnumeration enumerationState = this.domainModelState.getEnumerationByName(enumerationReferenceContext.getText());
        boolean          isOptional       = optionalMarkerContext != null;

        // TODO: Superclass above all modifiers. Modifiers hold their owners.
        ImmutableList<AntlrPropertyModifier> propertyModifiers = ListAdapter.adapt(ctx.propertyModifier())
                .collectWithIndex(this::getAntlrPropertyModifier)
                .toImmutable();

        AntlrEnumerationProperty primitivePropertyState = new AntlrEnumerationProperty(
                ctx,
                this.currentCompilationUnit,
                this.isInference,
                identifier,
                propertyName,
                this.classState.getNumMembers() + 1,
                this.classState, isOptional,
                propertyModifiers,
                enumerationState);

        this.classState.enterDataTypeProperty(primitivePropertyState);
    }

    @Nonnull
    public AntlrPropertyModifier getAntlrPropertyModifier(@Nonnull PropertyModifierContext context, int ordinal)
    {
        return new AntlrPropertyModifier(
                context,
                this.currentCompilationUnit,
                this.isInference,
                context,
                context.getText(),
                ordinal + 1);
    }
}
