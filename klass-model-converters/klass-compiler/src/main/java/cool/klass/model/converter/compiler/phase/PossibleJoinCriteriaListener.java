package cool.klass.model.converter.compiler.phase;

import java.util.List;

import cool.klass.model.converter.compiler.state.AntlrClass;
import cool.klass.model.converter.compiler.state.AntlrDomainModel;
import cool.klass.model.meta.grammar.KlassBaseListener;
import cool.klass.model.meta.grammar.KlassParser.AssociationEndReferenceContext;
import cool.klass.model.meta.grammar.KlassParser.ClassReferenceContext;
import cool.klass.model.meta.grammar.KlassParser.CriteriaAllContext;
import cool.klass.model.meta.grammar.KlassParser.CriteriaEdgePointContext;
import cool.klass.model.meta.grammar.KlassParser.CriteriaExpressionOrContext;
import cool.klass.model.meta.grammar.KlassParser.CriteriaNativeContext;
import cool.klass.model.meta.grammar.KlassParser.CriteriaOperatorContext;
import cool.klass.model.meta.grammar.KlassParser.ExpressionValueContext;
import cool.klass.model.meta.grammar.KlassParser.LiteralContext;
import cool.klass.model.meta.grammar.KlassParser.TypeMemberReferencePathContext;

public class PossibleJoinCriteriaListener extends KlassBaseListener
{
    private final AntlrDomainModel domainModelState;
    private final AntlrClass       targetType;

    private boolean allEqualityOperators         = true;
    private boolean allOperatorsCrossTypes       = true;
    private boolean allMemberReferencesAreDirect = true;
    private boolean allTypeMembersMatch          = true;
    private boolean allReferencesResolve         = true;

    public PossibleJoinCriteriaListener(
            AntlrDomainModel domainModelState,
            AntlrClass targetType)
    {
        this.domainModelState = domainModelState;
        this.targetType = targetType;
    }

    public boolean hasForeignKeys()
    {
        return this.allEqualityOperators
                && this.allOperatorsCrossTypes
                && this.allMemberReferencesAreDirect
                && this.allTypeMembersMatch
                && this.allReferencesResolve;
    }

    @Override
    public void enterCriteriaEdgePoint(CriteriaEdgePointContext ctx)
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName()
                + ".enterCriteriaEdgePoint() not implemented yet");
    }

    @Override
    public void enterCriteriaNative(CriteriaNativeContext ctx)
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName()
                + ".enterCriteriaNative() not implemented yet");
    }

    @Override
    public void enterCriteriaAll(CriteriaAllContext ctx)
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName()
                + ".enterCriteriaAll() not implemented yet");
    }

    @Override
    public void enterCriteriaOperator(CriteriaOperatorContext ctx)
    {
        super.enterCriteriaOperator(ctx);
        if (ctx.operator().equalityOperator() == null)
        {
            this.allEqualityOperators = false;
        }
        ExpressionValueContext source = ctx.source;
        ExpressionValueContext target = ctx.target;
        boolean noThisReference = source.thisMemberReferencePath() == null
                && target.thisMemberReferencePath() == null;
        boolean noTypeReference = source.typeMemberReferencePath() == null
                && target.typeMemberReferencePath() == null;
        if (noThisReference || noTypeReference)
        {
            this.allOperatorsCrossTypes = false;
        }

        // TODO: check time ranges against time instants
    }

    @Override
    public void enterCriteriaExpressionOr(CriteriaExpressionOrContext ctx)
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName()
                + ".enterCriteriaExpressionOr() not implemented yet");
    }

    @Override
    public void enterTypeMemberReferencePath(TypeMemberReferencePathContext ctx)
    {
        super.enterTypeMemberReferencePath(ctx);
        ClassReferenceContext                classReferenceContext           = ctx.classReference();
        List<AssociationEndReferenceContext> associationEndReferenceContexts = ctx.associationEndReference();

        AntlrClass klass = this.domainModelState.getClassByName(classReferenceContext.getText());

        if (!associationEndReferenceContexts.isEmpty())
        {
            this.allMemberReferencesAreDirect = false;
        }
        else if (klass != this.targetType)
        {
            this.allTypeMembersMatch = false;
        }
        else if (klass == AntlrClass.NOT_FOUND || klass == AntlrClass.AMBIGUOUS)
        {
            this.allReferencesResolve = false;
        }
    }

    @Override
    public void enterLiteral(LiteralContext ctx)
    {
        // TODO: Not sure if this should count. But the example is:
        // this.key == Comment.blueprintKey
        //         && Comment.replyToId == null
    }
}
