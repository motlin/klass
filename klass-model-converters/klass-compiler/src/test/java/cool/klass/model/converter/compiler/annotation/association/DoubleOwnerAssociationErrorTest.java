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
        // TODO: It should be an error, or maybe just a warning, for two classes to own the same other class
        this.assertNoCompilerErrors();
    }
}
