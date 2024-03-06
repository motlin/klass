package cool.klass.model.meta.domain;

import java.util.Objects;
import java.util.Optional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import cool.klass.model.meta.domain.InterfaceImpl.InterfaceBuilder;
import cool.klass.model.meta.domain.api.Classifier;
import cool.klass.model.meta.domain.api.Element;
import cool.klass.model.meta.domain.api.Interface;
import cool.klass.model.meta.domain.api.modifier.Modifier;
import cool.klass.model.meta.domain.api.property.AssociationEndSignature;
import cool.klass.model.meta.domain.api.property.DataTypeProperty;
import cool.klass.model.meta.domain.api.source.SourceCode;
import cool.klass.model.meta.domain.api.source.SourceCode.SourceCodeBuilder;
import cool.klass.model.meta.domain.api.source.TopLevelElementWithSourceCode;
import cool.klass.model.meta.domain.property.AbstractDataTypeProperty.DataTypePropertyBuilder;
import cool.klass.model.meta.domain.property.AssociationEndSignatureImpl.AssociationEndSignatureBuilder;
import cool.klass.model.meta.domain.property.ModifierImpl.ModifierBuilder;
import org.antlr.v4.runtime.ParserRuleContext;
import org.eclipse.collections.api.list.ImmutableList;

public abstract class AbstractClassifier
        extends AbstractPackageableElement
        implements Classifier, TopLevelElementWithSourceCode
{
    private ImmutableList<DataTypeProperty> dataTypeProperties;
    private ImmutableList<Modifier>         modifiers;
    private ImmutableList<Interface>        interfaces;
    private ImmutableList<AssociationEndSignature> associationEndSignatures;

    protected AbstractClassifier(
            @Nonnull ParserRuleContext elementContext,
            @Nonnull Optional<Element> macroElement,
            @Nullable SourceCode sourceCode,
            @Nonnull ParserRuleContext nameContext,
            int ordinal,
            @Nonnull String packageName)
    {
        super(elementContext, macroElement, sourceCode, nameContext, ordinal, packageName);
    }

    @Nonnull
    @Override
    public ImmutableList<DataTypeProperty> getDeclaredDataTypeProperties()
    {
        return this.dataTypeProperties;
    }

    protected void setDataTypeProperties(@Nonnull ImmutableList<DataTypeProperty> dataTypeProperties)
    {
        if (this.dataTypeProperties != null)
        {
            throw new IllegalStateException();
        }
        this.dataTypeProperties = Objects.requireNonNull(dataTypeProperties);
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

    public abstract static class ClassifierBuilder<BuiltElement extends AbstractClassifier>
            extends PackageableElementBuilder<BuiltElement>
            implements TypeGetter, TopLevelElementBuilderWithSourceCode
    {
        protected ImmutableList<DataTypePropertyBuilder<?, ?, ?>> dataTypePropertyBuilders;
        protected ImmutableList<ModifierBuilder>                  modifierBuilders;
        protected ImmutableList<InterfaceBuilder>                 interfaceBuilders;
        protected ImmutableList<AssociationEndSignatureBuilder>   associationEndSignatureBuilders;

        protected ClassifierBuilder(
                @Nonnull ParserRuleContext elementContext,
                @Nonnull Optional<ElementBuilder<?>> macroElement,
                @Nullable SourceCodeBuilder sourceCode,
                @Nonnull ParserRuleContext nameContext,
                int ordinal,
                @Nonnull String packageName)
        {
            super(elementContext, macroElement, sourceCode, nameContext, ordinal, packageName);
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
            this.element.setDataTypeProperties(dataTypeProperties);
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

        @Override
        public abstract BuiltElement getType();
    }
}
