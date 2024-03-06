package cool.klass.model.converter.compiler.state;

import java.util.Objects;

import cool.klass.model.meta.domain.Element;
import cool.klass.model.meta.domain.Property.PropertyBuilder;
import cool.klass.model.meta.domain.Type;
import org.antlr.v4.runtime.ParserRuleContext;

public abstract class AntlrProperty<C extends ParserRuleContext, T extends Type>
{
    public static final AntlrProperty<ParserRuleContext, Type> AMBIGUOUS = new AntlrProperty<ParserRuleContext, Type>(
            Element.NO_CONTEXT,
            Element.NO_CONTEXT,
            "ambiguous property"
    )
    {
        @Override
        public PropertyBuilder<Type, ?> build()
        {
            throw new UnsupportedOperationException(this.getClass().getSimpleName() + ".build() not implemented");
        }
    };

    protected final C                 context;
    protected final ParserRuleContext nameContext;
    protected final String            name;

    protected AntlrProperty(
            C context,
            ParserRuleContext nameContext,
            String name)
    {
        this.context = Objects.requireNonNull(context);
        this.nameContext = Objects.requireNonNull(nameContext);
        this.name = Objects.requireNonNull(name);
    }

    public ParserRuleContext getNameContext()
    {
        return this.nameContext;
    }

    public String getName()
    {
        return this.name;
    }

    public abstract PropertyBuilder<T, ?> build();
}
