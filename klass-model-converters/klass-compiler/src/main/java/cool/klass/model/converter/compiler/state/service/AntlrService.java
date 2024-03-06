package cool.klass.model.converter.compiler.state.service;

import java.util.Objects;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import cool.klass.model.converter.compiler.CompilationUnit;
import cool.klass.model.converter.compiler.error.CompilerErrorHolder;
import cool.klass.model.converter.compiler.state.AntlrElement;
import cool.klass.model.converter.compiler.state.criteria.AntlrCriteria;
import cool.klass.model.converter.compiler.state.service.url.AntlrUrl;
import cool.klass.model.meta.domain.criteria.Criteria.CriteriaBuilder;
import cool.klass.model.meta.domain.service.Service.ServiceBuilder;
import cool.klass.model.meta.domain.service.ServiceMultiplicity;
import cool.klass.model.meta.domain.service.ServiceProjectionDispatch.ServiceProjectionDispatchBuilder;
import cool.klass.model.meta.domain.service.Verb;
import cool.klass.model.meta.domain.service.url.Url.UrlBuilder;
import org.antlr.v4.runtime.ParserRuleContext;
import org.eclipse.collections.api.bag.ImmutableBag;
import org.eclipse.collections.api.list.ImmutableList;
import org.eclipse.collections.api.list.MutableList;
import org.eclipse.collections.impl.factory.Lists;

public class AntlrService extends AntlrElement
{
    @Nonnull
    public static final AntlrService AMBIGUOUS = new AntlrService(
            new ParserRuleContext(),
            null,
            true,
            AntlrUrl.AMBIGUOUS,
            AntlrVerb.AMBIGUOUS,
            AntlrServiceMultiplicity.AMBIGUOUS);

    @Nonnull
    private final AntlrUrl                 urlState;
    @Nonnull
    private final AntlrVerb                verbState;
    @Nonnull
    private final AntlrServiceMultiplicity serviceMultiplicityState;

    private final MutableList<AntlrServiceCriteria> serviceCriteriaStates = Lists.mutable.empty();

    private AntlrServiceProjectionDispatch serviceProjectionDispatchState;
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
    public AntlrVerb getVerbState()
    {
        return this.verbState;
    }

    public void reportDuplicateVerb(@Nonnull CompilerErrorHolder compilerErrorHolder)
    {
        String message = String.format("ERR_DUP_VRB: Duplicate verb: '%s'.", this.verbState.getVerb());

        compilerErrorHolder.add(
                this.compilationUnit,
                message,
                this.verbState.getElementContext(),
                this.urlState.getElementContext(),
                this.urlState.getServiceGroup().getElementContext());
    }

    public void enterServiceCriteriaDeclaration(AntlrServiceCriteria antlrServiceCriteria)
    {
        this.serviceCriteriaStates.add(antlrServiceCriteria);
    }

    public void enterServiceProjectionDispatch(@Nonnull AntlrServiceProjectionDispatch projectionDispatch)
    {
        this.serviceProjectionDispatchState = Objects.requireNonNull(projectionDispatch);
    }

    public void reportErrors(CompilerErrorHolder compilerErrorHolder)
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
    }

    public ImmutableList<ParserRuleContext> getParserRuleContexts()
    {
        MutableList<ParserRuleContext> parserRuleContexts = Lists.mutable.empty();
        this.getParserRuleContexts(parserRuleContexts);
        return parserRuleContexts.toImmutable();
    }

    private void getParserRuleContexts(MutableList<ParserRuleContext> parserRuleContexts)
    {
        parserRuleContexts.add(this.getElementContext());
        this.urlState.getParserRuleContexts(parserRuleContexts);
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
        this.serviceBuilder = new ServiceBuilder(this.elementContext, urlBuilder, verb, serviceMultiplicity);

        for (AntlrServiceCriteria serviceCriteriaState : this.serviceCriteriaStates)
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
}
