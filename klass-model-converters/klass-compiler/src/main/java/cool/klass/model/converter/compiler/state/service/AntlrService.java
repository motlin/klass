package cool.klass.model.converter.compiler.state.service;

import java.util.LinkedHashMap;
import java.util.Objects;
import java.util.Optional;

import javax.annotation.Nonnull;

import cool.klass.model.converter.compiler.CompilationUnit;
import cool.klass.model.converter.compiler.error.CompilerErrorState;
import cool.klass.model.converter.compiler.state.AntlrClass;
import cool.klass.model.converter.compiler.state.AntlrElement;
import cool.klass.model.converter.compiler.state.IAntlrElement;
import cool.klass.model.converter.compiler.state.criteria.AntlrCriteria;
import cool.klass.model.converter.compiler.state.order.AntlrOrderBy;
import cool.klass.model.converter.compiler.state.order.AntlrOrderByOwner;
import cool.klass.model.converter.compiler.state.projection.AntlrProjection;
import cool.klass.model.converter.compiler.state.property.AntlrAssociationEnd;
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
import org.antlr.v4.runtime.ParserRuleContext;
import org.eclipse.collections.api.bag.ImmutableBag;
import org.eclipse.collections.api.list.ImmutableList;
import org.eclipse.collections.api.list.MutableList;
import org.eclipse.collections.api.map.ImmutableMap;
import org.eclipse.collections.api.map.MutableOrderedMap;
import org.eclipse.collections.api.partition.list.PartitionMutableList;
import org.eclipse.collections.impl.factory.Lists;
import org.eclipse.collections.impl.factory.Maps;
import org.eclipse.collections.impl.map.ordered.mutable.OrderedMapAdapter;

public class AntlrService extends AntlrElement implements AntlrOrderByOwner
{
    @Nonnull
    public static final AntlrService AMBIGUOUS = new AntlrService(
            new ParserRuleContext(),
            Optional.empty(),
            AntlrUrl.AMBIGUOUS,
            AntlrVerb.AMBIGUOUS,
            AntlrServiceMultiplicity.AMBIGUOUS);

    private static final ImmutableMap<Verb, ImmutableList<String>> ALLOWED_CRITERIA_TYPES =
            Maps.immutable.<Verb, ImmutableList<String>>empty()
                    .newWithKeyValue(Verb.GET, Lists.immutable.with("authorize", "criteria", "version"))
                    .newWithKeyValue(Verb.POST, Lists.immutable.with("authorize"))
                    .newWithKeyValue(Verb.PUT, Lists.immutable.with("authorize", "criteria", "conflict"))
                    .newWithKeyValue(Verb.PATCH, Lists.immutable.with("authorize", "criteria", "conflict"))
                    .newWithKeyValue(Verb.DELETE, Lists.immutable.with("authorize", "criteria", "conflict"));

    @Nonnull
    private final AntlrUrl                 urlState;
    @Nonnull
    private final AntlrVerb                verbState;
    @Nonnull
    private final AntlrServiceMultiplicity serviceMultiplicityState;

    private final MutableList<AntlrServiceCriteria> serviceCriteriaStates = Lists.mutable.empty();

    private final MutableOrderedMap<ServiceCriteriaDeclarationContext, AntlrServiceCriteria> serviceCriteriaByContext =
            OrderedMapAdapter.adapt(new LinkedHashMap<>());

    private Optional<AntlrServiceProjectionDispatch> serviceProjectionDispatchState;
    @Nonnull
    private Optional<AntlrOrderBy>                   orderByState = Optional.empty();
    private ServiceBuilder                           elementBuilder;

    public AntlrService(
            @Nonnull ParserRuleContext elementContext,
            @Nonnull Optional<CompilationUnit> compilationUnit,
            @Nonnull AntlrUrl urlState,
            @Nonnull AntlrVerb verbState,
            @Nonnull AntlrServiceMultiplicity serviceMultiplicityState)
    {
        super(elementContext, compilationUnit);
        this.urlState = Objects.requireNonNull(urlState);
        this.verbState = Objects.requireNonNull(verbState);
        this.serviceMultiplicityState = Objects.requireNonNull(serviceMultiplicityState);
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
        return Optional.of(this.urlState);
    }

    @Nonnull
    public AntlrUrl getUrlState()
    {
        return this.urlState;
    }

    @Nonnull
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

