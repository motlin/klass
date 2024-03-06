/*
 * Copyright 2020 Craig Motlin
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.liftwizard.reladomo.test.resource.writer.tests;

import io.liftwizard.reladomo.test.resource.writer.ReladomoTestResourceWriter;
import io.liftwizard.reladomo.test.rule.ExecuteSqlTestRule;
import io.liftwizard.reladomo.test.rule.ReladomoInitializeTestRule;
import io.liftwizard.reladomo.test.rule.ReladomoLoadDataTestRule;
import io.liftwizard.reladomo.test.rule.ReladomoPurgeAllTestRule;
import io.liftwizard.reladomo.test.rule.ReladomoTestFile;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.RuleChain;

import static org.junit.Assert.assertEquals;

public class ReladomoTestResourceWriterTest
{
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
                + "propertiesOptionalId, optionalString                              , optionalInteger, optionalLong, optionalDouble, optionalFloat, optionalBoolean, optionalInstant        , optionalLocalDate, createdById  , createdOn                , lastUpdatedById, systemFrom               , systemTo               \n"
                + "                   1, \"PropertiesOptional optionalString 1 \\u261D\",               1, 100000000000,   1.0123456789,     1.0123457,            true, \"1999-12-31 23:59:00.0\", \"1999-12-31\"     , \"test user 1\", \"1999-12-31 23:59:59.999\", \"test user 1\"  , \"1999-12-31 23:59:59.999\", \"9999-12-01 23:59:00.0\"\n"
                + "class cool.klass.xample.coverage.PropertiesOptionalVersion\n"
                + "propertiesOptionalId, number, createdById  , createdOn                , lastUpdatedById, systemFrom               , systemTo               \n"
                + "                   1,      1, \"test user 1\", \"1999-12-31 23:59:59.999\", \"test user 1\"  , \"1999-12-31 23:59:59.999\", \"9999-12-01 23:59:00.0\"\n"
                + "class cool.klass.xample.coverage.PropertiesRequired\n"
                + "propertiesRequiredId, requiredString                              , requiredInteger, requiredLong, requiredDouble, requiredFloat, requiredBoolean, requiredInstant        , requiredLocalDate, createdById  , createdOn                , lastUpdatedById, systemFrom               , systemTo               \n"
                + "                   1, \"PropertiesRequired requiredString 1 \\u261D\",               1, 100000000000,   1.0123456789,     1.0123457,            true, \"1999-12-31 23:59:00.0\", \"1999-12-31\"     , \"test user 1\", \"1999-12-31 23:59:59.999\", \"test user 1\"  , \"1999-12-31 23:59:59.999\", \"9999-12-01 23:59:00.0\"\n"
                + "class cool.klass.xample.coverage.PropertiesRequiredVersion\n"
                + "propertiesRequiredId, number, createdById  , createdOn                , lastUpdatedById, systemFrom               , systemTo               \n"
                + "                   1,      1, \"test user 1\", \"1999-12-31 23:59:59.999\", \"test user 1\"  , \"1999-12-31 23:59:59.999\", \"9999-12-01 23:59:00.0\"\n";
        assertEquals(expected, actual);
    }
}
