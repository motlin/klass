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
import org.antlr.v4.runtime.ParserRuleContext;
import org.eclipse.collections.api.list.ImmutableList;

public final class EnumerationImpl
        extends AbstractPackageableElement
        implements Enumeration, TopLevelElementWithSourceCode
{
    private ImmutableList<EnumerationLiteral> enumerationLiterals;

    private EnumerationImpl(
            @Nonnull ParserRuleContext elementContext,
            @Nonnull Optional<Element> macroElement,
            @Nullable SourceCode sourceCode,
            @Nonnull ParserRuleContext nameContext,
            int ordinal,
            @Nonnull String packageName)
    {
        super(elementContext, macroElement, sourceCode, nameContext, ordinal, packageName);
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
                @Nonnull ParserRuleContext elementContext,
                @Nonnull Optional<ElementBuilder<?>> macroElement,
                @Nullable SourceCodeBuilder sourceCode,
                @Nonnull ParserRuleContext nameContext,
                int ordinal,
                @Nonnull String packageName)
        {
            super(elementContext, macroElement, sourceCode, nameContext, ordinal, packageName);
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
                    this.elementContext,
                    this.macroElement.map(ElementBuilder::getElement),
                    this.sourceCode.build(),
                    this.nameContext,
                    this.ordinal,
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
