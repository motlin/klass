package cool.klass.model.converter.compiler.state;

import java.util.LinkedHashMap;
import java.util.Objects;

import javax.annotation.Nonnull;

import cool.klass.model.converter.compiler.CompilationUnit;
import cool.klass.model.converter.compiler.error.CompilerErrorState;
import cool.klass.model.converter.compiler.state.criteria.AntlrCriteria;
import cool.klass.model.converter.compiler.state.property.AntlrAssociationEnd;
import cool.klass.model.converter.compiler.state.property.AntlrAssociationEndModifier;
import cool.klass.model.meta.domain.AssociationImpl.AssociationBuilder;
import cool.klass.model.meta.domain.criteria.AbstractCriteria.AbstractCriteriaBuilder;
import cool.klass.model.meta.domain.property.AssociationEndImpl.AssociationEndBuilder;
import cool.klass.model.meta.grammar.KlassParser.AssociationDeclarationContext;
import cool.klass.model.meta.grammar.KlassParser.AssociationEndContext;
import org.antlr.v4.runtime.ParserRuleContext;
import org.eclipse.collections.api.list.ImmutableList;
import org.eclipse.collections.api.list.MutableList;
import org.eclipse.collections.api.map.MutableOrderedMap;
import org.eclipse.collections.impl.factory.Lists;
import org.eclipse.collections.impl.map.ordered.mutable.OrderedMapAdapter;

public class AntlrAssociation extends AntlrPackageableElement implements AntlrTopLevelElement
{
    @Nonnull
    public static final AntlrAssociation AMBIGUOUS = new AntlrAssociation(
            new ParserRuleContext(),
            null,
            true,
            new ParserRuleContext(),
            "ambiguous association",
            -1,
            new ParserRuleContext(),
            "klass.meta")
    {
        @Override
        public void enterAssociationEnd(@Nonnull AntlrAssociationEnd associationEndState)
        {
            throw new UnsupportedOperationException(this.getClass().getSimpleName()
                    + ".enterAssociationEnd() not implemented yet");
        }
    };

    private final MutableList<AntlrAssociationEnd>                              associationEndStates     = Lists.mutable.empty();
    private final MutableOrderedMap<AssociationEndContext, AntlrAssociationEnd> associationEndsByContext = OrderedMapAdapter.adapt(
            new LinkedHashMap<>());

    private AntlrCriteria criteriaState;

    private AssociationBuilder associationBuilder;

    public AntlrAssociation(
            @Nonnull ParserRuleContext elementContext,
            CompilationUnit compilationUnit,
            boolean inferred,
            @Nonnull ParserRuleContext nameContext,
            @Nonnull String name,
            int ordinal,
            ParserRuleContext packageContext,
            String packageName)
    {
        super(elementContext, compilationUnit, inferred, nameContext, name, ordinal, packageContext, packageName);
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

        if (sourceType == AntlrClass.NOT_FOUND
                || targetType == AntlrClass.NOT_FOUND
                || sourceType == AntlrClass.AMBIGUOUS
                || targetType == AntlrClass.AMBIGUOUS)
        {
            return;
        }

        targetType.enterAssociationEnd(this.getSourceEnd());
        sourceType.enterAssociationEnd(this.getTargetEnd());
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

        AbstractCriteriaBuilder<?> criteriaBuilder = this.criteriaState.build();

        this.associationBuilder = new AssociationBuilder(
                this.elementContext,
                this.inferred,
                this.nameContext,
                this.name,
                this.ordinal,
                this.packageName,
                criteriaBuilder);

        ImmutableList<AssociationEndBuilder> associationEndBuilders = this.associationEndStates
                .collect(AntlrAssociationEnd::build)
                .toImmutable();

        this.associationBuilder.setAssociationEndBuilders(associationEndBuilders);
        return this.associationBuilder;
    }

    @Nonnull
    @Override
    public AssociationBuilder getElementBuilder()
    {
        return this.associationBuilder;
    }

