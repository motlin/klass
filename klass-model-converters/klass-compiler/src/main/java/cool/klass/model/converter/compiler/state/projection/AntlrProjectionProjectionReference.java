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
import cool.klass.model.converter.compiler.state.property.AntlrReferenceProperty;
import cool.klass.model.meta.domain.projection.ProjectionImpl.ProjectionBuilder;
import cool.klass.model.meta.domain.projection.ProjectionProjectionReferenceImpl.ProjectionProjectionReferenceBuilder;
import cool.klass.model.meta.grammar.KlassParser.IdentifierContext;
import cool.klass.model.meta.grammar.KlassParser.ProjectionProjectionReferenceContext;
import org.antlr.v4.runtime.ParserRuleContext;
import org.eclipse.collections.api.list.MutableList;

public class AntlrProjectionProjectionReference
        extends AntlrIdentifierElement
        implements AntlrProjectionChild
{
    @Nonnull
    public static final AntlrProjectionProjectionReference AMBIGUOUS = new AntlrProjectionProjectionReference(
            new ProjectionProjectionReferenceContext(null, -1),
            Optional.empty(),
            new IdentifierContext(null, -1),
            -1,
            AntlrClass.AMBIGUOUS,
            AntlrProjection.AMBIGUOUS,
            AntlrAssociationEnd.AMBIGUOUS,
            AntlrProjection.AMBIGUOUS);

    @Nonnull
    public static final AntlrProjectionProjectionReference NOT_FOUND = new AntlrProjectionProjectionReference(
            new ProjectionProjectionReferenceContext(null, -1),
            Optional.empty(),
            new IdentifierContext(null, -1),
            -1,
            AntlrClass.NOT_FOUND,
            AntlrProjection.NOT_FOUND,
            AntlrAssociationEnd.NOT_FOUND,
            AntlrProjection.AMBIGUOUS);

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
            @Nonnull IdentifierContext nameContext,
            int ordinal,
            @Nonnull AntlrClassifier classifier,
            @Nonnull AntlrProjectionParent antlrProjectionParent,
            @Nonnull AntlrReferenceProperty<?> referenceProperty,
            @Nonnull AntlrProjection referencedProjectionState)
    {
        super(elementContext, compilationUnit, nameContext, ordinal);
        this.classifier                = Objects.requireNonNull(classifier);
        this.antlrProjectionParent     = Objects.requireNonNull(antlrProjectionParent);
        this.referenceProperty         = Objects.requireNonNull(referenceProperty);
        this.referencedProjectionState = Objects.requireNonNull(referencedProjectionState);
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
                this.elementContext,
                this.getMacroElementBuilder(),
                this.getSourceCodeBuilder(),
                this.getNameContext(),
                this.ordinal,
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

    @Override
    public void reportDuplicateMemberName(@Nonnull CompilerErrorState compilerErrorHolder)
    {
        String message = String.format("Duplicate member: '%s'.", this.getName());
        compilerErrorHolder.add("ERR_DUP_PRJ", message, this);
    }

    @Override
    public void reportErrors(@Nonnull CompilerErrorState compilerErrorHolder)
    {
        if (this.antlrProjectionParent.getClassifier() == AntlrClass.NOT_FOUND)
        {
            return;
        }

        if (this.referenceProperty == AntlrAssociationEnd.NOT_FOUND)
        {
            String message = String.format("Not found: '%s'.", this.getName());
            compilerErrorHolder.add("ERR_PAE_NFD", message, this);
        }

        if (this.classifier != this.referencedProjectionState.getClassifier()
                && !this.classifier.isSubClassOf(this.referencedProjectionState.getClassifier()))
        {
            String message = String.format(
                    "Type mismatch: '%s' has type '%s' but '%s' has type '%s'.",
                    this.getName(),
                    this.classifier.getName(),
                    this.referencedProjectionState.getName(),
                    this.referencedProjectionState.getClassifier().getName());
            compilerErrorHolder.add("ERR_PRR_KLS", message, this);
        }
    }

    @Override
    public void reportNameErrors(@Nonnull CompilerErrorState compilerErrorHolder)
    {
        // Intentionally blank. Reference to a named element that gets its name checked.
    }

    @Nonnull
    @Override
    protected Pattern getNamePattern()
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName()
                + ".getNamePattern() not implemented yet");
    }

    @Override
    public void getParserRuleContexts(@Nonnull MutableList<ParserRuleContext> parserRuleContexts)
    {
        parserRuleContexts.add(this.elementContext);
        this.antlrProjectionParent.getParserRuleContexts(parserRuleContexts);
    }
}
