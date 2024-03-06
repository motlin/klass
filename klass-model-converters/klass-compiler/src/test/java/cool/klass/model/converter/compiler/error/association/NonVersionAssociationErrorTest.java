package cool.klass.model.converter.compiler.error.association;

import cool.klass.model.converter.compiler.error.AbstractKlassCompilerErrorTestCase;
import org.junit.Test;

public class NonVersionAssociationErrorTest
        extends AbstractKlassCompilerErrorTestCase
{
    @Test
    @Override
    public void smokeTest()
    {
        this.assertNoCompilerErrors();
    }
}
