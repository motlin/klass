package cool.klass.model.meta.domain.service;

import java.util.Objects;
import java.util.Optional;

import javax.annotation.Nonnull;

import cool.klass.model.meta.domain.AbstractElement;
import cool.klass.model.meta.domain.api.criteria.Criteria;
import cool.klass.model.meta.domain.api.service.Service;
import cool.klass.model.meta.domain.api.service.ServiceMultiplicity;
import cool.klass.model.meta.domain.api.service.Verb;
import cool.klass.model.meta.domain.criteria.AbstractCriteria.CriteriaBuilder;
import cool.klass.model.meta.domain.service.ServiceProjectionDispatchImpl.ServiceProjectionDispatchBuilder;
import cool.klass.model.meta.domain.service.url.UrlImpl;
import cool.klass.model.meta.domain.service.url.UrlImpl.UrlBuilder;
import org.antlr.v4.runtime.ParserRuleContext;

public final class ServiceImpl extends AbstractElement implements Service
{
    @Nonnull
    private final UrlImpl                       url;
    @Nonnull
    private final Verb                          verb;
    @Nonnull
    private final ServiceMultiplicity           serviceMultiplicity;
    private       Optional<Criteria>            queryCriteria;
    private       Optional<Criteria>            authorizeCriteria;
    private       Optional<Criteria>            validateCriteria;
    private       Optional<Criteria>            conflictCriteria;
    private       Optional<Criteria>            versionCriteria;
    private       ServiceProjectionDispatchImpl projectionDispatch;

    private ServiceImpl(
            @Nonnull ParserRuleContext elementContext,
            boolean inferred,
            @Nonnull UrlImpl url,
            @Nonnull Verb verb,
            @Nonnull ServiceMultiplicity serviceMultiplicity)
    {
        super(elementContext, inferred);
        this.url = Objects.requireNonNull(url);
        this.verb = Objects.requireNonNull(verb);
        this.serviceMultiplicity = Objects.requireNonNull(serviceMultiplicity);
    }

    @Override
    @Nonnull
    public UrlImpl getUrl()
    {
        return this.url;
    }

    @Override
    @Nonnull
    public Verb getVerb()
    {
        return this.verb;
    }

    @Override
    @Nonnull
    public ServiceMultiplicity getServiceMultiplicity()
    {
        return this.serviceMultiplicity;
    }

    @Override
    public Optional<Criteria> getQueryCriteria()
    {
        return this.queryCriteria;
    }

    private void setQueryCriteria(@Nonnull Optional<Criteria> queryCriteria)
    {
        if (this.queryCriteria != null)
        {
            throw new IllegalStateException();
        }
        this.queryCriteria = Objects.requireNonNull(queryCriteria);
    }

    @Override
    public Optional<Criteria> getAuthorizeCriteria()
    {
        return this.authorizeCriteria;
    }

    private void setAuthorizeCriteria(@Nonnull Optional<Criteria> authorizeCriteria)
    {
        if (this.authorizeCriteria != null)
        {
            throw new IllegalStateException();
        }
        this.authorizeCriteria = Objects.requireNonNull(authorizeCriteria);
    }

    @Override
    public Optional<Criteria> getValidateCriteria()
    {
        return this.validateCriteria;
    }

    private void setValidateCriteria(@Nonnull Optional<Criteria> validateCriteria)
    {
        if (this.validateCriteria != null)
        {
            throw new IllegalStateException();
        }
        this.validateCriteria = Objects.requireNonNull(validateCriteria);
    }

    @Override
    public Optional<Criteria> getConflictCriteria()
    {
        return this.conflictCriteria;
    }

    private void setConflictCriteria(@Nonnull Optional<Criteria> conflictCriteria)
    {
        if (this.conflictCriteria != null)
        {
            throw new IllegalStateException();
        }
        this.conflictCriteria = Objects.requireNonNull(conflictCriteria);
    }

    @Override
    public Optional<Criteria> getVersionCriteria()
    {
        return this.versionCriteria;
    }

    private void setVersionCriteria(@Nonnull Optional<Criteria> versionCriteria)
    {
        if (this.versionCriteria != null)
        {
            throw new IllegalStateException();
        }
        this.versionCriteria = Objects.requireNonNull(versionCriteria);
    }

    @Override
    public ServiceProjectionDispatchImpl getProjectionDispatch()
    {
        return Objects.requireNonNull(this.projectionDispatch);
    }

    private void setProjectionDispatch(ServiceProjectionDispatchImpl projectionDispatch)
    {
        if (this.projectionDispatch != null)
        {
            throw new IllegalStateException();
        }
        this.projectionDispatch = Objects.requireNonNull(projectionDispatch);
    }

