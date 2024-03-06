package cool.klass.model.converter.compiler.error.version;

import cool.klass.model.converter.compiler.error.AbstractKlassCompilerErrorTestCase;
import org.junit.Test;

public class VersionAssociationEndErrorTest
        extends AbstractKlassCompilerErrorTestCase
{
    @Test
    @Override
    public void smokeTest()
    {
        this.assertNoCompilerErrors();
    }
}
