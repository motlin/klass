package cool.klass.model.converter.compiler.phase;

import java.util.Objects;
import java.util.Optional;

import javax.annotation.Nonnull;

import cool.klass.model.converter.compiler.CompilerState;
import cool.klass.model.converter.compiler.state.AntlrClassifier;
import cool.klass.model.converter.compiler.state.projection.AntlrProjection;
import cool.klass.model.converter.compiler.state.projection.AntlrProjectionDataTypeProperty;
import cool.klass.model.converter.compiler.state.projection.AntlrProjectionParent;
import cool.klass.model.converter.compiler.state.projection.AntlrProjectionProjectionReference;
import cool.klass.model.converter.compiler.state.projection.AntlrProjectionReferenceProperty;
import cool.klass.model.converter.compiler.state.property.AntlrDataTypeProperty;
import cool.klass.model.converter.compiler.state.property.AntlrReferenceProperty;
import cool.klass.model.meta.grammar.KlassParser.ClassifierReferenceContext;
import cool.klass.model.meta.grammar.KlassParser.HeaderContext;
import cool.klass.model.meta.grammar.KlassParser.IdentifierContext;
import cool.klass.model.meta.grammar.KlassParser.ProjectionDeclarationContext;
import cool.klass.model.meta.grammar.KlassParser.ProjectionParameterizedPropertyContext;
import cool.klass.model.meta.grammar.KlassParser.ProjectionPrimitiveMemberContext;
import cool.klass.model.meta.grammar.KlassParser.ProjectionProjectionReferenceContext;
import cool.klass.model.meta.grammar.KlassParser.ProjectionReferenceContext;
import cool.klass.model.meta.grammar.KlassParser.ProjectionReferencePropertyContext;
import org.eclipse.collections.api.stack.MutableStack;
import org.eclipse.collections.impl.factory.Stacks;

public class ProjectionPhase
        extends AbstractCompilerPhase
{
    private final MutableStack<AntlrProjectionParent> elementStack = Stacks.mutable.empty();

    public ProjectionPhase(@Nonnull CompilerState compilerState)
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
    public void exitProjectionDeclaration(@Nonnull ProjectionDeclarationContext ctx)
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
                ? projectionParentState.getClassifier()
                : this.compilerState.getDomainModelState().getClassifierByName(classifierReferenceContext.getText());

        AntlrDataTypeProperty<?> dataTypePropertyState = classifierState.getDataTypePropertyByName(name);

        AntlrProjectionDataTypeProperty projectionPrimitiveMemberState = new AntlrProjectionDataTypeProperty(
                ctx,
                Optional.of(this.compilerState.getCompilerWalkState().getCurrentCompilationUnit()),
                nameContext,
                projectionParentState.getNumChildren() + 1,
                header,
                headerText,
                projectionParentState,
                dataTypePropertyState);

        projectionParentState.enterAntlrProjectionMember(projectionPrimitiveMemberState);
    }

    @Override
    public void enterProjectionReferenceProperty(@Nonnull ProjectionReferencePropertyContext ctx)
    {
        super.enterProjectionReferenceProperty(ctx);

        AntlrProjectionParent projectionParentState = this.elementStack.peek();

        IdentifierContext nameContext = ctx.identifier();
        String            name        = nameContext.getText();

        ClassifierReferenceContext classifierReferenceContext = ctx.classifierReference();
        AntlrClassifier classifierState = classifierReferenceContext == null
                ? projectionParentState.getClassifier()
                : this.compilerState.getDomainModelState().getClassifierByName(classifierReferenceContext.getText());
        AntlrReferenceProperty<?> referenceProperty = classifierState.getReferencePropertyByName(name);

        AntlrProjectionReferenceProperty projectionReferencePropertyState = new AntlrProjectionReferenceProperty(
                ctx,
                Optional.of(this.compilerState.getCompilerWalkState().getCurrentCompilationUnit()),
                nameContext,
                projectionParentState.getNumChildren() + 1,
                referenceProperty.getType(),
                projectionParentState,
                referenceProperty);

        projectionParentState.enterAntlrProjectionMember(projectionReferencePropertyState);

        this.elementStack.push(projectionReferencePropertyState);
    }

    @Override
    public void exitProjectionReferenceProperty(@Nonnull ProjectionReferencePropertyContext ctx)
    {
        this.elementStack.pop();
        super.exitProjectionReferenceProperty(ctx);
    }

    @Override
    public void enterProjectionProjectionReference(@Nonnull ProjectionProjectionReferenceContext ctx)
    {
        super.enterProjectionProjectionReference(ctx);

        AntlrProjectionParent projectionParentState = this.elementStack.peek();

        IdentifierContext          nameContext                = ctx.identifier();
        String                     name                       = nameContext.getText();
        ProjectionReferenceContext projectionReferenceContext = ctx.projectionReference();
        String                     projectionName             = projectionReferenceContext.identifier().getText();

        ClassifierReferenceContext classifierReferenceContext = ctx.classifierReference();
        AntlrClassifier classifierState = classifierReferenceContext == null
                ? projectionParentState.getClassifier()
                : this.compilerState.getDomainModelState().getClassByName(classifierReferenceContext.getText());
        AntlrReferenceProperty<?> referenceProperty = classifierState.getReferencePropertyByName(name);

        AntlrProjection projectionState = this.compilerState.getDomainModelState().getProjectionByName(projectionName);

        AntlrProjectionProjectionReference projectionProjectionReferenceState = new AntlrProjectionProjectionReference(
                ctx,
                Optional.of(this.compilerState.getCompilerWalkState().getCurrentCompilationUnit()),
                nameContext,
                projectionParentState.getNumChildren() + 1,
                referenceProperty.getType(),
                projectionParentState,
                referenceProperty,
                projectionState);

        projectionParentState.enterAntlrProjectionMember(projectionProjectionReferenceState);
    }

    @Override
    public void exitProjectionProjectionReference(@Nonnull ProjectionProjectionReferenceContext ctx)
    {
        super.exitProjectionProjectionReference(ctx);
    }

    @Override
    public void enterProjectionParameterizedProperty(@Nonnull ProjectionParameterizedPropertyContext ctx)
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName()
                + ".enterProjectionParameterizedProperty() not implemented yet");
    }

    @Override
    public void exitProjectionParameterizedProperty(@Nonnull ProjectionParameterizedPropertyContext ctx)
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName()
                + ".exitProjectionParameterizedProperty() not implemented yet");
    }
}
