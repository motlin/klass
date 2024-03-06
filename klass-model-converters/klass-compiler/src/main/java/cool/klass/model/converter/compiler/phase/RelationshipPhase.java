package cool.klass.model.converter.compiler.phase;

import javax.annotation.Nonnull;

import cool.klass.model.converter.compiler.CompilerState;
import cool.klass.model.converter.compiler.phase.criteria.CriteriaVisitor;
import cool.klass.model.converter.compiler.state.AntlrAssociation;
import cool.klass.model.converter.compiler.state.AntlrClass;
import cool.klass.model.converter.compiler.state.criteria.AntlrCriteria;
import cool.klass.model.converter.compiler.state.property.AntlrAssociationEnd;
import cool.klass.model.meta.grammar.KlassParser.CriteriaExpressionContext;
import cool.klass.model.meta.grammar.KlassParser.RelationshipContext;
import cool.klass.model.meta.grammar.KlassVisitor;
import org.antlr.v4.runtime.tree.ParseTreeWalker;
import org.eclipse.collections.api.list.MutableList;

public class RelationshipPhase extends AbstractCompilerPhase
{
    public RelationshipPhase(CompilerState compilerState)
    {
        super(compilerState);
    }

    @Override
    public void enterRelationship(@Nonnull RelationshipContext ctx)
    {
        super.enterRelationship(ctx);

        AntlrAssociation associationState = this.compilerState.getCompilerWalkState().getAssociationState();

        KlassVisitor<AntlrCriteria> visitor = new CriteriaVisitor(
                this.compilerState,
                associationState);

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

        AntlrClass typeWithForeignKeys = endWithForeignKeys.getOwningClassifierState();
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
            boolean foreignKeysOnThis = this.compilerState.getCompilerWalkState().getThisReference()
                    == typeWithForeignKeys;
            criteriaState.addForeignKeys(foreignKeysOnThis, endWithForeignKeys);
        }
    }

    protected boolean hasPossibleJoinCriteria(
            @Nonnull CriteriaExpressionContext criteriaExpressionContext,
            AntlrClass targetType)
    {
        PossibleJoinCriteriaListener listener = new PossibleJoinCriteriaListener(
                this.compilerState.getDomainModelState(),
                targetType);
        ParseTreeWalker parseTreeWalker = new ParseTreeWalker();
        parseTreeWalker.walk(listener, criteriaExpressionContext);
        return listener.hasForeignKeys();
    }
}
