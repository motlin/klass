package cool.klass.model.converter.compiler.state.projection;

import java.util.Objects;
import java.util.Optional;
import java.util.regex.Pattern;

import javax.annotation.Nonnull;

import cool.klass.model.converter.compiler.CompilationUnit;
import cool.klass.model.converter.compiler.error.CompilerErrorState;
import cool.klass.model.converter.compiler.state.AntlrClass;
import cool.klass.model.converter.compiler.state.AntlrClassifier;
import cool.klass.model.converter.compiler.state.AntlrIdentifierElement;
import cool.klass.model.converter.compiler.state.property.AntlrDataTypeProperty;
import cool.klass.model.converter.compiler.state.property.AntlrEnumerationProperty;
import cool.klass.model.converter.compiler.state.property.AntlrPrimitiveProperty;
import cool.klass.model.converter.compiler.state.property.AntlrReferenceProperty;
import cool.klass.model.meta.domain.projection.ProjectionDataTypePropertyImpl.ProjectionDataTypePropertyBuilder;
import cool.klass.model.meta.grammar.KlassParser.HeaderContext;
import cool.klass.model.meta.grammar.KlassParser.IdentifierContext;
import cool.klass.model.meta.grammar.KlassParser.ProjectionPrimitiveMemberContext;
import org.antlr.v4.runtime.Token;
import org.eclipse.collections.api.tuple.Pair;

public class AntlrProjectionDataTypeProperty
        extends AntlrIdentifierElement
        implements AntlrProjectionChild
{
    @Nonnull
    public static final AntlrProjectionDataTypeProperty AMBIGUOUS = new AntlrProjectionDataTypeProperty(
            new ProjectionPrimitiveMemberContext(null, -1),
            Optional.empty(),
            -1,
            new IdentifierContext(null, -1),
            new HeaderContext(null, -1),
            "ambiguous header",
            AntlrProjection.AMBIGUOUS,
            AntlrPrimitiveProperty.AMBIGUOUS);

    @Nonnull
    private final HeaderContext            headerContext;
    @Nonnull
    private final String                   headerText;
    @Nonnull
    private final AntlrProjectionParent    antlrProjectionParent;
    @Nonnull
    private final AntlrDataTypeProperty<?> dataTypeProperty;

    private ProjectionDataTypePropertyBuilder projectionDataTypePropertyBuilder;

    public AntlrProjectionDataTypeProperty(
            @Nonnull ProjectionPrimitiveMemberContext elementContext,
            @Nonnull Optional<CompilationUnit> compilationUnit,
            int ordinal,
            @Nonnull IdentifierContext nameContext,
            @Nonnull HeaderContext headerContext,
            @Nonnull String headerText,
            @Nonnull AntlrProjectionParent antlrProjectionParent,
            @Nonnull AntlrDataTypeProperty<?> dataTypeProperty)
    {
        super(elementContext, compilationUnit, ordinal, nameContext);
        this.antlrProjectionParent = Objects.requireNonNull(antlrProjectionParent);
        this.headerText            = Objects.requireNonNull(headerText);
        this.headerContext         = Objects.requireNonNull(headerContext);
        this.dataTypeProperty      = Objects.requireNonNull(dataTypeProperty);
    }

    @Nonnull
    public AntlrDataTypeProperty<?> getDataTypeProperty()
    {
        return this.dataTypeProperty;
    }

    @Nonnull
    @Override
    public ProjectionDataTypePropertyBuilder build()
    {
        if (this.projectionDataTypePropertyBuilder != null)
        {
            throw new IllegalStateException();
        }
        this.projectionDataTypePropertyBuilder = new ProjectionDataTypePropertyBuilder(
                (ProjectionPrimitiveMemberContext) this.elementContext,
                this.getMacroElementBuilder(),
                this.getSourceCodeBuilder(),
                this.ordinal,
                this.getNameContext(),
                this.headerContext,
                this.headerText,
                this.antlrProjectionParent.getElementBuilder(),
                this.dataTypeProperty.getElementBuilder());
        return this.projectionDataTypePropertyBuilder;
    }

    @Override
    public void build2()
    {
        // Deliberately empty
    }

    @Override
    @Nonnull
    public ProjectionDataTypePropertyBuilder getElementBuilder()
    {
        return Objects.requireNonNull(this.projectionDataTypePropertyBuilder);
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
        if (this.headerText.trim().isEmpty())
        {
            compilerErrorHolder.add("ERR_PRJ_HDR", "Empty header string.", this, this.headerContext);
        }

        AntlrClassifier parentClassifier = this.antlrProjectionParent.getClassifier();
        if (parentClassifier == AntlrClass.NOT_FOUND
                || parentClassifier == AntlrClass.AMBIGUOUS
                || parentClassifier == AntlrClassifier.AMBIGUOUS
                || parentClassifier == AntlrClassifier.NOT_FOUND)
        {
            return;
        }

        if (this.dataTypeProperty == AntlrEnumerationProperty.NOT_FOUND)
        {
            AntlrReferenceProperty<?> referenceProperty = parentClassifier.getReferencePropertyByName(this.getName());
            if (referenceProperty == AntlrReferenceProperty.NOT_FOUND)
            {
                String message = String.format("Cannot find member '%s.%s'.", parentClassifier.getName(), this.getName());
                compilerErrorHolder.add("ERR_PRJ_DTP", message, this);
            }
            else
            {
                String message = "Leaf projection nodes require a data type property, but found a reference property '%s.%s' with type '%s'".formatted(
                        parentClassifier.getName(),
                        referenceProperty.getName(),
                        referenceProperty.getTypeName());
                compilerErrorHolder.add("ERR_PRJ_TYP", message, this);
            }
        }

        if (this.dataTypeProperty.isPrivate())
        {
            String message = String.format(
                    "Projection includes private property '%s.%s'.",
                    this.dataTypeProperty.getOwningClassifierState().getName(),
                    this.getName());
            compilerErrorHolder.add("ERR_PRJ_PRV", message, this);
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
    public boolean isContext()
    {
        return true;
    }

    @Override
    public Pair<Token, Token> getContextBefore()
    {
        return this.getEntireContext();
    }
}
