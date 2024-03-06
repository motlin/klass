package klass.model.meta.domain.dropwizard.test;

import org.junit.Test;

public class EnumerationResourceManualTest
        extends AbstractResourceTestCase
{
    @Test
    public void getAllMeta()
    {
        this.assertUrlReturns("getAllMeta", "/meta/enumeration");
    }
}
