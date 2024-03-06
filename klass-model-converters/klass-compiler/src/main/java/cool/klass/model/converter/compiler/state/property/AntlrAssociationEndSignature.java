package cool.klass.model.converter.compiler.state.property;

import java.util.LinkedHashMap;
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
import cool.klass.model.meta.grammar.KlassParser.AssociationEndModifierContext;
import cool.klass.model.meta.grammar.KlassParser.AssociationEndSignatureContext;
import cool.klass.model.meta.grammar.KlassParser.IdentifierContext;
import org.antlr.v4.runtime.ParserRuleContext;
import org.eclipse.collections.api.list.MutableList;
import org.eclipse.collections.api.map.MutableOrderedMap;
import org.eclipse.collections.impl.factory.Lists;
import org.eclipse.collections.impl.map.ordered.mutable.OrderedMapAdapter;

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

    @Nonnull
    private final MutableList<AntlrAssociationEndModifier> associationEndModifierStates = Lists.mutable.empty();

    // private AssociationEndSignatureBuilder associationEndSignatureBuilder;

    private final MutableOrderedMap<String, AntlrAssociationEndModifier> associationEndModifiersByName =
            OrderedMapAdapter.adapt(new LinkedHashMap<>());

    private final MutableOrderedMap<AssociationEndModifierContext, AntlrAssociationEndModifier> associationEndModifiersByContext =
            OrderedMapAdapter.adapt(new LinkedHashMap<>());

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

    public int getNumModifiers()
    {
        return this.associationEndModifierStates.size();
    }

    @Nonnull
    public MutableList<AntlrAssociationEndModifier> getAssociationEndModifiers()
    {
        return this.associationEndModifierStates.asUnmodifiable();
    }

    @Nonnull
    @Override
    public AssociationEndBuilder build()
    {
        throw new AssertionError();
    }

    public boolean isOwned()
    {
        return this.associationEndModifierStates.anySatisfy(AntlrAssociationEndModifier::isOwned);
    }

    @Override
    public void reportErrors(@Nonnull CompilerErrorState compilerErrorHolder)
    {
        // TODO: ☑ Check that there are no duplicate modifiers

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

    public void enterAssociationEndModifier(@Nonnull AntlrAssociationEndModifier associationEndModifierState)
    {
        this.associationEndModifierStates.add(associationEndModifierState);
        this.associationEndModifiersByName.compute(
                associationEndModifierState.getName(),
                (name, builder) -> builder == null
                        ? associationEndModifierState
                        : AntlrAssociationEndModifier.AMBIGUOUS);

        AntlrAssociationEndModifier duplicate = this.associationEndModifiersByContext.put(
                associationEndModifierState.getElementContext(),
                associationEndModifierState);
        if (duplicate != null)
        {
            throw new AssertionError();
        }
    }

    public AntlrAssociationEndModifier getAssociationEndModifierByName(String name)
    {
        return this.associationEndModifiersByName.get(name);
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
