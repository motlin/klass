package cool.klass.model.converter.compiler.state.parameter;

import java.util.Objects;
import java.util.Optional;
import java.util.regex.Pattern;

import javax.annotation.Nonnull;

import cool.klass.model.converter.compiler.CompilationUnit;
import cool.klass.model.converter.compiler.error.CompilerErrorHolder;
import cool.klass.model.converter.compiler.state.AntlrBuildableElement;
import cool.klass.model.converter.compiler.state.AntlrMultiplicity;
import cool.klass.model.converter.compiler.state.AntlrNamedElement;
import cool.klass.model.converter.compiler.state.AntlrType;
import cool.klass.model.converter.compiler.state.IAntlrElement;
import cool.klass.model.meta.domain.parameter.AbstractParameter;
import cool.klass.model.meta.domain.parameter.AbstractParameter.AbstractParameterBuilder;
import org.antlr.v4.runtime.ParserRuleContext;
import org.eclipse.collections.api.list.MutableList;
import org.eclipse.collections.impl.factory.Lists;

public abstract class AntlrParameter<BuiltElement extends AbstractParameter>
        extends AntlrNamedElement
        implements AntlrBuildableElement
{
    @Nonnull
    protected final AntlrMultiplicity   multiplicityState;
    @Nonnull
    protected final AntlrParameterOwner parameterOwner;

    // TODO: Factor modifiers into type checking
    private final MutableList<AntlrParameterModifier> parameterModifiers = Lists.mutable.empty();

    protected AntlrParameter(
            @Nonnull ParserRuleContext elementContext,
            CompilationUnit compilationUnit,
            boolean inferred,
            @Nonnull ParserRuleContext nameContext,
            @Nonnull String name,
            int ordinal,
            @Nonnull AntlrMultiplicity multiplicityState,
            @Nonnull AntlrParameterOwner parameterOwner)
    {
        super(elementContext, compilationUnit, inferred, nameContext, name, ordinal);
        this.multiplicityState = Objects.requireNonNull(multiplicityState);
        this.parameterOwner = Objects.requireNonNull(parameterOwner);
    }

    @Override
    public boolean omitParentFromSurroundingElements()
    {
        return true;
    }

    @Nonnull
    @Override
    public Optional<IAntlrElement> getSurroundingElement()
    {
        return Optional.of((IAntlrElement) this.parameterOwner);
    }

    @Override
    protected Pattern getNamePattern()
    {
        return MEMBER_NAME_PATTERN;
    }

    public int getNumModifiers()
    {
        return this.parameterModifiers.size();
    }

    public void enterParameterModifier(AntlrParameterModifier parameterModifierState)
    {
        this.parameterModifiers.add(parameterModifierState);
    }

    public void reportDuplicateParameterName(@Nonnull CompilerErrorHolder compilerErrorHolder)
    {
        String message = String.format("ERR_DUP_PAR: Duplicate parameter: '%s'.", this.getName());
        compilerErrorHolder.add(message, this);
    }

    @Nonnull
    public abstract AntlrType getType();

    @Nonnull
    public abstract AbstractParameterBuilder<BuiltElement> build();

    @Nonnull
    public abstract AbstractParameterBuilder<BuiltElement> getElementBuilder();
}
