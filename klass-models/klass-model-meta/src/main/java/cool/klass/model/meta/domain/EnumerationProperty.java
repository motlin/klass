package cool.klass.model.meta.domain;

import cool.klass.model.meta.domain.Klass.KlassBuilder;
import org.antlr.v4.runtime.ParserRuleContext;

public class EnumerationProperty extends DataTypeProperty<Enumeration>
{
    protected EnumerationProperty(
            ParserRuleContext elementContext,
            ParserRuleContext nameContext,
            String name,
            ParserRuleContext enumerationContext,
            Enumeration enumeration,
            ParserRuleContext owningKlassContext,
            Klass owningKlass,
            boolean isKey,
            boolean isOptional)
    {
        super(
                elementContext,
                nameContext,
                name,
                enumerationContext,
                enumeration,
                owningKlassContext,
                owningKlass,
                isKey,
                isOptional);
    }

    public static class EnumerationPropertyBuilder extends DataTypePropertyBuilder<Enumeration>
    {
        public EnumerationPropertyBuilder(
                ParserRuleContext elementContext,
                ParserRuleContext nameContext,
                String name,
                ParserRuleContext enumerationContext,
                Enumeration enumeration,
                KlassBuilder owningKlassBuilder,
                boolean isKey,
                boolean isOptional)
        {
            super(
                    elementContext,
                    nameContext,
                    name,
                    enumerationContext,
                    enumeration,
                    owningKlassBuilder,
                    isKey,
                    isOptional);
        }

        @Override
        public EnumerationProperty build(ParserRuleContext owningKlassContext, Klass owningKlass)
        {
            return new EnumerationProperty(
                    this.elementContext,
                    this.nameContext,
                    this.name,
                    this.typeContext,
                    this.type,
                    owningKlassContext,
                    owningKlass,
                    this.isKey,
                    this.isOptional);
        }
    }
}
