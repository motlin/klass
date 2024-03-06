package cool.klass.model.meta.domain;

import java.util.Objects;
import java.util.Optional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import cool.klass.model.meta.domain.api.Classifier;
import cool.klass.model.meta.domain.api.Element;
import cool.klass.model.meta.domain.api.Klass;
import cool.klass.model.meta.domain.api.NamedElement;
import cool.klass.model.meta.domain.api.property.AssociationEnd;
import cool.klass.model.meta.domain.api.property.DataTypeProperty;
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
    private final boolean isAbstract;
    private final boolean isUser;
    private final boolean isTransient;

    private ImmutableList<AssociationEnd>        declaredAssociationEnds;
    private ImmutableMap<String, AssociationEnd> declaredAssociationEndsByName;
    private ImmutableList<AssociationEnd>        associationEnds;
    private ImmutableMap<String, AssociationEnd> associationEndsByName;

    @Nonnull
    private Optional<AssociationEnd> versionProperty   = Optional.empty();
    @Nonnull
    private Optional<AssociationEnd> versionedProperty = Optional.empty();

    private Optional<Klass>      superClass;
    private ImmutableList<Klass> subClasses;

    private KlassImpl(
            @Nonnull ClassDeclarationContext elementContext,
            @Nonnull Optional<Element> macroElement,
            @Nullable SourceCode sourceCode,
            int ordinal,
            @Nonnull IdentifierContext nameContext,
            @Nonnull String packageName,
            boolean isAbstract,
            boolean isUser,
            boolean isTransient)
    {
        super(elementContext, macroElement, sourceCode, ordinal, nameContext, packageName);
        this.isAbstract  = isAbstract;
        this.isUser      = isUser;
        this.isTransient = isTransient;
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

    @Override
    @Nonnull
    public Optional<AssociationEnd> getVersionedProperty()
    {
        return this.versionedProperty;
    }

    @Override
    public boolean isAbstract()
    {
        return this.isAbstract;
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

    private void setDeclaredAssociationEnds(ImmutableList<AssociationEnd> declaredAssociationEnds)
    {
        if (this.declaredAssociationEnds != null)
        {
            throw new IllegalStateException();
        }
        this.declaredAssociationEnds       = Objects.requireNonNull(declaredAssociationEnds);
        this.declaredAssociationEndsByName = this.declaredAssociationEnds.groupByUniqueKey(AssociationEnd::getName);
    }

    @Override
    public ImmutableList<AssociationEnd> getDeclaredAssociationEnds()
    {
        return Objects.requireNonNull(this.declaredAssociationEnds);
    }

    @Override
    public AssociationEnd getDeclaredAssociationEndByName(String name)
    {
        return this.declaredAssociationEndsByName.get(name);
    }

    private void setAssociationEnds(ImmutableList<AssociationEnd> associationEnds)
    {
        if (this.associationEnds != null)
        {
            throw new IllegalStateException();
        }
        this.associationEnds       = Objects.requireNonNull(associationEnds);
        this.associationEndsByName = this.associationEnds.groupByUniqueKey(AssociationEnd::getName);

        this.versionProperty   = this.associationEnds.detectOptional(AssociationEnd::isVersion);
        this.versionedProperty = this.associationEnds.detectOptional(AssociationEnd::isVersioned);
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
        private final boolean isAbstract;
        private final boolean isUser;
        private final boolean isTransient;

        @Nullable
        private ImmutableList<AssociationEndBuilder> associationEndBuilders;

        private Optional<KlassBuilder>      superClassBuilder;
        private ImmutableList<KlassBuilder> subClassBuilders;

        public KlassBuilder(
                @Nonnull ClassDeclarationContext elementContext,
                @Nonnull Optional<ElementBuilder<?>> macroElement,
                @Nullable SourceCodeBuilder sourceCode,
                int ordinal,
                @Nonnull IdentifierContext nameContext,
                @Nonnull String packageName,
                boolean isAbstract,
                boolean isUser,
                boolean isTransient)
        {
            super(elementContext, macroElement, sourceCode, ordinal, nameContext, packageName);
            this.isAbstract  = isAbstract;
            this.isUser      = isUser;
            this.isTransient = isTransient;
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
                    this.isAbstract,
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
        protected ImmutableList<DataTypeProperty> getDataTypeProperties()
        {
            ImmutableList<DataTypeProperty> declaredDataTypeProperties = this.dataTypePropertyBuilders
                    .collect(property -> property.getElement());

            ImmutableList<DataTypeProperty> interfaceProperties = this.interfaceBuilders
                    .collect(ElementBuilder::getElement)
                    .flatCollect(Classifier::getDataTypeProperties)
                    .toImmutable();

            ImmutableList<DataTypeProperty> superClassProperties = this.superClassBuilder
                    .map(ElementBuilder::getElement)
                    .map(Classifier::getDataTypeProperties)
                    .orElseGet(Lists.immutable::empty);

            ImmutableList<DataTypeProperty> allDataTypeProperties = interfaceProperties
                    .newWithAll(superClassProperties)
                    .newWithAll(declaredDataTypeProperties);

            ImmutableList<DataTypeProperty> result = allDataTypeProperties
                    .toReversed()
                    .distinctBy(NamedElement::getName)
                    .toReversed();

            return result;
        }

        @Override
        public KlassImpl getType()
        {
            return Objects.requireNonNull(this.element);
        }
    }
}
