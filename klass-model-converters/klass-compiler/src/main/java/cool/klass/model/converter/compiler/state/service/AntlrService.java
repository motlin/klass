package cool.klass.model.converter.compiler.state.service;

import java.util.LinkedHashMap;
import java.util.Objects;
import java.util.Optional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import cool.klass.model.converter.compiler.CompilationUnit;
import cool.klass.model.converter.compiler.annotation.CompilerAnnotationState;
import cool.klass.model.converter.compiler.state.AntlrElement;
import cool.klass.model.converter.compiler.state.IAntlrElement;
import cool.klass.model.converter.compiler.state.criteria.AntlrCriteria;
import cool.klass.model.converter.compiler.state.order.AntlrOrderBy;
import cool.klass.model.converter.compiler.state.order.AntlrOrderByOwner;
import cool.klass.model.converter.compiler.state.service.url.AntlrUrl;
import cool.klass.model.meta.domain.api.service.ServiceMultiplicity;
import cool.klass.model.meta.domain.api.service.Verb;
import cool.klass.model.meta.domain.criteria.AbstractCriteria.AbstractCriteriaBuilder;
import cool.klass.model.meta.domain.order.OrderByImpl.OrderByBuilder;
import cool.klass.model.meta.domain.service.ServiceImpl.ServiceBuilder;
import cool.klass.model.meta.domain.service.ServiceProjectionDispatchImpl.ServiceProjectionDispatchBuilder;
import cool.klass.model.meta.domain.service.url.UrlImpl.UrlBuilder;
import cool.klass.model.meta.grammar.KlassParser.ServiceCriteriaDeclarationContext;
import cool.klass.model.meta.grammar.KlassParser.ServiceDeclarationContext;
import cool.klass.model.meta.grammar.KlassParser.ServiceProjectionDispatchContext;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.Token;
import org.eclipse.collections.api.bag.ImmutableBag;
import org.eclipse.collections.api.list.ImmutableList;
import org.eclipse.collections.api.list.MutableList;
import org.eclipse.collections.api.map.ImmutableMap;
import org.eclipse.collections.api.map.MutableOrderedMap;
import org.eclipse.collections.api.tuple.Pair;
import org.eclipse.collections.impl.factory.Lists;
import org.eclipse.collections.impl.factory.Maps;
import org.eclipse.collections.impl.map.ordered.mutable.OrderedMapAdapter;
import org.eclipse.collections.impl.tuple.Tuples;

public class AntlrService extends AntlrElement implements AntlrOrderByOwner
{
    public static final AntlrService AMBIGUOUS = new AntlrService(
            new ServiceDeclarationContext(null, -1),
            Optional.empty(),
            AntlrUrl.AMBIGUOUS,
            AntlrVerb.AMBIGUOUS);

    private static final ImmutableMap<Verb, ImmutableList<String>> ALLOWED_CRITERIA_TYPES =
            Maps.immutable.<Verb, ImmutableList<String>>empty()
                    .newWithKeyValue(Verb.GET, Lists.immutable.with("authorize", "criteria", "version"))
                    .newWithKeyValue(Verb.POST, Lists.immutable.with("authorize"))
                    .newWithKeyValue(Verb.PUT, Lists.immutable.with("authorize", "criteria", "conflict"))
                    .newWithKeyValue(Verb.PATCH, Lists.immutable.with("authorize", "criteria", "conflict"))
                    .newWithKeyValue(Verb.DELETE, Lists.immutable.with("authorize", "criteria", "conflict"));

    @Nonnull
    private final AntlrUrl  urlState;
    @Nonnull
    private final AntlrVerb verbState;

    private final MutableList<AntlrServiceCriteria> serviceCriteriaStates = Lists.mutable.empty();

    private final MutableOrderedMap<ServiceCriteriaDeclarationContext, AntlrServiceCriteria> serviceCriteriaByContext =
            OrderedMapAdapter.adapt(new LinkedHashMap<>());

    @Nullable
    private AntlrServiceMultiplicity serviceMultiplicityState;

    @Nonnull
    private Optional<AntlrServiceProjectionDispatch> serviceProjectionDispatchState = Optional.empty();
    @Nonnull
    private Optional<AntlrOrderBy>                   orderByState                   = Optional.empty();
    private ServiceBuilder                           elementBuilder;

