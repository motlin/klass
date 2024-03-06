package klass.model.meta.domain.dropwizard.test;

import org.junit.Test;

public class MetaResourceManualTest
        extends AbstractResourceTestCase
{
    @Test
    public void metaEnumeration()
    {
        this.assertUrlReturns("metaEnumeration", "/meta/enumeration/PrimitiveType");
    }

    @Test
    public void metaInterface()
    {
        this.assertUrlReturns("metaInterface", "/meta/interface/NamedElement");
    }

    @Test
    public void metaClass()
    {
        this.assertUrlReturns("metaClass", "/meta/class/Classifier");
    }

    @Test
    public void metaAssociation()
    {
        this.assertUrlReturns("metaAssociation", "/meta/association/DataTypePropertyHasModifiers");
    }

    @Test
    public void metaProjection()
    {
        this.assertUrlReturns("metaProjection", "/meta/projection/ProjectionElementProjection");
    }

    @Test
    public void metaServiceGroup()
    {
        this.assertUrlReturns("metaServiceGroup", "/meta/serviceGroup/ServiceGroupResource");
    }
}
