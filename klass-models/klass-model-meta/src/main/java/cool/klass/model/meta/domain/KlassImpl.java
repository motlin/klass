package cool.klass.model.meta.domain;

import java.util.Objects;
import java.util.Optional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import cool.klass.model.meta.domain.api.Element;
import cool.klass.model.meta.domain.api.InheritanceType;
import cool.klass.model.meta.domain.api.Klass;
import cool.klass.model.meta.domain.api.property.AssociationEnd;
import cool.klass.model.meta.domain.api.source.KlassWithSourceCode;
import cool.klass.model.meta.domain.api.source.SourceCode;
import cool.klass.model.meta.domain.api.source.SourceCode.SourceCodeBuilder;
import cool.klass.model.meta.domain.property.AssociationEndImpl.AssociationEndBuilder;
import cool.klass.model.meta.grammar.KlassParser.ClassDeclarationContext;
import cool.klass.model.meta.grammar.KlassParser.IdentifierContext;
import org.eclipse.collections.api.list.ImmutableList;

public final class KlassImpl
        extends AbstractClassifier
        implements KlassWithSourceCode
{
    private final InheritanceType inheritanceType;
    private final boolean         isUser;
    private final boolean         isTransient;

    private ImmutableList<AssociationEnd> associationEnds;

    @Nonnull
    private Optional<AssociationEnd> versionProperty   = Optional.empty();
    @Nonnull
    private Optional<AssociationEnd> versionedProperty = Optional.empty();
    private Optional<Klass>          superClass;

    private KlassImpl(
            @Nonnull ClassDeclarationContext elementContext,
            @Nonnull Optional<Element> macroElement,
            @Nullable SourceCode sourceCode,
            int ordinal,
            @Nonnull IdentifierContext nameContext,
            @Nonnull String packageName,
            @Nonnull InheritanceType inheritanceType,
            boolean isUser,
            boolean isTransient)
    {
        super(elementContext, macroElement, sourceCode, ordinal, nameContext, packageName);
        this.inheritanceType = Objects.requireNonNull(inheritanceType);
        this.isUser          = isUser;
        this.isTransient     = isTransient;
    }

    @Nonnull
    @Override
    public ClassDeclarationContext getElementContext()
    {
        return (ClassDeclarationContext) super.getElementContext();
    }

    @Override
    public ImmutableList<AssociationEnd> getDeclaredAssociationEnds()
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
        this.versionProperty = Objects.requireNonNull(versionProperty);
    }

    @Override
    @Nonnull
    public Optional<AssociationEnd> getVersionedProperty()
    {
        return this.versionedProperty;
    }

    private void setVersionedProperty(@Nonnull Optional<AssociationEnd> versionedProperty)
    {
        this.versionedProperty = Objects.requireNonNull(versionedProperty);
    }

    @Override
    public InheritanceType getInheritanceType()
    {
        return this.inheritanceType;
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

    @Override
    public boolean isAbstract()
    {
        return this.inheritanceType != InheritanceType.NONE;
    }

    private void setAssociationEnds(ImmutableList<AssociationEnd> associationEnds)
    {
        if (this.associationEnds != null)
        {
            throw new IllegalStateException();
        }
        this.associationEnds = Objects.requireNonNull(associationEnds);
    }

    @Override
    @Nonnull
    public Optional<Klass> getSuperClass()
    {
        return this.superClass;
    }

    private void setSuperClass(Optional<Klass> superClass)
    {
        this.superClass = Objects.requireNonNull(superClass);
    }

    public static final class KlassBuilder
            extends ClassifierBuilder<KlassImpl>
    {
        @Nonnull
        private final InheritanceType inheritanceType;
        private final boolean         isUser;
        private final boolean         isTransient;

        @Nullable
        private ImmutableList<AssociationEndBuilder> associationEndBuilders;

        @Nonnull
        private Optional<AssociationEndBuilder> versionPropertyBuilder   = Optional.empty();
        @Nonnull
        private Optional<AssociationEndBuilder> versionedPropertyBuilder = Optional.empty();

        private Optional<KlassBuilder> superClassBuilder;

        public KlassBuilder(
                @Nonnull ClassDeclarationContext elementContext,
                @Nonnull Optional<ElementBuilder<?>> macroElement,
                @Nullable SourceCodeBuilder sourceCode,
                int ordinal,
                @Nonnull IdentifierContext nameContext,
                @Nonnull String packageName,
                @Nonnull InheritanceType inheritanceType,
                boolean isUser,
                boolean isTransient)
        {
            super(elementContext, macroElement, sourceCode, ordinal, nameContext, packageName);
            this.inheritanceType = Objects.requireNonNull(inheritanceType);
            this.isUser          = isUser;
            this.isTransient     = isTransient;
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

        @Override
        @Nonnull
        protected KlassImpl buildUnsafe()
        {
            return new KlassImpl(
                    (ClassDeclarationContext) this.elementContext,
                    this.macroElement.map(ElementBuilder::getElement),
                    this.sourceCode.build(),
                    this.ordinal,
                    this.getNameContext(),
                    this.packageName,
                    this.inheritanceType,
                    this.isUser,
                    this.isTransient);
        }

        public void setSuperClassBuilder(@Nonnull Optional<KlassBuilder> superClassBuilder)
        {
            this.superClassBuilder = Objects.requireNonNull(superClassBuilder);
        }

        @Override
        public void build2()
        {
            super.build2();

            ImmutableList<AssociationEnd> associationEnds = this.associationEndBuilders
                    .<AssociationEnd>collect(AssociationEndBuilder::getElement)
                    .toImmutable();
            this.element.setAssociationEnds(associationEnds);

            this.element.setVersionProperty(this.versionPropertyBuilder.map(AssociationEndBuilder::getElement));
            this.element.setVersionedProperty(this.versionedPropertyBuilder.map(AssociationEndBuilder::getElement));
            Optional<Klass> u = this.superClassBuilder.map(ElementBuilder::getElement);
            this.element.setSuperClass(u);
        }

        @Override
        public KlassImpl getType()
        {
            return Objects.requireNonNull(this.element);
        }
    }
}
