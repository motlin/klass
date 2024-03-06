package cool.klass.model.converter.compiler.state.property;

import java.util.Optional;
import java.util.regex.Pattern;

import javax.annotation.Nonnull;

import cool.klass.model.converter.compiler.CompilationUnit;
import cool.klass.model.converter.compiler.error.CompilerErrorState;
import cool.klass.model.converter.compiler.state.AntlrNamedElement;
import cool.klass.model.meta.domain.property.AbstractModifier.ModifierBuilder;
import org.antlr.v4.runtime.ParserRuleContext;

public abstract class AntlrModifier extends AntlrNamedElement
{
    protected AntlrModifier(
            @Nonnull ParserRuleContext elementContext,
            @Nonnull Optional<CompilationUnit> compilationUnit,
            @Nonnull ParserRuleContext nameContext,
            @Nonnull String name,
            int ordinal)
    {
        super(elementContext, compilationUnit, nameContext, name, ordinal);
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

    public void reportAuditErrors(@Nonnull CompilerErrorState compilerErrorHolder)
    {
        if (!this.isAudit() && !this.isUser())
        {
            return;
        }

        ParserRuleContext offendingToken = this.getElementContext();
        String message = String.format(
                "Modifier '%s' requires one 'user' class in the domain model.",
                offendingToken.getText());
        compilerErrorHolder.add("ERR_ADT_MOD", message, this, offendingToken);
    }

    protected abstract boolean isAudit();

    protected abstract boolean isUser();

    @Override
    public boolean omitParentFromSurroundingElements()
    {
        return true;
    }

    @Nonnull
    public abstract ModifierBuilder<?> build();

    @Override
    @Nonnull
    public abstract ModifierBuilder<?> getElementBuilder();
}
