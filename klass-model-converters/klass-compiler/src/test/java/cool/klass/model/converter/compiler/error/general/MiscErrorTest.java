package cool.klass.model.converter.compiler.error.general;

import cool.klass.model.converter.compiler.error.AbstractKlassCompilerErrorTestCase;
import org.junit.Test;

public class MiscErrorTest
        extends AbstractKlassCompilerErrorTestCase
{
    // TODO: Implement projection parameterized properties
    @Override
    @Test(expected = RuntimeException.class)
    public void smokeTest()
    {
        this.assertCompilerErrors();
    }
}
