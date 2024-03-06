package cool.klass.model.converter.compiler.state.property.validation;

import java.util.Optional;

import javax.annotation.Nonnull;

import cool.klass.model.converter.compiler.CompilationUnit;
import cool.klass.model.converter.compiler.state.property.AntlrDataTypeProperty;
import cool.klass.model.meta.domain.property.validation.MaxPropertyValidationImpl.MaxPropertyValidationBuilder;
import org.antlr.v4.runtime.ParserRuleContext;

public class AntlrMaxPropertyValidation extends AbstractAntlrNumericPropertyValidation
{
    private MaxPropertyValidationBuilder elementBuilder;

    public AntlrMaxPropertyValidation(
            @Nonnull ParserRuleContext elementContext,
            @Nonnull Optional<CompilationUnit> compilationUnit,
            @Nonnull AntlrDataTypeProperty<?> owningPropertyState,
            int number)
    {
        super(elementContext, compilationUnit, owningPropertyState, number);
    }

    @Override
    public MaxPropertyValidationBuilder build()
    {
        if (this.elementBuilder != null)
        {
            throw new IllegalStateException();
        }
        this.elementBuilder = new MaxPropertyValidationBuilder(
                this.elementContext,
                this.getMacroElementBuilder(),
                this.owningPropertyState.getElementBuilder(),
                this.number);
        return this.elementBuilder;
    }

    @Nonnull
    @Override
    public MaxPropertyValidationBuilder getElementBuilder()
    {
        return this.elementBuilder;
    }
}
