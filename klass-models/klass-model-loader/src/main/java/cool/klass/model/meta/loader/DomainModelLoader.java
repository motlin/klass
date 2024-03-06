package cool.klass.model.meta.loader;

import java.net.URL;
import java.util.Set;
import java.util.regex.Pattern;

import javax.annotation.Nullable;

import cool.klass.model.converter.compiler.KlassCompiler;
import cool.klass.model.converter.compiler.error.CompilerError;
import cool.klass.model.converter.compiler.error.CompilerErrorHolder;
import cool.klass.model.meta.domain.DomainModel;
import org.eclipse.collections.api.list.ImmutableList;
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
        ImmutableList<URL> urls = this.klassSourcePackages.flatCollect(ClasspathHelper::forPackage);
        ConfigurationBuilder configurationBuilder = new ConfigurationBuilder()
                .setScanners(new ResourcesScanner())
                .setUrls(urls.castToList());
        Reflections         reflections         = new Reflections(configurationBuilder);
        Set<String>         klassLocations      = reflections.getResources(Pattern.compile(".*\\.klass"));
        CompilerErrorHolder compilerErrorHolder = new CompilerErrorHolder();
        KlassCompiler       klassCompiler       = new KlassCompiler(compilerErrorHolder);
        DomainModel         domainModel         = klassCompiler.compile(klassLocations);

        if (compilerErrorHolder.hasCompilerErrors())
        {
            ImmutableList<CompilerError> compilerErrors = compilerErrorHolder.getCompilerErrors();
            for (CompilerError compilerError : compilerErrors)
            {
                LOGGER.warn(compilerError.toString());
            }
            throw new RuntimeException("There were compiler errors.");
        }

        return domainModel;
    }
}
