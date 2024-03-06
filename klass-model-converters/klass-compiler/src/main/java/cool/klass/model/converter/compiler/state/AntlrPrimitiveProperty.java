package cool.klass.model.converter.compiler.state;

import cool.klass.model.meta.domain.PrimitiveType;
import cool.klass.model.meta.grammar.KlassParser.PrimitivePropertyContext;

public class AntlrPrimitiveProperty extends AntlrProperty<PrimitivePropertyContext>
{
    public static final AntlrPrimitiveProperty AMBIGUOUS = new AntlrPrimitiveProperty(
            null,
            null,
            null,
            false);

    private final PrimitiveType primitiveType;
    private final boolean       isOptional;

    public AntlrPrimitiveProperty(
            PrimitivePropertyContext ctx,
            String name,
            PrimitiveType primitiveType,
            boolean isOptional)
    {
        super(ctx, name);
        this.primitiveType = primitiveType;
        this.isOptional = isOptional;
    }

    public PrimitiveType getPrimitiveType()
    {
        return this.primitiveType;
    }

    public boolean isOptional()
    {
        return this.isOptional;
    }
}
