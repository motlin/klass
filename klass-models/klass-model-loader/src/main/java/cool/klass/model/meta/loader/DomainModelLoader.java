package cool.klass.model.meta.loader;

import java.net.URL;
import java.util.Set;
import java.util.regex.Pattern;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import cool.klass.model.converter.compiler.CompilationResult;
import cool.klass.model.converter.compiler.CompilationUnit;
import cool.klass.model.converter.compiler.CompilerState;
import cool.klass.model.converter.compiler.DomainModelCompilationResult;
import cool.klass.model.converter.compiler.ErrorsCompilationResult;
import cool.klass.model.converter.compiler.KlassCompiler;
import cool.klass.model.converter.compiler.error.RootCompilerError;
import cool.klass.model.meta.domain.api.DomainModel;
import org.eclipse.collections.api.list.ImmutableList;
import org.eclipse.collections.api.list.MutableList;
import org.eclipse.collections.impl.factory.Lists;
import org.reflections.Reflections;
import org.reflections.scanners.ResourcesScanner;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DomainModelLoader
{
    private static final Logger LOGGER = LoggerFactory.getLogger(DomainModelLoader.class);

    private final ImmutableList<String> klassSourcePackages;

    public DomainModelLoader(ImmutableList<String> klassSourcePackages)
    {
        this.klassSourcePackages = klassSourcePackages;
    }

    @Nullable
    public DomainModel load()
    {
        ImmutableList<String> klassSourcePackagesImmutable = Lists.immutable.withAll(this.klassSourcePackages);

        MutableList<CompilationUnit> compilationUnits = this.getCompilationUnits(klassSourcePackagesImmutable);

        CompilerState     compilerState     = new CompilerState(compilationUnits);
        KlassCompiler     klassCompiler     = new KlassCompiler(compilerState);
        CompilationResult compilationResult = klassCompiler.compile();
        return this.handleResult(compilationResult);
    }

    @Nonnull
    private MutableList<CompilationUnit> getCompilationUnits(ImmutableList<String> klassSourcePackages)
    {
        ImmutableList<URL> urls = klassSourcePackages.flatCollect(ClasspathHelper::forPackage);
        ConfigurationBuilder configurationBuilder = new ConfigurationBuilder()
                .setScanners(new ResourcesScanner())
                .setUrls(urls.castToList());
        Reflections reflections    = new Reflections(configurationBuilder);
        Set<String> klassLocations = reflections.getResources(Pattern.compile(".*\\.klass"));

        MutableList<CompilationUnit> compilationUnits = Lists.mutable.withAll(klassLocations)
                .collect(CompilationUnit::createFromClasspathLocation);

        if (compilationUnits.isEmpty())
        {
            String message = "Could not find any files matching *.klass in urls: " + urls;
            throw new RuntimeException(message);
        }
        return compilationUnits;
    }

    private DomainModel handleResult(CompilationResult compilationResult)
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

    private DomainModel handleFailure(ErrorsCompilationResult compilationResult)
    {
        ImmutableList<RootCompilerError> compilerErrors = compilationResult.getCompilerErrors();
        for (RootCompilerError compilerError : compilerErrors)
        {
            LOGGER.warn(compilerError.toString());
        }
        throw new RuntimeException("There were compiler errors.");
    }

    private DomainModel handleSuccess(DomainModelCompilationResult compilationResult)
    {
        return compilationResult.getDomainModel();
    }
}
