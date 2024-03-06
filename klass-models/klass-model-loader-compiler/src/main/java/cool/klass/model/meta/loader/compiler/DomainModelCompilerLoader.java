package cool.klass.model.meta.loader.compiler;

import java.net.URL;
import java.time.Duration;
import java.time.Instant;
import java.util.Objects;
import java.util.regex.Pattern;

import javax.annotation.Nonnull;

import cool.klass.model.converter.compiler.CompilationResult;
import cool.klass.model.converter.compiler.CompilationUnit;
import cool.klass.model.converter.compiler.DomainModelCompilationResult;
import cool.klass.model.converter.compiler.ErrorsCompilationResult;
import cool.klass.model.converter.compiler.KlassCompiler;
import cool.klass.model.converter.compiler.annotation.RootCompilerAnnotation;
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
        Instant start = Instant.now();

        ImmutableCollection<CompilationUnit> compilationUnits = this.getCompilationUnits();

        KlassCompiler             klassCompiler     = new KlassCompiler(compilationUnits);
        CompilationResult         compilationResult = klassCompiler.compile();
        DomainModelWithSourceCode domainModel       = this.handleResult(compilationResult);

        Instant  end      = Instant.now();
        Duration duration = Duration.between(start, end);
        String durationPrettyString = duration.toString().substring(2)
                .replaceAll("(\\d[HMS])(?!$)", "$1 ")
                .toLowerCase();
        LOGGER.info("Domain model compilation completed in {}", durationPrettyString);

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
                .collectWithIndex((each, index) -> CompilationUnit.createFromClasspathLocation(index, each));

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
        ImmutableList<RootCompilerAnnotation> compilerAnnotations = compilationResult.getCompilerAnnotations();
        for (RootCompilerAnnotation compilerAnnotation : compilerAnnotations)
        {
            LOGGER.warn(compilerAnnotation.toString());
        }
        throw new RuntimeException("There were compiler errors.");
    }

    @Nonnull
    private DomainModelWithSourceCode handleSuccess(@Nonnull DomainModelCompilationResult compilationResult)
    {
        return compilationResult.getDomainModel();
    }
}
