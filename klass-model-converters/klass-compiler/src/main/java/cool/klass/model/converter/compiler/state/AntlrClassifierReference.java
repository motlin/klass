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
    private final AntlrClassifierReferenceOwner classifierReferenceOwner;
    @Nonnull
    private final AntlrClassifier               classifier;

    public AntlrClassifierReference(
            @Nonnull ClassifierReferenceContext classifierReferenceContext,
            @Nonnull Optional<CompilationUnit> compilationUnit,
            @Nonnull AntlrClassifierReferenceOwner classifierReferenceOwner,
            @Nonnull AntlrClassifier classifier)
    {
        super(classifierReferenceContext, compilationUnit);
        this.classifierReferenceOwner = Objects.requireNonNull(classifierReferenceOwner);
        this.classifier               = Objects.requireNonNull(classifier);
    }

    @Nonnull
    @Override
    public Optional<IAntlrElement> getSurroundingElement()
    {
        return Optional.of(this.classifierReferenceOwner);
    }

    @Nonnull
    public AntlrClassifier getClassifier()
    {
        return this.classifier;
    }
}
