package cool.klass.model.meta.domain.property;

import java.util.Optional;

import javax.annotation.Nonnull;

import cool.klass.model.meta.domain.AbstractClassifier;
import cool.klass.model.meta.domain.AbstractClassifier.ClassifierBuilder;
import cool.klass.model.meta.domain.api.Element;
import cool.klass.model.meta.domain.api.Multiplicity;
import cool.klass.model.meta.domain.api.modifier.AssociationEndModifier;
import cool.klass.model.meta.domain.api.property.AssociationEndSignature;
import cool.klass.model.meta.domain.api.source.SourceCode;
import cool.klass.model.meta.domain.api.source.SourceCode.SourceCodeBuilder;
import org.antlr.v4.runtime.ParserRuleContext;
import org.eclipse.collections.api.list.ImmutableList;

// TODO: Super class for reference-type-property?
public final class AssociationEndSignatureImpl
        extends ReferencePropertyImpl<AbstractClassifier>
        implements AssociationEndSignature
{
    private ImmutableList<AssociationEndModifier> associationEndModifiers;

    private AssociationEndSignatureImpl(
            @Nonnull ParserRuleContext elementContext,
            @Nonnull Optional<Element> macroElement,
            @Nonnull Optional<SourceCode> sourceCode,
            @Nonnull ParserRuleContext nameContext,
            @Nonnull String name,
            int ordinal,
            @Nonnull AbstractClassifier type,
            @Nonnull AbstractClassifier owningClassifier,
            @Nonnull Multiplicity multiplicity,
            boolean owned)
    {
        super(
                elementContext,
                macroElement,
                sourceCode,
                nameContext,
                name,
                ordinal,
                type,
                owningClassifier,
                multiplicity,
                owned);
    }

    @Override
    public String toString()
    {
        return String.format(
                "%s.%s: %s[%s]",
                this.getOwningClassifier().getName(),
                this.getName(),
                this.getType().getName(),
                this.multiplicity.getPrettyName());
    }

    public static final class AssociationEndSignatureBuilder
            extends ReferencePropertyBuilder<AbstractClassifier, ClassifierBuilder<?>, AssociationEndSignatureImpl>
    {
        public AssociationEndSignatureBuilder(
                @Nonnull ParserRuleContext elementContext,
                @Nonnull Optional<ElementBuilder<?>> macroElement,
                @Nonnull Optional<SourceCodeBuilder> sourceCode,
                @Nonnull ParserRuleContext nameContext,
                @Nonnull String name,
                int ordinal,
                @Nonnull ClassifierBuilder<?> type,
                @Nonnull ClassifierBuilder<?> owningKlassBuilder,
                @Nonnull Multiplicity multiplicity,
                boolean isOwned)
        {
            super(
                    elementContext,
                    macroElement,
                    sourceCode,
                    nameContext,
                    name,
                    ordinal,
                    type,
                    owningKlassBuilder,
                    multiplicity,
                    isOwned);
        }

        @Override
        @Nonnull
        protected AssociationEndSignatureImpl buildUnsafe()
        {
            return new AssociationEndSignatureImpl(
                    this.elementContext,
                    this.macroElement.map(ElementBuilder::getElement),
                    this.sourceCode.map(SourceCodeBuilder::build),
                    this.nameContext,
                    this.name,
                    this.ordinal,
                    this.typeBuilder.getElement(),
                    this.owningClassifierBuilder.getElement(),
                    this.multiplicity,
                    this.isOwned);
        }
    }
}
