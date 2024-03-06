package cool.klass.model.meta.domain;

import java.util.Objects;
import java.util.Optional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import cool.klass.model.meta.domain.api.Element;
import cool.klass.model.meta.domain.api.InheritanceType;
import cool.klass.model.meta.domain.api.Klass;
import cool.klass.model.meta.domain.api.NamedElement;
import cool.klass.model.meta.domain.api.property.AssociationEnd;
import cool.klass.model.meta.domain.api.source.KlassWithSourceCode;
import cool.klass.model.meta.domain.api.source.SourceCode;
import cool.klass.model.meta.domain.api.source.SourceCode.SourceCodeBuilder;
import cool.klass.model.meta.domain.property.AssociationEndImpl.AssociationEndBuilder;
import cool.klass.model.meta.grammar.KlassParser.ClassDeclarationContext;
import cool.klass.model.meta.grammar.KlassParser.IdentifierContext;
import org.eclipse.collections.api.factory.Lists;
import org.eclipse.collections.api.list.ImmutableList;
import org.eclipse.collections.api.map.ImmutableMap;

public final class KlassImpl
        extends AbstractClassifier
        implements KlassWithSourceCode
{
    private final InheritanceType inheritanceType;
    private final boolean isUser;
    private final boolean isTransient;

    private ImmutableList<AssociationEnd>        declaredAssociationEnds;

    private ImmutableList<AssociationEnd>        associationEnds;
    private ImmutableMap<String, AssociationEnd> associationEndsByName;

    @Nonnull
    private Optional<AssociationEnd> versionProperty   = Optional.empty();
    @Nonnull
    private Optional<AssociationEnd> versionedProperty = Optional.empty();
    private Optional<Klass>          superClass;
    private ImmutableList<Klass>     subClasses;

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

    @Override
    public ImmutableList<AssociationEnd> getDeclaredAssociationEnds()
    {
        return Objects.requireNonNull(this.declaredAssociationEnds);
    }

    private void setDeclaredAssociationEnds(ImmutableList<AssociationEnd> declaredAssociationEnds)
    {
        if (this.declaredAssociationEnds != null)
        {
            throw new IllegalStateException();
        }
        this.declaredAssociationEnds       = Objects.requireNonNull(declaredAssociationEnds);
    }

    private void setAssociationEnds(ImmutableList<AssociationEnd> associationEnds)
    {
        if (this.associationEnds != null)
        {
            throw new IllegalStateException();
        }
        this.associationEnds = Objects.requireNonNull(associationEnds);
        this.associationEndsByName = this.associationEnds.groupByUniqueKey(AssociationEnd::getName);
    }

    @Override
    public ImmutableList<AssociationEnd> getAssociationEnds()
    {
        return Objects.requireNonNull(this.associationEnds);
    }

    @Override
    public AssociationEnd getAssociationEndByName(String name)
    {
        return this.associationEndsByName.get(name);
    }

    @Override
    @Nonnull
    public Optional<Klass> getSuperClass()
    {
        return this.superClass;
    }

    private void setSuperClass(Optional<Klass> superClass)
    {
        if (this.superClass != null)
        {
            throw new IllegalStateException();
        }
        this.superClass = Objects.requireNonNull(superClass);
    }

    @Override
    public ImmutableList<Klass> getSubClasses()
    {
        return Objects.requireNonNull(this.subClasses);
    }

    public void setSubClasses(ImmutableList<Klass> subClasses)
    {
        if (this.subClasses != null)
        {
            throw new IllegalStateException();
        }
        this.subClasses = Objects.requireNonNull(subClasses);
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
        private ImmutableList<KlassBuilder> subClassBuilders;

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
            if (this.superClassBuilder != null)
            {
                throw new IllegalStateException();
            }
            this.superClassBuilder = Objects.requireNonNull(superClassBuilder);
        }

        public void setSubClassBuilders(ImmutableList<KlassBuilder> subClassBuilders)
        {
            if (this.subClassBuilders != null)
            {
                throw new IllegalStateException();
            }
            this.subClassBuilders = Objects.requireNonNull(subClassBuilders);
        }

        @Override
        public void build2()
        {
            super.build2();

            ImmutableList<AssociationEnd> declaredAssociationEnds = this.associationEndBuilders
                    .<AssociationEnd>collect(AssociationEndBuilder::getElement)
                    .toImmutable();
            this.element.setDeclaredAssociationEnds(declaredAssociationEnds);

            this.element.setVersionProperty(this.versionPropertyBuilder.map(AssociationEndBuilder::getElement));
            this.element.setVersionedProperty(this.versionedPropertyBuilder.map(AssociationEndBuilder::getElement));
            Optional<Klass> maybeSuperClass = this.superClassBuilder.map(ElementBuilder::getElement);
            this.element.setSuperClass(maybeSuperClass);
            ImmutableList<Klass> subClasses = this.subClassBuilders.collect(ElementBuilder::getElement);
            this.element.setSubClasses(subClasses);

            ImmutableList<AssociationEnd> associationEnds = maybeSuperClass
                    .map(Klass::getAssociationEnds)
                    .orElseGet(Lists.immutable::empty)
                    .newWithAll(declaredAssociationEnds)
                    .toReversed()
                    .distinctBy(NamedElement::getName)
                    .toReversed();

            this.element.setAssociationEnds(associationEnds);
        }

        @Override
        public KlassImpl getType()
        {
            return Objects.requireNonNull(this.element);
        }
    }
}
