package klass.model.meta.domain.dropwizard.test;

import org.junit.Test;

public class AssociationResourceManualTest extends AbstractBootstrappedResourceTest
{
    @Test
    public void getAllMeta()
    {
        this.assertUrlReturns("getAllMeta", "/meta/association");
    }
}
