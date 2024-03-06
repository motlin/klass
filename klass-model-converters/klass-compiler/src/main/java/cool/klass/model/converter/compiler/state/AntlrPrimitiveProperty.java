package cool.klass.model.converter.compiler.state;

import cool.klass.model.meta.domain.Element;
import cool.klass.model.meta.domain.PrimitiveProperty.PrimitivePropertyBuilder;
import cool.klass.model.meta.domain.PrimitiveType;
import cool.klass.model.meta.domain.PrimitiveType.PrimitiveTypeBuilder;
import cool.klass.model.meta.grammar.KlassParser.PrimitivePropertyContext;
import org.antlr.v4.runtime.ParserRuleContext;
import org.eclipse.collections.api.list.ImmutableList;
import org.eclipse.collections.impl.factory.Lists;

public class AntlrPrimitiveProperty extends AntlrDataTypeProperty<PrimitiveType>
{
    public static final AntlrPrimitiveProperty AMBIGUOUS = new AntlrPrimitiveProperty(
            new PrimitivePropertyContext(null, -1),
            Element.NO_CONTEXT,
            "ambiguous primitive property name",
            false,
            Lists.immutable.empty(),
            null,
            null);

    private final PrimitiveTypeBuilder primitiveTypeBuilder;

    private PrimitivePropertyBuilder primitivePropertyBuilder;

    public AntlrPrimitiveProperty(
            ParserRuleContext context,
            ParserRuleContext nameContext,
            String name,
            boolean isOptional,
            ImmutableList<AntlrPropertyModifier> modifiers,
            PrimitiveTypeBuilder primitiveTypeBuilder,
            AntlrClass owningClassState)
    {
        super(context, nameContext, name, isOptional, modifiers, owningClassState);
        this.primitiveTypeBuilder = primitiveTypeBuilder;
    }

    @Override
    public boolean isTemporal()
    {
        return this.primitiveTypeBuilder.getPrimitiveType().isTemporal();
    }

    @Override
    public PrimitivePropertyBuilder build()
    {
        if (this.primitivePropertyBuilder != null)
        {
            throw new IllegalStateException();
        }

        this.primitivePropertyBuilder = new PrimitivePropertyBuilder(
                this.context,
                this.nameContext,
                this.name,
                this.primitiveTypeBuilder,
                this.owningClassState.getKlassBuilder(),
                this.isKey(),
                this.isOptional);
        return this.primitivePropertyBuilder;
    }
}
