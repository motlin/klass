package cool.klass.model.converter.compiler.state.projection;

import java.util.Objects;
import java.util.Optional;
import java.util.regex.Pattern;

import javax.annotation.Nonnull;

import cool.klass.model.converter.compiler.CompilationUnit;
import cool.klass.model.converter.compiler.error.CompilerErrorState;
import cool.klass.model.converter.compiler.state.AntlrClass;
import cool.klass.model.converter.compiler.state.AntlrClassifier;
import cool.klass.model.converter.compiler.state.AntlrIdentifierElement;
import cool.klass.model.converter.compiler.state.property.AntlrAssociationEnd;
import cool.klass.model.converter.compiler.state.property.AntlrDataTypeProperty;
import cool.klass.model.converter.compiler.state.property.AntlrEnumerationProperty;
import cool.klass.model.converter.compiler.state.property.AntlrReferenceProperty;
import cool.klass.model.meta.domain.projection.ProjectionImpl.ProjectionBuilder;
import cool.klass.model.meta.domain.projection.ProjectionProjectionReferenceImpl.ProjectionProjectionReferenceBuilder;
import cool.klass.model.meta.grammar.KlassParser.IdentifierContext;
import cool.klass.model.meta.grammar.KlassParser.ProjectionProjectionReferenceContext;
import org.antlr.v4.runtime.Token;
import org.eclipse.collections.api.tuple.Pair;

