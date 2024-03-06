package cool.klass.model.meta.domain;

import java.util.Objects;
import java.util.Optional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import cool.klass.model.meta.domain.InterfaceImpl.InterfaceBuilder;
import cool.klass.model.meta.domain.api.Classifier;
import cool.klass.model.meta.domain.api.Element;
import cool.klass.model.meta.domain.api.Interface;
import cool.klass.model.meta.domain.api.NamedElement;
import cool.klass.model.meta.domain.api.modifier.Modifier;
import cool.klass.model.meta.domain.api.property.AssociationEndSignature;
import cool.klass.model.meta.domain.api.property.DataTypeProperty;
import cool.klass.model.meta.domain.api.property.PrimitiveProperty;
import cool.klass.model.meta.domain.api.source.ClassifierWithSourceCode;
import cool.klass.model.meta.domain.api.source.SourceCode;
import cool.klass.model.meta.domain.api.source.SourceCode.SourceCodeBuilder;
import cool.klass.model.meta.domain.property.AbstractDataTypeProperty.DataTypePropertyBuilder;
import cool.klass.model.meta.domain.property.AssociationEndSignatureImpl.AssociationEndSignatureBuilder;
import cool.klass.model.meta.domain.property.ModifierImpl.ModifierBuilder;
import cool.klass.model.meta.grammar.KlassParser.IdentifierContext;
import org.antlr.v4.runtime.ParserRuleContext;
import org.eclipse.collections.api.factory.Lists;
import org.eclipse.collections.api.list.ImmutableList;
import org.eclipse.collections.api.map.ImmutableMap;

