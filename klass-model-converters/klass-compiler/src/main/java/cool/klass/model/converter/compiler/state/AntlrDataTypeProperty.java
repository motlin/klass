package cool.klass.model.converter.compiler.state;

import cool.klass.model.meta.domain.DataType;
import cool.klass.model.meta.domain.DataTypeProperty.DataTypePropertyBuilder;
import org.antlr.v4.runtime.ParserRuleContext;
import org.eclipse.collections.api.list.ImmutableList;

public abstract class AntlrDataTypeProperty<C extends ParserRuleContext, T extends DataType> extends AntlrProperty<C, T>
{
    protected final boolean                              isOptional;
    protected final ImmutableList<AntlrPropertyModifier> modifiers;
    protected final AntlrClass                           owningClassState;

    protected AntlrDataTypeProperty(
            C context,
            ParserRuleContext nameContext,
            String name,
            boolean isOptional,
            ImmutableList<AntlrPropertyModifier> modifiers,
            AntlrClass owningClassState)
    {
        super(context, nameContext, name);
        this.isOptional = isOptional;
        this.modifiers = modifiers;
        this.owningClassState = owningClassState;
    }

    public boolean isKey()
    {
        return this.modifiers.anySatisfy(AntlrPropertyModifier::isKey);
    }

    @Override
    public abstract DataTypePropertyBuilder<T, ?> build();
}
