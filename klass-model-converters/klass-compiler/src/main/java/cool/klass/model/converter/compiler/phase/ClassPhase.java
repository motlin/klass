package cool.klass.model.converter.compiler.phase;

import cool.klass.model.converter.compiler.CompilationUnit;
import cool.klass.model.converter.compiler.EscapedIdentifierVisitor;
import cool.klass.model.converter.compiler.error.CompilerErrorHolder;
import cool.klass.model.converter.compiler.state.AntlrClass;
import cool.klass.model.converter.compiler.state.AntlrDomainModel;
import cool.klass.model.converter.compiler.state.AntlrEnumeration;
import cool.klass.model.converter.compiler.state.AntlrEnumerationProperty;
import cool.klass.model.converter.compiler.state.AntlrPrimitiveProperty;
import cool.klass.model.converter.compiler.state.AntlrPropertyModifier;
import cool.klass.model.meta.domain.PrimitiveType;
import cool.klass.model.meta.domain.PrimitiveType.PrimitiveTypeBuilder;
import cool.klass.model.meta.grammar.KlassParser.ClassDeclarationContext;
import cool.klass.model.meta.grammar.KlassParser.CompilationUnitContext;
import cool.klass.model.meta.grammar.KlassParser.EnumerationPropertyContext;
import cool.klass.model.meta.grammar.KlassParser.EnumerationReferenceContext;
import cool.klass.model.meta.grammar.KlassParser.EscapedIdentifierContext;
import cool.klass.model.meta.grammar.KlassParser.OptionalMarkerContext;
import cool.klass.model.meta.grammar.KlassParser.PrimitivePropertyContext;
import cool.klass.model.meta.grammar.KlassParser.PrimitiveTypeContext;
import org.antlr.v4.runtime.ParserRuleContext;
import org.eclipse.collections.api.list.ImmutableList;
import org.eclipse.collections.api.map.MapIterable;
import org.eclipse.collections.impl.list.mutable.ListAdapter;

public class ClassPhase extends AbstractCompilerPhase
{
    private final AntlrDomainModel domainModelState;

    private AntlrClass classState;

    public ClassPhase(
            CompilerErrorHolder compilerErrorHolder,
            MapIterable<CompilationUnitContext, CompilationUnit> compilationUnitsByContext,
            AntlrDomainModel domainModelState)
    {
        super(compilerErrorHolder, compilationUnitsByContext);
        this.domainModelState = domainModelState;
    }

    @Override
    public void enterClassDeclaration(ClassDeclarationContext ctx)
    {
        this.classState = new AntlrClass(
                ctx,
                this.currentCompilationUnit,
                ctx.identifier(),
                ctx.identifier().getText(),
                this.packageName);
        this.domainModelState.enterClassDeclaration(this.classState);
    }

    @Override
    public void exitClassDeclaration(ClassDeclarationContext ctx)
    {
        this.classState = null;
    }

    @Override
    public void enterPrimitiveProperty(PrimitivePropertyContext ctx)
    {
        EscapedIdentifierContext escapedIdentifierContext = ctx.escapedIdentifier();
        PrimitiveTypeContext     primitiveTypeContext     = ctx.primitiveType();
        OptionalMarkerContext    optionalMarkerContext    = ctx.optionalMarker();

        ParserRuleContext    nameContext          = EscapedIdentifierVisitor.getNameContext(escapedIdentifierContext);
        String               propertyName         = nameContext.getText();
        PrimitiveType        primitiveType        = PrimitiveType.valueOf(primitiveTypeContext.getText());
        boolean              isOptional           = optionalMarkerContext != null;
        PrimitiveTypeBuilder primitiveTypeBuilder = new PrimitiveTypeBuilder(primitiveTypeContext, primitiveType);

        ImmutableList<AntlrPropertyModifier> propertyModifiers = ListAdapter.adapt(ctx.propertyModifier())
                .collect(AntlrPropertyModifier::new)
                .toImmutable();

        AntlrPrimitiveProperty primitivePropertyState = new AntlrPrimitiveProperty(
                ctx,
                this.currentCompilationUnit,
                propertyName,
                nameContext,
                isOptional,
                propertyModifiers,
                this.classState,
                primitiveTypeBuilder);

        this.classState.enterDataTypeProperty(primitivePropertyState);
    }

    @Override
    public void enterEnumerationProperty(EnumerationPropertyContext ctx)
    {
        EscapedIdentifierContext    escapedIdentifierContext    = ctx.escapedIdentifier();
        EnumerationReferenceContext enumerationReferenceContext = ctx.enumerationReference();
        OptionalMarkerContext       optionalMarkerContext       = ctx.optionalMarker();

        ParserRuleContext nameContext = EscapedIdentifierVisitor.getNameContext(escapedIdentifierContext);

        String           propertyName     = nameContext.getText();
        AntlrEnumeration antlrEnumeration = this.domainModelState.getEnumerationByName(enumerationReferenceContext.getText());
        boolean          isOptional       = optionalMarkerContext != null;

        ImmutableList<AntlrPropertyModifier> propertyModifiers = ListAdapter.adapt(ctx.propertyModifier())
                .collect(AntlrPropertyModifier::new)
                .toImmutable();

        AntlrEnumerationProperty primitivePropertyState = new AntlrEnumerationProperty(
                ctx,
                this.currentCompilationUnit,
                nameContext,
                propertyName,
                isOptional,
                propertyModifiers,
                this.classState,
                antlrEnumeration);

        this.classState.enterDataTypeProperty(primitivePropertyState);
    }
}
