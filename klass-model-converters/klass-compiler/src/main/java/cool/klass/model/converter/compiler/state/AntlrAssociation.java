package cool.klass.model.converter.compiler.state;

import java.util.LinkedHashMap;
import java.util.Objects;
import java.util.Optional;

import javax.annotation.Nonnull;

import cool.klass.model.converter.compiler.CompilationUnit;
import cool.klass.model.converter.compiler.annotation.CompilerAnnotationState;
import cool.klass.model.converter.compiler.state.property.AntlrAssociationEnd;
import cool.klass.model.converter.compiler.state.property.AntlrModifier;
import cool.klass.model.meta.domain.AssociationImpl.AssociationBuilder;
import cool.klass.model.meta.domain.criteria.AbstractCriteria.AbstractCriteriaBuilder;
import cool.klass.model.meta.domain.property.AssociationEndImpl.AssociationEndBuilder;
import cool.klass.model.meta.grammar.KlassParser.AssociationBodyContext;
import cool.klass.model.meta.grammar.KlassParser.AssociationDeclarationContext;
import cool.klass.model.meta.grammar.KlassParser.AssociationEndContext;
import cool.klass.model.meta.grammar.KlassParser.IdentifierContext;
import org.eclipse.collections.api.list.ImmutableList;
import org.eclipse.collections.api.list.MutableList;
import org.eclipse.collections.api.map.MutableOrderedMap;
import org.eclipse.collections.impl.factory.Lists;
import org.eclipse.collections.impl.map.ordered.mutable.OrderedMapAdapter;

