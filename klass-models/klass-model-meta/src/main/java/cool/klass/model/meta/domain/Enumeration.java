package cool.klass.model.meta.domain;

import java.util.Objects;

import javax.annotation.Nonnull;

import cool.klass.model.meta.domain.EnumerationLiteral.EnumerationLiteralBuilder;
import org.antlr.v4.runtime.ParserRuleContext;
import org.eclipse.collections.api.list.ImmutableList;

public final class Enumeration extends DataType
{
    private ImmutableList<EnumerationLiteral> enumerationLiterals;

    private Enumeration(
            @Nonnull ParserRuleContext elementContext,
            @Nonnull ParserRuleContext nameContext,
            @Nonnull String name,
            @Nonnull String packageName)
    {
        super(elementContext, nameContext, name, packageName);
    }

    public ImmutableList<EnumerationLiteral> getEnumerationLiterals()
    {
        return this.enumerationLiterals;
    }

    private void setEnumerationLiterals(ImmutableList<EnumerationLiteral> enumerationLiterals)
    {
        this.enumerationLiterals = enumerationLiterals;
    }

    public static class EnumerationBuilder extends DataTypeBuilder<Enumeration>
    {
        private ImmutableList<EnumerationLiteralBuilder> enumerationLiteralBuilders;
        private Enumeration                              enumeration;

        public EnumerationBuilder(
                @Nonnull ParserRuleContext elementContext,
                @Nonnull ParserRuleContext nameContext,
                @Nonnull String name,
                @Nonnull String packageName)
        {
            super(elementContext, nameContext, name, packageName);
        }

        public Enumeration build()
        {
            if (this.enumeration != null)
            {
                throw new IllegalStateException();
            }

            this.enumeration = new Enumeration(
                    this.elementContext,
                    this.nameContext,
                    this.name,
                    this.packageName);

            ImmutableList<EnumerationLiteral> enumerationLiterals = this.enumerationLiteralBuilders.collect(
                    EnumerationLiteralBuilder::build);

            this.enumeration.setEnumerationLiterals(enumerationLiterals);
            return this.enumeration;
        }

        @Nonnull
        public Enumeration getEnumeration()
        {
            return Objects.requireNonNull(this.enumeration);
        }

        public void setEnumerationLiteralBuilders(ImmutableList<EnumerationLiteralBuilder> enumerationLiteralBuilders)
        {
            this.enumerationLiteralBuilders = enumerationLiteralBuilders;
        }
    }
}
