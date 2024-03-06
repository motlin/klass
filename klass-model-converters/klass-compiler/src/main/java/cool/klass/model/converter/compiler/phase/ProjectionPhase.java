package cool.klass.model.converter.compiler.phase;

import java.util.Objects;

import javax.annotation.Nonnull;

import cool.klass.model.converter.compiler.CompilerState;
import cool.klass.model.converter.compiler.state.AntlrClass;
import cool.klass.model.converter.compiler.state.AntlrClassifier;
import cool.klass.model.converter.compiler.state.projection.AntlrProjection;
import cool.klass.model.converter.compiler.state.projection.AntlrProjectionAssociationEnd;
import cool.klass.model.converter.compiler.state.projection.AntlrProjectionDataTypeProperty;
import cool.klass.model.converter.compiler.state.projection.AntlrProjectionParent;
import cool.klass.model.converter.compiler.state.projection.AntlrProjectionProjectionReference;
import cool.klass.model.converter.compiler.state.property.AntlrAssociationEnd;
import cool.klass.model.converter.compiler.state.property.AntlrDataTypeProperty;
import cool.klass.model.meta.grammar.KlassParser.ClassifierReferenceContext;
import cool.klass.model.meta.grammar.KlassParser.HeaderContext;
import cool.klass.model.meta.grammar.KlassParser.IdentifierContext;
import cool.klass.model.meta.grammar.KlassParser.ProjectionAssociationEndContext;
import cool.klass.model.meta.grammar.KlassParser.ProjectionDeclarationContext;
import cool.klass.model.meta.grammar.KlassParser.ProjectionParameterizedPropertyContext;
import cool.klass.model.meta.grammar.KlassParser.ProjectionPrimitiveMemberContext;
import cool.klass.model.meta.grammar.KlassParser.ProjectionProjectionReferenceContext;
import cool.klass.model.meta.grammar.KlassParser.ProjectionReferenceContext;
import org.eclipse.collections.api.stack.MutableStack;
import org.eclipse.collections.impl.factory.Stacks;

public class ProjectionPhase extends AbstractCompilerPhase
{
    private final MutableStack<AntlrProjectionParent> elementStack = Stacks.mutable.empty();

    public ProjectionPhase(CompilerState compilerState)
    {
        super(compilerState);
    }

    @Override
    public void enterProjectionDeclaration(@Nonnull ProjectionDeclarationContext ctx)
    {
        super.enterProjectionDeclaration(ctx);

        AntlrProjection projectionState = this.compilerState.getCompilerWalkState().getProjectionState();
        Objects.requireNonNull(projectionState);
        this.elementStack.push(projectionState);
    }

    @Override
    public void exitProjectionDeclaration(ProjectionDeclarationContext ctx)
    {
        this.elementStack.pop();
        super.exitProjectionDeclaration(ctx);
    }

    @Override
    public void enterProjectionPrimitiveMember(@Nonnull ProjectionPrimitiveMemberContext ctx)
    {
        super.enterProjectionPrimitiveMember(ctx);

        AntlrProjectionParent projectionParentState = this.elementStack.peek();
        Objects.requireNonNull(projectionParentState);

        IdentifierContext nameContext      = ctx.identifier();
        String            name             = nameContext.getText();
        HeaderContext     header           = ctx.header();
        String            headerQuotedText = header.StringLiteral().getText();
        // TODO: ❓ Unescaping header text?
        String headerText = headerQuotedText.substring(1, headerQuotedText.length() - 1);

        ClassifierReferenceContext classifierReferenceContext = ctx.classifierReference();
        AntlrClassifier classifierState = classifierReferenceContext == null
                ? projectionParentState.getKlass()
                : this.compilerState.getDomainModelState().getClassifierByName(classifierReferenceContext.getText());
        AntlrDataTypeProperty<?> dataTypePropertyState = classifierState.getDataTypePropertyByName(name);

        AntlrProjectionDataTypeProperty projectionPrimitiveMemberState = new AntlrProjectionDataTypeProperty(
                ctx,
                this.compilerState.getCompilerWalkState().getCurrentCompilationUnit(),
                this.compilerState.getCompilerInputState().isInference(),
                nameContext,
                name,
                projectionParentState.getNumChildren() + 1,
                header,
                headerText,
                projectionParentState,
                dataTypePropertyState);

        projectionParentState.enterAntlrProjectionMember(projectionPrimitiveMemberState);
    }

