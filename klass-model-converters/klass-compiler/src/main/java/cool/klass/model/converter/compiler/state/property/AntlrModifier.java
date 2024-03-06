package cool.klass.model.converter.compiler.state.property;

import java.util.Optional;
import java.util.regex.Pattern;

import javax.annotation.Nonnull;

import cool.klass.model.converter.compiler.CompilationUnit;
import cool.klass.model.converter.compiler.error.CompilerErrorState;
import cool.klass.model.converter.compiler.state.AntlrElement;
import cool.klass.model.converter.compiler.state.AntlrNamedElement;
import cool.klass.model.meta.domain.AbstractNamedElement.NamedElementBuilder;
import org.antlr.v4.runtime.ParserRuleContext;

public abstract class AntlrModifier extends AntlrNamedElement
{
    protected AntlrModifier(
            @Nonnull ParserRuleContext elementContext,
            CompilationUnit compilationUnit,
            Optional<AntlrElement> macroElement,
            @Nonnull ParserRuleContext nameContext,
            @Nonnull String name,
            int ordinal)
    {
        super(elementContext, compilationUnit, macroElement, nameContext, name, ordinal);
    }

    @Nonnull
    @Override
    protected Pattern getNamePattern()
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName()
                + ".getNamePattern() not implemented yet");
    }

    @Override
    public void reportNameErrors(@Nonnull CompilerErrorState compilerErrorHolder)
    {
        // intentionally blank
    }

    @Override
    public boolean omitParentFromSurroundingElements()
    {
        return true;
    }

    public abstract NamedElementBuilder<?> build();

    public abstract NamedElementBuilder<?> getElementBuilder();
}
