package cool.klass.model.converter.compiler.phase;

import java.util.Optional;

import javax.annotation.Nonnull;

import cool.klass.model.converter.compiler.CompilerState;
import cool.klass.model.converter.compiler.phase.criteria.CriteriaVisitor;
import cool.klass.model.converter.compiler.state.AntlrAssociation;
import cool.klass.model.converter.compiler.state.AntlrClass;
import cool.klass.model.converter.compiler.state.AntlrRelationship;
import cool.klass.model.converter.compiler.state.criteria.AntlrCriteria;
import cool.klass.model.converter.compiler.state.property.AntlrAssociationEnd;
import cool.klass.model.meta.grammar.KlassParser.CriteriaExpressionContext;
import cool.klass.model.meta.grammar.KlassParser.RelationshipContext;
import cool.klass.model.meta.grammar.KlassVisitor;
import org.antlr.v4.runtime.tree.ParseTreeWalker;
import org.eclipse.collections.api.list.MutableList;

public class RelationshipPhase
        extends AbstractCompilerPhase
{
    public RelationshipPhase(@Nonnull CompilerState compilerState)
    {
        super(compilerState);
    }

    @Override
    public void enterRelationship(@Nonnull RelationshipContext ctx)
    {
        super.enterRelationship(ctx);

        AntlrAssociation association = this.compilerState.getCompilerWalk().getAssociation();

        AntlrRelationship relationship = new AntlrRelationship(
                ctx,
                Optional.of(this.compilerState.getCompilerWalk().getCurrentCompilationUnit()),
                association);
        association.setRelationship(relationship);

        KlassVisitor<AntlrCriteria> visitor = new CriteriaVisitor(
                this.compilerState,
                relationship);

        CriteriaExpressionContext criteriaExpressionContext = ctx.criteriaExpression();
        AntlrCriteria             criteria                  = visitor.visit(criteriaExpressionContext);
        relationship.setCriteria(criteria);

        MutableList<AntlrAssociationEnd> associationEnds = association.getAssociationEnds();
        if (associationEnds.size() != 2)
        {
            return;
        }

        if (association.isManyToMany())
        {
            return;
        }

        boolean possibleJoinCriteria = this.hasPossibleJoinCriteria(
                criteriaExpressionContext,
                association.getTargetEnd().getType());

        if (possibleJoinCriteria)
        {
            criteria.addForeignKeys();
        }
    }

    private boolean hasPossibleJoinCriteria(
            @Nonnull CriteriaExpressionContext criteriaExpressionContext,
            @Nonnull AntlrClass targetType)
    {
        PossibleJoinCriteriaListener listener = new PossibleJoinCriteriaListener(
                this.compilerState.getDomainModel(),
                targetType);
        ParseTreeWalker parseTreeWalker = new ParseTreeWalker();
        parseTreeWalker.walk(listener, criteriaExpressionContext);
        return listener.hasForeignKeys();
    }
}
