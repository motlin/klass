/*
 * Copyright 2024 Craig Motlin
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

import java.util.List;

import javax.annotation.Nonnull;

import com.fasterxml.jackson.core.JsonParser.Feature;
import com.fasterxml.jackson.databind.ObjectMapper;
import cool.klass.dropwizard.configuration.domain.model.loader.compiler.DomainModelCompilerFactory;
import cool.klass.model.meta.domain.api.DomainModel;
import cool.klass.model.meta.domain.api.NamedElement;
import io.liftwizard.junit.extension.log.marker.LogMarkerTestExtension;
import io.liftwizard.junit.extension.match.file.FileMatchExtension;
import io.liftwizard.junit.rule.liquibase.migrations.LiquibaseTestRule;
import io.liftwizard.reladomo.test.resource.writer.ReladomoTestResourceWriter;
import io.liftwizard.reladomo.test.rule.ReladomoInitializeTestRule;
import io.liftwizard.reladomo.test.rule.ReladomoLoadDataTestRule;
import io.liftwizard.reladomo.test.rule.ReladomoTestFile;
import org.eclipse.collections.api.list.ImmutableList;
import org.junit.Rule;
import org.junit.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.junit.rules.RuleChain;

public class ReladomoTestResourceWriterTest
{
    @RegisterExtension
    private final FileMatchExtension fileMatchExtension = new FileMatchExtension(this.getClass());

    @RegisterExtension
    private final LogMarkerTestExtension logMarkerTestExtension = new LogMarkerTestExtension();

    private final LiquibaseTestRule liquibaseTestRule = new LiquibaseTestRule(
            "cool/klass/xample/coverage/migrations.xml");

    private final ReladomoInitializeTestRule initializeTestRule =
            new ReladomoInitializeTestRule("reladomo-runtime-configuration/ReladomoRuntimeConfiguration.xml");

    private final ReladomoLoadDataTestRule loadDataTestRule = new ReladomoLoadDataTestRule();

    private final ObjectMapper objectMapper = getObjectMapper();
    private final DomainModel  domainModel  = getDomainModel(this.objectMapper);

    @Rule
    public final RuleChain ruleChain = RuleChain.emptyRuleChain()
            .around(this.liquibaseTestRule)
            .around(this.initializeTestRule)
            .around(this.loadDataTestRule);

    @Test
    @ReladomoTestFile("test-data/ReladomoTestResourceWriterTest.txt")
    public void reladomoTestResourceWriter()
    {
        ImmutableList<String> classNames = this.domainModel.getClasses().collect(NamedElement::getName);
        String                actual     = ReladomoTestResourceWriter.generate(classNames);

        String resourceClassPathLocation = this.getClass().getSimpleName() + ".reladomoTestResourceWriter.txt";
        this.fileMatchExtension.assertFileContents(resourceClassPathLocation, actual);
    }

    @Nonnull
    private static ObjectMapper getObjectMapper()
    {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(Feature.ALLOW_COMMENTS, true);
        objectMapper.configure(Feature.ALLOW_YAML_COMMENTS, true);
        objectMapper.configure(Feature.ALLOW_UNQUOTED_CONTROL_CHARS, true);
        objectMapper.configure(Feature.ALLOW_TRAILING_COMMA, true);
        return objectMapper;
    }

    private static DomainModel getDomainModel(ObjectMapper objectMapper)
    {
        DomainModelCompilerFactory domainModelCompilerFactory = new DomainModelCompilerFactory();
        domainModelCompilerFactory.setSourcePackages(List.of("cool.klass.xample.coverage"));
        return domainModelCompilerFactory.createDomainModel(objectMapper);
    }
}
