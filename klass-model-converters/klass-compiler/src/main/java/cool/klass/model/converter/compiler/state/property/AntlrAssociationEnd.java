package cool.klass.model.converter.compiler.state.property;

import java.util.Objects;

import javax.annotation.Nonnull;

import cool.klass.model.converter.compiler.CompilationUnit;
import cool.klass.model.converter.compiler.error.CompilerErrorHolder;
import cool.klass.model.converter.compiler.state.AntlrAssociation;
import cool.klass.model.converter.compiler.state.AntlrClass;
import cool.klass.model.converter.compiler.state.AntlrMultiplicity;
import cool.klass.model.meta.domain.Element;
import cool.klass.model.meta.domain.Klass;
import cool.klass.model.meta.domain.property.AssociationEnd.AssociationEndBuilder;
import cool.klass.model.meta.grammar.KlassParser.AssociationEndContext;
import org.antlr.v4.runtime.ParserRuleContext;
import org.eclipse.collections.api.list.ImmutableList;
import org.eclipse.collections.impl.factory.Lists;

public class AntlrAssociationEnd extends AntlrProperty<Klass>
{
    public static final AntlrAssociationEnd AMBIGUOUS = new AntlrAssociationEnd(
            new AssociationEndContext(null, -1),
            null,
            true,
            "ambiguous association end",
            Element.NO_CONTEXT,
            AntlrAssociation.AMBIGUOUS,
            AntlrClass.AMBIGUOUS,
            null,
            Lists.immutable.empty());
    public static final AntlrAssociationEnd NOT_FOUND = new AntlrAssociationEnd(
            new AssociationEndContext(null, -1),
            null,
            true,
            "not found association end",
            Element.NO_CONTEXT,
            AntlrAssociation.AMBIGUOUS,
            AntlrClass.AMBIGUOUS,
            null,
            Lists.immutable.empty());

    @Nonnull
    private final AntlrAssociation                           owningAssociationState;
    @Nonnull
    private final AntlrClass                                 type;
    private final AntlrMultiplicity                          antlrMultiplicity;
    @Nonnull
    private final ImmutableList<AntlrAssociationEndModifier> modifiers;

    private AntlrClass            owningClassState;
    private AntlrAssociationEnd   opposite;
    private AssociationEndBuilder associationEndBuilder;

    public AntlrAssociationEnd(
            @Nonnull AssociationEndContext elementContext,
            CompilationUnit compilationUnit,
            boolean inferred,
            @Nonnull String name,
            @Nonnull ParserRuleContext nameContext,
            @Nonnull AntlrAssociation owningAssociationState,
            @Nonnull AntlrClass type,
            AntlrMultiplicity antlrMultiplicity,
            @Nonnull ImmutableList<AntlrAssociationEndModifier> modifiers)
    {
        super(elementContext, compilationUnit, inferred, name, nameContext);
        this.owningAssociationState = Objects.requireNonNull(owningAssociationState);
        this.type = Objects.requireNonNull(type);
        this.antlrMultiplicity = antlrMultiplicity;
        this.modifiers = Objects.requireNonNull(modifiers);
    }

    @Nonnull
    @Override
    public AssociationEndContext getElementContext()
    {
        return (AssociationEndContext) super.getElementContext();
    }

    @Override
    @Nonnull
    public AntlrClass getType()
    {
        return this.type;
    }

    public AntlrMultiplicity getAntlrMultiplicity()
    {
        return this.antlrMultiplicity;
    }

    @Nonnull
    public ImmutableList<AntlrAssociationEndModifier> getModifiers()
    {
        return this.modifiers;
    }

    public AntlrAssociationEnd getOpposite()
    {
        return this.opposite;
    }

    public void setOpposite(@Nonnull AntlrAssociationEnd opposite)
    {
        this.opposite = Objects.requireNonNull(opposite);
    }

    @Override
    public AssociationEndBuilder build()
    {
        if (this.associationEndBuilder != null)
        {
            throw new IllegalStateException();
        }
        this.associationEndBuilder = new AssociationEndBuilder(
                this.elementContext,
                this.nameContext,
                this.name,
                this.type.getKlassBuilder(),
                this.owningClassState.getKlassBuilder(),
                this.owningAssociationState.getAssociationBuilder(),
                this.antlrMultiplicity.getMultiplicity(),
                this.isOwned());
        return this.associationEndBuilder;
    }

    @Override
    public AntlrClass getOwningClassState()
    {
        return this.owningClassState;
    }

    public void setOwningClassState(@Nonnull AntlrClass owningClassState)
    {
        this.owningClassState = Objects.requireNonNull(owningClassState);
    }

    public boolean isOwned()
    {
        return this.modifiers.anySatisfy(AntlrAssociationEndModifier::isOwned);
    }

    @Nonnull
    public AssociationEndBuilder getAssociationEndBuilder()
    {
        return Objects.requireNonNull(this.associationEndBuilder);
    }

    public void reportErrors(CompilerErrorHolder compilerErrorHolder)
    {
        // TODO: Check that there are no duplicate modifiers
    }

    public void reportDuplicateMemberName(@Nonnull CompilerErrorHolder compilerErrorHolder)
    {
        String message = String.format("ERR_DUP_MEM: Duplicate member: '%s'.", this.name);

        compilerErrorHolder.add(
                this.compilationUnit,
                message,
                this.nameContext,
                this.owningAssociationState.getElementContext());
    }
}
