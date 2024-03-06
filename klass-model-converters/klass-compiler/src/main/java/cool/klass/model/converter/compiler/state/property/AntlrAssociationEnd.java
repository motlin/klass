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
import cool.klass.model.converter.compiler.state.IAntlrElement;
import cool.klass.model.converter.compiler.state.order.AntlrOrderBy;
import cool.klass.model.meta.domain.AbstractElement;
import cool.klass.model.meta.domain.order.OrderByImpl.OrderByBuilder;
import cool.klass.model.meta.domain.property.AssociationEndImpl.AssociationEndBuilder;
import cool.klass.model.meta.domain.property.ModifierImpl.ModifierBuilder;
import cool.klass.model.meta.grammar.KlassParser.AssociationEndContext;
import cool.klass.model.meta.grammar.KlassParser.IdentifierContext;
import org.antlr.v4.runtime.ParserRuleContext;
import org.eclipse.collections.api.list.ImmutableList;
import org.eclipse.collections.api.list.MutableList;
import org.eclipse.collections.api.map.MutableOrderedMap;
import org.eclipse.collections.impl.map.ordered.mutable.OrderedMapAdapter;

public class AntlrAssociationEnd
        extends AntlrClassReferenceProperty
{
    @Nullable
    public static final AntlrAssociationEnd AMBIGUOUS = new AntlrAssociationEnd(
            new AssociationEndContext(null, -1),
            Optional.empty(),
            AbstractElement.NO_CONTEXT,
            -1,
            AntlrAssociation.AMBIGUOUS);

    @Nullable
    public static final AntlrAssociationEnd NOT_FOUND = new AntlrAssociationEnd(
            new AssociationEndContext(null, -1),
            Optional.empty(),
            AbstractElement.NO_CONTEXT,
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

    public AntlrAssociationEnd(
            @Nonnull AssociationEndContext elementContext,
            @Nonnull Optional<CompilationUnit> compilationUnit,
            @Nonnull ParserRuleContext nameContext,
            int ordinal,
            @Nonnull AntlrAssociation owningAssociationState)
    {
        super(elementContext, compilationUnit, nameContext, ordinal);
        this.owningAssociationState = Objects.requireNonNull(owningAssociationState);
    }

    @Nonnull
    @Override
    public Optional<IAntlrElement> getSurroundingElement()
    {
        return Optional.of(this.owningAssociationState);
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
                this.getSourceCodeBuilder(),
                this.nameContext,
                this.ordinal,
                this.getType().getElementBuilder(),
                this.owningClassState.getElementBuilder(),
                this.owningAssociationState.getElementBuilder(),
                this.multiplicityState.getMultiplicity());

        ImmutableList<ModifierBuilder> modifierBuilders = this.getModifiers()
                .collect(AntlrModifier::build)
                .toImmutable();

        this.associationEndBuilder.setModifierBuilders(modifierBuilders);

        Optional<OrderByBuilder> orderByBuilder = this.orderByState.map(AntlrOrderBy::build);
        this.associationEndBuilder.setOrderByBuilder(orderByBuilder);

        return this.associationEndBuilder;
    }

    @Override
    public void reportErrors(@Nonnull CompilerErrorState compilerErrorHolder)
    {
        super.reportErrors(compilerErrorHolder);

        this.orderByState.ifPresent(o -> o.reportErrors(compilerErrorHolder));

        this.reportInvalidMultiplicity(compilerErrorHolder);
        this.reportVersionEndUnowned(compilerErrorHolder);
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
        AntlrModifier versionModifier = this.getModifiers().detect(AntlrModifier::isVersion);
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

    public void addForeignKeyPropertyMatchingProperty(
            @Nonnull AntlrDataTypeProperty<?> foreignKeyProperty,
            @Nonnull AntlrDataTypeProperty<?> keyProperty)
    {
        this.foreignKeys.put(foreignKeyProperty, keyProperty);
        foreignKeyProperty.setKeyMatchingThisForeignKey(this, keyProperty);
        keyProperty.setForeignKeyMatchingThisKey(this, foreignKeyProperty);
    }

    public boolean hasForeignKey()
    {
        return this.isToOneRequired() && this.opposite.isToMany()
                || this.isToOneRequired() && this.opposite.isToOneOptional();
    }

    @Override
    protected IdentifierContext getTypeIdentifier()
    {
        return this.getElementContext().classReference().identifier();
    }

    public boolean isSourceEnd()
    {
        return this == this.owningAssociationState.getSourceEnd();
    }

    public boolean isTargetEnd()
    {
        return this == this.owningAssociationState.getTargetEnd();
    }
}
