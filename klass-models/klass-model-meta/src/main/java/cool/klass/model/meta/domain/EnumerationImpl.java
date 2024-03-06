package cool.klass.model.meta.domain;

import javax.annotation.Nonnull;

import cool.klass.model.meta.domain.EnumerationLiteralImpl.EnumerationLiteralBuilder;
import cool.klass.model.meta.domain.api.Enumeration;
import cool.klass.model.meta.domain.api.EnumerationLiteral;
import org.antlr.v4.runtime.ParserRuleContext;
import org.eclipse.collections.api.list.ImmutableList;

public final class EnumerationImpl extends AbstractPackageableElement implements TopLevelElement, Enumeration
{
    private ImmutableList<EnumerationLiteral> enumerationLiterals;

    private EnumerationImpl(
            @Nonnull ParserRuleContext elementContext,
            boolean inferred,
            @Nonnull ParserRuleContext nameContext,
            @Nonnull String name,
            int ordinal,
            @Nonnull String packageName)
    {
        super(elementContext, inferred, nameContext, name, ordinal, packageName);
    }

    @Override
    public ImmutableList<EnumerationLiteral> getEnumerationLiterals()
    {
        return this.enumerationLiterals;
    }

    private void setEnumerationLiterals(ImmutableList<EnumerationLiteral> enumerationLiterals)
    {
        this.enumerationLiterals = enumerationLiterals;
    }

    public static final class EnumerationBuilder
            extends PackageableElementBuilder<EnumerationImpl>
            implements DataTypeGetter, TopLevelElementBuilder
    {
        private ImmutableList<EnumerationLiteralBuilder> enumerationLiteralBuilders;

        public EnumerationBuilder(
                @Nonnull ParserRuleContext elementContext,
                boolean inferred,
                @Nonnull ParserRuleContext nameContext,
                @Nonnull String name,
                int ordinal,
                @Nonnull String packageName)
        {
            super(elementContext, inferred, nameContext, name, ordinal, packageName);
        }

        public void setEnumerationLiteralBuilders(ImmutableList<EnumerationLiteralBuilder> enumerationLiteralBuilders)
        {
            this.enumerationLiteralBuilders = enumerationLiteralBuilders;
        }

        @Override
        @Nonnull
        protected EnumerationImpl buildUnsafe()
        {
            return new EnumerationImpl(
                    this.elementContext,
                    this.inferred,
                    this.nameContext,
                    this.name,
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