public class AntlrProjectionProjectionReference
        extends AntlrIdentifierElement
        implements AntlrProjectionChild
{
    @Nonnull
    private final AntlrClassifier           classifier;
    @Nonnull
    private final AntlrProjectionParent     antlrProjectionParent;
    @Nonnull
    private final AntlrReferenceProperty<?> referenceProperty;
    @Nonnull
    private final AntlrProjection           referencedProjectionState;

    private ProjectionProjectionReferenceBuilder projectionProjectionReferenceBuilder;

    public AntlrProjectionProjectionReference(
            @Nonnull ProjectionProjectionReferenceContext elementContext,
            @Nonnull Optional<CompilationUnit> compilationUnit,
            int ordinal,
            @Nonnull IdentifierContext nameContext,
            @Nonnull AntlrClassifier classifier,
            @Nonnull AntlrProjectionParent antlrProjectionParent,
            @Nonnull AntlrReferenceProperty<?> referenceProperty,
            @Nonnull AntlrProjection referencedProjectionState)
    {
        super(elementContext, compilationUnit, ordinal, nameContext);
        this.classifier                = Objects.requireNonNull(classifier);
        this.antlrProjectionParent     = Objects.requireNonNull(antlrProjectionParent);
        this.referenceProperty         = Objects.requireNonNull(referenceProperty);
        this.referencedProjectionState = Objects.requireNonNull(referencedProjectionState);
    }

    @Nonnull
    @Override
    public ProjectionProjectionReferenceContext getElementContext()
    {
        return (ProjectionProjectionReferenceContext) super.getElementContext();
    }

    @Override
    public boolean isContext()
    {
        return true;
    }

    @Override
    public Pair<Token, Token> getContextBefore()
    {
        return this.getEntireContext();
    }

    @Nonnull
    @Override
    public ProjectionProjectionReferenceBuilder build()
    {
        if (this.projectionProjectionReferenceBuilder != null)
        {
            throw new IllegalStateException();
        }

        this.projectionProjectionReferenceBuilder = new ProjectionProjectionReferenceBuilder(
                (ProjectionProjectionReferenceContext) this.elementContext,
                this.getMacroElementBuilder(),
                this.getSourceCodeBuilder(),
                this.ordinal,
                this.getNameContext(),
                this.antlrProjectionParent.getElementBuilder(),
                this.referenceProperty.getElementBuilder());

        return this.projectionProjectionReferenceBuilder;
    }

    @Override
    public void build2()
    {
        ProjectionBuilder referencedProjectionBuilder = this.referencedProjectionState.getElementBuilder();
        this.projectionProjectionReferenceBuilder.setReferencedProjectionBuilder(referencedProjectionBuilder);
    }

    @Nonnull
    @Override
    public AntlrProjectionParent getParent()
    {
        return this.antlrProjectionParent;
    }

    //<editor-fold desc="Report Compiler Errors">
    @Override
    public void reportDuplicateMemberName(@Nonnull CompilerErrorState compilerErrorHolder)
    {
        String message = String.format("Duplicate member: '%s'.", this.getName());
        compilerErrorHolder.add("ERR_DUP_PRJ", message, this);
    }

    @Override
    public void reportErrors(@Nonnull CompilerErrorState compilerErrorHolder)
    {
        AntlrClassifier parentClassifier = this.antlrProjectionParent.getClassifier();
        if (parentClassifier == AntlrClass.NOT_FOUND
                || parentClassifier == AntlrClass.AMBIGUOUS
                || parentClassifier == AntlrClassifier.AMBIGUOUS
                || parentClassifier == AntlrClassifier.NOT_FOUND)
        {
            return;
        }

        if (this.referencedProjectionState == AntlrProjection.AMBIGUOUS)
        {
            return;
        }

        if (this.referenceProperty == AntlrReferenceProperty.NOT_FOUND
                || this.referenceProperty == AntlrAssociationEnd.NOT_FOUND)
        {
            AntlrDataTypeProperty<?> dataTypeProperty = parentClassifier.getDataTypePropertyByName(this.getName());
            if (dataTypeProperty == AntlrEnumerationProperty.NOT_FOUND)
            {
                String message = String.format(
                        "Cannot find member '%s.%s'.",
                        parentClassifier.getName(),
                        this.getName());
                compilerErrorHolder.add("ERR_PPR_NFD", message, this);
            }
            else
            {
                String message = "Projection reference '%s' requires a reference property with type '%s', but found a data type property '%s.%s' with type '%s'.".formatted(
                        this.referencedProjectionState.getName(),
                        this.referencedProjectionState.getClassifier().getName(),
                        parentClassifier.getName(),
                        this.getName(),
                        dataTypeProperty.getTypeName());

                compilerErrorHolder.add("ERR_PPR_TYP", message, this);
            }
        }
        else if (this.referenceProperty == AntlrReferenceProperty.AMBIGUOUS
                || this.referenceProperty == AntlrAssociationEnd.AMBIGUOUS)
        {
            String message = String.format("Ambiguous: '%s'.", this);
            compilerErrorHolder.add("ERR_PPR_AMB", message, this);
        }
        else if (this.referencedProjectionState == AntlrProjection.NOT_FOUND)
        {
            String message = String.format(
                    "Not found: '%s'.",
                    this.getElementContext().projectionReference().getText());
            compilerErrorHolder.add("ERR_PPR_NFD", message, this, this.getElementContext().projectionReference());
        }
        else
        {
            this.reportTypeMismatch(compilerErrorHolder);
            this.reportForwardReference(compilerErrorHolder);
        }
    }

    private void reportTypeMismatch(@Nonnull CompilerErrorState compilerErrorHolder)
    {
        if (this.classifier == this.referencedProjectionState.getClassifier()
                || this.classifier.isSubClassOf(this.referencedProjectionState.getClassifier()))
        {
            return;
        }

        String message = String.format(
                "Type mismatch: '%s' has type '%s' but '%s' has type '%s'.",
                this.getName(),
                this.classifier.getName(),
                this.referencedProjectionState.getName(),
                this.referencedProjectionState.getClassifier().getName());
        compilerErrorHolder.add("ERR_PRR_KLS", message, this);
    }

    private void reportForwardReference(CompilerErrorState compilerErrorHolder)
    {
        if (!this.referenceProperty.isToOneRequired()
                || !this.isForwardReference(this.referencedProjectionState))
        {
            return;
        }

        if (this.referenceProperty instanceof AntlrAssociationEnd associationEnd
                && associationEnd.isOwned())
        {
            return;
        }

        String message = String.format(
                "Projection property '%s' is declared on line %d and has a forward reference to projection '%s' which is declared later in the source file '%s' on line %d.",
                this.getName(),
                this.getElementContext().getStart().getLine(),
                this.referencedProjectionState.getName(),
                this.getCompilationUnit().get().getSourceName(),
                this.referencedProjectionState.getElementContext().getStart().getLine());
        compilerErrorHolder.add(
                "ERR_FWD_REF",
                message,
                this,
                this.getElementContext().projectionReference());
    }

    @Override
    public void reportNameErrors(@Nonnull CompilerErrorState compilerErrorHolder)
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
