package cool.klass.model.meta.domain;

import java.util.Objects;
import java.util.Optional;

import javax.annotation.Nonnull;

import cool.klass.model.meta.domain.ClassModifierImpl.ClassModifierBuilder;
import cool.klass.model.meta.domain.InterfaceImpl.InterfaceBuilder;
import cool.klass.model.meta.domain.api.ClassModifier;
import cool.klass.model.meta.domain.api.Classifier;
import cool.klass.model.meta.domain.api.Element;
import cool.klass.model.meta.domain.api.Interface;
import cool.klass.model.meta.domain.api.property.DataTypeProperty;
import cool.klass.model.meta.domain.property.AbstractDataTypeProperty.DataTypePropertyBuilder;
import org.antlr.v4.runtime.ParserRuleContext;
import org.eclipse.collections.api.list.ImmutableList;

public abstract class AbstractClassifier
        extends AbstractPackageableElement
        implements TopLevelElement, Classifier
{
    private ImmutableList<DataTypeProperty> dataTypeProperties;
    private ImmutableList<ClassModifier>    classModifiers;
    private ImmutableList<Interface>        interfaces;

    protected AbstractClassifier(
            @Nonnull ParserRuleContext elementContext,
            @Nonnull Optional<Element> macroElement,
            @Nonnull ParserRuleContext nameContext,
            @Nonnull String name,
            int ordinal,
            @Nonnull String packageName)
    {
        super(elementContext, macroElement, nameContext, name, ordinal, packageName);
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
    public ImmutableList<ClassModifier> getDeclaredClassModifiers()
    {
        return this.classModifiers;
    }

    protected void setClassModifiers(@Nonnull ImmutableList<ClassModifier> classModifiers)
    {
        if (this.classModifiers != null)
        {
            throw new IllegalStateException();
        }
        this.classModifiers = Objects.requireNonNull(classModifiers);
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

    public abstract static class ClassifierBuilder<BuiltElement extends AbstractClassifier>
            extends PackageableElementBuilder<BuiltElement>
            implements TypeGetter, TopLevelElementBuilder
    {
        protected ImmutableList<DataTypePropertyBuilder<?, ?, ?>> dataTypePropertyBuilders;
        protected ImmutableList<ClassModifierBuilder>             classModifierBuilders;
        protected ImmutableList<InterfaceBuilder>                 interfaceBuilders;

        protected ClassifierBuilder(
                @Nonnull ParserRuleContext elementContext,
                @Nonnull Optional<ElementBuilder<?>> macroElement,
                @Nonnull ParserRuleContext nameContext,
                @Nonnull String name,
                int ordinal,
                @Nonnull String packageName)
        {
            super(elementContext, macroElement, nameContext, name, ordinal, packageName);
        }

        public void setDataTypePropertyBuilders(@Nonnull ImmutableList<DataTypePropertyBuilder<?, ?, ?>> dataTypePropertyBuilders)
        {
            if (this.dataTypePropertyBuilders != null)
            {
                throw new IllegalStateException();
            }
            this.dataTypePropertyBuilders = Objects.requireNonNull(dataTypePropertyBuilders);
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
        protected void buildChildren()
        {
            ImmutableList<ClassModifier> classModifiers = this.classModifierBuilders.collect(ClassModifierBuilder::build);
            this.element.setClassModifiers(classModifiers);

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
        }

        @Override
        public abstract BuiltElement getType();
    }
}
