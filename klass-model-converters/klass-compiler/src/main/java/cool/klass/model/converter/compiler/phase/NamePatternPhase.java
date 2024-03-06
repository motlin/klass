package cool.klass.model.converter.compiler.phase;

import java.util.regex.Pattern;

import cool.klass.model.converter.compiler.CompilationUnit;
import cool.klass.model.converter.compiler.error.CompilerErrorHolder;
import cool.klass.model.meta.grammar.KlassParser.CompilationUnitContext;
import org.eclipse.collections.api.map.MapIterable;

// TODO: Check name patterns
public class NamePatternPhase extends AbstractCompilerPhase
{
    protected static final Pattern PACKAGE_NAME_PATTERN  = Pattern.compile("^[a-z]+(\\.[a-z][a-z0-9]*)*$");
    private static final   Pattern TYPE_NAME_PATTERN     = Pattern.compile("^[A-Z][a-zA-Z0-9]*$");
    private static final   Pattern MEMBER_NAME_PATTERN   = Pattern.compile("^[a-z][a-zA-Z0-9]*$");
    private static final   Pattern CONSTANT_NAME_PATTERN = Pattern.compile("^[A-Z][A-Z0-9]*(_[A-Z0-9]+)*$");

    protected NamePatternPhase(
            CompilerErrorHolder compilerErrorHolder,
            MapIterable<CompilationUnitContext, CompilationUnit> compilationUnitsByContext)
    {
        super(compilerErrorHolder, compilationUnitsByContext);
    }
}
