package cool.klass.model.converter.compiler.state;

import cool.klass.model.converter.compiler.EscapedIdentifierVisitor;
import cool.klass.model.meta.domain.Klass.KlassBuilder;
import cool.klass.model.meta.domain.PrimitiveProperty.PrimitivePropertyBuilder;
import cool.klass.model.meta.domain.PrimitiveType;
import cool.klass.model.meta.domain.Property.PropertyBuilder;
import cool.klass.model.meta.grammar.KlassParser.EscapedIdentifierContext;
import cool.klass.model.meta.grammar.KlassParser.PrimitivePropertyContext;
import org.eclipse.collections.api.set.ImmutableSet;
import org.eclipse.collections.impl.factory.Sets;

public class AntlrPrimitiveProperty extends AntlrProperty<PrimitivePropertyContext, PrimitiveType>
{
    public static final AntlrPrimitiveProperty AMBIGUOUS = new AntlrPrimitiveProperty(
            null,
            null,
            null,
            false,
            Sets.immutable.empty());

    private final PrimitiveType        primitiveType;
    private final boolean              isOptional;
    private final ImmutableSet<String> modifiers;

    private PrimitivePropertyBuilder primitivePropertyBuilder;

    public AntlrPrimitiveProperty(
            PrimitivePropertyContext ctx,
            String name,
            PrimitiveType primitiveType,
            boolean isOptional,
            ImmutableSet<String> modifiers)
    {
        super(ctx, name);
        this.primitiveType = primitiveType;
        this.isOptional = isOptional;
        this.modifiers = modifiers;
    }

    public PrimitiveType getPrimitiveType()
    {
        return this.primitiveType;
    }

    public boolean isOptional()
    {
        return this.isOptional;
    }

    @Override
    public PropertyBuilder<PrimitiveType> build(KlassBuilder klassBuilder)
    {
        EscapedIdentifierContext escapedIdentifierContext = this.ctx.escapedIdentifier();
        String                   name                     = EscapedIdentifierVisitor.get(escapedIdentifierContext);
        String                   primitiveTypeName        = this.ctx.primitiveType().getText();

        this.primitivePropertyBuilder = new PrimitivePropertyBuilder(
                this.ctx,
                escapedIdentifierContext,
                name,
                this.ctx.primitiveType(),
                PrimitiveType.valueOf(primitiveTypeName),
                klassBuilder,
                this.isKey(),
                this.isOptional);
        return this.primitivePropertyBuilder;
    }

    public boolean isKey()
    {
        return this.modifiers.contains("key");
    }
}
