package cool.klass.model.converter.compiler.state.service.url;

import java.util.Objects;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import cool.klass.model.converter.compiler.CompilationUnit;
import cool.klass.model.converter.compiler.state.AntlrMultiplicity;
import cool.klass.model.meta.domain.service.url.UrlPathParameter.UrlPathParameterBuilder;
import org.antlr.v4.runtime.ParserRuleContext;
import org.eclipse.collections.api.list.ImmutableList;

public abstract class AntlrUrlPathParameter extends AntlrUrlParameter implements AntlrUrlPathSegment
{
    public AntlrUrlPathParameter(
            @Nonnull ParserRuleContext elementContext,
            CompilationUnit compilationUnit,
            boolean inferred,
            @Nonnull ParserRuleContext nameContext,
            @Nonnull String name,
            int ordinal,
            @Nonnull AntlrMultiplicity multiplicityState,
            @Nonnull AntlrUrl urlState,
            ImmutableList<AntlrParameterModifier> parameterModifiers)
    {
        super(
                elementContext,
                compilationUnit,
                inferred,
                nameContext,
                name,
                ordinal,
                multiplicityState,
                urlState,
                parameterModifiers);
    }

    @Override
    public abstract UrlPathParameterBuilder build();

    @Nonnull
    @Override
    public Object toNormalized()
    {
        return new NormalizedPathParameter(this.getName());
    }

    private static final class NormalizedPathParameter
    {
        @Nonnull
        private final String name;

        private NormalizedPathParameter(@Nonnull String name)
        {
            this.name = Objects.requireNonNull(name);
        }

        @Override
        public int hashCode()
        {
            return this.name.hashCode();
        }

        @Override
        public boolean equals(@Nullable Object o)
        {
            if (this == o)
            {
                return true;
            }
            if (o == null || this.getClass() != o.getClass())
            {
                return false;
            }

            NormalizedPathParameter that = (NormalizedPathParameter) o;

            return this.name.equals(that.name);
        }
    }
}
