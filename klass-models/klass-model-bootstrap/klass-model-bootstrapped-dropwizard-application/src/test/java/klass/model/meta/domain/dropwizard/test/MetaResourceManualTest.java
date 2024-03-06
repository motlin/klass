package klass.model.meta.domain.dropwizard.test;

import org.junit.Test;

public class MetaResourceManualTest extends AbstractBootstrappedResourceTestCase
{
    @Test
    public void metaEnumeration()
    {
        this.assertUrlReturns("metaEnumeration", "/meta/enumeration/PrimitiveType");
    }

    @Test
    public void metaEnumerationSummary()
    {
        this.assertUrlReturns("metaEnumerationSummary", "/meta/enumeration/PrimitiveType/summary");
    }

    @Test
    public void metaInterface()
    {
        this.assertUrlReturns("metaInterface", "/meta/interface/PackageableElement");
    }

    @Test
    public void metaInterfaceSummary()
    {
        this.assertUrlReturns("metaInterfaceSummary", "/meta/interface/PackageableElement/summary");
    }

    @Test
    public void metaClass()
    {
        this.assertUrlReturns("metaClass", "/meta/class/Classifier");
    }

    @Test
    public void metaClassSummary()
    {
        this.assertUrlReturns("metaClassSummary", "/meta/class/Classifier/summary");
    }

    @Test
    public void metaAssociation()
    {
        this.assertUrlReturns("metaAssociation", "/meta/association/DataTypePropertyHasModifiers");
    }

    @Test
    public void metaAssociationSummary()
    {
        this.assertUrlReturns("metaAssociationSummary", "/meta/association/DataTypePropertyHasModifiers/summary");
    }

    @Test
    public void metaProjection()
    {
        this.assertUrlReturns("metaProjection", "/meta/projection/ProjectionElementSummaryProjection");
    }

    @Test
    public void metaServiceGroup()
    {
        this.assertUrlReturns("metaServiceGroup", "/meta/serviceGroup/ServiceGroup");
    }
}
