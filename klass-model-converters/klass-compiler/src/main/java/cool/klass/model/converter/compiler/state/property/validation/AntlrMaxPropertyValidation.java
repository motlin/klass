package cool.klass.model.converter.compiler.state.property.validation;

import java.util.Optional;

import javax.annotation.Nonnull;

import cool.klass.model.converter.compiler.CompilationUnit;
import cool.klass.model.converter.compiler.state.property.AntlrDataTypeProperty;
import cool.klass.model.meta.domain.property.validation.MaxPropertyValidationImpl.MaxPropertyValidationBuilder;
import cool.klass.model.meta.grammar.KlassParser.MaxValidationContext;
import cool.klass.model.meta.grammar.KlassParser.MaxValidationKeywordContext;

public class AntlrMaxPropertyValidation extends AbstractAntlrNumericPropertyValidation
{
    private MaxPropertyValidationBuilder elementBuilder;

    public AntlrMaxPropertyValidation(
            @Nonnull MaxValidationContext elementContext,
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
                (MaxValidationContext) this.elementContext,
                this.getMacroElementBuilder(),
                this.getSourceCodeBuilder(),
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

    @Nonnull
    @Override
    public MaxValidationContext getElementContext()
    {
        return (MaxValidationContext) super.getElementContext();
    }

    @Override
    public MaxValidationKeywordContext getKeywordToken()
    {
        return this.getElementContext().maxValidationKeyword();
    }
}
