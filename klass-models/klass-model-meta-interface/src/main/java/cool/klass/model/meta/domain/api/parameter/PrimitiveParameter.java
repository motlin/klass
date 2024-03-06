package cool.klass.model.meta.domain.api.parameter;

import javax.annotation.Nonnull;

import cool.klass.model.meta.domain.api.PrimitiveType;

public interface PrimitiveParameter extends IParameter
{
    @Override
    @Nonnull
    PrimitiveType getType();
}
