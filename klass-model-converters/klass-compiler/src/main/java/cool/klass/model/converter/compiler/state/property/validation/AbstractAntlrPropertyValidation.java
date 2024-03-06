package cool.klass.model.converter.compiler.state.property.validation;

import java.util.Objects;
import java.util.Optional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import cool.klass.model.converter.compiler.CompilationUnit;
import cool.klass.model.converter.compiler.error.CompilerErrorState;
import cool.klass.model.converter.compiler.state.AntlrElement;
import cool.klass.model.converter.compiler.state.IAntlrElement;
import cool.klass.model.converter.compiler.state.property.AntlrDataTypeProperty;
import cool.klass.model.meta.domain.api.PrimitiveType;
import org.antlr.v4.runtime.ParserRuleContext;

public abstract class AbstractAntlrPropertyValidation extends AntlrElement
{
    protected final AntlrDataTypeProperty<?> owningPropertyState;

    protected AbstractAntlrPropertyValidation(
            @Nonnull ParserRuleContext elementContext,
            @Nullable CompilationUnit compilationUnit,
            boolean inferred,
            AntlrDataTypeProperty<?> owningPropertyState)
    {
        super(elementContext, compilationUnit, inferred);
        this.owningPropertyState = Objects.requireNonNull(owningPropertyState);
    }

    @Override
    public boolean omitParentFromSurroundingElements()
    {
        return true;
    }

    @Override
    public Optional<IAntlrElement> getSurroundingElement()
    {
        return Optional.of(this.owningPropertyState);
    }

    public void reportInvalidType(
            CompilerErrorState compilerErrorHolder,
            PrimitiveType primitiveType)
    {
        ParserRuleContext offendingToken = this.getElementContext();
        String message = String.format(
                "ERR_VLD_TYP: Invalid validation '%s' for type %s.",
                offendingToken.getText(),
                primitiveType.getPrettyName());
        compilerErrorHolder.add(message, this, offendingToken);
    }
}
