package cool.klass.model.converter.compiler.state.property;

import java.util.Objects;
import java.util.Optional;

import javax.annotation.Nonnull;

import cool.klass.model.converter.compiler.CompilationUnit;
import cool.klass.model.converter.compiler.error.CompilerErrorHolder;
import cool.klass.model.converter.compiler.state.AntlrAssociation;
import cool.klass.model.converter.compiler.state.AntlrClass;
import cool.klass.model.converter.compiler.state.AntlrMultiplicity;
import cool.klass.model.converter.compiler.state.order.AntlrOrderBy;
import cool.klass.model.meta.domain.Element;
import cool.klass.model.meta.domain.order.OrderBy.OrderByBuilder;
import cool.klass.model.meta.domain.property.AssociationEnd.AssociationEndBuilder;
import cool.klass.model.meta.domain.property.AssociationEndModifier.AssociationEndModifierBuilder;
import cool.klass.model.meta.grammar.KlassParser.AssociationEndContext;
import org.antlr.v4.runtime.ParserRuleContext;
import org.eclipse.collections.api.list.ImmutableList;
import org.eclipse.collections.impl.factory.Lists;

public class AntlrAssociationEnd extends AntlrReferenceTypeProperty
{
    public static final AntlrAssociationEnd AMBIGUOUS = new AntlrAssociationEnd(
            new AssociationEndContext(null, -1),
            null,
            true,
            Element.NO_CONTEXT,
            "ambiguous association end",
            -1,
            AntlrAssociation.AMBIGUOUS,
            AntlrClass.AMBIGUOUS,
            null,
            Lists.immutable.empty());
    public static final AntlrAssociationEnd NOT_FOUND = new AntlrAssociationEnd(
            new AssociationEndContext(null, -1),
            null,
            true,
            Element.NO_CONTEXT,
            "not found association end",
            -1,
            AntlrAssociation.AMBIGUOUS,
            AntlrClass.AMBIGUOUS,
            null,
            Lists.immutable.empty());

    @Nonnull
    private final AntlrAssociation                           owningAssociationState;
    @Nonnull
    private final ImmutableList<AntlrAssociationEndModifier> associationEndModifierStates;

    private AntlrAssociationEnd   opposite;
    private AssociationEndBuilder associationEndBuilder;

    public AntlrAssociationEnd(
            @Nonnull AssociationEndContext elementContext,
            CompilationUnit compilationUnit,
            boolean inferred,
            @Nonnull ParserRuleContext nameContext,
            @Nonnull String name,
            int ordinal,
            @Nonnull AntlrAssociation owningAssociationState,
            @Nonnull AntlrClass type,
            AntlrMultiplicity multiplicityState,
            @Nonnull ImmutableList<AntlrAssociationEndModifier> associationEndModifierStates)
    {
        super(elementContext, compilationUnit, inferred, nameContext, name, ordinal, type, multiplicityState);
        this.owningAssociationState = Objects.requireNonNull(owningAssociationState);
        this.associationEndModifierStates = Objects.requireNonNull(associationEndModifierStates);
    }

    @Nonnull
    @Override
    public AssociationEndContext getElementContext()
    {
        return (AssociationEndContext) super.getElementContext();
    }

    @Override
    public AssociationEndBuilder build()
    {
        if (this.associationEndBuilder != null)
        {
            throw new IllegalStateException();
        }

        ImmutableList<AssociationEndModifierBuilder> associationEndModifierBuilders =
                this.associationEndModifierStates.collect(AntlrAssociationEndModifier::build);

        // TODO: 🔗 Set association end's opposite
        this.associationEndBuilder = new AssociationEndBuilder(
                this.elementContext,
                this.inferred,
                this.nameContext,
                this.name,
                this.ordinal,
                this.type.getKlassBuilder(),
                this.owningClassState.getKlassBuilder(),
                this.owningAssociationState.getAssociationBuilder(),
                this.multiplicityState.getMultiplicity(),
                associationEndModifierBuilders,
                this.isOwned());

        Optional<OrderByBuilder> orderByBuilder = this.orderByState.map(AntlrOrderBy::build);
        this.associationEndBuilder.setOrderByBuilder(orderByBuilder);

        return this.associationEndBuilder;
    }

    public boolean isOwned()
    {
        return this.associationEndModifierStates.anySatisfy(AntlrAssociationEndModifier::isOwned);
    }

    public void setOpposite(@Nonnull AntlrAssociationEnd opposite)
    {
        this.opposite = Objects.requireNonNull(opposite);
    }

    @Nonnull
    public AssociationEndBuilder getAssociationEndBuilder()
    {
        return Objects.requireNonNull(this.associationEndBuilder);
    }

    @Override
    public void reportNameErrors(@Nonnull CompilerErrorHolder compilerErrorHolder)
    {
        this.reportKeywordCollision(compilerErrorHolder, this.owningAssociationState.getElementContext());

        if (!MEMBER_NAME_PATTERN.matcher(this.name).matches())
        {
            String message = String.format(
                    "ERR_END_NME: Name must match pattern %s but was %s",
                    CONSTANT_NAME_PATTERN,
                    this.name);
            compilerErrorHolder.add(
                    message,
                    this.nameContext,
                    this.owningAssociationState.getElementContext());
        }
    }

    public void reportErrors(CompilerErrorHolder compilerErrorHolder)
    {
        // TODO: Check that there are no duplicate modifiers

        if (this.orderByState != null)
        {
            this.orderByState.ifPresent(o -> o.reportErrors(compilerErrorHolder));
        }
    }

    public void reportDuplicateMemberName(@Nonnull CompilerErrorHolder compilerErrorHolder)
    {
        String message = String.format("ERR_DUP_MEM: Duplicate member: '%s'.", this.name);

        compilerErrorHolder.add(
                message,
                this.nameContext,
                this.owningAssociationState.getElementContext());
    }
}
