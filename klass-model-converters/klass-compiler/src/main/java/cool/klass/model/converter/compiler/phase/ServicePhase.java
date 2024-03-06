package cool.klass.model.converter.compiler.phase;

import java.util.Objects;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import cool.klass.model.converter.compiler.CompilationUnit;
import cool.klass.model.converter.compiler.error.CompilerErrorHolder;
import cool.klass.model.converter.compiler.phase.criteria.CriteriaVisitor;
import cool.klass.model.converter.compiler.state.AntlrClass;
import cool.klass.model.converter.compiler.state.AntlrDomainModel;
import cool.klass.model.converter.compiler.state.AntlrEnumeration;
import cool.klass.model.converter.compiler.state.AntlrMultiplicity;
import cool.klass.model.converter.compiler.state.AntlrPrimitiveType;
import cool.klass.model.converter.compiler.state.criteria.AntlrCriteria;
import cool.klass.model.converter.compiler.state.projection.AntlrProjection;
import cool.klass.model.converter.compiler.state.service.AntlrService;
import cool.klass.model.converter.compiler.state.service.AntlrServiceCriteria;
import cool.klass.model.converter.compiler.state.service.AntlrServiceGroup;
import cool.klass.model.converter.compiler.state.service.AntlrServiceMultiplicity;
import cool.klass.model.converter.compiler.state.service.AntlrServiceProjectionDispatch;
import cool.klass.model.converter.compiler.state.service.AntlrVerb;
import cool.klass.model.converter.compiler.state.service.url.AntlrEnumerationUrlPathParameter;
import cool.klass.model.converter.compiler.state.service.url.AntlrEnumerationUrlQueryParameter;
import cool.klass.model.converter.compiler.state.service.url.AntlrParameterModifier;
import cool.klass.model.converter.compiler.state.service.url.AntlrPrimitiveUrlPathParameter;
import cool.klass.model.converter.compiler.state.service.url.AntlrPrimitiveUrlQueryParameter;
import cool.klass.model.converter.compiler.state.service.url.AntlrUrl;
import cool.klass.model.converter.compiler.state.service.url.AntlrUrlConstant;
import cool.klass.model.converter.compiler.state.service.url.AntlrUrlParameter;
import cool.klass.model.meta.domain.property.PrimitiveType;
import cool.klass.model.meta.domain.service.ServiceMultiplicity;
import cool.klass.model.meta.domain.service.Verb;
import cool.klass.model.meta.grammar.KlassParser;
import cool.klass.model.meta.grammar.KlassParser.ClassReferenceContext;
import cool.klass.model.meta.grammar.KlassParser.CriteriaExpressionContext;
import cool.klass.model.meta.grammar.KlassParser.EnumerationParameterDeclarationContext;
import cool.klass.model.meta.grammar.KlassParser.EnumerationReferenceContext;
import cool.klass.model.meta.grammar.KlassParser.IdentifierContext;
import cool.klass.model.meta.grammar.KlassParser.MultiplicityContext;
import cool.klass.model.meta.grammar.KlassParser.ParameterModifierContext;
import cool.klass.model.meta.grammar.KlassParser.PrimitiveParameterDeclarationContext;
import cool.klass.model.meta.grammar.KlassParser.PrimitiveTypeContext;
import cool.klass.model.meta.grammar.KlassParser.ProjectionReferenceContext;
import cool.klass.model.meta.grammar.KlassParser.QueryParameterListContext;
import cool.klass.model.meta.grammar.KlassParser.ServiceCriteriaDeclarationContext;
import cool.klass.model.meta.grammar.KlassParser.ServiceCriteriaKeywordContext;
import cool.klass.model.meta.grammar.KlassParser.ServiceDeclarationBodyContext;
import cool.klass.model.meta.grammar.KlassParser.ServiceDeclarationContext;
import cool.klass.model.meta.grammar.KlassParser.ServiceGroupDeclarationContext;
import cool.klass.model.meta.grammar.KlassParser.ServiceMultiplicityContext;
import cool.klass.model.meta.grammar.KlassParser.ServiceMultiplicityDeclarationContext;
import cool.klass.model.meta.grammar.KlassParser.ServiceProjectionDispatchContext;
import cool.klass.model.meta.grammar.KlassParser.UrlConstantContext;
import cool.klass.model.meta.grammar.KlassParser.UrlDeclarationContext;
import cool.klass.model.meta.grammar.KlassParser.VerbContext;
import org.antlr.v4.runtime.ParserRuleContext;
import org.eclipse.collections.api.list.ImmutableList;
import org.eclipse.collections.api.map.MutableMap;
import org.eclipse.collections.api.map.OrderedMap;
import org.eclipse.collections.impl.list.mutable.ListAdapter;

