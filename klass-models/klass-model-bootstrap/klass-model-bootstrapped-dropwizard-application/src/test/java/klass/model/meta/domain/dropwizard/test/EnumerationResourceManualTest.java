package klass.model.meta.domain.dropwizard.test;

import org.junit.Test;

public class EnumerationResourceManualTest extends AbstractBootstrappedResourceTestCase
{
    @Test
    public void getAllMeta()
    {
        this.assertUrlReturns("getAllMeta", "/meta/enumeration");
    }
}
