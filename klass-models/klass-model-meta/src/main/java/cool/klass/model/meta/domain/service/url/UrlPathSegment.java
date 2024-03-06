package cool.klass.model.meta.domain.service.url;

import javax.annotation.Nonnull;

public interface UrlPathSegment
{
    interface UrlPathSegmentBuilder
    {
        @Nonnull
        UrlPathSegment build();
    }
}
