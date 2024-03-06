package io.liftwizard.reladomo.test.resource.writer.tests;

import java.util.List;

import javax.annotation.Nonnull;

import com.fasterxml.jackson.core.JsonParser.Feature;
import com.fasterxml.jackson.databind.ObjectMapper;
import cool.klass.dropwizard.configuration.domain.model.loader.compiler.DomainModelCompilerFactory;
import cool.klass.model.meta.domain.api.DomainModel;
import cool.klass.model.meta.domain.api.NamedElement;
import io.liftwizard.junit.rule.log.marker.LogMarkerTestRule;
import io.liftwizard.reladomo.test.resource.writer.ReladomoTestResourceWriter;
import io.liftwizard.reladomo.test.rule.ExecuteSqlTestRule;
import io.liftwizard.reladomo.test.rule.ReladomoInitializeTestRule;
import io.liftwizard.reladomo.test.rule.ReladomoLoadDataTestRule;
import io.liftwizard.reladomo.test.rule.ReladomoPurgeAllTestRule;
import io.liftwizard.reladomo.test.rule.ReladomoTestFile;
import org.eclipse.collections.api.list.ImmutableList;
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

    private final ObjectMapper objectMapper = getObjectMapper();
    private final DomainModel  domainModel  = getDomainModel(this.objectMapper);

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
        ImmutableList<String> classNames = this.domainModel.getClasses().collect(NamedElement::getName);
        String                actual     = ReladomoTestResourceWriter.generate(classNames);
        String expected = """
                class cool.klass.xample.coverage.PropertiesRequired
                systemFrom               , systemTo               , propertiesRequiredId, requiredString      , requiredInteger, requiredLong, requiredDouble, requiredFloat, requiredBoolean, requiredInstant        , requiredLocalDate, createdById  , createdOn                , lastUpdatedById
                "1999-12-31 23:59:59.999", "9999-12-01 23:59:00.0",                    1, "requiredString 1 ☝",               1, 100000000000,   1.0123456789,     1.0123457,            true, "1999-12-31 23:59:00.0", "1999-12-31"     , "test user 1", "1999-12-31 23:59:59.999", "test user 1" \s

                class cool.klass.xample.coverage.PropertiesOptional
                systemFrom               , systemTo               , propertiesOptionalId, optionalString      , optionalInteger, optionalLong, optionalDouble, optionalFloat, optionalBoolean, optionalInstant        , optionalLocalDate, createdById  , createdOn                , lastUpdatedById
                "1999-12-31 23:59:59.999", "9999-12-01 23:59:00.0",                    1, "optionalString 1 ☝",               1, 100000000000,   1.0123456789,     1.0123457,            true, "1999-12-31 23:59:00.0", "1999-12-31"     , "test user 1", "1999-12-31 23:59:59.999", "test user 1" \s
                "1999-12-31 23:59:59.999", "9999-12-01 23:59:00.0",                    2, null                ,            null,         null,           null,          null,            null, null                   , null             , "test user 1", "1999-12-31 23:59:59.999", "test user 1" \s

                class cool.klass.xample.coverage.PropertiesRequiredVersion
                systemFrom               , systemTo               , propertiesRequiredId, number, createdById  , createdOn                , lastUpdatedById
                "1999-12-31 23:59:59.999", "9999-12-01 23:59:00.0",                    1,      1, "test user 1", "1999-12-31 23:59:59.999", "test user 1" \s

                class cool.klass.xample.coverage.PropertiesOptionalVersion
                systemFrom               , systemTo               , propertiesOptionalId, number, createdById  , createdOn                , lastUpdatedById
                "1999-12-31 23:59:59.999", "9999-12-01 23:59:00.0",                    1,      1, "test user 1", "1999-12-31 23:59:59.999", "test user 1" \s
                """;

        assertEquals(expected, actual);
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
