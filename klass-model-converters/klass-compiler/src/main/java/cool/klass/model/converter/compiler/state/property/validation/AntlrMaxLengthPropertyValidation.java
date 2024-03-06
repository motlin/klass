package cool.klass.model.converter.compiler.state.property.validation;

import java.util.Objects;
import java.util.Optional;

import javax.annotation.Nonnull;

import cool.klass.model.converter.compiler.CompilationUnit;
import cool.klass.model.converter.compiler.state.property.AntlrDataTypeProperty;
import cool.klass.model.meta.domain.property.validation.MaxLengthPropertyValidationImpl.MaxLengthPropertyValidationBuilder;
import cool.klass.model.meta.grammar.KlassParser.MaxLengthValidationContext;
import cool.klass.model.meta.grammar.KlassParser.MaxLengthValidationKeywordContext;

public class AntlrMaxLengthPropertyValidation
        extends AbstractAntlrNumericPropertyValidation
{
    private MaxLengthPropertyValidationBuilder elementBuilder;

    public AntlrMaxLengthPropertyValidation(
            @Nonnull MaxLengthValidationContext elementContext,
            @Nonnull Optional<CompilationUnit> compilationUnit,
            @Nonnull AntlrDataTypeProperty<?> owningPropertyState,
            int number)
    {
        super(elementContext, compilationUnit, owningPropertyState, number);
    }

    @Override
    public MaxLengthPropertyValidationBuilder build()
    {
        if (this.elementBuilder != null)
        {
            throw new IllegalStateException();
        }
        this.elementBuilder = new MaxLengthPropertyValidationBuilder(
                (MaxLengthValidationContext) this.elementContext,
                this.getMacroElementBuilder(),
                this.getSourceCodeBuilder(),
                this.owningPropertyState.getElementBuilder(),
                this.number);
        return this.elementBuilder;
    }

    @Nonnull
    @Override
    public MaxLengthPropertyValidationBuilder getElementBuilder()
    {
        return Objects.requireNonNull(this.elementBuilder);
    }

    @Nonnull
    @Override
    public MaxLengthValidationContext getElementContext()
    {
        return (MaxLengthValidationContext) super.getElementContext();
    }

    @Override
    public MaxLengthValidationKeywordContext getKeywordToken()
    {
        return this.getElementContext().maxLengthValidationKeyword();
    }
}
