package cool.klass.model.converter.compiler.state.projection;

import java.util.Objects;

import javax.annotation.Nonnull;

import cool.klass.model.converter.compiler.CompilationUnit;
import cool.klass.model.converter.compiler.error.CompilerErrorHolder;
import cool.klass.model.converter.compiler.state.AntlrClass;
import cool.klass.model.converter.compiler.state.property.AntlrAssociationEnd;
import cool.klass.model.meta.domain.projection.ProjectionAssociationEnd.ProjectionAssociationEndBuilder;
import cool.klass.model.meta.domain.projection.ProjectionMember.ProjectionMemberBuilder;
import cool.klass.model.meta.grammar.KlassParser.ProjectionAssociationEndContext;
import cool.klass.model.meta.grammar.KlassParser.ProjectionDeclarationContext;
import org.antlr.v4.runtime.ParserRuleContext;
import org.eclipse.collections.api.bag.ImmutableBag;
import org.eclipse.collections.api.list.ImmutableList;
import org.eclipse.collections.api.list.MutableList;

public class AntlrProjectionAssociationEnd extends AntlrProjectionParent implements AntlrProjectionMember
{
    @Nonnull
    public static final AntlrProjectionAssociationEnd AMBIGUOUS = new AntlrProjectionAssociationEnd(
            new ProjectionAssociationEndContext(null, -1),
            null,
            true,
            new ParserRuleContext(),
            "ambiguous projection",
            AntlrClass.AMBIGUOUS,
            AntlrProjection.AMBIGUOUS,
            AntlrAssociationEnd.AMBIGUOUS);

    @Nonnull
    public static final AntlrProjectionAssociationEnd NOT_FOUND = new AntlrProjectionAssociationEnd(
            new ProjectionAssociationEndContext(null, -1),
            null,
            true,
            new ParserRuleContext(),
            "not found projection",
            AntlrClass.NOT_FOUND,
            AntlrProjection.AMBIGUOUS,
            AntlrAssociationEnd.AMBIGUOUS);

    @Nonnull
    private final AntlrProjectionParent antlrProjectionParent;

    @Nonnull
    private final AntlrAssociationEnd associationEnd;

    private ProjectionAssociationEndBuilder projectionAssociationEndBuilder;

    public AntlrProjectionAssociationEnd(
            @Nonnull ProjectionAssociationEndContext elementContext,
            CompilationUnit compilationUnit,
            boolean inferred,
            @Nonnull ParserRuleContext nameContext,
            @Nonnull String name,
            @Nonnull AntlrClass klass,
            @Nonnull AntlrProjectionParent antlrProjectionParent,
            @Nonnull AntlrAssociationEnd associationEnd)
    {
        super(elementContext, compilationUnit, inferred, nameContext, name, klass);
        this.antlrProjectionParent = Objects.requireNonNull(antlrProjectionParent);
        this.associationEnd = Objects.requireNonNull(associationEnd);
    }

    @Override
    public void reportDuplicateMemberName(@Nonnull CompilerErrorHolder compilerErrorHolder)
    {
        String message = String.format("ERR_DUP_PRJ: Duplicate member: '%s'.", this.name);

        compilerErrorHolder.add(
                this.compilationUnit,
                message,
                this.nameContext,
                this.getParserRuleContexts().toArray(new ParserRuleContext[]{}));
    }

    @Override
    public ProjectionAssociationEndBuilder build()
    {
        if (this.projectionAssociationEndBuilder != null)
        {
            throw new IllegalStateException();
        }

        this.projectionAssociationEndBuilder = new ProjectionAssociationEndBuilder(
                this.elementContext,
                this.nameContext,
                this.name,
                this.associationEnd.getAssociationEndBuilder());

        ImmutableList<ProjectionMemberBuilder> projectionMemberBuilders = this.projectionMembers
                .collect(AntlrProjectionMember::build)
                .toImmutable();

        this.projectionAssociationEndBuilder.setProjectionMemberBuilders(projectionMemberBuilders);
        return this.projectionAssociationEndBuilder;
    }

    @Nonnull
    @Override
    public AntlrProjectionParent getParent()
    {
        return this.antlrProjectionParent;
    }

    @Nonnull
    public ProjectionAssociationEndBuilder getProjectionAssociationEndBuilder()
    {
        return Objects.requireNonNull(this.projectionAssociationEndBuilder);
    }

    @Override
    public void reportErrors(@Nonnull CompilerErrorHolder compilerErrorHolder)
    {
        ImmutableBag<String> duplicateMemberNames = this.getDuplicateMemberNames();

        for (AntlrProjectionMember projectionMember : this.projectionMembers)
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
        return this.projectionMembers
                .collect(AntlrProjectionMember::getName)
                .toBag()
                .selectByOccurrences(occurrences -> occurrences > 1)
                .toImmutable();
    }

    @Nonnull
    @Override
    public ProjectionDeclarationContext getElementContext()
    {
        return (ProjectionDeclarationContext) super.getElementContext();
    }

    @Override
    public void getParserRuleContexts(@Nonnull MutableList<ParserRuleContext> parserRuleContexts)
    {
        parserRuleContexts.add(this.elementContext);
        this.getParent().getParserRuleContexts(parserRuleContexts);
    }
}
