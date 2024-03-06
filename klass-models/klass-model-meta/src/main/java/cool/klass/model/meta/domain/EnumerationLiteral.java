package cool.klass.model.meta.domain;

import javax.annotation.Nonnull;

import cool.klass.model.meta.domain.Enumeration.EnumerationBuilder;
import org.antlr.v4.runtime.ParserRuleContext;

public final class EnumerationLiteral extends TypedElement<Enumeration>
{
    private final String prettyName;

    private EnumerationLiteral(
            @Nonnull ParserRuleContext elementContext,
            @Nonnull ParserRuleContext nameContext,
            @Nonnull String name,
            int ordinal,
            @Nonnull Enumeration enumeration,
            String prettyName)
    {
        super(elementContext, nameContext, name, ordinal, enumeration);
        this.prettyName = prettyName;
    }

    public String getPrettyName()
    {
        return this.prettyName;
    }

    public static class EnumerationLiteralBuilder extends NamedElementBuilder
    {
        private final String             prettyName;
        private final EnumerationBuilder enumerationBuilder;

        private EnumerationLiteral enumerationLiteral;

        public EnumerationLiteralBuilder(
                @Nonnull ParserRuleContext elementContext,
                @Nonnull ParserRuleContext nameContext,
                @Nonnull String name,
                int ordinal,
                String prettyName,
                EnumerationBuilder enumerationBuilder)
        {
            super(elementContext, nameContext, name, ordinal);
            this.prettyName = prettyName;
            this.enumerationBuilder = enumerationBuilder;
        }

        public EnumerationLiteral build()
        {
            if (this.enumerationLiteral != null)
            {
                throw new IllegalStateException();
            }

            this.enumerationLiteral = new EnumerationLiteral(
                    this.elementContext,
                    this.nameContext,
                    this.name,
                    this.ordinal,
                    this.enumerationBuilder.getEnumeration(),
                    this.prettyName);
            return this.enumerationLiteral;
        }
    }
}
