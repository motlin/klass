package cool.klass.model.meta.domain.property;

import java.util.Optional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import cool.klass.model.meta.domain.KlassImpl;
import cool.klass.model.meta.domain.KlassImpl.KlassBuilder;
import cool.klass.model.meta.domain.api.Element;
import cool.klass.model.meta.domain.api.Klass;
import cool.klass.model.meta.domain.api.Multiplicity;
import cool.klass.model.meta.domain.api.property.ParameterizedProperty;
import cool.klass.model.meta.domain.api.source.SourceCode;
import cool.klass.model.meta.domain.api.source.SourceCode.SourceCodeBuilder;
import cool.klass.model.meta.grammar.KlassParser.IdentifierContext;
import org.antlr.v4.runtime.ParserRuleContext;

// TODO: Super class for reference-type-property?
public final class ParameterizedPropertyImpl
        extends ReferencePropertyImpl<KlassImpl>
        implements ParameterizedProperty
{
    // @Nonnull
    // private final ImmutableList<ParameterizedPropertyModifier> parameterizedPropertyModifiers;

    private ParameterizedPropertyImpl(
            @Nonnull ParserRuleContext elementContext,
            @Nonnull Optional<Element> macroElement,
            @Nullable SourceCode sourceCode,
            int ordinal,
            @Nonnull IdentifierContext nameContext,
            @Nonnull KlassImpl type,
            @Nonnull KlassImpl owningKlass,
            @Nonnull Multiplicity multiplicity)
    {
        super(
                elementContext,
                macroElement,
                sourceCode,
                ordinal,
                nameContext,
                type,
                owningKlass,
                multiplicity);
    }

    @Nonnull
    @Override
    public Klass getOwningClassifier()
    {
        return (Klass) super.getOwningClassifier();
    }

    public static final class ParameterizedPropertyBuilder
            extends ReferencePropertyBuilder<KlassImpl, KlassBuilder, ParameterizedPropertyImpl>
    {
        public ParameterizedPropertyBuilder(
                @Nonnull ParserRuleContext elementContext,
                @Nonnull Optional<ElementBuilder<?>> macroElement,
                @Nullable SourceCodeBuilder sourceCode,
                int ordinal,
                @Nonnull IdentifierContext nameContext,
                @Nonnull KlassBuilder type,
                @Nonnull KlassBuilder owningKlassBuilder,
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
        protected ParameterizedPropertyImpl buildUnsafe()
        {
            return new ParameterizedPropertyImpl(
                    this.elementContext,
                    this.macroElement.map(ElementBuilder::getElement),
                    this.sourceCode.build(),
                    this.ordinal,
                    this.getNameContext(),
                    this.typeBuilder.getElement(),
                    (KlassImpl) this.owningClassifierBuilder.getElement(),
                    this.multiplicity);
        }
    }
}
