package klass.model.meta.domain.dropwizard.application;

import javax.annotation.Nonnull;

import cool.klass.dropwizard.bundle.reladomo.jsonview.ReladomoJsonViewBundle;
import cool.klass.dropwizard.bundle.reladomo.response.ReladomoResponseBundle;
import io.dropwizard.setup.Bootstrap;
import io.liftwizard.dropwizard.bundle.ddl.executor.DdlExecutorBundle;
import io.liftwizard.dropwizard.bundle.h2.H2Bundle;
import io.liftwizard.dropwizard.bundle.named.data.source.NamedDataSourceBundle;
import io.liftwizard.dropwizard.bundle.reladomo.ReladomoBundle;
import io.liftwizard.dropwizard.bundle.reladomo.connection.manager.ConnectionManagerBundle;
import io.liftwizard.dropwizard.bundle.reladomo.connection.manager.holder.ConnectionManagerHolderBundle;

public class KlassBootstrappedMetaModelApplication extends AbstractKlassBootstrappedMetaModelApplication
{
    public static void main(String[] args) throws Exception
    {
        new KlassBootstrappedMetaModelApplication().run(args);
    }

    @Override
    protected void initializeDynamicBundles(@Nonnull Bootstrap<KlassBootstrappedMetaModelConfiguration> bootstrap)
    {
    }

    @Override
    protected void initializeBundles(@Nonnull Bootstrap<KlassBootstrappedMetaModelConfiguration> bootstrap)
    {
        super.initializeBundles(bootstrap);

        bootstrap.addBundle(new H2Bundle());
        bootstrap.addBundle(new NamedDataSourceBundle());
        bootstrap.addBundle(new DdlExecutorBundle());
        bootstrap.addBundle(new ConnectionManagerBundle());
        bootstrap.addBundle(new ConnectionManagerHolderBundle());
        bootstrap.addBundle(new ReladomoBundle());
        bootstrap.addBundle(new ReladomoJsonViewBundle());
        bootstrap.addBundle(new ReladomoResponseBundle());
    }
}
