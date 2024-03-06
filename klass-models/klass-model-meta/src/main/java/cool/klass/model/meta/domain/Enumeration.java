package cool.klass.model.meta.domain;

import cool.klass.model.meta.domain.EnumerationLiteral.EnumerationLiteralBuilder;
import org.antlr.v4.runtime.ParserRuleContext;
import org.eclipse.collections.api.list.ImmutableList;

public class Enumeration extends DataType
{
    public Enumeration(String name, String packageName)
    {
        super(name, packageName);
    }

    public static class EnumerationBuilder extends DataTypeBuilder
    {
        private ImmutableList<EnumerationLiteralBuilder> enumerationLiteralBuilders;

        public EnumerationBuilder(
                ParserRuleContext elementContext,
                ParserRuleContext nameContext,
                String packageName)
        {
            super(elementContext, nameContext, packageName);
        }

        public void setEnumerationLiterals(ImmutableList<EnumerationLiteralBuilder> enumerationLiteralBuilders)
        {
            this.enumerationLiteralBuilders = enumerationLiteralBuilders;
        }

        public ImmutableList<EnumerationLiteralBuilder> getEnumerationLiteralBuilders()
        {
            return this.enumerationLiteralBuilders;
        }
    }
}
