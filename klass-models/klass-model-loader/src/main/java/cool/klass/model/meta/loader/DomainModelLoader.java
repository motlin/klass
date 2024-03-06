package cool.klass.model.meta.loader;

import java.net.URL;
import java.util.Set;
import java.util.regex.Pattern;

import javax.annotation.Nullable;

import cool.klass.model.converter.compiler.CompilationUnit;
import cool.klass.model.converter.compiler.CompilerState;
import cool.klass.model.converter.compiler.KlassCompiler;
import cool.klass.model.converter.compiler.error.CompilerError;
import cool.klass.model.meta.domain.api.DomainModel;
import org.eclipse.collections.api.list.ImmutableList;
import org.eclipse.collections.api.set.MutableSet;
import org.eclipse.collections.impl.factory.Lists;
import org.eclipse.collections.impl.set.mutable.SetAdapter;
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

        ImmutableList<URL> urls = klassSourcePackagesImmutable.flatCollect(ClasspathHelper::forPackage);
        ConfigurationBuilder configurationBuilder = new ConfigurationBuilder()
                .setScanners(new ResourcesScanner())
                .setUrls(urls.castToList());
        Reflections reflections    = new Reflections(configurationBuilder);
        Set<String> klassLocations = reflections.getResources(Pattern.compile(".*\\.klass"));

        MutableSet<CompilationUnit> compilationUnits = SetAdapter.adapt(klassLocations)
                .collect(CompilationUnit::createFromClasspathLocation);

        CompilerState compilerState = new CompilerState(compilationUnits);
        KlassCompiler klassCompiler = new KlassCompiler(compilerState);
        DomainModel   domainModel   = klassCompiler.compile();

        ImmutableList<CompilerError> compilerErrors = compilerState.getCompilerErrors();
        if (compilerErrors.notEmpty())
        {
            for (CompilerError compilerError : compilerErrors)
            {
                LOGGER.warn(compilerError.toString());
            }
            throw new RuntimeException("There were compiler errors.");
        }

        return domainModel;
    }
}
