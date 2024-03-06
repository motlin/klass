package cool.klass.model.meta.domain;

import cool.klass.model.meta.domain.EnumerationLiteral.EnumerationLiteralBuilder;
import org.antlr.v4.runtime.ParserRuleContext;
import org.eclipse.collections.api.list.ImmutableList;

public class Enumeration extends DataType
{
    private ImmutableList<EnumerationLiteral> enumerationLiterals;

    public Enumeration(
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

    public static class EnumerationBuilder extends DataTypeBuilder
    {
        private ImmutableList<EnumerationLiteralBuilder> enumerationLiteralBuilders;

        public EnumerationBuilder(
                ParserRuleContext elementContext,
                ParserRuleContext nameContext,
                String name,
                String packageName)
        {
            super(elementContext, nameContext, name, packageName);
        }

        public void setEnumerationLiterals(ImmutableList<EnumerationLiteralBuilder> enumerationLiteralBuilders)
        {
            this.enumerationLiteralBuilders = enumerationLiteralBuilders;
        }

        public Enumeration build()
        {
            Enumeration enumeration = new Enumeration(
                    this.elementContext,
                    this.nameContext,
                    this.name,
                    this.packageName);

            ImmutableList<EnumerationLiteral> enumerationLiterals = this.enumerationLiteralBuilders.collect(
                    each -> each.build(this.getElementContext(), enumeration));

            enumeration.setEnumerationLiterals(enumerationLiterals);
            return enumeration;
        }
    }
}
