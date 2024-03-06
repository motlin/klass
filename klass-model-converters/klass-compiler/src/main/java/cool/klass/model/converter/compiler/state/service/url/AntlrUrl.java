package cool.klass.model.converter.compiler.state.service.url;

import java.util.LinkedHashMap;
import java.util.Objects;
import java.util.Optional;

import javax.annotation.Nonnull;

import cool.klass.model.converter.compiler.CompilationUnit;
import cool.klass.model.converter.compiler.annotation.CompilerAnnotationState;
import cool.klass.model.converter.compiler.state.AntlrElement;
import cool.klass.model.converter.compiler.state.AntlrNamedElement;
import cool.klass.model.converter.compiler.state.IAntlrElement;
import cool.klass.model.converter.compiler.state.parameter.AntlrParameter;
import cool.klass.model.converter.compiler.state.property.ParameterHolder;
import cool.klass.model.converter.compiler.state.service.AntlrService;
import cool.klass.model.converter.compiler.state.service.AntlrServiceGroup;
import cool.klass.model.converter.compiler.state.service.AntlrVerb;
import cool.klass.model.meta.domain.AbstractElement.ElementBuilder;
import cool.klass.model.meta.domain.api.service.Verb;
import cool.klass.model.meta.domain.parameter.ParameterImpl.ParameterBuilder;
import cool.klass.model.meta.domain.service.ServiceImpl.ServiceBuilder;
import cool.klass.model.meta.domain.service.url.UrlImpl.UrlBuilder;
import cool.klass.model.meta.grammar.KlassParser.ServiceDeclarationContext;
import cool.klass.model.meta.grammar.KlassParser.UrlDeclarationContext;
import org.antlr.v4.runtime.Token;
import org.eclipse.collections.api.bag.ImmutableBag;
import org.eclipse.collections.api.list.ImmutableList;
import org.eclipse.collections.api.list.MutableList;
import org.eclipse.collections.api.map.MutableOrderedMap;
import org.eclipse.collections.api.map.OrderedMap;
import org.eclipse.collections.api.tuple.Pair;
import org.eclipse.collections.impl.factory.Lists;
import org.eclipse.collections.impl.map.ordered.mutable.OrderedMapAdapter;
import org.eclipse.collections.impl.tuple.Tuples;

