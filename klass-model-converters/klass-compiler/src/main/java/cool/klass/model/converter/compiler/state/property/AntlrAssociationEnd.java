package cool.klass.model.converter.compiler.state.property;

import java.util.LinkedHashMap;
import java.util.Objects;
import java.util.Optional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import cool.klass.model.converter.compiler.CompilationUnit;
import cool.klass.model.converter.compiler.error.CompilerErrorState;
import cool.klass.model.converter.compiler.state.AntlrAssociation;
import cool.klass.model.converter.compiler.state.AntlrClass;
import cool.klass.model.converter.compiler.state.AntlrClassType;
import cool.klass.model.converter.compiler.state.AntlrMultiplicity;
import cool.klass.model.converter.compiler.state.IAntlrElement;
import cool.klass.model.converter.compiler.state.order.AntlrOrderBy;
import cool.klass.model.meta.domain.AbstractElement;
import cool.klass.model.meta.domain.api.Multiplicity;
import cool.klass.model.meta.domain.order.OrderByImpl.OrderByBuilder;
import cool.klass.model.meta.domain.property.AssociationEndImpl.AssociationEndBuilder;
import cool.klass.model.meta.domain.property.AssociationEndModifierImpl.AssociationEndModifierBuilder;
import cool.klass.model.meta.grammar.KlassParser.AssociationEndContext;
import cool.klass.model.meta.grammar.KlassParser.IdentifierContext;
import org.antlr.v4.runtime.ParserRuleContext;
import org.eclipse.collections.api.list.ImmutableList;
import org.eclipse.collections.api.list.MutableList;
import org.eclipse.collections.api.map.MutableOrderedMap;
import org.eclipse.collections.impl.list.fixed.ArrayAdapter;
import org.eclipse.collections.impl.map.ordered.mutable.OrderedMapAdapter;

