package cool.klass.model.converter.compiler.error.version;

import cool.klass.model.converter.compiler.error.AbstractKlassCompilerErrorTestCase;
import org.junit.Test;

public class DuplicateVersionPropertyErrorTest
        extends AbstractKlassCompilerErrorTestCase
{
    // TODO: reject duplicate version properties

    @Test
    @Override
    public void smokeTest()
    {
        this.assertNoCompilerErrors();
    }
}
