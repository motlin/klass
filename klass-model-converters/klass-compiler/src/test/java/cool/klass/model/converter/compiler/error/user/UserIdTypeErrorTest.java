package cool.klass.model.converter.compiler.error.user;

import cool.klass.model.converter.compiler.error.AbstractKlassCompilerErrorTestCase;
import org.junit.Test;

public class UserIdTypeErrorTest
        extends AbstractKlassCompilerErrorTestCase
{
    @Test
    @Override
    public void smokeTest()
    {
        this.assertNoCompilerErrors();
    }
}
