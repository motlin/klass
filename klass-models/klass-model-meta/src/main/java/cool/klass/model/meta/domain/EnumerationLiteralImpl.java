package cool.klass.model.meta.domain;

import java.util.Objects;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import cool.klass.model.meta.domain.EnumerationImpl.EnumerationBuilder;
import cool.klass.model.meta.domain.api.EnumerationLiteral;
import org.antlr.v4.runtime.ParserRuleContext;

public final class EnumerationLiteralImpl extends AbstractTypedElement<EnumerationImpl> implements EnumerationLiteral
{
    @Nullable
    private final String prettyName;

    private EnumerationLiteralImpl(
            @Nonnull ParserRuleContext elementContext,
            boolean inferred,
            @Nonnull ParserRuleContext nameContext,
            @Nonnull String name,
            int ordinal,
            @Nonnull EnumerationImpl enumeration,
            @Nullable String prettyName)
    {
        super(elementContext, inferred, nameContext, name, ordinal, enumeration);
        this.prettyName = prettyName;
    }

    @Override
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

        @Nullable
        private EnumerationLiteralImpl enumerationLiteral;

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

        @Nonnull
        public EnumerationLiteralImpl build()
        {
            if (this.enumerationLiteral != null)
            {
                throw new IllegalStateException();
            }

            this.enumerationLiteral = new EnumerationLiteralImpl(
                    this.elementContext,
                    this.inferred,
                    this.nameContext,
                    this.name,
                    this.ordinal,
                    this.enumerationBuilder.getElement(),
                    this.prettyName);
            return this.enumerationLiteral;
        }
    }
}