public class ServicePhase extends AbstractCompilerPhase
{
    @Nonnull
    private final AntlrDomainModel domainModelState;

    @Nullable
    private AntlrServiceGroup serviceGroupState;
    @Nullable
    private AntlrUrl          urlState;
    @Nullable
    private AntlrService      serviceState;

    @Nullable
    private Boolean inQueryParameterList;

    public ServicePhase(
            @Nonnull CompilerErrorHolder compilerErrorHolder,
            @Nonnull MutableMap<ParserRuleContext, CompilationUnit> compilationUnitsByContext,
            @Nonnull AntlrDomainModel domainModelState,
            boolean isInference)
    {
        super(compilerErrorHolder, compilationUnitsByContext, isInference);
        this.domainModelState = Objects.requireNonNull(domainModelState);
    }

    @Override
    public void enterServiceGroupDeclaration(@Nonnull ServiceGroupDeclarationContext ctx)
    {
        ClassReferenceContext classReferenceContext = ctx.classReference();
        IdentifierContext     classNameContext      = classReferenceContext.identifier();
        String                className             = classNameContext.getText();
        AntlrClass            klass                 = this.domainModelState.getClassByName(className);

        this.serviceGroupState = new AntlrServiceGroup(
                ctx,
                this.currentCompilationUnit,
                false,
                classNameContext,
                className,
                this.domainModelState.getNumTopLevelElements() + 1,
                this.packageName,
                klass);
    }

    @Override
    public void exitServiceGroupDeclaration(ServiceGroupDeclarationContext ctx)
    {
        this.domainModelState.exitServiceGroupDeclaration(this.serviceGroupState);
        this.serviceGroupState = null;
    }

    @Override
    public void enterUrlDeclaration(@Nonnull UrlDeclarationContext ctx)
    {
        this.urlState = new AntlrUrl(ctx, this.currentCompilationUnit, false, this.serviceGroupState);
        this.inQueryParameterList = false;
        this.serviceGroupState.enterUrlDeclaration(this.urlState);
    }

    @Override
    public void exitUrlDeclaration(@Nonnull UrlDeclarationContext ctx)
    {
        // TODO: It no longer makes sense to share one url for a bunch of services, because of inference like this
        if (this.urlState.getServiceStates().anySatisfy(AntlrService::needsVersionCriteria))
        {
            this.inQueryParameterList = true;

            this.runCompilerMacro(
                    ctx.getStart(),
                    ServicePhase.class.getSimpleName(),
                    // TODO: Query parameters should be generated with @NonNull or @Nullable
                    "{version: Integer[0..1] version}",
                    KlassParser::urlParameterDeclaration);
        }

        // Resolve service variable references after inferring additional parameters like version
        OrderedMap<String, AntlrUrlParameter> formalParametersByName = this.urlState.getFormalParametersByName();
        for (AntlrService serviceState : this.urlState.getServiceStates())
        {
            for (AntlrServiceCriteria serviceCriteriaState : serviceState.getServiceCriteriaStates())
            {
                AntlrCriteria criteria = serviceCriteriaState.getCriteria();
                criteria.resolveServiceVariables(formalParametersByName);
                // TODO: Type inference here?
                criteria.resolveTypes();
            }
        }

        this.urlState = null;
        this.inQueryParameterList = null;
    }

    @Override
    public void enterServiceDeclaration(@Nonnull ServiceDeclarationContext ctx)
    {
        VerbContext                           verb                                  = ctx.verb();
        ServiceDeclarationBodyContext         serviceDeclarationBodyContext         = ctx.serviceDeclarationBody();
        ServiceMultiplicityDeclarationContext serviceMultiplicityDeclarationContext = serviceDeclarationBodyContext.serviceMultiplicityDeclaration();

        AntlrVerb antlrVerb = new AntlrVerb(verb, this.currentCompilationUnit, false, Verb.valueOf(verb.getText()));

        AntlrServiceMultiplicity serviceMultiplicity = this.getServiceMultiplicity(serviceMultiplicityDeclarationContext);

        this.serviceState = new AntlrService(ctx,
                this.currentCompilationUnit, false, this.urlState, antlrVerb, serviceMultiplicity);
    }