public abstract class AbstractClassifier
        extends AbstractPackageableElement
        implements ClassifierWithSourceCode
{
    private ImmutableList<DataTypeProperty>        declaredDataTypeProperties;
    private ImmutableList<DataTypeProperty>        dataTypeProperties;
    private ImmutableMap<String, DataTypeProperty> dataTypePropertiesByName;
    private ImmutableList<DataTypeProperty>        keyProperties;

    private Optional<PrimitiveProperty>             systemProperty;
    private Optional<PrimitiveProperty>             systemFromProperty;
    private Optional<PrimitiveProperty>             systemToProperty;
    private Optional<PrimitiveProperty>             validProperty;
    private Optional<PrimitiveProperty>             validFromProperty;
    private Optional<PrimitiveProperty>             validToProperty;
    private Optional<PrimitiveProperty>             createdByProperty;
    private Optional<PrimitiveProperty>             createdOnProperty;
    private Optional<PrimitiveProperty>             lastUpdatedByProperty;

    private ImmutableList<Modifier>                modifiers;
    private ImmutableList<Interface>               interfaces;
    private ImmutableList<AssociationEndSignature> associationEndSignatures;

    protected AbstractClassifier(
            @Nonnull ParserRuleContext elementContext,
            @Nonnull Optional<Element> macroElement,
            @Nullable SourceCode sourceCode,
            int ordinal,
            @Nonnull IdentifierContext nameContext,
            @Nonnull String packageName)
    {
        super(elementContext, macroElement, sourceCode, ordinal, nameContext, packageName);
    }

    @Nonnull
    @Override
    public ImmutableList<DataTypeProperty> getDeclaredDataTypeProperties()
    {
        return this.declaredDataTypeProperties;
    }

    protected void setDeclaredDataTypeProperties(@Nonnull ImmutableList<DataTypeProperty> declaredDataTypeProperties)
    {
        if (this.declaredDataTypeProperties != null)
        {
            throw new IllegalStateException();
        }
        this.declaredDataTypeProperties       = Objects.requireNonNull(declaredDataTypeProperties);
    }

    @Nonnull
    @Override
    public ImmutableList<DataTypeProperty> getDataTypeProperties()
    {
        return this.dataTypeProperties;
    }

    protected void setDataTypeProperties(@Nonnull ImmutableList<DataTypeProperty> dataTypeProperties)
    {
        if (this.dataTypeProperties != null)
        {
            throw new IllegalStateException();
        }
        this.dataTypeProperties       = Objects.requireNonNull(dataTypeProperties);
        this.dataTypePropertiesByName = dataTypeProperties.groupByUniqueKey(DataTypeProperty::getName);
    }

    @Override
    public DataTypeProperty getDataTypePropertyByName(String name)
    {
        return this.dataTypePropertiesByName.get(name);
    }

    @Nonnull
    @Override
    public ImmutableList<Modifier> getDeclaredModifiers()
    {
        return this.modifiers;
    }

    protected void setModifiers(@Nonnull ImmutableList<Modifier> classifierModifiers)
    {
        if (this.modifiers != null)
        {
            throw new IllegalStateException();
        }
        this.modifiers = Objects.requireNonNull(classifierModifiers);
    }

    @Override
    @Nonnull
    public ImmutableList<Interface> getInterfaces()
    {
        return Objects.requireNonNull(this.interfaces);
    }

    protected void setInterfaces(@Nonnull ImmutableList<Interface> interfaces)
    {
        if (this.interfaces != null)
        {
            throw new IllegalStateException();
        }
        this.interfaces = Objects.requireNonNull(interfaces);
    }

    protected void setAssociationEndSignatures(ImmutableList<AssociationEndSignature> associationEndSignatures)
    {
        if (this.associationEndSignatures != null)
        {
            throw new IllegalStateException();
        }
        this.associationEndSignatures = Objects.requireNonNull(associationEndSignatures);
    }

    @Override
    public ImmutableList<DataTypeProperty> getKeyProperties()
    {
        return this.keyProperties;
    }

    protected void setKeyProperties(ImmutableList<DataTypeProperty> keyProperties)
    {
        if (this.keyProperties != null)
        {
            throw new IllegalStateException();
        }
        this.keyProperties = Objects.requireNonNull(keyProperties);
    }

    @Override
    public Optional<PrimitiveProperty> getSystemProperty()
    {
        return this.systemProperty;
    }

    protected void setSystemProperty(Optional<PrimitiveProperty> systemProperty)
    {
        if (this.systemProperty != null)
        {
            throw new IllegalStateException();
        }
        this.systemProperty = Objects.requireNonNull(systemProperty);
    }

    @Override
    public Optional<PrimitiveProperty> getSystemFromProperty()
    {
        return this.systemFromProperty;
    }

    protected void setSystemFromProperty(Optional<PrimitiveProperty> systemFromProperty)
    {
        if (this.systemFromProperty != null)
        {
            throw new IllegalStateException();
        }
        this.systemFromProperty = Objects.requireNonNull(systemFromProperty);
    }

    @Override
    public Optional<PrimitiveProperty> getSystemToProperty()
    {
        return this.systemToProperty;
    }

    protected void setSystemToProperty(Optional<PrimitiveProperty> systemToProperty)
    {
        if (this.systemToProperty != null)
        {
            throw new IllegalStateException();
        }
        this.systemToProperty = Objects.requireNonNull(systemToProperty);
    }

    @Override
    public Optional<PrimitiveProperty> getValidProperty()
    {
        return this.validProperty;
    }

    protected void setValidProperty(Optional<PrimitiveProperty> validProperty)
    {
        if (this.validProperty != null)
        {
            throw new IllegalStateException();
        }
        this.validProperty = Objects.requireNonNull(validProperty);
    }

    @Override
    public Optional<PrimitiveProperty> getValidFromProperty()
    {
        return this.validFromProperty;
    }

    protected void setValidFromProperty(Optional<PrimitiveProperty> validFromProperty)
    {
        if (this.validFromProperty != null)
        {
            throw new IllegalStateException();
        }
        this.validFromProperty = Objects.requireNonNull(validFromProperty);
    }

    @Override
    public Optional<PrimitiveProperty> getValidToProperty()
    {
        return this.validToProperty;
    }

    protected void setValidToProperty(Optional<PrimitiveProperty> validToProperty)
    {
        if (this.validToProperty != null)
        {
            throw new IllegalStateException();
        }
        this.validToProperty = Objects.requireNonNull(validToProperty);
    }

    @Override
    public Optional<PrimitiveProperty> getCreatedByProperty()
    {
        return this.createdByProperty;
    }

    protected void setCreatedByProperty(Optional<PrimitiveProperty> createdByProperty)
    {
        if (this.createdByProperty != null)
        {
            throw new IllegalStateException();
        }
        this.createdByProperty = Objects.requireNonNull(createdByProperty);
    }

    @Override
    public Optional<PrimitiveProperty> getCreatedOnProperty()
    {
        return this.createdOnProperty;
    }

    protected void setCreatedOnProperty(Optional<PrimitiveProperty> createdOnProperty)
    {
        if (this.createdOnProperty != null)
        {
            throw new IllegalStateException();
        }
        this.createdOnProperty = Objects.requireNonNull(createdOnProperty);
    }

    @Override
    public Optional<PrimitiveProperty> getLastUpdatedByProperty()
    {
        return this.lastUpdatedByProperty;
    }

    protected void setLastUpdatedByProperty(Optional<PrimitiveProperty> lastUpdatedByProperty)
    {
        if (this.lastUpdatedByProperty != null)
        {
            throw new IllegalStateException();
        }
        this.lastUpdatedByProperty = Objects.requireNonNull(lastUpdatedByProperty);
    }

    public abstract static class ClassifierBuilder<BuiltElement extends AbstractClassifier>
            extends PackageableElementBuilder<BuiltElement>
            implements TypeGetter,
            TopLevelElementBuilderWithSourceCode
    {
        protected ImmutableList<DataTypePropertyBuilder<?, ?, ?>> dataTypePropertyBuilders;
        protected ImmutableList<ModifierBuilder>                  modifierBuilders;
        protected ImmutableList<InterfaceBuilder>                 interfaceBuilders;
        protected ImmutableList<AssociationEndSignatureBuilder>   associationEndSignatureBuilders;

        protected ClassifierBuilder(
                @Nonnull ParserRuleContext elementContext,
                @Nonnull Optional<ElementBuilder<?>> macroElement,
                @Nullable SourceCodeBuilder sourceCode,
                int ordinal,
                @Nonnull IdentifierContext nameContext,
                @Nonnull String packageName)
        {
            super(elementContext, macroElement, sourceCode, ordinal, nameContext, packageName);
        }

        public void setDataTypePropertyBuilders(@Nonnull ImmutableList<DataTypePropertyBuilder<?, ?, ?>> dataTypePropertyBuilders)
        {
            if (this.dataTypePropertyBuilders != null)
            {
                throw new IllegalStateException();
            }
            this.dataTypePropertyBuilders = Objects.requireNonNull(dataTypePropertyBuilders);
        }

        public void setModifierBuilders(@Nonnull ImmutableList<ModifierBuilder> modifierBuilders)
        {
            if (this.modifierBuilders != null)
            {
                throw new IllegalStateException();
            }
            this.modifierBuilders = Objects.requireNonNull(modifierBuilders);
        }

        public void setAssociationEndSignatureBuilders(@Nonnull ImmutableList<AssociationEndSignatureBuilder> associationEndSignatureBuilders)
        {
            if (this.associationEndSignatureBuilders != null)
            {
                throw new IllegalStateException();
            }
            this.associationEndSignatureBuilders = Objects.requireNonNull(associationEndSignatureBuilders);
        }

        @Override
        protected void buildChildren()
        {
            ImmutableList<Modifier> modifiers = this.modifierBuilders.collect(ModifierBuilder::build);
            this.element.setModifiers(modifiers);

            ImmutableList<DataTypeProperty> dataTypeProperties = this.dataTypePropertyBuilders
                    .<DataTypeProperty>collect(DataTypePropertyBuilder::build)
                    .toImmutable();
            this.element.setDeclaredDataTypeProperties(dataTypeProperties);
        }

        public void setInterfaceBuilders(ImmutableList<InterfaceBuilder> interfaceBuilders)
        {
            this.interfaceBuilders = interfaceBuilders;
        }

        public void build2()
        {
            if (this.element == null)
            {
                throw new IllegalStateException();
            }

            this.dataTypePropertyBuilders.each(DataTypePropertyBuilder::build2);
            ImmutableList<Interface> superInterfaces = this.interfaceBuilders.collect(InterfaceBuilder::getType);
            this.element.setInterfaces(superInterfaces);

            ImmutableList<AssociationEndSignature> associationEndSignatures = this.associationEndSignatureBuilders
                    .<AssociationEndSignature>collect(AssociationEndSignatureBuilder::build)
                    .toImmutable();
            this.element.setAssociationEndSignatures(associationEndSignatures);
        }

        public void build3()
        {
            ImmutableList<DataTypeProperty> dataTypeProperties = this.getDataTypeProperties();

            ImmutableList<DataTypeProperty> foreignKeys = dataTypeProperties.select(DataTypeProperty::isForeignKey);
            ImmutableList<DataTypeProperty> keysAndForeignKeys = foreignKeys.select(DataTypeProperty::isKey);
            ImmutableList<DataTypeProperty> keysAndForeignKeysNotSelf = keysAndForeignKeys.reject(DataTypeProperty::isForeignKeyToSelf);
            ImmutableList<DataTypeProperty> keysAndForeignKeysSelf = keysAndForeignKeys.select(DataTypeProperty::isForeignKeyToSelf);
            ImmutableList<DataTypeProperty> keys = dataTypeProperties.select(DataTypeProperty::isKey).reject(DataTypeProperty::isForeignKey);
            ImmutableList<DataTypeProperty> nonKeyForeignKeys = foreignKeys.reject(DataTypeProperty::isKey).reject(DataTypeProperty::isCreatedBy).reject(DataTypeProperty::isLastUpdatedBy);
            ImmutableList<DataTypeProperty> system = dataTypeProperties.select(DataTypeProperty::isSystemRange);
            ImmutableList<DataTypeProperty> systemFrom = dataTypeProperties.select(DataTypeProperty::isSystemFrom);
            ImmutableList<DataTypeProperty> systemTo = dataTypeProperties.select(DataTypeProperty::isSystemTo);
            ImmutableList<DataTypeProperty> valid = dataTypeProperties.select(DataTypeProperty::isValidRange);
            ImmutableList<DataTypeProperty> validFrom = dataTypeProperties.select(DataTypeProperty::isValidFrom);
            ImmutableList<DataTypeProperty> validTo = dataTypeProperties.select(DataTypeProperty::isValidTo);
            ImmutableList<DataTypeProperty> createdBy = dataTypeProperties.select(DataTypeProperty::isCreatedBy).reject(DataTypeProperty::isKey);
            ImmutableList<DataTypeProperty> createdOn = dataTypeProperties.select(DataTypeProperty::isCreatedOn);
            ImmutableList<DataTypeProperty> lastUpdatedBy = dataTypeProperties.select(DataTypeProperty::isLastUpdatedBy).reject(DataTypeProperty::isKey);

            ImmutableList<DataTypeProperty> initialDataTypeProperties = Lists.immutable
                    .withAll(keysAndForeignKeysNotSelf)
                    .newWithAll(keysAndForeignKeysSelf)
                    .newWithAll(keys)
                    .newWithAll(nonKeyForeignKeys)
                    .newWithAll(system)
                    .newWithAll(systemFrom)
                    .newWithAll(systemTo)
                    .newWithAll(valid)
                    .newWithAll(validFrom)
                    .newWithAll(validTo)
                    .newWithAll(createdBy)
                    .newWithAll(createdOn)
                    .newWithAll(lastUpdatedBy);

            ImmutableList<DataTypeProperty> otherDataTypeProperties = dataTypeProperties
                    .reject(initialDataTypeProperties::contains);

            ImmutableList<DataTypeProperty> result = initialDataTypeProperties.newWithAll(otherDataTypeProperties);

            if (!result.equals(result.distinct()))
            {
                throw new AssertionError(result);
            }

            this.element.setDataTypeProperties(result);
            this.element.setKeyProperties(result.select(DataTypeProperty::isKey));

            ImmutableList<PrimitiveProperty> primitiveProperties = dataTypeProperties.selectInstancesOf(PrimitiveProperty.class);

            this.element.setSystemProperty(primitiveProperties.detectOptional(DataTypeProperty::isSystemRange));
            this.element.setSystemFromProperty(primitiveProperties.detectOptional(DataTypeProperty::isSystemFrom));
            this.element.setSystemToProperty(primitiveProperties.detectOptional(DataTypeProperty::isSystemTo));
            this.element.setValidProperty(primitiveProperties.detectOptional(DataTypeProperty::isValidRange));
            this.element.setValidFromProperty(primitiveProperties.detectOptional(DataTypeProperty::isValidFrom));
            this.element.setValidToProperty(primitiveProperties.detectOptional(DataTypeProperty::isValidTo));
            this.element.setCreatedByProperty(primitiveProperties.detectOptional(DataTypeProperty::isCreatedBy));
            this.element.setCreatedOnProperty(primitiveProperties.detectOptional(DataTypeProperty::isCreatedOn));
            this.element.setLastUpdatedByProperty(primitiveProperties.detectOptional(DataTypeProperty::isLastUpdatedBy));
        }

        protected ImmutableList<DataTypeProperty> getDataTypeProperties()
        {
            ImmutableList<DataTypeProperty> declaredDataTypeProperties = this.dataTypePropertyBuilders
                    .collect(property -> property.getElement());

            ImmutableList<DataTypeProperty> interfaceProperties = this.interfaceBuilders
                    .collect(ElementBuilder::getElement)
                    .flatCollect(Classifier::getDataTypeProperties)
                    .toImmutable();

            ImmutableList<DataTypeProperty> allDataTypeProperties = interfaceProperties
                    .newWithAll(declaredDataTypeProperties);

            ImmutableList<DataTypeProperty> result = allDataTypeProperties
                    .toReversed()
                    .distinctBy(NamedElement::getName)
                    .toReversed();

            return result;
        }

        @Override
        public abstract BuiltElement getType();
    }
}
