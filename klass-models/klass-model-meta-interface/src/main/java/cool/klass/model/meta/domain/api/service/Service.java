package cool.klass.model.meta.domain.api.service;

import java.util.Optional;

import javax.annotation.Nonnull;

import cool.klass.model.meta.domain.api.Element;
import cool.klass.model.meta.domain.api.criteria.Criteria;
import cool.klass.model.meta.domain.api.order.OrderBy;
import cool.klass.model.meta.domain.api.service.url.Url;

public interface Service extends Element
{
    @Nonnull
    Url getUrl();

    @Nonnull
    Verb getVerb();

    @Nonnull
    ServiceMultiplicity getServiceMultiplicity();

    @Nonnull
    Optional<Criteria> getQueryCriteria();

    @Nonnull
    Optional<Criteria> getAuthorizeCriteria();

    @Nonnull
    Optional<Criteria> getValidateCriteria();

    @Nonnull
    Optional<Criteria> getConflictCriteria();

    @Nonnull
    Optional<Criteria> getVersionCriteria();

    ServiceProjectionDispatch getProjectionDispatch();

    @Nonnull
    Optional<OrderBy> getOrderBy();

    default int getNumParameters()
    {
        int numUrlParameters       = this.getUrl().getParameters().size();
        int numVersionParameters   = this.isVersionClauseRequired() ? 1 : 0;
        int numAuthorizeParameters = this.isAuthorizeClauseRequired() ? 1 : 0;
        return numUrlParameters + numVersionParameters + numAuthorizeParameters;
    }

    default boolean isVersionClauseRequired()
    {
        return this.getServiceMultiplicity() == ServiceMultiplicity.ONE
                && this.getUrl().getServiceGroup().getKlass().getVersionProperty().isPresent();
    }

    default boolean isAuthorizeClauseRequired()
    {
        return this.getAuthorizeCriteria().isPresent();
    }
}
