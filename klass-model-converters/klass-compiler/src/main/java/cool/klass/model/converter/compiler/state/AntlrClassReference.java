package cool.klass.model.converter.compiler.state;

import java.util.Objects;
import java.util.Optional;

import javax.annotation.Nonnull;

import cool.klass.model.converter.compiler.CompilationUnit;
import cool.klass.model.converter.compiler.state.property.AntlrAssociationEnd;
import cool.klass.model.meta.grammar.KlassParser.ClassReferenceContext;

public class AntlrClassReference
        extends AntlrElement
{
    public static final AntlrClassReference AMBIGUOUS = new AntlrClassReference(
            new ClassReferenceContext(AMBIGUOUS_PARENT, -1),
            Optional.empty(),
            AntlrAssociationEnd.AMBIGUOUS,
            AntlrClass.AMBIGUOUS);

    public static final AntlrClassReference NOT_FOUND = new AntlrClassReference(
            new ClassReferenceContext(NOT_FOUND_PARENT, -1),
            Optional.empty(),
            AntlrAssociationEnd.NOT_FOUND,
            AntlrClass.NOT_FOUND);

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

    @Nonnull
    @Override
    public ClassReferenceContext getElementContext()
    {
        return (ClassReferenceContext) super.getElementContext();
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
