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

package cool.klass.generator.relational.schema;

import java.util.Optional;

import com.google.common.base.CaseFormat;
import com.google.common.base.Converter;
import cool.klass.model.meta.domain.api.Klass;
import cool.klass.model.meta.domain.api.source.DomainModelWithSourceCode;
import cool.klass.model.meta.loader.compiler.DomainModelCompilerLoader;
import io.liftwizard.junit.rule.log.marker.LogMarkerTestRule;
import io.liftwizard.junit.rule.match.file.FileMatchRule;
import org.eclipse.collections.api.factory.Lists;
import org.eclipse.collections.api.list.ImmutableList;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestRule;

public class RelationalSchemaGeneratorTest
{
    private static final Converter<String, String> CONVERTER =
            CaseFormat.UPPER_CAMEL.converterTo(CaseFormat.UPPER_UNDERSCORE);

    @Rule
    public final         FileMatchRule             fileMatchRule = new FileMatchRule(this.getClass());

    @Rule
    public final TestRule logMarkerTestRule = new LogMarkerTestRule();

    @Test
    public void smokeTest()
    {
        ImmutableList<String> klassSourcePackages = Lists.immutable.with("cool.klass.generator.relational.schema");

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
