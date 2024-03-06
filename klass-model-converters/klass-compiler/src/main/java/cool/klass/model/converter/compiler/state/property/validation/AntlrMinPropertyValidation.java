package cool.klass.model.converter.compiler.state.property.validation;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import cool.klass.model.converter.compiler.CompilationUnit;
import cool.klass.model.converter.compiler.state.property.AntlrDataTypeProperty;
import cool.klass.model.meta.domain.property.validation.MinPropertyValidationImpl.MinPropertyValidationBuilder;
import org.antlr.v4.runtime.ParserRuleContext;

public class AntlrMinPropertyValidation extends AbstractAntlrNumericPropertyValidation
{
    public AntlrMinPropertyValidation(
            @Nonnull ParserRuleContext elementContext,
            @Nullable CompilationUnit compilationUnit,
            boolean inferred,
            AntlrDataTypeProperty<?> owningPropertyState,
            int number)
    {
        super(elementContext, compilationUnit, inferred, owningPropertyState, number);
    }

    public MinPropertyValidationBuilder build()
    {
        return new MinPropertyValidationBuilder(
                this.elementContext,
                this.inferred,
                this.owningPropertyState.getPropertyBuilder(),
                this.number);
    }
}
