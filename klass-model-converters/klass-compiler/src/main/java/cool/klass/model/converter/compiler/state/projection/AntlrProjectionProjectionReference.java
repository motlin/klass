package cool.klass.model.converter.compiler.state.projection;

import java.util.Objects;
import java.util.Optional;
import java.util.regex.Pattern;

import javax.annotation.Nonnull;

import cool.klass.model.converter.compiler.CompilationUnit;
import cool.klass.model.converter.compiler.error.CompilerErrorState;
import cool.klass.model.converter.compiler.state.AntlrClass;
import cool.klass.model.converter.compiler.state.AntlrNamedElement;
import cool.klass.model.converter.compiler.state.property.AntlrAssociationEnd;
import cool.klass.model.meta.domain.projection.ProjectionImpl.ProjectionBuilder;
import cool.klass.model.meta.domain.projection.ProjectionProjectionReferenceImpl.ProjectionProjectionReferenceBuilder;
import cool.klass.model.meta.grammar.KlassParser.ProjectionProjectionReferenceContext;
import org.antlr.v4.runtime.ParserRuleContext;
import org.eclipse.collections.api.list.MutableList;

public class AntlrProjectionProjectionReference extends AntlrNamedElement implements AntlrProjectionChild
{
    @Nonnull
    public static final AntlrProjectionProjectionReference AMBIGUOUS = new AntlrProjectionProjectionReference(
            new ProjectionProjectionReferenceContext(null, -1),
            Optional.empty(),
            new ParserRuleContext(),
            "ambiguous projection",
            -1,
            AntlrClass.AMBIGUOUS,
            AntlrProjection.AMBIGUOUS,
            AntlrAssociationEnd.AMBIGUOUS,
            AntlrProjection.AMBIGUOUS);

    @Nonnull
    public static final AntlrProjectionProjectionReference NOT_FOUND = new AntlrProjectionProjectionReference(
            new ProjectionProjectionReferenceContext(null, -1),
            Optional.empty(),
            new ParserRuleContext(),
            "not found projection",
            -1,
            AntlrClass.NOT_FOUND,
            AntlrProjection.NOT_FOUND,
            AntlrAssociationEnd.NOT_FOUND,
            AntlrProjection.AMBIGUOUS);

    @Nonnull
    private final AntlrClass            klass;
    @Nonnull
    private final AntlrProjectionParent antlrProjectionParent;
    @Nonnull
    private final AntlrAssociationEnd   associationEnd;
    @Nonnull
    private final AntlrProjection       referencedProjectionState;

    private ProjectionProjectionReferenceBuilder projectionProjectionReferenceBuilder;

    public AntlrProjectionProjectionReference(
            @Nonnull ProjectionProjectionReferenceContext elementContext,
            @Nonnull Optional<CompilationUnit> compilationUnit,
            @Nonnull ParserRuleContext nameContext,
            @Nonnull String name,
            int ordinal,
            @Nonnull AntlrClass klass,
            @Nonnull AntlrProjectionParent antlrProjectionParent,
            @Nonnull AntlrAssociationEnd associationEnd,
            @Nonnull AntlrProjection referencedProjectionState)
    {
        super(elementContext, compilationUnit, nameContext, name, ordinal);
        this.klass                     = Objects.requireNonNull(klass);
        this.antlrProjectionParent     = Objects.requireNonNull(antlrProjectionParent);
        this.associationEnd            = Objects.requireNonNull(associationEnd);
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
                this.nameContext,
                this.name,
                this.ordinal,
                this.antlrProjectionParent.getElementBuilder(),
                this.associationEnd.getElementBuilder());

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

        if (this.klass != this.referencedProjectionState.getKlass()
                && !this.klass.isSubTypeOf(this.referencedProjectionState.getKlass()))
        {
            String message = String.format(
                    "Type mismatch: '%s' has type '%s' but '%s' has type '%s'.",
                    this.name,
                    this.klass.getName(),
                    this.referencedProjectionState.getName(),
                    this.referencedProjectionState.getKlass().getName());
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
