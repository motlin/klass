package cool.klass.model.converter.compiler.annotation;

import javax.annotation.Nonnull;

public class ContextString extends AbstractContextString
{
    public ContextString(int line, @Nonnull String string)
    {
        super(line, string);
    }

    @Nonnull
    @Override
    protected String getLineNumberString(int line)
    {
        return String.valueOf(line);
    }
}
