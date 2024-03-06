package cool.klass.model.meta.domain;

import javax.annotation.Nonnull;

import org.antlr.v4.runtime.ParserRuleContext;

/**
 * A DataType is a kind of Classifier. DataType differs from Class in that instances of a DataType are identified only by their value. All instances of a DataType with the same value are considered to be equal instances.
 */
public abstract class DataType extends Type
{
    protected DataType(
            @Nonnull ParserRuleContext elementContext,
            @Nonnull ParserRuleContext nameContext,
            @Nonnull String name,
            @Nonnull String packageName)
    {
        super(elementContext, nameContext, name, packageName);
    }

    public abstract static class DataTypeBuilder extends TypeBuilder
    {
        protected DataTypeBuilder(
                @Nonnull ParserRuleContext elementContext,
                @Nonnull ParserRuleContext nameContext,
                @Nonnull String name,
                @Nonnull String packageName)
        {
            super(elementContext, nameContext, name, packageName);
        }
    }
}