    public void reportErrors(@Nonnull CompilerErrorState compilerErrorHolder)
    {
        int numAssociationEnds = this.associationEndStates.size();
        if (numAssociationEnds != 2)
        {
            String message = String.format(
                    "ERR_ASO_END: Association '%s' should have 2 ends. Found %d",
                    this.name,
                    numAssociationEnds);
            compilerErrorHolder.add(message, this);
            return;
        }

        if (this.getSourceEnd().isOwned() && this.getTargetEnd().isOwned())
        {
            String message = String.format(
                    "ERR_ASO_OWN: Both associations are owned in association '%s'. At most one end may be owned.",
                    this.name);
            AntlrAssociationEndModifier sourceOwnedModifier = this.getSourceEnd()
                    .getAssociationEndModifiers()
                    .detect(AntlrAssociationEndModifier::isOwned);
            AntlrAssociationEndModifier targetOwnedModifier = this.getTargetEnd()
                    .getAssociationEndModifiers()
                    .detect(AntlrAssociationEndModifier::isOwned);

            compilerErrorHolder.add(
                    message,
                    this,
                    Lists.immutable.with(
                            sourceOwnedModifier.getElementContext(),
                            targetOwnedModifier.getElementContext()));
        }
        else if (this.getSourceEnd().isToMany() && this.getTargetEnd().isToOne() && this.getTargetEnd().isOwned())
        {
            String message = String.format(
                    "ERR_OWN_ONE: Association end '%s.%s' is owned, but is on the to-one end of a many-to-one association.",
                    this.getSourceEnd().getOwningClassifierState().getName(),
                    this.getSourceEnd().getName());
            AntlrAssociationEndModifier ownedModifier = this.getTargetEnd()
                    .getAssociationEndModifiers()
                    .detect(AntlrAssociationEndModifier::isOwned);
            compilerErrorHolder.add(message, ownedModifier);
        }
        else if (this.getSourceEnd().isToOne() && this.getTargetEnd().isToMany() && this.getSourceEnd().isOwned())
        {
            String message = String.format(
                    "ERR_OWN_ONE: Association end '%s.%s' is owned, but is on the to-one end of a one-to-many association.",
                    this.getTargetEnd().getOwningClassifierState().getName(),
                    this.getTargetEnd().getName());
            AntlrAssociationEndModifier ownedModifier = this.getSourceEnd()
                    .getAssociationEndModifiers()
                    .detect(AntlrAssociationEndModifier::isOwned);
            compilerErrorHolder.add(message, ownedModifier);
        }
        else if (this.getSourceEnd().isToOne()
                && this.getTargetEnd().isToOne()
                && this.getSourceEnd().isToOneRequired() == this.getTargetEnd().isToOneRequired()
                && !this.getSourceEnd().isOwned()
                && !this.getTargetEnd().isOwned())
        {
            String message = String.format(
                    "ERR_ASO_SYM: Association '%s' is perfectly symmetrical, so foreign keys cannot be inferred. To break the symmetry, make one end owned, or make one end required and the other end optional.",
                    this.getName());
            compilerErrorHolder.add(
                    message,
                    this,
                    Lists.immutable.with(
                            this.getSourceEnd().getMultiplicity().getElementContext(),
                            this.getTargetEnd().getMultiplicity().getElementContext()));
        }

        if (this.getSourceEnd().getType() == AntlrClass.NOT_FOUND
                || this.getTargetEnd().getType() == AntlrClass.NOT_FOUND)
        {
            this.getSourceEnd().reportTypeNotFound(compilerErrorHolder);
            this.getTargetEnd().reportTypeNotFound(compilerErrorHolder);

            return;
        }

        if (this.criteriaState == null)
        {
            // TODO: Editor error matching this one
            String message = String.format(
                    "ERR_REL_INF: Relationship inference not yet supported. '%s' must declare a relationship.",
                    this.getName());
            compilerErrorHolder.add(message, this);
        }
        else
        {
            this.criteriaState.reportErrors(compilerErrorHolder);
        }
    }

    @Nonnull
    @Override
    public AssociationDeclarationContext getElementContext()
    {
        return (AssociationDeclarationContext) this.elementContext;
    }

    public void setCriteria(@Nonnull AntlrCriteria criteria)
    {
        this.criteriaState = Objects.requireNonNull(criteria);
    }

    public AntlrAssociationEnd getSourceEnd()
    {
        return this.associationEndStates.get(0);
    }

    public AntlrAssociationEnd getTargetEnd()
    {
        return this.associationEndStates.get(1);
    }

    public AntlrAssociationEnd getEndWithForeignKeys()
    {
        boolean sourceHasForeignKeys = this.getSourceEnd().hasForeignKeys();
        boolean targetHasForeignKeys = this.getTargetEnd().hasForeignKeys();

        if (sourceHasForeignKeys && !targetHasForeignKeys)
        {
            return this.getTargetEnd();
        }
        if (targetHasForeignKeys && !sourceHasForeignKeys)
        {
            return this.getSourceEnd();
        }
        if (sourceHasForeignKeys && targetHasForeignKeys)
        {
            // Error covered elsewhere: many-to-1 where many owns one
            return null;
        }
        // Error covered elsewhere: 1-to-1 symmetrical association
        return null;
    }
}
