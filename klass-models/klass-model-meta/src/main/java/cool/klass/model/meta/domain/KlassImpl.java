package cool.klass.model.meta.domain;

import java.util.Objects;
import java.util.Optional;

import javax.annotation.Nonnull;

import cool.klass.model.meta.domain.ClassModifierImpl.ClassModifierBuilder;
import cool.klass.model.meta.domain.api.ClassModifier;
import cool.klass.model.meta.domain.api.Klass;
import cool.klass.model.meta.domain.api.property.AssociationEnd;
import cool.klass.model.meta.domain.api.property.DataTypeProperty;
import cool.klass.model.meta.domain.property.AbstractDataTypeProperty.DataTypePropertyBuilder;
import cool.klass.model.meta.domain.property.AssociationEndImpl.AssociationEndBuilder;
import org.antlr.v4.runtime.ParserRuleContext;
import org.eclipse.collections.api.list.ImmutableList;

public final class KlassImpl extends AbstractPackageableElement implements TopLevelElement, Klass
{
    @Nonnull
    private final ImmutableList<ClassModifier> classModifiers;
    private final boolean                      isUser;
    private final boolean                      isTransient;

    private ImmutableList<DataTypeProperty> dataTypeProperties;
    private ImmutableList<AssociationEnd>   associationEnds;

    // TODO: Instead of Klasses, these two should be AssociationEnds
    @Nonnull
    private Optional<Klass> versionClass   = Optional.empty();
    @Nonnull
    private Optional<Klass> versionedClass = Optional.empty();

    private KlassImpl(
            @Nonnull ParserRuleContext elementContext,
            boolean inferred,
            @Nonnull ParserRuleContext nameContext,
            @Nonnull String name,
            int ordinal,
            @Nonnull String packageName,
            @Nonnull ImmutableList<ClassModifier> classModifiers,
            boolean isUser,
            boolean isTransient)
    {
        super(elementContext, inferred, nameContext, name, ordinal, packageName);
        this.classModifiers = Objects.requireNonNull(classModifiers);
        this.isUser = isUser;
        this.isTransient = isTransient;
    }

    @Override
    public ImmutableList<DataTypeProperty> getDataTypeProperties()
    {
        return this.dataTypeProperties;
    }

    private void setDataTypeProperties(ImmutableList<DataTypeProperty> dataTypeProperties)
    {
        this.dataTypeProperties = Objects.requireNonNull(dataTypeProperties);
    }

    @Override
    @Nonnull
    public Optional<Klass> getVersionClass()
    {
        return this.versionClass;
    }

    private void setVersionClass(@Nonnull KlassImpl versionClass)
    {
        this.versionClass = Optional.of(versionClass);
    }

    @Override
    @Nonnull
    public Optional<Klass> getVersionedClass()
    {
        return this.versionedClass;
    }

    private void setVersionedClass(@Nonnull KlassImpl versionedClass)
    {
        this.versionedClass = Optional.of(versionedClass);
    }

    @Override
    public ImmutableList<AssociationEnd> getAssociationEnds()
    {
        return this.associationEnds;
    }

    private void setAssociationEnds(ImmutableList<AssociationEnd> associationEnds)
    {
        this.associationEnds = associationEnds;
    }

    @Override
    @Nonnull
    public ImmutableList<ClassModifier> getClassModifiers()
    {
        return this.classModifiers;
    }

    @Override
    public boolean isUser()
    {
        return this.isUser;
    }

    @Override
    public boolean isTransient()
    {
        return this.isTransient;
    }

    public static final class KlassBuilder extends PackageableElementBuilder implements TypeGetter, TopLevelElementBuilder
    {
        @Nonnull
        private final ImmutableList<ClassModifierBuilder> classModifierBuilders;
        private final boolean                             isUser;
        private final boolean                             isTransient;

        private ImmutableList<DataTypePropertyBuilder<?, ?>> dataTypePropertyBuilders;
        private ImmutableList<AssociationEndBuilder>         associationEndBuilders;

        private KlassBuilder versionClassBuilder;
        private KlassBuilder versionedClassBuilder;

        private KlassImpl klass;

        public KlassBuilder(
                @Nonnull ParserRuleContext elementContext,
                boolean inferred,
                @Nonnull ParserRuleContext nameContext,
                @Nonnull String name,
                int ordinal,
                @Nonnull String packageName,
                @Nonnull ImmutableList<ClassModifierBuilder> classModifierBuilders,
                boolean isUser,
                boolean isTransient)
        {
            super(elementContext, inferred, nameContext, name, ordinal, packageName);
            this.classModifierBuilders = Objects.requireNonNull(classModifierBuilders);
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

        public KlassImpl build1()
        {
            if (this.klass != null)
            {
                throw new IllegalStateException();
            }

            ImmutableList<ClassModifier> classModifiers = this.classModifierBuilders.collect(ClassModifierBuilder::build);

            this.klass = new KlassImpl(
                    this.elementContext,
                    this.inferred,
                    this.nameContext,
                    this.name,
                    this.ordinal,
                    this.packageName,
                    classModifiers,
                    this.isUser,
                    this.isTransient);

            ImmutableList<DataTypeProperty> dataTypeProperties = this.dataTypePropertyBuilders
                    .<DataTypeProperty>collect(DataTypePropertyBuilder::build)
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
                this.klass.setVersionClass(this.versionClassBuilder.getElement());
            }

            if (this.versionedClassBuilder != null)
            {
                this.klass.setVersionedClass(this.versionedClassBuilder.getElement());
            }
        }

        public void build3()
        {
            if (this.klass == null)
            {
                throw new IllegalStateException();
            }

            ImmutableList<AssociationEnd> associationEnds = this.associationEndBuilders
                    .<AssociationEnd>collect(AssociationEndBuilder::getAssociationEnd)
                    .toImmutable();

            this.klass.setAssociationEnds(associationEnds);
        }

        @Override
        public KlassImpl getType()
        {
            return Objects.requireNonNull(this.klass);
        }

        @Override
        public KlassImpl getElement()
        {
            return Objects.requireNonNull(this.klass);
        }
    }
}
