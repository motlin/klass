package cool.klass.model.converter.compiler.state.service.url;

import javax.annotation.Nonnull;

import cool.klass.model.meta.domain.service.url.AbstractUrlPathSegment.UrlPathSegmentBuilder;

public interface AntlrUrlPathSegment
{
    @Nonnull
    Object toNormalized();

    UrlPathSegmentBuilder build();
}
