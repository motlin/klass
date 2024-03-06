package cool.klass.reladomo.test.rule;

import java.util.concurrent.TimeUnit;

import javax.annotation.Nonnull;

import com.gs.fw.common.mithra.MithraManager;
import com.gs.fw.common.mithra.MithraManagerProvider;
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

    private int      previousTransactionTimeout;

    private int      transactionTimeout         = 1;
    private TimeUnit transactionTimeoutTimeUnit = TimeUnit.MINUTES;

    public ReladomoTestRule(
            String runtimeConfigFilename,
            String... testDataFileNames)
    {
        this.runtimeConfigFilename = runtimeConfigFilename;
        this.testDataFileNames = testDataFileNames;
    }

    @Nonnull
    public ReladomoTestRule transactionTimeout(int transactionTimeout, TimeUnit timeUnit)
    {
        this.transactionTimeout = transactionTimeout;
        this.transactionTimeoutTimeUnit = timeUnit;
        return this;
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

    private void before(@Nonnull String[] testDataFileNames)
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

        MithraManager mithraManager = MithraManagerProvider.getMithraManager();
        this.previousTransactionTimeout = mithraManager.getTransactionTimeout();
        int nextTransactionTimeout = Math.toIntExact(TimeUnit.SECONDS.convert(
                this.transactionTimeout,
                this.transactionTimeoutTimeUnit));
        mithraManager.setTransactionTimeout(nextTransactionTimeout);
    }

    private void after()
    {
        MithraManagerProvider.getMithraManager().setTransactionTimeout(this.previousTransactionTimeout);

        this.mithraTestResource.tearDown();
    }
}
