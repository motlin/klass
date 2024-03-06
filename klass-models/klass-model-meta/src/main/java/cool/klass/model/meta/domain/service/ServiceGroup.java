package cool.klass.model.meta.domain.service;

import java.util.Objects;

import javax.annotation.Nonnull;

import cool.klass.model.meta.domain.Klass;
import cool.klass.model.meta.domain.Klass.KlassBuilder;
import cool.klass.model.meta.domain.PackageableElement;
import cool.klass.model.meta.domain.service.url.Url;
import cool.klass.model.meta.domain.service.url.Url.UrlBuilder;
import org.antlr.v4.runtime.ParserRuleContext;
import org.eclipse.collections.api.list.ImmutableList;

public final class ServiceGroup extends PackageableElement
{
    @Nonnull
    private final Klass klass;

    private ImmutableList<Url> urls;

    private ServiceGroup(
            @Nonnull ParserRuleContext elementContext,
            @Nonnull ParserRuleContext nameContext,
            @Nonnull String name,
            @Nonnull String packageName,
            @Nonnull Klass klass)
    {
        super(elementContext, nameContext, name, packageName);
        this.klass = Objects.requireNonNull(klass);
    }

    @Nonnull
    public Klass getKlass()
    {
        return this.klass;
    }

    @Nonnull
    public ImmutableList<Url> getUrls()
    {
        return Objects.requireNonNull(this.urls);
    }

    private void setUrls(@Nonnull ImmutableList<Url> urls)
    {
        if (this.urls != null)
        {
            throw new IllegalStateException();
        }
        this.urls = Objects.requireNonNull(urls);
    }

    public static class ServiceGroupBuilder extends PackageableElementBuilder
    {
        @Nonnull
        private final KlassBuilder klassBuilder;

        private ImmutableList<UrlBuilder> urlBuilders;
        private ServiceGroup              serviceGroup;

        public ServiceGroupBuilder(
                @Nonnull ParserRuleContext elementContext,
                @Nonnull ParserRuleContext nameContext,
                @Nonnull String name,
                @Nonnull String packageName,
                @Nonnull KlassBuilder klassBuilder)
        {
            super(elementContext, nameContext, name, packageName);
            this.klassBuilder = Objects.requireNonNull(klassBuilder);
        }

        public void setUrlBuilders(@Nonnull ImmutableList<UrlBuilder> urlBuilders)
        {
            this.urlBuilders = Objects.requireNonNull(urlBuilders);
        }

        @Nonnull
        public ServiceGroup build()
        {
            if (this.serviceGroup != null)
            {
                throw new IllegalStateException();
            }

            this.serviceGroup = new ServiceGroup(
                    this.elementContext,
                    this.nameContext,
                    this.name,
                    this.packageName,
                    this.klassBuilder.getKlass());

            ImmutableList<Url> urls = this.urlBuilders.collect(UrlBuilder::build);
            this.serviceGroup.setUrls(urls);

            return this.serviceGroup;
        }

        @Nonnull
        public ServiceGroup getServiceGroup()
        {
            return Objects.requireNonNull(this.serviceGroup);
        }
    }
}
