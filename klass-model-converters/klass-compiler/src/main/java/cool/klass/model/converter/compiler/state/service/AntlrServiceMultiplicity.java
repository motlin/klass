package cool.klass.model.converter.compiler.state.service;

import java.util.Objects;
import java.util.Optional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import cool.klass.model.converter.compiler.CompilationUnit;
import cool.klass.model.converter.compiler.state.AntlrElement;
import cool.klass.model.converter.compiler.state.IAntlrElement;
import cool.klass.model.meta.domain.api.service.ServiceMultiplicity;
import org.antlr.v4.runtime.ParserRuleContext;

public class AntlrServiceMultiplicity extends AntlrElement
{
    public static final AntlrServiceMultiplicity AMBIGUOUS = new AntlrServiceMultiplicity(
            new ParserRuleContext(),
            null,
            true,
            ServiceMultiplicity.ONE);

    @Nonnull
    private final ServiceMultiplicity serviceMultiplicity;

    public AntlrServiceMultiplicity(
            @Nonnull ParserRuleContext elementContext,
            @Nullable CompilationUnit compilationUnit,
            boolean inferred,
            @Nonnull ServiceMultiplicity serviceMultiplicity)
    {
        super(elementContext, compilationUnit, inferred);
        this.serviceMultiplicity = Objects.requireNonNull(serviceMultiplicity);
    }

    @Override
    public boolean omitParentFromSurroundingElements()
    {
        return false;
    }

    @Nonnull
    @Override
    public Optional<IAntlrElement> getSurroundingElement()
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName()
                + ".getSurroundingContext() not implemented yet");
    }

    @Nonnull
    public ServiceMultiplicity getServiceMultiplicity()
    {
        return this.serviceMultiplicity;
    }
}
