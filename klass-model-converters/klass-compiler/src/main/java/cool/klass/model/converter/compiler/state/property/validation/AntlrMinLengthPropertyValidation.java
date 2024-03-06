package cool.klass.model.converter.compiler.state.property.validation;

import java.util.Objects;
import java.util.Optional;

import javax.annotation.Nonnull;

import cool.klass.model.converter.compiler.CompilationUnit;
import cool.klass.model.converter.compiler.state.property.AntlrDataTypeProperty;
import cool.klass.model.meta.domain.property.validation.MinLengthPropertyValidationImpl.MinLengthPropertyValidationBuilder;
import cool.klass.model.meta.grammar.KlassParser.MinLengthValidationContext;
import cool.klass.model.meta.grammar.KlassParser.MinLengthValidationKeywordContext;

public class AntlrMinLengthPropertyValidation
        extends AbstractAntlrNumericPropertyValidation
{
    private MinLengthPropertyValidationBuilder elementBuilder;

    public AntlrMinLengthPropertyValidation(
            @Nonnull MinLengthValidationContext elementContext,
            @Nonnull Optional<CompilationUnit> compilationUnit,
            @Nonnull AntlrDataTypeProperty<?> owningProperty,
            int number)
    {
        super(elementContext, compilationUnit, owningProperty, number);
    }

    @Override
    public MinLengthPropertyValidationBuilder build()
    {
        if (this.elementBuilder != null)
        {
            throw new IllegalStateException();
        }
        this.elementBuilder = new MinLengthPropertyValidationBuilder(
                (MinLengthValidationContext) this.elementContext,
                this.getMacroElementBuilder(),
                this.getSourceCodeBuilder(),
                this.owningProperty.getElementBuilder(),
                this.number);
        return this.elementBuilder;
    }

    @Nonnull
    @Override
    public MinLengthPropertyValidationBuilder getElementBuilder()
    {
        return Objects.requireNonNull(this.elementBuilder);
    }

    @Nonnull
    @Override
    public MinLengthValidationContext getElementContext()
    {
        return (MinLengthValidationContext) super.getElementContext();
    }

    @Override
    public MinLengthValidationKeywordContext getKeywordToken()
    {
        return this.getElementContext().minLengthValidationKeyword();
    }
}
