package cool.klass.model.meta.domain.value;

import java.util.Optional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import cool.klass.model.meta.domain.KlassImpl;
import cool.klass.model.meta.domain.KlassImpl.KlassBuilder;
import cool.klass.model.meta.domain.api.Element;
import cool.klass.model.meta.domain.api.property.AssociationEnd;
import cool.klass.model.meta.domain.api.source.SourceCode;
import cool.klass.model.meta.domain.api.source.SourceCode.SourceCodeBuilder;
import cool.klass.model.meta.domain.api.source.value.TypeMemberReferencePathWithSourceCode;
import cool.klass.model.meta.domain.property.AbstractDataTypeProperty;
import cool.klass.model.meta.domain.property.AbstractDataTypeProperty.DataTypePropertyBuilder;
import cool.klass.model.meta.domain.property.AssociationEndImpl.AssociationEndBuilder;
import cool.klass.model.meta.grammar.KlassParser.TypeMemberReferencePathContext;
import org.eclipse.collections.api.list.ImmutableList;

public final class TypeMemberReferencePathImpl
        extends AbstractMemberReferencePath
        implements TypeMemberReferencePathWithSourceCode
{
    private TypeMemberReferencePathImpl(
            @Nonnull TypeMemberReferencePathContext elementContext,
            @Nonnull Optional<Element> macroElement,
            @Nullable SourceCode sourceCode,
            @Nonnull KlassImpl klass,
            @Nonnull ImmutableList<AssociationEnd> associationEnds,
            @Nonnull AbstractDataTypeProperty<?> property)
    {
        super(elementContext, macroElement, sourceCode, klass, associationEnds, property);
    }

    @Nonnull
    @Override
    public TypeMemberReferencePathContext getElementContext()
    {
        return (TypeMemberReferencePathContext) super.getElementContext();
    }

    public static final class TypeMemberReferencePathBuilder
            extends AbstractMemberReferencePathBuilder<TypeMemberReferencePathImpl>
    {
        public TypeMemberReferencePathBuilder(
                @Nonnull TypeMemberReferencePathContext elementContext,
                @Nonnull Optional<ElementBuilder<?>> macroElement,
                @Nullable SourceCodeBuilder sourceCode,
                @Nonnull KlassBuilder klassBuilder,
                @Nonnull ImmutableList<AssociationEndBuilder> associationEndBuilders,
                @Nonnull DataTypePropertyBuilder<?, ?, ?> propertyBuilder)
        {
            super(elementContext, macroElement, sourceCode, klassBuilder, associationEndBuilders, propertyBuilder);
        }

        @Override
        @Nonnull
        protected TypeMemberReferencePathImpl buildUnsafe()
        {
            return new TypeMemberReferencePathImpl(
                    (TypeMemberReferencePathContext) this.elementContext,
                    this.macroElement.map(ElementBuilder::getElement),
                    this.sourceCode.build(),
                    this.klassBuilder.getElement(),
                    this.associationEndBuilders.collect(AssociationEndBuilder::getElement),
                    this.propertyBuilder.getElement());
        }
    }
}
