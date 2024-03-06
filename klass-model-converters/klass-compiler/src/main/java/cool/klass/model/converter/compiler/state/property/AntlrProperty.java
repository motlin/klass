package cool.klass.model.converter.compiler.state.property;

import java.util.Optional;
import java.util.regex.Pattern;

import javax.annotation.Nonnull;
import javax.annotation.OverridingMethodsMustInvokeSuper;

import cool.klass.model.converter.compiler.CompilationUnit;
import cool.klass.model.converter.compiler.error.CompilerErrorState;
import cool.klass.model.converter.compiler.state.AntlrClassifier;
import cool.klass.model.converter.compiler.state.AntlrElement;
import cool.klass.model.converter.compiler.state.AntlrNamedElement;
import cool.klass.model.converter.compiler.state.AntlrType;
import cool.klass.model.meta.domain.api.Type;
import cool.klass.model.meta.domain.property.AbstractProperty.PropertyBuilder;
import org.antlr.v4.runtime.ParserRuleContext;

public abstract class AntlrProperty<T extends Type> extends AntlrNamedElement
{
    protected AntlrProperty(
            @Nonnull ParserRuleContext elementContext,
            CompilationUnit compilationUnit,
            Optional<AntlrElement> macroElement,
            @Nonnull ParserRuleContext nameContext,
            @Nonnull String name,
            int ordinal)
    {
        super(elementContext, compilationUnit, macroElement, nameContext, name, ordinal);
    }

    @Override
    public boolean omitParentFromSurroundingElements()
    {
        return false;
    }

    @Nonnull
    public abstract AntlrType getType();

    @Nonnull
    public abstract PropertyBuilder<T, ?, ?> build();

    @Nonnull
    public abstract PropertyBuilder<T, ?, ?> getElementBuilder();

    @OverridingMethodsMustInvokeSuper
    public abstract void reportErrors(CompilerErrorState compilerErrorHolder);

    public final void reportDuplicateMemberName(@Nonnull CompilerErrorState compilerErrorHolder)
    {
        String message = String.format(
                "Duplicate member: '%s.%s'.",
                this.getOwningClassifierState().getName(),
                this.name);

        compilerErrorHolder.add("ERR_DUP_PRP", message, this);
    }

    @Nonnull
    protected abstract AntlrClassifier getOwningClassifierState();

    @Override
    protected Pattern getNamePattern()
    {
        return MEMBER_NAME_PATTERN;
    }
}
