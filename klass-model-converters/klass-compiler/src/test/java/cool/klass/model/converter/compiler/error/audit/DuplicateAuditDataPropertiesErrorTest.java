package cool.klass.model.converter.compiler.error.audit;

import cool.klass.model.converter.compiler.error.AbstractKlassCompilerErrorTestCase;
import org.junit.Test;

public class DuplicateAuditDataPropertiesErrorTest
        extends AbstractKlassCompilerErrorTestCase
{
    @Test
    @Override
    public void smokeTest()
    {
        this.assertNoCompilerErrors();
    }
}
