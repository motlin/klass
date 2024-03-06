package cool.klass.model.meta.domain;

import cool.klass.model.meta.domain.Enumeration.EnumerationBuilder;
import org.antlr.v4.runtime.ParserRuleContext;

public class EnumerationLiteral extends TypedElement<Enumeration>
{
    private final String prettyName;

    protected EnumerationLiteral(
            ParserRuleContext elementContext,
            ParserRuleContext nameContext,
            String name,
            ParserRuleContext enumerationContext,
            Enumeration enumeration,
            String prettyName)
    {
        super(elementContext, nameContext, name, enumerationContext, enumeration);
        this.prettyName = prettyName;
    }

    public String getPrettyName()
    {
        return this.prettyName;
    }

    public static class EnumerationLiteralBuilder extends NamedElementBuilder
    {
        private final String             prettyName;
        private final EnumerationBuilder owningEnumerationBuilder;

        public EnumerationLiteralBuilder(
                ParserRuleContext elementContext,
                ParserRuleContext nameContext,
                String name,
                String prettyName,
                EnumerationBuilder owningEnumerationBuilder)
        {
            super(elementContext, nameContext, name);
            this.prettyName = prettyName;
            this.owningEnumerationBuilder = owningEnumerationBuilder;
        }

        public EnumerationLiteral build(ParserRuleContext enumerationContext, Enumeration enumeration)
        {
            return new EnumerationLiteral(
                    this.elementContext,
                    this.nameContext,
                    this.name,
                    enumerationContext,
                    enumeration,
                    this.prettyName);
        }
    }
}
