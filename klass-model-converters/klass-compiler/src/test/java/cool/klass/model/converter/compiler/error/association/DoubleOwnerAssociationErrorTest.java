package cool.klass.model.converter.compiler.error.association;

import cool.klass.model.converter.compiler.error.AbstractKlassCompilerErrorTestCase;
import org.junit.Test;

public class DoubleOwnerAssociationErrorTest
        extends AbstractKlassCompilerErrorTestCase
{
    @Test
    @Override
    public void smokeTest()
    {
        // TODO: It should be an error, or maybe just a warning, for two classes to own the same other class
        this.assertNoCompilerErrors();
    }
}
