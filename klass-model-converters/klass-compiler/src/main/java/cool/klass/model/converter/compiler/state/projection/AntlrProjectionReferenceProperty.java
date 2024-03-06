package cool.klass.model.converter.compiler.state.projection;

import java.util.Objects;
import java.util.Optional;
import java.util.regex.Pattern;

import javax.annotation.Nonnull;

import cool.klass.model.converter.compiler.CompilationUnit;
import cool.klass.model.converter.compiler.error.CompilerErrorState;
import cool.klass.model.converter.compiler.state.AntlrClass;
import cool.klass.model.converter.compiler.state.AntlrClassifier;
import cool.klass.model.converter.compiler.state.property.AntlrAssociationEnd;
import cool.klass.model.converter.compiler.state.property.AntlrAssociationEndSignature;
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
    private final AntlrReferenceProperty<?>          referenceProperty;
    private       ProjectionReferencePropertyBuilder projectionReferencePropertyBuilder;

    public AntlrProjectionReferenceProperty(
            @Nonnull ProjectionReferencePropertyContext elementContext,
            @Nonnull Optional<CompilationUnit> compilationUnit,
            int ordinal,
            @Nonnull IdentifierContext nameContext,
            @Nonnull AntlrClassifier classifier,
            @Nonnull AntlrProjectionParent antlrProjectionParent,
            @Nonnull AntlrReferenceProperty<?> referenceProperty)
    {
        super(elementContext, compilationUnit, ordinal, nameContext, classifier);
        this.antlrProjectionParent = Objects.requireNonNull(antlrProjectionParent);
        this.referenceProperty     = Objects.requireNonNull(referenceProperty);
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

    @Override
    public void reportDuplicateMemberName(@Nonnull CompilerErrorState compilerErrorHolder)
    {
        String message = String.format("Duplicate member: '%s'.", this.getName());
        compilerErrorHolder.add("ERR_DUP_PRJ", message, this);
    }

    @Override
    public void reportErrors(@Nonnull CompilerErrorState compilerErrorHolder)
    {
        super.reportErrors(compilerErrorHolder);

        if (this.antlrProjectionParent.getClassifier() == AntlrClass.NOT_FOUND
                || this.antlrProjectionParent.getClassifier() == AntlrClass.AMBIGUOUS
                || this.antlrProjectionParent.getClassifier() == AntlrClassifier.AMBIGUOUS
                || this.antlrProjectionParent.getClassifier() == AntlrClassifier.NOT_FOUND)
        {
            return;
        }

        if (this.referenceProperty == AntlrReferenceProperty.NOT_FOUND
                || this.referenceProperty == AntlrAssociationEnd.NOT_FOUND
                || this.referenceProperty == AntlrAssociationEndSignature.NOT_FOUND)
        {
            String message = String.format("Not found: '%s'.", this);
            compilerErrorHolder.add("ERR_PRP_NFD", message, this);
            return;
        }

        if (this.referenceProperty == AntlrReferenceProperty.AMBIGUOUS
                || this.referenceProperty == AntlrAssociationEnd.AMBIGUOUS
                || this.referenceProperty == AntlrAssociationEndSignature.AMBIGUOUS)
        {
            return;
        }

        for (AntlrProjectionChild child : this.children)
        {
            child.reportErrors(compilerErrorHolder);
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
}
