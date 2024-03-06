package io.liftwizard.reladomo.test.resource.writer.tests;

import io.liftwizard.junit.rule.log.marker.LogMarkerTestRule;
import io.liftwizard.reladomo.test.resource.writer.ReladomoTestResourceWriter;
import io.liftwizard.reladomo.test.rule.ExecuteSqlTestRule;
import io.liftwizard.reladomo.test.rule.ReladomoInitializeTestRule;
import io.liftwizard.reladomo.test.rule.ReladomoLoadDataTestRule;
import io.liftwizard.reladomo.test.rule.ReladomoPurgeAllTestRule;
import io.liftwizard.reladomo.test.rule.ReladomoTestFile;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.RuleChain;
import org.junit.rules.TestRule;

import static org.junit.Assert.assertEquals;

public class ReladomoTestResourceWriterTest
{
    @Rule
    public final TestRule logMarkerTestRule = new LogMarkerTestRule();

    private final ExecuteSqlTestRule executeSqlTestRule = new ExecuteSqlTestRule();

    private final ReladomoInitializeTestRule initializeTestRule =
            new ReladomoInitializeTestRule("reladomo-runtime-configuration/ReladomoRuntimeConfiguration.xml");

    private final ReladomoPurgeAllTestRule purgeAllTestRule = new ReladomoPurgeAllTestRule();
    private final ReladomoLoadDataTestRule loadDataTestRule = new ReladomoLoadDataTestRule();

    @Rule
    public final RuleChain ruleChain = RuleChain.emptyRuleChain()
            .around(this.executeSqlTestRule)
            .around(this.initializeTestRule)
            .around(this.purgeAllTestRule)
            .around(this.loadDataTestRule);

    @Test
    @ReladomoTestFile("test-data/ReladomoTestResourceWriterTest.txt")
    public void reladomoTestResourceWriter()
    {
        String actual = ReladomoTestResourceWriter.generate();
        String expected = ""
                + "class cool.klass.xample.coverage.PropertiesOptional\n"
                + "systemFrom               , systemTo               , propertiesOptionalId, optionalString      , optionalInteger, optionalLong, optionalDouble, optionalFloat, optionalBoolean, optionalInstant        , optionalLocalDate, createdById  , createdOn                , lastUpdatedById\n"
                + "\"1999-12-31 23:59:59.999\", \"9999-12-01 23:59:00.0\",                    1, \"optionalString 1 ☝\",               1, 100000000000,   1.0123456789,     1.0123457,            true, \"1999-12-31 23:59:00.0\", \"1999-12-31\"     , \"test user 1\", \"1999-12-31 23:59:59.999\", \"test user 1\"  \n"
                + "\"1999-12-31 23:59:59.999\", \"9999-12-01 23:59:00.0\",                    2, null                ,            null,         null,           null,          null,            null, null                   , null             , \"test user 1\", \"1999-12-31 23:59:59.999\", \"test user 1\"  \n"
                + "\n"
                + "class cool.klass.xample.coverage.PropertiesOptionalVersion\n"
                + "systemFrom               , systemTo               , propertiesOptionalId, number, createdById  , createdOn                , lastUpdatedById\n"
                + "\"1999-12-31 23:59:59.999\", \"9999-12-01 23:59:00.0\",                    1,      1, \"test user 1\", \"1999-12-31 23:59:59.999\", \"test user 1\"  \n"
                + "\n"
                + "class cool.klass.xample.coverage.PropertiesRequired\n"
                + "systemFrom               , systemTo               , propertiesRequiredId, requiredString      , requiredInteger, requiredLong, requiredDouble, requiredFloat, requiredBoolean, requiredInstant        , requiredLocalDate, createdById  , createdOn                , lastUpdatedById\n"
                + "\"1999-12-31 23:59:59.999\", \"9999-12-01 23:59:00.0\",                    1, \"requiredString 1 ☝\",               1, 100000000000,   1.0123456789,     1.0123457,            true, \"1999-12-31 23:59:00.0\", \"1999-12-31\"     , \"test user 1\", \"1999-12-31 23:59:59.999\", \"test user 1\"  \n"
                + "\n"
                + "class cool.klass.xample.coverage.PropertiesRequiredVersion\n"
                + "systemFrom               , systemTo               , propertiesRequiredId, number, createdById  , createdOn                , lastUpdatedById\n"
                + "\"1999-12-31 23:59:59.999\", \"9999-12-01 23:59:00.0\",                    1,      1, \"test user 1\", \"1999-12-31 23:59:59.999\", \"test user 1\"  \n";
        assertEquals(expected, actual);
    }
}
