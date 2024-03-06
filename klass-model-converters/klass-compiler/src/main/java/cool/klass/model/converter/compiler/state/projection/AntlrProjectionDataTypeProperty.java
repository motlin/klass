package cool.klass.model.converter.compiler.state.projection;

import java.util.Objects;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import cool.klass.model.converter.compiler.CompilationUnit;
import cool.klass.model.converter.compiler.error.CompilerErrorHolder;
import cool.klass.model.converter.compiler.state.AntlrNamedElement;
import cool.klass.model.converter.compiler.state.property.AntlrDataTypeProperty;
import cool.klass.model.converter.compiler.state.property.AntlrEnumerationProperty;
import cool.klass.model.converter.compiler.state.property.AntlrPrimitiveProperty;
import cool.klass.model.meta.domain.projection.AbstractProjectionElement.ProjectionElementBuilder;
import cool.klass.model.meta.domain.projection.ProjectionDataTypePropertyImpl.ProjectionDataTypePropertyBuilder;
import cool.klass.model.meta.grammar.KlassParser.HeaderContext;
import org.antlr.v4.runtime.ParserRuleContext;

public class AntlrProjectionDataTypeProperty extends AntlrNamedElement implements AntlrProjectionElement
{
    @Nonnull
    public static final AntlrProjectionDataTypeProperty AMBIGUOUS = new AntlrProjectionDataTypeProperty(
            new ParserRuleContext(),
            null,
            true,
            new ParserRuleContext(), "ambiguous projection member",
            -1,
            AntlrProjection.AMBIGUOUS,
            new HeaderContext(null, -1),
            "ambiguous header",
            AntlrPrimitiveProperty.AMBIGUOUS);

    @Nonnull
    private final AntlrProjectionParent antlrProjectionParent;
    @Nonnull
    private final HeaderContext headerContext;
    @Nonnull
    private final String headerText;
    @Nonnull
    private final AntlrDataTypeProperty<?> dataTypeProperty;

    public AntlrProjectionDataTypeProperty(
            @Nonnull ParserRuleContext elementContext,
            @Nullable CompilationUnit compilationUnit,
            boolean inferred,
            @Nonnull ParserRuleContext nameContext,
            @Nonnull String name,
            int ordinal,
            @Nonnull AntlrProjectionParent antlrProjectionParent,
            @Nonnull HeaderContext headerContext,
            @Nonnull String headerText,
            @Nonnull AntlrDataTypeProperty<?> dataTypeProperty)
    {
        super(elementContext, compilationUnit, inferred, nameContext, name, ordinal);
        this.antlrProjectionParent = Objects.requireNonNull(antlrProjectionParent);
        this.headerText = Objects.requireNonNull(headerText);
        this.headerContext = Objects.requireNonNull(headerContext);
        this.dataTypeProperty = Objects.requireNonNull(dataTypeProperty);
    }

    @Nonnull
    public AntlrDataTypeProperty<?> getDataTypeProperty()
    {
        return this.dataTypeProperty;
    }

    @Nonnull
    @Override
    public ProjectionElementBuilder build()
    {
        return new ProjectionDataTypePropertyBuilder(
                this.elementContext,
                this.inferred,
                this.nameContext,
                this.name,
                this.ordinal,
                this.headerContext,
                this.headerText,
                this.dataTypeProperty.getPropertyBuilder());
    }

    @Nonnull
    @Override
    public AntlrProjectionParent getParent()
    {
        return this.antlrProjectionParent;
    }

    @Override
    public void reportDuplicateMemberName(@Nonnull CompilerErrorHolder compilerErrorHolder)
    {
        String message = String.format("ERR_DUP_PRJ: Duplicate member: '%s'.", this.name);

        compilerErrorHolder.add(
                message,
                this.nameContext,
                this.getParserRuleContexts().toArray(new ParserRuleContext[]{}));
    }

    @Override
    public void reportErrors(@Nonnull CompilerErrorHolder compilerErrorHolder)
    {
        if (this.dataTypeProperty == AntlrEnumerationProperty.NOT_FOUND)
        {
            String message = String.format(
                    "ERR_PRJ_DTP: Cannot find member '%s.%s'.",
                    this.antlrProjectionParent.getKlass().getName(),
                    this.name);
            compilerErrorHolder.add(
                    message,
                    this.nameContext,
                    this.getParserRuleContexts().toArray(new ParserRuleContext[]{}));
        }

        if (this.headerText.trim().isEmpty())
        {
            compilerErrorHolder.add(
                    "Empty header string.",
                    this.headerContext,
                    this.getParserRuleContexts().toArray(new ParserRuleContext[]{}));
        }
    }

    @Override
    public void reportNameErrors(@Nonnull CompilerErrorHolder compilerErrorHolder)
    {
        // Intentionally blank. Reference to a named element that gets its name checked.
    }
}
