package cool.klass.model.converter.compiler.annotation.audit;

import cool.klass.model.converter.compiler.annotation.AbstractKlassCompilerErrorTestCase;
import org.junit.Test;

public class AuditWithoutTemporalErrorTest
        extends AbstractKlassCompilerErrorTestCase
{
    @Test
    @Override
    public void smokeTest()
    {
        this.assertNoCompilerErrors();
    }
}
