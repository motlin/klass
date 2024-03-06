package cool.klass.model.converter.compiler.state.projection;

import java.util.Objects;
import java.util.Optional;
import java.util.regex.Pattern;

import javax.annotation.Nonnull;

import cool.klass.model.converter.compiler.CompilationUnit;
import cool.klass.model.converter.compiler.error.CompilerErrorState;
import cool.klass.model.converter.compiler.state.AntlrClass;
import cool.klass.model.converter.compiler.state.AntlrClassifier;
import cool.klass.model.converter.compiler.state.AntlrCompilationUnit;
import cool.klass.model.converter.compiler.state.AntlrInterface;
import cool.klass.model.converter.compiler.state.AntlrTopLevelElement;
import cool.klass.model.converter.compiler.state.IAntlrElement;
import cool.klass.model.meta.domain.projection.AbstractProjectionElement.ProjectionChildBuilder;
import cool.klass.model.meta.domain.projection.ProjectionImpl.ProjectionBuilder;
import cool.klass.model.meta.grammar.KlassParser.IdentifierContext;
import cool.klass.model.meta.grammar.KlassParser.ProjectionDeclarationContext;
import org.antlr.v4.runtime.Token;
import org.eclipse.collections.api.list.ImmutableList;
import org.eclipse.collections.api.tuple.Pair;
import org.eclipse.collections.impl.tuple.Tuples;

public class AntlrProjection
        extends AntlrProjectionParent
        implements AntlrTopLevelElement
{
    //<editor-fold desc="AMBIGUOUS">
    public static final AntlrProjection AMBIGUOUS = new AntlrProjection(
            new ProjectionDeclarationContext(null, -1),
            Optional.empty(),
            -1,
            new IdentifierContext(null, -1),
            AntlrCompilationUnit.AMBIGUOUS,
            AntlrClassifier.AMBIGUOUS,
            null)
    {
        @Override
        public String toString()
        {
            return AntlrProjection.class + ".AMBIGUOUS";
        }
    };
    //</editor-fold>

    //<editor-fold desc="NOT_FOUND">
    public static final AntlrProjection NOT_FOUND = new AntlrProjection(
            new ProjectionDeclarationContext(null, -1),
            Optional.empty(),
            -1,
            new IdentifierContext(null, -1),
            AntlrCompilationUnit.NOT_FOUND,
            AntlrClassifier.NOT_FOUND,
            null)
    {
        @Override
        public String toString()
        {
            return AntlrProjection.class + ".NOT_FOUND";
        }
    };
    //</editor-fold>

    @Nonnull
    private final AntlrCompilationUnit compilationUnitState;
    private final String               packageName;

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

    @Override
    public void reportErrors(@Nonnull CompilerErrorState compilerErrorHolder)
    {
        super.reportErrors(compilerErrorHolder);

        // TODO: Move not-found and ambiguous error checking from compiler phase here for consistency
        if (this.classifier == AntlrClassifier.NOT_FOUND
                || this.classifier == AntlrClass.NOT_FOUND
                || this.classifier == AntlrInterface.NOT_FOUND)
        {
            String message = "Projection type not found " + this.getElementContext().classifierReference().getText();
            compilerErrorHolder.add("ERR_PRJ_NFD", message, this, this.getElementContext().classifierReference());
            return;
        }

        if (this.classifier == AntlrClassifier.AMBIGUOUS
                || this.classifier == AntlrClass.AMBIGUOUS
                || this.classifier == AntlrInterface.AMBIGUOUS)
        {
            return;
        }

        this.reportForwardReference(compilerErrorHolder);

        for (AntlrProjectionChild child : this.children)
        {
            child.reportErrors(compilerErrorHolder);
        }
    }

    protected void reportForwardReference(CompilerErrorState compilerErrorHolder)
    {
        if (this.isForwardReference(this.classifier))
        {
            String message = String.format(
                    "Projection '%s' is declared on line %d and has a forward reference to classifier '%s' which is declared later in the source file '%s' on line %d.",
                    this.getName(),
                    this.getElementContext().getStart().getLine(),
                    this.classifier.getName(),
                    this.getCompilationUnit().get().getSourceName(),
                    this.classifier.getElementContext().getStart().getLine());
            compilerErrorHolder.add(
                    "ERR_FWD_REF",
                    message,
                    this,
                    this.getElementContext().classifierReference());
        }
    }

    @Nonnull
    @Override
    public ProjectionDeclarationContext getElementContext()
    {
        return (ProjectionDeclarationContext) super.getElementContext();
    }

    @Override
    public Pair<Token, Token> getContextBefore()
    {
        return Tuples.pair(this.getElementContext().getStart(), this.getElementContext().projectionBody().getStart());
    }
}
