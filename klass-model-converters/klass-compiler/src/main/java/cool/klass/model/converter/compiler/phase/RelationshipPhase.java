package cool.klass.model.converter.compiler.phase;

import javax.annotation.Nonnull;

import cool.klass.model.converter.compiler.CompilationUnit;
import cool.klass.model.converter.compiler.error.CompilerErrorHolder;
import cool.klass.model.converter.compiler.phase.criteria.CriteriaVisitor;
import cool.klass.model.converter.compiler.state.AntlrAssociation;
import cool.klass.model.converter.compiler.state.AntlrClass;
import cool.klass.model.converter.compiler.state.AntlrDomainModel;
import cool.klass.model.converter.compiler.state.criteria.AntlrCriteria;
import cool.klass.model.converter.compiler.state.property.AntlrAssociationEnd;
import cool.klass.model.meta.grammar.KlassParser.CriteriaExpressionContext;
import cool.klass.model.meta.grammar.KlassParser.RelationshipContext;
import cool.klass.model.meta.grammar.KlassVisitor;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.tree.ParseTreeWalker;
import org.eclipse.collections.api.list.MutableList;
import org.eclipse.collections.api.map.MutableMap;

public class RelationshipPhase extends AbstractDomainModelCompilerPhase
{
    public RelationshipPhase(
            @Nonnull CompilerErrorHolder compilerErrorHolder,
            @Nonnull MutableMap<ParserRuleContext, CompilationUnit> compilationUnitsByContext,
            boolean isInference,
            AntlrDomainModel domainModelState)
    {
        super(compilerErrorHolder, compilationUnitsByContext, isInference, domainModelState);
    }

    @Override
    public void enterRelationship(@Nonnull RelationshipContext ctx)
    {
        if (this.thisReference != null)
        {
            throw new IllegalStateException();
        }

        AntlrAssociation associationState = this.associationState;
        this.thisReference = associationState.getAssociationEndStates()
                .getFirstOptional()
                .map(AntlrAssociationEnd::getType)
                .orElse(AntlrClass.NOT_FOUND);

        KlassVisitor<AntlrCriteria> visitor = new CriteriaVisitor(
                this.currentCompilationUnit,
                this.domainModelState,
                associationState,
                this.thisReference);

        CriteriaExpressionContext criteriaExpressionContext = ctx.criteriaExpression();
        AntlrCriteria             criteriaState             = visitor.visit(criteriaExpressionContext);
        associationState.setCriteria(criteriaState);

        MutableList<AntlrAssociationEnd> associationEndStates = associationState.getAssociationEndStates();
        if (associationEndStates.size() != 2)
        {
            return;
        }

        AntlrAssociationEnd endWithForeignKeys = associationState.getEndWithForeignKeys();
        if (endWithForeignKeys == null)
        {
            return;
        }

        AntlrClass typeWithForeignKeys = endWithForeignKeys.getOwningClassState();
        if (typeWithForeignKeys == AntlrClass.NOT_FOUND || typeWithForeignKeys == AntlrClass.AMBIGUOUS)
        {
            return;
        }

        if (!endWithForeignKeys.isToOne())
        {
            throw new AssertionError();
        }

        boolean possibleJoinCriteria = this.hasPossibleJoinCriteria(
                criteriaExpressionContext,
                associationState.getTargetEnd().getType());

        if (possibleJoinCriteria)
        {
            boolean foreignKeysOnThis = this.thisReference == typeWithForeignKeys;
            criteriaState.addForeignKeys(foreignKeysOnThis, endWithForeignKeys);
        }
    }

    protected boolean hasPossibleJoinCriteria(
            CriteriaExpressionContext criteriaExpressionContext,
            AntlrClass targetType)
    {
        PossibleJoinCriteriaListener listener = new PossibleJoinCriteriaListener(
                this.domainModelState,
                targetType);
        ParseTreeWalker parseTreeWalker = new ParseTreeWalker();
        parseTreeWalker.walk(listener, criteriaExpressionContext);
        return listener.hasForeignKeys();
    }
}
