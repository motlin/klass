package cool.klass.model.meta.domain;

import javax.annotation.Nonnull;

import cool.klass.model.meta.domain.Klass.KlassBuilder;
import cool.klass.model.meta.domain.PrimitiveType.PrimitiveTypeBuilder;
import org.antlr.v4.runtime.ParserRuleContext;

public final class PrimitiveProperty extends DataTypeProperty<PrimitiveType>
{
    private PrimitiveProperty(
            @Nonnull ParserRuleContext elementContext,
            @Nonnull ParserRuleContext nameContext,
            @Nonnull String name,
            @Nonnull PrimitiveType primitiveType,
            @Nonnull Klass owningKlass,
            boolean isKey,
            boolean isOptional)
    {
        super(
                elementContext,
                nameContext,
                name,
                primitiveType,
                owningKlass,
                isKey,
                isOptional);
    }

    @Override
    public boolean isTemporalRange()
    {
        return this.type.isTemporalRange();
    }

    @Override
    public boolean isTemporalInstant()
    {
        return this.type.isTemporalInstant();
    }

    @Override
    public boolean isTemporal()
    {
        return this.type.isTemporal();
    }

    public static class PrimitivePropertyBuilder extends DataTypePropertyBuilder<PrimitiveType, PrimitiveTypeBuilder>
    {
        private PrimitiveProperty primitiveProperty;

        public PrimitivePropertyBuilder(
                @Nonnull ParserRuleContext elementContext,
                @Nonnull ParserRuleContext nameContext,
                @Nonnull String name,
                @Nonnull PrimitiveTypeBuilder primitiveTypeBuilder,
                @Nonnull KlassBuilder owningKlassBuilder,
                boolean isKey,
                boolean isOptional)
        {
            super(elementContext, nameContext, name, primitiveTypeBuilder, owningKlassBuilder, isKey, isOptional);
        }

        @Override
        public PrimitiveProperty build()
        {
            if (this.primitiveProperty != null)
            {
                throw new IllegalStateException();
            }
            this.primitiveProperty = new PrimitiveProperty(
                    this.elementContext,
                    this.nameContext,
                    this.name,
                    this.typeBuilder.getPrimitiveType(),
                    this.owningKlassBuilder.getKlass(),
                    this.isKey,
                    this.isOptional);
            return this.primitiveProperty;
        }

        @Override
        public PrimitiveProperty getProperty()
        {
            return this.primitiveProperty;
        }
    }
}
