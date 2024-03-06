package cool.klass.model.meta.domain;

import java.util.Objects;

import javax.annotation.Nonnull;

import cool.klass.model.meta.domain.EnumerationLiteralImpl.EnumerationLiteralBuilder;
import cool.klass.model.meta.domain.api.DataType;
import cool.klass.model.meta.domain.api.Enumeration;
import cool.klass.model.meta.domain.api.EnumerationLiteral;
import org.antlr.v4.runtime.ParserRuleContext;
import org.eclipse.collections.api.list.ImmutableList;

public final class EnumerationImpl extends AbstractPackageableElement implements DataType, TopLevelElement, Enumeration
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

    public static class EnumerationBuilder extends PackageableElementBuilder implements DataTypeGetter, TopLevelElementBuilder
    {
        private ImmutableList<EnumerationLiteralBuilder> enumerationLiteralBuilders;
        private EnumerationImpl                          enumeration;

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

        public EnumerationImpl build()
        {
            if (this.enumeration != null)
            {
                throw new IllegalStateException();
            }

            this.enumeration = new EnumerationImpl(
                    this.elementContext,
                    this.inferred,
                    this.nameContext,
                    this.name,
                    this.ordinal,
                    this.packageName);

            ImmutableList<EnumerationLiteral> enumerationLiterals =
                    this.enumerationLiteralBuilders.collect(EnumerationLiteralBuilder::build);

            this.enumeration.setEnumerationLiterals(enumerationLiterals);
            return this.enumeration;
        }

        @Override
        @Nonnull
        public EnumerationImpl getType()
        {
            return Objects.requireNonNull(this.enumeration);
        }

        @Override
        @Nonnull
        public EnumerationImpl getElement()
        {
            return Objects.requireNonNull(this.enumeration);
        }

        public void setEnumerationLiteralBuilders(ImmutableList<EnumerationLiteralBuilder> enumerationLiteralBuilders)
        {
            this.enumerationLiteralBuilders = enumerationLiteralBuilders;
        }
    }
}
