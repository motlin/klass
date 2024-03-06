package cool.klass.model.converter.compiler.state;

import cool.klass.model.meta.domain.Enumeration;
import cool.klass.model.meta.domain.EnumerationProperty.EnumerationPropertyBuilder;
import cool.klass.model.meta.grammar.KlassParser.EnumerationPropertyContext;
import org.antlr.v4.runtime.ParserRuleContext;
import org.eclipse.collections.api.list.ImmutableList;

public class AntlrEnumerationProperty extends AntlrDataTypeProperty<Enumeration>
{
    // TODO: Check that it's not NOT_FOUND
    private final AntlrEnumeration antlrEnumeration;

    private EnumerationPropertyBuilder enumerationPropertyBuilder;

    public AntlrEnumerationProperty(
            EnumerationPropertyContext context,
            ParserRuleContext nameContext,
            String name,
            boolean isOptional,
            ImmutableList<AntlrPropertyModifier> modifiers,
            AntlrClass owningClassState,
            AntlrEnumeration antlrEnumeration)
    {
        super(context, nameContext, name, isOptional, modifiers, owningClassState);
        this.antlrEnumeration = antlrEnumeration;
    }

    @Override
    public boolean isTemporal()
    {
        return false;
    }

    @Override
    public EnumerationPropertyBuilder build()
    {
        if (this.enumerationPropertyBuilder != null)
        {
            throw new IllegalStateException();
        }

        this.enumerationPropertyBuilder = new EnumerationPropertyBuilder(
                this.context,
                this.nameContext,
                this.name,
                this.antlrEnumeration.getEnumerationBuilder(),
                this.owningClassState.getKlassBuilder(),
                this.isKey(),
                this.isOptional);
        return this.enumerationPropertyBuilder;
    }
}
