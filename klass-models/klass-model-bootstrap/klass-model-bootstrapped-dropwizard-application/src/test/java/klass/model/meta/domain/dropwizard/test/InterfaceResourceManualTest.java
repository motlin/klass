package klass.model.meta.domain.dropwizard.test;

import org.junit.Test;

public class InterfaceResourceManualTest extends AbstractBootstrappedResourceTest
{
    @Test
    public void getAllMeta()
    {
        this.assertUrlReturns("getAllMeta", "/meta/interface");
    }
}
