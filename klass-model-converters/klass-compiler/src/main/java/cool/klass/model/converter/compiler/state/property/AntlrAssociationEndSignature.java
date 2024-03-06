package cool.klass.model.converter.compiler.state.property;

import java.util.Objects;
import java.util.Optional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import cool.klass.model.converter.compiler.CompilationUnit;
import cool.klass.model.converter.compiler.error.CompilerErrorState;
import cool.klass.model.converter.compiler.state.AntlrClassifier;
import cool.klass.model.converter.compiler.state.AntlrClassifierType;
import cool.klass.model.converter.compiler.state.AntlrMultiplicity;
import cool.klass.model.converter.compiler.state.IAntlrElement;
import cool.klass.model.meta.domain.AbstractElement;
import cool.klass.model.meta.domain.property.AssociationEndImpl.AssociationEndBuilder;
import cool.klass.model.meta.grammar.KlassParser.AssociationEndSignatureContext;
import cool.klass.model.meta.grammar.KlassParser.IdentifierContext;
import org.antlr.v4.runtime.ParserRuleContext;
import org.eclipse.collections.api.list.MutableList;

public class AntlrAssociationEndSignature
        extends AntlrReferenceTypeProperty<AntlrClassifier>
        implements AntlrClassifierTypeOwner
{
    @Nullable
    public static final AntlrAssociationEndSignature AMBIGUOUS = new AntlrAssociationEndSignature(
            new AssociationEndSignatureContext(null, -1),
            Optional.empty(),
            AbstractElement.NO_CONTEXT,
            "ambiguous association end",
            -1,
            AntlrClassifier.AMBIGUOUS);

    @Nullable
    public static final AntlrAssociationEndSignature NOT_FOUND = new AntlrAssociationEndSignature(
            new AssociationEndSignatureContext(null, -1),
            Optional.empty(),
            AbstractElement.NO_CONTEXT,
            "not found association end",
            -1,
            // TODO: Not found here, instead of ambiguous
            AntlrClassifier.NOT_FOUND);

    @Nonnull
    private final AntlrClassifier owningClassifierState;

    // private AssociationEndSignatureBuilder associationEndSignatureBuilder;

    @Nullable
    private AntlrClassifierType classifierTypeState;

    public AntlrAssociationEndSignature(
            @Nonnull AssociationEndSignatureContext elementContext,
            @Nonnull Optional<CompilationUnit> compilationUnit,
            @Nonnull ParserRuleContext nameContext,
            @Nonnull String name,
            int ordinal,
            @Nonnull AntlrClassifier owningClassifierState)
    {
        super(elementContext, compilationUnit, nameContext, name, ordinal);
        this.owningClassifierState = Objects.requireNonNull(owningClassifierState);
    }

    @Nonnull
    @Override
    public Optional<IAntlrElement> getSurroundingElement()
    {
        return Optional.of(this.owningClassifierState);
    }

    @Override
    public AntlrMultiplicity getMultiplicity()
    {
        return this.classifierTypeState.getMultiplicity();
    }

    @Nonnull
    @Override
    public AssociationEndBuilder build()
    {
        throw new AssertionError();
    }

    public boolean isOwned()
    {
        // TODO: Consider generics instead of cast
        return this.getModifiers()
                .collect(AntlrAssociationEndModifier.class::cast)
                .anySatisfy(AntlrAssociationEndModifier::isOwned);
    }

    @Override
    public void reportErrors(@Nonnull CompilerErrorState compilerErrorHolder)
    {
        super.reportErrors(compilerErrorHolder);

        String message = String.format(
                "Reference type properties (single association ends in classifiers) are not yet supported but found '%s.%s'.",
                this.getOwningClassifierState().getName(),
                this.getName());
        compilerErrorHolder.add("ERR_ONE_END", message, this, this.nameContext);
    }

    @Nonnull
    @Override
    public AntlrClassifier getOwningClassifierState()
    {
        return Objects.requireNonNull(this.owningClassifierState);
    }

    @Override
    @Nonnull
    public AssociationEndBuilder getElementBuilder()
    {
        throw new AssertionError();
    }

    @Override
    protected IdentifierContext getTypeIdentifier()
    {
        return this.getElementContext().classifierType().classifierReference().identifier();
    }

    @Nonnull
    @Override
    public AssociationEndSignatureContext getElementContext()
    {
        return (AssociationEndSignatureContext) super.getElementContext();
    }

    @Override
    public void getParserRuleContexts(@Nonnull MutableList<ParserRuleContext> parserRuleContexts)
    {
        parserRuleContexts.add(this.getElementContext());
        this.owningClassifierState.getParserRuleContexts(parserRuleContexts);
    }

    @Nonnull
    @Override
    public AntlrClassifier getType()
    {
        return this.classifierTypeState.getType();
    }

    @Override
    public void enterClassifierType(@Nonnull AntlrClassifierType classifierTypeState)
    {
        if (this.classifierTypeState != null)
        {
            throw new AssertionError();
        }

        this.classifierTypeState = Objects.requireNonNull(classifierTypeState);
    }
}
