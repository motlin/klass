package io.liftwizard.reladomo.test.resource.writer.tests;

import java.util.List;

import javax.annotation.Nonnull;

import com.fasterxml.jackson.core.JsonParser.Feature;
import com.fasterxml.jackson.databind.ObjectMapper;
import cool.klass.dropwizard.configuration.domain.model.loader.compiler.DomainModelCompilerFactory;
import cool.klass.model.meta.domain.api.DomainModel;
import cool.klass.model.meta.domain.api.NamedElement;
import io.liftwizard.junit.rule.liquibase.migrations.LiquibaseTestRule;
import io.liftwizard.junit.rule.log.marker.LogMarkerTestRule;
import io.liftwizard.junit.rule.match.file.FileMatchRule;
import io.liftwizard.reladomo.test.resource.writer.ReladomoTestResourceWriter;
import io.liftwizard.reladomo.test.rule.ReladomoInitializeTestRule;
import io.liftwizard.reladomo.test.rule.ReladomoLoadDataTestRule;
import io.liftwizard.reladomo.test.rule.ReladomoTestFile;
import org.eclipse.collections.api.list.ImmutableList;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.RuleChain;
import org.junit.rules.TestRule;

public class ReladomoTestResourceWriterTest
{
    @Rule
    public final FileMatchRule fileMatchRule = new FileMatchRule(this.getClass());

    @Rule
    public final TestRule logMarkerTestRule = new LogMarkerTestRule();

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
        this.fileMatchRule.assertFileContents(resourceClassPathLocation, actual);
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