    @Override
    public void enterProjectionAssociationEnd(@Nonnull ProjectionAssociationEndContext ctx)
    {
        super.enterProjectionAssociationEnd(ctx);

        AntlrProjectionParent projectionParentState = this.elementStack.peek();

        IdentifierContext nameContext = ctx.identifier();
        String            name        = nameContext.getText();

        ClassifierReferenceContext classifierReferenceContext = ctx.classifierReference();
        AntlrClass classState = classifierReferenceContext == null
                ? projectionParentState.getKlass()
                : this.compilerState.getDomainModelState().getClassByName(classifierReferenceContext.getText());
        AntlrAssociationEnd associationEndState = classState.getAssociationEndByName(name);

        AntlrProjectionAssociationEnd projectionAssociationEndState = new AntlrProjectionAssociationEnd(
                ctx,
                this.compilerState.getCompilerWalkState().getCurrentCompilationUnit(),
                this.compilerState.getCompilerInputState().isInference(),
                nameContext,
                name,
                projectionParentState.getNumChildren() + 1,
                associationEndState.getType(),
                projectionParentState,
                associationEndState);

        projectionParentState.enterAntlrProjectionMember(projectionAssociationEndState);

        this.elementStack.push(projectionAssociationEndState);
    }

    @Override
    public void exitProjectionAssociationEnd(ProjectionAssociationEndContext ctx)
    {
        this.elementStack.pop();
        super.exitProjectionAssociationEnd(ctx);
    }

    @Override
    public void enterProjectionProjectionReference(ProjectionProjectionReferenceContext ctx)
    {
        super.enterProjectionProjectionReference(ctx);

        AntlrProjectionParent projectionParentState = this.elementStack.peek();

        IdentifierContext          nameContext                = ctx.identifier();
        String                     name                       = nameContext.getText();
        ProjectionReferenceContext projectionReferenceContext = ctx.projectionReference();
        String                     projectionName             = projectionReferenceContext.identifier().getText();

        ClassifierReferenceContext classifierReferenceContext = ctx.classifierReference();
        AntlrClass classState = classifierReferenceContext == null
                ? projectionParentState.getKlass()
                : this.compilerState.getDomainModelState().getClassByName(classifierReferenceContext.getText());
        AntlrAssociationEnd associationEndState = classState.getAssociationEndByName(name);

        AntlrProjection projectionState = this.compilerState.getDomainModelState().getProjectionByName(projectionName);

        AntlrProjectionProjectionReference projectionProjectionReferenceState = new AntlrProjectionProjectionReference(
                ctx,
                this.compilerState.getCompilerWalkState().getCurrentCompilationUnit(),
                this.compilerState.getCompilerInputState().isInference(),
                nameContext,
                name,
                projectionParentState.getNumChildren() + 1,
                associationEndState.getType(),
                projectionParentState,
                associationEndState,
                projectionState);

        projectionParentState.enterAntlrProjectionMember(projectionProjectionReferenceState);
    }

    @Override
    public void exitProjectionProjectionReference(ProjectionProjectionReferenceContext ctx)
    {
        super.exitProjectionProjectionReference(ctx);
    }

    @Override
    public void enterProjectionParameterizedProperty(ProjectionParameterizedPropertyContext ctx)
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName()
                + ".enterProjectionParameterizedProperty() not implemented yet");
    }

    @Override
    public void exitProjectionParameterizedProperty(ProjectionParameterizedPropertyContext ctx)
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName()
                + ".exitProjectionParameterizedProperty() not implemented yet");
    }
}
