package cool.klass.model.meta.domain;

import cool.klass.model.meta.domain.Klass.KlassBuilder;
import org.antlr.v4.runtime.ParserRuleContext;

public class PrimitiveProperty extends DataTypeProperty<PrimitiveType>
{
    protected PrimitiveProperty(
            ParserRuleContext elementContext,
            ParserRuleContext nameContext,
            String name,
            ParserRuleContext dataTypeContext,
            PrimitiveType primitiveType,
            ParserRuleContext owningKlassContext,
            Klass owningKlass,
            boolean isOptional)
    {
        super(
                elementContext,
                nameContext,
                name,
                dataTypeContext,
                primitiveType,
                owningKlassContext,
                owningKlass,
                isOptional);
    }

    public static class PrimitivePropertyBuilder extends DataTypePropertyBuilder<PrimitiveType>
    {
        public PrimitivePropertyBuilder(
                ParserRuleContext elementContext,
                ParserRuleContext nameContext,
                String name,
                ParserRuleContext typeContext,
                PrimitiveType primitiveType,
                KlassBuilder owningKlassBuilder,
                boolean isOptional)
        {
            super(elementContext, nameContext, name, typeContext, primitiveType, owningKlassBuilder, isOptional);
        }

        @Override
        public PrimitiveProperty build(ParserRuleContext owningKlassContext, Klass owningKlass)
        {
            return new PrimitiveProperty(
                    this.elementContext,
                    this.nameContext,
                    this.name,
                    this.typeContext,
                    this.type,
                    owningKlassContext,
                    owningKlass,
                    this.isOptional);
        }
    }
}
