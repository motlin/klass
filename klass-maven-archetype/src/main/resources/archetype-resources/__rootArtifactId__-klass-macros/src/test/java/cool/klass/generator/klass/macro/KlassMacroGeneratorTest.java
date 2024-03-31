package cool.klass.generator.klass.macro;

import java.util.Optional;

import cool.klass.model.meta.domain.api.source.DomainModelWithSourceCode;
import cool.klass.model.meta.domain.api.source.SourceCode;
import cool.klass.model.meta.loader.compiler.DomainModelCompilerLoader;
import io.liftwizard.junit.extension.log.marker.LogMarkerTestExtension;
import io.liftwizard.junit.extension.match.file.FileMatchExtension;
import org.eclipse.collections.api.factory.Lists;
import org.eclipse.collections.api.list.ImmutableList;
import org.eclipse.collections.api.multimap.list.ImmutableListMultimap;
import org.junit.Test;
import org.junit.jupiter.api.extension.RegisterExtension;

import static org.junit.Assert.fail;

public class KlassMacroGeneratorTest
{
    @RegisterExtension
    private final FileMatchExtension fileMatchExtension = new FileMatchExtension(this.getClass());

    @RegisterExtension
    private final LogMarkerTestExtension logMarkerTestExtension = new LogMarkerTestExtension();

    @Test
    public void smokeTest()
    {
        ImmutableList<String> klassSourcePackages = Lists.immutable.with("${package}");

        var domainModelCompilerLoader = new DomainModelCompilerLoader(
                klassSourcePackages,
                Thread.currentThread().getContextClassLoader(),
                DomainModelCompilerLoader::logCompilerError);

        DomainModelWithSourceCode domainModel = domainModelCompilerLoader.load();
        ImmutableList<SourceCode> sourceCodesFromMacros = domainModel
                .getSourceCodes()
                .select(each -> each.getMacroSourceCode().isPresent());
        ImmutableListMultimap<String, SourceCode> sourceCodesByFullPath = sourceCodesFromMacros.groupBy(SourceCode::getFullPathSourceName);
        sourceCodesByFullPath.forEachKeyMultiValues((fullPath, sourceCodes) ->
        {
            if (sourceCodes.size() > 1)
            {
                fail("Multiple source codes for " + fullPath);
            }
        });

        for (SourceCode sourceCode : domainModel.getSourceCodes())
        {
            Optional<SourceCode> macroSourceCode = sourceCode.getMacroSourceCode();
            if (macroSourceCode.isPresent())
            {
                String fullPathSourceName = sourceCode.getFullPathSourceName();
                String sourceCodeText     = sourceCode.getSourceCodeText();

                String resourceClassPathLocation = fullPathSourceName + ".klass";

                this.fileMatchExtension.assertFileContents(
                        resourceClassPathLocation,
                        sourceCodeText);
            }
        }
    }
}
