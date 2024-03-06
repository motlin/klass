package cool.klass.model.converter.compiler.state.service;

import java.util.LinkedHashMap;
import java.util.Objects;
import java.util.Optional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import cool.klass.model.converter.compiler.CompilationUnit;
import cool.klass.model.converter.compiler.error.CompilerErrorHolder;
import cool.klass.model.converter.compiler.state.AntlrClass;
import cool.klass.model.converter.compiler.state.AntlrElement;
import cool.klass.model.converter.compiler.state.criteria.AntlrCriteria;
import cool.klass.model.converter.compiler.state.order.AntlrOrderBy;
import cool.klass.model.converter.compiler.state.order.AntlrOrderByOwner;
import cool.klass.model.converter.compiler.state.projection.AntlrProjection;
import cool.klass.model.converter.compiler.state.projection.AntlrProjectionDataTypeProperty;
import cool.klass.model.converter.compiler.state.property.AntlrAssociationEnd;
import cool.klass.model.converter.compiler.state.property.AntlrDataTypeProperty;
import cool.klass.model.converter.compiler.state.property.AntlrProperty;
import cool.klass.model.converter.compiler.state.service.url.AntlrUrl;
import cool.klass.model.meta.domain.api.service.ServiceMultiplicity;
import cool.klass.model.meta.domain.api.service.Verb;
import cool.klass.model.meta.domain.criteria.AbstractCriteria.CriteriaBuilder;
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
            null,
            true,
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

    private AntlrServiceProjectionDispatch serviceProjectionDispatchState;
    // TODO: ❗ Service OrderBy
    @Nonnull
    private Optional<AntlrOrderBy>         orderByState = Optional.empty();
    private ServiceBuilder                 serviceBuilder;

    public AntlrService(
            @Nonnull ParserRuleContext elementContext,
            @Nullable CompilationUnit compilationUnit,
            boolean inferred,
            @Nonnull AntlrUrl urlState,
            @Nonnull AntlrVerb verbState,
            @Nonnull AntlrServiceMultiplicity serviceMultiplicityState)
    {
        super(elementContext, compilationUnit, inferred);
        this.urlState = Objects.requireNonNull(urlState);
        this.verbState = Objects.requireNonNull(verbState);
        this.serviceMultiplicityState = Objects.requireNonNull(serviceMultiplicityState);
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

    public void reportDuplicateVerb(@Nonnull CompilerErrorHolder compilerErrorHolder)
    {
        String message = String.format("ERR_DUP_VRB: Duplicate verb: '%s'.", this.verbState.getVerb());

        compilerErrorHolder.add(
                message,
                this.verbState.getElementContext(),
                this.urlState.getElementContext(),
                this.urlState.getServiceGroup().getElementContext());
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
        this.serviceProjectionDispatchState = Objects.requireNonNull(projectionDispatch);
    }

    public void reportErrors(@Nonnull CompilerErrorHolder compilerErrorHolder)
    {
        ImmutableBag<String> duplicateKeywords = this.serviceCriteriaStates
                .collect(AntlrServiceCriteria::getServiceCriteriaKeyword)
                .toBag()
                .selectByOccurrences(occurrences -> occurrences > 1)
                .toImmutable();

        this.serviceCriteriaStates
                .select(each -> duplicateKeywords.contains(each.getServiceCriteriaKeyword()))
                .forEachWith(AntlrServiceCriteria::reportDuplicateKeyword, compilerErrorHolder);

        // TODO: reportErrors: Find url parameters which are unused by any criteria

        reportInvalidProjection(compilerErrorHolder);

        Verb                  verb                 = this.verbState.getVerb();
        ImmutableList<String> allowedCriteriaTypes = ALLOWED_CRITERIA_TYPES.get(verb);

        for (AntlrServiceCriteria serviceCriteriaState: this.serviceCriteriaStates)
        {
            if (allowedCriteriaTypes == null)
            {
                throw new AssertionError(verb);
            }
            serviceCriteriaState.reportAllowedCriteriaTypes(compilerErrorHolder, allowedCriteriaTypes);

            ImmutableList<ParserRuleContext> parserRuleContexts = this.getParserRuleContexts();
            serviceCriteriaState.getCriteria().reportErrors(compilerErrorHolder, parserRuleContexts);
        }
    }

    private void reportInvalidProjection(CompilerErrorHolder compilerErrorHolder)
    {
        Verb verb = this.verbState.getVerb();

        if (verb == Verb.POST || verb == Verb.PUT)
        {
            AntlrProjection                       projection             = this.serviceProjectionDispatchState.getProjection();
            AntlrClass                            klass                  = projection.getKlass();
            MutableList<AntlrDataTypeProperty<?>> dataTypePropertyStates = klass.getDataTypePropertyStates();
            MutableList<AntlrDataTypeProperty<?>> requiredProperties = dataTypePropertyStates
                    .asLazy()
                    .reject(AntlrDataTypeProperty::isOptional)
                    .reject(AntlrDataTypeProperty::isKey)
                    .reject(AntlrDataTypeProperty::isID)
                    .reject(AntlrDataTypeProperty::isTemporal)
                    .reject(AntlrDataTypeProperty::isAudit)
                    .toList();

            MutableList<AntlrDataTypeProperty<?>> includedProperties = projection.getChildren()
                    .selectInstancesOf(AntlrProjectionDataTypeProperty.class)
                    .collect(AntlrProjectionDataTypeProperty::getDataTypeProperty);

            MutableList<AntlrDataTypeProperty<?>> missingProperties = requiredProperties.reject(includedProperties::contains);

            if (missingProperties.notEmpty())
            {
                String message = String.format(
                        "ERR_PRJ_DTP: Expected write projection '%s' to contain all required properties but was missing %s.",
                        projection.getName(),
                        missingProperties.collect(AntlrProperty::getName).makeString());

                compilerErrorHolder.add(
                        message,
                        this.serviceProjectionDispatchState.getElementContext().projectionReference(),
                        this.getParserRuleContexts().toArray(new ParserRuleContext[]{}));
            }

            // TODO: Do this after changing the versionClass to versionAssociationEnd
            if (klass.isOptimisticallyLocked())
            {
            }
            else
            {
            }

            // TODO: Recurse, differently on owned/unowned required/nonEmpty associationEnds
            // Include version associationEnds iff the service is optimistically locked
            MutableList<AntlrAssociationEnd> associationEndStates = klass.getAssociationEndStates();

            PartitionMutableList<AntlrAssociationEnd> partition = associationEndStates.partition(AntlrAssociationEnd::isOwned);

            MutableList<AntlrAssociationEnd> ownedAssociationEnds   = partition.getSelected();
            MutableList<AntlrAssociationEnd> unownedAssociationEnds = partition.getRejected();
        }
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
        this.urlState.getParserRuleContexts(parserRuleContexts);
    }

    @Nonnull
    @Override
    public ServiceDeclarationContext getElementContext()
    {
        return (ServiceDeclarationContext) super.getElementContext();
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
        if (this.serviceBuilder != null)
        {
            throw new IllegalStateException();
        }

        UrlBuilder          urlBuilder          = this.urlState.getUrlBuilder();
        Verb                verb                = this.verbState.getVerb();
        ServiceMultiplicity serviceMultiplicity = this.serviceMultiplicityState.getServiceMultiplicity();
        this.serviceBuilder = new ServiceBuilder(
                this.elementContext,
                this.inferred,
                urlBuilder,
                verb,
                serviceMultiplicity);

        for (AntlrServiceCriteria serviceCriteriaState: this.serviceCriteriaStates)
        {
            String          serviceCriteriaKeyword = serviceCriteriaState.getServiceCriteriaKeyword();
            AntlrCriteria   criteriaState          = serviceCriteriaState.getCriteria();
            CriteriaBuilder criteriaBuilder        = criteriaState.build();
            this.serviceBuilder.addCriteriaBuilder(serviceCriteriaKeyword, criteriaBuilder);
        }

        ServiceProjectionDispatchBuilder projectionDispatchBuilder = this.serviceProjectionDispatchState.build();
        this.serviceBuilder.setProjectionDispatch(projectionDispatchBuilder);

        return this.serviceBuilder;
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
        // TODO: Also the class should be opportunistically locked

        return this.urlState.getServiceGroup().getKlass().hasVersion()
                && this.verbState.getVerb() != Verb.GET
                && this.verbState.getVerb() != Verb.POST
                && this.serviceMultiplicityState.getServiceMultiplicity() == ServiceMultiplicity.ONE
                && !this.hasServiceCriteriaKeyword("conflict");
    }
}
