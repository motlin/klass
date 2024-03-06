package cool.klass.model.meta.domain.service;

import java.util.Objects;
import java.util.Optional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import cool.klass.model.meta.domain.AbstractPackageableElement;
import cool.klass.model.meta.domain.KlassImpl;
import cool.klass.model.meta.domain.KlassImpl.KlassBuilder;
import cool.klass.model.meta.domain.api.Element;
import cool.klass.model.meta.domain.api.service.ServiceGroup;
import cool.klass.model.meta.domain.api.service.url.Url;
import cool.klass.model.meta.domain.api.source.SourceCode;
import cool.klass.model.meta.domain.api.source.SourceCode.SourceCodeBuilder;
import cool.klass.model.meta.domain.api.source.TopLevelElementWithSourceCode;
import cool.klass.model.meta.domain.service.url.UrlImpl.UrlBuilder;
import cool.klass.model.meta.grammar.KlassParser.IdentifierContext;
import org.antlr.v4.runtime.ParserRuleContext;
import org.eclipse.collections.api.list.ImmutableList;

public final class ServiceGroupImpl
        extends AbstractPackageableElement
        implements ServiceGroup, TopLevelElementWithSourceCode
{
    @Nonnull
    private final KlassImpl klass;

    private ImmutableList<Url> urls;

    private ServiceGroupImpl(
            @Nonnull ParserRuleContext elementContext,
            @Nonnull Optional<Element> macroElement,
            @Nullable SourceCode sourceCode,
            int ordinal,
            @Nonnull IdentifierContext nameContext,
            @Nonnull String packageName,
            @Nonnull KlassImpl klass)
    {
        super(elementContext, macroElement, sourceCode, ordinal, nameContext, packageName);
        this.klass = Objects.requireNonNull(klass);
    }

    @Override
    @Nonnull
    public KlassImpl getKlass()
    {
        return this.klass;
    }

    @Override
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

    public static final class ServiceGroupBuilder
            extends PackageableElementBuilder<ServiceGroupImpl>
            implements TopLevelElementBuilderWithSourceCode
    {
        @Nonnull
        private final KlassBuilder klassBuilder;

        private ImmutableList<UrlBuilder> urlBuilders;

        public ServiceGroupBuilder(
                @Nonnull ParserRuleContext elementContext,
                @Nonnull Optional<ElementBuilder<?>> macroElement,
                @Nullable SourceCodeBuilder sourceCode,
                int ordinal,
                @Nonnull IdentifierContext nameContext,
                @Nonnull String packageName,
                @Nonnull KlassBuilder klassBuilder)
        {
            super(elementContext, macroElement, sourceCode, ordinal, nameContext, packageName);
            this.klassBuilder = Objects.requireNonNull(klassBuilder);
        }

        public void setUrlBuilders(@Nonnull ImmutableList<UrlBuilder> urlBuilders)
        {
            this.urlBuilders = Objects.requireNonNull(urlBuilders);
        }

        @Override
        @Nonnull
        protected ServiceGroupImpl buildUnsafe()
        {
            return new ServiceGroupImpl(
                    this.elementContext,
                    this.macroElement.map(ElementBuilder::getElement),
                    this.sourceCode.build(),
                    this.ordinal,
                    this.getNameContext(),
                    this.packageName,
                    this.klassBuilder.getElement());
        }

        @Override
        protected void buildChildren()
        {
            ImmutableList<Url> urls = this.urlBuilders.collect(UrlBuilder::build);
            this.element.setUrls(urls);
        }
    }
}
