package cool.klass.model.converter.compiler;

import java.util.Set;
import java.util.regex.Pattern;

import cool.klass.model.converter.compiler.error.CompilerError;
import cool.klass.model.converter.compiler.error.CompilerErrorHolder;
import cool.klass.model.meta.domain.DomainModel;
import cool.klass.model.meta.domain.DomainModel.DomainModelBuilder;
import org.junit.Test;
import org.reflections.Reflections;
import org.reflections.scanners.ResourcesScanner;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class KlassCompilerTest
{
    private static final Logger LOGGER = LoggerFactory.getLogger(KlassCompilerTest.class);

    @Test
    public void compile()
    {
        DomainModelBuilder domainModelBuilder = new DomainModelBuilder();
        // TODO: Just create the error holder inside the constructor?
        CompilerErrorHolder compilerErrorHolder = new CompilerErrorHolder();
        KlassCompiler       compiler            = new KlassCompiler(domainModelBuilder, compilerErrorHolder);

        Set<String> klassLocations = this.getResourceNames("com.test");

        compiler.compile(klassLocations);

        assertFalse(compilerErrorHolder.hasCompilerErrors());
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
        DomainModelBuilder  domainModelBuilder  = new DomainModelBuilder();
        CompilerErrorHolder compilerErrorHolder = new CompilerErrorHolder();
        KlassCompiler       compiler            = new KlassCompiler(domainModelBuilder, compilerErrorHolder);

        Set<String> klassLocations = this.getResourceNames("errors");
        compiler.compile(klassLocations);
        assertTrue(compilerErrorHolder.hasCompilerErrors());
        for (CompilerError compilerError : compilerErrorHolder.getCompilerErrors())
        {
            LOGGER.warn("{}", compilerError);
        }
        // TODO assertions
    }
}
