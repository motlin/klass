package cool.klass.model.converter.compiler.phase;

import cool.klass.model.converter.compiler.CompilationUnit;
import cool.klass.model.converter.compiler.error.CompilerErrorHolder;
import cool.klass.model.meta.grammar.KlassParser.AssociationEndContext;
import cool.klass.model.meta.grammar.KlassParser.ClassDeclarationContext;
import cool.klass.model.meta.grammar.KlassParser.ClassReferenceContext;
import cool.klass.model.meta.grammar.KlassParser.ClassServiceModifierContext;
import cool.klass.model.meta.grammar.KlassParser.CompilationUnitContext;
import cool.klass.model.meta.grammar.KlassParser.DataTypeContext;
import cool.klass.model.meta.grammar.KlassParser.EnumerationDeclarationContext;
import cool.klass.model.meta.grammar.KlassParser.EnumerationPropertyContext;
import cool.klass.model.meta.grammar.KlassParser.EnumerationReferenceContext;
import cool.klass.model.meta.grammar.KlassParser.ParameterDeclarationContext;
import cool.klass.model.meta.grammar.KlassParser.ParameterizedPropertyContext;
import cool.klass.model.meta.grammar.KlassParser.ProjectionDeclarationContext;
import cool.klass.model.meta.grammar.KlassParser.ProjectionReferenceContext;
import cool.klass.model.meta.grammar.KlassParser.ServiceGroupDeclarationContext;
import cool.klass.model.meta.grammar.KlassParser.ServiceProjectionDispatchContext;
import org.eclipse.collections.api.map.MapIterable;

public class ResolveTypeErrorsPhase extends AbstractCompilerPhase
{
    private final ResolveTypesPhase resolveTypesPhase;

    public ResolveTypeErrorsPhase(
            CompilerErrorHolder compilerErrorHolder,
            MapIterable<CompilationUnitContext, CompilationUnit> compilationUnitsByContext,
            ResolveTypesPhase resolveTypesPhase)
    {
        super(compilerErrorHolder, compilationUnitsByContext);
        this.resolveTypesPhase = resolveTypesPhase;
    }

    @Override
    public void enterClassServiceModifier(ClassServiceModifierContext ctx)
    {
        ProjectionDeclarationContext declaration = this.resolveTypesPhase.getType(ctx);
        if (declaration == DeclarationsByNamePhase.NO_SUCH_PROJECTION)
        {
            ProjectionReferenceContext reference = ctx.projectionReference();
            this.error(
                    String.format("Cannot find projection '%s'", reference.getText()),
                    reference,
                    ctx,
                    this.classDeclarationContext,
                    this.currentCompilationUnit.getCompilationUnitContext());
        }
    }

    @Override
    public void enterAssociationEnd(AssociationEndContext ctx)
    {
        ClassDeclarationContext declaration = this.resolveTypesPhase.getType(ctx);
        if (declaration == DeclarationsByNamePhase.NO_SUCH_CLASS)
        {
            ClassReferenceContext reference = ctx.classType().classReference();
            this.error(
                    String.format("Cannot find class '%s'", reference.getText()),
                    reference,
                    ctx,
                    this.associationDeclarationContext,
                    this.currentCompilationUnit.getCompilationUnitContext());
        }
    }

    @Override
    public void enterServiceProjectionDispatch(ServiceProjectionDispatchContext ctx)
    {
        ProjectionDeclarationContext declaration = this.resolveTypesPhase.getType(ctx);
        if (declaration == DeclarationsByNamePhase.NO_SUCH_PROJECTION)
        {
            ProjectionReferenceContext reference = ctx.projectionReference();
            this.error(
                    String.format("Cannot find projection '%s'", reference.getText()),
                    reference,
                    ctx,
                    this.serviceGroupDeclarationContext,
                    this.currentCompilationUnit.getCompilationUnitContext());
        }
    }

    @Override
    public void enterEnumerationProperty(EnumerationPropertyContext ctx)
    {
        EnumerationDeclarationContext declaration = this.resolveTypesPhase.getType(ctx);
        if (declaration == DeclarationsByNamePhase.NO_SUCH_ENUMERATION)
        {
            EnumerationReferenceContext reference = ctx.enumerationReference();
            this.error(
                    String.format("Cannot find enumeration '%s'", reference.getText()),
                    reference,
                    ctx,
                    this.classDeclarationContext,
                    this.currentCompilationUnit.getCompilationUnitContext());
        }
    }

    @Override
    public void enterParameterDeclaration(ParameterDeclarationContext ctx)
    {
        EnumerationDeclarationContext declaration = this.resolveTypesPhase.getType(ctx);
        if (declaration == DeclarationsByNamePhase.NO_SUCH_ENUMERATION)
        {
            // TODO: Many different error contexts possible for ParameterDeclarations
            DataTypeContext dataTypeContext = ctx.dataTypeDeclaration().dataType();
            this.error(
                    String.format("Cannot find enumeration '%s'", dataTypeContext.getText()),
                    dataTypeContext,
                    ctx,
                    this.classDeclarationContext,
                    this.currentCompilationUnit.getCompilationUnitContext());
        }
    }

    @Override
    public void enterProjectionDeclaration(ProjectionDeclarationContext ctx)
    {
        ClassDeclarationContext declaration = this.resolveTypesPhase.getType(ctx);
        if (declaration == DeclarationsByNamePhase.NO_SUCH_CLASS)
        {
            ClassReferenceContext reference = ctx.classReference();
            this.error(
                    String.format("Cannot find class '%s'", reference.getText()),
                    reference,
                    ctx,
                    this.currentCompilationUnit.getCompilationUnitContext());
        }
    }

    @Override
    public void enterServiceGroupDeclaration(ServiceGroupDeclarationContext ctx)
    {
        super.enterServiceGroupDeclaration(ctx);

        ClassDeclarationContext declaration = this.resolveTypesPhase.getType(ctx);
        if (declaration == DeclarationsByNamePhase.NO_SUCH_CLASS)
        {
            ClassReferenceContext reference = ctx.classReference();
            this.error(
                    String.format("Cannot find class '%s'", reference.getText()),
                    reference,
                    ctx,
                    this.currentCompilationUnit.getCompilationUnitContext());
        }
    }

    @Override
    public void enterParameterizedProperty(ParameterizedPropertyContext ctx)
    {
        ClassDeclarationContext declaration = this.resolveTypesPhase.getType(ctx);
        if (declaration == DeclarationsByNamePhase.NO_SUCH_CLASS)
        {
            ClassReferenceContext reference = ctx.classType().classReference();
            this.error(
                    String.format("Cannot find class '%s'", reference.getText()),
                    reference,
                    ctx,
                    this.classDeclarationContext,
                    this.currentCompilationUnit.getCompilationUnitContext());
        }
    }
}
