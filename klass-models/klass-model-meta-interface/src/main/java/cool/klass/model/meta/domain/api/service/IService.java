package cool.klass.model.meta.domain.api.service;

import java.util.Optional;

import javax.annotation.Nonnull;

import cool.klass.model.meta.domain.api.IElement;
import cool.klass.model.meta.domain.api.criteria.ICriteria;
import cool.klass.model.meta.domain.api.service.url.IUrl;

public interface IService extends IElement
{
    @Nonnull
    IUrl getUrl();

    @Nonnull
    Verb getVerb();

    @Nonnull
    ServiceMultiplicity getServiceMultiplicity();

    Optional<ICriteria> getQueryCriteria();

    Optional<ICriteria> getAuthorizeCriteria();

    Optional<ICriteria> getValidateCriteria();

    Optional<ICriteria> getConflictCriteria();

    Optional<ICriteria> getVersionCriteria();

    IServiceProjectionDispatch getProjectionDispatch();

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
