package cool.klass.model.converter.compiler.error;

import javax.annotation.Nonnull;

public class UnderlineContextString extends AbstractContextString
{
    public UnderlineContextString(int line, String string)
    {
        super(line, string);
    }

    @Override
    @Nonnull
    protected String getLineNumberString(int line)
    {
        return "";
    }
}
