package cool.klass.model.converter.compiler.state.parameter;

import java.util.Optional;

import javax.annotation.Nonnull;

import cool.klass.model.converter.compiler.CompilationUnit;
import cool.klass.model.converter.compiler.state.IAntlrElement;
import cool.klass.model.converter.compiler.state.property.AntlrModifier;
import cool.klass.model.meta.domain.AbstractNamedElement.NamedElementBuilder;
import org.antlr.v4.runtime.ParserRuleContext;

public class AntlrParameterModifier extends AntlrModifier
{
    public AntlrParameterModifier(
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
    public Optional<IAntlrElement> getSurroundingElement()
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName()
                + ".getSurroundingContext() not implemented yet");
    }

    public boolean isUserId()
    {
        return this.name.equals("userId");
    }

    public boolean isId()
    {
        return this.name.equals("id");
    }

    public boolean isVersionNumber()
    {
        return this.name.equals("version");
    }

    @Override
    public NamedElementBuilder<?> build()
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName() + ".build() not implemented yet");
    }

    @Override
    public NamedElementBuilder<?> getElementBuilder()
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName()
                + ".getElementBuilder() not implemented yet");
    }
}
