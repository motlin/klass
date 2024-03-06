package cool.klass.model.converter.compiler.state.service.url;

import java.util.LinkedHashMap;
import java.util.Objects;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import cool.klass.model.converter.compiler.CompilationUnit;
import cool.klass.model.converter.compiler.error.CompilerErrorHolder;
import cool.klass.model.converter.compiler.state.AntlrElement;
import cool.klass.model.converter.compiler.state.service.AntlrService;
import cool.klass.model.converter.compiler.state.service.AntlrServiceGroup;
import cool.klass.model.converter.compiler.state.service.AntlrVerb;
import cool.klass.model.meta.domain.service.Service.ServiceBuilder;
import cool.klass.model.meta.domain.service.Verb;
import cool.klass.model.meta.domain.service.url.Url.UrlBuilder;
import cool.klass.model.meta.domain.service.url.UrlParameter.UrlParameterBuilder;
import cool.klass.model.meta.domain.service.url.UrlPathSegment.UrlPathSegmentBuilder;
import cool.klass.model.meta.domain.service.url.UrlQueryParameter.UrlQueryParameterBuilder;
import cool.klass.model.meta.grammar.KlassParser.UrlDeclarationContext;
import org.antlr.v4.runtime.ParserRuleContext;
import org.eclipse.collections.api.bag.ImmutableBag;
import org.eclipse.collections.api.list.ImmutableList;
import org.eclipse.collections.api.list.MutableList;
import org.eclipse.collections.api.map.MutableOrderedMap;
import org.eclipse.collections.api.map.OrderedMap;
import org.eclipse.collections.impl.factory.Lists;
import org.eclipse.collections.impl.map.ordered.mutable.OrderedMapAdapter;

public class AntlrUrl extends AntlrElement
{
    @Nonnull
    public static final AntlrUrl AMBIGUOUS = new AntlrUrl(
            new ParserRuleContext(),
            null,
            true,
            AntlrServiceGroup.AMBIGUOUS);

    private final MutableList<AntlrUrlPathSegment> urlPathSegments = Lists.mutable.empty();

    private final MutableList<AntlrUrlPathParameter>  urlPathParameterStates  = Lists.mutable.empty();
    private final MutableList<AntlrUrlQueryParameter> urlQueryParameterStates = Lists.mutable.empty();
    private final MutableList<AntlrUrlParameter>      urlParameterStates      = Lists.mutable.empty();

    private final MutableOrderedMap<String, AntlrUrlParameter> urlParametersByName = OrderedMapAdapter.adapt(new LinkedHashMap<>());

    private final MutableList<AntlrService>             serviceStates  = Lists.mutable.empty();
    private final MutableOrderedMap<Verb, AntlrService> servicesByVerb = OrderedMapAdapter.adapt(new LinkedHashMap<>());

    @Nonnull
    private final AntlrServiceGroup serviceGroup;

    private UrlBuilder urlBuilder;

    public AntlrUrl(
            @Nonnull ParserRuleContext elementContext,
            @Nullable CompilationUnit compilationUnit,
            boolean inferred,
            @Nonnull AntlrServiceGroup serviceGroup)
    {
        super(elementContext, compilationUnit, inferred);
        this.serviceGroup = Objects.requireNonNull(serviceGroup);
    }

    @Nonnull
    public AntlrServiceGroup getServiceGroup()
    {
        return this.serviceGroup;
    }

    public boolean hasQueryParameters()
    {
        return this.urlQueryParameterStates.notEmpty();
    }

    public void enterUrlConstant(AntlrUrlConstant antlrUrlConstant)
    {
        this.urlPathSegments.add(antlrUrlConstant);
    }

    public void enterPathParameterDeclaration(@Nonnull AntlrUrlPathParameter antlrPrimitiveParameter)
    {
        this.urlPathSegments.add(antlrPrimitiveParameter);
        this.urlParameterStates.add(antlrPrimitiveParameter);
        this.urlPathParameterStates.add(antlrPrimitiveParameter);

        this.urlParametersByName.compute(
                antlrPrimitiveParameter.getName(),
                (name, builder) -> builder == null
                        ? antlrPrimitiveParameter
                        : AntlrPrimitiveUrlPathParameter.AMBIGUOUS);
    }

    public void enterQueryParameterDeclaration(@Nonnull AntlrUrlQueryParameter antlrPrimitiveParameter)
    {
        this.urlParameterStates.add(antlrPrimitiveParameter);
        this.urlQueryParameterStates.add(antlrPrimitiveParameter);

        this.urlParametersByName.compute(
                antlrPrimitiveParameter.getName(),
                (name, builder) -> builder == null
                        ? antlrPrimitiveParameter
                        : AntlrPrimitiveUrlQueryParameter.AMBIGUOUS);
    }

