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
import cool.klass.model.meta.domain.api.value.ThisMemberReferencePath;
import cool.klass.model.meta.domain.property.AbstractDataTypeProperty;
import cool.klass.model.meta.domain.property.AbstractDataTypeProperty.DataTypePropertyBuilder;
import cool.klass.model.meta.domain.property.AssociationEndImpl.AssociationEndBuilder;
import org.antlr.v4.runtime.ParserRuleContext;
import org.eclipse.collections.api.list.ImmutableList;

public final class ThisMemberReferencePathImpl
        extends AbstractMemberReferencePath
        implements ThisMemberReferencePath
{
    private ThisMemberReferencePathImpl(
            @Nonnull ParserRuleContext elementContext,
            @Nonnull Optional<Element> macroElement,
            @Nullable SourceCode sourceCode,
            @Nonnull KlassImpl klass,
            @Nonnull ImmutableList<AssociationEnd> associationEnds,
            @Nonnull AbstractDataTypeProperty<?> property)
    {
        super(elementContext, macroElement, sourceCode, klass, associationEnds, property);
    }

    public static final class ThisMemberReferencePathBuilder
            extends AbstractMemberReferencePathBuilder<ThisMemberReferencePathImpl>
    {
        public ThisMemberReferencePathBuilder(
                @Nonnull ParserRuleContext elementContext,
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
        protected ThisMemberReferencePathImpl buildUnsafe()
        {
            return new ThisMemberReferencePathImpl(
                    this.elementContext,
                    this.macroElement.map(ElementBuilder::getElement),
                    this.sourceCode.build(),
                    this.klassBuilder.getElement(),
                    this.associationEndBuilders.collect(AssociationEndBuilder::getElement),
                    this.propertyBuilder.getElement());
        }
    }
}
