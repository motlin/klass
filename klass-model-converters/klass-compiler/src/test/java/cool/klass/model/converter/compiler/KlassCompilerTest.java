package cool.klass.model.converter.compiler;

import java.util.Set;
import java.util.regex.Pattern;

import cool.klass.model.converter.compiler.error.CompilerErrorHolder;
import cool.klass.model.meta.domain.DomainModel;
import cool.klass.model.meta.domain.DomainModel.DomainModelBuilder;
import org.junit.Test;
import org.reflections.Reflections;
import org.reflections.scanners.ResourcesScanner;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;

public class KlassCompilerTest
{
    @Test
    public void compile()
    {
        DomainModelBuilder domainModelBuilder = new DomainModelBuilder();
        // TODO: Just create the error holder inside the constructor?
        KlassCompiler compiler = new KlassCompiler(domainModelBuilder, new CompilerErrorHolder());

        Set<String> klassLocations = this.getResourceNames("com.test");

        compiler.compile(klassLocations);
        DomainModel domainModel = domainModelBuilder.build();
        // TODO assertions
    }

    protected Set<String> getResourceNames(String packageName)
    {
        Reflections reflections = new Reflections(new ConfigurationBuilder()
                .setUrls(ClasspathHelper.forPackage(packageName.replaceAll("\\.", "/")))
                .setScanners(new ResourcesScanner()).filterInputsBy(path -> path.startsWith(packageName)));

        return reflections.getResources(Pattern.compile(".*\\.klass"));
    }

    @Test
    public void errors()
    {
        DomainModelBuilder domainModelBuilder = new DomainModelBuilder();
        KlassCompiler compiler = new KlassCompiler(domainModelBuilder, new CompilerErrorHolder());

        Set<String> klassLocations = this.getResourceNames("errors");

        compiler.compile(klassLocations);
        DomainModel domainModel = domainModelBuilder.build();
        // TODO assertions
    }
}
