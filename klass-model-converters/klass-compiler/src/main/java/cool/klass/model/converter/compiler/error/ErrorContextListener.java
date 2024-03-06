package cool.klass.model.converter.compiler.error;

import javax.annotation.Nonnull;

import cool.klass.model.converter.compiler.CompilationUnit;
import cool.klass.model.meta.grammar.KlassParser.AssociationDeclarationContext;
import cool.klass.model.meta.grammar.KlassParser.ClassDeclarationContext;
import cool.klass.model.meta.grammar.KlassParser.CompilationUnitContext;
import cool.klass.model.meta.grammar.KlassParser.EnumerationDeclarationContext;
import cool.klass.model.meta.grammar.KlassParser.InterfaceDeclarationContext;
import cool.klass.model.meta.grammar.KlassParser.ProjectionAssociationEndContext;
import cool.klass.model.meta.grammar.KlassParser.ProjectionDeclarationContext;
import cool.klass.model.meta.grammar.KlassParser.ServiceDeclarationContext;
import cool.klass.model.meta.grammar.KlassParser.ServiceGroupDeclarationContext;
import cool.klass.model.meta.grammar.KlassParser.UrlDeclarationContext;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.Token;
import org.eclipse.collections.api.list.MutableList;
import org.eclipse.collections.impl.list.mutable.ListAdapter;

public class ErrorContextListener extends AbstractErrorListener
{
    private int numProjectionAssociationEnds;

    public ErrorContextListener(
            @Nonnull CompilationUnit compilationUnit,
            @Nonnull MutableList<AbstractContextString> contextualStrings)
    {
        super(compilationUnit, contextualStrings);
    }

    @Override
    public void enterCompilationUnit(@Nonnull CompilationUnitContext ctx)
    {
        this.addTextInclusive("", ctx.getStart(), ctx.packageDeclaration().getStop());
    }

    @Override
    public void exitCompilationUnit(CompilationUnitContext ctx)
    {
        // Deliberately empty
    }

    @Override
    public void enterEnumerationDeclaration(@Nonnull EnumerationDeclarationContext ctx)
    {
        this.addTextInclusive("", ctx.getStart(), ctx.enumerationBody().getStart());
    }

    @Override
    public void exitEnumerationDeclaration(@Nonnull EnumerationDeclarationContext ctx)
    {
        this.addTextInclusive("", ctx.getStop(), ctx.getStop());
    }

    @Override
    public void enterInterfaceDeclaration(@Nonnull InterfaceDeclarationContext ctx)
    {
        this.addTextInclusive("", ctx.getStart(), ctx.interfaceBody().getStart());
    }

    @Override
    public void exitInterfaceDeclaration(@Nonnull InterfaceDeclarationContext ctx)
    {
        this.addTextInclusive("", ctx.getStop(), ctx.getStop());
    }

    @Override
    public void enterClassDeclaration(@Nonnull ClassDeclarationContext ctx)
    {
        this.addTextInclusive("", ctx.getStart(), ctx.classBody().getStart());
    }

    @Override
    public void exitClassDeclaration(@Nonnull ClassDeclarationContext ctx)
    {
        this.addTextInclusive("", ctx.getStop(), ctx.getStop());
    }

    @Override
    public void enterAssociationDeclaration(@Nonnull AssociationDeclarationContext ctx)
    {
        this.addTextInclusive("", ctx.getStart(), ctx.associationBody().getStart());
    }

    @Override
    public void exitAssociationDeclaration(@Nonnull AssociationDeclarationContext ctx)
    {
        this.addTextInclusive("", ctx.getStop(), ctx.getStop());
    }

    @Override
    public void enterProjectionDeclaration(@Nonnull ProjectionDeclarationContext ctx)
    {
        this.addTextInclusive("", ctx.getStart(), ctx.projectionBody().getStart());
    }

    @Override
    public void exitProjectionDeclaration(@Nonnull ProjectionDeclarationContext ctx)
    {
        this.addTextInclusive("", ctx.getStop(), ctx.getStop());
    }

    @Override
    public void enterProjectionAssociationEnd(@Nonnull ProjectionAssociationEndContext ctx)
    {
        this.numProjectionAssociationEnds++;

        String indent = this.getIndent(this.numProjectionAssociationEnds);
        this.addTextInclusive(indent, ctx.getStart(), ctx.projectionBody().getStart());
    }

    @Nonnull
    public String getIndent(int indents)
    {
        return new String(new char[indents]).replace("\0", "    ");
    }

    @Override
    public void exitProjectionAssociationEnd(ProjectionAssociationEndContext ctx)
    {
        this.numProjectionAssociationEnds--;
    }

    @Override
    public void enterServiceGroupDeclaration(@Nonnull ServiceGroupDeclarationContext ctx)
    {
        this.addTextInclusive("", ctx.getStart(), ctx.serviceGroupDeclarationBody().getStart());
    }

    @Override
    public void exitServiceGroupDeclaration(@Nonnull ServiceGroupDeclarationContext ctx)
    {
        this.addTextInclusive("", ctx.getStop(), ctx.getStop());
    }

    @Override
    public void enterUrlDeclaration(@Nonnull UrlDeclarationContext ctx)
    {
        this.addTextInclusive("    ", ctx.getStart(), ctx.url().getStop());
    }

    @Override
    public void exitUrlDeclaration(UrlDeclarationContext ctx)
    {
        // Deliberately empty
    }

    @Override
    public void enterServiceDeclaration(@Nonnull ServiceDeclarationContext ctx)
    {
        this.addTextInclusive("        ", ctx.getStart(), ctx.serviceDeclarationBody().getStart());
    }

    @Override
    public void exitServiceDeclaration(@Nonnull ServiceDeclarationContext ctx)
    {
        this.addTextInclusive("        ", ctx.getStop(), ctx.getStop());
    }

    private void addTextInclusive(String indent, @Nonnull Token start, @Nonnull Token stop)
    {
        String text   = this.getTextInclusive(start, stop);
        String string = indent + text;
        this.contextualStrings.add(new ContextString(start.getLine(), string));
    }

    private String getTextInclusive(@Nonnull Token startToken, @Nonnull Token stopToken)
    {
        CommonTokenStream tokenStream = (CommonTokenStream) this.compilationUnit.getTokenStream();
        MutableList<Token> tokens = ListAdapter.adapt(tokenStream.get(
                startToken.getTokenIndex(),
                stopToken.getTokenIndex()));

        return tokens
                .collect(AbstractErrorListener::colorize)
                .makeString("");
    }
}
