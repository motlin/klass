package klass.model.meta.domain.dropwizard.test;

import cool.klass.model.converter.bootstrap.writer.KlassBootstrapWriter;
import klass.model.meta.domain.dropwizard.application.KlassBootstrappedMetaModelApplication;
import org.junit.Before;

public class AbstractBootstrappedResourceTest extends AbstractResourceTest
{
    @Before
    public void bootstrap()
    {
        KlassBootstrappedMetaModelApplication application = RULE.getApplication();

        KlassBootstrapWriter klassBootstrapWriter = new KlassBootstrapWriter(
                application.getDomainModel(),
                application.getDataStore());
        klassBootstrapWriter.bootstrapMetaModel();
    }
}
