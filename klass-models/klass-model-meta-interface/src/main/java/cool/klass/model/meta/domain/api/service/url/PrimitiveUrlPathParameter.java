package cool.klass.model.meta.domain.api.service.url;

import javax.annotation.Nonnull;

import cool.klass.model.meta.domain.api.PrimitiveType;

public interface PrimitiveUrlPathParameter extends UrlPathParameter
{
    @Override
    @Nonnull
    PrimitiveType getType();
}
