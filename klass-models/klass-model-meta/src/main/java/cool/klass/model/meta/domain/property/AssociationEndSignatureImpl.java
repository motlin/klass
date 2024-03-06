package cool.klass.model.meta.domain.property;

import java.util.Optional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import cool.klass.model.meta.domain.AbstractClassifier;
import cool.klass.model.meta.domain.AbstractClassifier.ClassifierBuilder;
import cool.klass.model.meta.domain.api.Element;
import cool.klass.model.meta.domain.api.Multiplicity;
import cool.klass.model.meta.domain.api.property.AssociationEndSignature;
import cool.klass.model.meta.domain.api.source.SourceCode;
import cool.klass.model.meta.domain.api.source.SourceCode.SourceCodeBuilder;
import cool.klass.model.meta.grammar.KlassParser.IdentifierContext;
import org.antlr.v4.runtime.ParserRuleContext;

// TODO: Super class for reference-type-property?
public final class AssociationEndSignatureImpl
        extends ReferencePropertyImpl<AbstractClassifier>
        implements AssociationEndSignature
{
    private AssociationEndSignatureImpl(
            @Nonnull ParserRuleContext elementContext,
            @Nonnull Optional<Element> macroElement,
            @Nullable SourceCode sourceCode,
            int ordinal,
            @Nonnull IdentifierContext nameContext,
            @Nonnull AbstractClassifier type,
            @Nonnull AbstractClassifier owningClassifier,
            @Nonnull Multiplicity multiplicity)
    {
        super(
                elementContext,
                macroElement,
                sourceCode,
                ordinal,
                nameContext,
                type,
                owningClassifier,
                multiplicity);
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
                @Nullable SourceCodeBuilder sourceCode,
                int ordinal,
                @Nonnull IdentifierContext nameContext,
                @Nonnull ClassifierBuilder<?> type,
                @Nonnull ClassifierBuilder<?> owningKlassBuilder,
                @Nonnull Multiplicity multiplicity)
        {
            super(
                    elementContext,
                    macroElement,
                    sourceCode,
                    ordinal,
                    nameContext,
                    type,
                    owningKlassBuilder,
                    multiplicity);
        }

        @Override
        @Nonnull
        protected AssociationEndSignatureImpl buildUnsafe()
        {
            return new AssociationEndSignatureImpl(
                    this.elementContext,
                    this.macroElement.map(ElementBuilder::getElement),
                    this.sourceCode.build(),
                    this.ordinal,
                    this.getNameContext(),
                    this.typeBuilder.getElement(),
                    this.owningClassifierBuilder.getElement(),
                    this.multiplicity);
        }
    }
}
