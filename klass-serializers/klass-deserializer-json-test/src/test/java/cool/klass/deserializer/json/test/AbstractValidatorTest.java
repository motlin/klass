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

package cool.klass.deserializer.json.test;

import java.io.IOException;
import java.util.List;

import javax.annotation.Nonnull;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import cool.klass.deserializer.json.JsonTypeCheckingValidator;
import cool.klass.deserializer.json.OperationMode;
import cool.klass.deserializer.json.RequiredPropertiesValidator;
import cool.klass.dropwizard.configuration.domain.model.loader.compiler.DomainModelCompilerFactory;
import cool.klass.model.meta.domain.api.DomainModel;
import cool.klass.model.meta.domain.api.Klass;
import io.dropwizard.jackson.Jackson;
import io.liftwizard.junit.rule.log.marker.LogMarkerTestRule;
import io.liftwizard.junit.rule.match.file.FileMatchRule;
import io.liftwizard.junit.rule.match.json.JsonMatchRule;
import io.liftwizard.serialization.jackson.config.ObjectMapperConfig;
import org.eclipse.collections.api.list.MutableList;
import org.eclipse.collections.impl.factory.Lists;
import org.junit.Rule;
import org.junit.rules.TestRule;

public abstract class AbstractValidatorTest
{
    @Rule
    public final FileMatchRule fileMatchRule = new FileMatchRule(this.getClass());

    @Rule
    public final TestRule logMarkerTestRule = new LogMarkerTestRule();

    @Rule
    public final JsonMatchRule jsonMatchRule = new JsonMatchRule(this.getClass());

    protected final MutableList<String> actualErrors   = Lists.mutable.empty();
    protected final MutableList<String> actualWarnings = Lists.mutable.empty();

    protected final ObjectMapper objectMapper = AbstractValidatorTest.getObjectMapper();

    protected final DomainModel domainModel = AbstractValidatorTest.getDomainModel(this.objectMapper);

    protected final void validate(
            String testName) throws IOException
    {
        String incomingJsonName = this.getClass().getSimpleName() + '.' + testName + ".json5";
        String incomingJson     = FileMatchRule.slurp(incomingJsonName, this.getClass());

        ObjectNode incomingInstance = (ObjectNode) this.objectMapper.readTree(incomingJson);
        this.performValidation(incomingInstance);
        this.assertErrors(testName);
    }

    protected final void assertErrors(
            String testName)
            throws JsonProcessingException
    {
        this.jsonMatchRule.assertFileContents(
                this.getClass().getSimpleName() + '.' + testName + ".errors.json",
                this.objectMapper.writeValueAsString(this.actualErrors));

        this.jsonMatchRule.assertFileContents(
                this.getClass().getSimpleName() + '.' + testName + ".warnings.json",
                this.objectMapper.writeValueAsString(this.actualWarnings));
    }

    @Nonnull
    private static ObjectMapper getObjectMapper()
    {
        ObjectMapper objectMapper = Jackson.newObjectMapper();
        ObjectMapperConfig.configure(objectMapper);
        return objectMapper;
    }

    private static DomainModel getDomainModel(ObjectMapper objectMapper)
    {
        DomainModelCompilerFactory domainModelCompilerFactory = new DomainModelCompilerFactory();
        domainModelCompilerFactory.setSourcePackages(List.of("cool.klass.xample.coverage"));
        return domainModelCompilerFactory.createDomainModel(objectMapper);
    }

    protected final void performValidation(@Nonnull ObjectNode incomingInstance)
    {
        JsonTypeCheckingValidator.validate(
                incomingInstance,
                this.getKlass(),
                this.actualErrors);

        RequiredPropertiesValidator.validate(
                this.getKlass(),
                incomingInstance,
                this.getMode(),
                this.actualErrors,
                this.actualWarnings);
    }

    @Nonnull
    protected abstract Klass getKlass();

    @Nonnull
    protected abstract OperationMode getMode();
}
