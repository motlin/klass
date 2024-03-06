package cool.klass.model.converter.compiler.annotation.association;

import cool.klass.model.converter.compiler.annotation.AbstractKlassCompilerErrorTestCase;
import org.junit.Test;

public class DoubleOwnerAssociationErrorTest
        extends AbstractKlassCompilerErrorTestCase
{
    @Test
    @Override
    public void smokeTest()
    {
        this.assertNoCompilerErrors();
    }
}
