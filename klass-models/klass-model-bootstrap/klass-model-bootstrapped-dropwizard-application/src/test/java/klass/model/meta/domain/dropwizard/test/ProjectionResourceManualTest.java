package klass.model.meta.domain.dropwizard.test;

import org.junit.Test;

public class ProjectionResourceManualTest extends AbstractBootstrappedResourceTestCase
{
    @Test
    public void getAllMeta()
    {
        this.assertUrlReturns("getAllMeta", "/meta/projection");
    }
}
