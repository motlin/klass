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
import cool.klass.model.converter.compiler.state.service.url.AntlrPrimitiveUrlPathParameter;
import cool.klass.model.converter.compiler.state.service.url.AntlrPrimitiveUrlQueryParameter;
import cool.klass.model.converter.compiler.state.service.url.AntlrUrl;
import cool.klass.model.converter.compiler.state.service.url.AntlrUrlConstant;
import cool.klass.model.meta.domain.property.PrimitiveType;
import cool.klass.model.meta.domain.service.ServiceMultiplicity;
import cool.klass.model.meta.domain.service.Verb;
import cool.klass.model.meta.grammar.KlassParser.ClassReferenceContext;
import cool.klass.model.meta.grammar.KlassParser.CompilationUnitContext;
import cool.klass.model.meta.grammar.KlassParser.CriteriaExpressionContext;
import cool.klass.model.meta.grammar.KlassParser.EnumerationParameterDeclarationContext;
import cool.klass.model.meta.grammar.KlassParser.EnumerationReferenceContext;
import cool.klass.model.meta.grammar.KlassParser.IdentifierContext;
import cool.klass.model.meta.grammar.KlassParser.MultiplicityContext;
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
import org.eclipse.collections.api.map.MapIterable;
import org.eclipse.collections.impl.factory.Lists;

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
            @Nonnull MapIterable<CompilationUnitContext, CompilationUnit> compilationUnitsByContext,
            @Nonnull AntlrDomainModel domainModelState)
    {
        super(compilerErrorHolder, compilationUnitsByContext);
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
    public void exitUrlDeclaration(UrlDeclarationContext ctx)
    {
        // TODO: Move this for consistency with other phases
        this.urlState.reportErrors(this.compilerErrorHolder);
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
    public void exitServiceDeclaration(ServiceDeclarationContext ctx)
    {
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
                ctx.identifier().getText());
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

        AntlrServiceCriteria antlrServiceCriteria = new AntlrServiceCriteria(
                ctx,
                this.currentCompilationUnit,
                false,
                serviceCriteriaKeyword);

        CriteriaExpressionContext criteriaExpressionContext = ctx.criteriaExpression();

        ImmutableList<ParserRuleContext> parserRuleContexts = Lists.immutable.with(
                this.serviceState.getElementContext(),
                this.urlState.getElementContext(),
                this.serviceGroupState.getElementContext());

        CriteriaVisitor criteriaVisitor = new CriteriaVisitor(
                this.currentCompilationUnit,
                this.domainModelState, antlrServiceCriteria, this.serviceGroupState.getKlass(),
                this.urlState.getFormalParametersByName());

        AntlrCriteria antlrCriteria = criteriaVisitor.visit(criteriaExpressionContext);
        antlrCriteria.reportErrors(this.compilerErrorHolder, parserRuleContexts);

        antlrServiceCriteria.setCriteria(antlrCriteria);

        this.serviceState.enterServiceCriteriaDeclaration(antlrServiceCriteria);
    }

    @Override
    public void exitServiceCriteriaDeclaration(ServiceCriteriaDeclarationContext ctx)
    {
        this.serviceState.reportErrors(this.compilerErrorHolder);
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

        PrimitiveType      primitiveType      = PrimitiveType.valueOf(primitiveTypeContext.getText());
        AntlrPrimitiveType primitiveTypeState = AntlrPrimitiveType.valueOf(primitiveType);

        AntlrMultiplicity multiplicityState = new AntlrMultiplicity(
                multiplicityContext,
                this.currentCompilationUnit,
                false);

        if (this.inQueryParameterList)
        {
            AntlrPrimitiveUrlQueryParameter antlrPrimitiveUrlPathParameter = new AntlrPrimitiveUrlQueryParameter(
                    ctx,
                    this.currentCompilationUnit,
                    false,
                    identifier,
                    identifier.getText(),
                    primitiveTypeState,
                    multiplicityState,
                    this.urlState);

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
                    primitiveTypeState,
                    multiplicityState,
                    this.urlState);

            this.urlState.enterPathParameterDeclaration(antlrPrimitiveUrlPathParameter);
        }
    }

    @Override
    public void enterEnumerationParameterDeclaration(@Nonnull EnumerationParameterDeclarationContext ctx)
    {
        IdentifierContext           identifier                  = ctx.identifier();
        EnumerationReferenceContext enumerationReferenceContext = ctx.enumerationReference();
        MultiplicityContext         multiplicityContext         = ctx.multiplicity();

        AntlrEnumeration antlrEnumeration = this.domainModelState.getEnumerationByName(enumerationReferenceContext.getText());

        AntlrMultiplicity antlrMultiplicity = new AntlrMultiplicity(
                multiplicityContext,
                this.currentCompilationUnit,
                false);

        if (this.inQueryParameterList)
        {
            AntlrEnumerationUrlQueryParameter antlrEnumerationUrlPathParameter = new AntlrEnumerationUrlQueryParameter(
                    ctx,
                    this.currentCompilationUnit,
                    false,
                    identifier,
                    identifier.getText(),
                    antlrEnumeration,
                    antlrMultiplicity,
                    this.urlState);

            this.urlState.enterQueryParameterDeclaration(antlrEnumerationUrlPathParameter);
        }
        else
        {
            AntlrEnumerationUrlPathParameter antlrEnumerationUrlPathParameter = new AntlrEnumerationUrlPathParameter(
                    ctx,
                    this.currentCompilationUnit,
                    false,
                    identifier,
                    identifier.getText(),
                    antlrEnumeration,
                    antlrMultiplicity,
                    this.urlState);

            this.urlState.enterPathParameterDeclaration(antlrEnumerationUrlPathParameter);
        }
    }

    @Override
    public void exitEnumerationParameterDeclaration(EnumerationParameterDeclarationContext ctx)
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName()
                + ".exitEnumerationParameterDeclaration() not implemented yet");
    }
}
