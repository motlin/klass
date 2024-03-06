package cool.klass.model.converter.compiler.state.property;

import java.util.LinkedHashMap;
import java.util.Objects;
import java.util.Optional;

import javax.annotation.Nonnull;

import cool.klass.model.converter.compiler.CompilationUnit;
import cool.klass.model.converter.compiler.error.CompilerErrorState;
import cool.klass.model.converter.compiler.state.AntlrAssociation;
import cool.klass.model.converter.compiler.state.AntlrClass;
import cool.klass.model.converter.compiler.state.AntlrClassifier;
import cool.klass.model.converter.compiler.state.IAntlrElement;
import cool.klass.model.converter.compiler.state.order.AntlrOrderBy;
import cool.klass.model.meta.domain.order.OrderByImpl.OrderByBuilder;
import cool.klass.model.meta.domain.property.AssociationEndImpl.AssociationEndBuilder;
import cool.klass.model.meta.domain.property.ModifierImpl.ModifierBuilder;
import cool.klass.model.meta.grammar.KlassParser.AssociationEndContext;
import cool.klass.model.meta.grammar.KlassParser.IdentifierContext;
import org.eclipse.collections.api.list.ImmutableList;
import org.eclipse.collections.api.map.MutableOrderedMap;
import org.eclipse.collections.impl.factory.Lists;
import org.eclipse.collections.impl.map.ordered.mutable.OrderedMapAdapter;

