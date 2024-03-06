package cool.klass.model.converter.compiler.state.service.url;

import java.util.LinkedHashMap;
import java.util.Objects;
import java.util.Optional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import cool.klass.model.converter.compiler.CompilationUnit;
import cool.klass.model.converter.compiler.error.CompilerErrorHolder;
import cool.klass.model.converter.compiler.state.AntlrElement;
import cool.klass.model.converter.compiler.state.AntlrNamedElement;
import cool.klass.model.converter.compiler.state.IAntlrElement;
import cool.klass.model.converter.compiler.state.parameter.AntlrEnumerationParameter;
import cool.klass.model.converter.compiler.state.parameter.AntlrParameter;
import cool.klass.model.converter.compiler.state.parameter.AntlrParameterOwner;
import cool.klass.model.converter.compiler.state.parameter.AntlrPrimitiveParameter;
import cool.klass.model.converter.compiler.state.property.ParameterHolder;
import cool.klass.model.converter.compiler.state.service.AntlrService;
import cool.klass.model.converter.compiler.state.service.AntlrServiceGroup;
import cool.klass.model.converter.compiler.state.service.AntlrVerb;
import cool.klass.model.meta.domain.AbstractElement.ElementBuilder;
import cool.klass.model.meta.domain.api.service.Verb;
import cool.klass.model.meta.domain.parameter.AbstractParameter.AbstractParameterBuilder;
import cool.klass.model.meta.domain.service.ServiceImpl.ServiceBuilder;
import cool.klass.model.meta.domain.service.url.UrlImpl.UrlBuilder;
import cool.klass.model.meta.grammar.KlassParser.ServiceDeclarationContext;
import cool.klass.model.meta.grammar.KlassParser.UrlDeclarationContext;
import org.antlr.v4.runtime.ParserRuleContext;
import org.eclipse.collections.api.bag.ImmutableBag;
import org.eclipse.collections.api.list.ImmutableList;
import org.eclipse.collections.api.list.MutableList;
import org.eclipse.collections.api.map.MutableOrderedMap;
import org.eclipse.collections.api.map.OrderedMap;
import org.eclipse.collections.impl.factory.Lists;
import org.eclipse.collections.impl.map.ordered.mutable.OrderedMapAdapter;

public class AntlrUrl extends AntlrElement implements AntlrParameterOwner
{
    @Nonnull
    public static final  AntlrUrl                                                   AMBIGUOUS         = new AntlrUrl(
            new ParserRuleContext(),
            null,
            true,
            AntlrServiceGroup.AMBIGUOUS);
    private static final Object                                                     SENTINEL          = new Object();
    private final        MutableList<IAntlrElement>                                 urlPathSegments   = Lists.mutable.empty();
    private final        ParameterHolder                                            urlParameters     = new ParameterHolder();
    private final        ParameterHolder                                            pathParameters    = new ParameterHolder();
    private final        ParameterHolder                                            queryParameters   = new ParameterHolder();
    private final        MutableList<AntlrService>                                  serviceStates     = Lists.mutable.empty();
    private final        MutableOrderedMap<Verb, AntlrService>                      servicesByVerb    = OrderedMapAdapter.adapt(
            new LinkedHashMap<>());
    private final        MutableOrderedMap<ServiceDeclarationContext, AntlrService> servicesByContext =
            OrderedMapAdapter.adapt(new LinkedHashMap<>());
    @Nonnull
    private final        AntlrServiceGroup                                          serviceGroup;
    private              UrlBuilder                                                 urlBuilder;

    public AntlrUrl(
            @Nonnull ParserRuleContext elementContext,
            @Nullable CompilationUnit compilationUnit,
            boolean inferred,
            @Nonnull AntlrServiceGroup serviceGroup)
    {
        super(elementContext, compilationUnit, inferred);
        this.serviceGroup = Objects.requireNonNull(serviceGroup);
    }

    @Override
    public boolean omitParentFromSurroundingElements()
    {
        return false;
    }

