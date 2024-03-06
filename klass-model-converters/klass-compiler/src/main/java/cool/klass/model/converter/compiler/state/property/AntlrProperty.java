package cool.klass.model.converter.compiler.state.property;

import java.util.regex.Pattern;

import javax.annotation.Nonnull;

import cool.klass.model.converter.compiler.CompilationUnit;
import cool.klass.model.converter.compiler.error.CompilerErrorState;
import cool.klass.model.converter.compiler.state.AntlrClass;
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
            boolean inferred,
            @Nonnull ParserRuleContext nameContext,
            @Nonnull String name,
            int ordinal)
    {
        super(elementContext, compilationUnit, inferred, nameContext, name, ordinal);
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

    public abstract void reportErrors(CompilerErrorState compilerErrorHolder);

    public final void reportDuplicateMemberName(@Nonnull CompilerErrorState compilerErrorHolder)
    {
        String message = String.format(
                "ERR_DUP_PRP: Duplicate member: '%s.%s'.",
                this.getOwningClassState().getName(),
                this.name);

        compilerErrorHolder.add(message, this);
    }

    @Nonnull
    protected abstract AntlrClass getOwningClassState();

    @Override
    protected Pattern getNamePattern()
    {
        return MEMBER_NAME_PATTERN;
    }
}
