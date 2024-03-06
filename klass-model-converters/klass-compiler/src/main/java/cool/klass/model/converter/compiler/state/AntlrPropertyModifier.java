package cool.klass.model.converter.compiler.state;

import java.util.Objects;

import javax.annotation.Nonnull;

import cool.klass.model.meta.grammar.KlassParser.PropertyModifierContext;

public class AntlrPropertyModifier
{
    @Nonnull
    private final PropertyModifierContext context;
    @Nonnull
    private final String                  name;

    public AntlrPropertyModifier(@Nonnull PropertyModifierContext context)
    {
        this.context = Objects.requireNonNull(context);
        this.name = Objects.requireNonNull(context.getText());
    }

    @Nonnull
    public PropertyModifierContext getContext()
    {
        return this.context;
    }

    @Nonnull
    public String getName()
    {
        return this.name;
    }

    public boolean isKey()
    {
        return this.name.equals("key");
    }
}
