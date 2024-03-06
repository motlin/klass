package cool.klass.compiler.plugin;

import cool.klass.generator.plugin.AbstractGenerateMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.ResolutionScope;

/**
 * This plugin doesn't emit anything. It just compiles the source code and emits compiler errors if applicable. The goal is to emit compiler errors in the module which contains the code being compiled, rather than a dependent module. This helps with rerunnability.
 */
@Mojo(name = "compile", defaultPhase = LifecyclePhase.TEST, threadSafe = true, requiresDependencyResolution = ResolutionScope.TEST)
public class KlassCompilerMojo extends AbstractGenerateMojo
{
    @Override
    public void execute() throws MojoExecutionException
    {
        this.getDomainModel();
    }
}