    @Nonnull
    @Override
    public Optional<IAntlrElement> getSurroundingElement()
    {
        return Optional.of(this.serviceGroup);
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

    public void enterPathParameterDeclaration(@Nonnull AntlrParameter<?> pathParameterState)
    {
        this.urlPathSegments.add(pathParameterState);
        this.urlParameters.enterParameterDeclaration(pathParameterState);
        this.pathParameters.enterParameterDeclaration(pathParameterState);
    }

    public void enterQueryParameterDeclaration(@Nonnull AntlrParameter<?> queryParameterState)
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

    public void reportErrors(@Nonnull CompilerErrorHolder compilerErrorHolder)
    {
        this.reportParameterErrors(compilerErrorHolder);
        this.reportDuplicateParameterErrors(compilerErrorHolder);
        this.reportDuplicateVerbErrors(compilerErrorHolder);
        this.reportNoVerbs(compilerErrorHolder);

        for (AntlrService serviceState : this.serviceStates)
        {
            serviceState.reportErrors(compilerErrorHolder);
        }
    }

    private void reportParameterErrors(@Nonnull CompilerErrorHolder compilerErrorHolder)
    {
        for (AntlrParameter<?> parameterState : this.urlParameters.getParameterStates())
        {
            parameterState.reportNameErrors(compilerErrorHolder);
        }
    }

    private void reportDuplicateParameterErrors(CompilerErrorHolder compilerErrorHolder)
    {
        ImmutableBag<String> duplicateNames = this.urlParameters.getParameterStates()
                .collect(AntlrNamedElement::getName)
                .toBag()
                .selectByOccurrences(occurrences -> occurrences > 1)
                .toImmutable();

        this.urlParameters.getParameterStates()
                .select(each -> duplicateNames.contains(each.getName()))
                .forEachWith(AntlrParameter::reportDuplicateParameterName, compilerErrorHolder);
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

            compilerErrorHolder.add(message, this);
        }
    }

    @Nonnull
    @Override
    public UrlDeclarationContext getElementContext()
    {
        return (UrlDeclarationContext) super.getElementContext();
    }

    @Override
    public void getParserRuleContexts(@Nonnull MutableList<ParserRuleContext> parserRuleContexts)
    {
        parserRuleContexts.add(this.getElementContext());
        this.serviceGroup.getParserRuleContexts(parserRuleContexts);
    }

    @Nonnull
    public OrderedMap<String, AntlrParameter<?>> getFormalParametersByName()
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
        if (element instanceof AntlrUrlConstant)
        {
            AntlrUrlConstant urlConstant = (AntlrUrlConstant) element;
            return urlConstant.getName();
        }
        throw new AssertionError();
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
                this.inferred,
                this.serviceGroup.getElementBuilder());

        ImmutableList<ElementBuilder<?>> pathSegments = this.urlPathSegments
                .<ElementBuilder<?>>collect(this::buildPathSegment)
                .toImmutable();
        this.urlBuilder.setPathSegments(pathSegments);

        ImmutableList<AbstractParameterBuilder<?>> queryParameters = (ImmutableList<AbstractParameterBuilder<?>>) (ImmutableList<?>) this.queryParameters
                .getParameterStates()
                .collect(AntlrParameter::build)
                .toImmutable();
        this.urlBuilder.setQueryParameterBuilders(queryParameters);

        ImmutableList<AbstractParameterBuilder<?>> pathParameters = (ImmutableList<AbstractParameterBuilder<?>>) (ImmutableList<?>) this.pathParameters
                .getParameterStates()
                .collect(AntlrParameter::getElementBuilder)
                .toImmutable();
        this.urlBuilder.setPathParameterBuilders(pathParameters);

        ImmutableList<AbstractParameterBuilder<?>> parameterBuilders = (ImmutableList<AbstractParameterBuilder<?>>) (ImmutableList<?>) this.urlParameters
                .getParameterStates()
                .collect(AntlrParameter::getElementBuilder)
                .toImmutable();
        this.urlBuilder.setParameterBuilders(parameterBuilders);

        ImmutableList<ServiceBuilder> services = this.serviceStates
                .collect(AntlrService::build)
                .toImmutable();
        this.urlBuilder.setServices(services);

        return this.urlBuilder;
    }

    private ElementBuilder<?> buildPathSegment(IAntlrElement element)
    {
        if (element instanceof AntlrUrlConstant)
        {
            AntlrUrlConstant antlrUrlConstant = (AntlrUrlConstant) element;
            return antlrUrlConstant.build();
        }

        if (element instanceof AntlrParameter<?>)
        {
            AntlrParameter<?> antlrParameter = (AntlrParameter<?>) element;
            return antlrParameter.build();
        }

        throw new AssertionError(element.getClass().getSimpleName());
    }

    @Nonnull
    public UrlBuilder getUrlBuilder()
    {
        return Objects.requireNonNull(this.urlBuilder);
    }

    @Override
    public int getNumParameters()
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName()
                + ".getNumParameters() not implemented yet");
    }

    @Override
    public void enterParameterDeclaration(@Nonnull AntlrParameter<?> parameterState)
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName()
                + ".enterParameterDeclaration() not implemented yet");
    }

    @Override
    public void enterPrimitiveParameterDeclaration(@Nonnull AntlrPrimitiveParameter primitiveParameterState)
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName()
                + ".enterPrimitiveParameterDeclaration() not implemented yet");
    }

    @Override
    public void enterEnumerationParameterDeclaration(@Nonnull AntlrEnumerationParameter enumerationParameterState)
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName()
                + ".enterEnumerationParameterDeclaration() not implemented yet");
    }
}
