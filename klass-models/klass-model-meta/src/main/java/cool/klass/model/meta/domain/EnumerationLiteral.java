package cool.klass.model.meta.domain;

import java.util.Objects;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import cool.klass.model.meta.domain.Enumeration.EnumerationBuilder;
import org.antlr.v4.runtime.ParserRuleContext;

public final class EnumerationLiteral extends TypedElement<Enumeration>
{
    @Nullable
    private final String prettyName;

    private EnumerationLiteral(
            @Nonnull ParserRuleContext elementContext,
            boolean inferred,
            @Nonnull ParserRuleContext nameContext,
            @Nonnull String name,
            int ordinal,
            @Nonnull Enumeration enumeration,
            @Nullable String prettyName)
    {
        super(elementContext, inferred, nameContext, name, ordinal, enumeration);
        this.prettyName = prettyName;
    }

    @Nullable
    public String getPrettyName()
    {
        return this.prettyName;
    }

    public static class EnumerationLiteralBuilder extends NamedElementBuilder
    {
        @Nullable
        private final String             prettyName;
        @Nonnull
        private final EnumerationBuilder enumerationBuilder;

        private EnumerationLiteral enumerationLiteral;

        public EnumerationLiteralBuilder(
                @Nonnull ParserRuleContext elementContext,
                boolean inferred,
                @Nonnull ParserRuleContext nameContext,
                @Nonnull String name,
                int ordinal,
                @Nullable String prettyName,
                @Nonnull EnumerationBuilder enumerationBuilder)
        {
            super(elementContext, inferred, nameContext, name, ordinal);
            this.prettyName = prettyName;
            this.enumerationBuilder = Objects.requireNonNull(enumerationBuilder);
        }

        public EnumerationLiteral build()
        {
            if (this.enumerationLiteral != null)
            {
                throw new IllegalStateException();
            }

            this.enumerationLiteral = new EnumerationLiteral(
                    this.elementContext,
                    this.inferred,
                    this.nameContext,
                    this.name,
                    this.ordinal,
                    this.enumerationBuilder.getEnumeration(),
                    this.prettyName);
            return this.enumerationLiteral;
        }
    }
}
