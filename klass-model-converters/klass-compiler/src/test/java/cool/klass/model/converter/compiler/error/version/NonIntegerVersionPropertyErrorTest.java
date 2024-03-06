package cool.klass.model.converter.compiler.error.version;

import cool.klass.model.converter.compiler.error.AbstractKlassCompilerErrorTestCase;
import org.junit.Test;

public class NonIntegerVersionPropertyErrorTest
        extends AbstractKlassCompilerErrorTestCase
{
    // TODO: reject non-Integer version types

    @Test
    @Override
    public void smokeTest()
    {
        this.assertNoCompilerErrors();
    }
}
