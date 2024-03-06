package cool.klass.model.converter.compiler.phase;

import javax.annotation.Nonnull;

import cool.klass.model.converter.compiler.CompilationUnit;
import cool.klass.model.converter.compiler.error.CompilerErrorHolder;
import cool.klass.model.converter.compiler.state.AntlrClass;
import cool.klass.model.converter.compiler.state.AntlrDomainModel;
import cool.klass.model.converter.compiler.state.AntlrEnumeration;
import cool.klass.model.converter.compiler.state.projection.AntlrProjection;
import cool.klass.model.meta.grammar.KlassParser.AssociationEndContext;
import cool.klass.model.meta.grammar.KlassParser.ClassReferenceContext;
import cool.klass.model.meta.grammar.KlassParser.EnumerationParameterDeclarationContext;
import cool.klass.model.meta.grammar.KlassParser.EnumerationReferenceContext;
import cool.klass.model.meta.grammar.KlassParser.ProjectionDeclarationContext;
import cool.klass.model.meta.grammar.KlassParser.ProjectionReferenceContext;
import cool.klass.model.meta.grammar.KlassParser.ServiceGroupDeclarationContext;
import cool.klass.model.meta.grammar.KlassParser.ServiceProjectionDispatchContext;
import org.antlr.v4.runtime.ParserRuleContext;
import org.eclipse.collections.api.map.MutableMap;

public class ResolveTypeErrorsPhase extends AbstractDomainModelCompilerPhase
{
    private final ResolveTypesPhase resolveTypesPhase;

    public ResolveTypeErrorsPhase(
            @Nonnull CompilerErrorHolder compilerErrorHolder,
            @Nonnull MutableMap<ParserRuleContext, CompilationUnit> compilationUnitsByContext,
            ResolveTypesPhase resolveTypesPhase,
            @Nonnull AntlrDomainModel domainModelState)
    {
        super(compilerErrorHolder, compilationUnitsByContext, false, domainModelState);
        this.resolveTypesPhase = resolveTypesPhase;
    }

    @Override
    public void enterAssociationEnd(@Nonnull AssociationEndContext ctx)
    {
        AntlrClass classState = this.resolveTypesPhase.getType(ctx);
        if (classState == AntlrClass.NOT_FOUND)
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
        AntlrProjection projectionState = this.resolveTypesPhase.getType(ctx);
        if (projectionState == AntlrProjection.NOT_FOUND)
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
    public void enterEnumerationParameterDeclaration(@Nonnull EnumerationParameterDeclarationContext ctx)
    {
        AntlrEnumeration enumerationState = this.resolveTypesPhase.getType(ctx);
        if (enumerationState == AntlrEnumeration.NOT_FOUND)
        {
            // TODO: Many different error contexts possible for ParameterDeclarations
            EnumerationReferenceContext enumerationReferenceContext = ctx.enumerationReference();
            this.error(
                    String.format("Cannot find enumeration '%s'", enumerationReferenceContext.getText()),
                    enumerationReferenceContext,
                    this.currentCompilationUnit.getParserContext());
        }
    }

    @Override
    public void enterProjectionDeclaration(@Nonnull ProjectionDeclarationContext ctx)
    {
        AntlrClass classState = this.resolveTypesPhase.getType(ctx);
        if (classState == AntlrClass.NOT_FOUND)
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

        AntlrClass classState = this.resolveTypesPhase.getType(ctx);
        if (classState == AntlrClass.NOT_FOUND)
        {
            ClassReferenceContext reference = ctx.classReference();
            this.error(
                    String.format("Cannot find class '%s'", reference.getText()),
                    reference,
                    this.currentCompilationUnit.getParserContext());
        }
    }
}
