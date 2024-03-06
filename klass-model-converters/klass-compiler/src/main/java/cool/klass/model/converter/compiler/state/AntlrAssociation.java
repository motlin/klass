package cool.klass.model.converter.compiler.state;

import java.util.LinkedHashMap;
import java.util.Objects;

import javax.annotation.Nonnull;

import cool.klass.model.converter.compiler.CompilationUnit;
import cool.klass.model.converter.compiler.error.CompilerErrorHolder;
import cool.klass.model.converter.compiler.state.criteria.AntlrCriteria;
import cool.klass.model.converter.compiler.state.property.AntlrAssociationEnd;
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
            null)
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
            String packageName)
    {
        super(elementContext, compilationUnit, inferred, nameContext, name, ordinal, packageName);
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

        AntlrAssociationEnd sourceAntlrAssociationEnd = this.associationEndStates.get(0);
        AntlrAssociationEnd targetAntlrAssociationEnd = this.associationEndStates.get(1);

        AntlrClass sourceType = sourceAntlrAssociationEnd.getType();
        AntlrClass targetType = targetAntlrAssociationEnd.getType();

        if (sourceType == AntlrClass.NOT_FOUND
                || targetType == AntlrClass.NOT_FOUND
                || sourceType == AntlrClass.AMBIGUOUS
                || targetType == AntlrClass.AMBIGUOUS)
        {
            return;
        }

        sourceAntlrAssociationEnd.setOpposite(targetAntlrAssociationEnd);
        targetAntlrAssociationEnd.setOpposite(sourceAntlrAssociationEnd);

        sourceAntlrAssociationEnd.setOwningClassState(targetType);
        targetAntlrAssociationEnd.setOwningClassState(sourceType);

        sourceType.enterAssociationEnd(targetAntlrAssociationEnd);
        targetAntlrAssociationEnd.getType().enterAssociationEnd(sourceAntlrAssociationEnd);
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

    @Override
    public AssociationBuilder getElementBuilder()
    {
        return this.associationBuilder;
    }

    public void reportErrors(@Nonnull CompilerErrorHolder compilerErrorHolder)
    {
        int numAssociationEnds = this.associationEndStates.size();
        if (numAssociationEnds != 2)
        {
            String message = String.format(
                    "ERR_ASO_END: Association '%s' should have 2 ends. Found %d",
                    this.name,
                    numAssociationEnds);
            compilerErrorHolder.add(message, this);
        }

        // TODO: reportErrors: Check that both ends aren't owned
        // TODO: reportErrors: Check that both ends aren't versions

        AntlrAssociationEnd sourceAntlrAssociationEnd = this.associationEndStates.get(0);
        AntlrAssociationEnd targetAntlrAssociationEnd = this.associationEndStates.get(1);

        AntlrClass sourceType = sourceAntlrAssociationEnd.getType();
        AntlrClass targetType = targetAntlrAssociationEnd.getType();

        if (sourceType == AntlrClass.NOT_FOUND || targetType == AntlrClass.NOT_FOUND)
        {
            sourceAntlrAssociationEnd.reportTypeNotFound(compilerErrorHolder);
            targetAntlrAssociationEnd.reportTypeNotFound(compilerErrorHolder);

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
            this.criteriaState.reportErrors(compilerErrorHolder, this.getParserRuleContexts());
        }
    }

    @Nonnull
    @Override
    public AssociationDeclarationContext getElementContext()
    {
        return (AssociationDeclarationContext) this.elementContext;
    }

    @Nonnull
    public AntlrCriteria getCriteria()
    {
        return this.criteriaState;
    }

    public void setCriteria(@Nonnull AntlrCriteria criteria)
    {
        this.criteriaState = Objects.requireNonNull(criteria);
    }
}
