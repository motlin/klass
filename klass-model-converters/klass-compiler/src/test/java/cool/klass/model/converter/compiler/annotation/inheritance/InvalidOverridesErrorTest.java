package cool.klass.model.converter.compiler.annotation.inheritance;

import cool.klass.model.converter.compiler.annotation.AbstractKlassCompilerErrorTestCase;
import org.junit.Test;

public class InvalidOverridesErrorTest
        extends AbstractKlassCompilerErrorTestCase
{
    // TODO: Reject bad overrides with a compiler error

    @Test
    @Override
    public void smokeTest()
    {
        this.assertNoCompilerErrors();
    }
}
