package cool.klass.model.converter.compiler.state;

import java.util.Objects;
import java.util.Optional;

import javax.annotation.Nonnull;

import cool.klass.model.converter.compiler.CompilationUnit;
import cool.klass.model.meta.grammar.KlassParser.ClassReferenceContext;

public class AntlrClassReference
        extends AntlrElement
{
    @Nonnull
    private final AntlrClassReferenceOwner classReferenceOwnerState;
    @Nonnull
    private final AntlrClass               classState;

    public AntlrClassReference(
            @Nonnull ClassReferenceContext classReferenceContext,
            @Nonnull Optional<CompilationUnit> compilationUnit,
            @Nonnull AntlrClassReferenceOwner classReferenceOwnerState,
            @Nonnull AntlrClass classState)
    {
        super(classReferenceContext, compilationUnit);
        this.classReferenceOwnerState = Objects.requireNonNull(classReferenceOwnerState);
        this.classState               = Objects.requireNonNull(classState);
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
        return Optional.of(this.classReferenceOwnerState);
    }

    @Nonnull
    public AntlrClass getClassState()
    {
        return this.classState;
    }
}
