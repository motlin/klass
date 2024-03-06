package cool.klass.model.converter.compiler.state.property;

import java.util.LinkedHashMap;
import java.util.Objects;
import java.util.Optional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import cool.klass.model.converter.compiler.CompilationUnit;
import cool.klass.model.converter.compiler.error.CompilerErrorHolder;
import cool.klass.model.converter.compiler.state.AntlrAssociation;
import cool.klass.model.converter.compiler.state.AntlrClass;
import cool.klass.model.converter.compiler.state.AntlrMultiplicity;
import cool.klass.model.converter.compiler.state.IAntlrElement;
import cool.klass.model.converter.compiler.state.order.AntlrOrderBy;
import cool.klass.model.meta.domain.AbstractElement;
import cool.klass.model.meta.domain.api.Multiplicity;
import cool.klass.model.meta.domain.order.OrderByImpl.OrderByBuilder;
import cool.klass.model.meta.domain.property.AssociationEndImpl.AssociationEndBuilder;
import cool.klass.model.meta.domain.property.AssociationEndModifierImpl.AssociationEndModifierBuilder;
import cool.klass.model.meta.grammar.KlassParser.AssociationEndContext;
import cool.klass.model.meta.grammar.KlassParser.ClassTypeContext;
import org.antlr.v4.runtime.ParserRuleContext;
import org.eclipse.collections.api.list.ImmutableList;
import org.eclipse.collections.api.list.MutableList;
import org.eclipse.collections.api.map.MutableOrderedMap;
import org.eclipse.collections.impl.factory.Lists;
import org.eclipse.collections.impl.map.ordered.mutable.OrderedMapAdapter;

public class AntlrAssociationEnd extends AntlrReferenceTypeProperty
{
    @Nullable
    public static final AntlrAssociationEnd AMBIGUOUS = new AntlrAssociationEnd(
            new AssociationEndContext(null, -1),
            null,
            true,
            AbstractElement.NO_CONTEXT,
            "ambiguous association end",
            -1,
            AntlrAssociation.AMBIGUOUS,
            AntlrClass.AMBIGUOUS,
            null);
    @Nullable
    public static final AntlrAssociationEnd NOT_FOUND = new AntlrAssociationEnd(
            new AssociationEndContext(null, -1),
            null,
            true,
            AbstractElement.NO_CONTEXT,
            "not found association end",
            -1,
            AntlrAssociation.AMBIGUOUS,
            AntlrClass.AMBIGUOUS,
            null);

    @Nonnull
    private final AntlrAssociation                         owningAssociationState;
    @Nonnull
    private final MutableList<AntlrAssociationEndModifier> associationEndModifierStates = Lists.mutable.empty();

    private AntlrClass          owningClassState;
    private AntlrAssociationEnd opposite;

    private AssociationEndBuilder                                 associationEndBuilder;

    private final MutableOrderedMap<AntlrDataTypeProperty<?>, AntlrDataTypeProperty<?>> foreignKeys =
            OrderedMapAdapter.adapt(new LinkedHashMap<>());

    public AntlrAssociationEnd(
            @Nonnull AssociationEndContext elementContext,
            CompilationUnit compilationUnit,
            boolean inferred,
            @Nonnull ParserRuleContext nameContext,
            @Nonnull String name,
            int ordinal,
            @Nonnull AntlrAssociation owningAssociationState,
            @Nonnull AntlrClass type,
            AntlrMultiplicity multiplicityState)
    {
        super(elementContext, compilationUnit, inferred, nameContext, name, ordinal, type, multiplicityState);
        this.owningAssociationState = Objects.requireNonNull(owningAssociationState);
    }

    @Nonnull
    @Override
    public Optional<IAntlrElement> getSurroundingElement()
    {
        return Optional.of(this.owningAssociationState);
    }

    public int getNumModifiers()
    {
        return this.associationEndModifierStates.size();
    }

    @Nonnull
    @Override
    public AssociationEndBuilder build()
    {
        if (this.associationEndBuilder != null)
        {
            throw new IllegalStateException();
        }

        // TODO: 🔗 Set association end's opposite
        this.associationEndBuilder = new AssociationEndBuilder(
                this.elementContext,
                this.inferred,
                this.nameContext,
                this.name,
                this.ordinal,
                this.type.getElementBuilder(),
                this.owningClassState.getElementBuilder(),
                this.owningAssociationState.getElementBuilder(),
                this.multiplicityState.getMultiplicity(),
                this.isOwned());

        ImmutableList<AssociationEndModifierBuilder> associationEndModifierBuilders =
                this.associationEndModifierStates.collect(AntlrAssociationEndModifier::build)
                        .toImmutable();

        this.associationEndBuilder.setAssociationEndModifierBuilders(associationEndModifierBuilders);

        Optional<OrderByBuilder> orderByBuilder = this.orderByState.map(AntlrOrderBy::build);
        this.associationEndBuilder.setOrderByBuilder(orderByBuilder);

        return this.associationEndBuilder;
    }

