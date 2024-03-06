package cool.klass.model.meta.domain.api.service;

import java.util.Optional;

import javax.annotation.Nonnull;

import cool.klass.model.meta.domain.api.Element;
import cool.klass.model.meta.domain.api.criteria.Criteria;
import cool.klass.model.meta.domain.api.service.url.Url;

public interface Service extends Element
{
    @Nonnull
    Url getUrl();

    @Nonnull
    Verb getVerb();

    @Nonnull
    ServiceMultiplicity getServiceMultiplicity();

    Optional<Criteria> getQueryCriteria();

    Optional<Criteria> getAuthorizeCriteria();

    Optional<Criteria> getValidateCriteria();

    Optional<Criteria> getConflictCriteria();

    Optional<Criteria> getVersionCriteria();

    ServiceProjectionDispatch getProjectionDispatch();

    default int getNumParameters()
    {
        int numUrlParameters       = this.getUrl().getUrlParameters().size();
        int numVersionParameters   = this.isVersionClauseRequired() ? 1 : 0;
        int numAuthorizeParameters = this.isAuthorizeClauseRequired() ? 1 : 0;
        return numUrlParameters + numVersionParameters + numAuthorizeParameters;
    }

    default boolean isVersionClauseRequired()
    {
        return this.getServiceMultiplicity() == ServiceMultiplicity.ONE
                && this.getUrl().getServiceGroup().getKlass().getVersionClass().isPresent();
    }

    default boolean isAuthorizeClauseRequired()
    {
        return this.getAuthorizeCriteria().isPresent();
    }
}
