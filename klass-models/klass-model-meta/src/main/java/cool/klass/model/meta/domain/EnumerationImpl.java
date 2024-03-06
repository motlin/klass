package cool.klass.model.meta.domain;

import java.util.Optional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import cool.klass.model.meta.domain.EnumerationLiteralImpl.EnumerationLiteralBuilder;
import cool.klass.model.meta.domain.api.Element;
import cool.klass.model.meta.domain.api.Enumeration;
import cool.klass.model.meta.domain.api.EnumerationLiteral;
import cool.klass.model.meta.domain.api.source.SourceCode;
import cool.klass.model.meta.domain.api.source.SourceCode.SourceCodeBuilder;
import cool.klass.model.meta.domain.api.source.TopLevelElementWithSourceCode;
import cool.klass.model.meta.grammar.KlassParser.EnumerationDeclarationContext;
import cool.klass.model.meta.grammar.KlassParser.IdentifierContext;
import org.eclipse.collections.api.list.ImmutableList;

public final class EnumerationImpl
        extends AbstractPackageableElement
        implements Enumeration, TopLevelElementWithSourceCode
{
    private ImmutableList<EnumerationLiteral> enumerationLiterals;

    private EnumerationImpl(
            @Nonnull EnumerationDeclarationContext elementContext,
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
    public EnumerationDeclarationContext getElementContext()
    {
        return (EnumerationDeclarationContext) super.getElementContext();
    }

    @Override
    public ImmutableList<EnumerationLiteral> getEnumerationLiterals()
    {
        return this.enumerationLiterals;
    }

    private void setEnumerationLiterals(@Nonnull ImmutableList<EnumerationLiteral> enumerationLiterals)
    {
        this.enumerationLiterals = enumerationLiterals;
    }

    public static final class EnumerationBuilder
            extends PackageableElementBuilder<EnumerationImpl>
            implements DataTypeGetter, TopLevelElementBuilderWithSourceCode
    {
        private ImmutableList<EnumerationLiteralBuilder> enumerationLiteralBuilders;

        public EnumerationBuilder(
                @Nonnull EnumerationDeclarationContext elementContext,
                @Nonnull Optional<ElementBuilder<?>> macroElement,
                @Nullable SourceCodeBuilder sourceCode,
                int ordinal,
                @Nonnull IdentifierContext nameContext,
                @Nonnull String packageName)
        {
            super(elementContext, macroElement, sourceCode, ordinal, nameContext, packageName);
        }

        public void setEnumerationLiteralBuilders(@Nonnull ImmutableList<EnumerationLiteralBuilder> enumerationLiteralBuilders)
        {
            this.enumerationLiteralBuilders = enumerationLiteralBuilders;
        }

        @Override
        @Nonnull
        protected EnumerationImpl buildUnsafe()
        {
            return new EnumerationImpl(
                    (EnumerationDeclarationContext) this.elementContext,
                    this.macroElement.map(ElementBuilder::getElement),
                    this.sourceCode.build(),
                    this.ordinal,
                    this.getNameContext(),
                    this.packageName);
        }

        @Override
        protected void buildChildren()
        {
            ImmutableList<EnumerationLiteral> enumerationLiterals =
                    this.enumerationLiteralBuilders.collect(EnumerationLiteralBuilder::build);
            this.element.setEnumerationLiterals(enumerationLiterals);
        }

        @Override
        @Nonnull
        public EnumerationImpl getType()
        {
            return this.getElement();
        }
    }
}