    public boolean isOwned()
    {
        return this.associationEndModifierStates.anySatisfy(AntlrAssociationEndModifier::isOwned);
    }

    @Override
    public void reportErrors(CompilerErrorHolder compilerErrorHolder)
    {
        // TODO: Check that there are no duplicate modifiers

        if (this.orderByState != null)
        {
            this.orderByState.ifPresent(o -> o.reportErrors(compilerErrorHolder));
        }
    }

    @Nonnull
    @Override
    public AntlrClass getOwningClassState()
    {
        return Objects.requireNonNull(this.owningClassState);
    }

    public void setOwningClassState(@Nonnull AntlrClass owningClassState)
    {
        if (this.owningClassState != null)
        {
            throw new IllegalStateException();
        }
        this.owningClassState = Objects.requireNonNull(owningClassState);
    }

    public boolean isVersioned()
    {
        // TODO: ❗️ Error if both ends are version
        return this.opposite.isVersion();
    }

    public boolean isVersion()
    {
        return this.associationEndModifierStates.anySatisfy(AntlrAssociationEndModifier::isVersion);
    }

    public void setOpposite(@Nonnull AntlrAssociationEnd opposite)
    {
        this.opposite = Objects.requireNonNull(opposite);
    }

    @Nonnull
    public AssociationEndBuilder getElementBuilder()
    {
        return Objects.requireNonNull(this.associationEndBuilder);
    }

    public void reportDuplicateVersionProperty(
            @Nonnull CompilerErrorHolder compilerErrorHolder,
            @Nonnull AntlrClass antlrClass)
    {
        AntlrAssociationEndModifier versionModifier = this.associationEndModifierStates.detect(
                AntlrAssociationEndModifier::isVersion);
        String message = String.format(
                "ERR_VER_END: Multiple version properties on '%s'.",
                antlrClass.getName());
        compilerErrorHolder.add(message, versionModifier);
    }

    public void reportDuplicateVersionedProperty(
            @Nonnull CompilerErrorHolder compilerErrorHolder,
            @Nonnull AntlrClass antlrClass)
    {
        String message = String.format(
                "ERR_VER_END: Multiple versioned properties on '%s'.",
                antlrClass.getName());
        compilerErrorHolder.add(message, this);
    }

    public void enterAssociationEndModifier(AntlrAssociationEndModifier antlrAssociationEndModifier)
    {
        this.associationEndModifierStates.add(antlrAssociationEndModifier);
    }

    @Override
    protected ClassTypeContext getClassType()
    {
        return this.getElementContext().classType();
    }

    @Nonnull
    @Override
    public AssociationEndContext getElementContext()
    {
        return (AssociationEndContext) super.getElementContext();
    }

    @Override
    public void getParserRuleContexts(@Nonnull MutableList<ParserRuleContext> parserRuleContexts)
    {
        parserRuleContexts.add(this.getElementContext());
        this.owningAssociationState.getParserRuleContexts(parserRuleContexts);
    }

    public boolean isToOne()
    {
        return this.getMultiplicity().isToOne();
    }

    public boolean isToMany()
    {
        return this.getMultiplicity().isToMany();
    }

    public void addForeignKeyPropertyMatchingProperty(
            AntlrDataTypeProperty<?> foreignKeyProperty,
            AntlrDataTypeProperty<?> keyProperty)
    {
        this.foreignKeys.put(foreignKeyProperty, keyProperty);
        foreignKeyProperty.setKeyMatchingThisForeignKey(this, keyProperty);
        keyProperty.setForeignKeyMatchingThisKey(this, foreignKeyProperty);
    }

    public boolean hasForeignKeys()
    {
        return this.isOwned()
                || this.isToMany() && this.opposite.isToOne()
                || this.isToOneOptional() && this.opposite.isToOneRequired();
    }

    public boolean isToOneOptional()
    {
        return this.getMultiplicity().getMultiplicity() == Multiplicity.ZERO_TO_ONE;
    }

    public boolean isToOneRequired()
    {
        return this.getMultiplicity().getMultiplicity() == Multiplicity.ONE_TO_ONE;
    }
}
