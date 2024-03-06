package cool.klass.model.meta.domain;

import cool.klass.model.meta.domain.Enumeration.EnumerationBuilder;
import cool.klass.model.meta.domain.Klass.KlassBuilder;
import org.antlr.v4.runtime.ParserRuleContext;

public final class EnumerationProperty extends DataTypeProperty<Enumeration>
{
    private EnumerationProperty(
            ParserRuleContext elementContext,
            ParserRuleContext nameContext,
            String name,
            Enumeration enumeration,
            Klass owningKlass,
            boolean isKey,
            boolean isOptional)
    {
        super(
                elementContext,
                nameContext,
                name,
                enumeration,
                owningKlass,
                isKey,
                isOptional);
    }

    public static class EnumerationPropertyBuilder extends DataTypePropertyBuilder<Enumeration, EnumerationBuilder>
    {
        private EnumerationProperty enumerationProperty;

        public EnumerationPropertyBuilder(
                ParserRuleContext elementContext,
                ParserRuleContext nameContext,
                String name,
                EnumerationBuilder enumerationBuilder,
                KlassBuilder owningKlassBuilder,
                boolean isKey,
                boolean isOptional)
        {
            super(
                    elementContext,
                    nameContext,
                    name,
                    enumerationBuilder,
                    owningKlassBuilder,
                    isKey,
                    isOptional);
        }

        @Override
        public EnumerationProperty build(Klass owningKlass)
        {
            if (this.enumerationProperty != null)
            {
                throw new IllegalStateException();
            }
            this.enumerationProperty = new EnumerationProperty(
                    this.elementContext,
                    this.nameContext,
                    this.name,
                    this.typeBuilder.getEnumeration(),
                    this.owningKlassBuilder.getKlass(),
                    this.isKey,
                    this.isOptional);
            return this.enumerationProperty;
        }
    }
}
