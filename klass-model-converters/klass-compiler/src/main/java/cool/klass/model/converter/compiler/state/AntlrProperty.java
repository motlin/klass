package cool.klass.model.converter.compiler.state;

import org.antlr.v4.runtime.ParserRuleContext;

public class AntlrProperty<T extends ParserRuleContext>
{
    public static final AntlrProperty<ParserRuleContext> AMBIGUOUS = new AntlrProperty<>(null, null);

    protected final T      ctx;
    protected final String name;

    public AntlrProperty(T ctx, String name)
    {
        this.ctx = ctx;
        this.name = name;
    }

    public T getCtx()
    {
        return this.ctx;
    }

    public String getName()
    {
        return this.name;
    }
}
