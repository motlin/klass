package cool.klass.model.converter.compiler.phase;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

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
import cool.klass.model.meta.domain.property.PrimitiveType;
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

public class ClassPhase extends AbstractCompilerPhase
{
    private final AntlrDomainModel domainModelState;

    @Nullable
    private AntlrClass classState;

    public ClassPhase(
            @Nonnull CompilerErrorHolder compilerErrorHolder,
            @Nonnull MutableMap<ParserRuleContext, CompilationUnit> compilationUnitsByContext,
            AntlrDomainModel domainModelState)
    {
        super(compilerErrorHolder, compilationUnitsByContext);
        this.domainModelState = domainModelState;
    }

    @Override
    public void enterClassDeclaration(@Nonnull ClassDeclarationContext ctx)
    {
        ImmutableList<AntlrClassModifier> classModifiers = ListAdapter.adapt(ctx.classModifier())
                .collect(this::getAntlrClassModifier)
                .toImmutable();

        String classOrUserKeyword = ctx.classOrUser().getText();

        AntlrClass classState = new AntlrClass(
                ctx,
                this.currentCompilationUnit,
                false,
                ctx.identifier(),
                ctx.identifier().getText(),
                this.domainModelState.getNumTopLevelElements() + 1,
                this.packageName,
                classModifiers,
                classOrUserKeyword.equals("user"));

        this.classState = classState;
    }

    @Override
    public void exitClassDeclaration(ClassDeclarationContext ctx)
    {
        this.domainModelState.exitClassDeclaration(this.classState);
        this.classState = null;
    }

    @Override
    public void enterPrimitiveProperty(@Nonnull PrimitivePropertyContext ctx)
    {
        IdentifierContext     identifier            = ctx.identifier();
        PrimitiveTypeContext  primitiveTypeContext  = ctx.primitiveType();
        OptionalMarkerContext optionalMarkerContext = ctx.optionalMarker();

        String  propertyName = identifier.getText();
        boolean isOptional   = optionalMarkerContext != null;

        PrimitiveType      primitiveType      = PrimitiveType.valueOf(primitiveTypeContext.getText());
        AntlrPrimitiveType primitiveTypeState = AntlrPrimitiveType.valueOf(primitiveType);

        ImmutableList<AntlrPropertyModifier> propertyModifiers = ListAdapter.adapt(ctx.propertyModifier())
                .collect(this::getAntlrPropertyModifier)
                .toImmutable();

        AntlrPrimitiveProperty primitivePropertyState = new AntlrPrimitiveProperty(
                ctx,
                this.currentCompilationUnit,
                false,
                identifier,
                propertyName,
                this.classState.getNumMembers() + 1,
                isOptional,
                propertyModifiers,
                this.classState,
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
        AntlrEnumeration antlrEnumeration = this.domainModelState.getEnumerationByName(enumerationReferenceContext.getText());
        boolean          isOptional       = optionalMarkerContext != null;

        ImmutableList<AntlrPropertyModifier> propertyModifiers = ListAdapter.adapt(ctx.propertyModifier())
                .collect(this::getAntlrPropertyModifier)
                .toImmutable();

        AntlrEnumerationProperty primitivePropertyState = new AntlrEnumerationProperty(
                ctx,
                this.currentCompilationUnit,
                false,
                identifier,
                propertyName,
                this.classState.getNumMembers() + 1,
                isOptional,
                propertyModifiers,
                this.classState,
                antlrEnumeration);

        this.classState.enterDataTypeProperty(primitivePropertyState);
    }

    @Nonnull
    public AntlrClassModifier getAntlrClassModifier(@Nonnull ClassModifierContext context)
    {
        return new AntlrClassModifier(context, this.currentCompilationUnit, false, context.getText());
    }

    @Nonnull
    public AntlrPropertyModifier getAntlrPropertyModifier(@Nonnull PropertyModifierContext context)
    {
        return new AntlrPropertyModifier(context, this.currentCompilationUnit, false, context.getText());
    }
}
