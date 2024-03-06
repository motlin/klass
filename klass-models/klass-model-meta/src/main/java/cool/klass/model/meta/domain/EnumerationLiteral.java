package cool.klass.model.meta.domain;

import org.antlr.v4.runtime.ParserRuleContext;

public final class EnumerationLiteral extends TypedElement<Enumeration>
{
    private final String prettyName;

    private EnumerationLiteral(
            ParserRuleContext elementContext,
            ParserRuleContext nameContext,
            String name,
            ParserRuleContext enumerationContext,
            Enumeration enumeration,
            String prettyName)
    {
        super(elementContext, nameContext, name, enumeration);
        this.prettyName = prettyName;
    }

    public String getPrettyName()
    {
        return this.prettyName;
    }

    public static class EnumerationLiteralBuilder extends NamedElementBuilder
    {
        public static final EnumerationLiteralBuilder AMBIGUOUS = new EnumerationLiteralBuilder(
                NO_CONTEXT,
                NO_CONTEXT,
                "ambiguous enumeration literal",
                null);

        private final String prettyName;

        private EnumerationLiteral enumerationLiteral;

        public EnumerationLiteralBuilder(
                ParserRuleContext elementContext,
                ParserRuleContext nameContext,
                String name,
                String prettyName)
        {
            super(elementContext, nameContext, name);
            this.prettyName = prettyName;
        }

        public String getPrettyName()
        {
            return this.prettyName;
        }

        public EnumerationLiteral build(ParserRuleContext enumerationContext, Enumeration enumeration)
        {
            if (this.enumerationLiteral != null)
            {
                throw new IllegalStateException();
            }

            this.enumerationLiteral = new EnumerationLiteral(
                    this.elementContext,
                    this.nameContext,
                    this.name,
                    enumerationContext,
                    enumeration,
                    this.prettyName);
            return this.enumerationLiteral;
        }

        public EnumerationLiteral getEnumerationLiteral()
        {
            return this.enumerationLiteral;
        }
    }
}
