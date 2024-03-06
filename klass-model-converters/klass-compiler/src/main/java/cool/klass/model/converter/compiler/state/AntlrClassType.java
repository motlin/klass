package cool.klass.model.converter.compiler.state;

import java.util.Objects;
import java.util.Optional;

import javax.annotation.Nonnull;

import cool.klass.model.converter.compiler.CompilationUnit;
import cool.klass.model.converter.compiler.state.property.AntlrClassTypeOwner;
import cool.klass.model.meta.grammar.KlassParser.ClassTypeContext;

public class AntlrClassType
        extends AntlrElement
        implements AntlrMultiplicityOwner
{
    private final AntlrClassTypeOwner classTypeOwnerState;

    private AntlrClass        classState;
    private AntlrMultiplicity multiplicityState;

    public AntlrClassType(
            @Nonnull ClassTypeContext elementContext,
            @Nonnull Optional<CompilationUnit> compilationUnit,
            @Nonnull AntlrClassTypeOwner classTypeOwnerState)
    {
        super(elementContext, compilationUnit);
        this.classTypeOwnerState = Objects.requireNonNull(classTypeOwnerState);
    }

    @Override
    public boolean omitParentFromSurroundingElements()
    {
        return true;
    }

    @Nonnull
    @Override
    public Optional<IAntlrElement> getSurroundingElement()
    {
        return Optional.of(this.classTypeOwnerState);
    }

    public AntlrClass getType()
    {
        return this.classState;
    }

    public AntlrMultiplicity getMultiplicity()
    {
        return this.multiplicityState;
    }

    public void enterClassReference(AntlrClass classState)
    {
        if (this.classState != null)
        {
            throw new AssertionError();
        }

        this.classState = Objects.requireNonNull(classState);
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
