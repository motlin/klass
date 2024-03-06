package cool.klass.model.converter.compiler.phase;

import java.util.IdentityHashMap;
import java.util.Map;
import java.util.Objects;

import cool.klass.model.converter.compiler.CompilationUnit;
import cool.klass.model.converter.compiler.error.CompilerErrorHolder;
import cool.klass.model.meta.domain.DataTypeProperty.DataTypePropertyBuilder;
import cool.klass.model.meta.domain.DomainModel.DomainModelBuilder;
import cool.klass.model.meta.domain.Enumeration.EnumerationBuilder;
import cool.klass.model.meta.domain.EnumerationProperty.EnumerationPropertyBuilder;
import cool.klass.model.meta.domain.Klass.KlassBuilder;
import cool.klass.model.meta.domain.PrimitiveProperty.PrimitivePropertyBuilder;
import cool.klass.model.meta.grammar.KlassParser.ClassDeclarationContext;
import cool.klass.model.meta.grammar.KlassParser.CompilationUnitContext;
import cool.klass.model.meta.grammar.KlassParser.EnumerationDeclarationContext;
import cool.klass.model.meta.grammar.KlassParser.EnumerationPropertyContext;
import cool.klass.model.meta.grammar.KlassParser.EnumerationReferenceContext;
import cool.klass.model.meta.grammar.KlassParser.EscapedIdentifierContext;
import cool.klass.model.meta.grammar.KlassParser.IdentifierContext;
import cool.klass.model.meta.grammar.KlassParser.OptionalMarkerContext;
import cool.klass.model.meta.grammar.KlassParser.PackageDeclarationContext;
import cool.klass.model.meta.grammar.KlassParser.PackageNameContext;
import cool.klass.model.meta.grammar.KlassParser.PrimitivePropertyContext;
import cool.klass.model.meta.grammar.KlassParser.PrimitiveTypeContext;
import cool.klass.model.meta.grammar.KlassParser.PropertyModifierContext;
import org.eclipse.collections.api.map.MapIterable;

// TODO: Split out the check for duplicate modifier names
public class CompilerPhaseEnumerationsAndClasses extends AbstractCompilerPhase
{
    private final DomainModelBuilder domainModelBuilder;
    private final ResolveTypesPhase  resolveTypesPhase;

    private final Map<EnumerationDeclarationContext, EnumerationBuilder>      enumerationBuilders         = new IdentityHashMap<>();
    private final Map<ClassDeclarationContext, KlassBuilder>                  klassBuilders               = new IdentityHashMap<>();
    private final Map<PrimitivePropertyContext, PrimitivePropertyBuilder>     primitivePropertyBuilders   =
            new IdentityHashMap<>();
    private final Map<EnumerationPropertyContext, EnumerationPropertyBuilder> enumerationPropertyBuilders =
            new IdentityHashMap<>();

    private String packageName;

    private KlassBuilder               klassBuilder;
    private EnumerationBuilder         enumerationBuilder;
    private DataTypePropertyBuilder    dataTypePropertyBuilder;
    private PrimitivePropertyBuilder   primitivePropertyBuilder;
    private EnumerationPropertyBuilder enumerationPropertyBuilder;

    public CompilerPhaseEnumerationsAndClasses(
            MapIterable<CompilationUnitContext, CompilationUnit> compilationUnitsByContext,
            CompilerErrorHolder compilerErrorHolder,
            DomainModelBuilder domainModelBuilder,
            ResolveTypesPhase resolveTypesPhase)
    {
        super(compilerErrorHolder, compilationUnitsByContext);
        this.domainModelBuilder = Objects.requireNonNull(domainModelBuilder);
        this.resolveTypesPhase = Objects.requireNonNull(resolveTypesPhase);
    }

    @Override
    public void enterPackageDeclaration(PackageDeclarationContext ctx)
    {
        PackageNameContext packageNameContext = ctx.packageName();
        this.packageName = packageNameContext.getText();
    }

    @Override
    public void enterClassDeclaration(ClassDeclarationContext ctx)
    {
        IdentifierContext identifier = ctx.identifier();
        this.klassBuilder = new KlassBuilder(ctx, identifier, this.packageName);
        this.klassBuilders.put(ctx, this.klassBuilder);
    }

    @Override
    public void exitClassDeclaration(ClassDeclarationContext ctx)
    {
        this.domainModelBuilder.klass(this.klassBuilder);
        this.klassBuilder = null;
    }

    @Override
    public void enterEnumerationDeclaration(EnumerationDeclarationContext ctx)
    {
        IdentifierContext identifier = ctx.identifier();
        this.enumerationBuilder = new EnumerationBuilder(ctx, identifier, this.packageName);
        this.enumerationBuilders.put(ctx, this.enumerationBuilder);
    }

    @Override
    public void exitEnumerationDeclaration(EnumerationDeclarationContext ctx)
    {
        this.domainModelBuilder.enumeration(this.enumerationBuilder);
        this.enumerationBuilder = null;
    }

    @Override
    public void enterPrimitiveProperty(PrimitivePropertyContext ctx)
    {
        EscapedIdentifierContext escapedIdentifierContext = ctx.escapedIdentifier();

        OptionalMarkerContext optionalMarkerContext = ctx.optionalMarker();
        PrimitiveTypeContext  primitiveTypeContext  = ctx.primitiveType();

        boolean isOptional = optionalMarkerContext != null;

        this.primitivePropertyBuilder =
                new PrimitivePropertyBuilder(ctx, escapedIdentifierContext, primitiveTypeContext, isOptional);
        this.dataTypePropertyBuilder = this.primitivePropertyBuilder;
        this.primitivePropertyBuilders.put(ctx, this.primitivePropertyBuilder);
    }

    @Override
    public void exitPrimitiveProperty(PrimitivePropertyContext ctx)
    {
        this.klassBuilder.primitiveProperty(this.primitivePropertyBuilder);
        this.dataTypePropertyBuilder = null;
        this.primitivePropertyBuilder = null;
    }

    @Override
    public void enterEnumerationProperty(EnumerationPropertyContext ctx)
    {
        IdentifierContext           identifier                  = ctx.escapedIdentifier().identifier();
        OptionalMarkerContext       optionalMarkerContext       = ctx.optionalMarker();
        EnumerationReferenceContext enumerationReferenceContext = ctx.enumerationReference();

        boolean isOptional = optionalMarkerContext != null;

        this.enumerationPropertyBuilder =
                new EnumerationPropertyBuilder(ctx, identifier, enumerationReferenceContext, isOptional);
        this.dataTypePropertyBuilder = this.enumerationPropertyBuilder;
        this.enumerationPropertyBuilders.put(ctx, this.enumerationPropertyBuilder);
    }

    @Override
    public void exitEnumerationProperty(EnumerationPropertyContext ctx)
    {
        this.klassBuilder.enumerationProperty(this.enumerationPropertyBuilder);
        this.dataTypePropertyBuilder = null;
        this.enumerationPropertyBuilder = null;
    }

    @Override
    public void enterPropertyModifier(PropertyModifierContext ctx)
    {
        String  text  = ctx.getText();
        boolean added = this.dataTypePropertyBuilder.addModifier(text);
        if (!added)
        {
            this.emitErrorDuplicateModifier(text, ctx);
        }
    }

    protected void emitErrorDuplicateModifier(String text, PropertyModifierContext ctx)
    {
        String message = String.format("Duplicate modifier '%s'.", text);
        this.error(
                message,
                ctx,
                this.primitivePropertyBuilder.getElementContext(),
                this.klassBuilder.getElementContext());
    }
}