    private AntlrServiceMultiplicity getServiceMultiplicity(@Nullable ServiceMultiplicityDeclarationContext serviceMultiplicityDeclarationContext)
    {
        if (serviceMultiplicityDeclarationContext == null)
        {
            return new AntlrServiceMultiplicity(
                    new ParserRuleContext(),
                    this.currentCompilationUnit,
                    true,
                    ServiceMultiplicity.ONE);
        }

        ServiceMultiplicityContext serviceMultiplicityContext = serviceMultiplicityDeclarationContext.serviceMultiplicity();

        return new AntlrServiceMultiplicity(
                serviceMultiplicityContext,
                this.currentCompilationUnit,
                false,
                this.getServiceMultiplicity(serviceMultiplicityContext));
    }

    @Nonnull
    private ServiceMultiplicity getServiceMultiplicity(ServiceMultiplicityContext serviceMultiplicityContext)
    {
        if (serviceMultiplicityContext.one != null)
        {
            return ServiceMultiplicity.ONE;
        }
        if (serviceMultiplicityContext.many != null)
        {
            return ServiceMultiplicity.MANY;
        }
        throw new AssertionError();
    }

    @Override
    public void exitServiceDeclaration(@Nonnull ServiceDeclarationContext ctx)
    {
        if (this.serviceState.needsVersionCriteriaInferred())
        {
            // TODO: Get names from model (system, version, number, version)
            String sourceCodeText = "            version: this.system equalsEdgePoint && this.version.number == version;";
            this.runCompilerMacro(
                    ctx.getStart(),
                    ServicePhase.class.getSimpleName(),
                    sourceCodeText,
                    KlassParser::serviceCriteriaDeclaration);
        }

        if (this.serviceState.needsConflictCriteriaInferred())
        {
            // TODO: Get names from model (version, version)
            String sourceCodeText = "            conflict: this.version.number == version;";
            this.runCompilerMacro(
                    ctx.getStart(),
                    ServicePhase.class.getSimpleName(),
                    sourceCodeText,
                    KlassParser::serviceCriteriaDeclaration);
        }

        this.urlState.exitServiceDeclaration(this.serviceState);
        this.serviceState = null;
    }

    @Override
    public void enterUrlConstant(@Nonnull UrlConstantContext ctx)
    {
        AntlrUrlConstant antlrUrlConstant = new AntlrUrlConstant(
                ctx,
                this.currentCompilationUnit,
                false,
                ctx.identifier(),
                ctx.identifier().getText(),
                this.urlState.getNumPathSegments() + 1);
        this.urlState.enterUrlConstant(antlrUrlConstant);
    }

    @Override
    public void enterQueryParameterList(QueryParameterListContext ctx)
    {
        this.inQueryParameterList = true;
    }

    @Override
    public void enterServiceCriteriaDeclaration(@Nonnull ServiceCriteriaDeclarationContext ctx)
    {
        ServiceCriteriaKeywordContext serviceCriteriaKeywordContext = ctx.serviceCriteriaKeyword();

        String serviceCriteriaKeyword = serviceCriteriaKeywordContext.getText();

        AntlrServiceCriteria serviceCriteriaState = new AntlrServiceCriteria(
                ctx,
                this.currentCompilationUnit,
                false,
                serviceCriteriaKeyword,
                this.serviceState);

        CriteriaExpressionContext criteriaExpressionContext = ctx.criteriaExpression();

        CriteriaVisitor criteriaVisitor = new CriteriaVisitor(
                this.currentCompilationUnit,
                this.domainModelState,
                serviceCriteriaState,
                this.serviceGroupState.getKlass());

        AntlrCriteria antlrCriteria = criteriaVisitor.visit(criteriaExpressionContext);

        serviceCriteriaState.setCriteria(antlrCriteria);

        this.serviceState.enterServiceCriteriaDeclaration(serviceCriteriaState);
    }

    @Override
    public void enterServiceProjectionDispatch(@Nonnull ServiceProjectionDispatchContext ctx)
    {
        ProjectionReferenceContext projectionReferenceContext = ctx.projectionReference();

        String          projectionName = projectionReferenceContext.identifier().getText();
        AntlrProjection projection     = this.domainModelState.getProjectionByName(projectionName);

        if (ctx.argumentList() != null && !ctx.argumentList().argument().isEmpty())
        {
            throw new AssertionError();
        }

        AntlrServiceProjectionDispatch projectionDispatch = new AntlrServiceProjectionDispatch(
                ctx,
                this.currentCompilationUnit,
                false,
                this.serviceState,
                projection);

        projectionDispatch.reportErrors(this.compilerErrorHolder);

        this.serviceState.enterServiceProjectionDispatch(projectionDispatch);
    }

