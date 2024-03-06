package cool.klass.model.converter.compiler.state.parameter;

import java.util.Objects;

import javax.annotation.Nonnull;

import cool.klass.model.converter.compiler.CompilationUnit;
import cool.klass.model.converter.compiler.error.CompilerErrorHolder;
import cool.klass.model.converter.compiler.state.AntlrMultiplicity;
import cool.klass.model.converter.compiler.state.AntlrNamedElement;
import cool.klass.model.converter.compiler.state.AntlrType;
import cool.klass.model.meta.domain.parameter.AbstractParameter.ParameterBuilder;
import org.antlr.v4.runtime.ParserRuleContext;
import org.eclipse.collections.api.list.ImmutableList;

public abstract class AntlrParameter extends AntlrNamedElement
{
    @Nonnull
    protected final AntlrMultiplicity   multiplicityState;
    @Nonnull
    protected final AntlrParameterOwner parameterOwner;

    // TODO: Factor modifiers into type checking
    private final ImmutableList<AntlrParameterModifier> parameterModifiers;

    protected AntlrParameter(
            @Nonnull ParserRuleContext elementContext,
            CompilationUnit compilationUnit,
            boolean inferred,
            @Nonnull ParserRuleContext nameContext,
            @Nonnull String name,
            int ordinal,
            @Nonnull AntlrMultiplicity multiplicityState,
            @Nonnull AntlrParameterOwner parameterOwner,
            ImmutableList<AntlrParameterModifier> parameterModifiers)
    {
        super(elementContext, compilationUnit, inferred, nameContext, name, ordinal);
        this.multiplicityState = Objects.requireNonNull(multiplicityState);
        this.parameterOwner = Objects.requireNonNull(parameterOwner);
        this.parameterModifiers = Objects.requireNonNull(parameterModifiers);
    }

    @Override
    public void reportNameErrors(@Nonnull CompilerErrorHolder compilerErrorHolder)
    {
        this.reportKeywordCollision(compilerErrorHolder);

        // TODO: Split out parameter name pattern if necessary
        if (!MEMBER_NAME_PATTERN.matcher(this.name).matches())
        {
            String message = String.format(
                    "ERR_PAR_NME: Name must match pattern %s but was %s",
                    CONSTANT_NAME_PATTERN,
                    this.name);
            compilerErrorHolder.add(
                    message,
                    this.nameContext);
        }
    }

    public void reportDuplicateParameterName(@Nonnull CompilerErrorHolder compilerErrorHolder)
    {
        String message = String.format("ERR_DUP_PAR: Duplicate parameter: '%s'.", this.name);

        compilerErrorHolder.add(
                message,
                this.nameContext,
                this.parameterOwner.getParserRuleContexts().toArray(new ParserRuleContext[]{}));
    }

    @Nonnull
    public abstract AntlrType getType();

    @Nonnull
    public abstract ParameterBuilder build();
}
