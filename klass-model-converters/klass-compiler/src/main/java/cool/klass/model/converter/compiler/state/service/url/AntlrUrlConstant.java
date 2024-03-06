package cool.klass.model.converter.compiler.state.service.url;

import java.util.Objects;
import java.util.Optional;
import java.util.regex.Pattern;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import cool.klass.model.converter.compiler.CompilationUnit;
import cool.klass.model.converter.compiler.error.CompilerErrorState;
import cool.klass.model.converter.compiler.state.AntlrElement;
import cool.klass.model.converter.compiler.state.AntlrNamedElement;
import cool.klass.model.converter.compiler.state.IAntlrElement;
import cool.klass.model.meta.domain.service.url.UrlConstantImpl.UrlConstantBuilder;
import org.antlr.v4.runtime.ParserRuleContext;

public class AntlrUrlConstant extends AntlrNamedElement
{
    private UrlConstantBuilder elementBuilder;

    public AntlrUrlConstant(
            @Nonnull ParserRuleContext elementContext,
            @Nullable CompilationUnit compilationUnit,
            Optional<AntlrElement> macroElement,
            @Nonnull ParserRuleContext nameContext,
            @Nonnull String name,
            int ordinal)
    {
        super(elementContext, compilationUnit, macroElement, nameContext, name, ordinal);
    }

    @Nonnull
    @Override
    public Optional<IAntlrElement> getSurroundingElement()
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName()
                + ".getSurroundingContext() not implemented yet");
    }

    @Override
    public boolean omitParentFromSurroundingElements()
    {
        return true;
    }

    @Nonnull
    public UrlConstantBuilder build()
    {
        if (this.elementBuilder != null)
        {
            throw new IllegalStateException();
        }
        this.elementBuilder = new UrlConstantBuilder(
                this.elementContext,
                this.macroElement.map(AntlrElement::getElementBuilder),
                this.nameContext,
                this.name,
                this.ordinal);
        return this.elementBuilder;
    }

    @Nonnull
    public UrlConstantBuilder getElementBuilder()
    {
        return Objects.requireNonNull(this.elementBuilder);
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
        // TODO: URLs can contain almost anything. The parser is probably already more strict than any error checking that needs to happen here.
        // https://stackoverflow.com/questions/7109143/what-characters-are-valid-in-a-url
    }
}
