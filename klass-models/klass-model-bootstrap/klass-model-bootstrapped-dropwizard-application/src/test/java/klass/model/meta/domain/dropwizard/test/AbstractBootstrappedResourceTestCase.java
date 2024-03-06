package klass.model.meta.domain.dropwizard.test;

import com.fasterxml.jackson.databind.ObjectMapper;
import cool.klass.data.store.DataStore;
import cool.klass.dropwizard.configuration.KlassFactory;
import cool.klass.model.converter.bootstrap.writer.KlassBootstrapWriter;
import cool.klass.model.meta.domain.api.DomainModel;
import org.junit.Before;

public class AbstractBootstrappedResourceTestCase
        extends AbstractResourceTestCase
{
    @Before
    public void bootstrap()
    {
        ObjectMapper objectMapper = this.appRule.getEnvironment().getObjectMapper();
        KlassFactory klassFactory = this.appRule.getConfiguration().getKlassFactory();
        DataStore    dataStore    = klassFactory.getDataStoreFactory().createDataStore();
        DomainModel  domainModel  = klassFactory.getDomainModelFactory().createDomainModel(objectMapper);

        KlassBootstrapWriter klassBootstrapWriter = new KlassBootstrapWriter(domainModel, dataStore);
        klassBootstrapWriter.bootstrapMetaModel();
    }
}
