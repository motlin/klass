package cool.klass.model.converter.compiler.state;

import java.util.Objects;
import java.util.Optional;

import javax.annotation.Nonnull;

import cool.klass.model.converter.compiler.CompilationUnit;
import cool.klass.model.meta.grammar.KlassParser.ClassifierReferenceContext;

public class AntlrClassifierReference
        extends AntlrElement
{
    @Nonnull
    private final AntlrClassifierReferenceOwner classifierReferenceOwnerState;
    @Nonnull
    private final AntlrClassifier               classifierState;

    public AntlrClassifierReference(
            @Nonnull ClassifierReferenceContext classifierReferenceContext,
            @Nonnull Optional<CompilationUnit> compilationUnit,
            @Nonnull AntlrClassifierReferenceOwner classifierReferenceOwnerState,
            @Nonnull AntlrClassifier classifierState)
    {
        super(classifierReferenceContext, compilationUnit);
        this.classifierReferenceOwnerState = Objects.requireNonNull(classifierReferenceOwnerState);
        this.classifierState               = Objects.requireNonNull(classifierState);
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
        return Optional.of(this.classifierReferenceOwnerState);
    }

    @Nonnull
    public AntlrClassifier getClassifierState()
    {
        return this.classifierState;
    }
}
