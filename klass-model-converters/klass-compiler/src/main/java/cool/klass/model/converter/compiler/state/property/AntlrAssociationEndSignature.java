package cool.klass.model.converter.compiler.state.property;

import java.util.Objects;
import java.util.Optional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import cool.klass.model.converter.compiler.CompilationUnit;
import cool.klass.model.converter.compiler.error.CompilerErrorState;
import cool.klass.model.converter.compiler.state.AntlrClassifier;
import cool.klass.model.converter.compiler.state.AntlrClassifierReference;
import cool.klass.model.converter.compiler.state.AntlrClassifierReferenceOwner;
import cool.klass.model.converter.compiler.state.IAntlrElement;
import cool.klass.model.converter.compiler.state.order.AntlrOrderBy;
import cool.klass.model.meta.domain.AbstractElement;
import cool.klass.model.meta.domain.order.OrderByImpl.OrderByBuilder;
import cool.klass.model.meta.domain.property.AssociationEndModifierImpl.AssociationEndModifierBuilder;
import cool.klass.model.meta.domain.property.AssociationEndSignatureImpl.AssociationEndSignatureBuilder;
import cool.klass.model.meta.grammar.KlassParser.AssociationEndSignatureContext;
import cool.klass.model.meta.grammar.KlassParser.IdentifierContext;
import org.antlr.v4.runtime.ParserRuleContext;
import org.eclipse.collections.api.list.ImmutableList;
import org.eclipse.collections.api.list.MutableList;

public class AntlrAssociationEndSignature
        extends AntlrReferenceProperty<AntlrClassifier>
        implements AntlrClassifierReferenceOwner
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

    private AssociationEndSignatureBuilder associationEndSignatureBuilder;

    private AntlrClassifierReference classifierReferenceState;

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

    @Nonnull
    @Override
    public AssociationEndSignatureBuilder build()
    {
        if (this.associationEndSignatureBuilder != null)
        {
            throw new IllegalStateException();
        }

        // TODO: 🔗 Set association end's opposite
        this.associationEndSignatureBuilder = new AssociationEndSignatureBuilder(
                this.elementContext,
                this.getMacroElementBuilder(),
                this.getSourceCodeBuilder(),
                this.nameContext,
                this.name,
                this.ordinal,
                this.getType().getElementBuilder(),
                this.owningClassifierState.getElementBuilder(),
                this.multiplicityState.getMultiplicity(),
                this.isOwned());

        ImmutableList<AssociationEndModifierBuilder> associationEndModifierBuilders = this.getModifiers()
                .collect(AntlrAssociationEndModifier.class::cast)
                .collect(AntlrAssociationEndModifier::build)
                .toImmutable();

        this.associationEndSignatureBuilder.setAssociationEndModifierBuilders(associationEndModifierBuilders);

        Optional<OrderByBuilder> orderByBuilder = this.orderByState.map(AntlrOrderBy::build);
        this.associationEndSignatureBuilder.setOrderByBuilder(orderByBuilder);

        return this.associationEndSignatureBuilder;
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

        if (this.orderByState != null)
        {
            this.orderByState.ifPresent(o -> o.reportErrors(compilerErrorHolder));
        }

        this.reportInvalidMultiplicity(compilerErrorHolder);
    }

    @Nonnull
    @Override
    public AntlrClassifier getOwningClassifierState()
    {
        return Objects.requireNonNull(this.owningClassifierState);
    }

    @Override
    @Nonnull
    public AssociationEndSignatureBuilder getElementBuilder()
    {
        return Objects.requireNonNull(this.associationEndSignatureBuilder);
    }

    @Override
    protected IdentifierContext getTypeIdentifier()
    {
        return this.getElementContext().classifierReference().identifier();
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
        return this.classifierReferenceState.getClassifierState();
    }

    @Override
    public void enterClassifierReference(@Nonnull AntlrClassifierReference classifierReferenceState)
    {
        if (this.classifierReferenceState != null)
        {
            throw new AssertionError();
        }
        this.classifierReferenceState = Objects.requireNonNull(classifierReferenceState);
    }
}
