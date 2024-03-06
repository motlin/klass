package cool.klass.model.meta.domain.service;

import java.util.Objects;

import javax.annotation.Nonnull;

import cool.klass.model.meta.domain.Element;
import cool.klass.model.meta.domain.criteria.AllCriteria;
import cool.klass.model.meta.domain.criteria.Criteria;
import cool.klass.model.meta.domain.criteria.Criteria.CriteriaBuilder;
import cool.klass.model.meta.domain.service.ServiceProjectionDispatch.ServiceProjectionDispatchBuilder;
import cool.klass.model.meta.domain.service.url.Url;
import cool.klass.model.meta.domain.service.url.Url.UrlBuilder;
import org.antlr.v4.runtime.ParserRuleContext;

public final class Service extends Element
{
    @Nonnull
    private final Url                 url;
    @Nonnull
    private final Verb                verb;
    @Nonnull
    private final ServiceMultiplicity serviceMultiplicity;
    private       Criteria            queryCriteria;
    private       Criteria            authorizeCriteria;
    private       Criteria            validateCriteria;
    private       Criteria            conflictCriteria;

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

    private void setQueryCriteria(@Nonnull Criteria queryCriteria)
    {
        if (this.queryCriteria != null)
        {
            throw new IllegalStateException();
        }
        this.queryCriteria = Objects.requireNonNull(queryCriteria);
    }

    private void setAuthorizeCriteria(@Nonnull Criteria authorizeCriteria)
    {
        if (this.authorizeCriteria != null)
        {
            throw new IllegalStateException();
        }
        this.authorizeCriteria = Objects.requireNonNull(authorizeCriteria);
    }

    private void setValidateCriteria(@Nonnull Criteria validateCriteria)
    {
        if (this.validateCriteria != null)
        {
            throw new IllegalStateException();
        }
        this.validateCriteria = Objects.requireNonNull(validateCriteria);
    }

    private void setConflictCriteria(@Nonnull Criteria conflictCriteria)
    {
        if (this.conflictCriteria != null)
        {
            throw new IllegalStateException();
        }
        this.conflictCriteria = Objects.requireNonNull(conflictCriteria);
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

        private CriteriaBuilder criteria;
        private CriteriaBuilder authorize;
        private CriteriaBuilder validate;
        private CriteriaBuilder conflict;
        private Service         service;

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
                    if (this.criteria != null)
                    {
                        throw new IllegalStateException();
                    }
                    this.criteria = criteriaBuilder;
                    return;
                case "authorize":
                    if (this.authorize != null)
                    {
                        throw new IllegalStateException();
                    }
                    this.authorize = criteriaBuilder;
                    return;
                case "validate":
                    if (this.validate != null)
                    {
                        throw new IllegalStateException();
                    }
                    this.validate = criteriaBuilder;
                    return;
                case "conflict":
                    if (this.conflict != null)
                    {
                        throw new IllegalStateException();
                    }
                    this.conflict = criteriaBuilder;
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

            this.projectionDispatchBuilder.build();

            Criteria queryCriteria     = this.criteria == null ? AllCriteria.INSTANCE : this.criteria.build();
            Criteria authorizeCriteria = this.authorize == null ? AllCriteria.INSTANCE : this.authorize.build();
            Criteria validateCriteria  = this.validate == null ? AllCriteria.INSTANCE : this.validate.build();
            Criteria conflictCriteria  = this.conflict == null ? AllCriteria.INSTANCE : this.conflict.build();

            this.service.setQueryCriteria(queryCriteria);
            this.service.setAuthorizeCriteria(authorizeCriteria);
            this.service.setValidateCriteria(validateCriteria);
            this.service.setConflictCriteria(conflictCriteria);

            return this.service;
        }
    }
}
