package cool.klass.generator.reladomo.plugin;

import java.util.Set;
import java.util.regex.Pattern;

import cool.klass.model.converter.compiler.KlassCompiler;
import cool.klass.model.converter.compiler.error.CompilerError;
import cool.klass.model.converter.compiler.error.CompilerErrorHolder;
import cool.klass.model.meta.domain.DomainModel;
import cool.klass.model.meta.domain.DomainModel.DomainModelBuilder;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.eclipse.collections.api.list.MutableList;
import org.eclipse.collections.impl.factory.Lists;
import org.reflections.Reflections;
import org.reflections.scanners.ResourcesScanner;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;

public abstract class AbstractGenerateReladomoMojo extends AbstractMojo
{
    @Parameter(property = "rootPackageName", required = true, readonly = true)
    private String rootPackageName;

    protected DomainModel getDomainModel()
    {
        Reflections reflections = new Reflections(new ConfigurationBuilder().setScanners(new ResourcesScanner())
                .setUrls(ClasspathHelper.forPackage(this.rootPackageName)));
        Set<String> klassLocations =
                reflections.getResources(Pattern.compile(".*\\.klass"));
        DomainModelBuilder domainModelBuilder = new DomainModelBuilder();
        MutableList<CompilerError> compilerErrors = Lists.mutable.empty();
        CompilerErrorHolder compilerErrorHolder = new CompilerErrorHolder();
        KlassCompiler klassCompiler = new KlassCompiler(domainModelBuilder, compilerErrorHolder);
        klassCompiler.compile(klassLocations);
        DomainModel domainModel = domainModelBuilder.build();
        // TODO: Check if errors are empty
        return domainModel;
    }
}
