package cool.klass.model.converter.compiler.state.property;

import java.util.regex.Pattern;

import javax.annotation.Nonnull;

import cool.klass.model.converter.compiler.CompilationUnit;
import cool.klass.model.converter.compiler.error.CompilerErrorHolder;
import cool.klass.model.converter.compiler.state.AntlrNamedElement;
import org.antlr.v4.runtime.ParserRuleContext;

public abstract class AntlrModifier extends AntlrNamedElement
{
    protected AntlrModifier(
            @Nonnull ParserRuleContext elementContext,
            CompilationUnit compilationUnit,
            boolean inferred,
            @Nonnull ParserRuleContext nameContext,
            @Nonnull String name,
            int ordinal)
    {
        super(elementContext, compilationUnit, inferred, nameContext, name, ordinal);
    }

    @Nonnull
    @Override
    protected Pattern getNamePattern()
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName()
                + ".getNamePattern() not implemented yet");
    }

    @Override
    public void reportNameErrors(@Nonnull CompilerErrorHolder compilerErrorHolder)
    {
        // intentionally blank
    }

    @Override
    public boolean omitParentFromSurroundingElements()
    {
        return true;
    }
}
