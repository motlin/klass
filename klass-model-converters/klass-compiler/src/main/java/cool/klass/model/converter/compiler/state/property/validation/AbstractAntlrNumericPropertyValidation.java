package cool.klass.model.converter.compiler.state.property.validation;

import java.util.Optional;

import javax.annotation.Nonnull;

import cool.klass.model.converter.compiler.CompilationUnit;
import cool.klass.model.converter.compiler.state.property.AntlrDataTypeProperty;
import cool.klass.model.meta.domain.property.validation.AbstractNumericPropertyValidation.NumericPropertyValidationBuilder;
import org.antlr.v4.runtime.ParserRuleContext;

public abstract class AbstractAntlrNumericPropertyValidation
        extends AbstractAntlrPropertyValidation
{
    protected final int number;

    protected AbstractAntlrNumericPropertyValidation(
            @Nonnull ParserRuleContext elementContext,
            @Nonnull Optional<CompilationUnit> compilationUnit,
            @Nonnull AntlrDataTypeProperty<?> owningProperty,
            int number)
    {
        super(elementContext, compilationUnit, owningProperty);
        this.number = number;
    }

    @Override
    public abstract NumericPropertyValidationBuilder build();

    @Nonnull
    @Override
    public abstract NumericPropertyValidationBuilder getElementBuilder();
}
