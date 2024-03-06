package cool.klass.model.meta.domain;

import java.util.Objects;
import java.util.Optional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

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
    private final boolean isUser;
    private final boolean isTransient;

    private ImmutableList<DataTypeProperty> dataTypeProperties;
    private ImmutableList<AssociationEnd>   associationEnds;

    private ImmutableList<ClassModifier> classModifiers;

    @Nonnull
    private Optional<AssociationEnd> versionProperty   = Optional.empty();
    @Nonnull
    private Optional<AssociationEnd> versionedProperty = Optional.empty();

    private KlassImpl(
            @Nonnull ParserRuleContext elementContext,
            boolean inferred,
            @Nonnull ParserRuleContext nameContext,
            @Nonnull String name,
            int ordinal,
            @Nonnull String packageName,
            boolean isUser,
            boolean isTransient)
    {
        super(elementContext, inferred, nameContext, name, ordinal, packageName);
        this.isUser = isUser;
        this.isTransient = isTransient;
    }

    @Override
    public ImmutableList<DataTypeProperty> getDataTypeProperties()
    {
        return Objects.requireNonNull(this.dataTypeProperties);
    }

    private void setDataTypeProperties(ImmutableList<DataTypeProperty> dataTypeProperties)
    {
        if (this.dataTypeProperties != null)
        {
            throw new IllegalStateException();
        }
        this.dataTypeProperties = Objects.requireNonNull(dataTypeProperties);
    }

    @Override
    public ImmutableList<AssociationEnd> getAssociationEnds()
    {
        return Objects.requireNonNull(this.associationEnds);
    }

    @Override
    @Nonnull
    public Optional<AssociationEnd> getVersionProperty()
    {
        return this.versionProperty;
    }

    private void setVersionProperty(@Nonnull Optional<AssociationEnd> versionProperty)
    {
        this.versionProperty = versionProperty;
    }

    @Override
    @Nonnull
    public Optional<AssociationEnd> getVersionedProperty()
    {
        return this.versionedProperty;
    }

    private void setVersionedProperty(@Nonnull Optional<AssociationEnd> versionedProperty)
    {
        this.versionedProperty = versionedProperty;
    }

    @Override
    @Nonnull
    public ImmutableList<ClassModifier> getClassModifiers()
    {
        return Objects.requireNonNull(this.classModifiers);
    }

    public void setClassModifiers(ImmutableList<ClassModifier> classModifiers)
    {
        if (this.classModifiers != null)
        {
            throw new IllegalStateException();
        }
        this.classModifiers = Objects.requireNonNull(classModifiers);
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

    private void setAssociationEnds(ImmutableList<AssociationEnd> associationEnds)
    {
        if (this.associationEnds != null)
        {
            throw new IllegalStateException();
        }
        this.associationEnds = Objects.requireNonNull(associationEnds);
    }

    public static final class KlassBuilder extends PackageableElementBuilder<KlassImpl> implements TypeGetter, TopLevelElementBuilder
    {
        private final boolean                             isUser;
        private final boolean                             isTransient;

        @Nullable
        private ImmutableList<DataTypePropertyBuilder<?, ?, ?>> dataTypePropertyBuilders;
        @Nullable
        private ImmutableList<AssociationEndBuilder>            associationEndBuilders;
        @Nullable
        private  ImmutableList<ClassModifierBuilder> classModifierBuilders;

        @Nonnull
        private Optional<AssociationEndBuilder> versionPropertyBuilder = Optional.empty();
        @Nonnull
        private Optional<AssociationEndBuilder> versionedPropertyBuilder = Optional.empty();

        public KlassBuilder(
                @Nonnull ParserRuleContext elementContext,
                boolean inferred,
                @Nonnull ParserRuleContext nameContext,
                @Nonnull String name,
                int ordinal,
                @Nonnull String packageName,
                boolean isUser,
                boolean isTransient)
        {
            super(elementContext, inferred, nameContext, name, ordinal, packageName);
            this.isUser = isUser;
            this.isTransient = isTransient;
        }

        public void setDataTypePropertyBuilders(@Nonnull ImmutableList<DataTypePropertyBuilder<?, ?, ?>> dataTypePropertyBuilders)
        {
            if (this.dataTypePropertyBuilders != null)
            {
                throw new IllegalStateException();
            }
            this.dataTypePropertyBuilders = Objects.requireNonNull(dataTypePropertyBuilders);
        }

        public void setVersionPropertyBuilder(@Nonnull Optional<AssociationEndBuilder> versionPropertyBuilder)
        {
            if (this.versionedPropertyBuilder.isPresent())
            {
                throw new IllegalStateException();
            }
            this.versionPropertyBuilder = Objects.requireNonNull(versionPropertyBuilder);
        }

        public void setVersionedPropertyBuilder(@Nonnull Optional<AssociationEndBuilder> versionedPropertyBuilder)
        {
            if (this.versionedPropertyBuilder.isPresent())
            {
                throw new IllegalStateException();
            }
            this.versionedPropertyBuilder = Objects.requireNonNull(versionedPropertyBuilder);
        }

        public void setAssociationEndBuilders(@Nonnull ImmutableList<AssociationEndBuilder> associationEndBuilders)
        {
            if (this.associationEndBuilders != null)
            {
                throw new IllegalStateException();
            }
            this.associationEndBuilders = Objects.requireNonNull(associationEndBuilders);
        }

        public void setClassModifierBuilders(@Nonnull ImmutableList<ClassModifierBuilder> classModifierBuilders)
        {
            if (this.classModifierBuilders != null)
            {
                throw new IllegalStateException();
            }
            this.classModifierBuilders = Objects.requireNonNull(classModifierBuilders);
        }

        @Override
        @Nonnull
        protected KlassImpl buildUnsafe()
        {
            return new KlassImpl(
                    this.elementContext,
                    this.inferred,
                    this.nameContext,
                    this.name,
                    this.ordinal,
                    this.packageName,
                    this.isUser,
                    this.isTransient);
        }

        @Override
        protected void buildChildren()
        {
            ImmutableList<ClassModifier> classModifiers = this.classModifierBuilders.collect(ClassModifierBuilder::build);
            this.element.setClassModifiers(classModifiers);

            ImmutableList<DataTypeProperty> dataTypeProperties = this.dataTypePropertyBuilders
                    .<DataTypeProperty>collect(DataTypePropertyBuilder::build)
                    .toImmutable();
            this.element.setDataTypeProperties(dataTypeProperties);
        }

        public void build2()
        {
            if (this.element == null)
            {
                throw new IllegalStateException();
            }

            this.element.setVersionProperty(this.versionPropertyBuilder.map(AssociationEndBuilder::getElement));
            this.element.setVersionedProperty(this.versionedPropertyBuilder.map(AssociationEndBuilder::getElement));

            ImmutableList<AssociationEnd> associationEnds = this.associationEndBuilders
                    .<AssociationEnd>collect(AssociationEndBuilder::getElement)
                    .toImmutable();

            this.element.setAssociationEnds(associationEnds);
        }

        @Override
        public KlassImpl getType()
        {
            return Objects.requireNonNull(this.element);
        }
    }
}
