package cool.klass.model.converter.compiler.state.projection;

import java.util.Objects;
import java.util.Optional;
import java.util.regex.Pattern;

import javax.annotation.Nonnull;

import cool.klass.model.converter.compiler.CompilationUnit;
import cool.klass.model.converter.compiler.error.CompilerErrorState;
import cool.klass.model.converter.compiler.state.AntlrClass;
import cool.klass.model.converter.compiler.state.AntlrElement;
import cool.klass.model.converter.compiler.state.property.AntlrAssociationEnd;
import cool.klass.model.meta.domain.projection.AbstractProjectionElement.ProjectionChildBuilder;
import cool.klass.model.meta.domain.projection.AbstractProjectionParent;
import cool.klass.model.meta.domain.projection.AbstractProjectionParent.AbstractProjectionParentBuilder;
import cool.klass.model.meta.domain.projection.ProjectionAssociationEndImpl.ProjectionAssociationEndBuilder;
import cool.klass.model.meta.grammar.KlassParser.ProjectionAssociationEndContext;
import org.antlr.v4.runtime.ParserRuleContext;
import org.eclipse.collections.api.bag.ImmutableBag;
import org.eclipse.collections.api.list.ImmutableList;
import org.eclipse.collections.api.list.MutableList;

public class AntlrProjectionAssociationEnd extends AntlrProjectionParent implements AntlrProjectionChild
{
    @Nonnull
    public static final AntlrProjectionAssociationEnd AMBIGUOUS = new AntlrProjectionAssociationEnd(
            new ProjectionAssociationEndContext(null, -1),
            null,
            Optional.empty(),
            new ParserRuleContext(),
            "ambiguous projection",
            -1,
            AntlrClass.AMBIGUOUS,
            AntlrProjection.AMBIGUOUS,
            AntlrAssociationEnd.AMBIGUOUS);

    @Nonnull
    public static final AntlrProjectionAssociationEnd NOT_FOUND = new AntlrProjectionAssociationEnd(
            new ProjectionAssociationEndContext(null, -1),
            null,
            Optional.empty(),
            new ParserRuleContext(),
            "not found projection",
            -1,
            AntlrClass.NOT_FOUND,
            AntlrProjection.NOT_FOUND,
            AntlrAssociationEnd.NOT_FOUND);

    @Nonnull
    private final AntlrProjectionParent antlrProjectionParent;

    @Nonnull
    private final AntlrAssociationEnd associationEnd;

    private ProjectionAssociationEndBuilder projectionAssociationEndBuilder;

    public AntlrProjectionAssociationEnd(
            @Nonnull ProjectionAssociationEndContext elementContext,
            CompilationUnit compilationUnit,
            Optional<AntlrElement> macroElement,
            @Nonnull ParserRuleContext nameContext,
            @Nonnull String name,
            int ordinal,
            @Nonnull AntlrClass klass,
            @Nonnull AntlrProjectionParent antlrProjectionParent,
            @Nonnull AntlrAssociationEnd associationEnd)
    {
        super(elementContext, compilationUnit, macroElement, nameContext, name, ordinal, klass);
        this.antlrProjectionParent = Objects.requireNonNull(antlrProjectionParent);
        this.associationEnd = Objects.requireNonNull(associationEnd);
    }

    @Nonnull
    @Override
    public ProjectionAssociationEndBuilder build()
    {
        if (this.projectionAssociationEndBuilder != null)
        {
            throw new IllegalStateException();
        }

        this.projectionAssociationEndBuilder = new ProjectionAssociationEndBuilder(
                this.elementContext,
                this.macroElement.map(AntlrElement::getElementBuilder),
                this.nameContext,
                this.name,
                this.ordinal,
                this.antlrProjectionParent.getElementBuilder(),
                this.associationEnd.getElementBuilder());

        ImmutableList<ProjectionChildBuilder> projectionMemberBuilders = this.children
                .collect(AntlrProjectionChild::build)
                .toImmutable();

        this.projectionAssociationEndBuilder.setChildBuilders(projectionMemberBuilders);
        return this.projectionAssociationEndBuilder;
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
        return Objects.requireNonNull(this.projectionAssociationEndBuilder);
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
        String message = String.format("Duplicate member: '%s'.", this.name);
        compilerErrorHolder.add("ERR_DUP_PRJ", message, this);
    }

    @Override
    public void reportErrors(@Nonnull CompilerErrorState compilerErrorHolder)
    {
        if (this.antlrProjectionParent.getKlass() == AntlrClass.NOT_FOUND)
        {
            return;
        }

        if (this.associationEnd == AntlrAssociationEnd.NOT_FOUND)
        {
            String message = String.format("Not found: '%s'.", this.name);
            compilerErrorHolder.add("ERR_PAE_NFD", message, this);
        }

        ImmutableBag<String> duplicateMemberNames = this.getDuplicateMemberNames();

        for (AntlrProjectionElement projectionMember : this.children)
        {
            if (duplicateMemberNames.contains(projectionMember.getName()))
            {
                projectionMember.reportDuplicateMemberName(compilerErrorHolder);
            }
            projectionMember.reportErrors(compilerErrorHolder);
        }
    }

    public ImmutableBag<String> getDuplicateMemberNames()
    {
        return this.children
                .collect(AntlrProjectionElement::getName)
                .toBag()
                .selectByOccurrences(occurrences -> occurrences > 1)
                .toImmutable();
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
