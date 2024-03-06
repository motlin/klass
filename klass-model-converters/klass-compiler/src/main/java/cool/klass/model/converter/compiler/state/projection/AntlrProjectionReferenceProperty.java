package cool.klass.model.converter.compiler.state.projection;

import java.util.Objects;
import java.util.Optional;
import java.util.regex.Pattern;

import javax.annotation.Nonnull;

import cool.klass.model.converter.compiler.CompilationUnit;
import cool.klass.model.converter.compiler.annotation.CompilerAnnotationState;
import cool.klass.model.converter.compiler.state.AntlrClass;
import cool.klass.model.converter.compiler.state.AntlrClassifier;
import cool.klass.model.converter.compiler.state.property.AntlrAssociationEnd;
import cool.klass.model.converter.compiler.state.property.AntlrAssociationEndSignature;
import cool.klass.model.converter.compiler.state.property.AntlrDataTypeProperty;
import cool.klass.model.converter.compiler.state.property.AntlrEnumerationProperty;
import cool.klass.model.converter.compiler.state.property.AntlrReferenceProperty;
import cool.klass.model.meta.domain.projection.AbstractProjectionElement.ProjectionChildBuilder;
import cool.klass.model.meta.domain.projection.AbstractProjectionParent;
import cool.klass.model.meta.domain.projection.AbstractProjectionParent.AbstractProjectionParentBuilder;
import cool.klass.model.meta.domain.projection.ProjectionReferencePropertyImpl.ProjectionReferencePropertyBuilder;
import cool.klass.model.meta.grammar.KlassParser.IdentifierContext;
import cool.klass.model.meta.grammar.KlassParser.ProjectionReferencePropertyContext;
import org.antlr.v4.runtime.Token;
import org.eclipse.collections.api.list.ImmutableList;
import org.eclipse.collections.api.tuple.Pair;
import org.eclipse.collections.impl.tuple.Tuples;

