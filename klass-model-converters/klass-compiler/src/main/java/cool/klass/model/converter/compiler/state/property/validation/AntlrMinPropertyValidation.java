package cool.klass.model.converter.compiler.state.property.validation;

import java.util.Objects;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import cool.klass.model.converter.compiler.CompilationUnit;
import cool.klass.model.converter.compiler.state.property.AntlrDataTypeProperty;
import cool.klass.model.meta.domain.property.validation.MinPropertyValidationImpl.MinPropertyValidationBuilder;
import org.antlr.v4.runtime.ParserRuleContext;

public class AntlrMinPropertyValidation extends AbstractAntlrNumericPropertyValidation
{
    private MinPropertyValidationBuilder elementBuilder;

    public AntlrMinPropertyValidation(
            @Nonnull ParserRuleContext elementContext,
            @Nullable CompilationUnit compilationUnit,
            boolean inferred,
            AntlrDataTypeProperty<?> owningPropertyState,
            int number)
    {
        super(elementContext, compilationUnit, inferred, owningPropertyState, number);
    }

    @Override
    public MinPropertyValidationBuilder build()
    {
        if (this.elementBuilder != null)
        {
            throw new IllegalStateException();
        }
        this.elementBuilder = new MinPropertyValidationBuilder(
                this.elementContext,
                this.inferred,
                this.owningPropertyState.getElementBuilder(),
                this.number);
        return this.elementBuilder;
    }

    @Nonnull
    @Override
    public MinPropertyValidationBuilder getElementBuilder()
    {
        return Objects.requireNonNull(this.elementBuilder);
    }
}
