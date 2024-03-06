package cool.klass.model.converter.compiler.state.property.validation;

import java.util.Objects;
import java.util.Optional;

import javax.annotation.Nonnull;

import cool.klass.model.converter.compiler.CompilationUnit;
import cool.klass.model.converter.compiler.state.property.AntlrDataTypeProperty;
import cool.klass.model.meta.domain.property.validation.MinPropertyValidationImpl.MinPropertyValidationBuilder;
import cool.klass.model.meta.grammar.KlassParser.MinValidationContext;
import cool.klass.model.meta.grammar.KlassParser.MinValidationKeywordContext;
import org.antlr.v4.runtime.ParserRuleContext;

public class AntlrMinPropertyValidation extends AbstractAntlrNumericPropertyValidation
{
    private MinPropertyValidationBuilder elementBuilder;

    public AntlrMinPropertyValidation(
            @Nonnull ParserRuleContext elementContext,
            @Nonnull Optional<CompilationUnit> compilationUnit,
            @Nonnull AntlrDataTypeProperty<?> owningPropertyState,
            int number)
    {
        super(elementContext, compilationUnit, owningPropertyState, number);
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
                this.getMacroElementBuilder(),
                this.getSourceCodeBuilder(),
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

    @Nonnull
    @Override
    public MinValidationContext getElementContext()
    {
        return (MinValidationContext) super.getElementContext();
    }

    @Override
    public MinValidationKeywordContext getKeywordToken()
    {
        return this.getElementContext().minValidationKeyword();
    }
}
