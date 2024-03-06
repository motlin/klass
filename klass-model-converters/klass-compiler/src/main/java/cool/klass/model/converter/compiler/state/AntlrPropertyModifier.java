package cool.klass.model.converter.compiler.state;

import java.util.Objects;

import cool.klass.model.meta.grammar.KlassParser.PropertyModifierContext;

public class AntlrPropertyModifier
{
    private final PropertyModifierContext context;
    private final String                  name;

    public AntlrPropertyModifier(PropertyModifierContext context)
    {
        this.context = Objects.requireNonNull(context);
        this.name = Objects.requireNonNull(context.getText());
    }

    public PropertyModifierContext getContext()
    {
        return this.context;
    }

    public String getName()
    {
        return this.name;
    }

    public boolean isKey()
    {
        return this.name.equals("key");
    }
}
