package cool.klass.reladomo.test.rule;

import javax.annotation.Nonnull;

import com.gs.fw.common.mithra.test.ConnectionManagerForTests;
import com.gs.fw.common.mithra.test.MithraTestResource;
import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;

public class ReladomoTestRule implements TestRule
{
    private final String   runtimeConfigFilename;
    private final String[] testDataFileNames;

    private MithraTestResource mithraTestResource;

    public ReladomoTestRule(
            String runtimeConfigFilename,
            String... testDataFileNames)
    {
        this.runtimeConfigFilename = runtimeConfigFilename;
        this.testDataFileNames = testDataFileNames;
    }

    @Nonnull
    @Override
    public Statement apply(@Nonnull Statement base, @Nonnull Description description)
    {
        ReladomoTestFile reladomoTestFileAnnotation = description.getAnnotation(ReladomoTestFile.class);
        String[] testDataFileNames = reladomoTestFileAnnotation == null
                ? this.testDataFileNames
                : reladomoTestFileAnnotation.value();

        return new Statement()
        {
            @Override
            public void evaluate() throws Throwable
            {
                ReladomoTestRule.this.before(testDataFileNames);
                try
                {
                    base.evaluate();
                }
                finally
                {
                    ReladomoTestRule.this.after();
                }
            }
        };
    }

    private void before(String[] testDataFileNames)
    {
        this.mithraTestResource = new MithraTestResource(this.runtimeConfigFilename);
        // TODO: Make the test database name configurable
        ConnectionManagerForTests connectionManager = ConnectionManagerForTests.getInstance("testdb");
        this.mithraTestResource.createSingleDatabase(connectionManager);

        for (String testDataFileName : testDataFileNames)
        {
            this.mithraTestResource.addTestDataToDatabase(testDataFileName, connectionManager);
        }
        this.mithraTestResource.setUp();
    }

    private void after()
    {
        this.mithraTestResource.tearDown();
    }
}