    public AntlrService(
            @Nonnull ServiceDeclarationContext elementContext,
            @Nonnull Optional<CompilationUnit> compilationUnit,
            @Nonnull AntlrUrl urlState,
            @Nonnull AntlrVerb verbState)
    {
        super(elementContext, compilationUnit);
        this.urlState  = Objects.requireNonNull(urlState);
        this.verbState = Objects.requireNonNull(verbState);
    }

    @Nonnull
    @Override
    public Optional<IAntlrElement> getSurroundingElement()
    {
        return Optional.of(this.urlState);
    }

    @Override
    public boolean isContext()
    {
        return true;
    }

    @Override
    public Pair<Token, Token> getContextBefore()
    {
        return Tuples.pair(this.getElementContext().getStart(), this.getElementContext().serviceDeclarationBody().getStart());
    }

    @Override
    public Pair<Token, Token> getContextAfter()
    {
        return Tuples.pair(this.getElementContext().serviceDeclarationBody().getStop(), this.getElementContext().serviceDeclarationBody().getStop());
    }

    @Nonnull
    public AntlrUrl getUrlState()
    {
        return this.urlState;
    }

    @Nullable
    public AntlrServiceMultiplicity getServiceMultiplicityState()
    {
        return this.serviceMultiplicityState;
    }

    public MutableList<AntlrServiceCriteria> getServiceCriteriaStates()
    {
        return this.serviceCriteriaStates.asUnmodifiable();
    }

    public AntlrServiceCriteria getServiceCriteriaByContext(ServiceCriteriaDeclarationContext ctx)
    {
        return this.serviceCriteriaByContext.get(ctx);
    }

    public void reportDuplicateVerb(@Nonnull CompilerAnnotationState compilerAnnotationHolder)
    {
        String message = String.format("Duplicate verb: '%s'.", this.verbState.getVerb());

        compilerAnnotationHolder.add("ERR_DUP_VRB", message, this, this.verbState.getElementContext());
    }

    public void enterServiceCriteriaDeclaration(@Nonnull AntlrServiceCriteria serviceCriteriaState)
    {
        this.serviceCriteriaStates.add(serviceCriteriaState);
        AntlrServiceCriteria duplicate = this.serviceCriteriaByContext.put(
                serviceCriteriaState.getElementContext(),
                serviceCriteriaState);
        if (duplicate != null)
        {
            throw new AssertionError();
        }
    }

    public void enterServiceProjectionDispatch(@Nonnull AntlrServiceProjectionDispatch projectionDispatch)
    {
        this.serviceProjectionDispatchState = Optional.of(projectionDispatch);
    }

    //<editor-fold desc="Report Compiler Errors">
    public void reportErrors(@Nonnull CompilerAnnotationState compilerAnnotationHolder)
    {
        this.reportDuplicateKeywords(compilerAnnotationHolder);

        // TODO: ☑ reportErrors: Find url parameters which are unused by any criteria

        this.reportInvalidProjection(compilerAnnotationHolder);

        Verb                  verb                 = this.verbState.getVerb();
        ImmutableList<String> allowedCriteriaTypes = ALLOWED_CRITERIA_TYPES.get(verb);

        for (AntlrServiceCriteria serviceCriteriaState : this.serviceCriteriaStates)
        {
            if (allowedCriteriaTypes == null)
            {
                throw new AssertionError(verb);
            }
            serviceCriteriaState.reportAllowedCriteriaTypes(compilerAnnotationHolder, allowedCriteriaTypes);
            serviceCriteriaState.getCriteria().reportErrors(compilerAnnotationHolder);
        }

        this.orderByState.ifPresent(orderBy -> orderBy.reportErrors(compilerAnnotationHolder));
    }

    protected void reportDuplicateKeywords(@Nonnull CompilerAnnotationState compilerAnnotationHolder)
    {
        ImmutableBag<String> duplicateKeywords = this.serviceCriteriaStates
                .collect(AntlrServiceCriteria::getServiceCriteriaKeyword)
                .toBag()
                .selectByOccurrences(occurrences -> occurrences > 1)
                .toImmutable();

        this.serviceCriteriaStates
                .select(each -> duplicateKeywords.contains(each.getServiceCriteriaKeyword()))
                .forEachWith(AntlrServiceCriteria::reportDuplicateKeyword, compilerAnnotationHolder);
    }

