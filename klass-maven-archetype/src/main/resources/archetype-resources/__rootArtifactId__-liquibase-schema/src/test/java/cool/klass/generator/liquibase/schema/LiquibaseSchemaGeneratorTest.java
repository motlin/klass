package cool.klass.generator.liquibase.schema;

import cool.klass.model.meta.domain.api.source.DomainModelWithSourceCode;
import cool.klass.model.meta.loader.compiler.DomainModelCompilerLoader;
import io.liftwizard.junit.extension.log.marker.LogMarkerTestExtension;
import org.eclipse.collections.api.list.ImmutableList;
import org.eclipse.collections.impl.factory.Lists;
import org.junit.Test;

public class LiquibaseSchemaGeneratorTest
{
    public static final String FULLY_QUALIFIED_PACKAGE = "${package}";

    @RegisterExtension
    private final FileMatchExtension fileMatchExtension = new FileMatchExtension(this.getClass());

    @RegisterExtension
    private final LogMarkerTestExtension logMarkerTestExtension = new LogMarkerTestExtension();

    @Test
    public void smokeTest()
    {
        ImmutableList<String> klassSourcePackages = Lists.immutable.with(FULLY_QUALIFIED_PACKAGE);

        var domainModelCompilerLoader = new DomainModelCompilerLoader(
                klassSourcePackages,
                Thread.currentThread().getContextClassLoader(),
                DomainModelCompilerLoader::logCompilerError);

        DomainModelWithSourceCode domainModel = domainModelCompilerLoader.load();

        this.fileMatchExtension.assertFileContents(
                this.getClass().getCanonicalName() + ".xml",
                SchemaGenerator.getSourceCode(domainModel, FULLY_QUALIFIED_PACKAGE));
    }
}