    @Override
    public void enterPrimitiveParameterDeclaration(@Nonnull PrimitiveParameterDeclarationContext ctx)
    {
        if (this.urlState == null)
        {
            return;
        }

        IdentifierContext    identifier           = ctx.identifier();
        PrimitiveTypeContext primitiveTypeContext = ctx.primitiveType();
        MultiplicityContext  multiplicityContext  = ctx.multiplicity();

        PrimitiveType      primitiveType      = PrimitiveType.byPrettyName(primitiveTypeContext.getText());
        AntlrPrimitiveType primitiveTypeState = AntlrPrimitiveType.valueOf(primitiveType);

        AntlrMultiplicity multiplicityState = new AntlrMultiplicity(
                multiplicityContext,
                this.currentCompilationUnit,
                false);

        ImmutableList<AntlrParameterModifier> parameterModifiers = ListAdapter.adapt(ctx.parameterModifier())
                .collect(this::getAntlrParameterModifier)
                .toImmutable();

        if (this.inQueryParameterList)
        {
            AntlrPrimitiveUrlQueryParameter antlrPrimitiveUrlPathParameter = new AntlrPrimitiveUrlQueryParameter(
                    ctx,
                    this.currentCompilationUnit,
                    false,
                    identifier,
                    identifier.getText(),
                    this.urlState.getNumQueryParameters() + 1,
                    primitiveTypeState,
                    multiplicityState,
                    this.urlState,
                    parameterModifiers);

            this.urlState.enterQueryParameterDeclaration(antlrPrimitiveUrlPathParameter);
        }
        else
        {
            AntlrPrimitiveUrlPathParameter antlrPrimitiveUrlPathParameter = new AntlrPrimitiveUrlPathParameter(
                    ctx,
                    this.currentCompilationUnit,
                    false,
                    identifier,
                    identifier.getText(),
                    this.urlState.getNumPathSegments() + 1,
                    primitiveTypeState,
                    multiplicityState,
                    this.urlState,
                    parameterModifiers);

            this.urlState.enterPathParameterDeclaration(antlrPrimitiveUrlPathParameter);
        }
    }

    @Override
    public void enterEnumerationParameterDeclaration(@Nonnull EnumerationParameterDeclarationContext ctx)
    {
        IdentifierContext           identifier                  = ctx.identifier();
        EnumerationReferenceContext enumerationReferenceContext = ctx.enumerationReference();
        MultiplicityContext         multiplicityContext         = ctx.multiplicity();

        AntlrEnumeration enumerationState = this.domainModelState.getEnumerationByName(enumerationReferenceContext.getText());

        AntlrMultiplicity multiplicityState = new AntlrMultiplicity(
                multiplicityContext,
                this.currentCompilationUnit,
                false);

        ImmutableList<AntlrParameterModifier> parameterModifiers = ListAdapter.adapt(ctx.parameterModifier())
                .collect(this::getAntlrParameterModifier)
                .toImmutable();

        if (this.inQueryParameterList)
        {
            AntlrEnumerationUrlQueryParameter enumerationStateUrlPathParameter = new AntlrEnumerationUrlQueryParameter(
                    ctx,
                    this.currentCompilationUnit,
                    false,
                    identifier,
                    identifier.getText(),
                    this.urlState.getNumQueryParameters() + 1,
                    enumerationState,
                    multiplicityState,
                    this.urlState,
                    parameterModifiers);

            this.urlState.enterQueryParameterDeclaration(enumerationStateUrlPathParameter);
        }
        else
        {
            AntlrEnumerationUrlPathParameter enumerationStateUrlPathParameter = new AntlrEnumerationUrlPathParameter(
                    ctx,
                    this.currentCompilationUnit,
                    false,
                    identifier,
                    identifier.getText(),
                    this.urlState.getNumPathSegments(),
                    enumerationState,
                    multiplicityState,
                    this.urlState,
                    parameterModifiers);

            this.urlState.enterPathParameterDeclaration(enumerationStateUrlPathParameter);
        }
    }

    @Nonnull
    public AntlrParameterModifier getAntlrParameterModifier(@Nonnull ParameterModifierContext context)
    {
        return new AntlrParameterModifier(context, this.currentCompilationUnit, false, context.getText());
    }
}
