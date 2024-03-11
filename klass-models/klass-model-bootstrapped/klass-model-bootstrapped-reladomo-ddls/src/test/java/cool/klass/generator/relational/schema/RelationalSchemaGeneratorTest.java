package cool.klass.generator.relational.schema;

import java.util.Optional;

import com.google.common.base.CaseFormat;
import com.google.common.base.Converter;
import cool.klass.model.meta.domain.api.Klass;
import cool.klass.model.meta.domain.api.source.DomainModelWithSourceCode;
import cool.klass.model.meta.loader.compiler.DomainModelCompilerLoader;
import io.liftwizard.junit.rule.log.marker.LogMarkerTestRule;
import io.liftwizard.junit.rule.match.file.FileMatchRule;
import org.eclipse.collections.api.list.ImmutableList;
import org.eclipse.collections.impl.factory.Lists;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestRule;

public class RelationalSchemaGeneratorTest
{
    private static final Converter<String, String> CONVERTER =
            CaseFormat.UPPER_CAMEL.converterTo(CaseFormat.UPPER_UNDERSCORE);

    @Rule
    public final FileMatchRule fileMatchRule = new FileMatchRule(this.getClass());

    @Rule
    public final TestRule logMarkerTestRule = new LogMarkerTestRule();

    @Test
    public void smokeTest()
    {
        ImmutableList<String> klassSourcePackages = Lists.immutable.with("klass.model.meta.domain");

        var domainModelCompilerLoader = new DomainModelCompilerLoader(
                klassSourcePackages,
                Thread.currentThread().getContextClassLoader(),
                DomainModelCompilerLoader::logCompilerError);

        DomainModelWithSourceCode domainModel = domainModelCompilerLoader.load();

        for (Klass klass : domainModel.getClasses())
        {
            String tableName = CONVERTER.convert(klass.getName());

            String ddlSourceCode = DdlGenerator.getDdl(klass);
            this.fileMatchRule.assertFileContents(
                    tableName + ".ddl",
                    ddlSourceCode);

            String idxSourceCode = IdxGenerator.getIdx(klass);
            this.fileMatchRule.assertFileContents(
                    tableName + ".idx",
                    idxSourceCode);

            Optional<String> maybeFkSourceCode = FkGenerator.getFk(klass);
            maybeFkSourceCode.ifPresent(fkSourceCode -> this.fileMatchRule.assertFileContents(
                    tableName + ".fk",
                    fkSourceCode));
        }
    }
}
