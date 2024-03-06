package cool.klass.model.converter.compiler.state;

import cool.klass.model.meta.domain.AssociationEnd.AssociationEndBuilder;
import cool.klass.model.meta.domain.Element;
import cool.klass.model.meta.domain.Klass;
import cool.klass.model.meta.grammar.KlassParser.AssociationEndContext;
import org.antlr.v4.runtime.ParserRuleContext;
import org.eclipse.collections.api.list.ImmutableList;
import org.eclipse.collections.impl.factory.Lists;

public class AntlrAssociationEnd extends AntlrProperty<AssociationEndContext, Klass>
{
    public static final AntlrAssociationEnd AMBIGUOUS = new AntlrAssociationEnd(
            new AssociationEndContext(null, -1),
            Element.NO_CONTEXT,
            "ambiguous property",
            null,
            null,
            Lists.immutable.empty());

    private final AntlrClass                                 type;
    private final AntlrMultiplicity                          antlrMultiplicity;
    private final ImmutableList<AntlrAssociationEndModifier> modifiers;

    private AntlrClass          owningClassState;
    private AntlrAssociationEnd opposite;

    public AntlrAssociationEnd(
            AssociationEndContext context,
            ParserRuleContext nameContext,
            String name,
            AntlrClass type,
            AntlrMultiplicity antlrMultiplicity,
            ImmutableList<AntlrAssociationEndModifier> modifiers)
    {
        super(context, nameContext, name);
        this.type = type;
        this.antlrMultiplicity = antlrMultiplicity;
        this.modifiers = modifiers;
    }

    public AntlrClass getType()
    {
        return this.type;
    }

    public AntlrMultiplicity getAntlrMultiplicity()
    {
        return this.antlrMultiplicity;
    }

    public ImmutableList<AntlrAssociationEndModifier> getModifiers()
    {
        return this.modifiers;
    }

    public AntlrAssociationEnd getOpposite()
    {
        return this.opposite;
    }

    public void setOpposite(AntlrAssociationEnd antlrAssociationEnd)
    {
        this.opposite = antlrAssociationEnd;
    }

    public void setOwningClassState(AntlrClass owningClassState)
    {
        this.owningClassState = owningClassState;
    }

    @Override
    public AssociationEndBuilder build()
    {
        return new AssociationEndBuilder(
                this.context,
                this.nameContext,
                this.name,
                this.type.getKlassBuilder(),
                this.owningClassState.getKlassBuilder(),
                this.antlrMultiplicity.getMultiplicity(),
                this.isOwned());
    }

    public boolean isOwned()
    {
        return this.modifiers.anySatisfy(AntlrAssociationEndModifier::isOwned);
    }
}
