/*
 * Copyright 2020 Craig Motlin
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

package cool.klass.model.meta.loader.compiler;

import java.net.URL;
import java.util.Objects;
import java.util.regex.Pattern;

import javax.annotation.Nonnull;

import cool.klass.model.converter.compiler.CompilationResult;
import cool.klass.model.converter.compiler.CompilationUnit;
import cool.klass.model.converter.compiler.DomainModelCompilationResult;
import cool.klass.model.converter.compiler.ErrorsCompilationResult;
import cool.klass.model.converter.compiler.KlassCompiler;
import cool.klass.model.converter.compiler.error.RootCompilerError;
import cool.klass.model.meta.domain.api.source.DomainModelWithSourceCode;
import cool.klass.model.meta.loader.DomainModelLoader;
import org.eclipse.collections.api.collection.ImmutableCollection;
import org.eclipse.collections.api.list.ImmutableList;
import org.eclipse.collections.impl.factory.Lists;
import org.reflections.Reflections;
import org.reflections.scanners.ResourcesScanner;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;
import org.reflections.util.FilterBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DomainModelCompilerLoader
        implements DomainModelLoader
{
    private static final Logger LOGGER = LoggerFactory.getLogger(DomainModelCompilerLoader.class);

    @Nonnull
    private final ImmutableList<String> klassSourcePackages;
    @Nonnull
    private final ClassLoader           classLoader;

    public DomainModelCompilerLoader(@Nonnull ImmutableList<String> klassSourcePackages)
    {
        this(klassSourcePackages, Thread.currentThread().getContextClassLoader());
    }

    public DomainModelCompilerLoader(
            @Nonnull ImmutableList<String> klassSourcePackages,
            @Nonnull ClassLoader classLoader)
    {
        this.klassSourcePackages = Objects.requireNonNull(klassSourcePackages);
        this.classLoader         = Objects.requireNonNull(classLoader);
    }

    @Override
    @Nonnull
    public DomainModelWithSourceCode load()
    {
        LOGGER.info("Scanning source packages: {}", this.klassSourcePackages);

        ImmutableCollection<CompilationUnit> compilationUnits = this.getCompilationUnits();

        KlassCompiler             klassCompiler     = new KlassCompiler(compilationUnits);
        CompilationResult         compilationResult = klassCompiler.compile();
        DomainModelWithSourceCode domainModel       = this.handleResult(compilationResult);

        LOGGER.info("Completing domain model compilation.");

        return domainModel;
    }

    @Nonnull
    private ImmutableCollection<CompilationUnit> getCompilationUnits()
    {
        ImmutableList<URL> urls = this.klassSourcePackages.flatCollectWith(
                ClasspathHelper::forPackage,
                this.classLoader);
        FilterBuilder filterBuilder = new FilterBuilder();
        this.klassSourcePackages.forEach(filterBuilder::includePackage);
        ConfigurationBuilder configurationBuilder = new ConfigurationBuilder()
                .setScanners(new ResourcesScanner())
                .setUrls(urls.castToList())
                .filterInputsBy(filterBuilder);
        Reflections reflections = new Reflections(configurationBuilder);
        ImmutableList<String> klassLocations = Lists.immutable.withAll(reflections.getResources(Pattern.compile(".*\\.klass")));

        LOGGER.debug("Found source files on classpath: {}", klassLocations);

        ImmutableList<CompilationUnit> compilationUnits = klassLocations
                .collect(CompilationUnit::createFromClasspathLocation);

        if (compilationUnits.isEmpty())
        {
            String message = "Could not find any files matching *.klass in urls: " + urls;
            throw new RuntimeException(message);
        }
        return compilationUnits;
    }

    @Nonnull
    private DomainModelWithSourceCode handleResult(@Nonnull CompilationResult compilationResult)
    {
        if (compilationResult instanceof ErrorsCompilationResult)
        {
            return this.handleFailure((ErrorsCompilationResult) compilationResult);
        }

        if (compilationResult instanceof DomainModelCompilationResult)
        {
            return this.handleSuccess((DomainModelCompilationResult) compilationResult);
        }

        throw new AssertionError(compilationResult.getClass().getSimpleName());
    }

    @Nonnull
    private DomainModelWithSourceCode handleFailure(@Nonnull ErrorsCompilationResult compilationResult)
    {
        ImmutableList<RootCompilerError> compilerErrors = compilationResult.getCompilerErrors();
        for (RootCompilerError compilerError : compilerErrors)
        {
            LOGGER.warn(compilerError.toString());
        }
        throw new RuntimeException("There were compiler errors.");
    }

    @Nonnull
    private DomainModelWithSourceCode handleSuccess(@Nonnull DomainModelCompilationResult compilationResult)
    {
        return compilationResult.getDomainModel();
    }
}