public class AntlrAssociation
        extends AntlrPackageableElement
        implements AntlrTopLevelElement
{
    //<editor-fold desc="AMBIGUOUS">
    public static final AntlrAssociation AMBIGUOUS = new AntlrAssociation(
            new AssociationDeclarationContext(null, -1),
            Optional.empty(),
            -1,
            new IdentifierContext(null, -1),
            AntlrCompilationUnit.AMBIGUOUS)
    {
        @Override
        public void enterAssociationEnd(@Nonnull AntlrAssociationEnd associationEndState)
        {
            throw new UnsupportedOperationException(this.getClass().getSimpleName()
                    + ".enterAssociationEnd() not implemented yet");
        }
    };
    //</editor-fold>

    //<editor-fold desc="NOT_FOUND">
    public static final AntlrAssociation NOT_FOUND = new AntlrAssociation(
            new AssociationDeclarationContext(null, -1),
            Optional.empty(),
            -1,
            new IdentifierContext(null, -1),
            AntlrCompilationUnit.NOT_FOUND)
    {
        @Override
        public void enterAssociationEnd(@Nonnull AntlrAssociationEnd associationEndState)
        {
            throw new UnsupportedOperationException(this.getClass().getSimpleName()
                    + ".enterAssociationEnd() not implemented yet");
        }
    };
    //</editor-fold>

    private final MutableList<AntlrAssociationEnd>                              associationEndStates     =
            Lists.mutable.empty();
    private final MutableOrderedMap<AssociationEndContext, AntlrAssociationEnd> associationEndsByContext =
            OrderedMapAdapter.adapt(new LinkedHashMap<>());

    private AntlrRelationship relationship;

    private AssociationBuilder associationBuilder;

    public AntlrAssociation(
            @Nonnull AssociationDeclarationContext elementContext,
            @Nonnull Optional<CompilationUnit> compilationUnit,
            int ordinal,
            @Nonnull IdentifierContext nameContext,
            @Nonnull AntlrCompilationUnit compilationUnitState)
    {
        super(elementContext, compilationUnit, ordinal, nameContext, compilationUnitState);
    }

    @Nonnull
    @Override
    public AssociationDeclarationContext getElementContext()
    {
        return (AssociationDeclarationContext) this.elementContext;
    }

    @Override
    public AssociationBodyContext getBodyContext()
    {
        return this.getElementContext().associationBody();
    }

    public MutableList<AntlrAssociationEnd> getAssociationEndStates()
    {
        return this.associationEndStates.asUnmodifiable();
    }

    public int getNumAssociationEnds()
    {
        return this.associationEndStates.size();
    }

    public AntlrAssociationEnd getAssociationEndByContext(AssociationEndContext ctx)
    {
        return this.associationEndsByContext.get(ctx);
    }

    public void enterAssociationEnd(@Nonnull AntlrAssociationEnd associationEndState)
    {
        AntlrAssociationEnd duplicate = this.associationEndsByContext.put(
                associationEndState.getElementContext(),
                associationEndState);
        if (duplicate != null)
        {
            throw new AssertionError();
        }

        this.associationEndStates.add(associationEndState);
    }

    public void exitAssociationDeclaration()
    {
        int numAssociationEnds = this.associationEndStates.size();
        if (numAssociationEnds != 2)
        {
            throw new AssertionError(numAssociationEnds);
        }

        AntlrClass sourceType = this.getSourceEnd().getType();
        AntlrClass targetType = this.getTargetEnd().getType();

        this.getSourceEnd().setOpposite(this.getTargetEnd());
        this.getTargetEnd().setOpposite(this.getSourceEnd());

        this.getSourceEnd().setOwningClassState(targetType);
        this.getTargetEnd().setOwningClassState(sourceType);

        if (sourceType != AntlrClass.NOT_FOUND
                && sourceType != AntlrClass.AMBIGUOUS)
        {
            sourceType.enterAssociationEnd(this.getTargetEnd());
        }

        if (targetType != AntlrClass.NOT_FOUND
                && targetType != AntlrClass.AMBIGUOUS)
        {
            targetType.enterAssociationEnd(this.getSourceEnd());
        }
    }

    public AssociationBuilder build()
    {
        if (this.associationBuilder != null)
        {
            throw new IllegalStateException();
        }

        int numAssociationEnds = this.associationEndStates.size();
        if (numAssociationEnds != 2)
        {
            throw new AssertionError(numAssociationEnds);
        }

        this.associationBuilder = new AssociationBuilder(
                (AssociationDeclarationContext) this.elementContext,
                this.getMacroElementBuilder(),
                this.getSourceCodeBuilder(),
                this.ordinal,
                this.getNameContext(),
                this.getPackageName());

        ImmutableList<AssociationEndBuilder> associationEndBuilders = this.associationEndStates
                .collect(AntlrAssociationEnd::build)
                .toImmutable();

        this.associationBuilder.setAssociationEndBuilders(associationEndBuilders);

        AbstractCriteriaBuilder<?> criteriaBuilder = this.relationship.getCriteria().build();
        this.associationBuilder.setCriteriaBuilder(criteriaBuilder);

        return this.associationBuilder;
    }

    @Nonnull
    @Override
    public AssociationBuilder getElementBuilder()
    {
        return this.associationBuilder;
    }

    public void reportErrors(@Nonnull CompilerAnnotationState compilerAnnotationHolder)
    {
        int numAssociationEnds = this.associationEndStates.size();
        if (numAssociationEnds != 2)
        {
            String message = String.format(
                    "Association '%s' should have 2 ends. Found %d",
                    this.getName(),
                    numAssociationEnds);
            compilerAnnotationHolder.add("ERR_ASO_END", message, this);
            return;
        }

        if (this.getSourceEnd().isOwned() && this.getTargetEnd().isOwned())
        {
            String message = String.format(
                    "Both association ends are owned in '%s'. At most one end may be owned.",
                    this.getName());
            AntlrModifier sourceOwnedModifier = this.getSourceEnd().getModifiers().detect(AntlrModifier::isOwned);
            AntlrModifier targetOwnedModifier = this.getTargetEnd().getModifiers().detect(AntlrModifier::isOwned);

            compilerAnnotationHolder.add(
                    "ERR_ASO_OWN",
                    message,
                    this,
                    Lists.immutable
                            .<IAntlrElement>with(this.getTargetEnd())
                            .newWithAll(this.getSourceEnd().getSurroundingElements())
                            .distinct(),
                    Lists.immutable.with(
                            sourceOwnedModifier.getElementContext(),
                            targetOwnedModifier.getElementContext()));
        }
        else if (this.getSourceEnd().isToMany() && this.getTargetEnd().isToOne() && this.getTargetEnd().isOwned())
        {
            String message = String.format(
                    "Association end '%s.%s' is owned, but is on the to-one end of a many-to-one association.",
                    this.getTargetEnd().getOwningClassifierState().getName(),
                    this.getTargetEnd().getName());
            AntlrModifier ownedModifier = this.getTargetEnd().getModifiers().detect(AntlrModifier::isOwned);
            compilerAnnotationHolder.add(
                    "ERR_OWN_ONE",
                    message,
                    ownedModifier,
                    Lists.immutable.with(
                            ownedModifier.getElementContext(),
                            this.getElementContext().associationBody().associationEnd(1).multiplicity()));
        }
        else if (this.getSourceEnd().isToOne() && this.getTargetEnd().isToMany() && this.getSourceEnd().isOwned())
        {
            String message = String.format(
                    "Association end '%s.%s' is owned, but is on the to-one end of a one-to-many association.",
                    this.getSourceEnd().getOwningClassifierState().getName(),
                    this.getSourceEnd().getName());
            AntlrModifier ownedModifier = this.getSourceEnd().getModifiers().detect(AntlrModifier::isOwned);
            compilerAnnotationHolder.add(
                    "ERR_OWN_ONE",
                    message,
                    ownedModifier,
                    Lists.immutable.with(
                            ownedModifier.getElementContext(),
                            this.getElementContext().associationBody().associationEnd(0).multiplicity()));
        }
        else if (this.getSourceEnd().isToOne()
                && this.getTargetEnd().isToOne()
                && this.getSourceEnd().isToOneRequired() == this.getTargetEnd().isToOneRequired()
                && !this.getSourceEnd().isOwned()
                && !this.getTargetEnd().isOwned())
        {
            String message = String.format(
                    "Association '%s' is perfectly symmetrical, so foreign keys cannot be inferred. To break the symmetry, make one end owned, or make one end required and the other end optional.",
                    this.getName());
            compilerAnnotationHolder.add(
                    "ERR_ASO_SYM",
                    message,
                    this,
                    Lists.immutable
                            .<IAntlrElement>with(this.getTargetEnd())
                            .newWithAll(this.getSourceEnd().getSurroundingElements())
                            .distinct(),
                    Lists.immutable.with(
                            this.getSourceEnd().getMultiplicity().getElementContext(),
                            this.getTargetEnd().getMultiplicity().getElementContext()));
        }

        if (this.getSourceEnd().getType() == AntlrClass.NOT_FOUND
                || this.getTargetEnd().getType() == AntlrClass.NOT_FOUND)
        {
            this.getSourceEnd().reportTypeNotFound(compilerAnnotationHolder);
            this.getTargetEnd().reportTypeNotFound(compilerAnnotationHolder);

            return;
        }

        if (this.getSourceEnd().getType() == AntlrClass.AMBIGUOUS
                || this.getTargetEnd().getType() == AntlrClass.AMBIGUOUS)
        {
            return;
        }

        this.relationship.reportErrors(compilerAnnotationHolder);
    }

    public AntlrRelationship getRelationship()
    {
        return Objects.requireNonNull(this.relationship);
    }

    public void setRelationship(@Nonnull AntlrRelationship relationship)
    {
        if (this.relationship != null)
        {
            throw new IllegalStateException();
        }
        this.relationship = Objects.requireNonNull(relationship);
    }

    public AntlrAssociationEnd getSourceEnd()
    {
        return this.associationEndStates.get(0);
    }

    public AntlrAssociationEnd getTargetEnd()
    {
        return this.associationEndStates.get(1);
    }

    public boolean isManyToMany()
    {
        return this.getSourceEnd().isToMany() && this.getTargetEnd().isToMany();
    }
}