public class AntlrUrl
        extends AntlrElement
{
    public static final AntlrUrl AMBIGUOUS = new AntlrUrl(
            new UrlDeclarationContext(null, -1),
            Optional.empty(),
            AntlrServiceGroup.AMBIGUOUS);

    private static final Object SENTINEL = new Object();

    private final MutableList<IAntlrElement> urlPathSegments = Lists.mutable.empty();

    private final ParameterHolder urlParameters   = new ParameterHolder();
    private final ParameterHolder pathParameters  = new ParameterHolder();
    private final ParameterHolder queryParameters = new ParameterHolder();

    private final MutableList<AntlrService>                                  serviceStates     = Lists.mutable.empty();
    private final MutableOrderedMap<Verb, AntlrService>                      servicesByVerb    =
            OrderedMapAdapter.adapt(new LinkedHashMap<>());
    private final MutableOrderedMap<ServiceDeclarationContext, AntlrService> servicesByContext =
            OrderedMapAdapter.adapt(new LinkedHashMap<>());

    @Nonnull
    private final AntlrServiceGroup serviceGroup;
    private       UrlBuilder        elementBuilder;

    public AntlrUrl(
            @Nonnull UrlDeclarationContext elementContext,
            @Nonnull Optional<CompilationUnit> compilationUnit,
            @Nonnull AntlrServiceGroup serviceGroup)
    {
        super(elementContext, compilationUnit);
        this.serviceGroup = Objects.requireNonNull(serviceGroup);
    }

    @Nonnull
    @Override
    public Optional<IAntlrElement> getSurroundingElement()
    {
        return Optional.of(this.serviceGroup);
    }

    @Override
    public boolean isContext()
    {
        return true;
    }

    @Override
    public Pair<Token, Token> getContextBefore()
    {
        return Tuples.pair(this.getElementContext().getStart(), this.getElementContext().url().getStop());
    }

    @Nonnull
    public AntlrServiceGroup getServiceGroup()
    {
        return this.serviceGroup;
    }

    public MutableList<AntlrService> getServiceStates()
    {
        return this.serviceStates.asUnmodifiable();
    }

    public AntlrService getServiceByContext(ServiceDeclarationContext ctx)
    {
        return this.servicesByContext.get(ctx);
    }

    public int getNumPathSegments()
    {
        return this.urlPathSegments.size();
    }

    public int getNumQueryParameters()
    {
        return this.queryParameters.getNumParameters();
    }

    public void enterUrlConstant(AntlrUrlConstant antlrUrlConstant)
    {
        this.urlPathSegments.add(antlrUrlConstant);
    }

    public void enterPathParameterDeclaration(@Nonnull AntlrParameter pathParameterState)
    {
        this.urlPathSegments.add(pathParameterState);
        this.urlParameters.enterParameterDeclaration(pathParameterState);
        this.pathParameters.enterParameterDeclaration(pathParameterState);
    }

    public void enterQueryParameterDeclaration(@Nonnull AntlrParameter queryParameterState)
    {
        this.urlParameters.enterParameterDeclaration(queryParameterState);
        this.queryParameters.enterParameterDeclaration(queryParameterState);
    }

    public void exitServiceDeclaration(@Nonnull AntlrService antlrService)
    {
        this.serviceStates.add(antlrService);
        this.servicesByVerb.compute(
                antlrService.getVerbState().getVerb(),
                (name, builder) -> builder == null
                        ? antlrService
                        : AntlrService.AMBIGUOUS);

        AntlrService duplicate = this.servicesByContext.put(
                antlrService.getElementContext(),
                antlrService);
        if (duplicate != null)
        {
            throw new AssertionError();
        }
    }

    //<editor-fold desc="Report Compiler Errors">
    public void reportErrors(@Nonnull CompilerAnnotationState compilerAnnotationHolder)
    {
        this.reportDuplicateParameterErrors(compilerAnnotationHolder);
        this.reportDuplicateVerbErrors(compilerAnnotationHolder);
        this.reportNoVerbs(compilerAnnotationHolder);

        this.urlParameters.getParameterStates().forEachWith(AntlrParameter::reportErrors, compilerAnnotationHolder);
        this.serviceStates.forEachWith(AntlrService::reportErrors, compilerAnnotationHolder);
    }

    private void reportDuplicateParameterErrors(CompilerAnnotationState compilerAnnotationHolder)
    {
        ImmutableBag<String> duplicateNames = this.urlParameters.getParameterStates()
                .collect(AntlrNamedElement::getName)
                .toBag()
                .selectByOccurrences(occurrences -> occurrences > 1)
                .toImmutable();

        this.urlParameters.getParameterStates()
                .select(each -> duplicateNames.contains(each.getName()))
                .forEachWith(AntlrParameter::reportDuplicateParameterName, compilerAnnotationHolder);
    }

    private void reportDuplicateVerbErrors(CompilerAnnotationState compilerAnnotationHolder)
    {
        ImmutableBag<Verb> duplicateVerbs = this.serviceStates
                .collect(AntlrService::getVerbState)
                .collect(AntlrVerb::getVerb)
                .toBag()
                .selectByOccurrences(occurrences -> occurrences > 1)
                .toImmutable();

        this.serviceStates
                .select(each -> duplicateVerbs.contains(each.getVerbState().getVerb()))
                .forEachWith(AntlrService::reportDuplicateVerb, compilerAnnotationHolder);
    }

    private void reportNoVerbs(@Nonnull CompilerAnnotationState compilerAnnotationHolder)
    {
        if (this.serviceStates.isEmpty())
        {
            String message = String.format(
                    "Service url should declare at least one verb: '%s'.",
                    this.getElementContext().url().getText());

            compilerAnnotationHolder.add("ERR_URL_EMP", message, this);
        }
    }
    //</editor-fold>

    @Nonnull
    @Override
    public UrlDeclarationContext getElementContext()
    {
        return (UrlDeclarationContext) super.getElementContext();
    }

    @Nonnull
    public OrderedMap<String, AntlrParameter> getFormalParametersByName()
    {
        return this.urlParameters.getParameterStatesByName();
    }

    public ImmutableList<Object> getNormalizedPathSegments()
    {
        return this.urlPathSegments.collect(this::toNormalized).toImmutable();
    }

    @Nonnull
    private Object toNormalized(IAntlrElement element)
    {
        if (element instanceof AntlrParameter)
        {
            return SENTINEL;
        }
        if (element instanceof AntlrUrlConstant urlConstant)
        {
            return urlConstant.getName();
        }
        throw new AssertionError();
    }

    @Nonnull
    public UrlBuilder build()
    {
        if (this.elementBuilder != null)
        {
            throw new IllegalStateException();
        }

        this.elementBuilder = new UrlBuilder(
                (UrlDeclarationContext) this.elementContext,
                this.getMacroElementBuilder(),
                this.getSourceCodeBuilder(),
                this.serviceGroup.getElementBuilder());

        ImmutableList<ElementBuilder<?>> pathSegments = this.urlPathSegments
                .<ElementBuilder<?>>collect(this::buildPathSegment)
                .toImmutable();
        this.elementBuilder.setPathSegmentBuilders(pathSegments);

        ImmutableList<ParameterBuilder> queryParameterBuilders = this.queryParameters
                .getParameterStates()
                .collect(AntlrParameter::build)
                .toImmutable();
        this.elementBuilder.setQueryParameterBuilders(queryParameterBuilders);

        ImmutableList<ParameterBuilder> pathParameterBuilders = this.pathParameters
                .getParameterStates()
                .collect(AntlrParameter::getElementBuilder)
                .toImmutable();
        this.elementBuilder.setPathParameterBuilders(pathParameterBuilders);

        ImmutableList<ParameterBuilder> parameterBuilders = this.urlParameters
                .getParameterStates()
                .collect(AntlrParameter::getElementBuilder)
                .toImmutable();
        this.elementBuilder.setParameterBuilders(parameterBuilders);

        ImmutableList<ServiceBuilder> serviceBuilders = this.serviceStates
                .collect(AntlrService::build)
                .toImmutable();
        this.elementBuilder.setServiceBuilders(serviceBuilders);

        return this.elementBuilder;
    }

    @Nonnull
    private ElementBuilder<?> buildPathSegment(IAntlrElement element)
    {
        if (element instanceof AntlrUrlConstant antlrUrlConstant)
        {
            return antlrUrlConstant.build();
        }

        if (element instanceof AntlrParameter antlrParameter)
        {
            return antlrParameter.build();
        }

        throw new AssertionError(element.getClass().getSimpleName());
    }

    @Override
    @Nonnull
    public UrlBuilder getElementBuilder()
    {
        return Objects.requireNonNull(this.elementBuilder);
    }
}
