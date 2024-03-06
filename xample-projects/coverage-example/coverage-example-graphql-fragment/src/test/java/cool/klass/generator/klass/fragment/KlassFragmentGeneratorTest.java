package cool.klass.generator.klass.fragment;

import cool.klass.generator.grahql.fragment.GraphQLFragmentSourceCodeGenerator;
import cool.klass.model.meta.domain.api.PackageableElement;
import cool.klass.model.meta.domain.api.source.DomainModelWithSourceCode;
import cool.klass.model.meta.loader.compiler.DomainModelCompilerLoader;
import io.liftwizard.junit.rule.log.marker.LogMarkerTestRule;
import io.liftwizard.junit.rule.match.file.FileMatchRule;
import org.eclipse.collections.api.factory.Lists;
import org.eclipse.collections.api.list.ImmutableList;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestRule;

public class KlassFragmentGeneratorTest
{
    @Rule
    public final FileMatchRule fileMatchRule = new FileMatchRule(this.getClass());

    @Rule
    public final TestRule logMarkerTestRule = new LogMarkerTestRule();

    @Test
    public void smokeTest()
    {
        ImmutableList<String> klassSourcePackages = Lists.immutable.with("cool.klass.xample.coverage");

        var domainModelCompilerLoader = new DomainModelCompilerLoader(
                klassSourcePackages,
                Thread.currentThread().getContextClassLoader(),
                DomainModelCompilerLoader::logCompilerError);

        DomainModelWithSourceCode domainModel = domainModelCompilerLoader.load();
        ImmutableList<String> packageNames = domainModel
                .getClassifiers()
                .asLazy()
                .collect(PackageableElement::getPackageName)
                .distinct()
                .toImmutableList();

        for (String packageName : packageNames)
        {
            String sourceCode = GraphQLFragmentSourceCodeGenerator.getPackageSourceCode(domainModel, packageName);

            String resourceClassPathLocation = packageName + ".graphql";

            this.fileMatchRule.assertFileContents(
                    resourceClassPathLocation,
                    sourceCode);
        }
    }
}
