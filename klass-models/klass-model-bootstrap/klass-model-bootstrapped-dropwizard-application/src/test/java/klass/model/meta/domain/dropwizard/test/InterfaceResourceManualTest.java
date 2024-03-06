package klass.model.meta.domain.dropwizard.test;

import org.junit.Test;

public class InterfaceResourceManualTest
        extends AbstractResourceTestCase
{
    @Test
    public void getAllMeta()
    {
        this.assertUrlReturns("getAllMeta", "/meta/interface");
    }
}