    private void reportInvalidProjection(@Nonnull CompilerAnnotationState compilerAnnotationHolder)
    {
        Verb verb = this.verbState.getVerb();

        if (verb == Verb.GET)
        {
            this.serviceProjectionDispatchState.ifPresentOrElse(
                    projectionDispatch -> projectionDispatch.reportErrors(compilerAnnotationHolder),
                    () -> this.reportMissingProjection(compilerAnnotationHolder));
        }
        else
        {
            this.serviceProjectionDispatchState
                    .ifPresent(projectionDispatch -> this.reportPresentProjection(
                            projectionDispatch,
                            compilerAnnotationHolder));
        }
    }

    private void reportMissingProjection(CompilerAnnotationState compilerAnnotationHolder)
    {
        ParserRuleContext verbContext = this.verbState.getElementContext();

        compilerAnnotationHolder.add("ERR_GET_PRJ", "GET services require a projection.", this, verbContext);
    }

    private void reportPresentProjection(
            AntlrServiceProjectionDispatch projectionDispatch,
            CompilerAnnotationState compilerAnnotationHolder)
    {
        ServiceProjectionDispatchContext elementContext = projectionDispatch.getElementContext();

        compilerAnnotationHolder.add(
                "ERR_PUT_PRJ",
                String.format("%s services must not have a projection.", this.verbState.getVerb().name()),
                projectionDispatch,
                elementContext);
    }
    //</editor-fold>

    @Nonnull
    public AntlrVerb getVerbState()
    {
        return this.verbState;
    }

    public void enterServiceMultiplicityDeclaration(@Nonnull AntlrServiceMultiplicity serviceMultiplicityState)
    {
        this.serviceMultiplicityState = Objects.requireNonNull(serviceMultiplicityState);
    }

    @Override
    public void enterOrderByDeclaration(@Nonnull AntlrOrderBy orderByState)
    {
        if (this.orderByState.isPresent())
        {
            throw new IllegalStateException();
        }
        this.orderByState = Optional.of(orderByState);
    }

    @Override
    @Nonnull
    public Optional<AntlrOrderBy> getOrderByState()
    {
        return this.orderByState;
    }

    public ServiceBuilder build()
    {
        if (this.elementBuilder != null)
        {
            throw new IllegalStateException();
        }

        UrlBuilder          urlBuilder          = this.urlState.getElementBuilder();
        Verb                verb                = this.verbState.getVerb();
        ServiceMultiplicity serviceMultiplicity = this.serviceMultiplicityState.getServiceMultiplicity();
        this.elementBuilder = new ServiceBuilder(
                (ServiceDeclarationContext) this.elementContext,
                this.getMacroElementBuilder(),
                this.getSourceCodeBuilder(),
                urlBuilder,
                verb,
                serviceMultiplicity);

        for (AntlrServiceCriteria serviceCriteriaState : this.serviceCriteriaStates)
        {
            String                     serviceCriteriaKeyword = serviceCriteriaState.getServiceCriteriaKeyword();
            AntlrCriteria              criteriaState          = serviceCriteriaState.getCriteria();
            AbstractCriteriaBuilder<?> criteriaBuilder        = criteriaState.build();
            this.elementBuilder.addCriteriaBuilder(serviceCriteriaKeyword, criteriaBuilder);
        }

        Optional<ServiceProjectionDispatchBuilder> projectionDispatchBuilder =
                this.serviceProjectionDispatchState.map(AntlrServiceProjectionDispatch::build);
        this.elementBuilder.setProjectionDispatchBuilder(projectionDispatchBuilder);

        Optional<OrderByBuilder> orderByBuilder = this.orderByState.map(AntlrOrderBy::build);
        this.elementBuilder.setOrderByBuilder(orderByBuilder);

        return this.elementBuilder;
    }

    @Override
    @Nonnull
    public ServiceBuilder getElementBuilder()
    {
        return Objects.requireNonNull(this.elementBuilder);
    }

    @Nonnull
    @Override
    public ServiceDeclarationContext getElementContext()
    {
        return (ServiceDeclarationContext) super.getElementContext();
    }
}
