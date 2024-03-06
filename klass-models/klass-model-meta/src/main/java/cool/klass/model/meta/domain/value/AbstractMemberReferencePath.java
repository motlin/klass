package cool.klass.model.meta.domain.value;

import java.util.Objects;
import java.util.Optional;

import javax.annotation.Nonnull;

import cool.klass.model.meta.domain.KlassImpl;
import cool.klass.model.meta.domain.KlassImpl.KlassBuilder;
import cool.klass.model.meta.domain.api.Element;
import cool.klass.model.meta.domain.api.property.AssociationEnd;
import cool.klass.model.meta.domain.api.value.MemberReferencePath;
import cool.klass.model.meta.domain.property.AbstractDataTypeProperty;
import cool.klass.model.meta.domain.property.AbstractDataTypeProperty.DataTypePropertyBuilder;
import cool.klass.model.meta.domain.property.AssociationEndImpl.AssociationEndBuilder;
import org.antlr.v4.runtime.ParserRuleContext;
import org.eclipse.collections.api.list.ImmutableList;

public abstract class AbstractMemberReferencePath extends AbstractExpressionValue implements MemberReferencePath
{
    @Nonnull
    private final KlassImpl                     klass;
    @Nonnull
    private final ImmutableList<AssociationEnd> associationEnds;
    @Nonnull
    private final AbstractDataTypeProperty<?>   property;

    protected AbstractMemberReferencePath(
            @Nonnull ParserRuleContext elementContext,
            Optional<Element> macroElement,
            @Nonnull KlassImpl klass,
            @Nonnull ImmutableList<AssociationEnd> associationEnds,
            @Nonnull AbstractDataTypeProperty<?> property)
    {
        super(elementContext, macroElement);
        this.klass = Objects.requireNonNull(klass);
        this.associationEnds = Objects.requireNonNull(associationEnds);
        this.property = Objects.requireNonNull(property);
    }

    @Override
    @Nonnull
    public KlassImpl getKlass()
    {
        return this.klass;
    }

    @Override
    @Nonnull
    public ImmutableList<AssociationEnd> getAssociationEnds()
    {
        return this.associationEnds;
    }

    @Override
    @Nonnull
    public AbstractDataTypeProperty<?> getProperty()
    {
        return this.property;
    }

    public abstract static class AbstractMemberReferencePathBuilder<BuiltElement extends AbstractMemberReferencePath> extends AbstractExpressionValueBuilder<BuiltElement>
    {
        @Nonnull
        protected final KlassBuilder                         klassBuilder;
        @Nonnull
        protected final ImmutableList<AssociationEndBuilder> associationEndBuilders;
        @Nonnull
        protected final DataTypePropertyBuilder<?, ?, ?>        propertyBuilder;

        protected AbstractMemberReferencePathBuilder(
                @Nonnull ParserRuleContext elementContext,
                Optional<ElementBuilder<?>> macroElement,
                @Nonnull KlassBuilder klassBuilder,
                @Nonnull ImmutableList<AssociationEndBuilder> associationEndBuilders,
                @Nonnull DataTypePropertyBuilder<?, ?, ?> propertyBuilder)
        {
            super(elementContext, macroElement);
            this.klassBuilder = Objects.requireNonNull(klassBuilder);
            this.associationEndBuilders = Objects.requireNonNull(associationEndBuilders);
            this.propertyBuilder = Objects.requireNonNull(propertyBuilder);
        }
    }
}
