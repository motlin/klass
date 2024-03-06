package cool.klass.model.meta.domain.service.url;

import javax.annotation.Nonnull;

import cool.klass.model.meta.domain.api.service.url.UrlPathSegment;

public interface AbstractUrlPathSegment extends UrlPathSegment
{
    interface UrlPathSegmentBuilder
    {
        @Nonnull
        AbstractUrlPathSegment build();
    }
}
