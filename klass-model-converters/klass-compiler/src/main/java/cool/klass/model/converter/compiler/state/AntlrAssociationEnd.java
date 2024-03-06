package cool.klass.model.converter.compiler.state;

import java.util.Objects;

import cool.klass.model.converter.compiler.CompilationUnit;
import cool.klass.model.converter.compiler.error.CompilerErrorHolder;
import cool.klass.model.meta.domain.Association.AssociationBuilder;
import cool.klass.model.meta.domain.AssociationEnd.AssociationEndBuilder;
import cool.klass.model.meta.domain.Element;
import cool.klass.model.meta.domain.Klass;
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
            "ambiguous property",
            Element.NO_CONTEXT,
            AntlrClass.AMBIGUOUS,
            null,
            Lists.immutable.empty());

    private final AntlrClass                                 type;
    private final AntlrMultiplicity                          antlrMultiplicity;
    private final ImmutableList<AntlrAssociationEndModifier> modifiers;

    private AntlrClass            owningClassState;
    private AntlrAssociationEnd   opposite;
    private AssociationBuilder    associationBuilder;
    private AssociationEndBuilder associationEndBuilder;

    public AntlrAssociationEnd(
            AssociationEndContext elementContext,
            CompilationUnit compilationUnit,
            boolean inferred,
            String name,
            ParserRuleContext nameContext,
            AntlrClass type,
            AntlrMultiplicity antlrMultiplicity,
            ImmutableList<AntlrAssociationEndModifier> modifiers)
    {
        super(elementContext, compilationUnit, inferred, name, nameContext);
        this.type = Objects.requireNonNull(type);
        this.antlrMultiplicity = antlrMultiplicity;
        this.modifiers = Objects.requireNonNull(modifiers);
    }

    public AntlrClass getType()
    {
        return this.type;
    }

    public AntlrMultiplicity getAntlrMultiplicity()
    {
        return this.antlrMultiplicity;
    }

    public ImmutableList<AntlrAssociationEndModifier> getModifiers()
    {
        return this.modifiers;
    }

    public AntlrAssociationEnd getOpposite()
    {
        return this.opposite;
    }

    public void setOpposite(AntlrAssociationEnd opposite)
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
                this.associationBuilder,
                this.antlrMultiplicity.getMultiplicity(),
                this.isOwned());
        return this.associationEndBuilder;
    }

    @Override
    public AntlrClass getOwningClassState()
    {
        return this.owningClassState;
    }

    public void setOwningClassState(AntlrClass owningClassState)
    {
        this.owningClassState = Objects.requireNonNull(owningClassState);
    }

    public boolean isOwned()
    {
        return this.modifiers.anySatisfy(AntlrAssociationEndModifier::isOwned);
    }

    public AssociationBuilder getAssociationBuilder()
    {
        return Objects.requireNonNull(this.associationBuilder);
    }

    public AssociationEndBuilder getAssociationEndBuilder()
    {
        return Objects.requireNonNull(this.associationEndBuilder);
    }

    public void setOwningAssociation(AssociationBuilder associationBuilder)
    {
        this.associationBuilder = Objects.requireNonNull(associationBuilder);
    }

    public void reportErrors(CompilerErrorHolder compilerErrorHolder)
    {
        // TODO: Check that there are no duplicate modifiers
    }
}