    public void exitServiceDeclaration(@Nonnull AntlrService antlrService)
    {
        this.serviceStates.add(antlrService);
        this.servicesByVerb.compute(
                antlrService.getVerbState().getVerb(),
                (name, builder) -> builder == null
                        ? antlrService
                        : AntlrService.AMBIGUOUS);
    }

    public void reportErrors(@Nonnull CompilerErrorHolder compilerErrorHolder)
    {
        this.reportDuplicateParameterErrors(compilerErrorHolder);
        this.reportDuplicateVerbErrors(compilerErrorHolder);
        this.reportNoVerbs(compilerErrorHolder);
    }

    private void reportDuplicateParameterErrors(CompilerErrorHolder compilerErrorHolder)
    {
        ImmutableBag<String> duplicateNames = this.urlParameterStates
                .collect(AntlrUrlParameter::getName)
                .toBag()
                .selectByOccurrences(occurrences -> occurrences > 1)
                .toImmutable();

        this.urlParameterStates
                .select(each -> duplicateNames.contains(each.getName()))
                .forEachWith(AntlrUrlParameter::reportDuplicateParameterName, compilerErrorHolder);
    }

    private void reportDuplicateVerbErrors(CompilerErrorHolder compilerErrorHolder)
    {
        ImmutableBag<Verb> duplicateVerbs = this.serviceStates
                .collect(AntlrService::getVerbState)
                .collect(AntlrVerb::getVerb)
                .toBag()
                .selectByOccurrences(occurrences -> occurrences > 1)
                .toImmutable();

        this.serviceStates
                .select(each -> duplicateVerbs.contains(each.getVerbState().getVerb()))
                .forEachWith(AntlrService::reportDuplicateVerb, compilerErrorHolder);
    }

    private void reportNoVerbs(@Nonnull CompilerErrorHolder compilerErrorHolder)
    {
        if (this.serviceStates.isEmpty())
        {
            String message = String.format(
                    "ERR_URL_EMP: Service url should declare at least one verb: '%s'.",
                    this.getElementContext().url().getText());

            compilerErrorHolder.add(
                    this.compilationUnit,
                    message,
                    this.getElementContext(),
                    this.getParserRuleContexts().toArray(new ParserRuleContext[]{}));
        }
    }

    @Nonnull
    @Override
    public UrlDeclarationContext getElementContext()
    {
        return (UrlDeclarationContext) super.getElementContext();
    }

    public ImmutableList<ParserRuleContext> getParserRuleContexts()
    {
        MutableList<ParserRuleContext> parserRuleContexts = Lists.mutable.empty();
        this.getParserRuleContexts(parserRuleContexts);
        return parserRuleContexts.toImmutable();
    }

    public void getParserRuleContexts(@Nonnull MutableList<ParserRuleContext> parserRuleContexts)
    {
        parserRuleContexts.add(this.getElementContext());
        this.serviceGroup.getParserRuleContexts(parserRuleContexts);
    }

    @Nonnull
    public OrderedMap<String, AntlrUrlParameter> getFormalParametersByName()
    {
        return this.urlParametersByName;
    }

    public ImmutableList<Object> getNormalizedPathSegments()
    {
        return this.urlPathSegments.collect(AntlrUrlPathSegment::toNormalized).toImmutable();
    }

    @Nonnull
    public UrlBuilder build()
    {
        if (this.urlBuilder != null)
        {
            throw new IllegalStateException();
        }

        this.urlBuilder = new UrlBuilder(
                this.elementContext,
                this.serviceGroup.getServiceGroupBuilder());

        ImmutableList<UrlPathSegmentBuilder> pathSegments = this.urlPathSegments
                .collect(AntlrUrlPathSegment::build)
                .toImmutable();
        this.urlBuilder.setPathSegments(pathSegments);

        ImmutableList<UrlQueryParameterBuilder> queryParameters = this.urlQueryParameterStates
                .collect(AntlrUrlQueryParameter::build)
                .toImmutable();
        this.urlBuilder.setQueryParameters(queryParameters);

        ImmutableList<UrlParameterBuilder> urlParameters = this.urlParameterStates
                .collect(AntlrUrlParameter::getUrlParameterBuilder)
                .toImmutable();
        this.urlBuilder.setUrlParameters(urlParameters);

        ImmutableList<ServiceBuilder> services = this.serviceStates
                .collect(AntlrService::build)
                .toImmutable();
        this.urlBuilder.setServices(services);

        return this.urlBuilder;
    }

    @Nonnull
    public UrlBuilder getUrlBuilder()
    {
        return Objects.requireNonNull(this.urlBuilder);
    }
}
