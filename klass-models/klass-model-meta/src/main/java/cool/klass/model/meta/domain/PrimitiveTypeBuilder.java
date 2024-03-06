package cool.klass.model.meta.domain;

import java.util.Objects;
import java.util.Optional;

import javax.annotation.Nonnull;

import cool.klass.model.meta.domain.AbstractElement.ElementBuilder;
import cool.klass.model.meta.domain.api.DataType.DataTypeGetter;
import cool.klass.model.meta.domain.api.PrimitiveType;
import cool.klass.model.meta.domain.api.source.SourceCode.SourceCodeBuilder;
import org.antlr.v4.runtime.ParserRuleContext;

public class PrimitiveTypeBuilder
        extends ElementBuilder<PrimitiveType>
        implements DataTypeGetter
{
    @Nonnull
    private final PrimitiveType primitiveType;

    public PrimitiveTypeBuilder(
            @Nonnull ParserRuleContext elementContext,
            @Nonnull Optional<ElementBuilder<?>> macroElement,
            @Nonnull Optional<SourceCodeBuilder> sourceCode,
            @Nonnull PrimitiveType primitiveType)
    {
        super(elementContext, macroElement, sourceCode);
        this.primitiveType = Objects.requireNonNull(primitiveType);
    }

    @Nonnull
    @Override
    public PrimitiveType getType()
    {
        return this.primitiveType;
    }

    @Nonnull
    @Override
    protected PrimitiveType buildUnsafe()
    {
        return this.primitiveType;
    }
}

