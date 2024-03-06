package cool.klass.compiler.plugin;

import cool.klass.generator.plugin.AbstractGenerateMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;

/**
 * This plugin doesn't emit anything. It just compiles the source code and emits compiler errors if applicable, in order to get compiler errors in the correct module. This helps with rerunnability.
 */
@Mojo(name = "compile", defaultPhase = LifecyclePhase.TEST, threadSafe = true)
public class KlassCompilerMojo extends AbstractGenerateMojo
{
    @Override
    public void execute() throws MojoExecutionException
    {
        this.getDomainModel();
    }
}
