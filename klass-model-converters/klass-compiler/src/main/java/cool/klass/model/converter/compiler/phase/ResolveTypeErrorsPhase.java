package cool.klass.model.converter.compiler.phase;

import javax.annotation.Nonnull;

import cool.klass.model.converter.compiler.CompilationUnit;
import cool.klass.model.converter.compiler.error.CompilerErrorHolder;
import cool.klass.model.meta.grammar.KlassParser.AssociationEndContext;
import cool.klass.model.meta.grammar.KlassParser.ClassDeclarationContext;
import cool.klass.model.meta.grammar.KlassParser.ClassReferenceContext;
import cool.klass.model.meta.grammar.KlassParser.ClassServiceModifierContext;
import cool.klass.model.meta.grammar.KlassParser.EnumerationDeclarationContext;
import cool.klass.model.meta.grammar.KlassParser.EnumerationParameterDeclarationContext;
import cool.klass.model.meta.grammar.KlassParser.EnumerationPropertyContext;
import cool.klass.model.meta.grammar.KlassParser.EnumerationReferenceContext;
import cool.klass.model.meta.grammar.KlassParser.ParameterizedPropertyContext;
import cool.klass.model.meta.grammar.KlassParser.ProjectionDeclarationContext;
import cool.klass.model.meta.grammar.KlassParser.ProjectionReferenceContext;
import cool.klass.model.meta.grammar.KlassParser.ServiceGroupDeclarationContext;
import cool.klass.model.meta.grammar.KlassParser.ServiceProjectionDispatchContext;
import cool.klass.model.meta.grammar.KlassParser.VersionsContext;
import org.antlr.v4.runtime.ParserRuleContext;
import org.eclipse.collections.api.map.MutableMap;

public class ResolveTypeErrorsPhase extends AbstractCompilerPhase
{
    private final ResolveTypesPhase resolveTypesPhase;

    public ResolveTypeErrorsPhase(
            @Nonnull CompilerErrorHolder compilerErrorHolder,
            @Nonnull MutableMap<ParserRuleContext, CompilationUnit> compilationUnitsByContext,
            ResolveTypesPhase resolveTypesPhase)
    {
        super(compilerErrorHolder, compilationUnitsByContext);
        this.resolveTypesPhase = resolveTypesPhase;
    }

    @Override
    public void enterClassServiceModifier(@Nonnull ClassServiceModifierContext ctx)
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
                    this.currentCompilationUnit.getParserContext());
        }
    }

    @Override
    public void enterAssociationEnd(@Nonnull AssociationEndContext ctx)
    {
        ClassDeclarationContext declaration = this.resolveTypesPhase.getType(ctx);
        if (declaration == DeclarationsByNamePhase.NO_SUCH_CLASS)
        {
            ClassReferenceContext reference = ctx.classType().classReference();
            /*
            this.error(
                    String.format("Cannot find class '%s'", reference.getText()),
                    reference,
                    this.associationDeclarationContext,
                    this.currentCompilationUnit.getCompilationUnitContext());
            */
        }
    }

    @Override
    public void enterServiceProjectionDispatch(@Nonnull ServiceProjectionDispatchContext ctx)
    {
        ProjectionDeclarationContext declaration = this.resolveTypesPhase.getType(ctx);
        if (declaration == DeclarationsByNamePhase.NO_SUCH_PROJECTION)
        {
            ProjectionReferenceContext reference = ctx.projectionReference();
            this.error(
                    String.format("Cannot find projection '%s'", reference.getText()),
                    reference,
                    ctx,
                    this.serviceDeclarationContext,
                    this.urlDeclarationContext,
                    this.serviceGroupDeclarationContext,
                    this.currentCompilationUnit.getParserContext());
        }
    }

    @Override
    public void enterEnumerationProperty(@Nonnull EnumerationPropertyContext ctx)
    {
        EnumerationDeclarationContext declaration = this.resolveTypesPhase.getType(ctx);
        if (declaration == DeclarationsByNamePhase.NO_SUCH_ENUMERATION)
        {
            EnumerationReferenceContext reference = ctx.enumerationReference();
            this.error(
                    String.format("Cannot find enumeration '%s'", reference.getText()),
                    reference,
                    this.classDeclarationContext,
                    this.currentCompilationUnit.getParserContext());
        }
    }

    @Override
    public void enterEnumerationParameterDeclaration(@Nonnull EnumerationParameterDeclarationContext ctx)
    {
        EnumerationDeclarationContext declaration = this.resolveTypesPhase.getType(ctx);
        if (declaration == DeclarationsByNamePhase.NO_SUCH_ENUMERATION)
        {
            // TODO: Many different error contexts possible for ParameterDeclarations
            EnumerationReferenceContext enumerationReferenceContext = ctx.enumerationReference();
            this.error(
                    String.format("Cannot find enumeration '%s'", enumerationReferenceContext.getText()),
                    enumerationReferenceContext,
                    ctx,
                    this.classDeclarationContext,
                    this.currentCompilationUnit.getParserContext());
        }
    }

    @Override
    public void enterVersions(VersionsContext ctx)
    {
        ClassDeclarationContext declaration = this.resolveTypesPhase.getType(ctx);
        if (declaration == DeclarationsByNamePhase.NO_SUCH_CLASS)
        {
            ClassReferenceContext reference = ctx.classReference();
            this.error(
                    String.format("Cannot find class '%s'", reference.getText()),
                    reference,
                    this.currentCompilationUnit.getParserContext());
        }
    }

    @Override
    public void enterProjectionDeclaration(@Nonnull ProjectionDeclarationContext ctx)
    {
        ClassDeclarationContext declaration = this.resolveTypesPhase.getType(ctx);
        if (declaration == DeclarationsByNamePhase.NO_SUCH_CLASS)
        {
            ClassReferenceContext reference = ctx.classReference();
            this.error(
                    String.format("Cannot find class '%s'", reference.getText()),
                    reference,
                    this.currentCompilationUnit.getParserContext());
        }
    }

    @Override
    public void enterServiceGroupDeclaration(@Nonnull ServiceGroupDeclarationContext ctx)
    {
        super.enterServiceGroupDeclaration(ctx);

        ClassDeclarationContext declaration = this.resolveTypesPhase.getType(ctx);
        if (declaration == DeclarationsByNamePhase.NO_SUCH_CLASS)
        {
            ClassReferenceContext reference = ctx.classReference();
            this.error(
                    String.format("Cannot find class '%s'", reference.getText()),
                    reference,
                    this.currentCompilationUnit.getParserContext());
        }
    }

    @Override
    public void enterParameterizedProperty(@Nonnull ParameterizedPropertyContext ctx)
    {
        ClassDeclarationContext declaration = this.resolveTypesPhase.getType(ctx);
        if (declaration == DeclarationsByNamePhase.NO_SUCH_CLASS)
        {
            ClassReferenceContext reference = ctx.classType().classReference();
            this.error(
                    String.format("Cannot find class '%s'", reference.getText()),
                    reference,
                    this.classDeclarationContext,
                    this.currentCompilationUnit.getParserContext());
        }
    }
}
