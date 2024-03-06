package cool.klass.model.converter.compiler.state.projection;

import javax.annotation.Nonnull;

import cool.klass.model.converter.compiler.CompilationUnit;
import cool.klass.model.converter.compiler.error.CompilerErrorHolder;
import cool.klass.model.converter.compiler.state.AntlrClass;
import cool.klass.model.meta.domain.projection.Projection.ProjectionBuilder;
import cool.klass.model.meta.domain.projection.ProjectionElement.ProjectionElementBuilder;
import cool.klass.model.meta.grammar.KlassParser.ProjectionDeclarationContext;
import org.antlr.v4.runtime.ParserRuleContext;
import org.eclipse.collections.api.bag.ImmutableBag;
import org.eclipse.collections.api.list.ImmutableList;
import org.eclipse.collections.api.list.MutableList;

public class AntlrProjection extends AntlrProjectionParent
{
    @Nonnull
    public static final AntlrProjection AMBIGUOUS = new AntlrProjection(
            new ProjectionDeclarationContext(null, -1),
            null,
            true,
            new ParserRuleContext(),
            "ambiguous projection",
            -1,
            AntlrClass.AMBIGUOUS,
            null);

    @Nonnull
    public static final AntlrProjection NOT_FOUND = new AntlrProjection(
            new ProjectionDeclarationContext(null, -1),
            null,
            true,
            new ParserRuleContext(),
            "not found projection",
            -1,
            AntlrClass.NOT_FOUND,
            null);

    private final String packageName;

    private ProjectionBuilder projectionBuilder;

    public AntlrProjection(
            @Nonnull ProjectionDeclarationContext elementContext,
            CompilationUnit compilationUnit,
            boolean inferred,
            @Nonnull ParserRuleContext nameContext,
            @Nonnull String name,
            int ordinal,
            @Nonnull AntlrClass klass,
            String packageName)
    {
        super(elementContext, compilationUnit, inferred, nameContext, name, ordinal, klass);
        this.packageName = packageName;
    }

    public ProjectionBuilder build()
    {
        if (this.projectionBuilder != null)
        {
            throw new IllegalStateException();
        }

        this.projectionBuilder = new ProjectionBuilder(
                this.elementContext,
                this.nameContext,
                this.name,
                ordinal, this.packageName,
                this.klass.getKlassBuilder());

        ImmutableList<ProjectionElementBuilder> children = this.children
                .collect(AntlrProjectionElement::build)
                .toImmutable();

        this.projectionBuilder.setChildBuilders(children);
        return this.projectionBuilder;
    }

    public ProjectionBuilder getProjectionBuilder()
    {
        return this.projectionBuilder;
    }

    @Override
    public void reportNameErrors(@Nonnull CompilerErrorHolder compilerErrorHolder)
    {
        // TODO: ⬇ Potentially refine a smaller list of keywords that clash with projections and a separate name pattern

        this.reportKeywordCollision(compilerErrorHolder);

        if (!TYPE_NAME_PATTERN.matcher(this.name).matches())
        {
            String message = String.format(
                    "ERR_PRJ_NME: Name must match pattern %s but was %s",
                    CONSTANT_NAME_PATTERN,
                    this.name);
            compilerErrorHolder.add(
                    message,
                    this.nameContext);
        }
    }

    public void reportErrors(@Nonnull CompilerErrorHolder compilerErrorHolder)
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
    }

    public ImmutableBag<String> getDuplicateMemberNames()
    {
        return this.children
                .collect(AntlrProjectionElement::getName)
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

    public void reportDuplicateTopLevelName(@Nonnull CompilerErrorHolder compilerErrorHolder)
    {
        String message = String.format("ERR_DUP_TOP: Duplicate top level item name: '%s'.", this.name);
        compilerErrorHolder.add(message, this.nameContext);
    }

    @Override
    public void getParserRuleContexts(@Nonnull MutableList<ParserRuleContext> parserRuleContexts)
    {
        parserRuleContexts.add(this.elementContext);
    }
}
