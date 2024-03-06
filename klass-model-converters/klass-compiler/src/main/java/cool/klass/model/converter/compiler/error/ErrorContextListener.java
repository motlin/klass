package cool.klass.model.converter.compiler.error;

import cool.klass.model.converter.compiler.CompilationUnit;
import cool.klass.model.meta.grammar.KlassParser.AssociationDeclarationContext;
import cool.klass.model.meta.grammar.KlassParser.ClassDeclarationContext;
import cool.klass.model.meta.grammar.KlassParser.CompilationUnitContext;
import cool.klass.model.meta.grammar.KlassParser.EnumerationDeclarationContext;
import cool.klass.model.meta.grammar.KlassParser.ProjectionDeclarationContext;
import org.eclipse.collections.api.list.MutableList;

public class ErrorContextListener extends BaseErrorListener
{
    public ErrorContextListener(CompilationUnit compilationUnit, MutableList<String> contextualStrings)
    {
        super(compilationUnit, contextualStrings);
    }

    @Override
    public void enterCompilationUnit(CompilationUnitContext ctx)
    {
        this.addTextInclusive(ctx.getStart(), ctx.packageDeclaration().getStop());
    }

    @Override
    public void enterClassDeclaration(ClassDeclarationContext ctx)
    {
        this.addTextInclusive(ctx.getStart(), ctx.classBody().getStart());
    }

    @Override
    public void exitClassDeclaration(ClassDeclarationContext ctx)
    {
        this.contextualStrings.add("}");
    }

    @Override
    public void enterEnumerationDeclaration(EnumerationDeclarationContext ctx)
    {
        this.addTextInclusive(ctx.getStart(), ctx.enumerationBody().getStart());
    }

    @Override
    public void exitEnumerationDeclaration(EnumerationDeclarationContext ctx)
    {
        this.contextualStrings.add("}");
    }

    @Override
    public void enterAssociationDeclaration(AssociationDeclarationContext ctx)
    {
        this.addTextInclusive(ctx.getStart(), ctx.associationBody().getStart());
    }

    @Override
    public void exitAssociationDeclaration(AssociationDeclarationContext ctx)
    {
        this.contextualStrings.add("}");
    }

    @Override
    public void enterProjectionDeclaration(ProjectionDeclarationContext ctx)
    {
        this.addTextInclusive(ctx.getStart(), ctx.projectionBody().getStart());
    }

    @Override
    public void exitProjectionDeclaration(ProjectionDeclarationContext ctx)
    {
        this.contextualStrings.add("}");
    }
}
