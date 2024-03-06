package cool.klass.model.converter.compiler.state.projection;

import java.util.Optional;
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
            Optional.empty(),
            new ParserRuleContext(),
            "ambiguous projection",
            -1,
            AntlrClass.AMBIGUOUS,
            null);

    @Nonnull
    public static final AntlrProjection NOT_FOUND = new AntlrProjection(
            new ProjectionDeclarationContext(null, -1),
            Optional.empty(),
            new ParserRuleContext(),
            "not found projection",
            -1,
            AntlrClass.NOT_FOUND,
            null);

    private final String packageName;

    private ProjectionBuilder projectionBuilder;

    public AntlrProjection(
            @Nonnull ProjectionDeclarationContext elementContext,
            @Nonnull Optional<CompilationUnit> compilationUnit,
            @Nonnull ParserRuleContext nameContext,
            @Nonnull String name,
            int ordinal,
            @Nonnull AntlrClass klass,
            String packageName)
    {
        super(elementContext, compilationUnit, nameContext, name, ordinal, klass);
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
                this.getMacroElementBuilder(),
                this.getSourceCodeBuilder(),
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

    public void build2()
    {
        this.children.each(AntlrProjectionElement::build2);
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
                    "ERR_PRJ_TYP",
                    String.format("Cannot find class '%s'", this.getElementContext().classReference().getText()),
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
            if (this.klass != AntlrClass.NOT_FOUND && this.klass != AntlrClass.AMBIGUOUS)
            {
                projectionMember.reportErrors(compilerErrorHolder);
            }
        }
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
        parserRuleContexts.add(this.compilationUnit.get().getParserContext());
    }
}
