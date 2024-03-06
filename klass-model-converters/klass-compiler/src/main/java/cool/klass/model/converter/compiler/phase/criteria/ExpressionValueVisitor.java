package cool.klass.model.converter.compiler.phase.criteria;

import cool.klass.model.converter.compiler.CompilationUnit;
import cool.klass.model.converter.compiler.state.AntlrAssociation;
import cool.klass.model.converter.compiler.state.AntlrAssociationEnd;
import cool.klass.model.converter.compiler.state.AntlrClass;
import cool.klass.model.converter.compiler.state.AntlrDataTypeProperty;
import cool.klass.model.converter.compiler.state.AntlrDomainModel;
import cool.klass.model.converter.compiler.state.value.AntlrExpressionValue;
import cool.klass.model.converter.compiler.state.value.AntlrThisMemberValue;
import cool.klass.model.converter.compiler.state.value.AntlrTypeMemberValue;
import cool.klass.model.meta.grammar.KlassBaseVisitor;
import cool.klass.model.meta.grammar.KlassParser.ClassReferenceContext;
import cool.klass.model.meta.grammar.KlassParser.MemberReferenceContext;
import cool.klass.model.meta.grammar.KlassParser.ThisMemberReferenceContext;
import cool.klass.model.meta.grammar.KlassParser.TypeMemberReferenceContext;
import org.antlr.v4.runtime.tree.TerminalNode;

public class ExpressionValueVisitor extends KlassBaseVisitor<AntlrExpressionValue>
{
    private final CompilationUnit  compilationUnit;
    private final AntlrAssociation associationState;
    private final AntlrDomainModel domainModelState;

    public ExpressionValueVisitor(
            CompilationUnit compilationUnit,
            AntlrAssociation associationState,
            AntlrDomainModel domainModelState)
    {
        this.compilationUnit = compilationUnit;
        this.associationState = associationState;
        this.domainModelState = domainModelState;
    }

    @Override
    public AntlrExpressionValue visitTerminal(TerminalNode node)
    {
        throw new AssertionError();
    }

    @Override
    public AntlrExpressionValue visitThisMemberReference(ThisMemberReferenceContext ctx)
    {
        MemberReferenceContext memberReferenceContext = ctx.memberReference();

        String memberName = memberReferenceContext.identifier().getText();

        AntlrClass classState = this.associationState.getAssociationEndStates()
                .getFirstOptional()
                .map(AntlrAssociationEnd::getType)
                .orElse(AntlrClass.NOT_FOUND);
        AntlrDataTypeProperty<?> dataTypePropertyState = classState.getDataTypePropertyByName(memberName);

        return new AntlrThisMemberValue(ctx, this.compilationUnit, false, classState, dataTypePropertyState);
    }

    @Override
    public AntlrTypeMemberValue visitTypeMemberReference(TypeMemberReferenceContext ctx)
    {
        ClassReferenceContext  classReferenceContext  = ctx.classReference();
        MemberReferenceContext memberReferenceContext = ctx.memberReference();

        String className  = classReferenceContext.identifier().getText();
        String memberName = memberReferenceContext.identifier().getText();

        AntlrClass               classState            = this.domainModelState.getClassByName(className);
        AntlrDataTypeProperty<?> dataTypePropertyState = classState.getDataTypePropertyByName(memberName);

        return new AntlrTypeMemberValue(ctx, this.compilationUnit, false, classState, dataTypePropertyState);
    }
}
