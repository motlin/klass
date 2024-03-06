package cool.klass.model.meta.domain;

import javax.annotation.Nonnull;

import cool.klass.model.meta.domain.property.AssociationEnd;
import cool.klass.model.meta.domain.property.AssociationEnd.AssociationEndBuilder;
import cool.klass.model.meta.domain.property.DataTypeProperty;
import cool.klass.model.meta.domain.property.DataTypeProperty.DataTypePropertyBuilder;
import org.antlr.v4.runtime.ParserRuleContext;
import org.eclipse.collections.api.list.ImmutableList;

public final class Klass extends Type
{
    private ImmutableList<DataTypeProperty<?>> dataTypeProperties;
    private ImmutableList<AssociationEnd>      associationEnds;

    private Klass(
            @Nonnull ParserRuleContext elementContext,
            @Nonnull ParserRuleContext nameContext,
            @Nonnull String name,
            @Nonnull String packageName)
    {
        super(elementContext, nameContext, name, packageName);
    }

    public ImmutableList<AssociationEnd> getAssociationEnds()
    {
        return this.associationEnds;
    }

    private void setAssociationEnds(ImmutableList<AssociationEnd> associationEnds)
    {
        this.associationEnds = associationEnds;
    }

    public ImmutableList<DataTypeProperty<?>> getDataTypeProperties()
    {
        return this.dataTypeProperties;
    }

    private void setDataTypeProperties(ImmutableList<DataTypeProperty<?>> dataTypeProperties)
    {
        this.dataTypeProperties = dataTypeProperties;
    }

    public static final class KlassBuilder extends TypeBuilder<Klass>
    {
        private ImmutableList<DataTypePropertyBuilder<?, ?>> dataTypePropertyBuilders;
        private ImmutableList<AssociationEndBuilder>         associationEndBuilders;
        private Klass                                        klass;

        public KlassBuilder(
                @Nonnull ParserRuleContext elementContext,
                @Nonnull ParserRuleContext nameContext,
                @Nonnull String name,
                @Nonnull String packageName)
        {
            super(elementContext, nameContext, name, packageName);
        }

        public void setDataTypePropertyBuilders(ImmutableList<DataTypePropertyBuilder<?, ?>> dataTypePropertyBuilders)
        {
            this.dataTypePropertyBuilders = dataTypePropertyBuilders;
        }

        public void setAssociationEndBuilders(ImmutableList<AssociationEndBuilder> associationEndBuilders)
        {
            this.associationEndBuilders = associationEndBuilders;
        }

        public Klass build1()
        {
            if (this.klass != null)
            {
                throw new IllegalStateException();
            }

            this.klass = new Klass(
                    this.elementContext,
                    this.nameContext,
                    this.name,
                    this.packageName);

            ImmutableList<DataTypeProperty<?>> dataTypeProperties = this.dataTypePropertyBuilders
                    .<DataTypeProperty<?>>collect(DataTypePropertyBuilder::build)
                    .toImmutable();

            this.klass.setDataTypeProperties(dataTypeProperties);
            return this.klass;
        }

        public Klass build2()
        {
            if (this.klass == null)
            {
                throw new IllegalStateException();
            }

            ImmutableList<AssociationEnd> associationEnds = this.associationEndBuilders
                    .collect(AssociationEndBuilder::getAssociationEnd)
                    .toImmutable();

            this.klass.setAssociationEnds(associationEnds);
            return this.klass;
        }

        public Klass getKlass()
        {
            return this.klass;
        }
    }
}
