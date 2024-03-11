package klass.model.meta.domain.dropwizard.test;

import org.junit.Test;

public class ClassResourceManualTest
        extends AbstractResourceTestCase
{
    @Test
    public void getAllMeta()
    {
        this.assertUrlReturns("getAllMeta", "/meta/class");
    }
}
