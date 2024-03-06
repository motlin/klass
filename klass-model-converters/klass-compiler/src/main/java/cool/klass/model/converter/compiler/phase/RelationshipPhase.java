package cool.klass.model.converter.compiler.phase;

import javax.annotation.Nonnull;

import cool.klass.model.converter.compiler.CompilationUnit;
import cool.klass.model.converter.compiler.error.CompilerErrorHolder;
import cool.klass.model.converter.compiler.phase.criteria.CriteriaVisitor;
import cool.klass.model.converter.compiler.state.AntlrClass;
import cool.klass.model.converter.compiler.state.AntlrDomainModel;
import cool.klass.model.converter.compiler.state.criteria.AntlrCriteria;
import cool.klass.model.converter.compiler.state.property.AntlrAssociationEnd;
import cool.klass.model.meta.grammar.KlassParser.RelationshipContext;
import cool.klass.model.meta.grammar.KlassVisitor;
import org.antlr.v4.runtime.ParserRuleContext;
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
        AntlrClass thisReference = this.associationState.getAssociationEndStates()
                .getFirstOptional()
                .map(AntlrAssociationEnd::getType)
                .orElse(AntlrClass.NOT_FOUND);

        KlassVisitor<AntlrCriteria> visitor = new CriteriaVisitor(
                this.currentCompilationUnit,
                this.domainModelState,
                this.associationState,
                thisReference);
        AntlrCriteria criteriaState = visitor.visit(ctx.criteriaExpression());
        this.associationState.setCriteria(criteriaState);
    }
}
