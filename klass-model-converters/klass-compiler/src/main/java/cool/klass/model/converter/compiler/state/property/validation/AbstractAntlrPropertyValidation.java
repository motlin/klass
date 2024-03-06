package cool.klass.model.converter.compiler.state.property.validation;

import java.util.Objects;
import java.util.Optional;

import javax.annotation.Nonnull;

import cool.klass.model.converter.compiler.CompilationUnit;
import cool.klass.model.converter.compiler.annotation.CompilerAnnotationHolder;
import cool.klass.model.converter.compiler.state.AntlrElement;
import cool.klass.model.converter.compiler.state.IAntlrElement;
import cool.klass.model.converter.compiler.state.property.AntlrDataTypeProperty;
import cool.klass.model.meta.domain.api.PrimitiveType;
import cool.klass.model.meta.domain.property.validation.AbstractPropertyValidation.PropertyValidationBuilder;
import org.antlr.v4.runtime.ParserRuleContext;

// TODO: Property validations should have ordinal
public abstract class AbstractAntlrPropertyValidation
        extends AntlrElement
{
    @Nonnull
    protected final AntlrDataTypeProperty<?> owningProperty;

    protected AbstractAntlrPropertyValidation(
            @Nonnull ParserRuleContext elementContext,
            @Nonnull Optional<CompilationUnit> compilationUnit,
            @Nonnull AntlrDataTypeProperty<?> owningProperty)
    {
        super(elementContext, compilationUnit);
        this.owningProperty = Objects.requireNonNull(owningProperty);
    }

    @Nonnull
    @Override
    public Optional<IAntlrElement> getSurroundingElement()
    {
        return Optional.of(this.owningProperty);
    }

    public abstract PropertyValidationBuilder<?> build();

    @Override
    @Nonnull
    public abstract PropertyValidationBuilder<?> getElementBuilder();

    public void reportInvalidType(
            @Nonnull CompilerAnnotationHolder compilerAnnotationHolder,
            @Nonnull PrimitiveType primitiveType)
    {
        ParserRuleContext offendingToken = this.getKeywordToken();
        String message = String.format(
                "Invalid validation '%s' for type %s.",
                offendingToken.getText(),
                primitiveType.getPrettyName());
        compilerAnnotationHolder.add("ERR_VLD_TYP", message, this, offendingToken);
    }

    public abstract ParserRuleContext getKeywordToken();
}
