package cool.klass.model.converter.compiler.state.property;

import java.util.Objects;

import javax.annotation.Nonnull;

import cool.klass.model.meta.grammar.KlassParser.AssociationEndModifierContext;

public class AntlrAssociationEndModifier
{
    @Nonnull
    private final AssociationEndModifierContext context;
    @Nonnull
    private final String                        name;

    public AntlrAssociationEndModifier(@Nonnull AssociationEndModifierContext context)
    {
        this.context = Objects.requireNonNull(context);
        this.name = Objects.requireNonNull(context.getText());
    }

    @Nonnull
    public AssociationEndModifierContext getContext()
    {
        return this.context;
    }

    @Nonnull
    public String getName()
    {
        return this.name;
    }

    public boolean isOwned()
    {
        return this.name.equals("owned");
    }
}