public class AntlrAssociationEnd
        extends AntlrClassReferenceProperty
{
    //<editor-fold desc="AMBIGUOUS">
    public static final AntlrAssociationEnd AMBIGUOUS = new AntlrAssociationEnd(
            new AssociationEndContext(null, -1),
            Optional.empty(),
            -1,
            new IdentifierContext(null, -1),
            AntlrAssociation.AMBIGUOUS)
    {
        @Nonnull
        @Override
        public AntlrClass getType()
        {
            return AntlrClass.AMBIGUOUS;
        }

        @Override
        public String toString()
        {
            return AntlrAssociationEnd.class.getSimpleName() + ".AMBIGUOUS";
        }
    };
    //</editor-fold>

    //<editor-fold desc="NOT_FOUND">
    public static final AntlrAssociationEnd NOT_FOUND = new AntlrAssociationEnd(
            new AssociationEndContext(null, -1),
            Optional.empty(),
            -1,
            new IdentifierContext(null, -1),
            AntlrAssociation.NOT_FOUND)
    {
        @Nonnull
        @Override
        public AntlrClass getType()
        {
            return AntlrClass.NOT_FOUND;
        }

        @Override
        public String toString()
        {
            return AntlrAssociationEnd.class.getSimpleName() + ".NOT_FOUND";
        }
    };
    //</editor-fold>

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
            int ordinal,
            @Nonnull IdentifierContext nameContext,
            @Nonnull AntlrAssociation owningAssociationState)
    {
        super(elementContext, compilationUnit, ordinal, nameContext);
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
                this.getElementContext(),
                this.getMacroElementBuilder(),
                this.getSourceCodeBuilder(),
                this.ordinal,
                this.getNameContext(),
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
        this.reportNonVersionEnd(compilerErrorHolder);
        this.reportNonUserAuditEnd(compilerErrorHolder);
        this.reportPluralName(compilerErrorHolder);
        this.reportDeclarationOrderTypes(compilerErrorHolder);
        this.reportForwardReference(compilerErrorHolder);
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

    private void reportNonVersionEnd(CompilerErrorState compilerErrorHolder)
    {
        if (this.isVersion() && !this.getType().isVersion())
        {
            String message = String.format(
                    "Expected version association end '%s.%s' to have version type, but %s has no version property.",
                    this.getOwningClassifierState().getName(),
                    this.getName(),
                    this.getType().getName());
            compilerErrorHolder.add("ERR_VER_END", message, this, this.nameContext);
        }
        if (!this.getOwningClassifierState().isUser()
                && !this.isVersion() && this.getType().isVersion()
                && !this.getOpposite().isCreatedBy()
                && !this.getOpposite().isLastUpdatedBy())
        {
            String message = String.format(
                    "Association end '%s.%s' has version type %s, but is missing the version modifier.",
                    this.getOwningClassifierState().getName(),
                    this.getName(),
                    this.getType().getName());
            compilerErrorHolder.add(
                    "ERR_VER_TYP",
                    message,
                    this,
                    Lists.immutable.with(this.nameContext, this.getElementContext().classReference()));
        }
    }

    private void reportNonUserAuditEnd(CompilerErrorState compilerErrorHolder)
    {
        if (this.isCreatedBy() && !this.getType().isUser())
        {
            AntlrModifier modifier = this.getModifiers().detect(AntlrModifier::isCreatedBy);
            String message = String.format(
                    "Expected createdBy association end '%s.%s' to have user type, but was %s.",
                    this.getOwningClassifierState().getName(),
                    this.getName(),
                    this.getType().getName());
            compilerErrorHolder.add(
                    "ERR_AUD_END",
                    message,
                    modifier,
                    modifier.getSurroundingElements(),
                    Lists.immutable.with(this.getElementContext().classReference(), modifier.getElementContext()));
        }
        if (this.isLastUpdatedBy() && !this.getType().isUser())
        {
            AntlrModifier modifier = this.getModifiers().detect(AntlrModifier::isLastUpdatedBy);
            String message = String.format(
                    "Expected lastUpdatedBy association end '%s.%s' to have user type, but was %s.",
                    this.getOwningClassifierState().getName(),
                    this.getName(),
                    this.getType().getName());

            compilerErrorHolder.add(
                    "ERR_AUD_END",
                    message,
                    modifier,
                    modifier.getSurroundingElements(),
                    Lists.immutable.with(this.getElementContext().classReference(), modifier.getElementContext()));
        }
    }

    public void reportDuplicateOppositeWithModifier(
            @Nonnull CompilerErrorState compilerErrorHolder,
            @Nonnull AntlrClassifier classifier,
            String modifier)
    {
        AntlrModifier modifierState = this.getModifiers().detectWith(AntlrModifier::is, modifier);
        String message = String.format(
                "Multiple %s association ends point at '%s'.",
                modifier,
                classifier.getName());
        compilerErrorHolder.add("ERR_DUP_END", message, modifierState);
    }

    private void reportPluralName(CompilerErrorState compilerErrorHolder)
    {
        if (this.multiplicityState.isToMany())
        {
            if (this.classReferenceState.getElementContext().identifier().getText().toLowerCase().endsWith(this.getName().toLowerCase()))
            {
                String message = "Expected to-many association end '%s.%s' to have a plural name, but name exactly matched type association end type '%s'.".formatted(
                        this.getOwningClassifierState().getName(),
                        this.getName(),
                        this.classReferenceState.getElementContext().identifier().getText());
                compilerErrorHolder.add("ERR_ASS_PLU", message, this, this.nameContext);
            }
        }
    }

    private void reportDeclarationOrderTypes(CompilerErrorState compilerErrorHolder)
    {
        if (!this.isToOneRequired())
        {
            return;
        }

        if (this.foreignKeys.isEmpty())
        {
            return;
        }

        if (this.isVersion())
        {
            // We reach here in the case of a version association end that is not owned
            // This will trigger a different error: ERR_VER_OWN
            return;
        }

        if (this.opposite.getType().isForwardReference(this.getType()))
        {
            String message = String.format(
                    "Association '%s' establishes that type '%s' requires type '%s', so it ought to be declared later in the source file. '%s' is declared on line %d and '%s' is declared on line %d in source file '%s'.",
                    this.owningAssociationState.getName(),
                    this.opposite.getType().getName(),
                    this.getType().getName(),
                    this.opposite.getType().getName(),
                    this.opposite.getType().getElementContext().getStart().getLine(),
                    this.getType().getName(),
                    this.getType().getElementContext().getStart().getLine(),
                    this.getCompilationUnit().get().getSourceName());
            compilerErrorHolder.add(
                    "ERR_ASO_ORD",
                    message,
                    this,
                    this.getElementContext().classReference());
        }
    }

    private void reportForwardReference(CompilerErrorState compilerErrorHolder)
    {
        if (!this.owningAssociationState.isForwardReference(this.getType()))
        {
            return;
        }
        String message = String.format(
                "Association end '%s.%s' is declared on line %d and has a forward reference to type '%s' which is declared later in the source file '%s' on line %d.",
                this.getOwningClassifierState().getName(),
                this.getName(),
                this.getElementContext().getStart().getLine(),
                this.getType().getName(),
                this.getCompilationUnit().get().getSourceName(),
                this.getType().getElementContext().getStart().getLine());
        compilerErrorHolder.add(
                "ERR_ASO_ORD",
                message,
                this,
                this.getElementContext().classReference());
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

    @Nonnull
    @Override
    public AssociationEndContext getElementContext()
    {
        return (AssociationEndContext) super.getElementContext();
    }

    public void addForeignKeyPropertyMatchingProperty(
            @Nonnull AntlrDataTypeProperty<?> foreignKeyProperty,
            @Nonnull AntlrDataTypeProperty<?> keyProperty)
    {
        this.foreignKeys.put(foreignKeyProperty, keyProperty);
        foreignKeyProperty.setKeyMatchingThisForeignKey(this, keyProperty);
        keyProperty.setForeignKeyMatchingThisKey(this, foreignKeyProperty);
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

    public AntlrAssociationEnd getOpposite()
    {
        return this.opposite;
    }
}