    @Override
    public int getNumParameters()
    {
        int numUrlParameters       = this.url.getUrlParameters().size();
        int numVersionParameters   = this.isVersionClauseRequired() ? 1 : 0;
        int numAuthorizeParameters = this.isAuthorizeClauseRequired() ? 1 : 0;
        return numUrlParameters + numVersionParameters + numAuthorizeParameters;
    }

    @Override
    public boolean isVersionClauseRequired()
    {
        return this.serviceMultiplicity == ServiceMultiplicity.ONE
                && this.url.getServiceGroup().getKlass().getVersionProperty().isPresent();
    }

    @Override
    public boolean isAuthorizeClauseRequired()
    {
        return this.authorizeCriteria.isPresent();
    }

    public static final class ServiceBuilder extends ElementBuilder
    {
        @Nonnull
        private final UrlBuilder          urlBuilder;
        @Nonnull
        private final Verb                verb;
        @Nonnull
        private final ServiceMultiplicity serviceMultiplicity;

        private ServiceProjectionDispatchBuilder projectionDispatchBuilder;

        private Optional<CriteriaBuilder> criteria  = Optional.empty();
        private Optional<CriteriaBuilder> authorize = Optional.empty();
        private Optional<CriteriaBuilder> validate  = Optional.empty();
        private Optional<CriteriaBuilder> conflict  = Optional.empty();
        private Optional<CriteriaBuilder> version   = Optional.empty();
        private ServiceImpl               service;

        public ServiceBuilder(
                @Nonnull ParserRuleContext elementContext,
                boolean inferred,
                @Nonnull UrlBuilder urlBuilder,
                @Nonnull Verb verb,
                @Nonnull ServiceMultiplicity serviceMultiplicity)
        {
            super(elementContext, inferred);
            this.urlBuilder = Objects.requireNonNull(urlBuilder);
            this.verb = Objects.requireNonNull(verb);
            this.serviceMultiplicity = Objects.requireNonNull(serviceMultiplicity);
        }

        public void addCriteriaBuilder(@Nonnull String criteriaKeyword, @Nonnull CriteriaBuilder criteriaBuilder)
        {
            Objects.requireNonNull(criteriaKeyword);
            Objects.requireNonNull(criteriaBuilder);

            switch (criteriaKeyword)
            {
                case "criteria":
                    if (this.criteria.isPresent())
                    {
                        throw new IllegalStateException();
                    }
                    this.criteria = Optional.of(criteriaBuilder);
                    return;
                case "authorize":
                    if (this.authorize.isPresent())
                    {
                        throw new IllegalStateException();
                    }
                    this.authorize = Optional.of(criteriaBuilder);
                    return;
                case "validate":
                    if (this.validate.isPresent())
                    {
                        throw new IllegalStateException();
                    }
                    this.validate = Optional.of(criteriaBuilder);
                    return;
                case "conflict":
                    if (this.conflict.isPresent())
                    {
                        throw new IllegalStateException();
                    }
                    this.conflict = Optional.of(criteriaBuilder);
                    return;
                case "version":
                    if (this.version.isPresent())
                    {
                        throw new IllegalStateException();
                    }
                    this.version = Optional.of(criteriaBuilder);
                    return;
                default:
                    throw new AssertionError();
            }
        }

        public void setProjectionDispatch(@Nonnull ServiceProjectionDispatchBuilder projectionDispatchBuilder)
        {
            this.projectionDispatchBuilder = Objects.requireNonNull(projectionDispatchBuilder);
        }

        public ServiceImpl build()
        {
            if (this.service != null)
            {
                throw new IllegalStateException();
            }
            this.service = new ServiceImpl(
                    this.elementContext,
                    this.inferred,
                    this.urlBuilder.getUrl(),
                    this.verb,
                    this.serviceMultiplicity);

            ServiceProjectionDispatchImpl projectionDispatch = this.projectionDispatchBuilder.build();
            this.service.setProjectionDispatch(projectionDispatch);

            Optional<Criteria> queryCriteria     = this.criteria.map(CriteriaBuilder::build);
            Optional<Criteria> authorizeCriteria = this.authorize.map(CriteriaBuilder::build);
            Optional<Criteria> validateCriteria  = this.validate.map(CriteriaBuilder::build);
            Optional<Criteria> conflictCriteria  = this.conflict.map(CriteriaBuilder::build);
            Optional<Criteria> versionCriteria   = this.version.map(CriteriaBuilder::build);

            this.service.setQueryCriteria(queryCriteria);
            this.service.setAuthorizeCriteria(authorizeCriteria);
            this.service.setValidateCriteria(validateCriteria);
            this.service.setConflictCriteria(conflictCriteria);
            this.service.setVersionCriteria(versionCriteria);

            return this.service;
        }
    }
}
