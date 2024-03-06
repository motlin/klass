package cool.klass.model.converter.compiler.state;

import cool.klass.model.meta.domain.Klass.KlassBuilder;
import cool.klass.model.meta.domain.Property.PropertyBuilder;
import cool.klass.model.meta.domain.Type;
import org.antlr.v4.runtime.ParserRuleContext;

public abstract class AntlrProperty<C extends ParserRuleContext, T extends Type>
{
    public static final AntlrProperty<ParserRuleContext, Type> AMBIGUOUS = new AntlrProperty<ParserRuleContext, Type>(
            null,
            null)
    {
        @Override
        public PropertyBuilder<Type> build(KlassBuilder klassBuilder)
        {
            throw new UnsupportedOperationException(this.getClass().getSimpleName() + ".build() not implemented yet");
        }
    };

    protected final C ctx;
    protected final String name;

    protected AntlrProperty(C ctx, String name)
    {
        this.ctx = ctx;
        this.name = name;
    }

    public C getCtx()
    {
        return this.ctx;
    }

    public String getName()
    {
        return this.name;
    }

    public abstract PropertyBuilder<T> build(KlassBuilder klassBuilder);
}
