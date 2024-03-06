package cool.klass.model.converter.compiler.state.value;

import java.util.Objects;
import java.util.Optional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import cool.klass.model.converter.compiler.CompilationUnit;
import cool.klass.model.converter.compiler.annotation.CompilerAnnotationState;
import cool.klass.model.converter.compiler.state.AntlrEnumeration;
import cool.klass.model.converter.compiler.state.AntlrType;
import cool.klass.model.converter.compiler.state.IAntlrElement;
import cool.klass.model.converter.compiler.state.parameter.AntlrParameter;
import cool.klass.model.meta.domain.value.VariableReferenceImpl.VariableReferenceBuilder;
import cool.klass.model.meta.grammar.KlassParser.VariableReferenceContext;
import org.eclipse.collections.api.list.ImmutableList;
import org.eclipse.collections.api.map.OrderedMap;
import org.eclipse.collections.impl.factory.Lists;

public class AntlrVariableReference
        extends AntlrExpressionValue
{
    @Nonnull
    private final String variableName;

    @Nullable
    private AntlrParameter           antlrParameter;
    private VariableReferenceBuilder elementBuilder;

    public AntlrVariableReference(
            @Nonnull VariableReferenceContext elementContext,
            @Nonnull Optional<CompilationUnit> compilationUnit,
            @Nonnull String variableName,
            @Nonnull IAntlrElement expressionValueOwner)
    {
        super(elementContext, compilationUnit, expressionValueOwner);
        this.variableName = Objects.requireNonNull(variableName);
    }

    @Nonnull
    @Override
    public VariableReferenceBuilder build()
    {
        if (this.elementBuilder != null)
        {
            throw new IllegalStateException();
        }
        this.elementBuilder = new VariableReferenceBuilder(
                (VariableReferenceContext) this.elementContext,
                this.getMacroElementBuilder(),
                this.getSourceCodeBuilder(),
                this.antlrParameter.getElementBuilder());
        return this.elementBuilder;
    }

    @Nonnull
    @Override
    public VariableReferenceBuilder getElementBuilder()
    {
        return Objects.requireNonNull(this.elementBuilder);
    }

    @Override
    public void reportErrors(@Nonnull CompilerAnnotationState compilerAnnotationHolder)
    {
        if (this.antlrParameter == AntlrParameter.AMBIGUOUS)
        {
            return;
        }

        if (this.antlrParameter == AntlrParameter.NOT_FOUND)
        {
            String message = String.format("Cannot find parameter '%s'.", this.elementContext.getText());
            compilerAnnotationHolder.add("ERR_VAR_REF", message, this);
        }
    }

    @Nonnull
    @Override
    public ImmutableList<AntlrType> getPossibleTypes()
    {
        Objects.requireNonNull(this.antlrParameter);
        AntlrType type = this.antlrParameter.getType();
        return type == AntlrEnumeration.NOT_FOUND || type == AntlrEnumeration.AMBIGUOUS
                ? Lists.immutable.empty()
                : type.getPotentialWiderTypes();
    }

    @Override
    public void resolveServiceVariables(@Nonnull OrderedMap<String, AntlrParameter> formalParametersByName)
    {
        this.antlrParameter = formalParametersByName.getIfAbsentValue(
                this.variableName,
                AntlrParameter.NOT_FOUND);
    }

    @Override
    public void visit(AntlrExpressionValueVisitor visitor)
    {
        visitor.visitVariableReference(this);
    }
}
