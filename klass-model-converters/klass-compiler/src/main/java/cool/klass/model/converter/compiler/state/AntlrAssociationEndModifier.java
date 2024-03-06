package cool.klass.model.converter.compiler.state;

import java.util.Objects;

import cool.klass.model.meta.grammar.KlassParser.AssociationEndModifierContext;

public class AntlrAssociationEndModifier
{
    private final AssociationEndModifierContext context;
    private final String                        name;

    public AntlrAssociationEndModifier(AssociationEndModifierContext context)
    {
        this.context = Objects.requireNonNull(context);
        this.name = Objects.requireNonNull(context.getText());
    }

    public AssociationEndModifierContext getContext()
    {
        return this.context;
    }

    public String getName()
    {
        return this.name;
    }

    public boolean isOwned()
    {
        return this.name.equals("owned");
    }
}
