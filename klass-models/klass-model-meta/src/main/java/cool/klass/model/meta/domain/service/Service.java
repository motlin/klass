package cool.klass.model.meta.domain.service;

import java.util.Objects;
import java.util.Optional;

import javax.annotation.Nonnull;

import cool.klass.model.meta.domain.Element;
import cool.klass.model.meta.domain.criteria.Criteria;
import cool.klass.model.meta.domain.criteria.Criteria.CriteriaBuilder;
import cool.klass.model.meta.domain.service.ServiceProjectionDispatch.ServiceProjectionDispatchBuilder;
import cool.klass.model.meta.domain.service.url.Url;
import cool.klass.model.meta.domain.service.url.Url.UrlBuilder;
import org.antlr.v4.runtime.ParserRuleContext;

public final class Service extends Element
{
    @Nonnull
    private final Url                       url;
    @Nonnull
    private final Verb                      verb;
    @Nonnull
    private final ServiceMultiplicity       serviceMultiplicity;
    private       Optional<Criteria>        queryCriteria;
    private       Optional<Criteria>        authorizeCriteria;
    private       Optional<Criteria>        validateCriteria;
    private       Optional<Criteria>        conflictCriteria;
    private       ServiceProjectionDispatch projectionDispatch;

    private Service(
            @Nonnull ParserRuleContext elementContext,
            @Nonnull Url url,
            @Nonnull Verb verb,
            @Nonnull ServiceMultiplicity serviceMultiplicity)
    {
        super(elementContext);
        this.url = Objects.requireNonNull(url);
        this.verb = Objects.requireNonNull(verb);
        this.serviceMultiplicity = Objects.requireNonNull(serviceMultiplicity);
    }

    @Nonnull
    public Url getUrl()
    {
        return this.url;
    }

    @Nonnull
    public Verb getVerb()
    {
        return this.verb;
    }

    @Nonnull
    public ServiceMultiplicity getServiceMultiplicity()
    {
        return this.serviceMultiplicity;
    }

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

    public ServiceProjectionDispatch getProjectionDispatch()
    {
        return Objects.requireNonNull(this.projectionDispatch);
    }

    private void setProjectionDispatch(ServiceProjectionDispatch projectionDispatch)
    {
        if (this.projectionDispatch != null)
        {
            throw new IllegalStateException();
        }
        this.projectionDispatch = Objects.requireNonNull(projectionDispatch);
    }

    public int getNumParameters()
    {
        int numUrlParameters       = this.url.getUrlParameters().size();
        int numVersionParameters   = this.isVersionClauseRequired() ? 1 : 0;
        int numAuthorizeParameters = this.isAuthorizeClauseRequired() ? 1 : 0;
        return numUrlParameters + numVersionParameters + numAuthorizeParameters;
    }

    public boolean isVersionClauseRequired()
    {
        return this.serviceMultiplicity == ServiceMultiplicity.ONE
                && this.url.getServiceGroup().getKlass().getVersionClass().isPresent();
    }

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
        private Service                   service;

        public ServiceBuilder(
                @Nonnull ParserRuleContext elementContext,
                @Nonnull UrlBuilder urlBuilder,
                @Nonnull Verb verb,
                @Nonnull ServiceMultiplicity serviceMultiplicity)
        {
            super(elementContext);
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
                default:
                    throw new AssertionError();
            }
        }

        public void setProjectionDispatch(@Nonnull ServiceProjectionDispatchBuilder projectionDispatchBuilder)
        {
            this.projectionDispatchBuilder = Objects.requireNonNull(projectionDispatchBuilder);
        }

        public Service build()
        {
            if (this.service != null)
            {
                throw new IllegalStateException();
            }
            this.service = new Service(
                    this.elementContext,
                    this.urlBuilder.getUrl(),
                    this.verb,
                    this.serviceMultiplicity);

            ServiceProjectionDispatch projectionDispatch = this.projectionDispatchBuilder.build();
            this.service.setProjectionDispatch(projectionDispatch);

            Optional<Criteria> queryCriteria     = this.criteria.map(CriteriaBuilder::build);
            Optional<Criteria> authorizeCriteria = this.authorize.map(CriteriaBuilder::build);
            Optional<Criteria> validateCriteria  = this.validate.map(CriteriaBuilder::build);
            Optional<Criteria> conflictCriteria  = this.conflict.map(CriteriaBuilder::build);

            this.service.setQueryCriteria(queryCriteria);
            this.service.setAuthorizeCriteria(authorizeCriteria);
            this.service.setValidateCriteria(validateCriteria);
            this.service.setConflictCriteria(conflictCriteria);

            return this.service;
        }
    }
}
