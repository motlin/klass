package cool.klass.model.meta.domain;

import java.util.Objects;

import cool.klass.model.meta.domain.EnumerationLiteral.EnumerationLiteralBuilder;
import org.antlr.v4.runtime.ParserRuleContext;
import org.eclipse.collections.api.list.ImmutableList;

public final class Enumeration extends DataType
{
    private ImmutableList<EnumerationLiteral> enumerationLiterals;

    private Enumeration(
            ParserRuleContext elementContext,
            ParserRuleContext nameContext,
            String name,
            String packageName)
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
        private final ImmutableList<EnumerationLiteralBuilder> enumerationLiteralBuilders;
        private       Enumeration                              enumeration;

        public EnumerationBuilder(
                ParserRuleContext elementContext,
                ParserRuleContext nameContext,
                String name,
                String packageName,
                ImmutableList<EnumerationLiteralBuilder> enumerationLiteralBuilders)
        {
            super(elementContext, nameContext, name, packageName);
            this.enumerationLiteralBuilders = enumerationLiteralBuilders;
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
                    each -> each.build(this.getElementContext(), this.enumeration));

            this.enumeration.setEnumerationLiterals(enumerationLiterals);
            return this.enumeration;
        }

        public Enumeration getEnumeration()
        {
            return Objects.requireNonNull(this.enumeration);
        }
    }
}
