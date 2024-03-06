package cool.klass.model.meta.domain;

import java.util.Objects;
import java.util.Optional;

import javax.annotation.Nonnull;

import cool.klass.model.meta.domain.property.AssociationEnd;
import cool.klass.model.meta.domain.property.AssociationEnd.AssociationEndBuilder;
import cool.klass.model.meta.domain.property.DataTypeProperty;
import cool.klass.model.meta.domain.property.DataTypeProperty.DataTypePropertyBuilder;
import org.antlr.v4.runtime.ParserRuleContext;
import org.eclipse.collections.api.list.ImmutableList;

public final class Klass extends Type
{
    private final boolean isUser;
    private final boolean isTransient;

    private ImmutableList<DataTypeProperty<?>> dataTypeProperties;
    private ImmutableList<AssociationEnd>      associationEnds;

    @Nonnull
    private Optional<Klass> versionClass   = Optional.empty();
    @Nonnull
    private Optional<Klass> versionedClass = Optional.empty();

    private Klass(
            @Nonnull ParserRuleContext elementContext,
            @Nonnull ParserRuleContext nameContext,
            @Nonnull String name,
            @Nonnull String packageName,
            boolean isUser,
            boolean isTransient)
    {
        super(elementContext, nameContext, name, packageName);
        this.isUser = isUser;
        this.isTransient = isTransient;
    }

    public ImmutableList<DataTypeProperty<?>> getDataTypeProperties()
    {
        return this.dataTypeProperties;
    }

    private void setDataTypeProperties(ImmutableList<DataTypeProperty<?>> dataTypeProperties)
    {
        this.dataTypeProperties = Objects.requireNonNull(dataTypeProperties);
    }

    @Nonnull
    public Optional<Klass> getVersionClass()
    {
        return this.versionClass;
    }

    private void setVersionClass(@Nonnull Klass versionClass)
    {
        this.versionClass = Optional.of(versionClass);
    }

    @Nonnull
    public Optional<Klass> getVersionedClass()
    {
        return this.versionedClass;
    }

    private void setVersionedClass(@Nonnull Klass versionedClass)
    {
        this.versionedClass = Optional.of(versionedClass);
    }

    public ImmutableList<AssociationEnd> getAssociationEnds()
    {
        return this.associationEnds;
    }

    private void setAssociationEnds(ImmutableList<AssociationEnd> associationEnds)
    {
        this.associationEnds = associationEnds;
    }

    public boolean isUser()
    {
        return this.isUser;
    }

    public boolean isTransient()
    {
        return this.isTransient;
    }

    public static final class KlassBuilder extends TypeBuilder
    {
        private final boolean isUser;
        private final boolean isTransient;

        private ImmutableList<DataTypePropertyBuilder<?, ?>> dataTypePropertyBuilders;
        private ImmutableList<AssociationEndBuilder>         associationEndBuilders;

        private KlassBuilder versionClassBuilder;
        private KlassBuilder versionedClassBuilder;

        private Klass klass;

        public KlassBuilder(
                @Nonnull ParserRuleContext elementContext,
                @Nonnull ParserRuleContext nameContext,
                @Nonnull String name,
                @Nonnull String packageName,
                boolean isUser,
                boolean isTransient)
        {
            super(elementContext, nameContext, name, packageName);
            this.isUser = isUser;
            this.isTransient = isTransient;
        }

        public void setDataTypePropertyBuilders(ImmutableList<DataTypePropertyBuilder<?, ?>> dataTypePropertyBuilders)
        {
            this.dataTypePropertyBuilders = Objects.requireNonNull(dataTypePropertyBuilders);
        }

        public void setVersionClassBuilder(KlassBuilder versionClassBuilder)
        {
            this.versionClassBuilder = Objects.requireNonNull(versionClassBuilder);
        }

        public void setVersionedClassBuilder(KlassBuilder versionedClassBuilder)
        {
            this.versionedClassBuilder = Objects.requireNonNull(versionedClassBuilder);
        }

        public void setAssociationEndBuilders(ImmutableList<AssociationEndBuilder> associationEndBuilders)
        {
            this.associationEndBuilders = Objects.requireNonNull(associationEndBuilders);
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
                    this.packageName,
                    this.isUser,
                    this.isTransient);

            ImmutableList<DataTypeProperty<?>> dataTypeProperties = this.dataTypePropertyBuilders
                    .<DataTypeProperty<?>>collect(DataTypePropertyBuilder::build)
                    .toImmutable();

            this.klass.setDataTypeProperties(dataTypeProperties);
            return this.klass;
        }

        public void build2()
        {
            if (this.klass == null)
            {
                throw new IllegalStateException();
            }

            if (this.versionClassBuilder != null)
            {
                this.klass.setVersionClass(this.versionClassBuilder.getKlass());
            }

            if (this.versionedClassBuilder != null)
            {
                this.klass.setVersionedClass(this.versionedClassBuilder.getKlass());
            }
        }

        public Klass getKlass()
        {
            return Objects.requireNonNull(this.klass);
        }

        public void build3()
        {
            if (this.klass == null)
            {
                throw new IllegalStateException();
            }

            ImmutableList<AssociationEnd> associationEnds = this.associationEndBuilders
                    .collect(AssociationEndBuilder::getAssociationEnd)
                    .toImmutable();

            this.klass.setAssociationEnds(associationEnds);
        }

        @Override
        public Klass getType()
        {
            return Objects.requireNonNull(this.klass);
        }
    }
}
