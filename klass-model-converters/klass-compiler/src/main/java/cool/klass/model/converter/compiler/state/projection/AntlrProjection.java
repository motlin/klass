package cool.klass.model.converter.compiler.state.projection;

import java.util.Objects;
import java.util.Optional;
import java.util.regex.Pattern;

import javax.annotation.Nonnull;

import cool.klass.model.converter.compiler.CompilationUnit;
import cool.klass.model.converter.compiler.error.CompilerErrorState;
import cool.klass.model.converter.compiler.state.AntlrClassifier;
import cool.klass.model.converter.compiler.state.AntlrCompilationUnit;
import cool.klass.model.converter.compiler.state.AntlrTopLevelElement;
import cool.klass.model.converter.compiler.state.IAntlrElement;
import cool.klass.model.meta.domain.projection.AbstractProjectionElement.ProjectionChildBuilder;
import cool.klass.model.meta.domain.projection.ProjectionImpl.ProjectionBuilder;
import cool.klass.model.meta.grammar.KlassParser.ClassifierReferenceContext;
import cool.klass.model.meta.grammar.KlassParser.IdentifierContext;
import cool.klass.model.meta.grammar.KlassParser.ProjectionDeclarationContext;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.Token;
import org.eclipse.collections.api.bag.ImmutableBag;
import org.eclipse.collections.api.list.ImmutableList;
import org.eclipse.collections.api.list.MutableList;
import org.eclipse.collections.api.tuple.Pair;
import org.eclipse.collections.impl.tuple.Tuples;

public class AntlrProjection
        extends AntlrProjectionParent
        implements AntlrTopLevelElement
{
    @Nonnull
    public static final AntlrProjection AMBIGUOUS = new AntlrProjection(
            new ProjectionDeclarationContext(null, -1),
            Optional.empty(),
            -1,
            new IdentifierContext(null, -1),
            AntlrCompilationUnit.AMBIGUOUS,
            AntlrClassifier.AMBIGUOUS,
            null);

    @Nonnull
    public static final AntlrProjection NOT_FOUND = new AntlrProjection(
            new ProjectionDeclarationContext(null, -1),
            Optional.empty(),
            -1,
            new IdentifierContext(null, -1),
            AntlrCompilationUnit.NOT_FOUND,
            AntlrClassifier.NOT_FOUND,
            null);

    @Nonnull
    private final AntlrCompilationUnit compilationUnitState;
    private final String packageName;

    private ProjectionBuilder projectionBuilder;

    public AntlrProjection(
            @Nonnull ProjectionDeclarationContext elementContext,
            @Nonnull Optional<CompilationUnit> compilationUnit,
            int ordinal,
            @Nonnull IdentifierContext nameContext,
            @Nonnull AntlrCompilationUnit compilationUnitState,
            @Nonnull AntlrClassifier classifier,
            String packageName)
    {
        super(elementContext, compilationUnit, ordinal, nameContext, classifier);
        this.compilationUnitState = Objects.requireNonNull(compilationUnitState);
        this.packageName          = packageName;
    }

    @Nonnull
    @Override
    public Optional<IAntlrElement> getSurroundingElement()
    {
        return Optional.of(this.compilationUnitState);
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
                (ProjectionDeclarationContext) this.elementContext,
                this.getMacroElementBuilder(),
                this.getSourceCodeBuilder(),
                this.ordinal,
                this.getNameContext(),
                this.packageName,
                this.classifier.getElementBuilder());

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
        if (this.classifier == AntlrClassifier.NOT_FOUND)
        {
            ClassifierReferenceContext offendingContext = this.getElementContext().classifierReference();
            String                     message          = String.format(
                    "Cannot find classifier '%s'",
                    offendingContext.getText());
            compilerErrorHolder.add("ERR_PRJ_TYP", message, this, offendingContext);
        }

        ImmutableBag<String> duplicateMemberNames = this.getDuplicateMemberNames();

        for (AntlrProjectionElement projectionMember : this.children)
        {
            if (duplicateMemberNames.contains(projectionMember.getName()))
            {
                projectionMember.reportDuplicateMemberName(compilerErrorHolder);
            }
            // TODO: Move not-found and ambiguous error checking from compiler phase here for consistency
            if (this.classifier != AntlrClassifier.NOT_FOUND && this.classifier != AntlrClassifier.AMBIGUOUS)
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

    @Override
    public Pair<Token, Token> getContextBefore()
    {
        return Tuples.pair(this.getElementContext().getStart(), this.getElementContext().projectionBody().getStart());
    }
}