    public void reportDuplicateVerb(@Nonnull CompilerErrorState compilerErrorHolder)
    {
        String message = String.format("Duplicate verb: '%s'.", this.verbState.getVerb());

        compilerErrorHolder.add("ERR_DUP_VRB", message, this, this.verbState.getElementContext());
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

    public void reportErrors(@Nonnull CompilerErrorState compilerErrorHolder)
    {
        this.reportDuplicateKeywords(compilerErrorHolder);

        // TODO: ☑ reportErrors: Find url parameters which are unused by any criteria

        this.reportInvalidProjection(compilerErrorHolder);

        Verb                  verb                 = this.verbState.getVerb();
        ImmutableList<String> allowedCriteriaTypes = ALLOWED_CRITERIA_TYPES.get(verb);

        for (AntlrServiceCriteria serviceCriteriaState : this.serviceCriteriaStates)
        {
            if (allowedCriteriaTypes == null)
            {
                throw new AssertionError(verb);
            }
            serviceCriteriaState.reportAllowedCriteriaTypes(compilerErrorHolder, allowedCriteriaTypes);
            serviceCriteriaState.getCriteria().reportErrors(compilerErrorHolder);
        }
    }

    protected void reportDuplicateKeywords(@Nonnull CompilerErrorState compilerErrorHolder)
    {
        ImmutableBag<String> duplicateKeywords = this.serviceCriteriaStates
                .collect(AntlrServiceCriteria::getServiceCriteriaKeyword)
                .toBag()
                .selectByOccurrences(occurrences -> occurrences > 1)
                .toImmutable();

        this.serviceCriteriaStates
                .select(each -> duplicateKeywords.contains(each.getServiceCriteriaKeyword()))
                .forEachWith(AntlrServiceCriteria::reportDuplicateKeyword, compilerErrorHolder);
    }

    private void reportInvalidProjection(@Nonnull CompilerErrorState compilerErrorHolder)
    {
        if (!this.serviceProjectionDispatchState.isPresent())
        {
            return;
        }

        AntlrServiceProjectionDispatch projectionDispatch = this.serviceProjectionDispatchState.get();
        AntlrProjection                projection         = projectionDispatch.getProjection();
        projectionDispatch.reportErrors(compilerErrorHolder);

        if (projection == AntlrProjection.NOT_FOUND || projection.getKlass() == AntlrClass.NOT_FOUND)
        {
            return;
        }

        Verb verb = this.verbState.getVerb();

        if (verb == Verb.POST || verb == Verb.PUT)
        {
            // TODO: ☑ Check that [1..*] association ends are non-empty
            // TODO: Recurse, differently on owned/unowned required/nonEmpty associationEnds
            // Include version associationEnds iff the service is optimistically locked
            MutableList<AntlrAssociationEnd> associationEndStates = projection.getKlass().getAssociationEndStates();

            PartitionMutableList<AntlrAssociationEnd> partition = associationEndStates.partition(AntlrAssociationEnd::isOwned);

            MutableList<AntlrAssociationEnd> ownedAssociationEnds   = partition.getSelected();
            MutableList<AntlrAssociationEnd> unownedAssociationEnds = partition.getRejected();
        }
    }

    @Nonnull
    public AntlrVerb getVerbState()
    {
        return this.verbState;
    }

    @Override
    public void setOrderByState(@Nonnull Optional<AntlrOrderBy> orderByState)
    {
        this.orderByState = Objects.requireNonNull(orderByState);
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
                this.elementContext,
                this.getMacroElementBuilder(),
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
        this.elementBuilder.setProjectionDispatch(projectionDispatchBuilder);

        Optional<OrderByBuilder> orderByBuilder = this.orderByState.map(AntlrOrderBy::build);
        this.elementBuilder.setOrderByBuilder(orderByBuilder);

        return this.elementBuilder;
    }

    @Nonnull
    public ServiceBuilder getElementBuilder()
    {
        return Objects.requireNonNull(this.elementBuilder);
    }

    public boolean needsVersionCriteriaInferred()
    {
        return this.needsVersionCriteria() && !this.hasServiceCriteriaKeyword("version");
    }

    public boolean needsVersionCriteria()
    {
        return this.urlState.getServiceGroup().getKlass().hasVersion()
                && this.verbState.getVerb() == Verb.GET
                && this.serviceMultiplicityState.getServiceMultiplicity() == ServiceMultiplicity.ONE;
    }

    private boolean hasServiceCriteriaKeyword(String serviceCriteriaKeyword)
    {
        return this.serviceCriteriaStates
                .asLazy()
                .collect(AntlrServiceCriteria::getServiceCriteriaKeyword)
                .contains(serviceCriteriaKeyword);
    }

    public boolean needsConflictCriteriaInferred()
    {
        // TODO: PUT many and PATCH many could get the version numbers from the body
        // TODO: DELETE many would have to take a body too?
        // TODO: Or maybe there's no such thing as DELETE many, and it's implied through a merge api
        // TODO: Also the class should be optimistically locked

        return this.urlState.getServiceGroup().getKlass().hasVersion()
                && this.verbState.getVerb() != Verb.GET
                && this.verbState.getVerb() != Verb.POST
                && this.serviceMultiplicityState.getServiceMultiplicity() == ServiceMultiplicity.ONE
                && !this.hasServiceCriteriaKeyword("conflict");
    }

    @Override
    public void getParserRuleContexts(@Nonnull MutableList<ParserRuleContext> parserRuleContexts)
    {
        parserRuleContexts.add(this.getElementContext());
        this.urlState.getParserRuleContexts(parserRuleContexts);
    }

    @Nonnull
    @Override
    public ServiceDeclarationContext getElementContext()
    {
        return (ServiceDeclarationContext) super.getElementContext();
    }
}
