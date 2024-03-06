package cool.klass.model.converter.compiler.state.projection;

import java.util.regex.Pattern;

import javax.annotation.Nonnull;

import cool.klass.model.converter.compiler.CompilationUnit;
import cool.klass.model.converter.compiler.error.CompilerErrorState;
import cool.klass.model.converter.compiler.state.AntlrClass;
import cool.klass.model.converter.compiler.state.AntlrTopLevelElement;
import cool.klass.model.meta.domain.projection.AbstractProjectionElement.ProjectionChildBuilder;
import cool.klass.model.meta.domain.projection.ProjectionImpl.ProjectionBuilder;
import cool.klass.model.meta.grammar.KlassParser.ProjectionDeclarationContext;
import org.antlr.v4.runtime.ParserRuleContext;
import org.eclipse.collections.api.bag.ImmutableBag;
import org.eclipse.collections.api.list.ImmutableList;
import org.eclipse.collections.api.list.MutableList;

public class AntlrProjection extends AntlrProjectionParent implements AntlrTopLevelElement
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

    @Override
    protected Pattern getNamePattern()
    {
        return TYPE_NAME_PATTERN;
    }

    public ProjectionBuilder build()
    {
        if (this.projectionBuilder != null)
        {
            throw new IllegalStateException();
        }

        this.projectionBuilder = new ProjectionBuilder(
                this.elementContext,
                this.inferred,
                this.nameContext,
                this.name,
                this.ordinal,
                this.packageName,
                this.klass.getElementBuilder());

        ImmutableList<ProjectionChildBuilder> children = this.children
                .collect(AntlrProjectionChild::build)
                .toImmutable();

        this.projectionBuilder.setChildBuilders(children);
        return this.projectionBuilder;
    }

    @Nonnull
    @Override
    public ProjectionBuilder getElementBuilder()
    {
        return this.projectionBuilder;
    }

    public void reportErrors(@Nonnull CompilerErrorState compilerErrorHolder)
    {
        if (this.klass == AntlrClass.NOT_FOUND)
        {
            compilerErrorHolder.add(
                    String.format("ERR_PRJ_TYP: Cannot find class '%s'", this.getElementContext().classReference().getText()),
                    this,
                    this.getElementContext().classReference());
        }

        ImmutableBag<String> duplicateMemberNames = this.getDuplicateMemberNames();

        for (AntlrProjectionElement projectionMember : this.children)
        {
            if (duplicateMemberNames.contains(projectionMember.getName()))
            {
                projectionMember.reportDuplicateMemberName(compilerErrorHolder);
            }
            // TODO: Move not-found and ambiguous error checking from compiler phase here for consistency
            if (this.klass != AntlrClass.NOT_FOUND)
            {
                projectionMember.reportErrors(compilerErrorHolder);
            }
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

    @Override
    public void getParserRuleContexts(@Nonnull MutableList<ParserRuleContext> parserRuleContexts)
    {
        parserRuleContexts.add(this.elementContext);
        parserRuleContexts.add(this.compilationUnit.getParserContext());
    }
}
