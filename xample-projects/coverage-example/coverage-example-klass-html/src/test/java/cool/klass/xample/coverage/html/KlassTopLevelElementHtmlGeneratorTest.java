package cool.klass.xample.coverage.html;

import cool.klass.generator.klass.html.KlassTopLevelElementHtmlGenerator;
import cool.klass.model.meta.domain.api.TopLevelElement;
import cool.klass.model.meta.domain.api.source.DomainModelWithSourceCode;
import cool.klass.model.meta.domain.api.source.TopLevelElementWithSourceCode;
import cool.klass.model.meta.loader.compiler.DomainModelCompilerLoader;
import io.liftwizard.junit.rule.log.marker.LogMarkerTestRule;
import io.liftwizard.junit.rule.match.file.FileMatchRule;
import org.eclipse.collections.api.factory.Lists;
import org.eclipse.collections.api.list.ImmutableList;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestRule;

public class KlassTopLevelElementHtmlGeneratorTest
{
    @Rule
    public final FileMatchRule fileMatchRule = new FileMatchRule();

    @Rule
    public final TestRule logMarkerTestRule = new LogMarkerTestRule();

    @Test
    public void smokeTest()
    {
        ImmutableList<String> klassSourcePackages = Lists.immutable.with("cool.klass.xample.coverage");

        var domainModelCompilerLoader = new DomainModelCompilerLoader(klassSourcePackages);

        DomainModelWithSourceCode domainModel = domainModelCompilerLoader.load();

        for (TopLevelElement topLevelElement : domainModel.getTopLevelElements())
        {
            TopLevelElementWithSourceCode topLevelElementWithSourceCode =
                    (TopLevelElementWithSourceCode) topLevelElement;

            String html = KlassTopLevelElementHtmlGenerator.writeHtml(domainModel, topLevelElementWithSourceCode);

            String resourceClassPathLocation = "%s_%s.html".formatted(
                    topLevelElement.getClass().getSimpleName(),
                    topLevelElement.getName());
            this.fileMatchRule.assertFileContents(
                    resourceClassPathLocation,
                    html,
                    this.getClass());
        }
    }
}