public class AntlrAssociationEnd
        extends AntlrReferenceTypeProperty<AntlrClass>
        implements AntlrClassTypeOwner
{
    @Nullable
    public static final AntlrAssociationEnd AMBIGUOUS = new AntlrAssociationEnd(
            new AssociationEndContext(null, -1),
            Optional.empty(),
            AbstractElement.NO_CONTEXT,
            "ambiguous association end",
            -1,
            AntlrAssociation.AMBIGUOUS);

    @Nullable
    public static final AntlrAssociationEnd NOT_FOUND = new AntlrAssociationEnd(
            new AssociationEndContext(null, -1),
            Optional.empty(),
            AbstractElement.NO_CONTEXT,
            "not found association end",
            -1,
            // TODO: Not found here, instead of ambiguous
            AntlrAssociation.AMBIGUOUS);

    @Nonnull
    private final AntlrAssociation owningAssociationState;

    private AntlrClass          owningClassState;
    private AntlrAssociationEnd opposite;

    private AssociationEndBuilder associationEndBuilder;

    private final MutableOrderedMap<AntlrDataTypeProperty<?>, AntlrDataTypeProperty<?>> foreignKeys =
            OrderedMapAdapter.adapt(new LinkedHashMap<>());

    private AntlrClassType classTypeState;

    public AntlrAssociationEnd(
            @Nonnull AssociationEndContext elementContext,
            @Nonnull Optional<CompilationUnit> compilationUnit,
            @Nonnull ParserRuleContext nameContext,
            @Nonnull String name,
            int ordinal,
            @Nonnull AntlrAssociation owningAssociationState)
    {
        super(elementContext, compilationUnit, nameContext, name, ordinal);
        this.owningAssociationState = Objects.requireNonNull(owningAssociationState);
    }

    @Nonnull
    @Override
    public Optional<IAntlrElement> getSurroundingElement()
    {
        return Optional.of(this.owningAssociationState);
    }

    @Override
    public AntlrMultiplicity getMultiplicity()
    {
        return this.classTypeState.getMultiplicity();
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
                this.getMacroElementBuilder(),
                this.nameContext,
                this.name,
                this.ordinal,
                this.getType().getElementBuilder(),
                this.owningClassState.getElementBuilder(),
                this.owningAssociationState.getElementBuilder(),
                this.getMultiplicity().getMultiplicity(),
                this.isOwned());

        ImmutableList<AssociationEndModifierBuilder> associationEndModifierBuilders = this.getModifiers()
                .collect(AntlrAssociationEndModifier.class::cast)
                .collect(AntlrAssociationEndModifier::build)
                .toImmutable();

        this.associationEndBuilder.setAssociationEndModifierBuilders(associationEndModifierBuilders);

        Optional<OrderByBuilder> orderByBuilder = this.orderByState.map(AntlrOrderBy::build);
        this.associationEndBuilder.setOrderByBuilder(orderByBuilder);

        return this.associationEndBuilder;
    }

    public boolean isOwned()
    {
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
        this.reportVersionEndUnowned(compilerErrorHolder);
    }

    private void reportInvalidMultiplicity(@Nonnull CompilerErrorState compilerErrorHolder)
    {
        if (this.getMultiplicity().getMultiplicity() == null)
        {
            String multiplicityChoices = ArrayAdapter.adapt(Multiplicity.values())
                    .collect(Multiplicity::getPrettyName)
                    .collect(each -> '[' + each + ']')
                    .makeString();

            String message = String.format(
                    "Association end '%s: %s[%s..%s]' has invalid multiplicity. Expected one of %s.",
                    this.getName(),
                    this.getOwningClassifierState().getName(),
                    this.getMultiplicity().getLowerBoundText(),
                    this.getMultiplicity().getUpperBoundText(),
                    multiplicityChoices);

            compilerErrorHolder.add("ERR_ASO_MUL", message, this.getMultiplicity());
        }
    }

    private void reportVersionEndUnowned(@Nonnull CompilerErrorState compilerErrorHolder)
    {
        if (this.isVersion() && !this.isOwned())
        {
            String message = String.format(
                    "Expected version association end '%s.%s' to be owned.",
                    this.getOwningClassifierState().getName(),
                    this.getName());
            compilerErrorHolder.add("ERR_VER_OWN", message, this, this.nameContext);
        }
    }

    @Nonnull
    @Override
    public AntlrClass getOwningClassifierState()
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
        return this.opposite.isVersion();
    }

    public boolean isVersion()
    {
        return this.getModifiers()
                .collect(AntlrAssociationEndModifier.class::cast)
                .anySatisfy(AntlrAssociationEndModifier::isVersion);
    }

    public void setOpposite(@Nonnull AntlrAssociationEnd opposite)
    {
        this.opposite = Objects.requireNonNull(opposite);
    }

    @Override
    @Nonnull
    public AssociationEndBuilder getElementBuilder()
    {
        return Objects.requireNonNull(this.associationEndBuilder);
    }

    public void reportDuplicateVersionProperty(
            @Nonnull CompilerErrorState compilerErrorHolder,
            @Nonnull AntlrClass antlrClass)
    {
        AntlrAssociationEndModifier versionModifier = this.getModifiers()
                .collect(AntlrAssociationEndModifier.class::cast)
                .detect(AntlrAssociationEndModifier::isVersion);
        String message = String.format(
                "Multiple version properties on '%s'.",
                antlrClass.getName());
        compilerErrorHolder.add("ERR_VER_END", message, versionModifier);
    }

    public void reportDuplicateVersionedProperty(
            @Nonnull CompilerErrorState compilerErrorHolder,
            @Nonnull AntlrClass antlrClass)
    {
        String message = String.format(
                "Multiple versioned properties on '%s'.",
                antlrClass.getName());
        compilerErrorHolder.add("ERR_VER_END", message, this);
    }

    @Override
    protected IdentifierContext getTypeIdentifier()
    {
        return this.getElementContext().classType().classReference().identifier();
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
        AntlrMultiplicity multiplicity = this.getMultiplicity();
        return multiplicity != null && multiplicity.isToOne();
    }

    public boolean isToMany()
    {
        AntlrMultiplicity multiplicity = this.getMultiplicity();
        return multiplicity != null && multiplicity.isToMany();
    }

    public void addForeignKeyPropertyMatchingProperty(
            @Nonnull AntlrDataTypeProperty<?> foreignKeyProperty,
            @Nonnull AntlrDataTypeProperty<?> keyProperty)
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
        AntlrMultiplicity multiplicity = this.getMultiplicity();
        return multiplicity != null && multiplicity.getMultiplicity() == Multiplicity.ZERO_TO_ONE;
    }

    public boolean isToOneRequired()
    {
        AntlrMultiplicity multiplicity = this.getMultiplicity();
        return multiplicity != null && multiplicity.getMultiplicity() == Multiplicity.ONE_TO_ONE;
    }

    @Nonnull
    @Override
    public AntlrClass getType()
    {
        return this.classTypeState.getType();
    }

    @Override
    public void enterClassType(@Nonnull AntlrClassType classTypeState)
    {
        if (this.classTypeState != null)
        {
            throw new AssertionError();
        }

        this.classTypeState = Objects.requireNonNull(classTypeState);
    }
}
