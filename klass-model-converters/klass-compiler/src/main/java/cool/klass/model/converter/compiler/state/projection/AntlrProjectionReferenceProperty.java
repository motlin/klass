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
import cool.klass.model.converter.compiler.state.property.AntlrReferenceProperty;
import cool.klass.model.meta.domain.projection.AbstractProjectionElement.ProjectionChildBuilder;
import cool.klass.model.meta.domain.projection.AbstractProjectionParent;
import cool.klass.model.meta.domain.projection.AbstractProjectionParent.AbstractProjectionParentBuilder;
import cool.klass.model.meta.domain.projection.ProjectionReferencePropertyImpl.ProjectionReferencePropertyBuilder;
import cool.klass.model.meta.grammar.KlassParser.IdentifierContext;
import cool.klass.model.meta.grammar.KlassParser.ProjectionReferencePropertyContext;
import org.antlr.v4.runtime.ParserRuleContext;
import org.eclipse.collections.api.bag.ImmutableBag;
import org.eclipse.collections.api.list.ImmutableList;
import org.eclipse.collections.api.list.MutableList;

public class AntlrProjectionReferenceProperty
        extends AntlrProjectionParent
        implements AntlrProjectionChild
{
    @Nonnull
    public static final AntlrProjectionReferenceProperty AMBIGUOUS = new AntlrProjectionReferenceProperty(
            new ProjectionReferencePropertyContext(null, -1),
            Optional.empty(),
            new IdentifierContext(null, -1),
            -1,
            AntlrClassifier.AMBIGUOUS,
            AntlrProjection.AMBIGUOUS,
            AntlrAssociationEnd.AMBIGUOUS);

    @Nonnull
    public static final AntlrProjectionReferenceProperty NOT_FOUND = new AntlrProjectionReferenceProperty(
            new ProjectionReferencePropertyContext(null, -1),
            Optional.empty(),
            new IdentifierContext(null, -1),
            -1,
            AntlrClassifier.NOT_FOUND,
            AntlrProjection.NOT_FOUND,
            AntlrAssociationEnd.NOT_FOUND);

    @Nonnull
    private final AntlrProjectionParent              antlrProjectionParent;
    @Nonnull
    private final AntlrReferenceProperty<?>          referenceProperty;
    private       ProjectionReferencePropertyBuilder projectionReferencePropertyBuilder;

    public AntlrProjectionReferenceProperty(
            @Nonnull ProjectionReferencePropertyContext elementContext,
            @Nonnull Optional<CompilationUnit> compilationUnit,
            @Nonnull IdentifierContext nameContext,
            int ordinal,
            @Nonnull AntlrClassifier classifier,
            @Nonnull AntlrProjectionParent antlrProjectionParent,
            @Nonnull AntlrReferenceProperty<?> referenceProperty)
    {
        super(elementContext, compilationUnit, nameContext, ordinal, classifier);
        this.antlrProjectionParent = Objects.requireNonNull(antlrProjectionParent);
        this.referenceProperty     = Objects.requireNonNull(referenceProperty);
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
                this.elementContext,
                this.getMacroElementBuilder(),
                this.getSourceCodeBuilder(),
                this.getNameContext(),
                this.ordinal,
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
        ImmutableBag<String> duplicateMemberNames = this.getDuplicateMemberNames();

        for (AntlrProjectionElement projectionMember : this.children)
        {
            if (duplicateMemberNames.contains(projectionMember.getName()))
            {
                projectionMember.reportDuplicateMemberName(compilerErrorHolder);
            }
            projectionMember.reportErrors(compilerErrorHolder);
        }

        if (this.antlrProjectionParent.getClassifier() == AntlrClass.NOT_FOUND)
        {
            return;
        }

        if (this.referenceProperty == AntlrAssociationEnd.NOT_FOUND)
        {
            String message = String.format("Not found: '%s'.", this.getName());
            compilerErrorHolder.add("ERR_PAE_NFD", message, this);
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
