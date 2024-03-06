package klass.model.meta.domain.dropwizard.test;

import cool.klass.data.store.DataStore;
import cool.klass.dropwizard.configuration.AbstractKlassConfiguration;
import cool.klass.dropwizard.configuration.KlassFactory;
import cool.klass.model.converter.bootstrap.writer.KlassBootstrapWriter;
import klass.model.meta.domain.dropwizard.application.KlassBootstrappedMetaModelApplication;
import org.junit.Before;

public class AbstractBootstrappedResourceTest extends AbstractResourceTest
{
    @Before
    public void bootstrap()
    {
        AbstractKlassConfiguration configuration = RULE.getConfiguration();
        KlassFactory               klassFactory  = configuration.getKlassFactory();
        DataStore                  dataStore     = klassFactory.getDataStoreFactory().getDataStore();

        KlassBootstrappedMetaModelApplication application = RULE.getApplication();

        KlassBootstrapWriter klassBootstrapWriter = new KlassBootstrapWriter(
                application.getDomainModel(),
                dataStore);
        klassBootstrapWriter.bootstrapMetaModel();
    }
}