public class AntlrProjectionReferenceProperty
        extends AntlrProjectionParent
        implements AntlrProjectionChild
{
    @Nonnull
    private final AntlrProjectionParent              antlrProjectionParent;
    @Nonnull
    private final AntlrClassifier                    classifierState;
    @Nonnull
    private final AntlrReferenceProperty<?>          referenceProperty;
    private       ProjectionReferencePropertyBuilder projectionReferencePropertyBuilder;

    public AntlrProjectionReferenceProperty(
            @Nonnull ProjectionReferencePropertyContext elementContext,
            @Nonnull Optional<CompilationUnit> compilationUnit,
            int ordinal,
            @Nonnull IdentifierContext nameContext,
            @Nonnull AntlrClassifier classifier,
            @Nonnull AntlrProjectionParent antlrProjectionParent,
            @Nonnull AntlrClassifier classifierState,
            @Nonnull AntlrReferenceProperty<?> referenceProperty)
    {
        super(elementContext, compilationUnit, ordinal, nameContext, classifier);
        this.antlrProjectionParent = Objects.requireNonNull(antlrProjectionParent);
        this.classifierState       = Objects.requireNonNull(classifierState);
        this.referenceProperty     = Objects.requireNonNull(referenceProperty);
    }

    @Nonnull
    public AntlrReferenceProperty<?> getProperty()
    {
        return this.referenceProperty;
    }

    @Override
    public boolean isContext()
    {
        return true;
    }

    @Override
    public Pair<Token, Token> getContextBefore()
    {
        return Tuples.pair(this.getElementContext().getStart(), this.getElementContext().projectionBody().getStart());
    }

    @Override
    public Pair<Token, Token> getContextAfter()
    {
        return Tuples.pair(
                this.getElementContext().projectionBody().getStop(),
                this.getElementContext().PUNCTUATION_COMMA().getSymbol());
    }

    @Nonnull
    @Override
    public ProjectionReferencePropertyContext getElementContext()
    {
        return (ProjectionReferencePropertyContext) super.getElementContext();
    }

    @Nonnull
    @Override
    public ProjectionReferencePropertyBuilder build()
    {
        if (this.projectionReferencePropertyBuilder != null)
        {
            throw new IllegalStateException();
        }

        this.projectionReferencePropertyBuilder = new ProjectionReferencePropertyBuilder(
                (ProjectionReferencePropertyContext) this.elementContext,
                this.getMacroElementBuilder(),
                this.getSourceCodeBuilder(),
                this.ordinal,
                this.getNameContext(),
                this.antlrProjectionParent.getElementBuilder(),
                this.classifierState.getElementBuilder(),
                this.referenceProperty.getElementBuilder());

        ImmutableList<ProjectionChildBuilder> projectionMemberBuilders = this.children
                .collect(AntlrProjectionChild::build)
                .toImmutable();

        this.projectionReferencePropertyBuilder.setChildBuilders(projectionMemberBuilders);
        return this.projectionReferencePropertyBuilder;
    }

    @Override
    public void build2()
    {
        this.children.forEach(AntlrProjectionElement::build2);
    }

    @Override
    public void visit(@Nonnull AntlrProjectionVisitor visitor)
    {
        visitor.visitReferenceProperty(this);
    }

    @Nonnull
    @Override
    public AbstractProjectionParentBuilder<? extends AbstractProjectionParent> getElementBuilder()
    {
        return Objects.requireNonNull(this.projectionReferencePropertyBuilder);
    }

    @Nonnull
    @Override
    public AntlrProjectionParent getParent()
    {
        return this.antlrProjectionParent;
    }

    //<editor-fold desc="Report Compiler Errors">
    @Override
    public void reportDuplicateMemberName(@Nonnull CompilerAnnotationState compilerAnnotationHolder)
    {
        String message = String.format("Duplicate member: '%s'.", this.getName());
        compilerAnnotationHolder.add("ERR_DUP_PRJ", message, this);
    }

    @Override
    public void reportErrors(@Nonnull CompilerAnnotationState compilerAnnotationHolder)
    {
        super.reportErrors(compilerAnnotationHolder);

        AntlrClassifier parentClassifier = this.antlrProjectionParent.getClassifier();
        if (parentClassifier == AntlrClass.NOT_FOUND
                || parentClassifier == AntlrClass.AMBIGUOUS
                || parentClassifier == AntlrClassifier.AMBIGUOUS
                || parentClassifier == AntlrClassifier.NOT_FOUND)
        {
            return;
        }

        if (this.referenceProperty == AntlrReferenceProperty.NOT_FOUND
                || this.referenceProperty == AntlrAssociationEnd.NOT_FOUND
                || this.referenceProperty == AntlrReferenceProperty.NOT_FOUND)
        {
            AntlrDataTypeProperty<?> dataTypeProperty = parentClassifier.getDataTypePropertyByName(this.getName());

            if (dataTypeProperty == AntlrEnumerationProperty.NOT_FOUND)
            {
                String message = String.format(
                        "Cannot find member '%s.%s'.",
                        parentClassifier.getName(),
                        this.getName());
                compilerAnnotationHolder.add("ERR_PRP_NFD", message, this);
            }
            else
            {
                String message = "Nested projection nodes require a reference property, but found a data type property '%s.%s' with type '%s'.".formatted(
                        parentClassifier.getName(),
                        this.getName(),
                        dataTypeProperty.getTypeName());
                compilerAnnotationHolder.add("ERR_PRP_TYP", message, this);
            }

            return;
        }

        if (this.referenceProperty == AntlrReferenceProperty.AMBIGUOUS
                || this.referenceProperty == AntlrAssociationEnd.AMBIGUOUS
                || this.referenceProperty == AntlrAssociationEndSignature.AMBIGUOUS)
        {
            return;
        }

        this.reportForwardReference(compilerAnnotationHolder);

        for (AntlrProjectionChild child : this.children)
        {
            child.reportErrors(compilerAnnotationHolder);
        }
    }

    private void reportForwardReference(CompilerAnnotationState compilerAnnotationHolder)
    {
        if (!this.isForwardReference(this.referenceProperty))
        {
            return;
        }

        String message = String.format(
                "Projection property '%s' is declared on line %d and has a forward reference to property '%s' which is declared later in the source file '%s' on line %d.",
                this.getName(),
                this.getElementContext().getStart().getLine(),
                this.referenceProperty.getName(),
                this.getCompilationUnit().get().getSourceName(),
                this.referenceProperty.getElementContext().getStart().getLine());
        compilerAnnotationHolder.add(
                "ERR_FWD_REF",
                message,
                this,
                this.getElementContext().identifier());
    }

    @Override
    public void reportNameErrors(@Nonnull CompilerAnnotationState compilerAnnotationHolder)
    {
        // Intentionally blank. Reference to a named element that gets its name checked.
    }
    //</editor-fold>

    @Nonnull
    @Override
    protected Pattern getNamePattern()
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName()
                + ".getNamePattern() not implemented yet");
    }
}
