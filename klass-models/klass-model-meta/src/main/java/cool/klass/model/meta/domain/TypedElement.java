package cool.klass.model.meta.domain;

import java.util.Objects;

import cool.klass.model.meta.domain.Type.TypeBuilder;
import org.antlr.v4.runtime.ParserRuleContext;

public abstract class TypedElement<T extends Type> extends NamedElement
{
    protected final T type;

    protected TypedElement(
            ParserRuleContext elementContext,
            ParserRuleContext nameContext,
            String name,
            T type)
    {
        super(elementContext, nameContext, name);
        this.type = type;
    }

    public T getType()
    {
        return this.type;
    }

    public abstract static class TypedElementBuilder<T extends Type, TB extends TypeBuilder<T>> extends NamedElementBuilder
    {
        protected final TB typeBuilder;

        protected TypedElementBuilder(
                ParserRuleContext elementContext,
                ParserRuleContext nameContext,
                String name,
                TB typeBuilder)
        {
            super(elementContext, nameContext, name);
            this.typeBuilder = Objects.requireNonNull(typeBuilder);
        }
    }
}
