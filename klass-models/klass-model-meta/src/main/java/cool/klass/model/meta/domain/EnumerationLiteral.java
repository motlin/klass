package cool.klass.model.meta.domain;

import cool.klass.model.meta.domain.Enumeration.EnumerationBuilder;
import org.antlr.v4.runtime.ParserRuleContext;

public class EnumerationLiteral extends TypedElement
{
    protected EnumerationLiteral(String name, Type type)
    {
        super(name, type);
    }

    public static class EnumerationLiteralBuilder extends NamedElementBuilder
    {
        private final String             literalName;
        private final String             prettyName;
        private final EnumerationBuilder enumerationBuilder;

        public EnumerationLiteralBuilder(
                ParserRuleContext elementContext,
                ParserRuleContext nameContext,
                String literalName,
                String prettyName,
                EnumerationBuilder enumerationBuilder)
        {
            super(elementContext, nameContext);
            this.literalName = literalName;
            this.prettyName = prettyName;
            this.enumerationBuilder = enumerationBuilder;
        }

        public String getLiteralName()
        {
            return this.literalName;
        }

        public String getPrettyName()
        {
            return this.prettyName;
        }

        public EnumerationBuilder getEnumerationBuilder()
        {
            return this.enumerationBuilder;
        }
    }
}
