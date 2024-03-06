package cool.klass.model.meta.domain.api.parameter;

import javax.annotation.Nonnull;

import cool.klass.model.meta.domain.api.PrimitiveType;

public interface PrimitiveParameter extends Parameter
{
    @Override
    @Nonnull
    PrimitiveType getType();
}
