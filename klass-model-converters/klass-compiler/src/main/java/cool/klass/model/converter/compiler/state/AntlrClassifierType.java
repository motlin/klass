package cool.klass.model.converter.compiler.state;

import java.util.Objects;
import java.util.Optional;

import javax.annotation.Nonnull;

import cool.klass.model.converter.compiler.CompilationUnit;
import cool.klass.model.converter.compiler.state.property.AntlrClassifierTypeOwner;
import cool.klass.model.meta.grammar.KlassParser.ClassifierTypeContext;

public class AntlrClassifierType
        extends AntlrElement
        implements AntlrMultiplicityOwner
{
    private final AntlrClassifierTypeOwner classifierTypeOwnerState;

    private AntlrClassifier   classifierState;
    private AntlrMultiplicity multiplicityState;

    public AntlrClassifierType(
            @Nonnull ClassifierTypeContext elementContext,
            @Nonnull Optional<CompilationUnit> compilationUnit,
            @Nonnull AntlrClassifierTypeOwner classifierTypeOwnerState)
    {
        super(elementContext, compilationUnit);
        this.classifierTypeOwnerState = Objects.requireNonNull(classifierTypeOwnerState);
    }

    @Override
    public boolean omitParentFromSurroundingElements()
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName()
                + ".omitParentFromSurroundingElements() not implemented yet");
    }

    @Nonnull
    @Override
    public Optional<IAntlrElement> getSurroundingElement()
    {
        return Optional.of(this.classifierTypeOwnerState);
    }

    public AntlrClassifier getType()
    {
        return this.classifierState;
    }

    public AntlrMultiplicity getMultiplicity()
    {
        return this.multiplicityState;
    }

    public void enterClassifierReference(AntlrClassifier classifierState)
    {
        if (this.classifierState != null)
        {
            throw new AssertionError();
        }

        this.classifierState = Objects.requireNonNull(classifierState);
    }

    @Override
    public void enterMultiplicity(@Nonnull AntlrMultiplicity multiplicityState)
    {
        if (this.multiplicityState != null)
        {
            throw new AssertionError();
        }
        this.multiplicityState = Objects.requireNonNull(multiplicityState);
    }
}
