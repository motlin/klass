package cool.klass.model.converter.compiler.phase;

import cool.klass.model.meta.grammar.KlassBaseListener;
import cool.klass.model.meta.grammar.KlassParser.AssociationDeclarationContext;
import cool.klass.model.meta.grammar.KlassParser.ClassDeclarationContext;
import cool.klass.model.meta.grammar.KlassParser.EnumerationDeclarationContext;
import cool.klass.model.meta.grammar.KlassParser.ProjectionDeclarationContext;
import org.eclipse.collections.api.bag.MutableBag;
import org.eclipse.collections.impl.factory.Bags;

public class TopLevelElementNameCountPhase extends KlassBaseListener
{
    private final MutableBag<String> topLevelItemNames = Bags.mutable.empty();

    public int occurrencesOf(String topLevelItemName)
    {
        return this.topLevelItemNames.occurrencesOf(topLevelItemName);
    }

    @Override
    public void enterEnumerationDeclaration(EnumerationDeclarationContext ctx)
    {
        this.topLevelItemNames.add(ctx.identifier().getText());
    }

    @Override
    public void enterClassDeclaration(ClassDeclarationContext ctx)
    {
        this.topLevelItemNames.add(ctx.identifier().getText());
    }

    @Override
    public void enterAssociationDeclaration(AssociationDeclarationContext ctx)
    {
        this.topLevelItemNames.add(ctx.identifier().getText());
    }

    @Override
    public void enterProjectionDeclaration(ProjectionDeclarationContext ctx)
    {
        this.topLevelItemNames.add(ctx.identifier().getText());
    }
}
